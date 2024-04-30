package com.profitsoft.profitsoft.exception.artist;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Artist not found -> 404 Not Found
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException() {
    }

    public ArtistNotFoundException(String message) {
        super(message);
    }
}
