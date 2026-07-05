# Spring AI 学习资料

## 官方入口

| 资料 | 链接 |
|------|------|
| 官方文档首页 | https://docs.spring.io/spring-ai/reference/ |
| API 参考 | https://docs.spring.io/spring-ai/reference/api.html |
| Chat Client 指南 | https://docs.spring.io/spring-ai/reference/api/chatclient.html |
| Embedding Client 指南 | https://docs.spring.io/spring-ai/reference/api/embeddings.html |
| Function Calling | https://docs.spring.io/spring-ai/reference/api/functions.html |

## 版本说明

Spring AI 当前为 1.0.0-M4（Milestone），API 尚未稳定，锁定版本号。

Milestone 仓库地址：
```xml
<repository>
    <id>spring-milestones</id>
    <url>https://repo.spring.io/milestone</url>
</repository>
```

## 关键概念

### ChatClient 使用模式

```java
// 1. 简单字符串返回
String response = chatClient.prompt().user("hello").call().content();

// 2. 流式返回
Flux<String> stream = chatClient.prompt().user("hello").stream().content();

// 3. 带 System Prompt
chatClient.prompt()
    .system("You are a helpful assistant.")
    .user("hello")
    .call().content();

// 4. 带历史消息
chatClient.prompt()
    .messages(
        new UserMessage("hello"),
        new AssistantMessage("Hi!"),
        new UserMessage("tell me a joke"))
    .call().content();
```

### Function Calling 注册

```java
@Bean
@Description("Get the current weather for a location")
public Function<WeatherRequest, WeatherResponse> weatherFunction() {
    return request -> new WeatherResponse(request.location(), 22.0, "sunny");
}

// 自动发现所有 @Description Bean 并注册
@Bean
public ChatClient chatClient(ChatClient.Builder builder,
                               List<FunctionCallback> toolCallbacks) {
    return builder
        .defaultFunctions(toolCallbacks.toArray(new FunctionCallback[0]))
        .build();
}
```

### DeepSeek 适配要点

Spring AI 的 OpenAI starter 通过 base-url 指向 DeepSeek：
- Chat: `https://api.deepseek.com/v1`（兼容 `/chat/completions`）
- Embedding: DeepSeek 支持 `text-embedding-3-small`，1024 维
- DeepSeek 不支持的部分：图片生成、语音、GPT-4V 视觉等，不可用
