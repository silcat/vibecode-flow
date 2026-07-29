# 架构四层：协议 → 流程 → Agent → Skill

> 基于 vc-harness 项目实现，结合知识图谱（2858 节点）、领域图（6 域 8 流）和实际目录结构
> 生成日期：2026-07-24

整个系统从外到内分四层，像一组嵌套的齿轮，每层约束下一层的行为范围。

## 核心数据流全景

```
用户请求
    │
    ▼
┌─────────────────────────────────────────────────┐
│  意图检测器（AGENTS.md Routing Protocol）         │
│  - 关键词匹配 → 路由到对应 Agent                  │
│  - 已有 Plan 文件 → 恢复阶段                      │
│  - 含糊请求 → 澄清后再路由                        │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│  RIPER-5 流程编排（Phase 协议）                    │
│                                                   │
│  RESEARCH ──→ INNOVATE ──→ PLAN ──→ EXECUTE       │
│     │              │          │         │          │
│     ▼              ▼          ▼         ▼          │
│  vc-research  vc-innovate  vc-plan  vc-execute    │
│  + vc-scout   + vc-seq     + vc-gen-  + vc-tester │
│  + vc-docs-     -thinking    plan      + vc-code-  │
│    seeker                           reviewer       │
│                                                   │
│  ← ← 阶段锁定：上一个 DONE 才能进入下一个 → →     │
└─────────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│  Agent 层（行为定义 + 工具范围）                    │
│  - 每个 Agent 声明允许的 tool set                   │
│  - 核心 Agent 可调用 专家 Agent                     │
│  - Agent 定义在 .claude/agents/*.md               │
│  - Codex 镜像在 .codex/agents/*.toml              │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│  Skill 层（可复用的能力单元）                       │
│  - SKILL.md 定义 + 关联脚本/配置                   │
│  - 按需加载，Agent 通过工具 allowlist 使用           │
│  - 共享路径：.agents/skills/ → .claude/skills/     │
└─────────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│  Hooks & 开发者体验层（支撑全域）                   │
│  - session-init / session-state                  │
│  - 状态栏渲染 / privacy-block                    │
│  - scout-block / 配置计数器                       │
└─────────────────────────────────────────────────┘
```

## 第 1 层：协议（决定"怎么做"）

协议是最高层的规则框架，定义 Agent 之间的交互契约。核心文件在 `process/development-protocols/`：

| 文件 | 作用 |
|---|---|
| [orchestration.md](/D:/编程/AI/项目/学习项目/vibecode-pro-max-kit/process/development-protocols/orchestration.md) | 子 Agent 的派发上下文规则、Feature 范围检测、状态码（DONE/BLOCKED/NEEDS_CONTEXT） |
| [plan-lifecycle.md](/D:/编程/AI/项目/学习项目/vibecode-pro-max-kit/process/development-protocols/plan-lifecycle.md) | Plan 从创建到归档的生命周期 |
| [phase-programs.md](/D:/编程/AI/项目/学习项目/vibecode-pro-max-kit/process/development-protocols/phase-programs.md) | 大型多阶段项目的阶段计划管理 |
| [context-maintenance.md](/D:/编程/AI/项目/学习项目/vibecode-pro-max-kit/process/development-protocols/context-maintenance.md) | 上下文文档的维护规则 |
| [intent-clarification.md](/D:/编程/AI/项目/学习项目/vibecode-pro-max-kit/process/development-protocols/intent-clarification.md) | 用户意图的评分与澄清 |

这些协议文件本身被 `AGENTS.md` 引用，是整个系统的"宪法"。

## 第 2 层：流程（决定"走哪几步"）

流程是协议在时间轴上的展开，即 RIPER-5 方法论。来自领域图分析的跨域数据流：

```
研究与知识收集 → 规划与规格制定 → 代码实施与执行 → 质量保障 → 流程管理
```

每个域内有具体的业务流——比如质量保障域拆为"代码审查"和"根因调试"两条流，每条流再拆为 3-5 个步骤。这些步骤映射到具体的 Agent 和 Skill：

