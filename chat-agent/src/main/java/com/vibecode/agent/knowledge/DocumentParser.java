package com.vibecode.agent.knowledge;

import java.nio.file.Path;

public interface DocumentParser {
    String parse(Path filePath);
}
