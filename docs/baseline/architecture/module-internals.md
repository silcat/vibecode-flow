# 分层架构规范

## 用途

新增模块时，先选原型，再回答对应问题清单即可产出模块内部结构。

## 原型选择

| 原型 | 适用场景 | 问题清单 |
|------|---------|---------|
| crud-service | 围绕数据实体的标准业务服务 | [archetypes/crud-service.md](archetypes/crud-service.md) |
| agent-service | LLM 驱动的 AI 智能体 | [archetypes/agent-service.md](archetypes/agent-service.md) |

无匹配原型时，标记为 `custom`，在本文件底部追加模块专属结构说明。

## 已实施模块

### chat-agent
- 原型：[agent-service](archetypes/agent-service.md)

```
com.vibecode.agent/
├── AgentApplication.java          # Spring Boot 入口
├── llm/                           # LLM 抽象层
│   ├── LlmProvider.java           # 统一 LLM 接口（stream + complete）
│   ├── LlmStreamCallback.java     # 流式回调
│   ├── LlmMessage.java            # 消息模型（system/user/assistant）
│   ├── SpringAiLlmProvider.java   # Spring AI ChatClient 适配 DeepSeek
│   └── LlmConfig.java             # Spring Bean 装配
├── chat/                          # 对话层
│   ├── ChatController.java        # SSE 流式端点 + 会话 CRUD
│   ├── ChatService.java           # 对话编排（上下文构建 → LLM → 存储）
│   └── ApiResponse.java           # 统一响应 {code, message, data}
├── session/                       # 会话管理
│   ├── Session.java               # 会话模型（内存）
│   └── SessionService.java        # 会话 CRUD + 对话历史
├── mcp/                            # MCP 工具层
│   ├── McpConfig.java              # MCP Server 连接配置 + 工具注册
│   ├── McpToolRegistry.java        # 工具注册与调用
│   ├── ToolDefinition.java         # 工具定义模型
│   └── ToolNotFoundException.java  # 工具未找到异常
├── knowledge/                     # 知识库 RAG
    ├── DocumentService.java       # 文档上传/检索/删除
    ├── DocumentParser.java        # 文档解析接口
    ├── TikaDocumentParser.java    # Apache Tika 实现
    ├── EmbeddingService.java      # 向量嵌入接口
    ├── DeepSeekEmbeddingService.java  # DeepSeek Embedding API
    ├── VectorStore.java           # 向量存储接口
    ├── LuceneVectorStore.java     # Lucene HNSW 实现
    └── DocumentController.java    # 文档管理 REST API
```

## 更新触发条件

- 新增模块 → 选原型、回答对应问题清单、在本文件底部追加模块结构
- 新增原型 → 在 archetypes/ 下创建新原型文件、更新原型选择表
- 模块结构重大变化 → 更新本文件对应模块的结构描述
