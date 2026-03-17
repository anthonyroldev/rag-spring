package com.anthonyroldev.ragapi.model;

import com.anthonyroldev.ragapi.entities.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;


public record DocumentDto(UUID id,
                          String filename,
                          String s3Key,
                          StatusEnum status,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
}