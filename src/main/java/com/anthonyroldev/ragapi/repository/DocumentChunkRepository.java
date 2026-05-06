package com.anthonyroldev.ragapi.repository;

import com.anthonyroldev.ragapi.entities.DocumentChunk;
import com.anthonyroldev.ragapi.repository.projection.ChunkMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {
    @Query(value = """
            SELECT d.content, (d.embedding <=> :embeddingToSearch::vector) AS similarity_score
            FROM document_chunk
            ORDER BY similarity_score
            LIMIT :limit
            """, nativeQuery = true)
    List<ChunkMatch> findTopKBySimilarity(List<Float> embeddingToSearch, int limit);
}
