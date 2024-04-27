package com.alumconnect.enums;

public enum Role {
    ADMIN("Admin"),
    STUDENT("Student"),
    ALUMNI("Alumni");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
