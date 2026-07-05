# 计划文件模板（单计划）

适用：轻量级别、完整级别、项目级别的子计划。计划是**实施契约**，不是路线图。

## 命名

`docs/work/<工作目录>/plan.md`

## frontmatter

```yaml
---
branch: feature-xxx            # 对应的 git 分支
status: planned                # 取值见 docs/work/registry.md §字段来源
type: standalone               # standalone | sub
parent: none                   # 子计划指向主 plan.md（相对路径），无则写 none
requirement: requirement.md    # 需求文件路径（相对本目录），无则写 none
created: 2026-06-26
updated: 2026-06-26
blocker: none                  # 阻塞原因，无则写 none
---
```

| 字段 | 说明 |
|------|------|
| `type` | `standalone` = 完整/轻量级别计划；`sub` = 项目级别的子计划 |
| `requirement` | 子计划指向 AI 从总需求提取的 requirement.md |

| `autonomy` | 在 `docs/work/registry.md` 中设置（非 frontmatter）。默认值：standalone → `implement`；sub → `implement`。AI 不可升级。含义见 `docs/baseline/context/ai-autonomy-policy.md` §自治级别 |

---

## 模板

```markdown
---
branch: <分支名>
status: planned
type: standalone
requirement: requirement.md
created: <日期>
updated: <日期>
blocker: none
---

# [工作标识] 计划

## 项目章程

<!-- 多阶段实施时必填，单阶段可选 -->

- **北极星**：[最终目标]
- **完成定义**：[什么算彻底完事]
- **安全硬约束**：[不可逆/破坏性操作的边界，无则写"无"]
- **范围分层**：[Tier 1 → 阶段 X / Tier 2 → 阶段 Y]
- **明确不在范围**：[战略不做的事]

## 当前基线

[从 project-context.md / codebase-map.md 摘录与本计划相关的技术现状]

## 阶段 N：[阶段名]

- **状态**：planned
- **依赖**：[前置阶段或条件，无则写"无"]
- **目标**：[本阶段做什么]
- **非目标**：[本阶段不做什么]
- **触及面**：[变更的文件/模块]
- **公共契约**：[必须保持兼容的接口，不适用写"无"]
- **闭环关卡**：
  - [ ] [具体可验证的条件]
- **验证证据**：[截图/日志/测试输出/手动确认]
- **恢复指引**：[接手代理先读什么，当前进度在哪]

## 测试矩阵

| 验收标准 | 测试断言 | 测试文件 | 类型 |

## Skill

- Skill: [名称 或 none]
- 调研引用：[有则链接 research.md]
```

---

## 退出标准

进入 `plan-audit` 前全部满足：

- [ ] frontmatter 全部必填字段已填写
- [ ] 所有段已填写，无占位符
- [ ] 测试矩阵行数 = 需求文件验收标准条目数
- [ ] 每个声明的阶段均有闭环关卡定义
- [ ] Skill 已标注或标记 none
- [ ] 若 `research` 阶段触发，调研引用已链接

---

## 规则

- 计划写 **how**，不重复需求的 **what**
- 阶段完成标准是硬门槛——代码写完不等于阶段完成
- 不要将模糊的"后续完善"当作闭环关卡
- 触及面和公共契约段让接手代理知道安全边界——不写等于没有边界
