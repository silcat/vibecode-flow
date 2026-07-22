# 代码审计

## 文件角色

独立审查代码质量，产出 P0-P3 分级发现。实施者不得自审——本阶段由独立 agent 执行。

## 进入条件

| # | 条件 | 验证方式 |
|---|------|---------|
| 1 | implement stage 已通过 | implement-report.md 存在且裁决 PASS |
| 2 | 变更文件列表可获取 | `git diff --name-only main...HEAD` 或等价命令 |
| 3 | 已读取 `docs/skills/audit/code-quality-audit-prompt.md` | 对话中确认 |

## 执行步骤

### 1. 获取变更范围

从 implement-report.md 或 `git diff` 获取本次变更的文件列表。

### 2. 机械扫描

```bash
cd tools && pnpm audit:suspects
```

逐条输出发现。exit code ≠ 0 的记录到报告中（工具当前不设 exit 1，发现以输出形式呈现）。

### 3. 语义审查

按 `docs/skills/audit/code-quality-audit-prompt.md` 的 7 个聚焦领域，逐文件审查：

1. 架构和边界完整性
2. 核心实现正确性
3. 类型和契约质量
4. 错误处理和操作安全
5. 测试有效性
6. 可维护性和未来变更风险
7. 自动化和护栏覆盖

### 4. 分级

| 级别 | 定义 | 处理 |
|------|------|------|
| P0 | 数据丢失、安全破坏、生产停服 | 阻断，必须修复 |
| P1 | 用户可见的正确性 Bug 或强回归风险 | 阻断，必须修复 |
| P2 | 可维护性或测试缺口，可能导致未来回归 | 记录，建议修复 |
| P3 | 低风险小质量问题 | 记录，可选修复 |

P0/P1 发现 → 退回 implement 修复，修复后重新进入本阶段。

## 产出物

| 产出 | 路径 |
|------|------|
| 代码审计报告 | `docs/work/<branch>/code-audit.md` |

## 完成证明

阶段退出前，在对话中输出以下报告，同时写入 `docs/work/<branch>/code-audit.md`。

```
::code-audit-report

## 1. 变更范围
[git diff --stat 摘要]

## 2. 机械扫描
[cd tools && pnpm audit:suspects 的实际输出]

## 3. 语义发现
### P0（阻塞）
[N 条，逐条列出：文件:行号 + 问题描述 + 修复建议]

### P1（阻塞）
[N 条，同上]

### P2（建议修复）
[N 条，同上]

### P3（可选）
[N 条，同上]

## 4. 正向观察
[值得保留的做法]

## 5. 裁决
PASS（P0/P1 已清零） / FAIL（P0/P1 未清零）

::code-audit-report
```

PASS → 进入 closure。

FAIL → 退回 implement。退回时携带完整发现列表，实施者修复 P0/P1 后重新进入本阶段（不重跑 PASS 的条目）。

无发现（P0-P3 均为 0）→ 明确声明"无发现"，列出检查过的残余缺口。

## 退出路由

| 条件 | 去向 |
|------|------|
| PASS + 有计划 | → closure |
| PASS + 轻量路径 | → log |
| FAIL | → implement（携带 P0/P1 发现列表） |
