# 流程管理

## 文件角色

qa 验证通过后，对本次开发流程做归档、经验记录、上下文更新与收尾总结。合并了原 log 阶段的日志追加和错误模式检测功能。

## 进入条件

| scope | 条件 |
|-------|------|
| 轻量 / 单计划 | `test-cases.md` 存在且 `## 裁定` Route = human（qa 通过） |
| 多计划 | 集成审计通过（见 [program-management.md](../project/program-management.md) §集成审计） |

## 执行步骤

| # | 步骤 |
|---|------|
| 1 | 归档计划：plan.md frontmatter `status` → `completed`，同步更新 `registry.md`；`docs/work/<branch>/` 目录标记为归档 |
| 2 | 记录经验教训：将本次开发中发现的可用知识写回文档 — 可复用的模式 → `docs/baseline/context/`；实质性偏离 → `docs/retro/`；非显而易见回归 → `docs/bugs/` |
| 3 | 评估上下文影响：本次变更是否改变了模块边界、公共契约、验证命令或技术基线 → 是则更新 `docs/baseline/context/` 和 `docs/baseline/standards/` 中受影响的文件 |
| 4 | 跨模块流程检查：搜索 `docs/flows/` 的"边界"列，若命中则判断本次改动是否改变了调用序列、通道或失败策略 → 是则更新对应 flow |
| 5 | Git 提交：按 `docs/skills/engineering/git-commit/SKILL.md` 执行，代码与文档拆分提交 |
| 6 | 追加每日开发日志到 `docs/logs/YYYY/MM-DD.md`（倒序，格式见 `docs/logs/00-log-writing-guide.md`） |
| 7 | 错误模式检测与技能提取：搜索 `docs/logs/` 中错误/缺陷/回归/失败关键词 → 出现 ≥2 次则将重复教训提升为 `docs/skills/<分类>/<名称>/SKILL.md`；<2 次则跳过 |
| 8 | 输出流程管理摘要 → 写入 `docs/work/<branch>/process-summary.md` |

## 产出物

| 产出 | 路径 |
|------|------|
| 流程管理摘要 | `docs/work/<branch>/process-summary.md` |
| 开发日志 | `docs/logs/YYYY/MM-DD.md` |
| 经验记录 | `docs/retro/`、`docs/bugs/`、或 `docs/baseline/context/`（如有） |
| 新技能 | `docs/skills/<分类>/<名称>/SKILL.md`（如有） |

摘要格式：

```markdown
# 流程管理摘要 — <branch>

## 归档
- 计划状态: [planned / in-progress → completed]
- registry 同步: [Y / N]
- work 目录: [归档路径]

## 经验教训
- [发现的知识 / 模式 / 教训，无则"无"]
- 记录位置: [context/ | retro/ | bugs/ | 无]

## 上下文变更
- 受影响的 context/ 文件: [列出，无则"无"]
- 受影响的 standards/ 文件: [列出，无则"无"]

## 跨模块流程影响
- 受影响的 flow 文件: [列出，无则"无"]

## Git
- 分支: [分支名]
- 提交: [hash / 简短描述]

## 开发日志与技能
- 日志文件: `docs/logs/YYYY/MM-DD.md`
- 错误模式检测: [<2 次（跳过）/ ≥2 次（已提取技能 → `docs/skills/...`）]

## 后续
- [待办项 / 派生需求，无则"无"]
```

## 完成证明

- `Test-Path docs/work/<branch>/process-summary.md` 确认文件存在
- `rg` 确认当日日志中本次变更日期条目存在

## 完成报告

完成证明通过后，输出以下块：

`
**总结：** [1-2 句话总结本阶段产出]
**阻塞/顾虑：** [仅在有阻塞或顾虑时出现此字段]
**下一步：** 流程结束。
`

## 退出路由

完成证明通过后，流程结束。无需等待下一阶段。

| 条件 | 去向 |
|------|------|
| 完成 | 流程结束 |