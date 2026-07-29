# 快速模式

## 用途

需求工作流快速入口。当意图检测判定路由为 `快速模式` 时进入本文件。

适用于需求明确、范围小、方案无争议的变更——跳过摸底/方案/范围判定/审计/计划，直接进入核心实施闭环。

## 首先阅读

- [routing.md](../routing.md) — 路由表，本文件是路由目标之一
- [intent-detect.md](../intent-detect.md) — 快速模式触发判定逻辑

## 核心序列

| 阶段 | 条件 | 模块 |
|------|------|------|
| 收集输入 | 始终 | [collect](stages/collect/README.md) |
| 澄清模糊 | 始终 | [clarify](stages/clarify/README.md) |
| 合成需求 | 始终 | [requirement](stages/requirement/README.md) |
| 实施验证 | 始终 | [implement](stages/implement/README.md) |
| 行为验证 | 始终 | [qa](stages/qa/README.md) |
| 流程管理 | 始终 | [process-management](stages/process-management/README.md) |

跳过判定由本文件持有，阶段 README 不内置执行条件。

## 跳过的阶段

以下主流程公共序列中的阶段在快速模式下跳过：

| 跳过的阶段 | 原因 |
|-----------|------|
| 摸底（investigate） | 快速模式条件已确认单模块、无架构影响 |
| 方案（solution） | 快速模式条件已确认方案唯一且明显 |
| 范围判定（scope） | 快速模式条件隐含 = 轻量 |
| 更新基线（baseline） | 快速模式不涉及架构/技术基线变更 |
| 需求审计（requirement-audit） | 快速模式范围小、风险低 |
| 编写计划（plan） | 快速模式无需正式计划，requirement.md 即为实施契约 |
| 计划审计（plan-audit） | 同上 |

## 退出条件

快速模式不设 scope 分流。完成流程管理后流程结束。

若执行中发现实际情况不满足快速模式条件（如涉及跨模块、需要方案决策），**立即中止当前流程，退回到需求路由重新进入 main.md**。

## 保护

快速模式不豁免保护区规则。触及保护区（支付、Gateway 认证/鉴权、DDL、Kafka topic、跨服务契约、API 版本废弃）时，按 `docs/baseline/context/ai-autonomy-policy.md` 中保护区规则执行——即使条件满足快速模式，保护区操作仍需人类确认。

## 模块索引

| 阶段 | 模块 |
|------|------|
| 合成需求 | [requirement](stages/requirement/README.md) |
| 实施验证 | [implement](stages/implement/README.md) |
| 行为验证 | [qa](stages/qa/README.md) |
| 流程管理 | [process-management](stages/process-management/README.md) |

## 属于这里的

- 快速模式阶段序列定义
- 跳过阶段的说明和原因
- 降级退出条件
- 保护区规则引用

## 不属于这里的

- 单个阶段的执行细节 → 各 `stages/<stage>/README.md`
- 快速模式触发判定逻辑 → [intent-detect.md](../intent-detect.md)
- 标准主流程 → [main.md](main.md)

## 更新原则

- 新增跳过的阶段 → 同步更新"跳过的阶段"表和原因
- 新增核心序列阶段 → 同步更新核心序列表和模块索引
- 保护区规则变更 → 引用 `ai-autonomy-policy.md`，本文件不独立维护保护区列表