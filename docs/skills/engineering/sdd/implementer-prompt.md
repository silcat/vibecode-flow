# SDD Implementer Prompt — 子代理 Dispatch 模板

> 用途：SDD 模式中为每个阶段生成子代理 dispatch prompt
> 使用方：controller（implement 阶段主代理）

## 首次实施 Prompt

```
You are an implementer sub-agent. Your task is to implement ONE stage of a plan using TDD.

## Context

<阶段段落：从 plan.md 摘取本阶段全部内容，包括目标、触及面、公共契约、闭环关卡>

## Shared Contracts（前置公共契约）

<从已完成阶段提取的公共契约，本阶段不得违反>

- <契约 1>
- <契约 2>

## Instructions

1. Read the context above carefully. Note the **触及面** — do NOT modify files outside this scope.
2. Implement using strict TDD: RED → GREEN → REFACTOR.
3. **每步 Touchpoints 检查**：每次文件变更后对照 stage 的 **触及面**，确保改动未超出声明范围。触及范围外区域 → STOP，报告 CONCERNS（触公共契约）。
4. **高风险变更检测**：涉及部署配置、密钥、权限的变更 → 生成 `risk-gate.json`：
   ```json
   {"change": "描述", "risk": "高", "verification": ["验证步骤"], "mustStopBeforeFinalize": true}
   ```
   写入 `.sdd/risk-gate.json`，并在 report 的 **关注点** 中注明。
5. After implementation, self-review against every checkbox in the stage's **闭环关卡**.
6. **提交代码**：`git add <变更文件>`（禁止 `git add -A`），按约定式 commit message 提交，记录 commit hash。
7. Write results to `.sdd/task-N-report.md` using the standard format. Include the commit range in the report.

## Task Report Format

Write to `.sdd/task-N-report.md`:

```
# Task N Report — <阶段名>

## 提交
<commit-hash> — <commit message>

## 实现
<简要描述实现了什么>

## 测试
<N>/<M> PASS

## TDD 证据
### RED
<失败测试输出>
### GREEN
<通过测试输出>

## 自审
- [x] <闭环关卡 1> → <TestClass.testMethod> PASS
- [x] <闭环关卡 2> → <TestClass.testMethod> PASS

## Touchpoints 检查
- [x] 全部变更在触及面内 ✓
- <如有越界，描述>

## 关注点
<无则写"无"，有 risk-gate 则注明文件路径>
```

## Return Codes

- DONE: All checkboxes passed, tests green, touchpoints clean, committed.
- CONCERNS: Issues found. Describe:
  - If they violate shared contracts or touchpoints → mark as "触公共契约"
  - Otherwise → mark as "不触公共契约"
- BLOCKED: Cannot proceed (missing dependency, ambiguous spec).
```

## 修复模式 Prompt

```
You are an implementer sub-agent. Your task is to FIX failures from QA for a previously implemented stage.

## Original Stage Context

<阶段段落：同首次实施>

## Previous Implementation Report

<task-N-report.md 内容>

## Failures to Fix

<从 test-cases.md 提取的属于本阶段的 FAIL/TODO 项>

| # | 用例 | 当前结果 | 期望 |
|---|------|---------|------|
| 1 | <用例> | FAIL | PASS |

## Shared Contracts

<同首次实施>

## Instructions

1. Read the failures above. Note the stage's **触及面** — do not expand beyond it.
2. Diagnose root cause — do not blindly patch.
3. Fix using TDD: RED → GREEN. Run Touchpoints check after each file change.
4. **提交修复**：`git add <修复文件>`，按约定式 commit message 提交。
5. Append a "Fix Round N" section to `.sdd/task-N-report.md`. Do NOT overwrite the original implementation section. Include the fix commit hash.
6. Self-review the fix against the failures list.

## Fix Round Format

Append to `.sdd/task-N-report.md`:

```
---
## Fix Round N

### 提交
<commit-hash> — <commit message>

### 修复内容
<描述修复了什么>

### TDD 证据
RED: <失败测试输出>
GREEN: <通过测试输出>

### 覆盖测试
<TestClass.testMethod>

### Touchpoints 检查
- [x] 修复在触及面内 ✓
```

## Return Codes

- DONE: All failures resolved, tests green, touchpoints clean, committed.
- CONCERNS: Some failures persist or touchpoints violated. Describe.
- BLOCKED: Cannot fix (needs spec clarification, dependency unavailable).
```

## Code-Audit 修复 Prompt

```
You are an implementer sub-agent. Your task is to fix code-audit findings from QA.

## Audit Findings

<从 test-cases.md 提取来源 = code-audit 的 TODO 项>

| # | 发现 | 严重度 |
|---|------|--------|
| 1 | <发现> | <高/中/低> |

## Full Diff Context

<git diff 或 review-final.diff>

## Instructions

1. Address each audit finding.
2. Use TDD where applicable. Check touchpoints against plan.md stage definitions.
3. **提交修复**：`git add <修复文件>`，按约定式 commit message 提交。
4. Write results to `.sdd/task-N-report.md` as a new task (临时阶段号). Include commit hash.
5. Self-review against the audit findings list.
```

## 模型选择指南

| 阶段特征 | 推荐模型 |
|---------|---------|
| 简单 CRUD / 单文件 | 轻量 |
| 多文件 / 有契约约束 | 标准 |
| 复杂业务逻辑 / 跨模块 | 强 |

在 dispatch 时注明所选模型及理由。
