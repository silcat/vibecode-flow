# 计划文件模板

适用：独立计划、项目级别的子计划。计划是**实施契约**，不是路线图。

## 命名

- 独立计划：`docs/work/<工作目录>/plan.md`
- 子计划：`docs/work/<总计划目录>/<子计划目录>/plan.md`

## frontmatter

```yaml
---
branch: feature-xxx            # 对应的 git 分支
status: planned                # planned | in-progress | paused | blocked | completed
autonomy: plan-first           # implement | plan-first | ask-first | research-only | blocked
type: standalone               # standalone | master | sub
parent: none                   # 子计划指向主 plan.md（相对路径），无则写 none
requirement: requirement.md    # 需求文件路径（相对本目录），无则写 none
created: 2026-06-26
updated: 2026-06-26
blocker: none                  # 阻塞原因，无则写 none
---
```

| 字段 | 说明 |
|------|------|
| `type` | `standalone` = 独立计划；`master` = 主计划（含子计划清单）；`sub` = 子计划 |
| `parent` | 子计划必填，指向主 plan.md（如 `../plan.md`）。独立/主计划写 `none` |
| `requirement` | 子计划指向 AI 从总需求提取的 requirement.md |

| autonomy 值 | 含义 |
|-------------|------|
| `implement` | AI 可直接实施 |
| `plan-first` | AI 可起草计划，实施需等审计通过 |
| `ask-first` | AI 必须先询问人类 |
| `research-only` | AI 只调研，不改产品行为 |
| `blocked` | 阻塞条件未解除前不得继续 |

详见 `docs/baseline/context/ai-autonomy-policy.md`。AI 修改 frontmatter `status` 时须同步更新 `docs/work/registry.md`。

---

## 主计划模板（type: master）

```markdown
---
branch: <分支名>
status: planned
autonomy: <自治级别>
type: master
parent: none
requirement: requirement.md
created: <日期>
updated: <日期>
blocker: none
---

# [工作标识] 总计划

## 项目章程

- **北极星**：[最终目标]
- **完成定义**：[什么算彻底完事]
- **安全硬约束**：[不可逆/破坏性操作的边界，无则写"无"]
- **范围分层**：[Tier 1 → 阶段 X / Tier 2 → 阶段 Y]
- **明确不在范围**：[战略不做的事]

## 当前基线

[从 project-context.md / codebase-map.md 摘录与技术现状]

## 子计划清单

| 子计划目录 | 依赖 | 可并行 |
|-----------|------|--------|
| <子计划1> | 无 | yes |
| <子计划2> | <子计划1> | no |

## 依赖图

`mermaid
graph TD
    sub1[子计划1] --> sub2[子计划2]
    sub1 --> sub3[子计划3]
`

## 集成审计

全部子计划完成后执行。验证跨模块联动正确。

- [ ] [跨模块场景 1：具体可验证条件]
- [ ] [跨模块场景 2：具体可验证条件]

## Skill

- Skill: [名称 或 none]
- 调研引用：[有则链接 research.md]
```

---

## 子计划模板（type: sub）

```markdown
---
branch: <分支名>
status: planned
autonomy: <自治级别>
type: sub
parent: ../plan.md
requirement: requirement.md
created: <日期>
updated: <日期>
blocker: none
---

# [子计划标识] 计划

## 前置检查

| 依赖项 | 状态 | 验证方法 |
|--------|------|---------|
| <依赖的子计划> 完成 | planned / completed | <如何验证> |

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
- [ ] 子计划已填写 `parent` 字段
- [ ] 所有段已填写，无占位符
- [ ] 测试矩阵行数 = 需求文件验收标准条目数
- [ ] 每个声明的阶段均有闭环关卡定义
- [ ] 子计划的"前置检查"表格依赖项全部指向存在的计划
- [ ] Skill 已标注或标记 none
- [ ] 若 `research` 阶段触发，调研引用已链接
- [ ] 主计划的依赖图与实际子计划清单一致

---

## 多计划状态联动规则

子计划 `status: completed` 时 AI 必须执行：

1. 更新 `docs/work/registry.md` 中该子计划的状态
2. 读取主计划的依赖图，检查是否所有兄弟子计划均已完成
3. 全部完成 → 在 registry 追加 `[ACTION] 所有子计划已完成，人类确认后触发集成审计`
4. 未全部完成 → 检查下一个未完成的子计划前置条件是否满足，满足则提醒人类可激活

---

## 规则

- 计划写 **how**，不重复需求的 **what**
- 阶段完成标准是硬门槛——代码写完不等于阶段完成
- 不要将模糊的"后续完善"当作闭环关卡
- 触及面和公共契约段让接手代理知道安全边界——不写等于没有边界
- 子计划完成不得直接标记主计划为完成——主计划完成 = 所有子计划完成 + 集成审计通过
