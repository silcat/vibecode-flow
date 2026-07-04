package com.vibecode.agent.llm;

import com.vibecode.agent.mcp.McpToolRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LlmConfig {

    @Bean
    public LlmProvider llmProvider(ChatClient.Builder builder, McpToolRegistry toolRegistry) {
        return new SpringAiLlmProvider(builder);
    }
}
