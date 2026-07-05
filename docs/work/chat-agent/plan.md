---
branch: codex/feature-chat-agent
status: completed
type: master
requirement: requirement.md
created: 2026-07-05
updated: 2026-07-05
verified: 2026-07-05
blocker: none
---

# chat-agent 总计划

## 项目章程

- **北极星**：构建一个企业级 AI 聊天 agent，支持对话、知识库 RAG、MCP 工具调用
- **完成定义**：三个子计划全部通过闭环审计 + 集成审计通过
- **安全硬约束**：无
- **范围分层**：
  - Tier 1 → chat-core（对话基础 + Web UI）
  - Tier 2 → knowledge-base（RAG 文档问答）
  - Tier 3 → mcp-tools（MCP 工具调用）
- **明确不在范围**：多租户、权限审计、高可用、其他 LLM 适配、Spring Cloud 微服务接入

## 当前基线

- 后端：Java + Spring Boot 3.2，端口 8090
- 前端：Vue 3
- LLM：DeepSeek API（抽象层）
- 向量存储：HNSW（Apache Lucene 内嵌）
- 独立项目，后续接入 Spring Cloud 体系

## 子计划清单

| 子计划目录 | 依赖 | 可并行 |
|-----------|------|--------|
| chat-core | 无 | ✅ |
| knowledge-base | chat-core | ✅ |
| mcp-tools | chat-core | ✅ |

## 依赖图

`mermaid
graph TD
    chat-core[chat-core 对话基础] --> knowledge-base[knowledge-base RAG]
    chat-core --> mcp-tools[mcp-tools MCP]
`

chat-core 先跑通基础对话 + Web UI，knowledge-base 和 mcp-tools 可以并行推进。

## 集成关卡

- [ ] 对话中同时触发 RAG 检索和 MCP 工具调用，结果正确融合到回复中
- [ ] 上传文档后立即对话可检索到新文档内容
- [ ] MCP 工具调用失败时对话不中断，agent 优雅降级并告知用户
- [ ] 三个模块共存的完整流程：用户提问 → RAG 检索 → MCP 工具调用 → LLM 生成回复

## Skill

- Skill: none