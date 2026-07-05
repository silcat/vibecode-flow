# Lucene HNSW 向量存储技能

## 是什么

基于 Apache Lucene 的本地向量存储方案。利用 Lucene 9.x 内置的 HNSW（Hierarchical Navigable Small World）图索引实现近似最近邻搜索，无需外部向量数据库。

## 快速接入

### 依赖

```xml
<dependency>
    <groupId>org.apache.lucene</groupId>
    <artifactId>lucene-core</artifactId>
    <version>9.9.2</version>
</dependency>
```

### 核心代码片段

```java
// 创建 HNSW 索引
HnswGraphBuilder builder = new HnswGraphBuilder(
    vectors,                           // float[][] 向量集合
    HnswGraphBuilder.randomSeed(42),   // 随机种子
    16,                                // M: 每个节点最大连接数
    100);                              // efConstruction: 构建时搜索宽度
HnswGraph graph = builder.build();

// 查询 Top-K
NeighborQueue results = HnswGraphSearcher.search(
    queryVector,    // float[] 查询向量
    5,              // K
    vectors,        // 向量源
    graph,          // 索引图
    50,             // efSearch: 搜索时搜索宽度
    random);

// 保存/加载索引
try (OutputStream out = Files.newOutputStream(path)) {
    HnswGraphWriter.write(graph, out);
}
```

## 已知坑

| 坑 | 现象 | 解法 |
|----|------|------|
| 维度不匹配 | 查询向量维度与索引不一致时报异常 | 确保 embedding 模型返回固定维度 |
| 内存占用 | 百万级向量内存需求高 | M 和 efConstruction 参数调小可降内存 |
| 增量写入 | Lucene HNSW 原生不支持增量 add | 批量重建全量索引或使用 Lucene Codec 层 |
| 并发读 | 多线程读安全，写不安全 | 写时加锁或读写分离 |

## 参考

| 资料 | 链接 |
|------|------|
| Lucene HNSW Javadoc | https://lucene.apache.org/core/9_9_0/core/org/apache/lucene/util/hnsw/package-summary.html |
| 近似最近邻搜索概述 | https://github.com/erikbern/ann-benchmarks |

更多资料见 [refer/](refer/)
