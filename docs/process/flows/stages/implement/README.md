# 实施与验证

## 文件角色

按计划执行 TDD 实施，产出通过全部单元测试的代码变更。支持首次实施和修复重入两种模式。

## 执行步骤

本阶段由 `.codex/agents/execute-agent.toml` 执行。实施细节见 Agent 定义文件。

| # | 步骤 |
|---|------|
| 1 | 检查 `test-cases.md` 是否存在 → 有则读取 `## 裁定` 提取 Failures 进入修复模式，无则首次实施 |
| 2 | 委派 `.codex/agents/execute-agent.toml` 执行 |
| 3 | 等待子 agent 完成，读取 `implement-report.md` |
| 3a | 校验 `implement-report.md` 格式合法：非空、含测试结果统计、非乱码/崩溃输出。不合法 → 留在 implement，标记产出异常，重新委派或等待人类介入 |
| 4 | 通过 → qa，失败 → 留在 implement |

## 进入条件

| # | 条件 |
|---|------|
| 1 | plan.md 存在 |

## 修复模式

qa 回退时（`test-cases.md` `## 裁定` Route = implement），execute-agent 读取 `test-cases.md`，提取 Failures，逐条 bug分析：明显问题直接 TDD 修复，需根因分析的先 vc:debug → 重新输出 `implement-report.md`。

## 产出物

| 产出 | 路径 |
|------|------|
| 代码变更 | `git diff --stat` |
| 单元测试 | `src/test/java/` 新增/修改文件 |
| 实施报告 | `docs/work/<branch>/implement-report.md` |

## 完成证明

### 文件存在

`Test-Path docs/work/<branch>/implement-report.md` 确认文件存在。

### 内容校验

读取 `implement-report.md`，在对话中输出自检：

```
::implement-check
- 测试通过率：[N]/[M]（全绿 ✓ / 未全绿 ✗）
- 覆盖率：行覆盖 [X]% / 分支覆盖 [Y]%
- 验收标准覆盖：[N]/[M] 条 AC 已验证（对照 plan.md 闭环关卡逐条映射）
- implement-report.md 含 Failures / TODO / 占位符：否 ✓ / 是 ✗
::implement-check
```

任一 ✗ → 阶段不完整，留在 implement 修复。全部 ✓ → 完成证明通过。

> 修复模式重入：追加校验 `test-cases.md` `## 裁定` 中的 Failures 全部在本轮 implement-report.md 中有对应修复记录且测试通过。未覆盖的 Failure → ✗，留在 implement。

## 退出路由

| 条件 | 去向 |
|------|------|
| 通过 | → qa |
| 失败 | 留在 implement |

等待人类确认后才进入下一阶段。禁止自动推进。