# 编写计划

## 文件角色

根据 `scope` 级别产出计划文件，作为实施契约。

- scope=单计划 → 产出 1 个 standalone 计划
- scope=多计划 → 由 project 阶段创建总计划 + 子计划目录骨架，本阶段仅负责填充子计划细节

## 首先阅读

| scope | 模板 |
|-------|------|
| 单计划 | [template.md](template.md)（standalone 格式） |
| 多计划（总计划） | 由 [project 阶段](../project/README.md) 使用 [template-master.md](template-master.md) 产出 |
| 多计划（子计划） | [template.md](template.md)（sub 格式，含前置检查 + parent 字段） |

## 执行

### scope=单计划

根据 requirement.md 和 audit 发现，按 template.md 的 standalone 格式填写。

### scope=多计划

总计划已由 project 阶段产出。编排循环激活子计划时：
1. AI 从总需求提取对应验收标准子集生成 `requirement.md`
2. 按 template.md 的 sub 格式填充实施细节（含前置检查 + parent 字段）

## 产出

- 单计划：`docs/work/<目录>/plan.md`
- 多计划：总计划由 project 阶段产出；子计划 `docs/work/<总目录>/<子计划>/plan.md`

## 退出

→ plan-audit

## 回退

计划不可执行 → audit 或 requirement

## 多计划协调

子计划间有依赖时，按依赖图顺序推进。允许无依赖的子计划并行。

子计划完成时的状态联动：见 [template.md](template.md) 的"多计划状态联动规则"。
