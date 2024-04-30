package com.profitsoft.profitsoft.exception.json;

/**
 * Exception to indicate an invalid file extension
 */
public class InvalidFileExtensionException extends RuntimeException {
    public InvalidFileExtensionException() {
    }

    public InvalidFileExtensionException(String message) {
        super(message);
    }
}
