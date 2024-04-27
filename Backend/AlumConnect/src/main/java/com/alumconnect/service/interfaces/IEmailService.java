package com.alumconnect.service.interfaces;

import jakarta.mail.MessagingException;
import org.jetbrains.annotations.NotNull;

public interface IEmailService {
    void sendPasswordResetEmail(@NotNull String email, @NotNull String token) throws MessagingException;

    void sendMatchNotificationEmail(@NotNull String emailAddress, @NotNull String matchName, @NotNull String role) throws MessagingException;
}
