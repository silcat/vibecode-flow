# 钩子门禁架构

> 基于 vc-harness 项目的实际实现，总结技术门禁的执行链路、原理和局限。
> 生成日期：2026-07-24 | 覆盖版本：v2.4.1+

## 一、问题背景

### 为什么需要钩子

AI Agent（Claude Code / Codex）的行为受两方面约束：

| 约束方式 | 机制 | 可靠性 |
|---|---|---|
| Prompt 规则 | 写在 AGENTS.md、Agent 定义中的行为指南 | **不可靠**——上下文压缩后 LLM 可能遗忘或忽视 |
| Tool Allowlist | 运行时引擎在工具路由阶段的 ACL 检查 | **部分可靠**——有 Bash 后门 |
| Hook 系统 | 工具调用前后启动独立 OS 进程，通过 exit code 通信 | **可靠**——进程间通信，LLM 无法绕过 |

**核心认识：** "AI 自觉"不是一个可靠的保障机制。Hook 系统的存在是因为需要操作系统级的进程间通信来做拦截，而不是依赖 LLM 对 prompt 规则的遵守。

## 二、技术门禁执行链路

### 完整链路（6 步）

```
LLM 生成工具调用
    |
    v
运行时引擎拦截，查 hooks.json 匹配
    |
    v
spawn hook 子进程，通过 stdin 传入工具调用 JSON
    |
    v
hook 子进程判定规则 -> exit(0) 或 exit(2)
    |
    v
运行时读 exit code：
  exit(0) -> 执行原始工具
  exit(2) -> 不执行，构造失败结果返回 LLM
    |
    v
LLM 收到工具调用结果（成功或 is_error: true）
```

### 关键机制：exit code 协议

运行时在 PreToolUse 阶段启动 hook 子进程，然后通过操作系统 API（Linux: waitpid, Windows: GetExitCodeProcess）读取子进程的退出码：

| 退出码 | 运行时行为 | 含义 |
|---|---|---|
| 0 | 继续执行原始工具 | ALLOW（放行） |
| 2 | 不执行原始工具，构造失败结果返回 LLM | BLOCK（阻止） |
| 其他 | 视运行时实现而定，通常 fail-open | 意外错误 |

**exit(2) 的来源：** Claude Code Hook Protocol 定义的约定（记录在官方 JSON Schema 的 asyncRewake 字段文档中）。Codex 沿用了同一协议。选择 2 而非 1 是因为 Bash 惯例中正常脚本几乎不会 exit(2)，误触率远低于 exit(1)。

## 三、三个技术强制门禁

### 门禁 1：scout-block（目录访问拦截）

**文件：** .claude/hooks/scout-block.cjs / .codex/hooks/scout-block.cjs

**注册：** 匹配 Bash|Glob|Grep|Read|Edit|Write 的 PreToolUse 钩子

**流程：**

1. 从 stdin 读取工具调用 JSON
2. 提取路径（从 toolInput.command、toolInput.file_path 等字段）
3. 如果是复合命令（&&、||、;），拆分后分别检查，构建命令直接放行
4. 加载 .claude/.vcignore 模式列表，做 gitignore 兼容的模式匹配
5. 匹配 -> formatBlockedError() + process.exit(2)
6. 不匹配 -> process.exit(0)

**拦截范围：** 路径层面的拦截（node_modules/**、dist/** 等），与具体工具名无关。

```
这些都会被 scout-block 拦截（路径匹配，不看命令名）：
  Bash "cat node_modules/foo.js"
  Bash "cp node_modules/foo.js /tmp/x"
  Read file_path="node_modules/foo.js"
  Glob "node_modules/**/*.js"
