# SDD 子代理驱动开发 — 融入 vibecode-flow 设计方案

> **决策**：不区分 inline/sdd 模式，SDD 是 implement 阶段的唯一实施方式。

## 一、架构总览

SDD 嵌入 implement-qa 回路，作为 implement 阶段的唯一执行路径。

```
plan-audit 通过
     │
     ▼
┌─ implement ──────────────────────────────────────────────────────────┐
│                                                                       │
│  第 1 步：初始化                                                      │
│    创建 .sdd/                                                         │
│    检查 progress.md → 有则恢复进度，无则新建                           │
│                                                                       │
│  第 2 步：加载上下文                                                  │
│    上下文表 #1–#7 + sdd/SKILL.md + progress.md                        │
│                                                                       │
│  第 3 步：解析任务                                                    │
│    plan.md 阶段列表 → task 序列                                       │
│    识别 [P]：依赖为空 + 触及面无重叠 → 并行                            │
│                                                                       │
│  第 4 步：按阶段 dispatch                                             │
│    组装 prompt（阶段段落 + 前置公共契约 + 闭环关卡）                    │
│    派发子代理 → TDD → Touchpoints 检查 → 自审 checkbox                │
│              → 高风险检测(risk-gate) → git commit                     │
│              → 写入 task-N-report.md（含 commit hash）                │
│    返回 DONE → progress.md 追加 → next                                │
│    返回 CONCERNS（触公共契约/Touchpoints）→ 指修 → next               │
│    返回 CONCERNS（不触公共契约）→ 记 report → next（qa 兜底）          │
│    返回 BLOCKED → 升级用户                                            │
│    [P] 阶段同时 dispatch                                              │
│                                                                       │
│  第 5 步：汇总                                                        │
│    controller → implement-report.md（含 commit 范围、风险门）          │
│    plan.md 阶段 status → completed                                    │
│    registry.md 同步                                                   │
│                                                                       │
│  第 6 步：偏离自检                                                    │
│    对照 plan.md：遗漏 → 补 dispatch                                   │
│                 超范围 → 回退或 ::deviation                           │
│                 Touchpoints 违规 → 回退 + 重跑                        │
│                                                                       │
│  第 7 步：退出 → qa                                                   │
└───────────────────────────────────────────────────────────────────────┘
     │
     ▼
┌─ qa ──────────────────────────────────────────────────────────────────┐
│  主 agent 执行：                                                      │
│    读取 plan.md 闭环关卡 + task-N-report.md + git diff                │
│    生成 test-cases.md（用例 + code-audit 发现）                        │
│    HTTP 验证 + 回归测试 + 代码审查                                     │
│    写入 ## 裁定                                                       │
│                                                                       │
│  裁定分流：                                                           │
│    Route: done      → process-management（归档 + rm .sdd/ + log）                 │
│    Route: implement → 回到 implement 修复模式                         │
│    Route: human     → 暂停等人                                        │
└───────────────────────────────────────────────────────────────────────┘
     │
     │  Route: implement
     ▼
┌─ implement 修复模式 ──────────────────────────────────────────────────┐
│  读 test-cases.md FAIL/TODO 项 → 按来源分流                           │
│    来源 = 阶段 N   → dispatch 新子代理（阶段段落 + task-N-report + FAIL 项）│
│    来源 = code-audit → dispatch 新子代理（临时阶段号）                 │
│  子代理修复 → TDD → Touchpoints → git commit                         │
│            → task-N-report.md 追加 Fix Round（含 commit hash）        │
│  progress.md 追加修复记录                                             │
│  implement-report.md 覆盖写入                                          │
│  → 再进 qa                                                           │
│                                                                       │
│  回路：implement → qa → implement → qa → ... → qa Route: done        │
└───────────────────────────────────────────────────────────────────────┘
```

---

## 二、文件格式

### 2.1 plan.md frontmatter

```yaml
---
branch: feature-xxx
status: planned
type: standalone
parent: none
requirement: requirement.md
created: 2026-07-30
updated: 2026-07-30
blocker: none
---
```

> `execution_mode` 已移除。所有计划统一走 SDD，无需字段区分。

### 2.2 plan.md 阶段模板

```markdown
## 阶段 1：[P] 用户数据层
- **依赖**：无
- **目标**：创建 UserRepository
- **触及面**：src/repository/UserRepository.java
- **公共契约**：UserRepository.save(User), UserRepository.findByEmail(String)
- **闭环关卡**：
  - [ ] save 后 findByEmail 能查到
  - [ ] 重复邮箱抛异常
```

`[P]` 条件：`**依赖**` 为空 + 与同批 `[P]` 阶段 `**触及面**` 无重叠文件。

### 2.3 .sdd/progress.md

```
# SDD ledger — plan: docs/work/feature-auth/plan.md

Task 1: complete (commits a1b2c3d..d4e5f6a)
Task 2: complete (commits d4e5f6a..b7c8d9e)
Task 3: complete (commits b7c8d9e..e0f1a2b)
Task 1: fix round 1 (commits e0f1a2b..f3c4d5e)
Task 3: fix round 1 (commits f3c4d5e..a6b7c8d)
Task 4: code-audit fix (commits a6b7c8d..d9e0f1a)
```

恢复逻辑：匹配首行 plan 路径 → 找到最后一条 complete/fix → 从下一个 task 继续。

### 2.4 .sdd/task-N-report.md

