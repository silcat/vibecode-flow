package com.vibecode.agent.chat;

import com.vibecode.agent.llm.LlmMessage;
import com.vibecode.agent.llm.LlmProvider;
import com.vibecode.agent.session.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ChatServiceTest {
    private ChatService service;
    private SessionService sessions;
    private List<String> capturedTokens;

    @BeforeEach
    void setUp() {
        sessions = new SessionService();
        LlmProvider mock = new LlmProvider() {
            @Override
            public void stream(List<LlmMessage> msgs, com.vibecode.agent.llm.LlmStreamCallback cb) {
                cb.onToken("Hello");
                cb.onToken(" from ");
                cb.onToken("mock");
            }
            @Override
            public String complete(List<LlmMessage> msgs) { return "Hello from mock"; }
        };
        service = new ChatService(mock, sessions);
        capturedTokens = new ArrayList<>();
    }

    @Test
    void chat_shouldStreamAllTokens() {
        var s = sessions.create("Test");
        service.chat(s.getId(), "Hi", capturedTokens::add);
        assertEquals(3, capturedTokens.size());
        assertEquals("Hello", capturedTokens.get(0));
    }

    @Test
    void chat_shouldBuildHistoryWithUserAndAssistant() {
        var s = sessions.create("Test");
        service.chat(s.getId(), "First", t -> {});
        var history = sessions.getHistory(s.getId());
        assertTrue(history.size() >= 2);
        assertEquals("user", history.get(0).role());
        assertEquals("assistant", history.get(history.size() - 1).role());
    }

    @Test
    void chat_shouldStoreAssistantResponseInHistory() {
        var s = sessions.create("Test");
        service.chat(s.getId(), "Hello", t -> {});
        var last = sessions.getHistory(s.getId()).get(sessions.getHistory(s.getId()).size() - 1);
        assertEquals("Hello from mock", last.content());
    }
}
