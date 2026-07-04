# 项目约定

## 用途

AI 代理应默认应用的项目级规则。具体规范按触发场景路由到 `docs/baseline/standards/`。

## 规范路由

| 规范 | 文件 | 触发场景 |
|------|------|---------|
| 后端编码 | `docs/baseline/standards/backend.md` | 编写 Java/Spring 后端业务代码 |
| API 设计 | `docs/baseline/standards/api.md` | 新增/修改 Controller、参数校验、分页 |
| API 版本化 | `docs/baseline/standards/api-versioning.md` | 新增 API 版本、废弃旧版本、版本间兼容性判断 |
| 数据库 | `docs/baseline/standards/database.md` | 新增表/字段/索引、编写迁移脚本 |
| 认证 | `docs/baseline/standards/auth.md` | JWT 签发/校验、Gateway 过滤器、服务间鉴权 |
| 服务间通信 | `docs/baseline/standards/service-communication.md` | Feign 调用、Kafka 事件、熔断降级 |
| 可观测性 | `docs/baseline/standards/observability.md` | 日志格式、traceId 传递、健康检查、指标暴露 |
| 测试 | `docs/baseline/standards/testing.md` | 编写单元测试、集成测试、Mock |
| 模块结构 | `docs/baseline/standards/module-structure.md` | 新增业务模块、创建 Maven 子模块 |
| 分层架构 | `docs/baseline/architecture/module-internals.md` | 新增类、抽取共享逻辑、处理循环依赖 |
| 模块边界 | `docs/baseline/architecture/module-boundaries.md` | 新增服务、调整模块职责、变更依赖方向 |
| 技术基线 | `docs/baseline/architecture/system-baseline.md` | 变更技术栈、引入外部平台、调整验证命令 |
| 跨模块流程 | `docs/baseline/architecture/business-flows.md` | 跨模块调用序列变更、通道切换、失败策略调整 |

## 全局约定

### 文件入 / 文件出

- 重要输入在实施前写入 `docs/work/`
- 重要输出写回仓库，不仅留聊天中

### Git

- 分支：`codex/feature-{需求}` / `codex/fix-{问题}`
- Commit：`{type}({scope}): {描述}`
- 不提交：`.env`、`*.log`、IDE 配置、`target/`

### 引用规则

- 涉及具体规范 → 按触发场景表加载对应文件，不加载全部
- 规范与框架规范冲突 → 框架规范优先（框架定义契约）
- 规范未覆盖 → 写入 `docs/work/<branch>/discussion.md`
- 重复教训 ≥2 次 → 提升为 standards/ 条目或 skills/ 审计提示词
- standards/ 只写项目级决策，不重述框架文档。若某个外部工具的用法需要调研 → 先形成 `skills/engineering/`，standards 只引用 skill 并追加项目特有约束（参考 testing.md → tdd/SKILL.md 模式）


## 真源优先级

文档冲突裁决规则见 `docs/baseline/context/source-of-truth-and-precedence.md`。

