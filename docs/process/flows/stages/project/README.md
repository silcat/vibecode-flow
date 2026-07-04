# 项目拆分

## 文件角色

以 `type: master` 产出总计划与子计划目录骨架，作为编排契约。进入条件：scope=多计划。

## 首先阅读

| 如果你需要… | 先读 |
|-------------|------|
| 总计划模板与退出标准 | [plan/template-master.md](../plan/template-master.md) |
| 子计划编排与生命周期规则 | [program-management.md](program-management.md) |

## 执行

按 [plan/template-master.md](../plan/template-master.md) 产出总计划，按 [program-management.md](program-management.md) 创建子计划目录骨架（嵌套在总计划目录内）。

## 产出

- `docs/work/<项目目录>/plan.md`（`type: master`）
- `docs/work/<项目目录>/<子计划目录>/` — 子计划目录骨架

## 退出

→ plan-audit（`type: master`），通过后进入编排循环。

## 回退

拆分缺陷 → 调整子计划清单、依赖图或重新 scope 判定
