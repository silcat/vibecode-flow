package com.vibecode.agent.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class SpringAiLlmProvider implements LlmProvider {
    private static final Logger log = LoggerFactory.getLogger(SpringAiLlmProvider.class);
    private final ChatClient chatClient;

    public SpringAiLlmProvider(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public void stream(List<LlmMessage> messages, LlmStreamCallback callback) {
        Flux<ChatResponse> flux = chatClient.prompt()
                .messages(toSpringMessages(messages))
                .stream()
                .chatResponse();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();

        flux.doOnComplete(latch::countDown)
            .doOnError(e -> { error.set(e); latch.countDown(); })
            .subscribe(response -> {
                String content = response.getResult().getOutput().getText();
                if (content != null && !content.isEmpty()) {
                    callback.onToken(content);
                }
            });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LlmException("Stream interrupted", e);
        }

        if (error.get() != null) {
            throw new LlmException("Stream failed", error.get());
        }
    }

    @Override
    public String complete(List<LlmMessage> messages) {
        String result = chatClient.prompt()
                .messages(toSpringMessages(messages))
                .call()
                .content();
        return result != null ? result : "";
    }

    private List<Message> toSpringMessages(List<LlmMessage> messages) {
        return messages.stream().map(m -> switch (m.role()) {
            case "system" -> new SystemMessage(m.content());
            case "assistant" -> new AssistantMessage(m.content());
            default -> new UserMessage(m.content());
        }).map(m -> (Message) m).toList();
    }
}
