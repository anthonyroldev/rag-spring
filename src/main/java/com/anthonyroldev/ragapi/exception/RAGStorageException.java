package com.anthonyroldev.ragapi.exception;

public class RAGStorageException extends RuntimeException {
    public RAGStorageException(String message) {
        super(message);
    }

    public static RAGStorageException documentUploadFailed(String filename) {
        return new RAGStorageException("Failed to upload document: " + filename);
    }

    public static RAGStorageException documentDownloadFailed(String key) {
        return new RAGStorageException("Failed to download document: " + key);
    }

    public static RAGStorageException clientSideError(String message) {
        return new RAGStorageException("Client-side error: " + message);
    }
}