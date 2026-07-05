# 计划文件模板（子计划）

适用：scope=多计划的子计划。计划是**实施契约**，不是路线图。

## 命名

`docs/work/<总计划目录>/<子计划目录>/plan.md`

## frontmatter

```yaml
---
branch: feature-xxx            # 对应的 git 分支
status: planned                # 取值见 docs/work/registry.md §字段来源
type: sub
parent: ../plan.md             # 指向主 plan.md（相对路径），必填
requirement: requirement.md    # AI 从总需求提取的需求文件（相对本目录），必填
created: 2026-06-26
updated: 2026-06-26
blocker: none                  # 阻塞原因，无则写 none
---
```

| 字段 | 说明 |
|------|------|
| `type` | 固定为 `sub` |
| `parent` | **必填**，指向主 plan.md 的相对路径（如 `../plan.md`） |
| `requirement` | **必填**，指向 AI 从总需求提取的 requirement.md |

| `autonomy` | 在 `docs/work/registry.md` 中设置（非 frontmatter）。默认值：`implement`。AI 不可升级。含义见 `docs/baseline/context/ai-autonomy-policy.md` §自治级别 |

---

## 模板

```markdown
---
branch: <分支名>
status: planned
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

- **状态**：planned。进入阶段→`in-progress`，完成→`completed`。同时同步 registry "当前激活"。
- **依赖**：[前置阶段或条件，无则写"无"]
- **目标**：[本阶段做什么]
- **非目标**：[本阶段不做什么]
- **触及面**：[变更的文件/模块]
- **公共契约**：[必须保持兼容的接口，不适用写"无"]
- **恢复指引**：[从哪个文件/类开始，当前进度一句话]
- **闭环关卡**：
  - [ ] [具体可验证的条件]
  - [ ] [具体可验证的条件]
- **验证证据**：[截图/日志/测试输出/手动确认]

## 测试矩阵

| 验收标准 | 测试断言 | 测试文件 | 类型 |

## Skill

- Skill: [名称 或 none]
- 调研引用：[有则链接 research.md]
```

---

## 退出标准

进入 `plan-audit` 前全部满足：

- [ ] frontmatter 全部必填字段已填写，`parent` 指向有效的主 plan.md
- [ ] 前置检查表已填写，所有依赖项状态已核实
- [ ] "前置检查"表格中的依赖项全部指向存在的计划
- [ ] 所有段已填写，无占位符
- [ ] 测试矩阵行数 = 需求文件验收标准条目数
- [ ] 每个声明的阶段均有闭环关卡定义
- [ ] Skill 已标注或标记 none
- [ ] 若 `research` 阶段触发，调研引用已链接

---

## 多计划状态联动规则

子计划 `status: completed` 时 AI 必须执行：

1. 更新 `docs/work/registry.md` 中该子计划的状态
2. 读取主计划的依赖图，检查是否所有兄弟子计划均已完成
3. 全部完成 → 在 registry 追加 `[ACTION] 所有子计划已完成，人类确认后触发集成审计`
4. 未全部完成 → 检查下一个未完成的子计划前置条件是否满足，满足则提醒人类可激活

---

## 断点恢复规则

中断后恢复流程见 [recovery.md](recovery.md)。

---

## 规则

- 计划写 **how**，不重复需求的 **what**
- 阶段完成标准是硬门槛——代码写完不等于阶段完成
- 不要将模糊的"后续完善"当作闭环关卡
- 触及面和公共契约段让接手代理知道安全边界——不写等于没有边界
- 子计划完成不得直接标记主计划为完成——主计划完成 = 所有子计划完成 + 集成审计通过
