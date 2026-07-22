# 项目拆分

## 文件角色

scope=多计划时，产出总计划与子计划目录骨架，作为编排契约。

## 进入条件

scope-analysis.md 判定 = 多计划。

## 执行步骤

1. 按 `template-master.md` 产出总计划 `docs/work/<项目>/plan.md`（type: master）
2. 按 `program-management.md` 创建子计划目录骨架 `docs/work/<项目>/<子计划>/`

总计划必含：项目章程、子计划清单、依赖图、集成关卡。

## 产出物

| 产出 | 路径 |
|------|------|
| 总计划 | `docs/work/<项目>/plan.md`（type: master） |
| 子计划骨架 | `docs/work/<项目>/<子计划>/` |

## 完成证明

`Test-Path` 确认总计划 plan.md 存在 + 子计划目录已创建。

## 退出路由

| 条件 | 去向 |
|------|------|
| 完成 | → plan-audit（type: master） |
| 拆分缺陷 | → project（调整清单/依赖图）或 → scope（重新判定） |
