# 计划审计

## 文件角色

独立复核计划作为契约的可行性和合理性，按 type 分三路审计。实施者不得自审。

## 进入条件

plan.md 存在，frontmatter type/status/autonomy 合法。

## 执行步骤

按 `audit-prompt.md` 执行。审计开始前读取 plan.md frontmatter `type` 决定审计模式：

| type | 模式 |
|------|------|
| master | 总计划审计 |
| standalone | 单计划审计 |
| sub | 子计划审计（单计划审计 + 子计划增量） |

## 产出物

| 产出 | 路径 |
|------|------|
| 审计报告 | `docs/work/<项目>/plan-audit.md` 或 `docs/work/<项目>/<子计划>/plan-audit.md` |

## 完成证明

`Test-Path` 确认 plan-audit.md 存在，裁决 PASS。


## 完成报告

完成证明通过后，输出以下块：

`
**总结：** [1-2 句话总结本阶段产出]
**阻塞/顾虑：** [仅在有阻塞或顾虑时出现此字段]
**下一步：** 当前命中 [退出路由中的实际条件]，等待人类确认后进入 [对应去向]。
`

## 退出路由

完成证明通过后，等待人类确认才进入下一阶段。禁止自动推进。


| 条件 | 去向 |
|------|------|
| PASS + standalone/sub | → implement |
| PASS + master | → 编排循环 |
| FAIL + standalone（< 3 次） | → plan（修改后重审） |
| FAIL + sub（< 3 次） | → project（修改子计划后重审） |
| FAIL + master（< 3 次） | → project（调整子计划清单/依赖图/集成关卡） |
| FAIL + standalone（第 3 次） | → human（循环上限，暂停等人类决策） |
| FAIL + sub（第 3 次） | → human（循环上限，暂停等人类决策） |
| FAIL + master（第 3 次） | → human（循环上限，暂停等人类决策） |




