# 大型项目管理指南

## 文件角色

级别判定 = 多计划时，由 project 阶段触发。定义总计划/子计划的文件结构、调度、编排和集成审计规则。

## 首先阅读

- `scope/README.md` — 拆分规则（多计划判定依据）
- `project/README.md` — 项目拆分入口
- `plan/template-master.md` — 总计划模板
- `plan/template-sub.md` — 子计划模板

## 文件结构

子计划嵌套在总计划目录内：

```
docs/work/<项目>/
├── scope-analysis.md           ← 范围判定
├── requirement.md              ← 总需求
├── requirement-audit.md        ← 需求审计
├── plan.md                     ← template-master.md（type: master）
├── plan-audit.md               ← 总计划审计
├── <子计划1>/
│   ├── plan.md                 ← template-sub.md
│   ├── plan-audit.md           ← 子计划审计
│   ├── implement-report.md     ← 实施报告
│   └── test-cases.md          ← 测试用例（qa 产出）
└── <子计划2>/
    └── ...
```

## 总计划

按 `template-master.md` 产出。必含：项目章程、子计划清单、依赖图、集成关卡。

### 子计划清单格式

| 子计划目录 | 依赖 | 可并行 |
|-----------|------|--------|
| refactor-login | 无 | yes |
| refactor-permission | refactor-login | no |

目录名为总计划目录下的子目录名。依赖 = 必须在哪个子计划完成后才能开始。可并行 = 依赖已全部 `completed` 的组内子计划 AI 可自主编排执行顺序。

## 两层调度

| 层 | 谁决策 | 粒度 |
|----|--------|------|
| 项目层 | 人类 | 总计划 |
| 任务层 | AI | 子计划 |

## 状态派生

状态取值见 `docs/process/flows/stages/plan/recovery.md` §注册表维护。

编排循环在每次子计划状态变更后，按以下规则更新总计划 frontmatter `status`：

- 全部 `planned` → `planned`
- 任一 `in-progress` → `in-progress`
- 任一 `blocked` 且无 `in-progress` → `blocked`
- 全部 `completed`，集成审计未做 → `in-progress`
- 全部 `completed` + 集成审计通过 → `completed`

子计划 frontmatter `status` 是唯一真源。总计划 `status` 为派生字段，不独立决策。

## 子计划编排

### 前置

总计划已通过 plan-audit，frontmatter `status: planned`。

### 子计划流程

编排循环激活子计划时，从总需求提取对应验收标准子集生成 `requirement.md`，然后执行：

| 步骤 | 阶段 | 模块 |
|------|------|------|
| 1 | plan | 填充 `template-sub.md` |
| 2 | plan-audit | [计划审计](../plan-audit/README.md) |
| 3 | implement | [实施与验证](../implement/README.md) |
| 4 | qa | [验证与闭环](../qa/README.md) |

跳过的阶段：requirement-audit（总计划已通过）、级别判定（已判定为多计划）、log（合并到项目完结日志）、skill（合并）。

### 编排循环

每轮：

1. 扫描子计划清单，读取各子计划 frontmatter `status`
2. 按依赖图筛出依赖已全部 `completed` 的子计划
3. 取清单声明顺序第一个可激活的子计划，激活并执行子计划流程
4. 子计划完成（qa 通过）→ 回到 1
5. 全部 `completed` → 集成审计
6. 无可激活子计划但存在未完成 → 检查 `blocked`：有则报告等人类决策；无则回到 1 等待依赖满足

### 中断恢复

见 `plan/recovery.md`。多计划特有：编排循环中断 → 从步骤 1 重新扫描。

## 阻塞处理

一个子计划 blocked：依赖它的暂停等待，不依赖的可继续。

## 集成审计

最后一个子计划 qa 通过后自动切入：

1. 回归检查已验证表面
2. 对照总计划集成关卡逐条验证
3. 检查跨子计划公共契约兼容性

通过 → 项目完结。未通过 → 按依赖图反向追溯回退相关子计划。子计划各自的 qa 验证不能替代集成审计。

## 项目完结

集成审计通过后：
1. 执行流程管理（见 `process-management/README.md`）— 含归档、经验记录、上下文更新、日志追加、错误模式检测
2. 总计划 frontmatter `status` → `completed`
3. 子计划 registry 状态改为 `completed`；总计划目录保留为归档
