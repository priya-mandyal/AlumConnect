package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.RegisterRequest;
import com.alumconnect.entities.ChatRoom;
import com.alumconnect.entities.ChatUser;
import com.alumconnect.entities.User;
import com.alumconnect.repository.ChatRoomRepository;
import com.alumconnect.repository.ChatUserRepository;
import com.alumconnect.repository.UserRepository;
import okhttp3.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChatServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatUserRepository chatUserRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private OkHttpClient mockHttpClient = mock(OkHttpClient.class);;

    private ChatService chatService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        chatService = new ChatService(userRepository, chatUserRepository, chatRoomRepository, mockHttpClient);
    }

    @Test
    public void returnNotNullResponseBody_whenChatUserCreated() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmailId("a@a.com");
        registerRequest.setFirstName("Mandy");
        registerRequest.setLastName("P");

        Request httpRequest = mock(Request.class);
        Call mockCall = mock(Call.class);

        ResponseBody responseBody = ResponseBody.create(MediaType.get("application/json"), "{\"id\": 123}");
        Response response = new Response.Builder()
                .request(httpRequest)
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message("OK")
                .body(responseBody)
                .build();

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(response);

        String responseBodyOutput = chatService.signUpAsChatUser(registerRequest); //act

        assertNotNull(responseBodyOutput); //assert
    }

    @Test
    public void returnNullResponseBody_whenChatUserNotCreated() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmailId("a@a.com");
        registerRequest.setFirstName("Mandy");
        registerRequest.setLastName("P");

        Call mockCall = mock(Call.class);
        Request mockRequest = mock(Request.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);

        Response response = new Response.Builder()
                .request(mockRequest)
                .protocol(Protocol.HTTP_2)
                .message("")
                .code(400)
                .body(mockResponseBody)
                .build();

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(response);

        String responseBodyOutput = chatService.signUpAsChatUser(registerRequest); //act

        assertNull(responseBodyOutput); //assert
    }

    @Test
    public void returnNullResponseBody_whenSignUpAsChatUserThrowsException() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmailId("a@a.com");
        registerRequest.setFirstName("Mandy");
        registerRequest.setLastName("P");

        Call mockCall = mock(Call.class);

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(new IOException("Invalid Call"));

        String responseBodyOutput = chatService.signUpAsChatUser(registerRequest); //act

        assertNull(responseBodyOutput); //assert
    }

    @Test
    public void verifySaveIsCalled_whenSaveChatUserDetailsMethodCalled() throws IOException {

        chatService.saveChatUserDetails("{\"id\": 123}", 1L);

        verify(chatUserRepository, times(1)).save(any(ChatUser.class));
    }

    @Test
    public void returnChatRoomExistsResponse_whenChatRoomAlreadyExists() {
        ChatRoom existingChatRoom = new ChatRoom();
        existingChatRoom.setId(1L);
        User mockUser = mock(User.class);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockUser));
        when(chatRoomRepository.findChatRoomIdByStudentIdAndAlumniId(1L, 2L)).thenReturn(Optional.of(existingChatRoom));

        ACResponse<Object> response = chatService.createChatRoom(true, 1L, 2L);

        assertEquals("Chat room already exists", response.getMessage());
    }

    @Test
    public void returnSuccessResponse_whenChatRoomIsCreated() throws IOException {
        ChatRoom existingChatRoom = new ChatRoom();
        existingChatRoom.setId(1L);
        User mockUser = mock(User.class);
        Response mockResponse = mock(Response.class);
        Call mockCall = mock(Call.class);
        ResponseBody mockResponseBody = mock(ResponseBody.class);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockUser));
        when(chatRoomRepository.findChatRoomIdByStudentIdAndAlumniId(1L, 2L)).thenReturn(Optional.empty());
        when(mockUser.getEmailId()).thenReturn("demo@mail.com");
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn("{\"id\": 123}");

        ACResponse<Object> response = chatService.createChatRoom(true, 1L, 2L);

        assertEquals("Chat room created successfully", response.getMessage());
    }

    @Test
    public void returnChatRoomNotCreatedResponse_whenChatRoomIsNotCreatedInEngine() throws IOException {
        ChatRoom existingChatRoom = new ChatRoom();
        existingChatRoom.setId(1L);
        User mockUser = mock(User.class);
        Response mockResponse = mock(Response.class);
        Call mockCall = mock(Call.class);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockUser));
        when(chatRoomRepository.findChatRoomIdByStudentIdAndAlumniId(1L, 2L)).thenReturn(Optional.empty());
        when(mockUser.getEmailId()).thenReturn("demo@mail.com");
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(false);

        ACResponse<Object> response = chatService.createChatRoom(true, 1L, 2L);

        assertEquals("Chat room could not be created in Chat Engine", response.getMessage());
    }

    @Test
    public void returnServerErrorResponse_whenThrowsIOException() throws IOException {
        ChatRoom existingChatRoom = new ChatRoom();
        existingChatRoom.setId(1L);
        User mockUser = mock(User.class);
        Call mockCall = mock(Call.class);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockUser));
        when(chatRoomRepository.findChatRoomIdByStudentIdAndAlumniId(1L, 2L)).thenReturn(Optional.empty());
        when(mockUser.getEmailId()).thenReturn("demo@mail.com");
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(IOException.class);

        ACResponse<Object> response = chatService.createChatRoom(true, 1L, 2L);

        assertEquals("Server Error: Chat room could not be created.", response.getMessage());
    }

}