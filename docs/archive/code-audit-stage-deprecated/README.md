# 代码审计

## 文件角色

独立审查代码质量，产出 P0-P3 分级发现。实施者不得自审——本阶段由独立 agent 执行。

本阶段由 `.codex/agents/code-reviewer.toml` 执行。审查细节（边界侦查、保护区检查、系统性审查、分级）见 Agent 定义文件。

## 进入条件

| # | 条件 |
|---|------|
| 1 | qa-report.md 存在且裁定 PASS |
| 2 | plan.md 存在 |

## 产出物

| 产出 | 路径 |
|------|------|
| 审查报告 | `docs/work/<branch>/code-review-report.md` |

## 完成证明

`code-review-report.md` 存在且 `## 5. 裁定` = PASS。

完成证明清单：

- [ ] 读取 agent 输出的 `**Status:** DONE` 或 `DONE_WITH_CONCERNS`
- [ ] code-review-report.md 存在

报告包含：变更范围、机械扫描、语义发现（P0-P3）、正向观察、裁定。

## 退出路由

| 条件 | 去向 |
|------|------|
| PASS | → closure |
| FAIL（P0/P1 未清零） | → implement（携带 P0/P1 发现列表） |

等待人类确认后才进入下一阶段。禁止自动推进。
