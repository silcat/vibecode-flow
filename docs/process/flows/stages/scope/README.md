# 范围判定

## 文件角色

根据需求文档和架构基线判定工作级别（轻量/单计划/多计划），决定后续阶段序列。

## 首先阅读

| 如果你需要… | 先读 |
|-------------|------|
| 了解模块边界定义 | `docs/baseline/architecture/module-boundaries.md` |
| 了解契约定义（api、database、auth 等） | `docs/baseline/standards/` |
| 了解各级别对应的后续序列 | [main.md](../main.md) §scope 退出 |

## 判定依据

- 需求文档：requirement 产出
- 模块边界：`docs/baseline/architecture/module-boundaries.md`
- 契约定义：`docs/baseline/standards/`（api、database、auth 等）

## 级别条件

| 级别 | 条件 |
|------|------|
| 轻量 | 全部满足：单模块 + 不碰契约 + ≤3 文件 + 无风险 |
| 单计划 | 碰以上任一条，但仍在单一模块内 |
| 多计划 | 可拆为多个独立交付的子工作 |

## 退出

按判定级别派发至对应模块序列。序列表见 [main.md](../main.md)。

## 回退

实施中发现误判 → 调整级别，编排者重新路由。
