# 提示词模式调研：实施 & 验证阶段

> 调研日期：2026-06-14
> 来源：v0、Claude Code、Bolt.new、Lovable、Cursor Rules 社区、BMAD 方法、Aider 等高星项目/产品的公开系统提示词与工作流设计
> 用途：为本项目路由协议阶段 ⑩（实施+验证）和阶段 ⑫（闭环审计）提供可借鉴的模式

---

## 一、实施提示词模式

### 1. Claude Code：编辑纪律

**核心内容**：

- 结构数据处理用解析器/结构化 API，不用临时字符串操作
- 编辑紧贴模块边界、所有权边界、行为面——不泛化改动
- 只在消除真实复杂度、减少有意义重复或匹配既有本地模式时才加抽象
- 测试与代码同时产出，不后补

**本项目已借鉴**：路由协议阶段 ⑩ C 段。

### 2. BMAD：Build → Measure → Analyze 循环

**核心内容**：

```
Build   — 实现最小完整切片
Measure — 跑测试 + 检查实际输出（curl API / 查 DB / 读日志）
Analyze — 实际行为 vs 预期行为对照
Deploy  — 分析通过才标记完成
```

**本项目已借鉴**：路由协议阶段 ⑩ D 段运行时验证矩阵。

### 3. Cursor Rules 社区：三段式项目记忆

**核心结构**：

1. Memory：项目类型 + 技术栈 + 硬约束
2. Reasoning：需求→子任务拆解（每个可独立验证）
3. Best Practices：项目特定规则 + 验证先于声称

**待借鉴**：当前上下文分散在五个文件。可考虑 `AGENTS_QUICK.md` 三段式快照（引用但不复制原文）。此项为可选，不在当前优先。

---

## 二、验证提示词模式

### 1. Claude Code：证据先行

**核心规则**：

- 运行验证命令并确认输出后才能声称成功
- 占位符命令不等于验证通过
- 禁止从记忆中宣称验证通过
- 禁止在命令未实际运行时说"测试全绿"

**本项目已借鉴**：路由协议阶段 ⑩ E 段证据硬规则。

### 2. Aider：增量测试阻断

**核心逻辑**：

```
每次编辑后 → 跑受影响测试 → 失败 → 阻断，先修复
不允许跨文件累积未测试变更
```

**本项目已借鉴**：路由协议阶段 ⑩ B 段 TDD 循环内嵌此逻辑。

### 3. rules_template：可逐条打勾的验证检查清单

**本项目已借鉴**：`docs/skills/engineering/verification-checklist/SKILL.md`，阶段 ⑪ 出口和阶段 ⑫ 入口分别引用。

---

## 三、测试矩阵与代码质量审计的互补

测试矩阵（`docs/process/flows/stages/plan/template.md` 定义）覆盖行为正确性："接口按约定返回了没有"。代码质量审计（`docs/skills/audit/code-quality-audit-prompt.md`）覆盖实现健壮性："返回的方式安全吗、干净吗、以后好改吗"。

| | 测试矩阵 | 代码质量审计 |
|---|---|---|
| 回答的问题 | 做对了没有 | 做得稳不稳 |
| 查什么 | 验收标准→断言 1:1 | 架构边界、类型契约、错误处理、可维护性 |
| 严重程度 | 通过/不通过 | P0-P3 分级 |
| 调用点 | 阶段 ⑪ TDD 循环 | 阶段 ⑪ 出口 + 阶段 ⑫ 闭环审计核对 |

---

## 四、来源清单

| 来源 | 类型 | 地址 |
|------|------|------|
| v0 系统提示词 | Vercel 产品内部提示词 | 收录于 x1xhlol/system-prompts-and-models-of-ai-tools (⭐140k) |
| Claude Code 系统提示词 | Anthropic 产品内部提示词 | 同上 |
| Bolt.new 提示词 | StackBlitz 产品 | 同上 |
| Lovable 提示词 | 独立产品 | 同上 |
| Cursor Rules 社区模板 | 社区仓库 | Bhartendu-Kumar/rules_template (⭐1k+) |
| BMAD 方法 | 方法论文档 | wesammustafa/Claude-Code-Everything-You-Need-to-Know (⭐2k+) |
| Aider 工作流 | 开源工具 | github.com/Aider-AI/aider |
| Awesome AI System Prompts | 精选合集 | dontriskit/awesome-ai-system-prompts (⭐6k) |

