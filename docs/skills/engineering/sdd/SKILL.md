# SDD — Sub-agent Driven Development

> 用途：阶段 implement 的唯一实施方式。按阶段独立 dispatch 子代理执行 TDD。

## 何时使用

始终。所有实施任务均走 SDD 流程。

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

## SDD 流程

### 第 1 步：初始化

创建 `.sdd/` 目录，检查 `progress.md` 是否存在：
- 存在 → 匹配首行 plan 路径，找最后一条 complete/fix，从下一个 task 恢复（断点续传）
- 不存在 → 新建 `progress.md`（首次实施）

### 第 2 步：加载上下文

按上方「上下文加载」表 #1–#7 加载全部内容 + `progress.md`。

### 第 3 步：解析任务

从 `plan.md` 阶段列表生成 task 序列。识别 `[P]` 标记：
- 条件：`**依赖**` 为空 + 与同批 `[P]` 阶段 `**触及面**` 无重叠文件
- 满足 → 标记为可并行

### 第 4 步：按阶段 dispatch

组装 prompt（阶段段落 + 前置公共契约 + 闭环关卡），派发子代理执行 TDD。

每个子代理必须：
- TDD 循环（RED → GREEN → REFACTOR）
- **每步 Touchpoints 检查**：每次文件变更后对照阶段 `**触及面**`，超出 → STOP，标记 CONCERNS
- **高风险变更检测**：涉及部署配置、密钥、权限 → 生成 `.sdd/risk-gate.json`
- 逐条自审 plan.md 该阶段 `**闭环关卡**` 的 checkbox，打勾
- **提交代码**：`git add <变更文件>`（禁止 `git add -A`），约定式 commit message
- 写入 `task-N-report.md`（含 commit hash）

返回码处理：
- 子代理返回 `DONE` → `progress.md` 追加 → next
- 子代理返回 `CONCERNS`（触公共契约/Touchpoints）→ 指修 → next
- 子代理返回 `CONCERNS`（不触公共契约）→ 记 report → next（qa 兜底）
- 子代理返回 `BLOCKED` → 升级用户
- `[P]` 阶段同时 dispatch

### 第 5 步：汇总

controller 生成 `implement-report.md`，汇总全部 `task-N-report.md`（含 commit 范围）、风险门条目。同步 plan.md 阶段 status → `completed`，更新 `registry.md`。

### 第 6 步：偏离自检

对照 plan.md 逐项检查：

| 维度 | 处理 |
|------|------|
| 遗漏（plan 声明的阶段无对应 task-N-report） | 补充 dispatch |
| 超范围（代码变更超出触及面） | 可回退 → 回退 + 重跑；无法回退 → `::deviation` 等人类 |
| Touchpoints 违规 | 回退 + 重跑 TDD |

禁止静默偏离。全部通过 → 进入第 7 步。

### 第 7 步：退出

→ qa 阶段

## 模型选择

子代理 dispatch 时按阶段复杂度选模型：
- 简单 CRUD / 单文件 → 轻量模型
- 多文件 / 有契约约束 → 标准模型
- 复杂业务逻辑 / 跨模块 → 强模型

记录在 `task-N-report.md` 中。

## 自审规范

每个子代理完成 TDD 后，必须逐条自审 plan.md 中该阶段的 `**闭环关卡**`：

```markdown
## 自审
- [x] <闭环关卡 1> → <测试类名> PASS
- [x] <闭环关卡 2> → <测试类名> PASS
```

未打勾的闭环关卡 → 不得返回 DONE。

## Touchpoints 检查

每个子代理在每次文件变更后，对照阶段 `**触及面**` 检查：

```markdown
## Touchpoints 检查
- [x] 全部变更在触及面内 ✓
- <如有越界，列出违规文件和原因>
```

触及范围外 → STOP，标记 CONCERNS（触公共契约），不得返回 DONE。

## 风险门

涉及以下类型的变更 → 子代理生成 `.sdd/risk-gate.json`：

- 部署配置（application.yml、Dockerfile、k8s 配置）
- 密钥/凭证（JWT 密钥、API Key、数据库密码）
- 权限/鉴权逻辑
- 数据库 DDL

格式：
```json
{"change": "描述", "risk": "高", "verification": ["验证步骤"], "mustStopBeforeFinalize": true}
```

controller 汇总到 `implement-report.md` 风险段。qa 前人类必须确认。

## 修复模式

qa 裁定 `Route: implement` 时进入修复模式：

1. 读 `test-cases.md` FAIL/TODO 项，按来源分流：
   - 来源 = 阶段 N → dispatch 新子代理（阶段段落 + task-N-report + FAIL 项）
   - 来源 = code-audit → dispatch 新子代理（临时阶段号）
2. 子代理修复 → TDD → 提交 → `task-N-report.md` 追加 `Fix Round N` 段
3. `progress.md` 追加修复记录
4. `implement-report.md` 覆盖写入
5. → 再进 qa

修复轮不覆盖首次实现内容。修复子代理同样执行 Touchpoints 检查和风险门检测。

## 并行 `[P]` 规则

修复模式 `[P]` 仍适用：同轮修复中无文件冲突的阶段可并行 dispatch。

## 回路终止

```
qa Route: DONE → process-management（归档 + rm -rf .sdd/ + log）→ 流程结束
```

## progress.md 格式

```
# SDD ledger — plan: docs/work/<branch>/plan.md

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
<无则写"无"，有 risk-gate 则注明>

---
## Fix Round N
### 提交
<commit-hash> — <commit message>
### 修复内容
### TDD 证据
### 覆盖测试
### Touchpoints 检查
```

## .sdd/ 目录结构

```
docs/work/<branch>/.sdd/
├── progress.md           ← controller 写，commit 范围 + 修复轮次
├── task-N-report.md      ← 子代理写，自审详情 + TDD 证据 + commit hash
├── risk-gate.json        ← 子代理写（有则），高风险变更门控
└── review-final.diff     ← 终审 diff
```

生命周期：qa Route: DONE → process-management 执行 `.sdd/` 清理 → 流程结束

## 所需输入

- plan.md（阶段列表、触及面、闭环关卡）
- 上下文加载表 #1–#7

## 预期输出

- `.sdd/progress.md`
- `.sdd/task-N-report.md`（每阶段一个，含 commit hash）
- `.sdd/risk-gate.json`（有则）
- `implement-report.md`
- 代码变更（TDD 产出，已提交）

## 关联

- 实施阶段：`docs/process/flows/stages/implement/README.md`
- 子代理 prompt 模板：`docs/skills/engineering/sdd/implementer-prompt.md`
- QA 阶段：`docs/process/flows/stages/qa/README.md`
- TDD：`docs/skills/engineering/tdd/SKILL.md`
