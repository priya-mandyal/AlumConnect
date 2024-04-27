package com.alumconnect.service;

import com.alumconnect.entities.ChatUser;
import com.alumconnect.repository.ChatUserRepository;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ChatEngineAvatarServiceTest {

    @Mock
    private ChatUserRepository chatUserRepository;

    @Mock
    private OkHttpClient client;

    @InjectMocks
    private ChatEngineAvatarService chatEngineAvatarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void throwsException_whenUserNotFound() {
        long userId = 1L;
        MultipartFile mockMultipartFile = mock(MultipartFile.class);

        when(chatUserRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            chatEngineAvatarService.uploadAvatar(userId, mockMultipartFile);
        });
    }

    @Test
    void uploadAvatar_SuccessfulUpload() throws IOException {
        long userId = 1L;
        ChatUser chatUser = new ChatUser();
        chatUser.setChatId(123L);
        Response mockResponse = mock(Response.class);
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        Call call = mock(Call.class);

        when(chatUserRepository.findByUserId(userId)).thenReturn(Optional.of(chatUser));
        when(mockMultipartFile.getOriginalFilename()).thenReturn("text.png");
        when(mockMultipartFile.getBytes()).thenReturn(new byte[]{1, 2, 1});
        when(client.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(mockResponse);

        chatEngineAvatarService.uploadAvatar(userId, mockMultipartFile); //act

        verify(client, times(1)).newCall(any(Request.class)); //assert
    }

}
