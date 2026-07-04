# <项目名称> 文档索引

## 用途

此 `docs/` 目录树是 `<项目名称>` 的持久记忆和路由平面。

- 在进行工作流、需求、设计或实施变更之前，从这里开始
- 优先阅读能回答当前问题的最小文件
- 将持久结论保存在文件中，而不仅仅是聊天中

## 路由权威

本文件是顶层文档路由器。

- `docs/index.md` 拥有导航和目录职责
- `AGENTS.md` 拥有代理工作流规则和执行预期
- `docs/baseline/architecture/` 拥有稳定的技术基线和 API 契约

## 首先阅读

| 如果你想…… | 首先阅读 | 然后阅读 |
|-----------|---------|---------|
| 理解强制性的 AI 上下文和当前项目状态 | `docs/baseline/context/project-context.md` | `docs/baseline/context/codebase-map.md`、`docs/baseline/context/conventions.md` |
| 理解轻量级默认开发工作流 | `docs/process/guides/application-development-workflow.md` | `AGENTS.md` |
| 选择下一个 AI 就绪的工作项 | `docs/work/` | `docs/baseline/context/project-context.md`（自治策略）、活跃需求 |
| 阅读原始材料或输入 | `docs/work/` | `docs/work/` 中的活跃文件 |
| 阅读解释性方法论文章 | `docs/reference/articles/README.md` | `docs/reference/articles/` 下的相关文章 |
| 澄清模糊需求 | `docs/work/<branch>/discussion.md` | `docs/process/flows/stages/requirement/README.md` |
| 进行技术选型调研 | `docs/process/flows/stages/research/evaluation-framework.md` | `docs/work/<branch>/research.md` |
| 查找某个第三方库的使用指南 | `docs/skills/tech/` | 对应 `tech-{技术名}/SKILL.md`
| 在编码前路由任务（含前置路由） | `AGENTS.md`、`docs/process/protocols/process/routing.md` | `docs/skills/README.md`、`docs/process/00-plan-authoring-and-execution-guide.md` |
| | 判断某个已有技能是否适用 | `docs/skills/index.md` | 对应 `SKILL.md`
| 理解项目目标和系统定位 | `docs/baseline/architecture/project-vision.md` | `docs/baseline/architecture/system-baseline.md` |
| 理解当前技术基线 | `docs/baseline/architecture/system-baseline.md` | `docs/baseline/architecture/module-boundaries.md` |
| 理解跨模块的端到端业务流程 | `docs/baseline/architecture/business-flows.md` | 相关服务的 `docs/flows/` 流程文件 |
| 理解 API 契约和服务间关系 | `docs/baseline/architecture/module-boundaries.md` | `docs/baseline/standards/api.md` |
| 理解角色与权限模型 | `docs/baseline/architecture/roles-and-permissions.md` | `docs/baseline/standards/auth.md` |
| 理解所有者文档优先级和真源边界 | `docs/baseline/context/conventions.md` | 相关所有者文档 |
| 开始或审查非平凡实施 | `AGENTS.md` | `docs/skills/README.md`、`docs/process/00-plan-authoring-and-execution-guide.md`、活跃计划及 `docs/process/00-audit-execution-guide.md` |
| 审查审计工作流或必需的审计 | `docs/process/00-audit-execution-guide.md` | `docs/skills/` 中的相关提示词 |
| 理解哪些文档应使用日期文件名而非固定名 | `docs/reference/guides/document-naming-and-timeliness.md` | 目标目录中的相关指南 |
| 复制现成的日期文档骨架 | `docs/examples/README.md` | 重命名最接近的 `.example.md` 文件 |
| 检查变更后必须更新哪些文档 | `docs/reference/guides/maintenance-checklist.md` | `docs/baseline/architecture/` 中最相关的文件 |
| 查看最近的实施历史 | `docs/logs/index.md` | 最新的带日期日志文件 |
| 查找过去的微妙回归 | `docs/bugs/00-bug-fix-note-writing-guide.md` | `docs/bugs/` 中的相关文件 |
| 记录或审查探索性/手动测试 | `docs/work/` | 相关的带日期测试记录 |
| 检查最新的已知良好验证状态 | `docs/logs/` | 最新的带日期测试或日志记录 |
| 审查权衡或开放的设计调查 | `docs/work/` | 相关分析记录 |
| 审查持久可复用的工程经验教训 | `docs/skills/` | 相关的编号经验 |
| 阅读可立即实施的需求 | `docs/work/` | 活跃需求文件 |
| 审查为何已落地的结果仍未达到预期 | `docs/retro/` | 相关回顾记录 |

