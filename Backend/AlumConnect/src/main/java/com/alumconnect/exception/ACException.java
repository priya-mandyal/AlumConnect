package com.alumconnect.exception;

/**
 * Custom exception class representing an exception specific to the AlumConnect application.
 */
public class ACException extends RuntimeException{

    public ACException(String message) {
        super(message);
    }
}
