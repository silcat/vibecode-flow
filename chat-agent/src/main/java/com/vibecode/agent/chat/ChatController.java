package com.vibecode.agent.chat;

import com.vibecode.agent.session.Session;
import com.vibecode.agent.session.SessionNotFoundException;
import com.vibecode.agent.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;
    private final SessionService sessionService;

    @PostMapping(value = "/chat/{sessionId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@PathVariable String sessionId, @RequestBody ChatRequest request) {
        SseEmitter emitter = new SseEmitter(120_000L);
        new Thread(() -> {
            try {
                chatService.chat(sessionId, request.message(), token -> {
                    try {
                        emitter.send(SseEmitter.event().data(token));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                });
                emitter.complete();
            } catch (SessionNotFoundException e) {
                emitter.completeWithError(e);
            } catch (Exception e) {
                log.error("chat error session={}", sessionId, e);
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }

    @PostMapping("/sessions")
    public ApiResponse<Session> createSession(@RequestBody Map<String, String> body) {
        String title = body.getOrDefault("title", "New Chat");
        return ApiResponse.ok(sessionService.create(title));
    }

    @GetMapping("/sessions")
    public ApiResponse<List<Session>> listSessions() {
        return ApiResponse.ok(sessionService.list());
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable String id) {
        try {
            sessionService.get(id);
            sessionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (SessionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
