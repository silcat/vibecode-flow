# 技术调研

## 文件角色

涉及新技术/库/服务且无现有模块可参考时，执行结构化技术选型评估。

## 首先阅读

| 如果你需要… | 先读 |
|-------------|------|
| 执行技术评估的 6 维度框架 | [evaluation-framework.md](evaluation-framework.md) |
| 调研报告模板 | [research-template.md](research-template.md) |
| tech skill 模板 | [skill-template.md](../../../../skills/tech/skill-template.md) |

## 执行

见 [evaluation-framework.md](evaluation-framework.md)

## 产出

- `docs/work/<branch>/research.md` — 技术选型评估与决策
- `docs/skills/tech/<tech-name>/SKILL.md` — 开箱即用技能指南（每个新引入的技术一份）
- `docs/skills/tech/<tech-name>/refer/` — 学习资料目录（按需）

## 退出

→ baseline

## 回退

调研改变需求范围 → requirement

## 退出条件（阻断）

`research.md` 结论部分必须由人类审查并填写批准记录后，才允许退出本阶段。

- [ ] 人类已审查推荐方案和弃用理由
- [ ] 人类已确认风险缓解措施可接受
- [ ] 人类已在结论栏填写批准记录
- [ ] **每个新引入的技术均已创建 SKILL.md**（至少包含快速接入 + 核心代码片段）

- [ ] **每个新引入的技术均已创建 refer/README.md**（至少包含官方文档链接 + 关键概念摘要）

缺失任一条 → 保持阻塞，不得进入 baseline 或后续阶段。

> 若 autonomy = implement：本阻断自动跳过（保护区操作除外）。AI 仍需产出 research.md + SKILL.md + refer/。见 `docs/baseline/context/ai-autonomy-policy.md` §implement 全自动模式。
