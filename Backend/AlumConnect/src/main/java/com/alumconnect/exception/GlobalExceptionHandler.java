package com.alumconnect.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class handles specific exceptions thrown during request processing across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the HttpMessageNotReadableException thrown when the server cannot read the incoming JSON request due to parsing errors.
     *
     * @param ex The HttpMessageNotReadableException instance.
     * @return A ResponseEntity with a 400 Bad Request status and a message indicating an invalid JSON format.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format.");
    }
}
