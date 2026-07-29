# 路由协议

可用路由与对应流程的参考目录。意图检测和路由判定由 [intent-detect.md](intent-detect.md) 执行。

## 路由表

| 路由 | 条件 | 流程 |
|------|------|------|
| 初始化 | 项目初始化 / init / 开始新项目 / 接入已有项目 / project-context.md 含模板占位符 | [init.md](flows/init.md) |
| 恢复执行 | registry 中有活跃计划 + 请求内容属于该计划范围 | 从当前阶段恢复 |
| 纯信息 | 不需代码变更的问答 | 直接回答 → 退出 |
| Bug | 修复问题/缺陷 | [bug.md](flows/bug.md) |
| 快速模式 | 需求明确 + 单模块 + 无架构影响 + 方案唯一且明显 | [fast.md](flows/fast.md) |
| 需求 | 新增功能 / 修改行为 / 架构变更 | [main.md](flows/main.md) |

## 流程文件

- [bug.md](flows/bug.md) — Bug 修复。诊断 → 判表层/契约 → 表层直接修复，契约切回主流程。
- [fast.md](flows/fast.md) — 需求快速模式。①收集→②澄清→③需求→④实施→⑤QA→⑥流程管理。跳过摸底/方案/范围/审计/计划。
- [init.md](flows/init.md) — 项目初始化。场景判定 → 场景 A 新项目 / 场景 B 已有项目接入。
- [main.md](flows/main.md) — 需求主流程。①收集→②澄清→③需求→④摸底→⑤方案→⑥范围判定→后续阶段。

## 更新原则

- 新增流程：在此文件同时更新路由表和流程文件段
- 路由表条件与对应流程文件内条件保持一致
- 流程文件保持独立，共享步骤通过引用调用
- **扩展时优先在现有流程内增加模式分支，而非新增路由条目。** 仅当新流程的生命周期结构与现有流程完全不兼容时才增加路由。
- 不属于本文件 → 意图检测细节见 [intent-detect.md](intent-detect.md)；阶段执行规则见各 `stages/<stage>/README.md`；具体流程的差异化行为见对应 flow 文件（如 [project/program-management.md](flows/stages/project/program-management.md)）

> 后续扩展路由在流程文件就绪后插入路由表。
