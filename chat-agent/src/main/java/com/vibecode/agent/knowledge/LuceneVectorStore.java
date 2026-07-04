package com.vibecode.agent.knowledge;

import org.apache.lucene.codecs.KnnVectorsFormat;
import org.apache.lucene.codecs.lucene99.Lucene99HnswVectorsFormat;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LuceneVectorStore implements VectorStore {
    private final Directory dir = new ByteBuffersDirectory();
    private final IndexWriter writer;

    public LuceneVectorStore() {
        try {
            var config = new IndexWriterConfig().setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writer = new IndexWriter(dir, config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(String id, float[] vector, DocumentChunk chunk) {
        try {
            var doc = new org.apache.lucene.document.Document();
            doc.add(new StringField("id", id, Field.Store.YES));
            doc.add(new StringField("documentId", chunk.documentId(), Field.Store.YES));
            doc.add(new TextField("content", chunk.content(), Field.Store.YES));
            doc.add(new KnnFloatVectorField("vector", vector));
            writer.addDocument(doc);
            writer.commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DocumentChunk> search(float[] queryVector, int k) {
        try (var reader = DirectoryReader.open(dir)) {
            var searcher = new IndexSearcher(reader);
            var query = new KnnFloatVectorQuery("vector", queryVector, k);
            var hits = searcher.search(query, k);

            List<DocumentChunk> results = new ArrayList<>();
            for (var hit : hits.scoreDocs) {
                var doc = searcher.doc(hit.doc);
                results.add(new DocumentChunk(
                    doc.get("documentId"), "", doc.get("content"), hit.score));
            }
            return results;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByDocumentId(String documentId) {
        try {
            writer.deleteDocuments(new TermQuery(new Term("documentId", documentId)));
            writer.commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
