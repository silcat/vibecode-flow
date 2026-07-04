package com.vibecode.agent.llm;

@FunctionalInterface
public interface LlmStreamCallback {
    void onToken(String token);
}
