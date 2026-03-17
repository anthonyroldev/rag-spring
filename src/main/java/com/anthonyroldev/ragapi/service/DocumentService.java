package com.anthonyroldev.ragapi.service;

import com.anthonyroldev.ragapi.client.S3RAGClient;
import com.anthonyroldev.ragapi.entities.Document;
import com.anthonyroldev.ragapi.entities.enums.StatusEnum;
import com.anthonyroldev.ragapi.exception.RAGDocumentException;
import com.anthonyroldev.ragapi.exception.RAGStorageException;
import com.anthonyroldev.ragapi.model.DocumentUploadedEvent;
import com.anthonyroldev.ragapi.model.response.DocumentStatusResponse;
import com.anthonyroldev.ragapi.model.response.DocumentUploadResponse;
import com.anthonyroldev.ragapi.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {
    private final S3RAGClient s3RAGClient;
    private final DocumentRepository documentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DocumentUploadResponse uploadDocument(MultipartFile file) {
        var fileKey = generateDocumentKey(file.getOriginalFilename());
        var document = Document.builder()
                .filename(file.getOriginalFilename())
                .status(StatusEnum.PENDING)
                .s3Key(fileKey)
                .build();
        documentRepository.save(document);
        try {
            s3RAGClient.uploadDocument(file, fileKey);
        } catch (RAGStorageException e) {
            document.setStatus(StatusEnum.FAILED);
            documentRepository.save(document);
            log.error("Failed to upload document {}: {}", file.getOriginalFilename(), e.getMessage());
            throw e;
        }
        eventPublisher.publishEvent(DocumentUploadedEvent.builder()
                .timestamp(LocalDateTime.now())
                .documentId(document.getId())
                .build());
        return DocumentUploadResponse.builder()
                .status(document.getStatus())
                .documentId(document.getId())
                .build();
    }

    public DocumentStatusResponse getDocumentStatus(UUID id) {
        var document = documentRepository.findById(id)
                .orElseThrow(() -> RAGDocumentException.documentNotFound("Document not found with id: " + id));
        return DocumentStatusResponse.builder()
                .uploadedAt(document.getCreatedAt())
                .filename(document.getFilename())
                .status(document.getStatus())
                .id(UUID.randomUUID())
                .build();
    }

    private String generateDocumentKey(String fileName) {
        return UUID.randomUUID() + "/" + fileName;
    }
}