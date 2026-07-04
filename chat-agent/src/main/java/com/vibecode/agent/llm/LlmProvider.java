package com.vibecode.agent.llm;

import java.util.List;

public interface LlmProvider {
    void stream(List<LlmMessage> messages, LlmStreamCallback callback);
    String complete(List<LlmMessage> messages);
}
