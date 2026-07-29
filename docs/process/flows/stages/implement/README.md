# 实施与验证

## 文件角色

按计划以 SDD（子代理驱动开发）模式执行 TDD 实施，产出通过全部单元测试的代码变更。支持首次实施和修复重入。

## 上下文加载

编码前按顺序加载，缺一不可：

| # | 加载内容 | 说明 |
|---|---------|------|
| 1 | `docs/baseline/context/README.md` → 路由表 | 按场景加载 project-context.md, conventions.md, codebase-map.md |
| 2 | `docs/baseline/context/conventions.md` → 规范路由表 | 匹配本次变更触发的 standards/ 文件 |
| 3 | 触发的全部 `docs/baseline/standards/` 文件 | 逐条读取 |
| 4 | `docs/skills/engineering/sdd/SKILL.md` | SDD 方法论 |
| 5 | `docs/skills/engineering/sdd/implementer-prompt.md` | 子代理 dispatch 模板 |
| 6 | `docs/work/<branch>/plan.md` | 阶段列表、触及面、闭环关卡 |
| 7 | 输出匹配摘要（文件名 + 触发场景） | 对话中可见 |

## 执行步骤

本阶段按 SDD 流程 dispatch 子代理。详见 [sdd/SKILL.md](../../../../skills/engineering/sdd/SKILL.md)。

| # | 步骤 |
|---|------|
| 1 | 初始化：创建 `.sdd/` 目录，检查 `progress.md` → 有则恢复进度，无则新建 |
| 2 | 加载上下文：按上方「上下文加载」表 #1–#7 + progress.md |
| 3 | 解析任务：从 plan.md 阶段列表生成 task 序列，识别 `[P]` 可并行阶段 |
| 4 | 按阶段 dispatch 子代理 → TDD → 自审 plan.md 闭环关卡 checkbox → Touchpoints 检查 → git commit → 写入 task-N-report.md |
| 5 | 汇总：生成 implement-report.md，同步 plan.md 阶段 status，更新 registry.md |
| 6 | 偏离自检：对照 plan.md 逐项检查遗漏/超范围/Touchpoints 违规 |
| 7 | 退出 → qa |

## 进入条件

| # | 条件 |
|---|------|
| 1 | plan.md 存在 |

## 修复模式

qa 裁定 `Route: implement` 时进入修复模式：

1. 读 `test-cases.md` FAIL/TODO 项，按来源分流：
   - 来源 = 阶段 N → dispatch 新子代理（阶段段落 + task-N-report + FAIL 项）
   - 来源 = code-audit → dispatch 新子代理（临时阶段号）
2. 子代理修复 → TDD → git commit → task-N-report.md 追加 Fix Round 段
3. progress.md 追加修复记录
4. implement-report.md 覆盖写入
5. → 再进 qa

详见 [sdd/SKILL.md](../../../../skills/engineering/sdd/SKILL.md) §修复模式。

## 偏离自检

全部 task 完成后、退出 qa 前，对照 plan.md 逐项自检：

| 维度 | 检查项 | 处理 |
|------|--------|------|
| 遗漏 | plan 声明的阶段是否全部有对应 task-N-report | 补充 dispatch 缺失阶段 |
| 超范围 | 代码变更是否超出 plan 声明的触及面 | 可回退 → 回退 + 重跑 TDD；无法回退 → 标 DEVIATION，等人类确认 |
| Touchpoints 违规 | 是否触碰了禁止触及的模块/端点 | 回退违规改动 + 重跑 TDD |

可自行修复的偏离修完后回到步骤 4 补 dispatch。无法自行修复 → 输出 `::deviation` 阻断退出，等人类确认。禁止静默偏离。

## 风险门

dispatch 步骤中发现子代理产出了 `risk-gate.json`（部署配置、密钥、权限相关变更）→ 汇总到 implement-report.md 的风险段：

```
## 风险门
| 变更 | 风险 | 验证步骤 |
|------|------|---------|
| <描述> | 高 | <步骤> |
```

qa 阶段前人类必须确认所有 risk-gate 条目。

## 产出物

| 产出 | 路径 |
|------|------|
| 代码变更 | `git diff --stat`（已提交） |
| 单元测试 | `src/test/java/` 新增/修改文件 |
| SDD ledger | `docs/work/<branch>/.sdd/progress.md` |
| 阶段报告 | `docs/work/<branch>/.sdd/task-N-report.md` |
| 风险门 | `docs/work/<branch>/.sdd/risk-gate.json`（有则） |
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
- 偏离自检：遗漏 0 / 超范围 0 / Touchpoints 违规 0 ✓
- 风险门：N 条待确认 / 0 条待确认 ✓
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
| 偏离未解决 | 阻断，等待人类 |

等待人类确认后才进入下一阶段。禁止自动推进。
