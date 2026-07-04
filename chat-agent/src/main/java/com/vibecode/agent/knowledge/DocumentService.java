package com.vibecode.agent.knowledge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);
    private final VectorStore vectorStore;
    private final EmbeddingService embeddingService;
    private final DocumentParser parser;

    public DocumentService(VectorStore vectorStore, EmbeddingService embeddingService, DocumentParser parser) {
        this.vectorStore = vectorStore;
        this.embeddingService = embeddingService;
        this.parser = parser;
    }

    public Document upload(Path filePath) {
        String docId = UUID.randomUUID().toString().substring(0, 8);
        String filename = filePath.getFileName().toString();
        String content = parser.parse(filePath);

        List<String> chunks = chunk(content, 500, 100);
        for (int i = 0; i < chunks.size(); i++) {
            String chunkId = docId + "-" + i;
            float[] vec = embeddingService.embed(chunks.get(i));
            var dc = new DocumentChunk(docId, filename, chunks.get(i), 0);
            vectorStore.add(chunkId, vec, dc);
        }

        log.info("document indexed id={} file={} chunks={}", docId, filename, chunks.size());
        return new Document(docId, filename, chunks.size());
    }

    public List<DocumentChunk> search(String query, int k) {
        float[] queryVec = embeddingService.embed(query);
        return vectorStore.search(queryVec, k);
    }

    public void delete(String documentId) {
        vectorStore.deleteByDocumentId(documentId);
        log.info("document deleted id={}", documentId);
    }

    private List<String> chunk(String text, int size, int overlap) {
        List<String> result = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + size, text.length());
            result.add(text.substring(start, end));
            start += (size - overlap);
        }
        return result;
    }
}
