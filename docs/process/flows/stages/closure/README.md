# 闭环审计

## 文件角色

独立复核实施是否匹配计划和需求，产出闭环审计发现。

## 首先阅读

| 如果你需要… | 先读 |
|-------------|------|
| 执行闭环审计的具体步骤 | [audit-prompt.md](audit-prompt.md) |

## 执行

使用 [audit-prompt.md](audit-prompt.md) 独立复核。关卡未过即回退。

退出时：plan.md frontmatter `status` 改为 `completed`，从 registry.md 删除该行。

## 项目级别

若当前工作为多计划（子计划），见 [program-management.md](../project/program-management.md)。子计划各自执行标准闭环审计；最后一个子计划闭环通过后触发集成审计。

## 红线

- 不可跳过闭环审计直接标记完成
- 不可自行放宽关卡

## 产出

`docs/audits/YYYY-MM-DD-closure-audit.md`

## 退出

→ log

## 回退

关卡未过 → implement
## 前置（阻断）

闭环审计前，必须逐项检查并更新以下文档。全部打勾后才允许进入审计：

- [ ] `docs/baseline/context/codebase-map.md` — 入口点、变更路由、脆弱文件是否反映当前实际
- [ ] `docs/baseline/architecture/module-internals.md` — 新增模块/包的内部结构是否已记录
- [ ] `docs/baseline/architecture/module-boundaries.md` — 新增服务/职责变更是否已注册
- [ ] `docs/baseline/context/project-context.md` — 新技术栈是否已注册

缺失任一条 → 先更新文档，再进入审计。不得以"稍后补"为由跳过。
