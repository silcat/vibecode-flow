# 实施与验证

## 文件角色

按计划在主线程内直接执行 TDD 实施，产出通过全部单元测试的代码变更。支持首次实施和修复重入。
## 进入条件

| # | 条件 |
|---|------|
| 1 | plan.md 存在 |

## 上下文加载

编码前按顺序加载，缺一不可：

| # | 加载内容 | 说明 |
|---|---------|------|
| 1 | `docs/baseline/context/README.md` → 路由表 | 按场景加载 project-context.md, conventions.md, codebase-map.md |
| 2 | `docs/baseline/context/conventions.md` → 规范路由表 | 匹配本次变更触发的 standards/ 文件 |
| 3 | 触发的全部 `docs/baseline/standards/` 文件 | 逐条读取 |
| 4 | `docs/work/<branch>/plan.md` | 阶段列表、触及面、闭环关卡 |
| 5 | 输出匹配摘要（文件名 + 触发场景） | 对话中可见 |

## 实施流程

### 第 0 步：模式判定

检查 `test-cases.md` 是否存在且 `## 裁定` 中 `**Route:** implement`：

- 是 → 修复模式。跳到 `§修复模式` 执行
- 否 → 首次实施。继续第 1 步

### 第 1 步：初始化

创建 `.implement/` 目录，检查 `progress.md` 是否存在：
- 存在 → 匹配首行 plan 路径，找最后一条 complete/fix，从下一个 task 恢复（断点续传）
- 不存在 → 新建 `progress.md`（首次实施）

### 第 2 步：加载上下文

按上方「上下文加载」表 #1–#5 加载全部内容 + `progress.md`。

### 第 3 步：解析任务

从 `plan.md` 阶段列表生成 task 序列。`[P]` 标记阶段在主线程内顺序执行。

### 第 4 步：按阶段执行

按阶段顺序直接执行 TDD。

每个阶段必须：
- TDD 循环（RED → GREEN → REFACTOR）
- **每步 Touchpoints 检查**：每次文件变更后对照阶段 `**触及面**`，超出 → 停止，记录关注点
- **高风险变更检测**：涉及部署配置、密钥、权限 → 在 task-N-report 关注点段标注，格式见 §风险门
- 逐条自审 plan.md 该阶段 `**闭环关卡**` 的 checkbox，打勾
- **提交代码**：`git add <变更文件>`（禁止 `git add -A`），约定式 commit message
- 写入 `task-N-report.md`（含 commit hash）

阻断处理：
- 触公共契约/Touchpoints → 修复后继续
- 不触公共契约的关注点 → 记 report，继续（qa 兜底）
- 无法继续 → 升级用户

### 第 5 步：汇总

同步 plan.md 阶段 status → `completed`，更新 `registry.md`。在对话中输出风险门汇总（如有）。

### 第 6 步：偏离自检

对照 plan.md 逐项检查：

| 维度 | 检查项 | 处理 |
|------|--------|------|
| 遗漏 | plan 声明的阶段是否全部有对应 task-N-report | 补充执行缺失阶段 |
| 超范围 | 代码变更是否超出 plan 声明的触及面 | 可回退 → 回退 + 重跑 TDD；无法回退 → 标 DEVIATION，等人类确认 |
| Touchpoints 违规 | 是否触碰了禁止触及的模块/端点 | 回退违规改动 + 重跑 TDD |

可自行修复的偏离修完后回到步骤 4 补执行。无法自行修复 → 输出 `::deviation` 阻断退出，等人类确认。禁止静默偏离。

### 第 7 步：退出

→ qa 阶段

## 修复模式

第 0 步判定为修复模式时进入。

### F1：加载失败清单

读 `test-cases.md`，提取全部 FAIL/TODO 项，按来源分流：

| 来源 | 含义 | 处理 |
|------|------|------|
| 阶段 N | 某个实现阶段的问题 | 定位对应的 `task-N-report.md` |
| code-audit | 代码审查发现（test-cases.md `## 审计缺陷`，编号 A<N>） | 创建临时 task 号 |

### F2：逐项修复

按来源分组，每组执行：

1. 读对应 `task-N-report.md` 了解原有实现
2. TDD 修复：RED（确认失败复现）→ GREEN（修复通过）
3. Touchpoints 检查：修复变更不得超出原阶段触及面
4. 风险门检测：修复引入高风险变更 → 在 Fix Round 中标注
5. `git add <修复文件>`，提交，记录 commit hash
6. 在 `task-N-report.md` 末尾追加 `Fix Round N` 段（不覆盖原有内容），段内必须写 `### 修改依据`（触发项 + 失败证据）和 `### 覆盖用例`（test-cases.md 用例/缺陷 #id）

阻断处理：
- 无法定位根因 → 标注 `::deviation`，等待人类
- Touchpoints 越界 → 回退，记录关注点
- 修复引入新失败 → 继续修复直到全绿

### F3：同步状态

