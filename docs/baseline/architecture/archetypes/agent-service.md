# Agent 服务原型

## 识别条件

模块核心是 LLM 驱动的智能体：对话、调用工具、检索知识库。选此原型后，以下目录结构、分层规则为固定模板，直接套用。`<base-package>` 替换为实际包名。

---

## 目录结构

```
<base-package>/
├── AgentApplication.java       # Spring Boot 入口
├── llm/                        # LLM 抽象层
│   ├── LlmProvider.java        # 统一接口（stream + complete）
│   ├── LlmMessage.java         # 消息模型（system/user/assistant）
│   ├── LlmConfig.java          # Bean 装配
│   └── *Provider.java          # 各 LLM 实现
├── chat/                       # 对话层
│   ├── ChatController.java     # REST/SSE 端点
│   ├── ChatService.java        # 对话编排（上下文构建 → LLM → 存储）
│   └── ApiResponse.java        # 统一响应 {code, message, data}
├── session/                    # 会话管理
│   ├── Session.java            # 会话模型
│   └── SessionService.java     # 会话 CRUD + 对话历史
├── mcp/                        # MCP 工具层
│   ├── McpConfig.java          # MCP Server 连接配置 + 工具注册
│   ├── McpToolRegistry.java    # 工具注册与调用
│   ├── ToolDefinition.java     # 工具定义模型
│   └── ToolNotFoundException.java
└── knowledge/                  # 知识库 RAG
    ├── DocumentService.java    # 文档上传/检索/删除
    ├── DocumentParser.java     # 文档解析接口
    ├── TikaDocumentParser.java # Apache Tika 实现
    ├── EmbeddingService.java   # 向量嵌入接口
    ├── DeepSeekEmbeddingService.java
    ├── VectorStore.java        # 向量存储接口
    ├── LuceneVectorStore.java  # Lucene HNSW 实现
    └── DocumentController.java # 文档管理 REST API
```

---

## 依赖方向（固定）

```
Controller → Service → LlmProvider / VectorStore / EmbeddingService
                    → SessionService（同级协作）
```

- ChatController 依赖 ChatService + SessionService
- DocumentController 依赖 DocumentService
- ChatService 依赖 LlmProvider + SessionService
- **禁止 Controller 直接调 LlmProvider / VectorStore**

---

## 各模块说明

### llm/ — LLM 抽象层

**职责**：封装 LLM Provider，提供统一的流式和同步调用接口。
**接口设计**：

```java
public interface LlmProvider {
    void stream(String systemPrompt, List<LlmMessage> history, String userMessage,
                LlmStreamCallback callback);
    String complete(String systemPrompt, List<LlmMessage> history, String userMessage);
}
```

### chat/ — 对话层

**职责**：对话端点（SSE 流式）+ 对话编排（构建上下文 → 调 LLM → 存储历史）。
**端点**：
- `POST /api/chat/stream` — SSE 流式对话
- `GET /api/sessions` — 会话列表
- `POST /api/sessions` — 新建会话

### session/ — 会话管理

**职责**：会话 CRUD + 对话历史管理。
**存储**：内存（ConcurrentHashMap），可替换为 Redis。

### mcp/ — MCP 工具层

**职责**：MCP Server 连接 + 工具注册 + 调用分发。
**核心类**：
- `McpConfig` — 扫描 `@Tool` 注解并注册
- `McpToolRegistry` — 工具查找与调用
- `ToolDefinition` — {name, description, parameters}

### knowledge/ — 知识库 RAG

**职责**：文档解析 → 向量化 → 存储 → 检索。
**流程**：上传文件 → Tika 解析 → Embedding → Lucene HNSW 索引 → Top-K 检索。
**端点**：
- `POST /api/documents/upload` — 上传文档
- `GET /api/documents/search?q=xxx` — 语义检索
- `DELETE /api/documents/{id}` — 删除文档
