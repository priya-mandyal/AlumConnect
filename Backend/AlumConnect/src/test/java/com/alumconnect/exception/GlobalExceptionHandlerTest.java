package com.alumconnect.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {


    @Test
    public void returnBadRequest_whenHttpMessageNotReadableException() throws Exception {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);

        assertEquals(HttpStatus.BAD_REQUEST, new GlobalExceptionHandler().handleHttpMessageNotReadableException(exception).getStatusCode());
    }
}
