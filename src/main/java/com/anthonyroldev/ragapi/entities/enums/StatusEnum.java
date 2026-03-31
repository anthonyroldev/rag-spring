package com.anthonyroldev.ragapi.entities.enums;

public enum StatusEnum {
    PENDING,
    PROCESSING_CHUNKS,
    PROCESSING_EMBEDDING,
    COMPLETED,
    FAILED_CHUNKING,
    FAILED_STORAGE,
    FAILED_EMBEDDING
}
