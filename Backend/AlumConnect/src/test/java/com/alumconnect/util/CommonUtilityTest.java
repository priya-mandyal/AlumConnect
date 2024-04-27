package com.alumconnect.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommonUtilityTest {

    @Test
    void testIsValidEmail_ValidEmail() {
        assertTrue(CommonUtility.isValidEmail("test@example.com"));
    }

    @Test
    void testIsValidEmail_InvalidEmail() {
        assertFalse(CommonUtility.isValidEmail("invalid-email"));
    }

    @Test
    void testIsValidEmail_Null() {
        assertFalse(CommonUtility.isValidEmail(""));
    }

    @Test
    void testIsNullOrEmpty_Null() {
        assertTrue(CommonUtility.isNullOrEmpty(""));
    }

    @Test
    void testIsNullOrEmpty_Empty() {
        assertTrue(CommonUtility.isNullOrEmpty(""));
    }

    @Test
    void testIsNullOrEmpty_NonEmpty() {
        assertFalse(CommonUtility.isNullOrEmpty("Non-empty"));
    }

    @Test
    void testIsStrongPassword_ValidPassword() {
        assertTrue(CommonUtility.isStrongPassword("Abcdefg1!"));
    }

    @Test
    void testIsStrongPassword_TooShort() {
        assertFalse(CommonUtility.isStrongPassword("Abc1!"));
    }

    @Test
    void testIsStrongPassword_NoUppercase() {
        assertFalse(CommonUtility.isStrongPassword("abcdefg1!"));
    }

    @Test
    void testIsStrongPassword_NoLowercase() {
        assertFalse(CommonUtility.isStrongPassword("ABCDEFG1!"));
    }

    @Test
    void testIsStrongPassword_NoDigit() {
        assertFalse(CommonUtility.isStrongPassword("Abcdefg!"));
    }

    @Test
    void testIsStrongPassword_NoSpecialCharacter() {
        assertFalse(CommonUtility.isStrongPassword("Abcdefg1"));
    }

    @Test
    void testIsStrongPassword_Null() {
        assertFalse(CommonUtility.isStrongPassword(""));
    }
}
