# 主流程

## 用途

需求工作流入口。当意图检测判定路由为 `需求` 时进入本文件。公共阶段始终执行，investigate → solution 后进入 scope 判定，按级别追加差异序列。

## 首先阅读

- [intent-detect.md](../intent-detect.md) — 意图检测与 Tier 响应
- [routing.md](../routing.md) — 路由表，本文件是路由目标之一
- [stages/project/README.md](stages/project/README.md) — 项目拆分阶段：入口、执行、产出
- [stages/project/program-management.md](stages/project/program-management.md) — 拆分后的编排规则：循环、状态、集成审计

## 公共序列

| 阶段 | 条件 | 模块 |
|------|------|------|
| 收集输入 | 始终 | [collect](stages/collect/README.md) |
| 澄清模糊 | 始终 | [clarify](stages/clarify/README.md) |
| 合成需求 | 始终 | [requirement](stages/requirement/README.md) |
| 摸底 | 始终 | [investigate](stages/investigate/README.md) |
| 方案 | 始终 | [solution](stages/solution/README.md) |
| 范围判定 | 始终 | [scope](stages/scope/README.md) |
| 更新基线 | scope-analysis.md 判定 ≠ 轻量 → 触发 | [baseline](stages/baseline/README.md) |

跳过判定由本文件持有，阶段 README 不内置执行条件。

## scope 退出：按级别追加序列

| 级别 | 条件 | 追加序列 |
|------|------|---------|
| 轻量 | scope-analysis.md 判定 = 轻量 | implement → qa → 流程管理 |
| 单计划 | scope-analysis.md 判定 = 单计划 | requirement-audit → plan → plan-audit → implement → qa → 流程管理 |
| 多计划 | scope-analysis.md 判定 = 多计划 | requirement-audit → project → plan-audit（总计划）→ 编排循环（每个子计划: plan → plan-audit → implement → qa）→ 集成审计 → 流程管理 |

> scope=单计划 序列中的阶段不另设独立条件——scope 判为单计划已编码了它们的触发前提。scope 的判定现基于 solution.md（已决策的方案），而非猜测。

## 模块索引

| 阶段 | 模块 |
|------|------|
| 摸底 | [investigate](stages/investigate/README.md) |
| 方案 | [solution](stages/solution/README.md) |
| 范围判定 | [scope](stages/scope/README.md) |
| 更新基线 | [baseline](stages/baseline/README.md) |
| 需求审计 | [requirement-audit](stages/requirement-audit/README.md) |
| 编写计划 | [plan](stages/plan/README.md) |
| 项目拆分 | [project](stages/project/README.md) |
| 计划审计 | [plan-audit](stages/plan-audit/README.md) |
| 实施验证 | [implement](stages/implement/README.md) |
| 行为验证 | [qa](stages/qa/README.md) |
| 流程管理 | [process-management](stages/process-management/README.md) |
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
- 阶段顺序变更 → 检查 [program-management.md](stages/project/program-management.md) 子计划生命周期截断序列和 [qa/README.md](stages/qa/README.md) 退出路由引用是否仍一致





