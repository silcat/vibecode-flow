package com.vibecode.agent.knowledge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DocumentServiceTest {
    private DocumentService service;

    @BeforeEach
    void setUp() {
        service = new DocumentService(new InMemoryVectorStore(), new StubEmbeddingService(), new TikaDocumentParser());
    }

    @Test
    void upload_validPdf_shouldIndexSuccessfully(@TempDir Path tmp) throws Exception {
        Path pdf = tmp.resolve("test.pdf");
        Files.writeString(pdf, "PDF content for testing");

        var doc = service.upload(pdf);
        assertNotNull(doc.id());
        assertEquals("test.pdf", doc.filename());
    }

    @Test
    void search_afterUpload_shouldFindRelevantChunks(@TempDir Path tmp) throws Exception {
        Path pdf = tmp.resolve("doc.pdf");
        Files.writeString(pdf, "The sky is blue on a clear day. Clouds are white.");
        service.upload(pdf);

        List<DocumentChunk> results = service.search("What color is the sky?", 3);
        assertFalse(results.isEmpty());
    }

    @Test
    void delete_shouldRemoveFromIndex(@TempDir Path tmp) throws Exception {
        Path pdf = tmp.resolve("doc.pdf");
        Files.writeString(pdf, "test content");
        var doc = service.upload(pdf);

        service.delete(doc.id());

        List<DocumentChunk> results = service.search("test", 3);
        assertTrue(results.isEmpty());
    }
}

class InMemoryVectorStore implements VectorStore {
    private final java.util.Map<String, float[]> store = new java.util.HashMap<>();
    private final java.util.Map<String, DocumentChunk> chunks = new java.util.HashMap<>();
    @Override public void add(String id, float[] vector, DocumentChunk chunk) { store.put(id, vector); chunks.put(id, chunk); }
    @Override public List<DocumentChunk> search(float[] query, int k) { return List.copyOf(chunks.values()).subList(0, Math.min(k, chunks.size())); }
    @Override public void deleteByDocumentId(String docId) { store.clear(); chunks.clear(); }
}

class StubEmbeddingService implements EmbeddingService {
    @Override public float[] embed(String text) { return new float[]{1.0f, 0.5f}; }
}
