# DeepSeek API 技能

## 是什么

DeepSeek 提供的大模型 API 服务，兼容 OpenAI SDK，支持对话（chat）、嵌入（embedding）模型。

## 快速接入

### 依赖

无需额外 SDK。兼容 OpenAI 格式，可直接用 Spring AI / openai-java / 标准 HTTP 调用。

### 最小可用配置

```yaml
# Spring Boot application.yml
deepseek:
  api-key: ${DEEPSEEK_API_KEY}
  base-url: https://api.deepseek.com/v1
  chat-model: deepseek-chat
  embedding-model: text-embedding-3-small
```

### 核心代码片段

```java
// 直接 HTTP 调用（不依赖 Spring AI）
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.deepseek.com/v1/chat/completions"))
    .header("Authorization", "Bearer " + apiKey)
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
    .build();
HttpResponse<String> response = client.send(request,
    HttpResponse.BodyHandlers.ofString());
```

## 已知坑

| 坑 | 现象 | 解法 |
|----|------|------|
| 账户余额不足 | 返回 402 Payment Required | 检查 https://platform.deepseek.com 的余额 |
| 速率限制 | 返回 429 Too Many Requests | 降低并发，或升级套餐 |
| embedding 维度 | DeepSeek embedding 返回 1024 维向量 | 向量存储索引维度必须匹配 |

## 参考

| 资料 | 链接 |
|------|------|
| DeepSeek API 文档 | https://platform.deepseek.com/api-docs/ |
| 定价页面 | https://platform.deepseek.com/pricing |

更多资料见 [refer/](refer/)
