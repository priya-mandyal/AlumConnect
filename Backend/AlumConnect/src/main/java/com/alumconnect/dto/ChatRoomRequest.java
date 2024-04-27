package com.alumconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequest {
    private Long loggedInUser;
    private boolean isStudent;
    private Long matchedUser;
}
