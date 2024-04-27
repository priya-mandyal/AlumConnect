package com.alumconnect.util;

import com.alumconnect.dto.RegisterRequest;
import com.alumconnect.enums.Gender;
import com.alumconnect.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilityTest {

    @Test
    void testValidateRegisterRequest_NullFirstName() {
        RegisterRequest request = createValidRequest();
        request.setFirstName(null);

        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtility.validateRegisterRequest(request));
    }

    @Test
    void testValidateRegisterRequest_WeakPassword() {
        RegisterRequest request = createValidRequest();
        request.setPassword("weak");

        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtility.validateRegisterRequest(request));
    }

    @Test
    void testValidateRegisterRequest_InvalidGender() {
        RegisterRequest request = createValidRequest();
        request.setGender(null);

        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtility.validateRegisterRequest(request));

    }

    @Test
    void testValidateRegisterRequest_InvalidRole() {
        RegisterRequest request = createValidRequest();
        request.setRole(null);

        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtility.validateRegisterRequest(request));
    }

    @Test
    void testValidateRegisterRequest_StudentRoleInvalidEmail() {
        RegisterRequest request = createValidRequest();
        request.setRole(Role.STUDENT);
        request.setEmailId("john@example.com");

        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtility.validateRegisterRequest(request));
    }

    private RegisterRequest createValidRequest() {
        return RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .emailId("john@example.com")
                .password("StrongPassword1!")
                .gender(Gender.MALE)
                .role(Role.STUDENT)
                .build();
    }
}
