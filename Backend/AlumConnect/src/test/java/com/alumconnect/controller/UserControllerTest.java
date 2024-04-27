package com.alumconnect.controller;

import com.alumconnect.config.JwtService;
import com.alumconnect.dto.ResetPassword;
import com.alumconnect.exception.ACException;
import com.alumconnect.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void verifyOkResponse_whenForgotPasswordRequested() throws Exception {
        String email = "a@a.com";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/forgot-password")
                        .param("email", email))
                .andExpect(status().isOk());

        verify(userService, times(1)).forgotPassword(anyString());
    }

    @Test
    @WithMockUser
    void verifyServerErrorException_whenForgotPasswordFails() throws Exception {
        String email = "a@a.com";
        doThrow(ACException.class).when(userService).forgotPassword(anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/forgot-password")
                        .param("email", email))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).forgotPassword(anyString());
    }

    @Test
    @WithMockUser
    void verifyOkResponse_whenPasswordResetWithToken() throws Exception {
        ResetPassword resetPassword = new ResetPassword();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/reset-password/token")
                        .param("token", "token123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(resetPassword)))
                .andExpect(status().isOk());

        verify(userService, times(1)).resetPasswordUsingToken(anyString(), any(ResetPassword.class));
    }

    @Test
    @WithMockUser
    void verifyOkResponse_whenPasswordReset() throws Exception {
        ResetPassword resetPassword = new ResetPassword();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(resetPassword)))
                .andExpect(status().isOk());

        verify(userService, times(1)).resetPassword(any(ResetPassword.class));
    }

    @Test
    @WithMockUser
    void verifyServerErrorException_whenPasswordResetFails() throws Exception {
        ResetPassword resetPassword = new ResetPassword();
        doThrow(RuntimeException.class).when(userService).resetPassword(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(resetPassword)))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).resetPassword(any(ResetPassword.class));
    }
}
