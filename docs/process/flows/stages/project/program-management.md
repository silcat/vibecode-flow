# 大型项目管理指南

## 文件角色

scope=多计划时，由 project 阶段触发。定义总计划/子计划的文件结构、调度、编排和集成审计规则。

## 首先阅读

- [scope/README.md](../scope/README.md) §级别条件 — 多计划判定条件
- [project/README.md](README.md) — 项目拆分入口与产出
- [plan/template-master.md](../plan/template-master.md) — 总计划模板
- [plan/template.md](../plan/template.md) — 子计划使用的单计划模板

## 文件结构

子计划嵌套在总计划目录内：

```
docs/work/
├── <项目目录>/
│   ├── plan.md                  ← template-master.md
│   ├── requirement.md           ← 总需求
│   ├── <子计划1>/
│   │   ├── plan.md              ← template.md
│   │   └── requirement.md       ← 编排循环激活时从总需求提取
│   └── <子计划2>/
│       ├── plan.md
│       └── requirement.md
```

## 总计划

使用 [plan/template-master.md](../plan/template-master.md)。必含：项目章程、子计划清单、依赖图、集成关卡。

### 子计划清单格式

| 子计划目录 | 依赖 | 可并行 |
|-----------|------|--------|
| refactor-login | 无 | ✅ |
| refactor-permission | refactor-login | ❌ |

目录名为总计划目录下的子目录名。依赖 = 必须在哪个子计划完成后才能开始。可并行 = 依赖已全部 `completed` 的组内子计划 AI 可自主编排执行顺序。

## 两层调度

| 层 | 谁决策 | 粒度 |
|----|--------|------|
| 项目层 | 人类 | 总计划 |
| 任务层 | AI | 子计划 |

## 状态派生

编排循环在每次子计划状态变更后，按以下规则更新总计划 frontmatter `status`。registry 的 `状态` 列随 frontmatter 自动同步（见 registry §字段来源）：

- 全部 `planned` → `planned`
- 任一 `in-progress` → `in-progress`
- 任一 `blocked` 且无 `in-progress` → `blocked`
- 全部 `completed`，集成审计未做 → `in-progress`
- 全部 `completed` + 集成审计通过 → `completed`

子计划 frontmatter `status` 是唯一真源。总计划 `status` 为派生字段，不独立决策。

## 子计划编排

### 前置

总计划已通过 `plan-audit`，frontmatter `status: planned`。

### 子计划流程

编排循环激活子计划时，AI 从总需求提取对应的验收标准子集生成 `requirement.md`，副本冻结。填充 `plan.md` 实施细节后，进入：

| 步骤 | 阶段 | 协议 |
|------|------|------|
| 1 | `plan-audit` | [计划审计](../plan-audit/README.md) |
| 2 | `implement` | [实施与验证](../implement/README.md) |
| 3 | `closure` | [闭环审计](../closure/README.md) |

跳过 `audit`（总计划已通过需求与基线审计）、`plan`（骨架已建，仅填充）、`log`（合并到项目完结日志）。

### 编排循环

每轮：

1. 扫描子计划清单，读取各子计划 frontmatter `status`
2. 按依赖图筛出依赖已全部 `completed` 的子计划
3. 取清单声明顺序第一个可激活的子计划，激活并执行：
   - frontmatter `status` → `in-progress`
   - registry 新增行（类型 `子计划 · <总计划目录>`，状态 🔄）
   - 执行子计划流程
4. 子计划完成（closure 通过，registry 删除该行，status `completed`）→ 回到 1
5. 全部 `completed` → 集成审计
6. 无可激活子计划但存在未完成 → 检查 `blocked`：有则报告等人类决策；无则回到 1 等待依赖满足

### 中断恢复

- registry "当前激活"为子计划 → 继续该子计划当前阶段
- 无当前激活 → 从编排循环步骤 1 重新扫描

## 阻塞处理

一个子计划 🚧：依赖它的暂停等待，不依赖的可继续。

## 集成审计

最后一个子计划 closure 通过后自动切入：

1. 回归检查已验证表面
2. 对照总计划集成关卡逐条验证
3. 检查跨子计划公共契约兼容性

通过 → 项目完结。未通过 → 按依赖图反向追溯回退相关子计划。子计划各自的闭环审计不能替代集成审计。

## 项目完结

集成审计通过后：
1. 追加项目完结日志（见 [log/README.md](../log/README.md) §项目完结条目）
2. 总计划 frontmatter `status` → `completed`
3. 子计划已从 registry 删除；总计划目录保留为归档

## 属于这里的

- 文件结构、总计划定义、子计划编排
- 两层调度、编排循环、状态派生
- 集成审计和项目完结

## 不属于这里的

- 多计划判定条件 → [scope/README.md](../scope/README.md)
- 项目拆分入口与产出 → [project/README.md](README.md)
- 总计划/单计划模板和退出标准 → [plan/](../plan/)
- 各阶段执行细节 → [stages/](../../)

## 更新原则

- 修改编排循环 → 检查 `implement`/`closure` 阶段 README 的项目分支是否受影响
- 修改状态派生 → 检查 `registry.md` §字段来源 映射
- 新增流程阶段 → 评估是否需项目级别差异并更新"子计划流程"
