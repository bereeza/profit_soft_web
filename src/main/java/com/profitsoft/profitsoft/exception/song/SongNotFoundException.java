package com.profitsoft.profitsoft.exception.song;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Song not found -> 404 Not Found
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException() {
    }

    public SongNotFoundException(String message) {
        super(message);
    }
}
