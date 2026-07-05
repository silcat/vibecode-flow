# 计划注册表

> AI 维护。仪表盘，列所有计划。树形缩进表示父子关系。已完成计划保留不删。

## 当前激活


## 全部计划

| 计划 | 类型 | 父计划 | 状态 | 自治 | 阶段 | 更新 |
|------|------|--------|------|------|------|------|

---

## 维护规则

### 注册

- 阶段 plan 创建主计划 → 新增主计划行，状态 `planned`
- 创建子计划 → 新增子计划行，缩进于父计划下，状态 `planned`，父计划列填父计划目录名
- 计划激活时 → 更新"当前激活"，状态改为 `in-progress`

### 状态变更

- 计划暂停/阻塞时 → 状态改为 `paused` 或 `blocked`（若当前激活为该计划则清除"当前激活"）
- 子计划完成时 → 状态改为 `completed`，同时：
  1. 检查同父计划下所有兄弟子计划是否均已完成
  2. 全部完成 → 在注册表末尾追加 `[ACTION] <父计划目录> 所有子计划已完成，人类确认后触发集成审计`
  3. 未全部完成 → 检查依赖图中下一个未完成子计划的前置条件是否满足，满足则追加 `NEXT: <子计划目录>`

### 注销

- 独立计划 / 子计划完成时 → 闭环审计通过后，状态改为 `completed`，清除"当前激活"
- 主计划完成时 → 所有子计划 completed + 集成审计通过后，状态改为 `completed`，清除"当前激活"

### 恢复

- 人类说"继续" / 关键词 → AI 按 `docs/process/flows/stages/plan/recovery.md` 执行恢复

### 字段来源

| registry 列 | frontmatter 字段 | 映射 |
|------------|-----------------|------|
| 计划 | — | 目录名，人工指定 |
| 类型 | `type` | `standalone` → 独立计划；`sub` → 子计划。`master` → 总计划，见 `docs/process/flows/stages/plan/template-master.md` |
| 父计划 | `parent` | 子计划从 `parent` 提取父目录名；其他 → `—` |
| 状态 | `status` | 直接同步。取值：`planned` / `in-progress` / `paused` / `blocked` / `completed` |
| 自治 | 手动设置 | 唯一控制点。取值：`implement` / `plan-first` / `ask-first` / `research-only` / `blocked`。默认值：`implement`。含义见 `docs/baseline/context/ai-autonomy-policy.md` |
| 阶段 | — | 当前实施的阶段名（如 `阶段 2`），无则 `—` |
| 更新 | `updated` | 直接同步 |
