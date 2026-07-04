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

## AI 阻塞条件

以下情况 AI 必须停止并等待人类输入：

- 涉及支付、退款、资金流转的代码变更
- 修改 Gateway 认证/鉴权过滤器
- 修改 Kafka topic 定义或消费者组
- 修改数据库 schema（DDL）
- 任何变更触及两个以上微服务的公共契约
- 修改 API 版本化策略或废弃现有 API 版本

## AI 自治策略

- **自治级别**：`实施`——AI 可直接编写代码、运行验证，无需逐条确认
- **保护区**：支付/资金相关代码、Gateway 过滤器、DDL、跨服务契约变更、API 版本废弃 → 必须先写入 `discussion.md` 等待确认
- **审查触发**：修改超过 5 个文件、涉及保护区、或修改 Feign 接口时触发独立审计


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
