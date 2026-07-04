# 可观测性规范

> 触发场景：配置日志、添加链路追踪、暴露健康检查或指标端点

## 日志

### 格式

生产环境使用 JSON 结构化日志：

```json
{
  "timestamp": "2026-06-15T10:30:00.123Z",
  "level": "INFO",
  "service": "service-a",
  "traceId": "abc123def456",
  "spanId": "span-001",
  "message": "order created: 42"
}
```

必含字段：`timestamp`、`level`、`service`、`traceId`、`message`

### 级别使用

| 级别 | 场景 |
|------|------|
| ERROR | 需要人工介入的异常（含堆栈） |
| WARN | 可自动恢复的异常、降级触发、接近阈值 |
| INFO | 关键业务操作（创建/更新/删除）、服务启动、配置加载 |
| DEBUG | 开发调试，生产环境默认关闭 |

### 禁止

- 循环内打日志
- 日志含敏感信息（密码、token、身份证号、手机号）
- 使用 `System.out.println` 或 `e.printStackTrace()`

## 链路追踪

### traceId 传递

```
外部请求 -> Gateway
  -> 生成 traceId（如不存在）
  -> MDC.put("traceId", traceId)
  -> Feign 拦截器注入 X-Trace-Id Header
    -> 下游服务
      -> Filter 提取 X-Trace-Id
      -> MDC.put("traceId", traceId)
      -> 后续 Feign 调用继续透传
```

- Gateway 层：从请求头读取 `X-Trace-Id`，不存在则生成 UUID
- Feign 透传：通过拦截器将 traceId 注入下游请求头
- 下游 Filter：提取 Header 中的 `X-Trace-Id` 写入 MDC



## 健康检查

### 端点

| 端点 | 用途 | 调用方 |
|------|------|--------|
| `/actuator/health` | 存活探针 | K8s liveness probe |
| `/actuator/health/readiness` | 就绪探针（含 DB/Kafka 连接检查） | K8s readiness probe |

- readiness 检查项：数据库连接、Kafka 连接、Redis 连接
- 探针间隔：liveness 10s、readiness 5s

## 指标

### 必暴露指标

| 指标 | 类型 | 用途 |
|------|------|------|
| `http_server_requests_seconds` | Histogram | HTTP 请求耗时分布 |
| `feign_client_requests_seconds` | Histogram | Feign 调用耗时和成功率 |
| `kafka_consumer_lag` | Gauge | Kafka 消费延迟 |
| `jvm_memory_used_bytes` | Gauge | JVM 内存使用 |
| `db_connection_pool_active` | Gauge | 数据库连接池使用数 |

### 端点

- Prometheus 抓取：`/actuator/prometheus`
- 抓取间隔：15s

### 告警阈值（建议）

| 条件 | 级别 |
|------|------|
| HTTP 5xx 率 > 1% | 严重 |
| Feign 调用成功率 < 99% | 严重 |
| Kafka 消费延迟 > 1000 | 警告 |
| JVM 堆内存 > 85% | 警告 |