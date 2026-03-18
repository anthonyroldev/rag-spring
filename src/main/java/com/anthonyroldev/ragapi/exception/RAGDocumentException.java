package com.anthonyroldev.ragapi.exception;

public class RAGDocumentException extends RuntimeException {
    public RAGDocumentException(String message) {
        super(message);
    }

    public static RAGDocumentException documentNotFound(String id) {
        return new RAGDocumentException("Document not found: " + id);
    }

    public static RAGDocumentException chunkError() {
        return new RAGDocumentException("Error occurred while chunking the document");
    }
}