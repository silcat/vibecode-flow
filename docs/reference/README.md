# 参考资料索引

## 用途

此目录存放支持实施和审查的稳定查阅材料，但其本身不定义强制性项目上下文。

如果某规则必须被每次 AI 会话应用，请将其放入 `docs/baseline/context/` 或 `AGENTS.md`，而非仅此处。

## spring/ — Spring 全家桶编码规范

| 文件 | 覆盖范围 |
|------|---------|
| `api-standards.md` | RESTful API 设计、统一响应格式、Controller 规范、DTO/VO、分页策略 |
| `coding-conventions.md` | 分层架构、依赖注入、事务管理、Redis 规范、命名规范、Lombok |
| `db-design.md` | 表设计、Entity/Mapper 规范、SQL 安全、批量操作、索引 |
| `exception-handling.md` | 异常分类、错误码设计、全局异常处理器、Service 层策略 |
| `logging-monitoring.md` | 日志级别、结构化日志、脱敏、链路追踪、监控指标 |
| `performance.md` | N+1 禁止、深分页禁止、缓存策略、查询优化、异步处理 |
| `security-standards.md` | SQL 注入防护、XSS、密码安全、JWT/API Key、数据脱敏、输入校验 |

## guides/ — 通用实施与维护指南

- `maintenance-checklist.md` — 文档和验证同步检查清单
- `implementation-guide.md` — 日常编码和审查约定
- `document-naming-and-timeliness.md` — 何时使用固定名称 vs 带日期文件名
- `playwright-e2e-guide.md` — Playwright E2E 测试指南
