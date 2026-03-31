package com.anthonyroldev.ragapi.exception;

public class RAGEmbeddingException extends RuntimeException {
    public RAGEmbeddingException(String s) {
        super(s);
    }

    public static RAGEmbeddingException embeddingError() {
        return new RAGEmbeddingException("Error occurred while embedding the document chunks");
    }
}
