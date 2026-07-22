# 主流程

## 用途

需求工作流入口。当意图检测判定路由为 `需求` 时进入本文件。公共阶段始终执行，scope 范围判定后按级别追加差异序列。

## 首先阅读

- [intent-detect.md](../intent-detect.md) — 意图检测与 Tier 响应，决定是否进入本流程
- [routing.md](../routing.md) — 路由表，本文件是路由目标之一
- [stages/project/README.md](stages/project/README.md) — 项目拆分阶段：入口、执行、产出
- [stages/project/program-management.md](stages/project/program-management.md) — 拆分后的编排规则：循环、状态、集成审计

## 公共序列

| 阶段 | 条件 | 模块 |
|------|------|------|
| 收集输入 | 始终 | [collect](stages/collect/README.md) |
| 澄清模糊 | 材料不完整或矛盾 → 触发 | [clarify](stages/clarify/README.md) |
| 合成需求 | 始终 | [requirement](stages/requirement/README.md) |
| 范围判定 | 始终 | [scope](stages/scope/README.md) |
| 技术调研 | scope-analysis.md 能力缺口非空 → 触发 | [research](stages/research/README.md) |
| 更新基线 | scope-analysis.md 能力缺口非空 或 契约触碰非空 → 触发 | [baseline](stages/baseline/README.md) |

跳过判定由本文件持有，阶段 README 不内置执行条件。

## scope 退出：按级别追加序列

| 级别 | 条件 | 追加序列 |
|------|------|---------|
| 轻量 | scope-analysis.md 判定 = 轻量 | implement → code-audit → log |
| 单计划 | scope-analysis.md 判定 = 单计划 | requirement-audit → plan → plan-audit → implement → code-audit → closure → log → skill |
| 多计划 | scope-analysis.md 判定 = 多计划 | requirement-audit → project → plan-audit（总计划）→ 编排循环（每个子计划: plan → plan-audit → implement → code-audit → closure）→ 集成审计 → log → skill |

> scope=单计划 序列中的阶段不另设独立条件——scope 判为单计划已编码了它们的触发前提。唯一例外是 skill：错误模式 ≥2 次才触发，与 scope 无关。

## 模块索引

| 阶段 | 模块 |
|------|------|
| 需求审计 | [requirement-audit](stages/requirement-audit/README.md) |
| 编写计划 | [plan](stages/plan/README.md) |
| 项目拆分 | [project](stages/project/README.md) |
| 计划审计 | [plan-audit](stages/plan-audit/README.md) |
| 实施验证 | [implement](stages/implement/README.md) |
| 代码审计 | [code-audit](stages/code-audit/README.md) |
| 闭环审计 | [closure](stages/closure/README.md) |
| 日志回顾 | [log](stages/log/README.md) |
| 技能提取 | [skill](stages/skill/README.md) |

## 属于这里的

- 阶段序列定义（公共段 + 级别分流追加段）
- 级别判定条件（引用 scope，本文件列结果不列判定逻辑）
- 阶段到模块文件的指针
- 各阶段的跳过条件

## 不属于这里的

- 单个阶段的执行细节 → 各 `stages/<stage>/README.md`
- 意图检测与 Tier 判定逻辑 → [intent-detect.md](../intent-detect.md)
- 路由表维护 → [routing.md](../routing.md)
- 多计划的编排规则 → [stages/project/program-management.md](stages/project/program-management.md)

## 更新原则

- 新增流程阶段 → 同步更新公共序列（如属公共）和模块索引；评估是否需要多计划级别覆盖
- 新增 scope 级别 → scope 退出表增加一行；同步更新 [scope/README.md](stages/scope/README.md) 判定条件
- 阶段跳过条件变更 → 仅修改本文件序列表，阶段 README 不动
- 阶段顺序变更 → 检查 [program-management.md](stages/project/program-management.md) 子计划生命周期截断序列和 [closure/README.md](stages/closure/README.md) 项目分支引用是否仍一致





