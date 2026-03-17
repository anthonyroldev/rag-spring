package com.anthonyroldev.ragapi.controller;

import com.anthonyroldev.ragapi.model.response.DocumentStatusResponse;
import com.anthonyroldev.ragapi.model.response.DocumentUploadResponse;
import com.anthonyroldev.ragapi.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentUploadResponse> uploadDocument(@RequestParam("file") MultipartFile file) {
        var response = documentService.uploadDocument(file);
        return ResponseEntity.accepted()
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentStatusResponse> getDocumentStatus(@PathVariable UUID id) {
        var response = documentService.getDocumentStatus(id);
        return ResponseEntity.ok(response);
    }
}
