package com.vibecode.agent.llm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringAiLlmProviderTest {

    @Autowired
    private LlmProvider llmProvider;

    @Test
    @EnabledIfEnvironmentVariable(named = "DEEPSEEK_API_KEY", matches = ".+")
    void stream_shouldReturnTokensFromDeepSeek() {
        List<String> tokens = new ArrayList<>();
        var messages = List.of(
            LlmMessage.system("Reply in one word only."),
            LlmMessage.user("What color is the sky on a clear day?")
        );

        llmProvider.stream(messages, tokens::add);
        assertFalse(tokens.isEmpty(), "Should receive tokens from DeepSeek");
    }
}
