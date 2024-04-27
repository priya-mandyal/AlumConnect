package com.alumconnect.controller;

import com.alumconnect.config.JwtService;
import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.ChatRoomRequest;
import com.alumconnect.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;


    @Test
    @WithMockUser
    void verifyCreateChatRoomWasCalledSuccessfully_whenValidRequest() throws Exception {
        ChatRoomRequest chatRoomRequest = new ChatRoomRequest(1L, true, 1L);
        ACResponse<Object> response = ACResponse.builder().success(true).build();

        when(chatService.createChatRoom(anyBoolean(), any(), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/chat/createChatRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRoomRequest)))
                .andExpect(status().isOk());

        verify(chatService, times(1)).createChatRoom(eq(true), any(), any());
    }

    @Test
    @WithMockUser
    void returnInternalServerError_whenExceptionOccurs() throws Exception {
        ChatRoomRequest chatRoomRequest = new ChatRoomRequest(1L, true, 1L);

        when(chatService.createChatRoom(anyBoolean(), any(), any())).thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/chat/createChatRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRoomRequest)))
                .andExpect(status().isInternalServerError());

        verify(chatService, times(1)).createChatRoom(true,1L, 1L);
    }
}
