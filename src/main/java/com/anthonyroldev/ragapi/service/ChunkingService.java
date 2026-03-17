package com.anthonyroldev.ragapi.service;

import com.anthonyroldev.ragapi.client.S3RAGClient;
import com.anthonyroldev.ragapi.entities.DocumentChunk;
import com.anthonyroldev.ragapi.entities.enums.StatusEnum;
import com.anthonyroldev.ragapi.exception.RAGDocumentException;
import com.anthonyroldev.ragapi.repository.DocumentChukRepository;
import com.anthonyroldev.ragapi.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ChunkingService {
    private final DocumentRepository documentRepository;
    private final DocumentChukRepository documentChukRepository;
    private final S3RAGClient s3RAGClient;

    @Transactional
    public void chunkDocument(UUID documentId) {
        var document = documentRepository.getDocumentById(documentId)
                .orElseThrow(() -> RAGDocumentException.documentNotFound(String.valueOf(documentId)));
        document.setStatus(StatusEnum.PROCESSING);
        documentRepository.save(document);
        try (var s3Stream = s3RAGClient.downloadDocument(document.getS3Key())) {
            var documentFromS3 = new String(s3Stream.readAllBytes(), StandardCharsets.UTF_8);
            splitDocument(documentFromS3);
            saveDocumentChunks(documentId, splitDocument(documentFromS3)
                    .stream()
                    .map(Document::getText)
                    .toList());
            document.setStatus(StatusEnum.COMPLETED);
            documentRepository.save(document);
        } catch (Exception e) {
            document.setStatus(StatusEnum.FAILED);
            documentRepository.save(document);
            log.error("Failed to chunk document with id: {}", documentId, e);
            throw new RAGDocumentException("Failed to chunk document with id: " + documentId);
        }
    }

    private void saveDocumentChunks(UUID documentId, List<String> chunks) {
        var document = documentRepository.getDocumentById(documentId)
                .orElseThrow(() -> RAGDocumentException.documentNotFound(String.valueOf(documentId)));
        var documentChunks = chunks.stream()
                .map(chunk -> DocumentChunk.builder()
                        .id(UUID.randomUUID())
                        .document(document)
                        .content(chunk)
                        .build())
                .toList();
        documentChukRepository.saveAll(documentChunks);
    }

    /**
     * Splits the provided content into token-based chunks.
     *
     * @param content document text content
     * @return list of split document chunks
     */
    private List<Document> splitDocument(String content) {
        Document doc = Document.builder()
                .text(content)
                .build();
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(1000)
                .build();
        return splitter.split(doc);
    }
}
