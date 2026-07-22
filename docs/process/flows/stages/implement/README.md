# 实施与验证

## 文件角色

按计划执行 TDD 实施并完成全量验证，产出通过全部测试的代码变更。

## 进入条件

编码前全部满足，否则回退到收集/澄清阶段补全：

| # | 条件 | 验证方式 |
|---|------|---------|
| 1 | 已读取 `docs/baseline/context/conventions.md` 路由表 | 对话中输出匹配的文件清单 |
| 2 | 已逐条读取本次变更触发的全部 `standards/` 文件 | 同上 |
| 3 | 已读取 `docs/skills/engineering/tdd/SKILL.md` | 同上 |
| 4 | 已输出匹配摘要：文件名 + 触发场景 | 对话中可见 |
| 5 | 工具链就绪 | `cd tools && pnpm check` 通过（文档锚点、乱码、超大文件检查） |

> 第 5 条：工具链不存在或未安装时输出警告并继续。此条不阻断。

## 执行步骤

### 1. 注册与锚定

- 有计划：plan.md frontmatter `status` → `in-progress`，更新 `registry.md`
- 无计划（轻量路径）：跳过注册表更新
- 读取 plan.md（如有），锚定实施范围（触及面 + 公共契约 + 非目标）

### 2. TDD 循环

按 `docs/skills/engineering/tdd/SKILL.md` 执行 RED → GREEN → REFACTOR。

**红线**（违反即回退）：
- 生产代码先于测试写出
- 测试写完直接通过（从未 RED）
- 禁止从记忆或推理声称结果
- 占位符命令不等同于通过

每轮必须输出：

```
::tdd-cycle::[N]
  RED:  [测试名] → 失败原因正确 ✓
  GREEN: [最小实现] → 测试通过 ✓
  REFACTOR: [重构内容] → 全绿 ✓
```

### 3. 首次切片特殊处理

无测试框架时，首个切片搭建测试基础设施。搭建完成前以运行时验证矩阵为最低标准。

### 4. 验证

- 执行 `docs/baseline/context/project-context.md` 中全部验证命令，实际运行并捕获输出
- 加载 `docs/skills/engineering/verification-checklist/SKILL.md` 逐条执行

## 产出物

| 产出 | 验证 |
|------|------|
| 代码变更 | `git diff --stat` |
| 测试代码 | 新增测试文件存在于 diff 中 |
| 验证输出 | project-context.md 全部验证命令的实际输出 |
| 实施报告 | `docs/work/<branch>/implement-report.md` |

## 完成证明

阶段退出前，按执行顺序在对话中输出以下报告，同时写入 `docs/work/<branch>/implement-report.md`。缺任一段 → 阶段不完整，禁止退出。

```
::implement-report

## 1. 前置检查
[conventions.md 路由匹配结果：命中了哪些 standards/ 文件]

## 2. TDD 循环
[每轮 ::tdd-cycle:: 的汇总]
- 共 N 轮，全部 GREEN + REFACTOR 通过

## 3. 工具链检查
[cd tools && pnpm check 的实际输出]

## 4. 项目验证命令
[project-context.md 中每条验证命令的实际运行输出]

## 5. 验证检查清单
[verification-checklist/SKILL.md 逐条打勾结果，每条附命令输出摘要]

## 6. 产出物
| 路径 | 操作 |
|------|------|
| [文件1] | 新增/修改 |
| ... | ... |

## 7. 裁决
PASS / FAIL

::implement-report
```

报告双写：对话中输出 `::implement-report` 块（出口实时拦截），同时写入 `docs/work/<branch>/implement-report.md`（持久化，供 code-audit 和 closure 引用）。

PASS → 进入 code-audit。

FAIL → 留在 implement。不得以"稍后补"、"需外部环境"、"需 API Key"为由跳过任何段。

若因外部依赖阻塞（如 API Key 未配置），在第 4 段中标注 `BLOCKED`，plan.md 状态改 `blocked`，阶段不标记 completed。

## 退出路由

| 条件 | 去向 |
|------|------|
| 通过 + 有计划 | → code-audit |
| 通过 + 轻量路径 | → code-audit |
| 失败 + 设计缺陷 | → baseline（轻量路径先升档至单计划路径） |
| 失败 + 实施问题 | 留在 implement，修复后重走完成证明 |

