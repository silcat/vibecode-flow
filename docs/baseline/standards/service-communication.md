# 服务间通信规范

> 触发场景：编写 Feign 调用、Kafka 生产者/消费者、配置熔断降级

## Feign 调用

### 超时配置

```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
          default:
            connectTimeout: 3000
            readTimeout: 5000
```

- connectTimeout：建立连接超时，默认 3s
- readTimeout：等待响应超时，默认 5s
- 长时间操作（如报表导出）按接口单独配置，不做全局放大

### 重试策略

- 仅对幂等操作（GET、PUT、DELETE）启用重试
- 非幂等操作（POST）**禁止自动重试**，防止重复创建
- 重试间隔：指数退避（1s → 2s → 4s），最多 3 次

### 熔断降级

- 每个 Feign 接口必须配置 fallback 或 fallbackFactory
- fallbackFactory 中记录 `log.error("circuit open for service-b", exception)`
- 降级策略按业务场景选择：返回缓存数据 / 返回空列表 / 抛业务异常让上游处理

### 调用链约束

- 同步调用链不超过 3 层：`gateway → service-a → service-b` 是极限
- 禁止 Feign 调用形成循环依赖（A 调 B 时 B 不能直接或间接调 A 的同步接口）

## Kafka 事件

### 消息格式

所有 Kafka 消息使用统一 envelope：

```json
{
  "eventId": "uuid",
  "eventType": "service-a.entity-created",
  "timestamp": "2026-06-15T10:30:00Z",
  "payload": { }
}
```

- `eventId`：消息唯一标识，用于幂等和排重
- `eventType`：`{服务名}.{实体}-{动作}`，全部小写
- `timestamp`：ISO 8601 UTC
- `payload`：业务数据，由各服务自行定义结构

### Topic 规范

- 命名：`{service}.{event}`（如 `service-a.entity-created`）
- 分区数：默认 3，按业务量调整
- 保留时间：默认 7 天

### 消费者组

- 命名：`{consumer-service}-group`
- 同一 consumer group 内每条消息只被一个实例消费
- 不同 consumer group 各自独立消费全量消息

### 幂等性

- 消费者必须按 `eventId` 做幂等：消费前检查 `eventId` 是否已处理，已处理则跳过
- 生产者发送前生成 UUID 作为 `eventId`

### 死信队列

- 消费失败重试 3 次（间隔递增）→ 进入死信队列
- 死信队列命名：`{topic}.dlq`
- 死信消息触发告警，需人工介入排查

## 禁止事项

| 禁止 | 原因 |
|------|------|
| POST 操作自动重试 | 可能重复创建资源 |
| Feign 接口无 fallback | 下游故障会级联扩散 |
| 同步调用链超过 3 层 | 延迟放大、故障半径扩大 |
| 消息体直接透传 entity | 耦合数据库 schema，应使用独立 DTO |
| 消费者不做幂等 | 重试导致重复处理 |
