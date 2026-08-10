# 计划文件模板（独立计划）

适用：级别判定 = 单计划的独立计划。计划是**实施契约**，不是路线图。

## 命名

`docs/work/<工作目录>/plan.md`

## frontmatter

```yaml
---
branch: feature-xxx            # 对应的 git 分支
status: planned                # 取值见 docs/process/flows/stages/plan/recovery.md §注册表维护
type: standalone
parent: none
requirement: requirement.md    # 需求文件路径（相对本目录），无则写 none
created: 2026-06-26
updated: 2026-06-26
blocker: none                  # 阻塞原因，无则写 none
---
```

| 字段 | 说明 |
|------|------|
| `type` | 固定为 `standalone` |
| `parent` | 固定为 `none` |
| `requirement` | 指向本目录下的 requirement.md |

| `autonomy` | 在 `docs/work/registry.md` 中设置（非 frontmatter）。默认值：`implement`。AI 不可升级。含义见 `docs/baseline/context/ai-autonomy-policy.md` §自治级别 |

---

## 模板

```markdown
---
branch: <分支名>
status: planned
type: standalone
parent: none
requirement: requirement.md
created: <日期>
updated: <日期>
blocker: none
---

# [计划标识] 计划

## 当前基线

[从 project-context.md / codebase-map.md 摘录与本计划相关的技术现状]

## 阶段 N：[阶段名]

- [ ] **状态**：进行中。进入阶段 → 保持未勾、同步 registry 流程节点为本阶段；完成 → 打勾并同步 registry 流程节点为下一阶段。
- **依赖**：[前置阶段或条件，无则写"无"]
- **目标**：[本阶段做什么]
- **非目标**：[本阶段不做什么]
- **触及面**：[变更的文件/模块]
- **公共契约**：[必须保持兼容的接口，不适用写"无"]
- **恢复指引**：[入口文件:行 + 当前进度一句话。实施阶段每个 task 完成后更新为最新入口]
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

- [ ] frontmatter 全部必填字段已填写
- [ ] 所有段已填写，无占位符
- [ ] 测试矩阵行数 = 需求文件验收标准条目数
- [ ] 每个声明的阶段均有闭环关卡定义
- [ ] Skill 已标注或标记 none
- [ ] 若 `research` 阶段触发，调研引用已链接

---

## 断点恢复规则

- 阶段 checkbox 勾选状态 + `**恢复指引**` 是恢复主依据，中断后恢复流程见 [recovery.md](recovery.md)。

---

## 规则

- 计划写 **how**，不重复需求的 **what**
- 阶段完成标准是硬门槛——代码写完不等于阶段完成
- 不要将模糊的"后续完善"当作闭环关卡
- 触及面和公共契约段让接手代理知道安全边界——不写等于没有边界
