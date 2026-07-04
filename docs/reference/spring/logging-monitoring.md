# Spring Boot 日志与监控规范

## 用途

日志输出、日志级别配置、链路追踪、Metrics 埋点时的强制性规范。

## 日志级别

| 级别  | 使用场景                                    |
| ----- | ------------------------------------------- |
| ERROR | 系统错误，需要立即关注（数据库连接失败等）    |
| WARN  | 可预期的异常情况（认证失败、资源不存在等）    |
| INFO  | 关键业务节点（服务启动、订单创建、用户登录等）|
| DEBUG | 开发调试信息，生产环境关闭（方法入参出参等） |

## 结构化日志

- 使用 `log.info("订单创建成功, orderId={}, userId={}", orderId, userId)` 格式
- 不用字符串拼接：`log.info("订单创建成功, orderId=" + orderId)`
- 记录关键业务对象时，包含唯一标识符

## 日志脱敏

- 密码、token、API Key、身份证号、手机号：禁止出现在日志中
- 手机号脱敏格式：`138****1234`
- 日志中打印 DTO 前，确认不含敏感字段

## 日志文件配置

```yaml
logging:
  level:
    root: INFO
    com.yourcompany: DEBUG  # 项目包路径
  file:
    name: logs/application.log
    max-size: 100MB
    max-history: 30
```

## 链路追踪

- 每个请求入口生成 traceId
- 跨服务调用通过 HTTP Header 传递 traceId
- 日志中必须包含 traceId

## 监控指标

- 接口响应时间（P50/P95/P99）
- 接口错误率
- 数据库连接池使用率
- 缓存命中率
