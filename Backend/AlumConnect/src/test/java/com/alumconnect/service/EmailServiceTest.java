package com.alumconnect.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    private EmailService emailService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(javaMailSender, templateEngine);
    }

    @Test
    public void sendPasswordResetEmail_sendsEmailSuccessfully() throws MessagingException {
        String emailAddress = "a@a.com";
        String token = "random Token";

        MimeMessage mockMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mockMessage);
        when(templateEngine.process(any(String.class), any())).thenReturn("html_content");

        emailService.sendPasswordResetEmail(emailAddress, token);

        verify(templateEngine).process(eq("resetpassword"), any(Context.class));
        verify(javaMailSender).send(mockMessage);
    }

    @Test
    public void sendMatchNotificationEmail_sendsEmailSuccessfully() throws MessagingException {
        String emailAddress = "a@a.com";
        String matchName = "P M";
        String role = "Student";

        MimeMessage mockMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mockMessage);
        when(templateEngine.process(any(String.class), any())).thenReturn("html_content");

        emailService.sendMatchNotificationEmail(emailAddress, matchName, role);

        verify(templateEngine).process(eq("matchNotification"), any(Context.class));
        verify(javaMailSender).send(mockMessage);
    }

}
