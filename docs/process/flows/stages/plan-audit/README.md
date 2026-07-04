# 计划审计

## 文件角色

独立复核计划作为契约的可行性和合理性，按 type 分三路审计。

## 首先阅读

- [audit-prompt.md](audit-prompt.md) — 计划审计具体步骤

## 执行

使用 [audit-prompt.md](audit-prompt.md) 独立复核计划。审计开始前读取计划 frontmatter `type`，audit-prompt.md 内按类型分三路审计。

## 产出

`docs/audits/YYYY-MM-DD-plan-audit.md`

## 退出

审计通过 → `standalone`/`sub` → implement；`master` → 编排循环

## 回退

审计未通过时，按计划类型差异化回退：

| type | 阻塞来源 | 回退路径 |
|------|---------|---------|
| `master` | 拆分缺陷、依赖图、集成关卡 | → `project`：修改总计划（调整子计划清单/依赖图/集成关卡），已有子计划骨架若受影响则级联标记为需重对齐 |
| `standalone` | 实施契约缺陷 | → `plan`：修改单计划后重审 |
| `sub` | 实施契约缺陷（单计划自身问题） | → `project`：修改子计划后重审 |
| `sub` | 范围对齐 / 并行冲突（根因在总计划分配） | → 标记总计划待调整，本子计划及受影响并行子计划暂停；总计划调整并重新通过 `plan-audit` 后再继续 |

子计划审计中若同时存在自身问题和总计划根因，先处理总计划根因（防止改完子计划又被总计划变更覆盖）。
