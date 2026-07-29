# Codex 钩子系统

## hooks.json 配置

文件位置：`.codex/hooks.json`

```json
{
  "hooks": {
    "PreToolUse": [
      { "matcher": "Write", "hooks": [{ "type": "command", "command": "node \"$(git rev-parse --show-toplevel)/.codex/hooks/descriptive-name.cjs\"" }] },
      { "matcher": "Bash|Glob|Grep|Read|Edit|Write", "hooks": [
        { "type": "command", "command": "node \"$(git rev-parse --show-toplevel)/.codex/hooks/scout-block.cjs\"" },
        { "type": "command", "command": "node \"$(git rev-parse --show-toplevel)/.codex/hooks/privacy-block.cjs\"" }
      ]}
    ],
    "PostToolUse": [
      { "matcher": "Edit|Write|MultiEdit", "hooks": [
        { "type": "command", "command": "node \"$(git rev-parse --show-toplevel)/.codex/hooks/session-state.cjs\"" },
        { "type": "command", "command": "node \"$(git rev-parse --show-toplevel)/.codex/hooks/post-edit-simplify-reminder.cjs\"" }
      ]}
    ],
    "SessionStart": [{ "hooks": [{ "type": "command", "command": "node \"$(git rev-parse --show-toplevel)/.codex/hooks/session-init.cjs\"" }] }],
    "SubagentStart": [{ "matcher": "*", "hooks": [{ "type": "command", "command": "node \"$(git rev-parse --show-toplevel)/.codex/hooks/subagent-init.cjs\"" }] }]
  }
}
```

## 事件类型总览

| 事件 | 触发时机 | 可阻断 | 注册钩子数 |
|---|---|---|---|
| SessionStart | 会话启动/恢复/清理/压缩 | 否 | 1 |
| SubagentStart | 子 Agent 启动时 | 否 | 1 |
| PreToolUse | 工具执行前 | 是（exit 2） | 3 |
| PostToolUse | 工具执行后 | 否 | 2 |

## 钩子脚本清单

### SessionStart

| 脚本 | 功能 |
|---|---|
| `session-init.cjs` | 加载配置、检测项目信息、设置环境变量、输出初始化上下文 |

触发方式：会话启动时自动触发一次。非阻塞。
退出码：0（成功）

---

### SubagentStart

| 脚本 | 功能 |
|---|---|
| `subagent-init.cjs` | 向子 Agent 注入精简运行上下文（~200 tokens），包含 Agent 类型、计划路径、报告路径、命名规则、语言偏好 |

匹配器：`*`（所有子 Agent）
输入：标准输入 JSON，含 `agent_type`、`agent_id`、`cwd`、`session_id`
输出：通过 `hookSpecificOutput.additionalContext` 将上下文注入子 Agent prompt

---

### PreToolUse（阻断型）

| 匹配器 | 脚本 | 功能 | 阻断方式 |
|---|---|---|---|
| `Write` | `descriptive-name.cjs` | 检查写入的文件名是否足够描述性 | exit(2) |
| `Bash|Glob|Grep|Read|Edit|Write` | `scout-block.cjs` | 阻断访问 .vcignore 中声明的敏感目录 | exit(2) |
| 同上 | `privacy-block.cjs` | 阻断包含 API 密钥、令牌等敏感信息的操作 | exit(2) |

阻断机制：
钩子读标准输入 JSON -> 解析 tool_name + tool_input -> 判断是否违规 -> process.exit(2) 阻断 / process.exit(0) 放行

阻断 vs 放行示例（scout-block）：
阻断：cd node_modules、cat dist/file.js、ls .git/objects/
放行：npm build、go build、docker build、kubectl apply、terraform plan

scout-block 不区分 Agent。vc-research-agent 和 vc-execute-agent 调同样的 Bash 命令，走同一套阻断规则。

---

### PostToolUse（非阻断型）

| 匹配器 | 脚本 | 功能 |
|---|---|---|
| `Edit|Write|MultiEdit` | `session-state.cjs` | 文件修改后记录会话状态 |
| 同上 | `post-edit-simplify-reminder.cjs` | 提示用户是否需要运行 code-simplifier |

