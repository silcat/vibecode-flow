# 实施与验证

## 文件角色

按计划执行 TDD 实施并完成全量验证，产出通过全部测试的代码变更。

## 首先阅读

| 如果你需要… | 先读 |
|-------------|------|
| TDD 执行循环 | `docs/skills/engineering/tdd/SKILL.md` |
| 验证检查清单 | `docs/skills/engineering/verification-checklist/SKILL.md` |
| 代码质量审计提示词 | `docs/skills/audit/code-quality-audit-prompt.md` |

## 前置（阻断）

编码前必须完成以下自检，全部打勾后才允许创建或修改代码文件：

- [ ] 已读取 `docs/baseline/context/conventions.md` 路由表
- [ ] 已匹配本次变更触发的全部 standards/ 文件，逐条读取
- [ ] 已读取 `docs/skills/engineering/tdd/SKILL.md`
- [ ] 已在对话中输出匹配摘要（文件名 + 触发场景）

缺失任一条 → 回退到收集/澄清阶段补全，不得进入实施。

## 执行

- 有计划：将 plan.md frontmatter `status` 改为 `in-progress`，更新 registry.md（设为当前激活，状态 in-progress（取值见 `docs/work/registry.md` §字段来源））
- 无计划（轻量路径）：跳过注册表更新
- 读取 plan.md（如有），锚定实施范围（触及面 + 公共契约 + 非目标）
- 按 `docs/skills/engineering/tdd/SKILL.md` 执行 TDD 循环（RED → GREEN → REFACTOR）
- 阶段出口：加载 `docs/skills/engineering/verification-checklist/SKILL.md` 逐条打勾
- 阶段出口：使用 `docs/skills/audit/code-quality-audit-prompt.md` 审计本次变更文件，P0/P1 发现即阻断
- 所有验证必须实际执行并捕获输出

## 红线

- 禁止从记忆或推理中声称结果
- 占位符命令不等同于通过
- 轻量路径也必须通过 code-quality-audit，P0 发现即阻断

## 无测试框架

首个切片 → 搭建测试基础设施。搭建完成前以运行时验证矩阵为最低标准。

## 产出

代码变更 + 验证输出 + code-quality-audit 结果。

## 退出门禁（阻断）

阶段退出前必须逐项通过以下检查，**全部打勾后才允许标记阶段完成或退出至 closure**：

- [ ] 已执行 `docs/baseline/context/project-context.md` 中与本阶段相关的全部验证命令，输出已捕获
- [ ] 已加载 `docs/skills/engineering/verification-checklist/SKILL.md` 并逐条打勾通过
- [ ] 已使用 `docs/skills/audit/code-quality-audit-prompt.md` 审计本次变更文件，P0/P1 发现已清零
- [ ] plan.md 中本阶段的每个闭环关卡均有运行时验证证据（日志/截图/测试输出），不得仅以编译通过为据
- [ ] 验证证据已写入 plan.md 对应阶段的"验证证据"段

任一未通过 → 留在 implement，不得退出。不得以"稍后补"、"需外部环境"、"需 API Key"为由跳过验证打勾。

若因外部依赖（如 API Key 未配置）确实无法完成运行时验证：
1. 未通过的关卡在 plan.md 中保留 `[ ]` 状态
2. 将阶段状态标记为 `blocked`（非 `completed`）
3. 在 blocker 字段写明缺失条件
4. 阶段不得标记为 completed 直到 blocker 解除且验证通过

## 退出

有计划 → closure（须先通过退出门禁）；无计划（轻量路径）→ log

## 回退

设计缺陷 → baseline（轻量路径则先升档至单计划路径）。
