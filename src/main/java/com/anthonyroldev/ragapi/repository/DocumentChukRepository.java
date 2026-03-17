package com.anthonyroldev.ragapi.repository;

import com.anthonyroldev.ragapi.entities.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentChukRepository extends JpaRepository<DocumentChunk, UUID> {
}
