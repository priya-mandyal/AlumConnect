package com.alumconnect.enums;

import org.junit.Test;
import static org.junit.Assert.*;

public class RoleTest {

    @Test
    public void testEnumValues_Admin() {
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
    }

    @Test
    public void testEnumValues_Student() {
        assertEquals(Role.STUDENT, Role.valueOf("STUDENT"));
    }

    @Test
    public void testEnumValues_Alumni() {
        assertEquals(Role.ALUMNI, Role.valueOf("ALUMNI"));
    }

    @Test
    public void testEnumCount() {
        Role[] roles = Role.values();
        assertEquals(3, roles.length);
    }
}
