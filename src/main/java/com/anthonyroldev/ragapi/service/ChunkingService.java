package com.anthonyroldev.ragapi.service;

import com.anthonyroldev.ragapi.client.S3RAGClient;
import com.anthonyroldev.ragapi.entities.DocumentChunk;
import com.anthonyroldev.ragapi.entities.DocumentEntity;
import com.anthonyroldev.ragapi.entities.enums.StatusEnum;
import com.anthonyroldev.ragapi.exception.RAGDocumentException;
import com.anthonyroldev.ragapi.exception.RAGEmbeddingException;
import com.anthonyroldev.ragapi.repository.DocumentChunkRepository;
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
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@AllArgsConstructor
public class ChunkingService {
    private final S3RAGClient s3RAGClient;
    private final TokenTextSplitter splitter;
    private final EmbeddingService embeddingService;
    private final DocumentRepository documentRepository;
    private final DocumentStatusUpdater documentStatusUpdater;
    private final DocumentChunkRepository documentChunkRepository;

    @Transactional
    public void chunkDocument(UUID documentId) {
        var document = documentRepository.getDocumentById(documentId)
                .orElseThrow(() -> RAGDocumentException.documentNotFound(String.valueOf(documentId)));
        documentStatusUpdater.setStatus(document, StatusEnum.PROCESSING_CHUNKS);
        List<DocumentChunk> savedDocumentChunks;

        try (var s3Stream = s3RAGClient.downloadDocument(document.getS3Key())) {
            var documentFromS3 = new String(s3Stream.readAllBytes(), StandardCharsets.UTF_8);
            var chunks = splitDocument(documentFromS3)
                    .stream()
                    .map(Document::getText)
                    .toList();
            savedDocumentChunks = saveDocumentChunks(document, chunks);
        } catch (Exception e) {
            log.error("Failed to chunk document with id: {}", documentId, e);
            documentStatusUpdater.setStatus(document, StatusEnum.FAILED_CHUNKING);
            throw RAGDocumentException.chunkError();
        }

        try {
            documentStatusUpdater.setStatus(document, StatusEnum.PROCESSING_EMBEDDING);
            embedAndSaveChunks(savedDocumentChunks);
            documentStatusUpdater.setStatus(document, StatusEnum.COMPLETED);
        } catch (Exception e) {
            log.error("Failed to embed chunks for document with id: {}", documentId, e);
            documentStatusUpdater.setStatus(document, StatusEnum.FAILED_EMBEDDING);
            throw RAGEmbeddingException.embeddingError();
        }
    }

    private List<DocumentChunk> saveDocumentChunks(DocumentEntity documentEntity, List<String> chunks) {
        var index = new AtomicInteger(0);
        var documentChunks = chunks.stream()
                .map(chunk -> DocumentChunk.builder()
                        .id(UUID.randomUUID())
                        .documentEntity(documentEntity)
                        .chunkIndex(index.getAndIncrement())
                        .content(chunk)
                        .build())
                .toList();
        return documentChunkRepository.saveAll(documentChunks);
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
        return splitter.split(doc);
    }

    private void embedAndSaveChunks(List<DocumentChunk> chunks) {
        var texts = chunks.stream()
                .map(DocumentChunk::getContent)
                .toList();
        var embeddings = embeddingService.embedChunks(texts);
        for (int i = 0; i < chunks.size(); i++) {
            chunks.get(i)
                    .setEmbedding(embeddings.get(i));
        }
        documentChunkRepository.saveAll(chunks);
    }
}
