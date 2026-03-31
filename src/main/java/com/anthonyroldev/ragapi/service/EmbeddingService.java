package com.anthonyroldev.ragapi.service;

import com.anthonyroldev.ragapi.exception.RAGEmbeddingException;
import lombok.AllArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    public List<float[]> embedChunks(List<String> chunks) {
        try {
            return embeddingModel.embed(chunks);
        } catch (Exception e) {
            throw new RAGEmbeddingException("Failed to embed document chunks : %s".formatted(e.getMessage()));
        }
    }
}
