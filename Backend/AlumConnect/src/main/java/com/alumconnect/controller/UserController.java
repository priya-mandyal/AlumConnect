package com.alumconnect.controller;

import com.alumconnect.dto.ResetPassword;
import com.alumconnect.exception.ACException;
import com.alumconnect.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestParam(name = "email", required = true) String email) throws MessagingException {
        try {
            return ResponseEntity.ok(userService.forgotPassword(email));
        } catch (ACException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send reset instructions: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password/token")
    public ResponseEntity<Object> resetPassword(@RequestParam String token, @RequestBody ResetPassword resetPasswor) {
        userService.resetPasswordUsingToken(token, resetPasswor);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPassword resetPassword) {
        userService.resetPassword(resetPassword);
        return ResponseEntity.ok("Password reset successfully");
    }

}
