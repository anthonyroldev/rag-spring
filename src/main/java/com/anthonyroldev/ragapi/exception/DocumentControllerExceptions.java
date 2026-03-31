package com.anthonyroldev.ragapi.exception;

import com.anthonyroldev.ragapi.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DocumentControllerExceptions {

    @ExceptionHandler(RAGDocumentException.class)
    public ResponseEntity<ErrorResponse> handleRAGDocumentException(RAGDocumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(RAGStorageException.class)
    public ResponseEntity<ErrorResponse> handleRAGStorageException(RAGStorageException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .message("Unexpected error")
                        .build());
    }

    @ExceptionHandler(RAGEmbeddingException.class)
    public ResponseEntity<ErrorResponse> handleRAGEmbeddingException(RAGEmbeddingException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorResponse.builder()
                        .message(e.getMessage())
                        .build());
    }
}
