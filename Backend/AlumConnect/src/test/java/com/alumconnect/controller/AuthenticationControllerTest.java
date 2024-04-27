package com.alumconnect.controller;

import com.alumconnect.config.JwtService;
import com.alumconnect.dto.LoginRequest;
import com.alumconnect.dto.RegisterRequest;
import com.alumconnect.exception.ACException;
import com.alumconnect.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void verifyRegisterWasCalledSuccessfully_whenValidRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mock(RegisterRequest.class))))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @WithMockUser
    void returnBadRequest_whenRegisterThrowsIllegalArgumentException() throws Exception {

        doThrow(new IllegalArgumentException("Invalid email")).when(authenticationService).register(any(RegisterRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mock(RegisterRequest.class))))
                .andExpect(status().isBadRequest());

        verify(authenticationService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @WithMockUser
    void verifyLoginWasCalledSuccessfully_whenValidCredentials() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mock(LoginRequest.class))))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).authenticate(any(), any());
    }

    @Test
    @WithMockUser
    void returnBadRequest_whenLoginThrowsACException() throws Exception {
        LoginRequest loginRequest = new LoginRequest("a@a.com", "incorrectPassword");

        doThrow(new ACException("Authentication failed")).when(authenticationService).authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, times(1)).authenticate(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
