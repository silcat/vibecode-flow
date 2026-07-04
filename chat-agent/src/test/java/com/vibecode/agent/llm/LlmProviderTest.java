package com.vibecode.agent.llm;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LlmProviderTest {

    @Test
    void stream_shouldDeliverTokensViaCallback() {
        LlmProvider mock = new LlmProvider() {
            @Override
            public void stream(List<LlmMessage> messages, LlmStreamCallback callback) {
                callback.onToken("Hello");
                callback.onToken(" World");
            }
            @Override
            public String complete(List<LlmMessage> messages) { return ""; }
        };

        List<String> tokens = new ArrayList<>();
        mock.stream(List.of(LlmMessage.user("hi")), tokens::add);

        assertEquals(2, tokens.size());
        assertEquals("Hello", tokens.get(0));
        assertEquals(" World", tokens.get(1));
    }

    @Test
    void complete_shouldReturnConcatenatedTokens() {
        LlmProvider mock = new LlmProvider() {
            @Override
            public void stream(List<LlmMessage> messages, LlmStreamCallback callback) {
                callback.onToken("A");
                callback.onToken("B");
            }

            @Override
            public String complete(List<LlmMessage> messages) {
                StringBuilder sb = new StringBuilder();
                stream(messages, sb::append);
                return sb.toString();
            }
        };

        assertEquals("AB", mock.complete(List.of(LlmMessage.user("test"))));
    }
}
