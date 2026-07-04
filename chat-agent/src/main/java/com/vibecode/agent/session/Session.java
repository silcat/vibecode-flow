package com.vibecode.agent.session;

import java.time.Instant;
import java.util.UUID;

public class Session {
    private final String id;
    private final String title;
    private final Instant createdAt;

    public Session(String title) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public Instant getCreatedAt() { return createdAt; }
}
