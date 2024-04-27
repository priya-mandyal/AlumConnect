package com.alumconnect.util;

import com.alumconnect.dto.RegisterRequest;
import com.alumconnect.enums.Gender;
import com.alumconnect.enums.Role;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class ValidationUtility {

    /**
     * Validates the fields inserted by the user to ensure they are in correct format.
     * Throws an error if any field is not in the correct format.
     * For students, the email should be a registered Dalhousie University ID.
     */
    public static void validateRegisterRequest(@NotNull RegisterRequest request) {
        validateFirstName(request.getFirstName());
        validateLastName(request.getLastName());
        validatePassword(request.getPassword());
        validateGender(request.getGender());
        validateEmail(request.getEmailId(), request.getRole());
        validatePassword(request.getPassword());
        validateGender(request.getGender());
        validateRole(request.getRole());
    }

    private static void validateFirstName(String firstName) {
        if (CommonUtility.isNullOrEmpty(firstName)) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
    }

    private static void validateLastName(String lastName) {
        if (CommonUtility.isNullOrEmpty(lastName)) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
    }

    private static void validateEmail(String email, Role role) {
        if (CommonUtility.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!CommonUtility.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (role == Role.STUDENT && !email.endsWith("@dal.ca")) {
            throw new IllegalArgumentException("Please enter your Dalhousie University email ID.");
        }
    }

    private static void validatePassword(String password) {
        if (CommonUtility.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (!CommonUtility.isStrongPassword(password)) {
            throw new IllegalArgumentException("Password is weak");
        }
    }

    private static void validateGender(Gender gender) {
        if (gender == null || !EnumSet.allOf(Gender.class).contains(gender)) {
            throw new IllegalArgumentException("Invalid Gender. Valid values are - MALE, FEMALE, OTHER");
        }
    }

    private static void validateRole(Role role) {
        if (role == null || !EnumSet.allOf(Role.class).contains(role)) {
            throw new IllegalArgumentException("Invalid Role. Valid values are - ADMIN, STUDENT, ALUMNI");
        }
    }
}
