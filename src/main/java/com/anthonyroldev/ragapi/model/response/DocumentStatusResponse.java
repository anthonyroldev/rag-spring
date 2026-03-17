package com.anthonyroldev.ragapi.model.response;

import com.anthonyroldev.ragapi.entities.enums.StatusEnum;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DocumentStatusResponse(
        UUID id,
        String filename,
        StatusEnum status,
        LocalDateTime uploadedAt
) {
}
