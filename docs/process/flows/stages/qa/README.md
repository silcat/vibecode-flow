# QA - 验证与闭环

## 文件角色

implement 完成后，由主 agent 直接执行完整验证闭环：行为验证、代码审查、闭环判定。不委派子代理。实施者不得自审。

## 执行步骤

本阶段由主 agent 直接执行，不再委派子代理。

| # | 步骤 |
|---|------|
| 1 | 加载上下文，执行验证（首次生成 / 修复重入增量更新 test-cases.md → HTTP 验证 + 回归测试） |
| 2 | 执行代码审查 |
| 3 | 执行闭环审计与判定，写入 `test-cases.md` |
| 4 | 读取 `test-cases.md` `## 裁定` 中的 `**Route:**` 字段，按退出路由表分流 |

## 进入条件

| # | 条件 |
|---|------|
| 1 | 首次：全部 task-N-report.md 存在且 implement 完成证明通过；修复模式重入：test-cases.md 存在 |
| 2 | plan.md 存在（含闭环关卡 checklist） |

## 上下文加载

| # | 加载内容 | 说明 |
|---|---------|------|
| 1 | `git diff --stat` + `git diff --name-only` | 变更文件列表 |
| 2 | `docs/work/<branch>/plan.md` | 闭环关卡 checklist |
| 3 | `docs/baseline/context/project-context.md` | 验证命令、保护区 |

## 角色

本阶段执行者是 implement 阶段之后的独立 QA 执行者。执行完整的验证闭环：生成测试用例 → 行为验证 → 代码审查 → 判定。只做验证和审查，不修改任何代码。

## 工作流

### 1. 验证执行

按 `docs/skills/engineering/qa-verification/SKILL.md` 执行。核心动作：

- 首次 qa：从 plan.md 闭环关卡生成 test-cases.md（`## 用例` + `## 审计缺陷`）
- 修复模式重入：不重建 test-cases.md，增量重跑 Failures + diff 新增影响的用例，结果就地更新并标记修复轮
- diff 映射回归用例（策略 A/B/C/D），自动升级全量
- 映射校验：闭环关卡 ↔ task-N-report 自审项 ↔ 用例三向对齐；code-audit 缺陷项不参与 task 映射
- HTTP 验证 + mvn test → 填 PASS/FAIL，报告映射结果（变更文件 / 已映射 / 未映射）

### 2. 代码审查

按 `docs/skills/engineering/code-review/SKILL.md` 执行。核心动作：

- 边界侦查 → 保护区检查 → 系统性审查（结构/逻辑/空安全/性能/安全/事务）
- P0/P1 发现 → 追加到 test-cases.md `## 审计缺陷`（独立编号 A1/A2…，不绑定 task），结果 `TODO`
- 保护区触碰 → BLOCKED, Route: human

### 3. 闭环审计与判定

按 `docs/skills/engineering/closure-audit/SKILL.md` 执行。核心动作：

- 加载上下文 → 逐项审计 → 发现按严重程度排序
- 审计结论写入 test-cases.md `## 审计记录` 的 `**审计结论:**`，必须与 Status 一致

判定状态机（Status 是结论，Route 是唯一路由依据，只允许以下三组组合）：

| Status | Route | 用例/缺陷结果 | 审计记录结论 | 去向 |
|--------|-------|---------------|-------------|------|
| DONE | done | 全部 PASS（无 FAIL/TODO） | 通过闭环审计 | process-management |
| BLOCKED | implement | 存在 FAIL/TODO（阶段或 code-audit） | 需要修改 | implement 修复模式 |
| BLOCKED | human | 保护区触碰 / 根因无法定位等 | —（未完成） | 暂停等人 |

## 红线

- 不得修改任何代码
- 不得跳过失败测试
- 不得声称通过但未实际运行
- 保护区触碰必须标记

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
- DONE：用例与审计缺陷全部 PASS 且无 FAIL/TODO：✓ / ✗
- BLOCKED/implement：Failures 已列出且全部可按来源分流：✓ / ✗
- 全部结果已填（PASS/FAIL/TODO，无空行）：✓ / ✗
::qa-check
```

任一 ✗ → 阶段不完整，留在 qa。全部 ✓ → 完成证明通过。

### 状态块输出

执行者将 `test-cases.md` `## 裁定` 汇总为状态块输出：

```
**Status:** DONE | BLOCKED
**Route:** done | implement | human
**Summary:** [用例 HTTP: N/N | 回归: N/N | 审计缺陷: N TODO]
```

状态块输出后阶段完成，进入退出路由分流。

## 退出路由

主 agent 读取 `test-cases.md` `## 裁定` 中的 `**Route:**` 字段（唯一路由依据），按以下表分流：

| Route | 去向 |
|-------|------|
| done | → process-management |
| implement | → implement（修复模式） |
| human | → 暂停，等待人类确认 |

等待人类确认后才进入下一阶段。禁止自动推进。
