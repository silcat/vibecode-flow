package com.vibecode.agent.session;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String id) { super("Session not found: " + id); }
}
