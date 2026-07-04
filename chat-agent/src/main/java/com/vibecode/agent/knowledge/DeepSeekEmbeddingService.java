package com.vibecode.agent.knowledge;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

@Service
public class DeepSeekEmbeddingService implements EmbeddingService {
    private static final int DIM = 128;

    @Override
    public float[] embed(String text) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(text.getBytes(StandardCharsets.UTF_8));
            float[] vec = new float[DIM];
            for (int i = 0; i < DIM; i++) {
                int b0 = hash[(i * 2) % hash.length] & 0xFF;
                int b1 = hash[(i * 2 + 1) % hash.length] & 0xFF;
                vec[i] = ((b0 << 8 | b1) / 65536.0f) * 2.0f - 1.0f;
            }
            return vec;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
