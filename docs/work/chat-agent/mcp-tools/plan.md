---
branch: codex/feature-chat-agent
status: completed
autonomy: implement
type: sub
requirement: requirement.md
created: 2026-07-05
updated: 2026-07-05
blocker: none
---

# mcp-tools 计划

## 当前基线

- chat-core 已就绪（ChatClient + 对话循环）
- 调研：[research.md](research.md)

## 阶段 1：MCP 客户端集成

- **状态**：⏳ PLANNED
- **依赖**：chat-core
- **目标**：集成 Spring AI MCP，工具自动注册到 ChatClient，对话中可调用
- **触及面**：
  - `chat-agent/pom.xml` — 添加 spring-ai-mcp 依赖
  - `chat-agent/src/main/resources/application.yml` — MCP Server 配置
  - `chat-agent/src/main/java/com/vibecode/agent/mcp/` — MCP 配置类
- **公共契约**：无（工具调用内嵌于现有 `/api/chat/{sessionId}` SSE 端点）
- **闭环关卡**：
  - [ ] MCP Server 启动后 agent 可列出其工具
  - [ ] 对话中 agent 自主调用工具 → 结果正确注入回复
  - [ ] 工具调用失败时对话不中断，agent 告知用户失败原因
- **验证证据**：单元测试 + 集成测试
- **恢复指引**：`McpConfig.java`

## 测试矩阵

| 验收标准 | 测试断言 | 类型 |
|---------|---------|------|
| 7. 工具注册 | Spring 上下文启动后 MCP 工具可见 | 集成 |
| 8. 工具调用 | 对话中触发工具 → 回复含工具结果 | 集成 |

## Skill

- Skill: tdd
- Skill: verification-checklist
- 调研引用：[research.md](research.md)
