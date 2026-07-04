package com.vibecode.agent.session;

import com.vibecode.agent.llm.LlmMessage;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final Map<String, List<LlmMessage>> histories = new ConcurrentHashMap<>();

    public Session create(String title) {
        var s = new Session(title);
        sessions.put(s.getId(), s);
        histories.put(s.getId(), new ArrayList<>());
        return s;
    }

    public List<Session> list() { return List.copyOf(sessions.values()); }

    public Session get(String id) {
        var s = sessions.get(id);
        if (s == null) throw new SessionNotFoundException(id);
        return s;
    }

    public void delete(String id) {
        sessions.remove(id);
        histories.remove(id);
    }

    public List<LlmMessage> getHistory(String sessionId) {
        var h = histories.get(sessionId);
        if (h == null) throw new SessionNotFoundException(sessionId);
        return h;
    }

    public void addMessage(String sessionId, LlmMessage message) {
        var h = histories.get(sessionId);
        if (h == null) throw new SessionNotFoundException(sessionId);
        h.add(message);
    }
}
