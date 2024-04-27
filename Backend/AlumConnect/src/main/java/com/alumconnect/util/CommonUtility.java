package com.alumconnect.util;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Contains helper classes and common functionalities
 */
public class CommonUtility {

    public static boolean isValidEmail(@NotNull String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isStrongPassword(@NotNull String password) {
        // Password must be at least 8 characters long and contain at least one uppercase letter,
        // one lowercase letter, and one digit
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }

}
