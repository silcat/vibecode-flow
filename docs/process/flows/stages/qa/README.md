# QA - 验证与闭环

## 文件角色

implement 完成后，独立执行完整验证闭环：行为验证、代码审查、闭环判定。实施者不得自审。

## 执行步骤

本阶段由 `.codex/agents/qa-agent.toml` 执行。实施细节见 Agent 定义文件。

| # | 步骤 |
|---|------|
| 1 | 委派 `.codex/agents/qa-agent.toml` 执行验证 |
| 2 | 等待子 agent 完成，读取 `test-cases.md` |
| 3 | 读取 `test-cases.md` `## 裁定` 中的 `**Route:**` 字段，按退出路由表分流 |

## 进入条件

| # | 条件 |
|---|------|
| 1 | implement-report.md 存在且裁决 PASS |
| 2 | plan.md 存在（含闭环关卡 checklist） |

## 产出物

| 产出 | 路径 |
|------|------|
| 测试用例 | `docs/work/<branch>/test-cases.md` |

## 完成证明

### 文件存在

`Test-Path docs/work/<branch>/test-cases.md` 确认文件存在。

### 内容校验

读取 `test-cases.md`，在对话中输出自检：

```
::qa-check
- ## 裁定 段存在：✓ / ✗
- **Status:** DONE / BLOCKED
- 全部用例结果非 TODO：✓ / ✗（有 TODO → ✗）
- 全部用例结果含 PASS/FAIL：✓ / ✗
::qa-check
```

任一 ✗ → 阶段不完整，留在 qa。全部 ✓ → 完成证明通过。

## 退出路由

主 agent 读取 `test-cases.md` `## 裁定` 中的 `**Route:**` 字段，按以下表分流：

| Route | 去向 |
|-------|------|
| implement | → implement（TDD 修复） |
| human | → 暂停，等待人类确认 |

等待人类确认后才进入下一阶段。禁止自动推进。