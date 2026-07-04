# Spring Boot 编码规范

## 用途

编写 Java 代码、设计类结构、依赖注入、事务管理、Redis 使用时的强制性规范。

## 分层架构

```
Controller（接口层）
    ↓ 调用
BO / BizManageService（编排层）
    ↓ 调用
Repository / Gateway（数据存取 / 外部依赖）
    ↓ 调用
Mapper / Feign / SDK（数据映射 / 外部通信）
    ↓
Database / 外部系统
```

**关键规则**：

- Controller 只接收参数、调用 BO、返回响应
- BO 负责编排业务流程、组合多个 Repository 和 Gateway、事务管理。**BO 之间禁止互调**
- Repository 只做单表 CRUD，继承 MyBatis Plus `ServiceImpl`
- 外部调用走 `gateway/`：接口放根目录，Feign 实现放 `gateway/feign/`，SDK 实现放 `gateway/sdk/`
- 纯计算抽到 `domain/`，不查库不写库
- 依赖只能向下，不能反向或跳层

## 信号驱动演进

| 信号 | 动作 |
|------|------|
| 外部调用重复 | 按 `docs/baseline/architecture/module-internals.md` 抽 gateway |
| 纯计算重复 | 抽 domain/ |
| 写库+消息+日志重复 | 抽子流程 bo |
| bo 循环依赖 | 共用部分下沉 |

## 铁规

**BO 之间禁止互调。** 依赖方向必须全部向下。
Spring 构造器注入的循环依赖检测是最终防线。

## 依赖注入

统一使用 `@Resource` 注入，不用 `@Autowired` 和 `@RequiredArgsConstructor`：

```java
@Service
@Slf4j
public class OrderBizManageService {

    @Resource
    private OrderRepository orderRepo;

    @Resource
    private PaymentGateway paymentGateway;
}
```

## 事务管理

涉及多表写操作必须加事务：

```java
@Transactional(rollbackFor = Exception.class)
public void createOrder(CreateOrderDTO dto) {
    orderRepo.save(order);
    orderItemRepo.saveBatch(items);
    stockRepo.deduct(dto.getItems());
}
```

## Redis 使用规范

- Key 命名格式：`{系统前缀}:{业务模块}:{类型}:{标识}`
- 示例：`systemCode:user:info:123`、`systemCode:lock:order:ORD001`
- 所有缓存必须设置过期时间：`redisTemplate.opsForValue().set(key, value, 1, TimeUnit.HOURS);`

## 命名规范

- 类名：`UpperCamelCase`
- 方法名、参数名、变量名：`lowerCamelCase`
- 常量：`UPPER_SNAKE_CASE`
- 包名全小写

## Lombok

- 实体类使用 `@Data`
- Service 层使用 `@Slf4j` 生成日志对象
- DTO 使用 `@Data`
- 不用 Lombok 的 `@Builder`，统一使用构造器或 setter
