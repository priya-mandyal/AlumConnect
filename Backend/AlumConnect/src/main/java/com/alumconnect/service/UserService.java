package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.ResetPassword;
import com.alumconnect.entities.ResetToken;
import com.alumconnect.entities.User;
import com.alumconnect.exception.ACException;
import com.alumconnect.repository.UserRepository;
import com.alumconnect.service.interfaces.IUserService;
import com.alumconnect.util.CommonUtility;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokenService resetTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ACResponse<Object> forgotPassword(String email) throws MessagingException {
        Optional<User> optionalUser = userRepository.findByEmailId(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            ResetToken token = resetTokenService.createResetToken(user);
            emailService.sendPasswordResetEmail(user.getEmailId(), token.getResetToken());

            return ACResponse.builder()
                    .success(true)
                    .message("Password reset link sent to your email")
                    .build();
        } else {
            return ACResponse.builder()
                    .success(false)
                    .message("Email Id is not registered")
                    .build();
        }
    }

    public void resetPasswordUsingToken(String token, ResetPassword resetPassword) {
        Optional<ResetToken> optionalToken = resetTokenService.findByToken(token);
        if (optionalToken.isPresent()) {
            ResetToken resetToken = optionalToken.get();
            if (!resetToken.getExpiryDate().isAfter(LocalDateTime.now())) throw new ACException("Token has expired");
            User user = resetToken.getUser();
            if(!CommonUtility.isStrongPassword(resetPassword.getNewPassword())) throw new ACException("Token has expired");
            user.setPassword(passwordEncoder.encode(resetPassword.getNewPassword()));
            userRepository.save(user);
            resetTokenService.deleteToken(resetToken);
        } else {
            throw new ACException("Invalid token");
        }
    }

    public void resetPassword(ResetPassword resetPassword) {
        Optional<User> optionalUser = userRepository.findByEmailId(resetPassword.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(resetPassword.getCurrentPassword(), user.getPassword())) {
                //if (passwordEncoder.encode(resetPassword.getCurrentPassword()).equals(user.getPassword())) {
                // If current password is correct, update the password
                if(!CommonUtility.isStrongPassword(resetPassword.getNewPassword())) throw new ACException("Token has expired");
                user.setPassword(passwordEncoder.encode(resetPassword.getNewPassword()));
                userRepository.save(user);
            } else {
                // If current password is incorrect, throw an exception
                throw new RuntimeException("Incorrect current password");
            }
        } else {
            // If user not found, throw an exception
            throw new RuntimeException("User not found with this email: " + resetPassword.getEmail());
        }
    }

}
