# 技术调研

## 文件角色

涉及新技术/库/服务且无现有模块可参考时，执行结构化技术选型评估。

## 进入条件

读取 `docs/work/<branch>/scope-analysis.md`，"能力缺口"非空 → 执行本阶段。为空 → 跳过。

## 执行步骤

按 `evaluation-framework.md` 的 6 维度框架执行评估。产出 `docs/work/<branch>/research.md`。

对每个新引入的技术：
- 创建 `docs/skills/tech/<name>/SKILL.md`（至少快速接入 + 核心代码片段）
- 创建 `docs/skills/tech/<name>/refer/README.md`（至少官方文档链接 + 关键概念摘要）

## 产出物

| 产出 | 路径 |
|------|------|
| 调研报告 | `docs/work/<branch>/research.md` |
| 技术技能 | `docs/skills/tech/<name>/SKILL.md`（每个新技术一份） |
| 参考资料 | `docs/skills/tech/<name>/refer/README.md`（同上） |

## 完成证明

`Test-Path` 确认 research.md + SKILL.md + refer/README.md 均已生成。

> 若 autonomy ≠ implement：research.md 结论部分须人类审查并填写批准记录后，才允许退出。

## 退出路由

| 条件 | 去向 |
|------|------|
| 完成 | → baseline |
| 调研改变需求范围 | → requirement |


