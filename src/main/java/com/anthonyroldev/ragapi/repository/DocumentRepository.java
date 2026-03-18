package com.anthonyroldev.ragapi.repository;

import com.anthonyroldev.ragapi.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
    Optional<DocumentEntity> getDocumentById(UUID id);
}