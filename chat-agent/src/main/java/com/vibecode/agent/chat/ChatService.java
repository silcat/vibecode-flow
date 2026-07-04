package com.vibecode.agent.chat;

import com.vibecode.agent.llm.LlmMessage;
import com.vibecode.agent.llm.LlmProvider;
import com.vibecode.agent.llm.LlmStreamCallback;
import com.vibecode.agent.session.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final LlmProvider llmProvider;
    private final SessionService sessionService;

    public ChatService(LlmProvider llmProvider, SessionService sessionService) {
        this.llmProvider = llmProvider;
        this.sessionService = sessionService;
    }

    public void chat(String sessionId, String userMessage, LlmStreamCallback callback) {
        sessionService.get(sessionId);
        sessionService.addMessage(sessionId, LlmMessage.user(userMessage));

        List<LlmMessage> history = new ArrayList<>(sessionService.getHistory(sessionId));

        StringBuilder fullResponse = new StringBuilder();
        llmProvider.stream(history, token -> {
            fullResponse.append(token);
            callback.onToken(token);
        });

        sessionService.addMessage(sessionId, LlmMessage.assistant(fullResponse.toString()));
        log.info("chat complete session={} len={}", sessionId, fullResponse.length());
    }
}
