# 技能索引

使用此目录存放可复用的提示词和工作流手册。

这些不是一次性的聊天消息，而是可复用的仓库记忆。

## 目录结构

```
skills/
├── engineering/              ← 方法技能（怎么做事）
│   ├── tdd/                  ← 测试驱动开发
│   │   └── SKILL.md
│   ├── glue-development/     ← 胶水开发实施规范
│   │   └── SKILL.md
│   ├── grill-with-docs/      ← 澄清模糊需求
│   │   └── SKILL.md
│   ├── verification-checklist/  ← 验证检查清单
│   │   └── SKILL.md
│   ├── code-refactor-prompt.md
│   ├── code-refactor-discovery-prompt.md
│   └── bug-diagnosis-prompt.md
├── tech/                     ← 外部技术（第三方库/SDK/框架的使用指南）
│   └── tech-{技术名}/
│       └── SKILL.md
├── audit/                    ← 合规检查技能（不产生代码，只检查）
│   ├── code-quality-audit-prompt.md
│   ├── index-routing-audit-prompt.md
│   ├── multi-dimensional-audit-prompt.md
│   ├── open-ended-audit-prompt.md
│   └── requirement-gap-retrospective-prompt.md
├── meta/                     ← 元技能（维护技能本身）
│   └── skill-authoring-prompt.md
└── README.md
```

> 技术评估框架已移至 `docs/process/flows/stages/research/evaluation-framework.md`，作为阶段 ④ 的核心方法论，不再是可选技能。

## 技能路由规则

选择技能之前：

1. 首先阅读相关需求和所有者文档。
2. 使用 `AGENTS.md` 分类任务类型。
3. 通过匹配工作方法来选择技能，而非仅匹配业务标签。
4. 如果多个技能可能适用，在实施前请独立子代理或审查者选择。
5. 如果没有现有技能明显匹配，记录 `Skill: none` 并按正常文档驱动工作流继续。
6. 对于非平凡计划，在计划中记录技能选择依据和审查结果。

不要添加宽泛的业务场景技能作为项目特定所有者文档的替代品。

子目录路由见各 `index.md`。详细注册信息如下。

## 注册表

### engineering/ — 方法技能

| 技能 | 何时使用 | 何时不使用 | 所需输入 | 预期输出 |
|------|---------|-----------|---------|---------|
| `tdd/SKILL.md` | 编写新功能或修 Bug，需要测试先行 | 测试存量充足，仅微调实现 | 需求 / Bug 描述、现有测试基座 | RED → GREEN → REFACTOR 循环完成的代码 + 测试 |
| `glue-development/SKILL.md` | 编写编排/调度/适配代码 | 需要从零实现核心领域逻辑 | 被编排服务的契约、流程定义 | 胶水代码 + 集成验证 |
| `grill-with-docs/SKILL.md` | 进入阶段 ② 澄清模糊时 | 材料清晰、边界明确 | 源材料、已有基线文档、代码库 | 逐个追问记录 + 术语决议 + 讨论文件 |
| `verification-checklist/SKILL.md` | 实施完成，准备退出阶段 ⑩ | 仍在 TDD 循环中 | 计划验收标准、变更文件列表 | 逐项打勾的验证报告 |
| `code-refactor-prompt.md` | 任务是改善结构而不改变受支持行为 | 任务变更了受支持的行为 | 目标区域、不变量、验证命令 | 安全重构执行和证据 |
| `code-refactor-discovery-prompt.md` | 在重构前发现结构清理候选 | 结构目标已达成一致 | 目标区域、所有者文档、当前代码 | 排序后的重构候选 |
| `bug-diagnosis-prompt.md` | Bug 真实存在但根因尚未被证明 | 缺陷已明显且局部 | Bug 报告、所有者文档、复现路径、验证命令 | 已确认的原因和证明路径 |

### tech/ — 外部技术

第三方库、SDK、框架、工具的使用指南。来源：调研固化、自行编写、外部引用适配。每个技术一个独立子目录。技术替换或过期时归档。

| 技能 | 来源 | 状态 |
|------|------|------|
| _暂无_ | — | — |

### audit/ — 合规检查技能

| 技能 | 何时使用 | 何时不使用 | 所需输入 | 预期输出 |
|------|---------|-----------|---------|---------|
| `code-quality-audit-prompt.md` | 审查代码的行为风险和实现质量 | 仅需要格式或琐碎挑剔 | 变更文件、所有者文档、测试或验证证据 | 按严重程度排序的发现 |
| `index-routing-audit-prompt.md` | 文档索引或目录结构需要路由有效性审查 | 索引没有路由角色或微不足道 | 顶层索引、子索引、目标文件 | 覆盖表、角色测试结果、结构发现 |
| `multi-dimensional-audit-prompt.md` | 高风险工作需要同时从多维度挑战 | 单一对象审计已足够 | 相关需求/所有者文档、计划或变更区域、验证证据 | 按维度分组的发现 |
| `open-ended-audit-prompt.md` | 标准检查清单之外可能存在隐藏问题 | 工作仅需要聚焦范围的结构化审计 | 相关需求/所有者文档、计划（如有）、日志、活变更代码 | 对抗性发现和未知风险说明 |
| `requirement-gap-retrospective-prompt.md` | 落地的工作仍未达到预期，需求管道需要诊断 | 需求仍在起草中 | 原始输入、需求/讨论文档、交付结果 | 回顾发现和过程修正 |

### meta/ — 元技能

| 技能 | 何时使用 | 何时不使用 | 所需输入 | 预期输出 |
|------|---------|-----------|---------|---------|
| `skill-authoring-prompt.md` | 需要创建新技能或从外部引入后转换格式 | 问题可通过修正路由、补充所有者文档或计划指导解决 | 重复问题描述/外部 skill 源材料、所有者文档、项目验证命令和技术栈 | 新技能文件 + 更新的注册表条目 |

## 技能应是方法选择器

技能选择工作方法。它们不替代需求、设计、架构或所有者文档路由。





