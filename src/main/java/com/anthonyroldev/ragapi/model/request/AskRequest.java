package com.anthonyroldev.ragapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AskRequest(
        @NotBlank
        @Size(max = 1000, message = "Question must be at most 1000 characters long")
        String question
) {
}
