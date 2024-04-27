package com.alumconnect.dto;

import com.alumconnect.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a response from the AlumConnect service.
 * This class encapsulates the success status, message, and data of the response.
 *
 * @param <T> The type of data included in the response.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ACResponse<T> {
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
     * The ID of the user in the response.
     */
    private Long userId;

    /**
     * The email address of the user in the response.
     */
    private String emailId;
}