不可阻断，只做记录或提示。

---

## 支撑库

### lib/

| 模块 | 功能 | 被钩子引用 |
|---|---|---|
| `scout-checker.cjs` | 统一入口：路径提取 + 模式匹配 + 构建命令白名单 | scout-block |
| `privacy-checker.cjs` | 敏感信息模式匹配逻辑 | privacy-block |
| `project-detector.cjs` | 项目和环境检测 | session-init |
| `session-state-manager.cjs` | 持久化/恢复会话状态 | session-state |
| `context-builder.cjs` | 构建注入上下文 | subagent-init |
| `vc-config-utils.cjs` | 配置加载、路径清理、常量 | 大部分钩子 |
| `hook-logger.cjs` | 结构化日志到 .logs/hook-log.jsonl | 所有钩子 |
| `git-info-cache.cjs` | Git 信息缓存（减少重复进程启动） | 状态栏系统 |
| `statusline-section-registry.cjs` | 状态栏区块注册表 | 状态栏系统 |
| `statusline-render-modes.cjs` | 状态栏渲染模式（full/compact/minimal） | 状态栏系统 |
| `statusline-activity-renderers.cjs` | Agent 流程和 TODO 行渲染 | 状态栏系统 |
| `statusline-string-utils.cjs` | 终端字符串工具（可见长度、时间格式化） | 状态栏系统 |
| `statusline-session-cache.cjs` | 状态栏会话快照缓存 | 状态栏系统 |
| `colors.cjs` | ANSI 终端颜色（跨平台） | 状态栏系统 |
| `transcript-parser.cjs` | 从会话 JSONL 提取工具/Agent/todo 状态 | 状态栏系统 |
| `usage-limits-cache.cjs` | 可用性限制缓存 | 状态栏系统 |
| `config-counter.cjs` | 配置计数器 | 内部工具 |

### scout-block/

| 模块 | 功能 |
|---|---|
| `pattern-matcher.cjs` | gitignore 规范模式匹配（通过 vendored ignore 包） |
| `path-extractor.cjs` | 从工具输入中提取文件路径和命令参数 |
| `broad-pattern-detector.cjs` | 检测过于宽泛的 glob 模式（防止填充上下文） |
| `error-formatter.cjs` | 格式化的阻断错误信息（Problem + Reason + Solution） |
| `vendor/ignore.cjs` | ignore 包 v5.3.0（vendored，无 npm 依赖） |

---

## 执行顺序

```
SessionStart（会话启动）
  -> session-init.cjs（加载配置、检测项目）
       |
       +-> SubagentStart（当子 Agent 被 spawn）
       |     -> subagent-init.cjs（注入 ~200 tokens 上下文）
       |
       +-> PreToolUse（每次工具调用前）
       |     +-> descriptive-name.cjs（Write 时检查文件名）
       |     +-> scout-block.cjs（路径阻断 -> exit 2 阻断 / exit 0 放行）
       |     +-> privacy-block.cjs（隐私阻断 -> exit 2 阻断 / exit 0 放行）
       |          阻断时不执行工具
       |
       +-> [工具实际执行]
       |
       +-> PostToolUse（工具执行后）
             +-> session-state.cjs（记录状态）
             +-> post-edit-simplify-reminder.cjs（提示简化）

每次工具调用都循环一次 PreToolUse -> 执行 -> PostToolUse
```

## 关键设计要点

1. PreToolUse 是唯一可阻断的钩子类型。退出码 2 = 阻断，0 = 放行。
2. 不区分 Agent。scout-block 和 privacy-block 只看 tool_name 和 tool_input，不知道来自哪个 Agent。Agent 级别的工具约束由 prompt 软约束管控。
3. 所有钩子 fail-open。脚本 crash 时 process.exit(0) 放行操作，避免钩子崩溃导致用户无法工作。
4. 日志写 JSONL。钩子运行记录在 .codex/hooks/.logs/hook-log.jsonl。
5. 可配置开关。vc-config-utils.cjs 中的 isHookEnabled() 支持按钩子名关闭特定钩子。