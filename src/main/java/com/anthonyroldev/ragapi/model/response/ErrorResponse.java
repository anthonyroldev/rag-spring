package com.anthonyroldev.ragapi.model.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message,
        int status
) {
}
