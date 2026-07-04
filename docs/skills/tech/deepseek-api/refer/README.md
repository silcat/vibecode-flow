# DeepSeek API 学习资料

## 官方入口

| 资料 | 链接 |
|------|------|
| API 文档首页 | https://platform.deepseek.com/api-docs/ |
| 对话补全 API | https://platform.deepseek.com/api-docs/chat-completion |
| Embedding API | https://platform.deepseek.com/api-docs/embeddings |
| 平台控制台 | https://platform.deepseek.com/ |
| 定价页面 | https://platform.deepseek.com/pricing |

## 关键概念

### 对话模型

| 模型 | 上下文长度 | 用途 |
|------|-----------|------|
| deepseek-chat | 64K | 通用对话，性价比最高 |
| deepseek-reasoner | 64K | 推理模型（DeepSeek-R1），复杂推理场景 |

### 请求格式（兼容 OpenAI）

```json
POST https://api.deepseek.com/v1/chat/completions
Authorization: Bearer <your-api-key>
Content-Type: application/json

{
  "model": "deepseek-chat",
  "messages": [
    {"role": "system", "content": "You are a helpful assistant."},
    {"role": "user", "content": "Hello!"}
  ],
  "temperature": 0.7,
  "max_tokens": 4096,
  "stream": true
}
```

### 流式响应 SSE 解析

```
data: {"id":"...","choices":[{"delta":{"content":"Hello"},"index":0}]}
data: {"id":"...","choices":[{"delta":{"content":" World"},"index":0}]}
data: [DONE]
```

### Embedding

```json
POST https://api.deepseek.com/v1/embeddings
{
  "model": "text-embedding-3-small",
  "input": "要向量化的文本"
}
// 返回 1024 维 float[] 向量
```

### 速率限制

| 套餐 | RPM (每分钟请求) | TPM (每分钟 token) |
|------|-----------------|-------------------|
| 免费 | 30 | 100,000 |
| 付费 | 100+ | 500,000+ |

超出返回 HTTP 429，需要指数退避重试。

### 常用参数

| 参数 | 默认值 | 说明 |
|------|--------|------|
| temperature | 0.7 | 0-2，越高越随机 |
| top_p | 1.0 | 核采样阈值 |
| max_tokens | 4096 | 最大输出 token 数 |
| stream | false | 是否流式返回 |
