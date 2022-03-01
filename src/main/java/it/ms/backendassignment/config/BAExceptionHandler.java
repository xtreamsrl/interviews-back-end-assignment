package it.ms.backendassignment.config;

import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.ErrorLog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class BAExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BAException.class})
    public ResponseEntity<ErrorLog> handleBAException(BAException exception) {
        ErrorLog errorLog = new ErrorLog();

        errorLog.setTimestamp(LocalDateTime.now());
        errorLog.setStatusCode(exception.getStatus());
        errorLog.setMessage(exception.getMessage());

        return ResponseEntity.status(exception.getStatus()).body(errorLog);
    }

}
