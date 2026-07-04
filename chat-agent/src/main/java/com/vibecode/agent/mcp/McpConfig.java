package com.vibecode.agent.mcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.Map;
import java.util.function.Function;

@Configuration
public class McpConfig {

    @Bean
    public McpToolRegistry mcpToolRegistry() {
        return new McpToolRegistry();
    }

    @Bean
    @Description("Echo back the input text")
    public Function<String, String> echoTool() {
        return input -> "Echo: " + input;
    }
}
