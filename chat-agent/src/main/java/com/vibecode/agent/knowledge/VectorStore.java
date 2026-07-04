package com.vibecode.agent.knowledge;

import java.util.List;

public interface VectorStore {
    void add(String id, float[] vector, DocumentChunk chunk);
    List<DocumentChunk> search(float[] queryVector, int k);
    void deleteByDocumentId(String documentId);
}
