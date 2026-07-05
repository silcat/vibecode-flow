---
branch: codex/feature-chat-agent
status: completed
type: sub
requirement: requirement.md
created: 2026-07-05
updated: 2026-07-05
blocker: none
---

# knowledge-base 计划

## 当前基线

- chat-core 已就绪（对话 + 会话管理 + Web UI）
- 依赖 DeepSeek API key
- 调研：[research.md](research.md)

## 阶段 1：文档解析 + 向量存储

- **状态**：⏳ PLANNED
- **依赖**：无
- **目标**：文档上传 API → Tika 解析 → 分块 → DeepSeek Embedding → Lucene HNSW 索引
- **触及面**：
  - `chat-agent/src/main/java/com/vibecode/agent/knowledge/` — DocumentService, VectorStore, EmbeddingService
  - `chat-agent/src/main/java/com/vibecode/agent/chat/` — ChatService 集成 RAG
- **公共契约**：`POST /api/documents`（上传）、`GET /api/documents`（列表）、`DELETE /api/documents/{id}`
- **闭环关卡**：
  - [ ] 上传 PDF → Tika 解析 → HNSW 索引成功
  - [ ] 删除文档 → 向量条目移除
  - [ ] 提问 → RAG 检索注入上下文 → 回复含文档信息
- **验证证据**：单元测试 + curl 验证
- **恢复指引**：`DocumentController.java`

## 测试矩阵

| 验收标准 | 测试断言 | 类型 |
|---------|---------|------|
| 4. 上传 PDF → 向量化 | 解析+分块+索引全流程无异常 | 集成 |
| 5. RAG 检索回复 | 回复含上传文档中的关键信息 | 集成 |
| 6. 删除文档不可检索 | 删除后检索结果不含该文档片段 | 单元 |

## Skill

- Skill: tdd
- Skill: verification-checklist
- 调研引用：[research.md](research.md)
