package com.anthonyroldev.ragapi.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DocumentUploadedEvent(UUID documentId,
                                    LocalDateTime timestamp) {
}
