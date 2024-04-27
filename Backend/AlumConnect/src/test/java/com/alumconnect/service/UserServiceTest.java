package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.ResetPassword;
import com.alumconnect.entities.ResetToken;
import com.alumconnect.entities.User;
import com.alumconnect.exception.ACException;
import com.alumconnect.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResetTokenService resetTokenService;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testForgotPassword_UserFound() throws Exception {
        String email = "a@a.com";
        User user = new User();
        user.setEmailId(email);
        when(userRepository.findByEmailId(email)).thenReturn(Optional.of(user));
        ResetToken token = new ResetToken();
        token.setResetToken("test_token");
        when(resetTokenService.createResetToken(user)).thenReturn(token);

        ACResponse<Object> response = userService.forgotPassword(email);

        assertEquals("Password reset link sent to your email", response.getMessage());
        verify(emailService, times(1)).sendPasswordResetEmail(email, token.getResetToken());
    }

    @Test
    void testForgotPassword_UserNotFound() throws Exception {
        String email = "a@a.com";
        when(userRepository.findByEmailId(email)).thenReturn(Optional.empty());

        ACResponse<Object> response = userService.forgotPassword(email);

        assertEquals("Email Id is not registered", response.getMessage());
        verify(emailService, never()).sendPasswordResetEmail(any(), any());
    }

    @Test
    void testResetPasswordUsingToken_ExpiredToken() {
        String token = "expired_token";
        ResetPassword resetPassword = new ResetPassword();
        ResetToken resetToken = new ResetToken();
        resetToken.setExpiryDate(LocalDateTime.now().minusDays(1)); // Token expired 1 day ago
        when(resetTokenService.findByToken(token)).thenReturn(Optional.of(resetToken));

        assertThrows(ACException.class, () -> userService.resetPasswordUsingToken(token, resetPassword));
    }

    @Test
    void testResetPassword_CurrentPasswordMismatch() {
        // Arrange
        String currentPassword = "oldPassword";
        String newPassword = "newStrongPassword";
        String email = "test@example.com";
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setEmail(email);
        resetPassword.setCurrentPassword(currentPassword);
        resetPassword.setNewPassword(newPassword);
        User user = new User();
        user.setEmailId(email);
        user.setPassword("oldPassword123"); // Existing user password, incorrect
        when(userRepository.findByEmailId(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> userService.resetPassword(resetPassword));
        verify(userRepository, never()).save(user);
    }

    @Test
    void testResetPassword_InvalidEmail() {
        // Arrange
        String email = "nonexistent@example.com";
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setEmail(email);
        resetPassword.setCurrentPassword("oldPassword");
        resetPassword.setNewPassword("newStrongPassword");
        when(userRepository.findByEmailId(email)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> userService.resetPassword(resetPassword));
        verify(userRepository, never()).save(any());
    }
}