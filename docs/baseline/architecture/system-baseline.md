# System Baseline

## 用途

记录 `vibecode-flow-init` 的运行时形状和技术基线。

此文件描述系统"长什么样"。

---

## 架构模式

**微服务**（本项目含 N 个独立部署服务）


## 技术栈

| 层 | 选型 | 版本 |
|----|------|------|
| 后端框架 | Spring Boot | 3.2 |
| 微服务框架 | Spring Cloud Alibaba | 2023 |
| 注册/配置中心 | Nacos | 2.3 |
| 网关 | Spring Cloud Gateway | — |
| 数据库 | MySQL | 8.0 |
| 缓存 | Redis | 7 |
| 消息队列 | Kafka | 3.6 |
| 服务调用（同步） | Spring Cloud OpenFeign | — |
| 认证 | Spring Security + JWT | — |
| 熔断 | Resilience4j CircuitBreaker | — |
| 可观测性 | Micrometer + Actuator | — |

## 外部平台

| 平台 | 用途 | 调研 | 技能 |
|------|------|------|------|
| Nacos | 服务注册、配置管理 | — | — |
| MySQL | 持久化存储 | — | — |
| Redis | 缓存 | — | — |
| Kafka | 异步消息 | — | — |

> 引入新外部平台时，先经阶段 ④ 调研产出 research.md，批准后在此注册。如何使用该平台见对应 tech 技能。

## 验证命令

| 用途 | 命令 |
|------|------|
| 安装依赖 | `mvn clean install -DskipTests` |
| 编译检查 | `mvn compile` |
| 单元测试 | `mvn test` |
| 集成测试 | `mvn verify -P integration` |
| 本地运行 | `docker-compose up -d`（Nacos / MySQL / Redis / Kafka），然后按服务逐个 `mvn spring-boot:run` |

## 更新触发条件

以下情况才需要改本文件：

- 技术栈版本升级
- 新增/移除外部平台依赖
- 验证命令变更
