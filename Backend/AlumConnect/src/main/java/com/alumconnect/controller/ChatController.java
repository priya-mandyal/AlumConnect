package com.alumconnect.controller;


import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.ChatRoomRequest;
import com.alumconnect.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling chat-related endpoints.
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Endpoint for creating a chat room.
     * @param chatRoomRequest Contains loggedIn user, matched user details.
     * @return ResponseEntity with the response containing the result of creating the chat room.
     */
    @PostMapping("/createChatRoom")
    public ResponseEntity<ACResponse<Object>> createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        return ResponseEntity.ok(chatService.createChatRoom(chatRoomRequest.isStudent(), chatRoomRequest.getLoggedInUser(), chatRoomRequest.getMatchedUser()));
    }

    /**
     * Exception handler for handling exceptions thrown within the createChatRoom endpoint.
     *
     * @param e The exception that occurred.
     * @return ResponseEntity with an appropriate error response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ACResponse<Object>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ACResponse.builder()
                .success(false)
                .message("Server Error: Internal error occurred while processing Chat APIs.")
                .build());
    }

}
