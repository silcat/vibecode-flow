# 注册表维护与中断恢复

## 注册表维护

> `docs/work/registry.md` 只展示进度，所有维护规则集中在本节。

### 注册

- collect 阶段创建 `docs/work/<目录>/` 时 → 新增工作项行，状态 `in-progress`，流程节点 `collect`，实施节点 `—`
- 类型按所属流程判定：快速模式 → `轻量`；单计划 → `standalone`；多计划 → `master`（子计划另行注册 `sub`，缩进于父计划下，父计划列填父计划目录名）
- Bug 路由 → 类型 `bug`，按 [bug.md](../../bug.md) 的阶段步骤恢复
- plan 阶段 → 更新类型/状态字段（与 plan.md frontmatter 同步），不重复注册

### 流程节点

- 每个阶段完成证明通过、人类确认进入下一阶段时 → 更新流程节点为下一阶段名
- 同阶段重入（修复/回退）→ 流程节点不变
- 流程节点取值 = `docs/process/flows/stages/<stage>/` 目录名：collect / clarify / requirement / investigate / solution / scope / baseline / requirement-audit / project / plan / plan-audit / implement / qa / process-management
- 轻量/无计划工作项同样按阶段推进，流程节点照常更新

### 实施节点

- implement 阶段每完成一个 task（commit 后）、任务中断或修复轮 → 同步为 `progress.md` 台账尾行快照（如 `Task 2: complete` / `Task 2: in-progress` / `Task 1: fix round 1`）
- 未进入实施 → `—`

### 状态

- 状态取值：`planned` / `in-progress` / `paused` / `blocked` / `completed`。计划前工作项一般为 `in-progress` / `paused` / `blocked`
- 工作项暂停/阻塞时 → 状态改为 `paused` 或 `blocked`（若当前激活为该工作项则清除"当前激活"）
- 子计划完成时 → 状态改为 `completed`，同时：
  1. 检查同父计划下所有兄弟子计划是否均已完成
  2. 全部完成 → 在注册表末尾追加 `[ACTION] <父计划目录> 所有子计划已完成，人类确认后触发集成审计`
  3. 未全部完成 → 检查依赖图中下一个未完成子计划的前置条件是否满足，满足则追加 `NEXT: <子计划目录>`

### 注销

- 独立工作项完成 → 闭环审计通过后，状态改为 `completed`，清除"当前激活"，行保留不删
- 主计划完成 → 所有子计划 completed + 集成审计通过后，状态改为 `completed`，清除"当前激活"
- 归档动作由 process-management 阶段执行

### 字段来源

| registry 列 | 来源 | 映射 |
|------------|------|------|
| 工作项 | — | 目录名，人工指定 |
| 类型 | frontmatter `type` | 计划前按流程判定：`轻量` / `bug` / `standalone`（`type: standalone`）/ `sub`（`type: sub`）/ `master`（`type: master`） |
| 父计划 | frontmatter `parent` | 子计划从 `parent` 提取父目录名；其他 → `—` |
| 状态 | frontmatter `status` | 计划阶段直接同步；计划前一般为 `in-progress` / `paused` / `blocked` |
| 流程节点 | 人工/AI 每阶段出口更新 | 当前阶段目录名，无则 `—` |
| 实施节点 | `progress.md` 台账尾行 | 每 task commit / 任务中断 / 修复轮完成后同步，如 `Task 2: complete` |
| 自治 | 手动设置 | 唯一控制点。取值：`implement` / `plan-first` / `ask-first` / `research-only` / `blocked`。默认值：`implement`。含义见 `docs/baseline/context/ai-autonomy-policy.md` |
| 更新 | frontmatter `updated` | 直接同步 |

---

## 中断恢复

人类触发恢复（"继续"/关键词）后，AI 按本章独立完成，不反复询问。

### 恢复入口

1. 读 `docs/work/registry.md`
2. 若"当前激活"有效 → 优先定位该工作项（人类的快捷书签）
3. 否则 → 扫描全部行，找 `status: in-progress` 的工作项，按 `updated` 倒序取第一条
4. 仍无匹配 → 找 `status: paused` 的工作项，同上
5. 仍无匹配 → 报告"无可恢复工作项"，停止

### 按流程节点分流

| 流程节点 | 恢复依据 | 动作 |
|---------|---------|------|
| collect / clarify / requirement / investigate / solution / scope / baseline / requirement-audit / project / plan-audit | work 目录产物（discussion.md / requirement.md / investigate.md / scope-analysis.md …） | 读对应 `stages/<stage>/README.md` 的进入条件与产出物，从断点继续 |
| plan | plan.md | §计划阶段恢复 |
| implement | `.implement/progress.md` + plan.md | §实施节点恢复 |
| qa | `test-cases.md` | §test-case 重入 |
| process-management | `process-summary.md` 是否存在 | 缺失 → 补归档 |

轻量模式（无 plan.md）与 Bug 路由：按流程节点对应阶段 README（或所属 flow 文件阶段定义）+ work 目录产物恢复；实施中断按 §实施节点恢复（progress.md 首行 `plan: none`）。

### 计划阶段恢复

1. 读 plan.md，取阶段进度 checkbox 中第一个未勾选阶段
2. 无未勾阶段但 plan 未完成 → 进入 qa 审计
3. 读该阶段 `**恢复指引**`（入口文件:行 + 当前进度一句话）

### 实施节点恢复

1. 读 `docs/work/<分支>/.implement/progress.md`，匹配首行 plan 路径
2. 找第一个未勾 checkbox 的任务行，按行状态分流：
   - `in-progress` → 任务已部分执行：读行尾恢复点，先刷新验证（git status / task-N-report 自审 / 测试）确定已完成子步骤，**继续未完成部分，不重头**
   - `待执行` → 任务未开始，从任务起点执行
3. 尾行语义：
   - `Task N: complete` → 从 Task N+1 开始；无下一个任务 → 阶段完成，同步 registry 流程节点 → qa
   - `Task N: in-progress` → 按第 2 步从该任务恢复点续做
   - `Task N: fix round M` → §test-case 重入（修复轮未闭环）
4. 打开对应 `task-N-report.md` 的 `## 代码定位` + plan.md 该阶段 `**恢复指引**`，交叉核对方向与事实后确认入口
5. `.implement/progress.md` 缺失（被清理/损坏）→ 不阻断：用 plan.md 该阶段 `**恢复指引**`（入口文件:行）+ `git log` 最近提交定位，从阶段入口恢复

### test-case 重入

1. 读 `test-cases.md` `## 裁定` 的 `**Route:**`：
   - `done` → 继续 qa 退出 → process-management
   - `implement` → 提取全部 FAIL/TODO，按来源分流（阶段 N → 对应 task-N-report.md；code-audit A<N> → 临时 task），从第一个未修复项开始 Fix Round（执行 `implement/README.md` §修复模式）
   - `human` → 暂停，等待人类决策
2. 修复轮中断（progress.md 尾行 `fix round M`）→ 从 test-cases.md 对应 FAIL 项继续**同一轮**，不重新计数
3. qa 阶段中断重入 → 不重建 test-cases.md，增量重跑 Failures + diff 影响用例

### 多计划项目恢复

若恢复的是子计划，子计划完成后按 [template-sub.md](template-sub.md) §多计划状态联动规则 执行联动。

主计划级别的编排循环中断 → 从 [program-management.md](../project/program-management.md) §编排循环 步骤 1 重新扫描。
