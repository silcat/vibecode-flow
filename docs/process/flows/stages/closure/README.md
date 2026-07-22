# 闭环审计

## 文件角色

独立复核实施是否匹配计划和需求，产出闭环审计发现。实施者不得自审。

## 进入条件

- implement-report.md 存在且裁决 PASS
- code-audit.md 存在且裁决 PASS（P0/P1 清零）
- plan.md 存在

## 执行步骤

### 1. 读取上下文

- plan.md — 闭环关卡清单
- implement-report.md — 实施者自证记录
- code-audit.md — 代码审查发现

### 2. 闭环审计

按 `audit-prompt.md` 执行：

- 活行为是否匹配需求
- 闭环关卡是否真正满足（对照 implement-report 验证输出）
- 证据是否在文件中，而非仅聊天中
- 基线文档是否已更新
- 验证失败是否被隐藏

### 3. 多计划

子计划各自执行标准闭环审计。最后一个子计划闭环通过后触发集成审计（见 `program-management.md`）。

## 产出物

| 产出 | 路径 |
|------|------|
| 闭环审计报告 | `docs/work/<branch>/closure-audit.md` |

## 完成证明

`Test-Path` 确认 closure-audit.md 存在，裁决 PASS。PASS → plan.md status → completed，从 registry.md 删除。

## 退出路由

| 条件 | 去向 |
|------|------|
| PASS | → log |
| FAIL + P0/P1 缺陷 | → code-audit |
| FAIL + 验证证据缺失 | → implement |
| FAIL + 闭环关卡不可验证 | → plan |
| FAIL + 需求未覆盖 | → requirement |
