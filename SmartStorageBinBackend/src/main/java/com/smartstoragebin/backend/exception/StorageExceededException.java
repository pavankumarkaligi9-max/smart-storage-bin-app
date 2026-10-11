package com.smartstoragebin.backend.exception;

public class StorageExceededException extends RuntimeException {
    public StorageExceededException(String message) {
        super(message);
    }
}
