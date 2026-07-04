# 吸引子引导工程模板（Attractor-Guided Engineering Template）

本模板是一个轻量级应用层项目脚手架，用于 AI 辅助产品开发。

适用于普通业务应用，如管理系统、门户、工作流应用、仪表盘、内部工具以及 CRUD 密集型的领域产品。

面向已有技术栈的中小型项目。

它不是起始应用，也不包含生成的产品代码。其目的是为仓库提供足够持久的骨架，让人和 AI 能够共享需求、所有者文档基线、计划、验证和项目记忆，而无需沉重的流程负担。

## AGE 是什么

AGE 即 **吸引子引导工程（Attractor-Guided Engineering）**。

AGE 从一个问题出发：

当人和 AI 随时间不断修改这个仓库时，它应该持续向什么收敛？

在本模板中，吸引子是指应用项目在快速 AI 辅助迭代过程中，应持续回归的稳定的产品、设计及架构结构。

对于应用项目，吸引子由一小套持久的所有者文件承载：

- `docs/baseline/context/` — 强制性的项目上下文和真源规则
- `docs/baseline/architecture/` — 稳定的技术结构、模块边界和应用行为基线
- `docs/baseline/standards/` — 按场景触发的编码规范
- `docs/work/` — 按分支组织的工作产物（需求、计划、讨论、调研）

计划、测试、审计、日志、Bug 记录和验证不是吸引子。它们是工程夹具：局部控制手段，用来证明某项变更将仓库推向了吸引子，而不仅仅是勾完了一份清单。技能（`docs/skills/`）和回顾（`docs/retro/`）是可选增强层。

如果一项变更与所有者文档基线矛盾、仅在计划中隐藏了行为变化、或让后续会话无法从仓库文件中恢复当前真相，那么即使测试通过，它在 AGE 中也不算通过。

## AGE 不仅仅是夹具工程

夹具优先的工程会问：

- 我们如何约束 AI？
- 我们如何验证输出？
- 我们如何审计并记住发生过什么？

AGE 则先问一个更前置的问题：

- 项目应该持续回归到什么样的稳定结构？

计划、测试、审计、日志、Bug 记录和 CI 这些夹具，只有在吸引子存在之后才有意义。它们本身不定义正确性，而是对照所有者文档基线来度量、纠正并保持仓库的轨迹。

## AGE 不是规格驱动开发

当行为变更需要以结构化的规格增量来组织时，规格驱动工作流是有用的。

AGE 不会强制每个事实都走同一套规格/变更/归档工作流，也不会把一棵规格树当作普适的真源。

在 AGE 中：

- 需求回答现在该构建什么
- 设计文档回答当前支持的应用行为
- 架构文档回答当前支持的技术结构
- 计划回答一个非平凡的切片将如何闭环
- 测试和审计挑战完成声明
- 日志、Bug 和测试记录保存轨迹记忆

规格仍然可以有用。它只是其中一种可能的夹具，而非顶层组织模型。

## AGE 不是技能库

可复用技能有助于执行重复的工作方法，但它们不是吸引子，也不能替代项目特定的路由。

一个缺乏路由的大型技能库通常会变成结构化的氛围编码：AI 可能快速执行熟悉的模式，但仍然需要人来纠正——哪些所有者文档重要、该用哪个技能、需要什么证明。

在 AGE 中，技能是方法选择器。它们必须通过以下路由：

- `AGENTS.md` — 代理运营协议和强制规则
- `docs/index.md` — 文档路由器和目录所有权
- 当前活跃需求（`docs/work/plan-registry.md`）
- 所有者文档（`docs/baseline/`）
- 计划中的技能选择记录

如果技能选择不明确，应由独立的子代理或审查者在实施前做出选择。对于非平凡计划，每个阶段或条目应记录 `Skill: <名称>` 或 `Skill: none`。

## 本模板的范围

本仓库是面向应用层项目的 AGE 模板。

AGE 本身比这个模板更广。框架级项目也可以使用 AGE，但需要围绕自己的吸引子来定制项目特定的所有者文档、指南、路由规则、审计提示词、验证策略和审查提示词。

框架级 AGE 实践的更深入示例包括：

