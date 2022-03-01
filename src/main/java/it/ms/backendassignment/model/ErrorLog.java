package it.ms.backendassignment.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorLog {
    private LocalDateTime timestamp;
    private HttpStatus statusCode;
    private String message;
}
