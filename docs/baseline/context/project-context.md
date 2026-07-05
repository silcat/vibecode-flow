# 项目上下文

## 项目标识

vibecode-flow-init

## 文档新鲜度

`fresh`

值域和维护规则见 `docs/baseline/context/ai-autonomy-policy.md`。

## 活跃工作

完整状态见 `docs/work/registry.md`。AI 自治策略见 `docs/baseline/context/ai-autonomy-policy.md`。

## 当前技术基线

- 后端：Spring Boot 3.2 + Spring Cloud Alibaba 2023
- 注册/配置：Nacos 2.3
- 网关：Spring Cloud Gateway
- 数据库：MySQL 8.0（每服务独立库）
- 缓存：Redis 7
- 消息队列：Kafka 3.6
- 服务调用：Spring Cloud OpenFeign（同步）+ Kafka（异步事件）
- 认证：Spring Security + JWT（在 Gateway 层统一校验）
- 熔断：Resilience4j CircuitBreaker
- 可观测性：Micrometer + Spring Boot Actuator（Prometheus 指标暴露）、SLF4J MDC（traceId 全链路传递）

| 服务 | 端口 |
|------|------|
| gateway | 8080 |
| service-a | 8081 |
| service-b | 8082 |

## 验证命令

| 用途 | 命令 |
|------|------|
| 安装依赖（平台） | `mvn clean install -DskipTests` |
| 编译检查（平台） | `mvn compile` |
| 单元测试（平台） | `mvn test` |
| 集成测试（平台） | `mvn verify -P integration` |
| 本地运行（平台） | `docker-compose up -d`（Nacos / MySQL / Redis / Kafka），然后按服务逐个 `mvn spring-boot:run` |

## 当前启用的可选层

- [x] `docs/skills/`
- [x] `docs/retro/`
- [x] `docs/baseline/architecture/`
- [x] `docs/baseline/standards/`

## chat-agent 技术基线

独立项目，后续接入微服务体系。

- 后端：Java（Spring Boot 3.2）
- 前端：Vue 3
- LLM：Spring AI + DeepSeek API（spring-ai-openai，统一 ChatClient 抽象层，后续可插拔）
- 向量存储：HNSW（Apache Lucene 内嵌索引，独立阶段零外部依赖）
- 向量存储（微服务阶段）：Milvus
- MCP：Spring AI MCP（spring-ai-mcp，工具自动注册到 ChatClient）
- 文档解析：Apache Tika
- 嵌入模型：DeepSeek Embedding API

| 模块 | 端口 |
|------|------|
| chat-agent 后端 | 8090 |

### 验证命令（chat-agent）

| 用途 | 命令 |
|------|------|
| 后端编译 | `mvn compile -f chat-agent/pom.xml` |
| 后端测试 | `mvn test -f chat-agent/pom.xml` |
| 后端运行 | `mvn spring-boot:run -f chat-agent/pom.xml` |
| 前端运行 | `cd chat-agent-ui && npm run dev` |
