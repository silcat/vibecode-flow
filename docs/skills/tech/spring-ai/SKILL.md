# Spring AI 技能

## 是什么

Spring 生态的 AI 集成框架，提供 `ChatClient`、`EmbeddingClient`、Function Calling 等统一抽象，底层可切换不同 LLM Provider。

## 快速接入

### 依赖

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
    <version>1.0.0-M4</version>
</dependency>
```

### 最小可用配置

```yaml
spring:
  ai:
    openai:
      api-key: ${DEEPSEEK_API_KEY}
      base-url: https://api.deepseek.com/v1
      chat:
        options:
          model: deepseek-chat
          temperature: 0.7
```

### 核心代码片段

```java
// 流式对话
@Autowired
private ChatClient chatClient;

public Flux<String> stream(String userMessage) {
    return chatClient.prompt()
        .user(userMessage)
        .stream()
        .content();
}

// Function Calling 注册
@Bean
public ChatClient chatClient(ChatClient.Builder builder,
                               List<FunctionCallback> toolCallbacks) {
    return builder
        .defaultFunctions(toolCallbacks.toArray(new FunctionCallback[0]))
        .build();
}
```

## 已知坑

| 坑 | 现象 | 解法 |
|----|------|------|
| DeepSeek 兼容性 | Spring AI 默认走 OpenAI 格式，与 DeepSeek 完全兼容，无需额外适配 | 只需改 base-url + api-key |
| M4 版本不稳定 | API 在 milestone 版本间可能有破坏性变更 | 锁定版本号，不要用 RELEASE |
| 流式超时 | 长文本流式响应可能超时 | 配置 `spring.ai.openai.chat.options.timeout` |

## 参考

| 资料 | 链接 |
|------|------|
| Spring AI 官方文档 | https://docs.spring.io/spring-ai/reference/ |
| DeepSeek API 兼容说明 | https://platform.deepseek.com/api-docs/ |

更多资料见 [refer/](refer/)
