package it.ms.backendassignment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BAException extends Exception {

    @Getter
    private final HttpStatus status;

    public BAException(HttpStatus status) {
        this.status = status;
    }

    public BAException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