| 阶段 | 负责 Agent | 关键 Skill |
|---|---|---|
| 研究与知识收集 | vc-research-agent | vc-scout, vc-docs-seeker |
| 规划与规格制定 | vc-innovate-agent → vc-plan-agent | vc-generate-plan, vc-sequential-thinking |
| 代码实施与执行 | vc-execute-agent / vc-fast-mode-agent | vc-frontend-design, vc-ui-ux-designer |
| 质量保障 | vc-debugger / vc-tester / vc-code-reviewer | vc-scenario, vc-security |
| 流程管理 | vc-update-process-agent / vc-git-manager | vc-generate-context, vc-audit-plans |
| 开发者体验（支撑层）| hooks 系统 | 状态栏、会话初始化、隐私过滤 |

## 第 3 层：Agent（决定"谁来做"）

Agent 是流程的执行者，每个 Agent 对应一个 `.md` 定义文件（`.claude/agents/`）和可选的 `.toml` 镜像（`.codex/agents/`）。图谱中的 "Agents and Roles" 层次包含 24 个节点，分为两类：

### RIPER-5 核心 Agent（6 个）

| Agent | 工具范围 | 行为约束 |
|---|---|---|
| vc-research-agent | Read, Grep, Glob, Bash | 只读操作，不能写文件、不能创建 Plan |
| vc-innovate-agent | Read, Grep, Glob | 讨论方案，不能做决策、不能写代码 |
| vc-plan-agent | Read, Write, Grep, Glob, Bash | 写 Plan 文件到 process/ 下，不能执行代码 |
| vc-execute-agent | 全权访问 | 按 Plan 实施，有 50% 检查点 |
| vc-fast-mode-agent | 全权访问 | 压缩版（研究→创新→规划→暂停→执行），暂停不可跳过 |
| vc-update-process-agent | Read, Write, Edit, Grep, Glob, Bash | 归档计划、记录经验、Git 提交 |

### 专家 Agent（7 个）

| Agent | 职责 | 调用时机 |
|---|---|---|
| vc-debugger | 先证据后假设的根因分析 | 实施中遇到 Bug 时 |
| vc-tester | 差异感知的测试验证 | 实施子步骤完成后 |
| vc-code-reviewer | 生产就绪审查 | 作为 pre-PR 质量门禁 |
| vc-code-simplifier | 纯风格重构 | 代码审查通过后 |
| vc-ui-ux-designer | 设计驱动的实现 | EXECUTE 阶段处理 UI 任务 |
| vc-git-manager | 结构化常规提交 | 实施完成后 |
| vc-fast-mode-agent | 也做专家 Agent | 快速场景下 |

组装方式：AGENTS.md 中的 Routing Protocol 定义了意图检测器，根据用户输入的关键词自动路由到对应的 Agent。协议约束了"上一个阶段没完成，不能进入下一个"。

## 第 4 层：Skill（决定"用什么工具"）

Skill 是最底层的可复用能力单元。图谱中"技能与工作流"层次包含 278 个节点，是最大的层次。每个 Skill 是一个 `SKILL.md` 文件，存放在 `.claude/skills/`（通过 symlink 共享给 `.agents/skills/`）。

按职责分三类：

### 契约技能（定义工作流产物的标准和流程契约）

| Skill | 作用 |
|---|---|
| vc-generate-plan | 创建实施计划文档（SIMPLE/COMPLEX 模板） |
| vc-generate-context | 刷新或生成项目上下文 |
| vc-audit-context | 审计上下文路由、分组和发现 |
| vc-audit-plans | 检查活跃计划的库存和过时情况 |
| vc-audit-vc | 审计 Agent/Skill 健康度和协议同步 |
| vc-update | 从远程拉取最新 toolkit |
| vc-publish | 推送本地的 toolkit 改进到远程 |

### 辅助技能（增强 Agent 能力但不拥有工作流）

| Skill | 作用 |
|---|---|
| vc-scout | 快速代码侦查（grep/glob 并行搜索） |
| vc-sequential-thinking | 分步推理 |
| vc-problem-solving | 认知工具箱 |
| vc-scenario | 边缘情况生成 |
| vc-preview | 可视化预览 |
| vc-tech-graph | 技术图生成（SVG/PNG） |
| vc-watzup | 仓库状态摘要 |
| vc-xia | 仓库对比与适应研究 |
| vc-repomix | 仓库打包 |
| vc-docs-seeker | 第三方库文档查询 |

