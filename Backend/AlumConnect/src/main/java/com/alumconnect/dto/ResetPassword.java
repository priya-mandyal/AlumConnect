package com.alumconnect.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassword {
    private String email;
    private String currentPassword;
    private String newPassword;
}
