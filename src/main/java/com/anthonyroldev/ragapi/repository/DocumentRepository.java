package com.anthonyroldev.ragapi.repository;

import com.anthonyroldev.ragapi.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    Optional<Document> getDocumentById(UUID id);
}