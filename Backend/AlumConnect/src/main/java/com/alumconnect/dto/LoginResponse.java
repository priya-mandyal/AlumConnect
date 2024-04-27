package com.alumconnect.dto;

import com.alumconnect.enums.Role;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse<T>{
    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;

    /**
     * A message describing the result of the operation.
     */
    private String message;

    /**
     * Additional data associated with the response, if any.
     */
    private T data;
    /**
     * The role associated with the user in the response.
     */
    private Role role;
    /**
     * The email address of the user in the response.
     */
    private String emailId;
    private String firstName;
    private String lastName;
    private Long userId;
    private String projectID;
}
