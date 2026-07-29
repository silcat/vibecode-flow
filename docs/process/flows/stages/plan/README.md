# 编写计划

## 文件角色

根据 scope 级别产出计划文件，作为实施契约。

- scope=单计划 → 产出 1 个 standalone 计划
- scope=多计划 → 总计划由 project 阶段产出，本阶段负责填充子计划细节

## 进入条件

| scope | 前提 |
|-------|------|
| 单计划 | requirement-audit 已通过 |
| 子计划 | project 阶段已产出总计划 + 编排循环已激活 |

investigate.md + solution.md 存在（若 scope ≠ 轻量）。

## 执行步骤

### 单计划

根据 requirement.md + scope-analysis.md + solution.md，按 `template-standalone.md` 填写。产出 `docs/work/<目录>/plan.md`。

### 多计划（子计划）

总计划已由 project 阶段产出。编排循环激活子计划时：
1. 从总需求提取对应验收标准子集生成 `requirement.md`
2. 按 `template-sub.md` 填充实施细节（含前置检查 + parent 字段）

产出 `docs/work/<总目录>/<子计划>/plan.md`。

### 中断恢复

见 `recovery.md`。

## 产出物

| scope | 路径 |
|-------|------|
| 单计划 | `docs/work/<目录>/plan.md` |
| 子计划 | `docs/work/<总目录>/<子计划>/plan.md` |

## 完成证明

`Test-Path` 确认 plan.md 存在，frontmatter type/status/autonomy 合法。


## 完成报告

完成证明通过后，输出以下块：

`
**总结：** [1-2 句话总结本阶段产出]
**阻塞/顾虑：** [仅在有阻塞或顾虑时出现此字段]
**下一步：** 当前命中 [退出路由中的实际条件]，等待人类确认后进入 [对应去向]。
`

## 退出路由

完成证明通过后，等待人类确认才进入下一阶段。禁止自动推进。


| 条件 | 去向 |
|------|------|
| 完成 | → plan-audit |
| 计划不可执行 | → requirement-audit 或 requirement |

> 多计划协调：子计划间有依赖时按依赖图顺序推进，无依赖的可并行。状态联动见 `template-sub.md`。