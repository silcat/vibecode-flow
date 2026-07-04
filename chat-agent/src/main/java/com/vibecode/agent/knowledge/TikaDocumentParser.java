package com.vibecode.agent.knowledge;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import java.nio.file.Path;

@Component
public class TikaDocumentParser implements DocumentParser {
    private final Tika tika = new Tika();

    @Override
    public String parse(Path filePath) {
        try {
            return tika.parseToString(filePath.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse document: " + filePath, e);
        }
    }
}
