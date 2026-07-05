# 架构基线索引

## 用途

`docs/baseline/architecture/` 存放项目的稳定技术基线和架构契约。

此目录是系统"长什么样"和"谁管什么"的唯一真源。当实施涉及新增服务、修改模块边界、变更技术栈或调整外部集成时，从这里开始。

## 首先阅读

1. `system-baseline.md` — 技术栈选型与版本、外部平台（含调研和技能引用）、验证命令
2. `module-boundaries.md` — 模块/服务清单（含端口）、依赖方向、数据所有权
3. `module-internals.md` — 原型选择路由（含已实施模块结构）
4. `archetypes/` — 模块原型问题清单（按场景选原型 → 回答问题 → 产出结构）
   - `archetypes/crud-service.md` — 标准 CRUD 业务模块
   - `archetypes/agent-service.md` — LLM 驱动的 AI 智能体模块
5. `business-flows.md` — 跨模块业务流程路由表

## 文件关系

| 文件 | 职责 | 依赖 |
|------|------|------|
| `system-baseline.md` | 技术栈、外部平台（含调研和技能引用）、验证命令 | 引用 `module-boundaries.md` |
| `module-boundaries.md` | 服务清单（名称/端口/职责/数据/保护级别）、依赖拓扑 | 独立，被 `system-baseline.md` 和 `business-flows.md` 引用 |
| `module-internals.md` | 原型选择路由 + 已实施模块结构列表 | 引用 `archetypes/`，被 `module-boundaries.md` 引用 |
| `archetypes/` | 模块原型问题清单（按场景选原型 → 回答问题 → 产出结构） | 被 `module-internals.md` 引用 |
| `business-flows.md` | 跨模块调用时序、通道、失败策略 | 引用 `module-boundaries.md`（拓扑） |

## 属于这里的

- 稳定的技术基线（架构模式、技术栈、外部平台）
- 模块所有权和依赖方向规则（模块间）
- 模块内分层结构和层间依赖规则
- 跨模块业务流程路由

## 不属于这里的

- 调研笔记 → `docs/work/`
- 外部技术使用指南 → `docs/skills/tech/`
- 编码规范 → `docs/baseline/standards/`
- 实施计划 → `docs/work/<branch>/`
- 日报和验证记录 → `docs/logs/`
- 方法论文章 → `docs/reference/articles/`

## 更新原则

- 每个文件末尾有独立的更新触发条件，按条件改对应文件
- 服务新增/删除只改 `module-boundaries.md`，不碰 `system-baseline.md`
- 引入外部平台：先经阶段 ④ 调研，批准后在 `system-baseline.md` 注册，使用模式固化到 `docs/skills/tech/`