## 推荐的默认路径

所有请求首先经过前置路由（详见 `docs/process/protocols/process/routing.md`），区分琐碎修复、纯问题、Bug、审计或主流程。进入主流程后：

1. `docs/baseline/context/`
2. 进入 `docs/work/<branch>/` 工作目录
3. `docs/baseline/architecture/`
4. 确认所有者文档 + 匹配可复用技能（任务分类已由前置路由完成）
5. 计划触发条件满足时 → `docs/work/<branch>/plan.md`
6. `docs/skills/` — 用于必需的计划/闭环审计
7. `docs/logs/`
8. 需要时 → `docs/bugs/`

仅在任务复杂度或模糊性需要时使用额外的 `docs/skills/` 和 `docs/retro/` 记录。

## 技能路由

| 如果任务是…… | 首先阅读 | 然后决定 |
|-------------|---------|---------|
| 不清晰的需求 | `docs/process/flows/stages/requirement/README.md` | 首先需要需求文件还是讨论文件 |
| 非平凡实施 | `AGENTS.md` | 每个阶段或条目需要哪些技能，然后使用 `docs/process/00-plan-authoring-and-execution-guide.md` |
| 文档、计划或闭环验证 | `docs/skills/README.md` | 适用哪个审计提示词或审查技能 |
| 重复的已知方法或审查模式 | 相关所有者文档 | 已有技能是否适用，还是应创建新技能 |

技能选择工作方法。它们不替代需求、设计、架构或所有者文档路由。

## 目录角色

- `docs/process/` — 工作流和操作过程文档
- `docs/baseline/context/` — 强制性的 AI 上下文、所有者优先级和项目级约定
- `docs/work/` — 按分支组织的工作产物。包含需求、计划、审计、讨论、调研笔记。分支名编码服务归属。
- `docs/baseline/architecture/` — 稳定的技术基线、模块边界、API 契约和角色权限文档
- `docs/baseline/standards/` — 具体编码规范（后端、API、数据库、认证、测试、通信、可观测性）
- `docs/reference/` — 稳定的查阅指南和维护辅助
- `docs/reference/articles/` — 面向外部的方法论和解释性文章
- `docs/skills/` — 可复用方法技能（标准 SKILL.md 格式，YAML 前置元数据）。审计提示词、审查手册、技术规范、经验教训。
- docs/skills/tech/ — 第三方库/SDK/框架/工具的使用指南（来源不限：调研固化、自行编写、外部引用适配）。胶水代码、最佳实践、实现模板。
- `docs/logs/` — 带日期的实施记忆
- `docs/bugs/` — 复杂回归历史和根因记录
- `docs/retro/` — 可选的交付后差距分析和过程改进
- `docs/archive/` — 由人类决策移至此处的非活跃文档；保留用于历史参考

## 核心原则

使用文件来承载持久真相。

- 基线（baseline）捕获项目级强制规则和技术真源
- 工作目录（work）按分支组织：需求、计划、审计、讨论、调研全部在同一分支目录下
- 技能（skills）捕获可复用的操作方法和审查手册
- 外部技术（skills/tech/）保存第三方库/SDK/框架/工具的使用指南
- 参考（reference）捕获外部查阅知识和团队手册
- 日志（logs）和 Bug 记录保存证明和记忆
- 回顾（retro）解释为什么上一轮迭代仍未达标

## 命名规则

- 稳定的所有者文档使用固定名称
- 有时效性的记录通常应包含日期
- 参见 `docs/reference/guides/document-naming-and-timeliness.md`





