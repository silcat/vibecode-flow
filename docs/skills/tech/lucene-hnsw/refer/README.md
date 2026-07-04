# Lucene HNSW 向量存储 学习资料

## 官方入口

| 资料 | 链接 |
|------|------|
| HNSW 包文档 | https://lucene.apache.org/core/9_9_0/core/org/apache/lucene/util/hnsw/package-summary.html |
| Lucene 官方文档 | https://lucene.apache.org/core/9_9_0/index.html |
| HNSW 论文 | https://arxiv.org/abs/1603.09320 |

## 关键概念

### HNSW 算法简介

HNSW（Hierarchical Navigable Small World）是一种近似最近邻搜索算法，构建多层图结构：
- 底层（0 层）：包含所有节点，密集连接
- 上层：逐层稀疏，随机抽样节点
- 搜索：从最上层开始贪心下降，到底层精确搜索

### 三个核心参数

| 参数 | 含义 | 推荐值 | 影响 |
|------|------|--------|------|
| M | 每个节点最大连接数 | 16-32 | M↑ → 精度↑、内存↑、构建慢 |
| efConstruction | 构建时搜索宽度 | 100-200 | ↑ → 索引质量↑、构建慢 |
| efSearch | 查询时搜索宽度 | 50-100 | ↑ → 查询精度↑、查询慢 |

### 内存估算

```
索引内存 ≈ N × (M × 4 字节 × 维度 + 图结构开销)
```

示例：100 万条 1024 维向量，M=16
≈ 1,000,000 × (16 × 4 × 1024 + 开销) ≈ 65GB+

### 增量写入方案

Lucene HNSW 原生不支持增量 add。替代方案：
1. **全量重建**：定时 batch 重建整个索引（适合数据量小、更新频率低）
2. **Lucene Codec 层**：用 Lucene 的 `IndexWriter` + `KnnVectorField`，支持文档级增删
3. **外挂增量**：新数据先存内存，达到阈值后合并重建

### Lucene Codec 方式（推荐生产环境）

```java
// 用 Lucene 的 KnnVectorField 替代底层 HNSW API
Directory dir = FSDirectory.open(Paths.get("index/"));
IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig());

Document doc = new Document();
doc.add(new KnnVectorField("vector", vector, VectorSimilarityFunction.COSINE));
doc.add(new StringField("id", docId, Field.Store.YES));
doc.add(new TextField("content", text, Field.Store.YES));
writer.addDocument(doc);
writer.commit();

// 查询
IndexReader reader = DirectoryReader.open(dir);
IndexSearcher searcher = new IndexSearcher(reader);
KnnVectorQuery query = new KnnVectorQuery("vector", queryVector, 5);
TopDocs results = searcher.search(query, 5);
```

## 对比其他方案

| 方案 | 部署复杂度 | 扩展性 | 性能 | 适用场景 |
|------|-----------|--------|------|---------|
| Lucene HNSW (Codec) | 低（嵌入式） | 中等 | 高 | 百万级，本地嵌入 |
| Milvus | 高（独立服务） | 高 | 高 | 亿级，分布式 |
| Pinecone | 无（SaaS） | 高 | 高 | 不想管基础设施 |
| Redis Stack | 中（已有 Redis 可用） | 中 | 中 | 已有 Redis 基础设施 |