- [`nop-chaos-flux`](https://gitee.com/canonical-entropy/nop-chaos-flux) — 前端框架和低代码运行时实践，包含所有者文档优先、计划闭环、审计、Bug、日志和 AGE 方法论
- [`nop-entropy`](https://gitee.com/canonical-entropy/nop-entropy) — 后端框架实践，包含自身的规范文档和开发过程记忆

不要将这个应用模板原封不动地复制到框架核心仓库就以为够了。请将其作为概念起点，然后定义框架自己的吸引子和指南。

## 本模板的由来

本模板是从现有 Nop 项目使用的 AI 开发工作流中提取轻量部分，并为中小型应用团队所做的适配。

主要参考来源：

- [`nop-chaos-flux`](https://gitee.com/canonical-entropy/nop-chaos-flux) — 所有者文档优先、计划闭环、审计、Bug、日志和 AGE 方法论
- [`nop-chaos-next`](https://gitee.com/canonical-entropy/nop-chaos-next) — 应用层 `design/`、`input/`、`logs/`、`bugs`、`skills` 和轻量项目文档实践
- [`nop-entropy`](https://gitee.com/canonical-entropy/nop-entropy) — 面向 AI 的规范文档与开发过程记忆的区分

模板经过多轮审查简化：

1. 从核心的 AGE 文件入/文件出理念开始。
2. 按职责拆分原始输入、需求、应用设计、架构、计划、日志、Bug 和经验教训。
3. 为有时效性要求的记录添加日期命名规则。
4. 为常见的日期文档添加可复制的示例。
5. 将强制性的 AI 上下文从 `docs/references/` 移到 `docs/baseline/context/`，因为代理通常不会主动阅读参考材料，除非材料在入口路径上。
6. 添加显式的 AI 自治、backlog、代码地图和已知良好基线钩子，让 AI 能在不每轮都重新发现仓库的情况下选择安全的下一步行动。
7. 保持回顾、技能、测试记录和分析等进阶层为可选项，让模板对中小项目保持可用。
8. 要求创建的计划在实施前进行计划审计、在完成前进行闭环审计。
9. 在计划中显式化技能选择，使可复用技能不会替代项目特定路由。

独立审查认为模板方向有用，但也预警了两个风险：文档表演和流程过重。当前版本通过保持精简的默认路径和显式标注可选层来应对这些风险。

## 首次使用

复制本模板后，从以下文件开始：

- `START-HERE-after-copy.md`

在 Day 0 清单完成到足够支撑第一个切片之前，不要让 AI 实施功能。

最重要的设置步骤是往 `docs/baseline/context/project-context.md` 中填入真实的活跃工作和真实验证命令。

## 本模板解决的问题

很多团队目前在两者之一：

- 把一大坨需求扔到聊天里，让 AI 生成整个系统
- 凭感觉写代码，只有零散的笔记和薄弱的历史记录

这两种方式通常都会漂移到 demo 级别的产出。

本模板把仓库变成一个持久的执行平面，提供一条精简的默认路径：

1. 原始输入收集（阶段 ①）
2. 澄清模糊 + 需求合成（阶段 ②③）
3. 技术调研（阶段 ③.5，条件触发）
4. 更新稳定基线（阶段 ⑤）
5. 编写执行计划 + 计划审计（阶段 ⑧⑨）
6. 实施与验证（阶段 ⑩，含 TDD + 子代理派生）
7. 闭环审计（阶段 ⑫）
8. 日志 / 回顾 / 技能提取（阶段 ⑬⑭）

对于创建的计划，计划审计和闭环审计是强制控制回路。完整 14 阶段流程见 `docs/process/protocols/routing-protocol.md` 和 `docs/process/full-workflow-sequence.md`。

## 本模板包含的内容

- `AGENTS.md` — AI 代理运营协议，含前置路由 + 主流程入口
- `START-HERE-after-copy.md` — 复制模板后的 Day 0 设置清单
- `docs/index.md` — 文档路由器及目录所有权基线
- `docs/baseline/context/` — 强制性的 AI 上下文（project-context、codebase-map、conventions）
- `docs/baseline/architecture/` — 稳定的技术基线和模块边界（system-baseline、module-boundaries、project-vision、roles-and-permissions）
- `docs/baseline/standards/` — 按场景触发的编码规范（api、api-versioning、auth、backend、database、observability、service-communication、testing）
- `docs/work/` — 按分支组织的工作产物：需求、计划、讨论、调研；含 plan-registry.md
- `docs/process/` — 工作流协议和操作指南
- `docs/process/protocols/` — AI 可执行的协议文件（routing-protocol、orchestration、plan-lifecycle）
- `docs/process/guides/` — 人读的操作指南（需求合成、计划编写、审计执行等）
- `docs/skills/` — 可复用技能（审计提示词、工程方法、技术评估）；复制后应针对本地项目调整
- `docs/logs/` — 按年/月-日组织的每日开发日志
- `docs/bugs/` — 复杂回归和根因记录
- `docs/retro/` — 可选的事后差距分析和过程改进
- `docs/reference/` — 外部参考：方法论文章、团队手册、框架规范（如 Spring）
- `docs/superpowers/` — Superpowers 技能框架生成的 spec 文件
- `docs/archive/` — 非活跃文档归档
- `tools/` — 辅助脚本（文档检查、代码统计、乱码扫描）

## 默认最小配置

对于大多数中小项目，只需以下内容即可开始编写第一个小切片：

- `AGENTS.md`
- `docs/baseline/context/`（project-context、codebase-map、conventions）
- `docs/work/`（需求文件 + plan-registry）
- `docs/index.md`
- `docs/process/protocols/routing-protocol.md`

触发式使用：

- `docs/baseline/standards/` — 编码时按场景加载
- `docs/work/<branch>/plan.md` — 满足计划触发条件时
- `docs/skills/` — 需要审计或可复用方法时
- `docs/logs/` — 有真实变更落地时
- `docs/bugs/` — 修复了非显而易见的 Bug 时
- `docs/retro/` — 原型与实施出现实质性偏离时

创建的计划在实施前需要计划审计，在完成前需要闭环审计。

其余一切都是可选的，仅在项目复杂度需要时才应使用。

模板中附带的审计提示词和技能是通用默认值。复制模板后，必须根据真实项目的保护区、所有者文档结构、部署模型、验证技术栈、命名约定、已知故障模式和误报容忍度进行调整。

## 稳定文件 vs 日期文件

模板沿用现有系统的基础分类：

- 稳定的所有者文档使用固定的文件名
- 有时效性的过程记录通常带有日期

示例：

- 稳定：`docs/baseline/architecture/system-baseline.md`、`docs/baseline/context/project-context.md`
- 日期：`docs/superpowers/specs/YYYY-MM-DD-<topic>-design.md`、`docs/work/<branch>/research.md`
- 按年组织的每日记录：`docs/logs/YYYY/MM-DD.md`

参见 `docs/reference/guides/document-naming-and-timeliness.md`。

## 核心原则

不要让重要工作只通过聊天来推进。

- 原始材料进入 `docs/work/input/`
- 强制上下文和所有者优先级进入 `docs/baseline/context/`
- 活跃工作和计划进入 `docs/work/`
- 不明确之处进入 `docs/work/<branch>/discussion.md`
- 稳定的设计决策进入 `docs/baseline/architecture/` 和 `docs/baseline/standards/`
- 执行控制进入 `docs/work/<branch>/plan.md`
- 证明和历史进入 `docs/logs/` 和 `docs/bugs/`
- 过程改进成为 `docs/skills/`、`docs/retro/` 或审计提示词

## 如何启动一个新项目

1. 将本模板复制到新仓库根目录
2. 完成 `START-HERE-after-copy.md` 中的 Day 0 清单
3. 将 PM 笔记、原型链接、原始材料放入 `docs/work/input/`
4. 如果输入仍然模糊，在实施前在 `docs/work/<branch>/discussion.md` 中澄清
5. 在要求 AI 编码前，将确定的范围合成为 `docs/work/<branch>/requirement.md`

## 推荐的执行模式

1. 在 `docs/work/input/` 中收集原始材料（阶段 ①）
2. 必要时在 `discussion.md` 中澄清模糊之处（阶段 ②）
3. 合成可立即实施的需求（阶段 ③）
4. 涉及新技术时执行技术调研，产出 `research.md`（阶段 ③.5）
5. 更新 `docs/baseline/` 下的稳定基线（阶段 ⑤）
6. 满足计划触发条件时创建 `plan.md`（阶段 ⑧）
7. 计划审计（阶段 ⑨）
8. 实施与验证，走 TDD 循环（阶段 ⑩）
9. 闭环审计（阶段 ⑫）
10. 更新日志和受影响的文档（阶段 ⑬）

仅在需要时使用：

- `docs/skills/audit/document-audit-prompt.md` — 文档审计
- `docs/retro/` — 当原型与实施出现实质性偏离时
- `docs/skills/` — 当同一方法重复次数足够多，值得提炼为可复用提示词时

如果重复的错误模式不断出现，不要止步于纯文字记录。应考虑逐步将其提升为可复用的审计提示词、检查清单、启发式脚本、静态检查、Lint 规则、CI 守卫或 codemod，并根据复制后项目的真实约定和误报容忍度进行调整。

完整工作流文档见 `docs/process/protocols/routing-protocol.md` 和 `docs/process/full-workflow-sequence.md`。

## 非目标

- 本模板不规定固定的前端/后端框架。
- 本模板不包含生成的应用代码。
- 本模板不替代你的包管理、Lint、测试或 CI 设置。
- 本模板不假定规格驱动开发是唯一有效的产物工作流。
- 本模板不是框架核心仓库的通用 AGE 模板；框架项目需要自己的领域特定所有者文档和指南。
- 本模板不是通用技能库。技能必须通过项目路由和所有者文档来选择。

## 延伸阅读

- `docs/reference/articles/from-spec-driven-development-to-attractor-guided-engineering.md`
- `docs/reference/articles/attractor-before-harness-ai-large-scale-development-methodology.md`
- `docs/reference/articles/README.md`

## 许可证

MIT

## 作者微信和微信讨论群
![](https://gitee.com/canonical-entropy/nop-entropy/raw/master/wechat-group.png)

添加微信时请注明：加入Nop平台群
## 微信公众号
![](https://gitee.com/canonical-entropy/nop-entropy/raw/master/wechat-public-account.jpg)