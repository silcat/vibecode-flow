# 后端编码规范

> 触发场景：编写 Java/Spring 后端业务代码

## 分层架构

```
Controller → Service → Mapper → DB
   ↕           ↕
  DTO ←──── Entity
```

- Controller：参数校验 + 调用 Service，不写业务逻辑
- Service：业务逻辑 + 事务 + 服务间调用
- Mapper：只定义数据操作，不写业务判断
- 禁止：Controller 直接调 Mapper、Service 之间循环依赖

## 命名

| 类型 | 格式 | 示例 |
|------|------|------|
| 类 | PascalCase | `UserService` |
| 方法 | camelCase | `findByEmail` |
| 常量 | UPPER_SNAKE | `MAX_RETRY_COUNT` |
| 包 | 全小写 | `com.petadopt.user.controller` |
| DTO | 后缀 DTO | `UserRegisterDTO` |
| 测试类 | 类名 + Test | `UserServiceTest` |

## 依赖注入

- 使用 `@RequiredArgsConstructor` + `private final`
- 禁止 `@Autowired` 字段注入

## 事务

- 写操作必须 `@Transactional(rollbackFor = Exception.class)`
- 只读操作加 `@Transactional(readOnly = true)`
- 事务边界在 Service 层，不在 Controller

## 异常处理

- 业务异常：`throw new BusinessException(ErrorCode.USER_DUPLICATE)`
- 禁止：`e.printStackTrace()`、捕获后吞掉
- 全局处理：`@RestControllerAdvice` 统一转 ApiResponse

## 日志

- Service 层关键操作打 INFO：`log.info("user registered: {}", userId)`
- 异常打 ERROR 含堆栈：`log.error("registration failed", e)`
- 禁止：循环内打日志、日志含敏感信息（密码、token）