```

### 门禁 2：privacy-block（敏感文件拦截）

**文件：** .claude/hooks/privacy-block.cjs / .codex/hooks/privacy-block.cjs

**注册：** 匹配 Bash|Glob|Grep|Read|Edit|Write 的 PreToolUse 钩子

**流程：**

1. 提取路径，匹配隐私模式（.env、*.pem、credentials 等）
2. 匹配 -> 检查是否有 APPROVED: 前缀
3. 无前缀 -> 输出报错 + @@PRIVACY_PROMPT@@ JSON -> process.exit(2)
4. 有前缀 -> 剥离前缀后继续检查隐私模式，放行 -> process.exit(0)

**拦截范围：** .env、.env.*、credentials、*.pem、*.key、SSH 密钥等

**安全文件例外：** .env.example、.env.sample、.env.template 自动放行

**特殊设计：** 报错输出包含 @@PRIVACY_PROMPT_START@@ / @@PRIVACY_PROMPT_END@@ JSON 标记，Codex 桌面版 UI 可将其渲染为交互式权限弹窗。

### 门禁 3：Tool Allowlist（工具白名单）

**载体：** Claude Code Agent YAML / Codex Agent TOML

**流程：**

1. 会话启动时，运行时加载 Agent 定义的 tools: 列表
2. 每次 LLM 生成工具调用时，运行时检查工具名是否在 allowlist 中
3. 不在 -> 运行时直接拒绝，不提交给钩子系统

**已知漏洞——Bash 后门：**

Research Agent 的 allowlist 包含 Bash，因此不能 Write 但可以：
  Bash "echo 'const x = 1' > src/new.ts"
运行时只看工具名 Bash，不分析命令内容。

## 四、Hook 的四种工作通路

| 通路 | 机制 | 项目实例 | 可靠性 |
|---|---|---|---|
| 阻断 | exit(2) -> 运行时中断工具执行 | scout-block, privacy-block | 高——OS 进程级，LLM 无法绕过 |
| 上下文注入 | stdout JSON -> 运行时注入 LLM 下一轮输入 | descriptive-name | 中高——注入到上下文了，但 LLM 仍可忽视 |
| 状态持久化 | 写入磁盘文件供后续钩子读取 | session-state | 不直接面对 LLM——钩子间通信 |
| 异步唤醒 | asyncRewake: true + exit(2) -> 运行时插入上下文 | 未使用 | 高——后台监控，运行时主动注入 |

### fail-open 设计

所有钩子统一采用 fail-open 策略——意外崩溃时 process.exit(0) 放行：

```javascript
catch (error) {
  process.exit(0);  // 钩子崩了？放行，不阻塞用户
}
```

这是设计选择：钩子不应成为工作流的单点故障。代价是钩子本身的 bug 会导致门禁静默失效。

## 五、未被技术强制执行的流程门禁

以下是仅靠 Prompt 规则、没有技术保障的门禁：

| 门禁 | 依赖方式 | 常见绕过场景 |
|---|---|---|
| Phase Lock（Research 不能写代码） | Agent prompt 中的行为约束 | 用 Bash echo > file 写入 |
| Fast Mode 强制暂停 | Fast Mode Agent prompt 中的 CRITICAL 标注 | 用户说"继续"后直接执行 |
| Mid-Implementation Check-In | Execute Agent prompt 中的 50% 检查点规则 | 小变更一路做完 |
| Quality Checklist | 各 Agent prompt 中的自检规则 | 除非用户明确要求，默认跳过 |
| Ready for Next Phase 确认 | Agent 输出问询后等待 "go" | 用户说"继续"后直接推进 |
| Closeout 分类 | Execute Agent 完成后的三态分类 | 略过，直接说"完成了" |

**根本原因：** 所有钩子都是操作级拦截（拦截单个 Read/Write/Bash 调用），没有一个知道"当前是 RESEARCH 还是 EXECUTE 阶段"。缺少一个 phase-aware 的 PreToolUse 钩子，在执行写入操作前验证当前 Agent 在当前阶段是否有写入权限。

## 六、阻塞后 LLM 的行为

运行时在阻断后不做任何后续强制。LLM 收到 is_error: true 的工具结果后，可以自由选择：

| 行为 | 是否被约束 |
|---|---|
| 承认失败，告知用户 | 无约束 |
| 换路径重试 | 如果新路径不匹配 -> exit(0) 放行 |
| 建议用户修改 .vcignore | 无约束 |
| 尝试间接读取 | scout-block 路径匹配再次 exit(2)，永远拿不到内容 |

**每次工具调用独立经过钩子检查，但运行时不提供"违规后降权"的状态性惩罚。**

## 七、Hook 注册方式

### 支持的事件类型

| 事件 | 触发时机 | 同步/异步 |
|---|---|---|
| PreToolUse | 工具执行前 | 同步（阻塞原始工具） |
| PostToolUse | 工具执行后 | 同步 |
| PostToolUseFailure | 工具执行失败后 | 同步 |
| SessionStart | 会话启动/恢复/压缩 | 同步 |
| SubagentStart | 子 Agent 启动时 | 同步 |
| Stop | 会话结束 | 同步或异步 |
| UserPromptSubmit | 用户提交消息时 | 同步 |

### 钩子命令字段

| 字段 | 说明 |
|---|---|
| type | "command"（执行 shell 命令）或 "prompt"（LLM 评估） |
| command | 要执行的 shell 命令 |
| matcher | 工具名匹配正则，如 "Bash|Read|Write" |
| timeout | 超时秒数（默认 600，UserPromptSubmit 时降至 30） |
| async | 异步运行，不阻塞主流程 |
| asyncRewake | 异步钩子 exit(2) 时唤醒模型，隐含 async |
| shell | "bash" 或 "powershell" |
| if | 可选的 permission-rule-syntax 过滤器 |
| args | exec 形式的参数列表，绕过 shell 解释 |
| statusMessage | 钩子运行期间显示的自定义旋转消息 |

## 八、关键认知

1. **LLM 不是门禁的阻止方。** 阻止行为由运行时根据 hook 子进程的 exit code 执行，LLM 是"被阻止"的一方。

2. **操作系统进程边界是保障的核心。** exit code 是内核在父子进程间传递的信号，不是 LLM token 解析的结果。

3. **Hook 保障的是"那一次操作执行不了"，不保障"LLM 接下来怎么做"。** 后续行为仍依赖训练和上下文引导。

4. **所有钩子 fail-open。** 这是设计选择（不阻塞用户工作流），但也意味着钩子 bug 会导致门禁静默失效。

5. **现有门禁缺少 phase awareness。** 没有一个钩子知道当前是 RESEARCH 还是 EXECUTE 阶段，因此无法防止 Research Agent 通过 Bash 后门写入文件。

## 附录：术语对照

| 术语 | 含义 |
|---|---|
| Hook | AI 运行时在工具调用前后自动启动的独立 OS 进程 |
| PreToolUse | 工具执行前触发的 hook 事件 |
| PostToolUse | 工具执行后触发的 hook 事件 |
| exit(0) | 子进程退出码 0 -> 运行时判定 ALLOW |
| exit(2) | 子进程退出码 2 -> 运行时判定 BLOCK |
| fail-open | 钩子崩溃时 exit(0) 放行，不阻塞工作流 |
| Tool Allowlist | Agent 定义中声明的可用工具列表，运行时在工具路由阶段检查 |
| Bash 后门 | Agent 通过 Bash 工具执行 shell 命令绕过 Write 限制 |
| Phase-aware Hook | 能感知当前 RIPER-5 阶段状态并据此判定是否放行的钩子（尚未实现） |
| asyncRewake | 异步钩子 exit(2) 时唤醒模型注入上下文 |
| .vcignore | scout-block 使用的路径模式屏蔽文件，gitignore 语法 |
| APPROVED: 前缀 | privacy-block 的放行协议，需 LLM 获得用户批准后在路径前加上该前缀 |

> 本文档基于 vc-harness 项目 .claude/hooks/ 和 .codex/hooks/ 的实际实现编写，反映 v2.4.1+ 版本的架构设计。
>
> 相关文件：
> - .claude/settings.json — Claude Code hook 注册
> - .codex/hooks.json — Codex hook 注册
> - .claude/hooks/scout-block.cjs
> - .claude/hooks/privacy-block.cjs
> - .claude/hooks/lib/ — 共享逻辑库
> - .claude/hooks/scout-block/ — 模式匹配与报错格式化