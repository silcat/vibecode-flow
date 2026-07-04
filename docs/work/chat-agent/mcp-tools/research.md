# 技术调研 — mcp-tools (MCP 客户端)

## 背景

chat-core 和 knowledge-base 已就绪。mcp-tools 需实现标准 MCP 客户端，连接外部 MCP Server，支持 agent 对话中调用工具。

MCP 协议基于 JSON-RPC，支持两种传输：stdio（进程通信）和 HTTP+SSE（网络通信）。

## 候选方案

### 方案 A：Spring AI MCP

| 维度 | 评估 |
|------|------|
| 功能匹配 | 原生集成 Spring AI，自动将 MCP 工具注册为 Spring AI Tool Function。与现有 ChatClient 无缝协作 |
| 技术质量 | Spring 官方维护，与 Spring AI 版本同步 |
| 集成成本 | 一个 starter 依赖 + 配置 MCP Server 连接信息 |
| 可维护性 | Spring 生态统一升级 |

### 方案 B：自研 MCP 客户端

| 维度 | 评估 |
|------|------|
| 功能匹配 | 需自研 JSON-RPC + 传输层 + 工具解析，约 300 行 |
| 集成成本 | 零依赖，全部自研 |
| 可维护性 | MCP 协议演进需手动适配 |

## 推荐结论

推荐 **方案 A：Spring AI MCP**。与现有技术栈一致，工具自动注册。

## 结论

已批准。2026-07-05
