package com.vibecode.agent.session;

import com.vibecode.agent.llm.LlmMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {
    private SessionService service;

    @BeforeEach
    void setUp() { service = new SessionService(); }

    @Test
    void create_shouldReturnSessionWithIdAndTitle() {
        var s = service.create("Test");
        assertNotNull(s.getId());
        assertEquals("Test", s.getTitle());
    }

    @Test
    void list_shouldReturnAllCreatedSessions() {
        service.create("A");
        service.create("B");
        assertEquals(2, service.list().size());
    }

    @Test
    void delete_shouldRemoveSession() {
        var s = service.create("X");
        service.delete(s.getId());
        assertThrows(SessionNotFoundException.class, () -> service.get(s.getId()));
    }

    @Test
    void getHistory_differentSessions_shouldBeIsolated() {
        var s1 = service.create("One");
        var s2 = service.create("Two");
        service.addMessage(s1.getId(), LlmMessage.user("msg1"));
        service.addMessage(s2.getId(), LlmMessage.user("msg2"));

        assertEquals(1, service.getHistory(s1.getId()).size());
        assertEquals("msg1", service.getHistory(s1.getId()).get(0).content());
        assertEquals(1, service.getHistory(s2.getId()).size());
        assertEquals("msg2", service.getHistory(s2.getId()).get(0).content());
    }

    @Test
    void getHistory_unknownSession_shouldThrow() {
        assertThrows(SessionNotFoundException.class, () -> service.getHistory("nonexistent"));
    }
}