### 工具技能

| Skill | 作用 |
|---|---|
| vc-chrome-devtools | 浏览器自动化（Puppeteer） |
| vc-agent-browser | AI 浏览器自动化 CLI |
| vc-web-testing | Playwright/Vitest 测试 |
| vc-security | STRIDE + OWASP 安全审计 |
| vc-context-engineering | Token/上下文优化 |
| vc-autoresearch | 自动迭代优化 |
| vc-mcp-management | MCP 服务管理 |
| vc-merge-worktree | Git worktree 合并 |
| vc-frontend-design | 设计感知的前端实现 |
| vc-debug | 根因分析 |
| vc-docs | 项目管理文档 |
| vc-setup | 新项目工具集脚手架 |
| vc-team | 多 Agent 并行协作 |

组装方式：Agent 的 YAML/TOML 定义中通过 tools: allowlist 显式声明可调用的 Skill。例如 vc-research-agent 只能调用 Read/Grep/Glob/Bash 工具，不能执行写操作——协议通过 Agent 的工具有效范围强制了阶段锁定。

## 钩子层：横切支撑

钩子不属于任何一个层次，它们在工具调用前后自动执行，通过 exit code 与运行时通信。

```
.claude/hooks/
├── session-init.cjs          会话启动 → 检测项目、加载配置
├── session-state.cjs         每次编辑 → 记录状态
├── subagent-init.cjs         子 Agent 启动 → 注入上下文
├── scout-block.cjs           目录门禁 → exit(2) 阻止访问 node_modules 等
├── privacy-block.cjs         隐私门禁 → exit(2) 阻止访问 .env 等
├── descriptive-name.cjs      Write 前 → 注入命名规范上下文
└── post-edit-simplify-reminder.cjs  编辑后 → 提醒简化代码
```

钩子通过四条通路影响四层架构：

| 通路 | 影响层 | 机制 |
|---|---|---|
| 阻断（exit 2） | Agent → Skill | 阻止访问禁止的目录/文件 |
| 上下文注入（stdout JSON） | Agent | 注入命名规范等上下文 |
| 状态持久化（文件） | 流程 | 记录会话状态供后续使用 |
| 异步唤醒（asyncRewake） | 流程 → Agent | 后台监控触发流程推进 |

## 关键设计原则

1. **协议锁阶段** — 上一个阶段 Agent 输出 DONE 之前，下一个阶段不能启动。这是 AGENTS.md 中 Phase Transition Rules 强制执行的。
2. **Agent 锁范围** — 每个 Agent 的 tool allowlist 决定了它能做什么、不能做什么。Research Agent 不能写文件，Execute Agent 才能改代码。
3. **Skill 锁职责** — 每个 Skill 只做一件事：vc-scout 只侦查、vc-generate-plan 只写计划、vc-git-manager 只管 Git。
4. **Hook 支撑全局** — 钩子系统不参与业务流程，但为所有 Agent 提供会话状态、隐私保护和配置管理。

## 对应目录结构

```
vibecode-pro-max-kit/
├── .claude/
│   ├── agents/              Agent 定义（13 个 Agent）
│   ├── hooks/               钩子系统（7 个钩子 + lib）
│   └── skills/              技能定义（30+ 个 Skill）
├── .codex/
│   ├── agents/              Codex TOML Agent 镜像
│   ├── hooks/               Codex 钩子（与 .claude 同步）
│   ├── hooks.json           Codex 钩子注册表
│   └── config.toml          Codex 运行配置
├── .agents/                 symlink → .claude/skills/
├── process/
│   ├── development-protocols/  核心协议文件
│   ├── context/                项目上下文
│   ├── general-plans/          通用计划存储
│   ├── features/               大功能计划隔离
│   └── _seeds/                 脚手架模板
├── AGENTS.md                入口 + RIPER-5 方法论
├── CLAUDE.md                Claude 入口
└── vc-manifest.json         项目清单
```