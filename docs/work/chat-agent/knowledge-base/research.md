# 技术调研 — knowledge-base (RAG)

## 背景

chat-core 已就绪。knowledge-base 子计划需实现文档上传 → 向量化 → RAG 检索。
已确定：向量存储用 HNSW（Lucene 内嵌索引）。需调研：文档解析、向量嵌入模型、分块策略。

## 候选方案

### 文档解析

| 方案 | 覆盖 | 集成成本 |
|------|------|---------|
| Apache Tika | PDF/Word/Markdown/HTML 全覆盖 | 一个依赖，一行 `tika.parseToString()` |
| Apache PDFBox + POI | PDF + Word | 两个依赖，需手动处理不同格式 |
| 仅 Markdown | 只支持 .md | 零依赖，但范围窄 |

### 向量嵌入模型

| 方案 | 维度 | 集成 |
|------|------|------|
| DeepSeek Embedding API | 未知 | 与现有 DeepSeek API key 复用，一个 HTTP 调用 |
| BGE-M3（本地） | 1024 | 需 GPU/ONNX Runtime，集成成本高 |
| text2vec-large-chinese（本地） | 1024 | 同上 |

### 分块策略

| 方案 | 适用场景 |
|------|---------|
| 固定大小（500 字符，100 重叠） | 通用文档，简单可靠 |
| 语义分块（按段落/标题） | 结构化文档 |
| 递归分块（固定 → 语义 fallback） | 混合场景，实现复杂 |

## 推荐结论

- **文档解析**：Apache Tika — 一个依赖覆盖全部格式
- **向量嵌入**：DeepSeek Embedding API — 复用现有 key，零额外部署
- **分块策略**：固定大小（500 字符 + 100 重叠）— 简单可靠，后续按需升级
- **向量存储**：HNSW via Lucene — 内嵌进程，零外部依赖

风险：Lucene HNSW 索引在独立阶段足够，微服务阶段需评估是否需要切 Milvus。

## 结论

已批准。2026-07-05
