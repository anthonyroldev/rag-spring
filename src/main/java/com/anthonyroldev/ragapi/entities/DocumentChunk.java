package com.anthonyroldev.ragapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_chunk")
public class DocumentChunk {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private DocumentEntity documentEntity;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "chunk_index", nullable = false)
    private int chunkIndex;

    @JdbcTypeCode(SqlTypes.VECTOR)
    @Column(name = "embedding", columnDefinition = "vector(768)")
    private float[] embedding;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
