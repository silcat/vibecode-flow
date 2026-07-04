# chat-core 需求子集

> 从 [总需求](../requirement.md) 提取，副本冻结。

## 范围内

构建 agent 的基础对话能力和 Web 聊天界面。

- 基于 DeepSeek API 的自然语言对话，流式返回
- 多轮对话上下文记忆（同一会话内）
- 会话管理（新建/切换/删除）
- LLM 抽象层，首期实现 DeepSeek adapter
- Vue 3 Web 聊天界面

## 范围外

- 知识库 RAG（子计划 knowledge-base）
- MCP 工具调用（子计划 mcp-tools）
- 多租户、权限审计、高可用

## 验收标准

1. 用户发送消息 → 3 秒内开始流式返回 LLM 回复
2. 同一会话内连续对话 → LLM 能引用上文内容
3. 创建新会话 → 上下文隔离，旧会话历史可回看