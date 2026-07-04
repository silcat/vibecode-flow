# 编写计划

## 文件角色

根据 `scope` 级别产出计划文件，作为实施契约。

- scope=单计划 → 产出 1 个 standalone 计划
- scope=多计划 → 产出 1 个 master 计划 + N 个 sub 计划

## 首先阅读

- [template.md](template.md) — 计划模板（含 master/sub 格式、退出标准、多计划联动规则）

## 执行

### scope=单计划

根据 requirement.md 和 audit 发现，按 template 的 standalone 格式填写。

### scope=多计划

1. 先写 master 计划：项目章程 + 子计划清单 + 依赖图 + 集成审计关卡
2. 再将总需求拆分为每个子计划的 requirement，放到 `<总目录>/<子计划>/requirement.md`
3. 逐个子计划按 template 的 sub 格式填写（含前置检查 + parent 字段）

## 产出

- 单计划：`docs/work/<目录>/plan.md`
- 多计划：`docs/work/<总目录>/plan.md` + `docs/work/<总目录>/<子计划>/plan.md` × N

## 退出

→ plan-audit

## 回退

计划不可执行 → audit 或 requirement

## 多计划协调

子计划间有依赖时，按依赖图顺序推进。允许无依赖的子计划并行。

子计划完成时的状态联动：见 [template.md](template.md) 的"多计划状态联动规则"。