1. `progress.md` 追加修复记录：`Task N: fix round M (commits <hash>)`
2. 对话输出修复摘要（含风险门如有）

### F4：退出

直接跳到 §完成证明，输出 `::implement-check`（含修复轮校验），通过后 → qa。

## 自审规范

每个阶段完成 TDD 后，必须逐条自审 plan.md 中该阶段的 `**闭环关卡**`：

```markdown
## 自审
- [x] <闭环关卡 1> → <测试类名> PASS
- [x] <闭环关卡 2> → <测试类名> PASS
```

未打勾的闭环关卡 → 不得进入下一阶段。

## Touchpoints 检查

每个阶段在每次文件变更后，对照阶段 `**触及面**` 检查：

```markdown
## Touchpoints 检查
- [x] 全部变更在触及面内 ✓
- <如有越界，列出违规文件和原因>
```

触及范围外 → 停止，修复后继续。

## 风险门

涉及以下类型的变更 → 在 `task-N-report.md` 关注点段标注：

- 部署配置（application.yml、Dockerfile、k8s 配置）
- 密钥/凭证（JWT 密钥、API Key、数据库密码）
- 权限/鉴权逻辑
- 数据库 DDL

标注格式：

```
## 关注点
### 风险门
| 变更 | 风险 | 验证步骤 |
|------|------|---------|
| <描述> | 高 | <步骤> |
```

全部阶段完成后，在对话中汇总输出所有风险门条目。qa 阶段前人类必须确认。





## 产出物

| 产出 | 路径 |
|------|------|
| 代码变更 | `git diff --stat`（已提交） |
| 单元测试 | `src/test/java/` 新增/修改文件 |
| 进度追踪 | `docs/work/<branch>/.implement/progress.md` |
| 阶段报告 | `docs/work/<branch>/.implement/task-N-report.md` |
| 终审 diff | `docs/work/<branch>/.implement/review-final.diff` |

## progress.md 格式

```
# 实施 ledger — plan: docs/work/<branch>/plan.md

Task 1: complete (commits a1b2c3d..d4e5f6a)
Task 2: complete (commits d4e5f6a..b7c8d9e)
Task 1: fix round 1 (commits e0f1a2b..f3c4d5e)
```

## task-N-report.md 格式

```
# Task N Report — <阶段名>

## 提交
<commit-hash> — <commit message>

## 实现
<实现摘要>

## 测试
<N>/<M> PASS

## TDD 证据
### RED
<失败测试输出>
### GREEN
<通过测试输出>

## 自审
- [x] <闭环关卡> → <测试名> PASS

## Touchpoints 检查
- [x] 全部变更在触及面内 ✓

## 关注点
<无则写"无">
<有风险门变更则按风险门标注格式>

---
## Fix Round N
### 修改依据
- 触发项：#<id>（test-cases.md `## 用例` / `## 审计缺陷`）| 原结果：<FAIL/TODO>（<来源>）
- 失败证据：<RED 输出 / 审查发现摘要>
### 提交
<commit-hash> — <commit message>
### 覆盖用例
#<id>
### 修复内容
### TDD 证据
### 覆盖测试
### Touchpoints 检查
```

## .implement/ 目录结构

```
docs/work/<branch>/.implement/
├── progress.md           ← 写入 commit 范围 + 修复轮次
├── task-N-report.md      ← 每阶段写，自审详情 + TDD 证据 + commit hash
└── review-final.diff     ← 终审 diff
```

生命周期：qa Route: done → process-management 执行 `.implement/` 清理 → 流程结束

## 完成证明

### 文件存在

确认 `.implement/` 目录下所有阶段的 `task-N-report.md` 均存在。

### 内容校验

汇总全部 `task-N-report.md`，在对话中输出自检：

```
::implement-check
- 测试通过率：[N]/[M]（全绿 ✓ / 未全绿 ✗）
- 覆盖率：行覆盖 [X]% / 分支覆盖 [Y]%
- 验收标准覆盖：[N]/[M] 条 AC 已验证（对照 plan.md 闭环关卡逐条映射）
- 偏离自检：遗漏 0 / 超范围 0 / Touchpoints 违规 0 ✓
- 风险门：N 条待确认 / 0 条待确认 ✓
- task-N-report.md 含 Failures / TODO / 占位符：否 ✓ / 是 ✗
::implement-check
```

任一 ✗ → 阶段不完整，留在 implement 修复。全部 ✓ → 完成证明通过。

> 修复模式重入：追加校验 `test-cases.md` `## 裁定` 中的 Failures 全部在对应 task-N-report.md 的 Fix Round 中有 `### 覆盖用例` 记录（#id 对应）且测试通过；code-audit 缺陷修复记录在临时 task report。未覆盖的 Failure → ✗，留在 implement。

## 退出路由

| 条件 | 去向 |
|------|------|
| 通过 | → qa |
| 失败 | 留在 implement |
| 偏离未解决 | 阻断，等待人类 |

等待人类确认后才进入下一阶段。禁止自动推进。
