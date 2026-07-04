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
