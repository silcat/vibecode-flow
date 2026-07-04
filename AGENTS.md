# AGENTS.md

## 项目意图

<项目名称> 使用轻量级的吸引子引导工程（Attractor-Guided Engineering）工作流进行 AI 辅助的后端微服务系统开发。

本仓库面向后端微服务系统，不是框架核心项目。

仓库是真源。聊天只是临时的工作平面。

## 首先阅读（强制）

- docs/baseline/context/README.md — 唯一入口，含全量文件路由表（按场景按需加载具体文件）
- docs/work/registry.md — 当前活跃工作（计划或无计划需求）
- 当需求含义依赖源材料时，docs/work/ 下的相关原始输入

需要时额外阅读：
- docs/process/routing.md 用于工作流问题
- docs/index.md 当你需要超出活跃文件范围的路由指引时

## 工作流入口

AI 代理收到请求后，按以下前置路由执行：

### 步骤 1：阅读上下文

读取 docs/baseline/context/README.md，按其路由表按场景加载所需文件。

### 步骤 2：意图检测

执行 docs/process/intent-detect.md：信号评分 → Tier 判定 → 自动跳过检查 → Tier 响应（静默路由 / 摘要确认 / 完整澄清）→ 路由选定。

### 步骤 3：路由
意图检测产出路由后，按 [routing.md](docs/process/routing.md) 中的路由表参考进入对应流程文件。
按 docs/process/routing.md 中的优先级路由表，进入对应流程文件。


## 运营规则

1. 优先采用文件入、文件出的协作方式。
2. 不要将聊天摘要当作持久的项目记忆。
3. 当范围仍不清晰时，不要从原始 PM 文本或原型截图直接跳到代码。
4. 如果输入模糊，先在 docs/work/ 中创建或更新文件。
5. 保持 docs/baseline/context/project-context.md 和 docs/baseline/standards/ 聚焦于当前支持的基线，而非迁移历史。
6. 保持日志简短、带日期、仅追加。完成任何重要的代码变更后，必须在 docs/logs/YYYY/MM-DD.md 中更新每日开发日志（倒序排列，格式见 docs/logs/00-log-writing-guide.md）。
7. 在 docs/bugs/ 中记录非显而易见的回归。
8. 如果原型和实施出现实质性偏离，在 docs/retro/ 中记录原因。
9. 只有当模式重复出现到值得复用时，才将重复的过程教训提升为 docs/skills/。
10. 每个创建的计划在实施开始前必须通过独立的计划审计，在标记为完成前必须通过独立的闭环审计。
11. 保持代码注释最少。优先编写自解释的代码。
12. 当引用的文件在预期路径找不到时，先检查 docs/archive/ 再下结论说文件不存在。
13. 将可复用技能视为方法选择器，而非需求、设计或架构文档的替代品。业务知识首先属于所有者文档。
14. 当相同的错误模式反复出现，先提升为可复用的审计提示词或检查清单。如果缺陷模式仍然重现，则评估提升为 Lint 规则、CI 守卫或 codemod。
15. 强制性规则放在 docs/baseline/context/ 或 AGENTS.md 中，不隐藏在 docs/reference/。

## 计划规则

计划触发条件和生命周期见 docs/process/flows/stages/plan/README.md。
计划审计和闭环审计使用 docs/skills/ 下的对应提示词。

## 技能使用规则

- 技能选择按工作方法匹配，非仅业务标签
- 使用前确认 docs/skills/README.md 中列出的所需输入可用
- 技术选型时使用 docs/skills/engineering/tech-evaluation/SKILL.md
- 对于非平凡计划，记录 Skill: <名称> 或 Skill: none
- 无匹配技能时按正常文档驱动工作流继续

## 给 AI 代理的提示

- 不要从单一功能列表生成完整产品
- 优先选择小的完整切片，而非广泛的占位符覆盖
- 优先选择项目已有的模式，而非自创抽象
- 如果信息缺失，将缺失的假设写入文件中，而不是默默编造
- 不要将代码级的实施细节放入计划文件中

- 修改跨模块通信代码（Feign 接口、Kafka 生产者/消费者、网关路由）后，搜索 `docs/flows/` 的"边界"列，若命中则判断本次改动是否改变了调用序列、通道或失败策略，是则更新对应 flow
- 优先引用已有的所有者文档，而不是在多个文件中重复相同的规则

## 文档维护

完成任何重要的代码变更后：
1. 更新每日开发日志 docs/logs/YYYY/MM-DD.md（倒序）
2. 当变更影响系统行为或技术结构时，更新 docs/baseline/context/project-context.md 或 docs/baseline/standards/

验证完全通过（全绿）时，在日志条目和 git commit message 中记录验证状态。

## 验证基线

使用 docs/baseline/context/project-context.md 中列出的真实命令。
如果验证命令为空或仍为占位符，先停下来填充它们，再报告验证成功。

