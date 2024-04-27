package com.alumconnect.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceIntegrationTest {

    private GreenMail greenMail;

    @Autowired
    private SpringTemplateEngine templateEngine;

    private EmailService emailService;

    @BeforeEach
    public void startMailServer() throws MessagingException {

        greenMail = new GreenMail(new ServerSetup(3026, null, "smtp"));
        greenMail.start();

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(greenMail.getSmtp().getPort());

        emailService = new EmailService(mailSender, templateEngine);
        emailService.sendPasswordResetEmail("a@a.com","abc");
    }

    @AfterEach
    public void stopMailServer() {
        greenMail.stop();
    }

    @Test
    public void testSendPasswordResetEmail() throws Exception {
        String testEmail = "a@a.com";
        String token = "dummyToken";

        emailService.sendPasswordResetEmail(testEmail, token);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals("Password Reset Request", receivedMessages[0].getSubject());
    }

    @Test
    public void testSendMatchNotificationEmail() throws Exception {
        String testEmail = "a@a.com";
        String matchName = "P M";
        String role = "Alumni";

        emailService.sendMatchNotificationEmail(testEmail, matchName, role);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals("New Match Found!", receivedMessages[1].getSubject());
    }
}