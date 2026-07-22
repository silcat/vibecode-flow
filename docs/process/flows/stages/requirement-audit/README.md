# 需求审计

## 文件角色

独立复核需求与基线的一致性，在计划编写前阻断结构性缺陷。

## 进入条件

requirement.md 存在。baseline 已执行（若触发）。

## 执行步骤

按 `audit-prompt.md` 执行。

## 产出物

| 产出 | 路径 |
|------|------|
| 审计报告 | `docs/work/<branch>/requirement-audit.md`（含 `::audit-mechanical` + `::audit-content` 块） |

## 完成证明

`Test-Path docs/work/<branch>/requirement-audit.md`，确认文件存在且含裁决 PASS/FAIL。

## 退出路由

| 条件 | 去向 |
|------|------|
| PASS + 单计划 | → plan |
| PASS + 多计划 | → project |
| PASS + 轻量 | → implement |
| FAIL | 修复阻塞发现后重审 |

