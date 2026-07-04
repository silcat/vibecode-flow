package com.vibecode.agent.knowledge;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    private final Map<String, Document> docs = new ConcurrentHashMap<>();

    @PostMapping("/documents")
    public ApiDocumentResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        Path tmp = Files.createTempFile("upload-", "-" + file.getOriginalFilename());
        file.transferTo(tmp.toFile());
        Document doc = documentService.upload(tmp);
        docs.put(doc.id(), doc);
        Files.deleteIfExists(tmp);
        return ApiDocumentResponse.ok(doc);
    }

    @GetMapping("/documents")
    public ApiDocumentResponse list() {
        return ApiDocumentResponse.ok(List.copyOf(docs.values()));
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        documentService.delete(id);
        docs.remove(id);
        return ResponseEntity.noContent().build();
    }

    public record ApiDocumentResponse(int code, String message, Object data) {
        static ApiDocumentResponse ok(Object data) { return new ApiDocumentResponse(200, "success", data); }
    }
}
