package com.alumconnect.dto;

import com.alumconnect.enums.Role;
import lombok.Data;

import java.util.List;

@Data
public class ProfileDTO {
    private long userId;
    private List<String> interests;
    private String email;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private Role role;
}