```
# Task 1 Report — 用户数据层

## 提交
a1b2c3d — feat(repo): add UserRepository with save and findByEmail

## 实现
UserRepository: save(User), findByEmail(String)

## 测试
3/3 PASS

## TDD 证据
### RED
mvn test → 3 FAIL（UserRepository 未定义）
### GREEN
mvn test → 3 PASS

## 自审
- [x] save 后 findByEmail 能查到 → UserRepositoryTest PASS
- [x] 重复邮箱抛 DuplicateEmailException → UserRepositoryTest PASS

## Touchpoints 检查
- [x] 全部变更在触及面内 ✓

## 关注点
无

---
## Fix Round 1

### 修改依据
- 触发项：#2（test-cases.md `## 用例`）| 原结果：FAIL（阶段 1）
- 失败证据：RED 输出（异常类型不匹配）

### 提交
f3c4d5e — fix(repo): use DuplicateEmailException instead of RuntimeException

### 覆盖用例
#2

### 修复内容
RuntimeException → DuplicateEmailException

### TDD 证据
RED: mvn test → shouldThrowOnDuplicateEmail FAIL（异常类型不匹配）
GREEN: mvn test → 3/3 PASS

### 覆盖测试
UserRepositoryTest#shouldThrowOnDuplicateEmail

### Touchpoints 检查
- [x] 修复在触及面内 ✓
```

修复轮追加 Fix Round N 段，不覆盖首次实现内容。每轮含独立 commit。

### 2.5 implement-report.md

```
**Status:** DONE
**Summary:** 3 阶段完成，11/11 单元测试通过，编译通过

### 阶段 1：用户数据层
- 提交：a1b2c3d..d4e5f6a（修复：f3c4d5e）
- 测试：3/3 PASS

### 阶段 2：注册接口
- 提交：d4e5f6a..b7c8d9e
- 测试：4/4 PASS

### 阶段 3：登录接口
- 提交：b7c8d9e..e0f1a2b（修复：a6b7c8d）
- 测试：4/4 PASS

## 风险门

| 变更 | 风险 | 验证步骤 |
|------|------|---------|
| <有则列出，无则写"无"> | — | — |
```

### 2.6 test-cases.md

```
# 测试用例 — feature-auth

## 用例

| # | 来源 | 关联 task | 用例 | 类型 | 结果 |
|---|------|-----------|------|------|------|
| 1 | 阶段 1 | task-1-report.md | save 后 findByEmail 能查到 | HTTP | PASS |
| 2 | 阶段 1 | task-1-report.md | 重复邮箱抛 DuplicateEmailException | HTTP | PASS |
| 3 | 阶段 2 | task-2-report.md | 新邮箱 → 201 + JWT | HTTP | PASS |
| 4 | 阶段 2 | task-2-report.md | 空邮箱 → 400 | HTTP | PASS |
| 5 | 阶段 3 | task-3-report.md | 正确密码 → 200 + JWT | HTTP | PASS |
| 6 | 阶段 3 | task-3-report.md | 错误密码 → 401 | HTTP | PASS |

## 审计缺陷

| # | 严重级别 | 问题 | 范围 | 结果 | 修复轮 |
|---|---------|------|------|------|--------|
| A1 | P0 | 密码未哈希存储 | 全局 | PASS | R1 |

## 修复历史

| # | 轮次 | 提交 | 结果 |
|---|------|------|------|
| A1 | R1 | f3c4d5e | PASS（R1） |

## 裁定

**Status:** DONE
**Route:** done
**Summary:** 用例 HTTP: 6/6 | 回归: 14/14 | 审计缺陷: 0 TODO

## 审计记录

**审计结论:** 通过闭环审计

| 日期 | 阶段 | 操作/发现 |
|------|------|---------|
```

---

## 三、.sdd/ 目录结构

```
docs/work/<branch>/.sdd/
├── progress.md           ← controller 写，commit 范围 + 修复轮次
├── task-N-report.md      ← 子代理写，自审详情 + TDD 证据 + commit hash
├── risk-gate.json        ← 子代理写（有则），高风险变更门控
└── review-final.diff     ← 终审 diff
```

生命周期：qa Route: done → process-management 执行 `.sdd/` 清理 → 流程结束

---

## 四、改动文件清单

| # | 文件 | 操作 | 内容 |
|---|------|------|------|
| 1 | `docs/skills/engineering/sdd/SKILL.md` | 新建 | SDD 流程、上下文加载表、模型选择、自审规范、Touchpoints、风险门、修复模式 |
| 2 | `docs/skills/engineering/sdd/implementer-prompt.md` | 新建 | 子代理 dispatch 模板：阶段段落+TDD+Touchpoints+risk-gate+commit+report 格式 |
| 3 | `docs/process/flows/stages/implement/README.md` | 修改 | 替换为 SDD 流程（7 步），含上下文加载表、偏离自检、风险门 |
| 4 | `docs/process/flows/stages/plan/template-standalone.md` | 修改 | 移除 `execution_mode`，`[P]` 始终生效 |
| 5 | `docs/skills/README.md` | 修改 | engineering/ 注册表加 sdd 条目 |

---

## 五、不变的部分

- qa 阶段（qa-agent 定义已合并入 qa/README.md，不委派子代理）
- test-cases.md 生成逻辑
- plan-audit / process-management
- 前置路由、意图检测
- baseline 体系

> `execute-agent.toml` 定义未修改，但 SDD 作为唯一实施路径后不再路由到该 agent。保留文件供参考。

---

## 六、并行 `[P]` 规则

```
[P] 条件：依赖为空 + 与同批 [P] 触及面无重叠
修复模式 [P] 仍适用：同轮修复中无文件冲突的阶段可并行 dispatch
```

---

## 七、回路终止

```
qa Route: done → process-management（归档 + rm -rf .sdd/ + log）→ 流程结束
```
