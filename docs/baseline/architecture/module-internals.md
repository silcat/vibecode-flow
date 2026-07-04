 # 分层架构规范
 
 ## 用途
 
 决定每个类应该放在哪一层、能依赖谁、不能依赖谁。
 新增业务类时的分层决策依据。
 
 ## 触发场景
 
 - 新增业务模块
 - 新增外部服务调用
 - 重复代码出现 >=2 次需要抽取
 - 编排层之间出现循环依赖
 
 ## 核心原则
 
 **依赖方向 > 层级命名。**
 
 所有带 Spring 组件注解的类，其注入箭头必须全部指向
 "更靠近基础设施"的方向。依赖图必须是 DAG，不允许回路。
 
 Spring 构造器注入的循环依赖检测是最终防线。
 
 ---
 
 ## 目录结构
 
 ```
 org.ytf.<pkg>/
 ├── controller/          ← HTTP 入口，参数校验 + 调 bo
 ├── bo/                  ← 编排层 BizManageService
 ├── repository/          ← 数据存取接口
 │   └── impl/            ← 数据存取实现
 ├── domain/              ← 纯计算
 ├── gateway/             ← 外部依赖
 │   ├── XxxGateway.java  ← 接口
 │   ├── feign/           ← 本项目 Feign 调用（实现 Gateway）
 │   └── sdk/             ← 第三方 SDK（实现 Gateway）
 ├── mapper/              ← MyBatis 映射
 ├── entity/              ← 数据对象
 ├── enums/
 ├── converter/
 └── utils/
 ```
 
 ---
 
 ## 依赖规则一览
 
 | 层 | 目录 | 可以依赖 | 禁止依赖 |
 |----|------|---------|---------|
 | 接口层 | `controller/` | bo、repository 接口、gateway 接口、domain | mapper、entity 直返 |
 | 编排层 | `bo/` | repository 接口、gateway 接口、domain | **其他 bo**、controller、mapper、gateway/feign、gateway/sdk |
 | 数据存取 | `repository/impl/` | mapper、entity | bo、controller、gateway |
 | 纯计算 | `domain/` | entity、java.* | mapper、gateway、bo、任何带 Spring 注解的类 |
 | 外部接口 | `gateway/`（根） | entity | 任何实现类 |
 | 外部实现 | `gateway/feign/` | gateway 接口、java.* | bo、repository、domain |
 | 外部实现 | `gateway/sdk/` | gateway 接口、第三方 SDK | bo、repository、domain |
 | 数据映射 | `mapper/` | entity | bo、repository |
 
 ---
 
 ## 各层详解
 
 ### controller/ — 接口层
 
 **职责**：HTTP 协议适配。参数校验 + 调用 bo + 返回响应。
 **禁止**：写业务逻辑、直接调 mapper、直接返回 entity。
 
 ### bo/ — 编排层
 
 **职责**：一个完整的业务用例流程。掌管 `@Transactional` 边界。
 **命名**：`{业务名}BizManageService`
 **接口**：不拆接口，直接实现类。只被 controller 调用。
 
 ```java
 // ✅ 正确
 import org.ytf.<pkg>.repository.AaaRepository;
 import org.ytf.<pkg>.gateway.BbbGateway;
 import org.ytf.<pkg>.domain.CccCalcService;
 
 @Service
 public class XxxBizManageService {
     private final AaaRepository aaaRepo;
     private final BbbGateway bbbGateway;
     private final CccCalcService cccCalc;
 }
 ```
 
 ```java
 // ❌ 禁止
 import org.ytf.<pkg>.bo.YyyBizManageService;           // 调别的 bo
 import org.ytf.<pkg>.mapper.XxxMapper;                  // 直接调 mapper
 import org.ytf.<pkg>.gateway.feign.XxxFeignClient;     // 直接调 feign
 ```
 
 ### repository/ — 数据存取层
 
 **职责**：单表 CRUD。不编排多表操作，不掌管跨表事务。
 **接口**：放 `repository/`，实现放 `repository/impl/`。
 **命名**：`{表名}Repository` / `{表名}RepositoryImpl`
 
 ```java
 // repository/AaaRepository.java
 public interface AaaRepository {
     Aaa getById(Long id);
     List<Aaa> listByStatus(String status);
     void save(Aaa entity);
 }
 
 // repository/impl/AaaRepositoryImpl.java
 @Repository
 public class AaaRepositoryImpl extends ServiceImpl<AaaMapper, Aaa>
         implements AaaRepository {
 
     @Override
     public Aaa getById(Long id) {
         return getById(id);
     }
 }
 ```
 
 可以加简单判断方法（`isVIP()`、`getActive()`），但不能调其他
 Repository 或 gateway。
 
 **与 domain/ 的边界**：有没有 `mapper` / `getById` / `selectList`。
 
 | 有数据库调用 | -> repository |
 | 纯参数进结果出 | -> domain |
 
 ### gateway/ — 外部依赖层
 
 **职责**：对外部系统的全部通信。接口放根目录，实现分 `feign/` 和 `sdk/`。
 **命名**：接口 `{外部能力}Gateway`，Feign 实现 `{服务名}Client`，SDK 实现 `{渠道名}Client`
 
 ```
 gateway/
 ├── PaymentGateway.java           ← 接口："我需要付钱"
 ├── UserGateway.java              ← 接口："我需要查用户"
 ├── feign/                        ← 本项目服务间调用
 │   └── UserServiceClient.java    ← implements UserGateway
 └── sdk/                          ← 第三方 SDK
     ├── AlipayTradeClient.java    ← implements PaymentGateway
     └── WechatPayClient.java      ← implements PaymentGateway
 ```
 
 ```java
 // gateway/PaymentGateway.java
 package org.ytf.<pkg>.gateway;
 
 public interface PaymentGateway {
     PaymentResult charge(Long bizId, BigDecimal amount);
 }
 
 // gateway/sdk/AlipayTradeClient.java
 package org.ytf.<pkg>.gateway.sdk;
 
 @Component
 public class AlipayTradeClient implements PaymentGateway {
     private final AlipayTradeApi api;
 
     @Override
     public PaymentResult charge(Long bizId, BigDecimal amount) {
         AlipayTradePayResponse resp = api.execute(buildRequest(bizId, amount));
         if (!resp.isSuccess()) throw new PaymentException(resp.getSubMsg());
         return new PaymentResult(resp.getTradeNo(), PaymentStatus.SUCCESS);
     }
 }
 
 // gateway/feign/UserServiceClient.java
 package org.ytf.<pkg>.gateway.feign;
 
 @Component
 public class UserServiceClient implements UserGateway {
     private final UserServiceApi api;
 
     @Override
     public User findById(Long userId) {
         UserDTO dto = api.getUser(userId);
         return new User(dto.getId(), dto.getNickName());
     }
 }
 ```
 
 **触发条件**：同一个外部调用在 >=2 个 bo 中重复出现。
 
 ### domain/ — 纯计算层
 
 **职责**：无副作用的业务规则计算。输入 -> 计算 -> 输出。
 **特征**：没有 `@Transactional`、不查库、不写库、不发消息、不调外部。
 **触发条件**：同一个纯计算规则在 >=2 个 bo 中重复出现。
 
 ```java
 package org.ytf.<pkg>.domain;
 
 @Component
 public class XxxCalcService {
     public BigDecimal calculate(List<XxxItem> items) {
         return items.stream()
             .map(i -> i.price().multiply(BigDecimal.valueOf(i.qty())))
             .reduce(BigDecimal.ZERO, BigDecimal::add);
     }
 }
 ```
 
 ---
 
 ## 信号驱动的演进路径
 
 不提前建空层。第 2 次重复出现时立刻抽，不等到第 3 次。
 
 | 信号 | 动作 | 建什么 |
 |------|------|--------|
 | 项目启动 | bo + repository + mapper | 必建 |
 | 同一个外部调用的翻译逻辑出现第 2 次 | 抽 gateway 接口 + 实现 | 1 接口 + 1 实现类 |
 | 同一个"写库+发消息+记日志"组合出现第 2 次 | 抽子流程 bo | 1 个 bo |
 | 同一个纯计算规则出现第 2 次 | 抽 domain | 1 个类 |
 | bo 之间出现循环依赖 | 被调方共用部分下沉 | gateway / domain / 子流程 bo |
 
 ---
 
 ## 处理共享逻辑
 
 | 共享的是什么 | 放哪 | 能被多个 bo 调吗 |
 |-------------|------|:--:|
 | 单表查询 + 简单判断 | repository 加方法 | YES |
 | 纯计算规则 | domain/ | YES |
 | 外部调用 + 翻译 | gateway/ 接口 + 实现 | YES |
 | 写库+发消息+记日志（固定组合） | bo/（子流程 bo） | YES |
 | 完整业务流程（查+算+调外部+写+通知） | 它自己就是 bo | NO |
 
 ## 处理同步互调
 
 两个 bo 出现同步双向依赖，三选一：
 
 | 解法 | 条件 |
 |------|------|
 | 合回去 | 两个类逻辑强绑定，拆开是为了拆而拆 |
 | 被调方自己查数据 | 被调方需要的数据在 repository 可查，不需要回调方提供 |
 | 数据生产下沉 | 数据是调用方内部计算的 -> 抽到 domain/，两边都调它 |
