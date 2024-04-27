package com.alumconnect.service;

import com.alumconnect.exception.ACException;
import com.alumconnect.service.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

        public void sendPasswordResetEmail(@NotNull String emailAddress, @NotNull String token) throws MessagingException {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            try {
                helper.setTo(emailAddress);
                helper.setSubject("Password Reset Request");

                Context context = new Context();
                context.setVariable("token", token);

                String htmlContent = templateEngine.process("resetpassword", context);

                helper.setText(htmlContent, true);

                javaMailSender.send(message);
            } catch (MessagingException e) {
                throw new ACException("Failed to send reset email: " + e.getMessage());
            }
        }

    public void sendMatchNotificationEmail(@NotNull String emailAddress, @NotNull String matchName, @NotNull String role) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            helper.setTo(emailAddress);
            helper.setSubject("New Match Found!");

            Context context = new Context();
            context.setVariable("name", matchName);
            context.setVariable("role", role);

            String htmlContent = templateEngine.process("matchNotification", context);

            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new ACException("Failed to send match notification email: " + e.getMessage());
        }
    }

}
