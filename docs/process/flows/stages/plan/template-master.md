# 计划文件模板（总计划）

适用：项目级别（scope=项目）。总计划是**编排契约**，不写实施细节。

## 命名

`docs/work/<项目目录>/plan.md`

## frontmatter

```yaml
---
branch: feature-xxx            # 对应的 git 分支
status: planned                # 取值见 docs/work/registry.md §字段来源

type: master
requirement: requirement.md    # 总需求文件路径
created: 2026-06-26
updated: 2026-06-26
blocker: none
---
```

AI 修改 frontmatter `status` 时须同步更新 `docs/work/registry.md`。

---

## 模板

```markdown
---
branch: <分支名>
status: planned
type: master
requirement: requirement.md
created: <日期>
updated: <日期>
blocker: none
---

# [项目名] 总计划

## 项目章程

- **北极星**：[最终目标]
- **完成定义**：[什么算彻底完事]
- **安全硬约束**：[不可逆/破坏性操作的边界，无则写"无"]
- **范围分层**：[Tier 1 → 子计划 X / Tier 2 → 子计划 Y]
- **明确不在范围**：[战略不做的事]

## 当前基线

[从 project-context.md / codebase-map.md 摘录与本项目相关的技术现状]

## 子计划清单

| 子计划目录 | 依赖 | 可并行 |
|-----------|------|--------|
| <子计划1> | 无 | ✅ |
| <子计划2> | <子计划1> | ❌ |

依赖 = 必须在哪个子计划完成后才能开始。可并行 = 该组内无依赖的子计划 AI 可自主编排执行顺序。

## 依赖图

<!-- 用 mermaid 或文字描述子计划之间的先后关系 -->

## 集成关卡

<!-- 所有子计划完成后需额外验证的跨计划行为 -->

- [ ] [可验证的跨计划条件]
- [ ] [可验证的跨计划条件]

## Skill

- Skill: [名称 或 none]
- 调研引用：[有则链接 research.md]
```

---

## 退出标准

进入 `plan-audit` 前全部满足：

- [ ] frontmatter 全部字段已填写，`type: master`
- [ ] 项目章程全部条目已填写，无占位符
- [ ] 子计划清单完整，每个子计划目录路径有效
- [ ] 依赖图无环，能覆盖清单中全部子计划
- [ ] 集成关卡逐条可验证，不是空洞承诺
- [ ] Skill 已标注或标记 none

---

## 规则

- 总计划是编排契约，不写实施细节——不写阶段块、触及面、公共契约、测试矩阵、验证证据
- 子计划各自的实施细节写在各自的 plan.md 中（使用单计划模板 template.md）
- 集成关卡是跨计划的硬验证，不重复子计划各自的闭环关卡
