package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.RegisterRequest;
import com.alumconnect.entities.ChatRoom;
import com.alumconnect.entities.ChatUser;
import com.alumconnect.entities.User;
import com.alumconnect.repository.ChatRoomRepository;
import com.alumconnect.repository.ChatUserRepository;
import com.alumconnect.repository.UserRepository;
import com.alumconnect.service.interfaces.IChatService;
import com.alumconnect.util.AlumConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Service class responsible for handling chat-related operations such as creating chat rooms,
 * signing up users, and saving chat user details.
 */
@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {

    private static final String PROJECT_ID = "Project-ID";
    private static final String PRIVATE_KEY = "Private-Key";
    private static final String USER_NAME = "User-Name";
    private static final String USER_SECRET = "User-Secret";
    private final UserRepository userRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final OkHttpClient httpClient;

    /**
     * Signs up a user as a chat user in the chat engine.
     *
     * @param request The registration request containing user details.
     * @return The response body from the signup request, or null if the request fails.
     */
    public String signUpAsChatUser(@NotNull RegisterRequest request) {
        FormBody requestBody = new FormBody.Builder()
                .add("username", request.getEmailId())
                .add("secret", request.getEmailId())
                .add("email", request.getEmailId())
                .add("first_name", request.getFirstName())
                .add("last_name", request.getLastName())
                .build();

        Request httpRequest = new Request.Builder()
                .url(AlumConstants.CHAT_ENGINE_URL_NEW)
                .addHeader(PRIVATE_KEY, AlumConstants.CHAT_ENGINE_PRIVATE_KEY)
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful() && response.body() == null) return null;
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Saves chat user details after signing up as a chat user in ChatEngine.
     *
     * @param responseBody The response body containing the chat user details.
     * @param id           The ID of the user linked with the chat user.
     */
    public void saveChatUserDetails(@NotNull String responseBody, long id) {
            Map<String, Object> responseData = new Gson().fromJson(responseBody, new TypeToken<Map<String, Object>>() {
            }.getType());

            Double chatId = (Double) responseData.get("id");
            ChatUser chatUser = ChatUser.builder()
                    .chatId(chatId.longValue())
                    .userId(id)
                    .build();
            chatUserRepository.save(chatUser);
    }

    /**
     * Creates a chat room between the logged in and matched user.
     *
     * @param isStudent     A boolean value, true if user is a student else false.
     * @param userId        The ID of the loggedIn user.
     * @param matchedUserId The ID of the matched user.
     * @return An {@code ACResponse<Object>} containing the response details.
     */
    public ACResponse<Object> createChatRoom(boolean isStudent, Long userId, Long matchedUserId) {
        Long alumniId = isStudent ? matchedUserId : userId;
        Long studentId = isStudent ? userId : matchedUserId;
        User loggedInUser = findUserById(userId);
        User matchedUser = findUserById(matchedUserId);

        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findChatRoomIdByStudentIdAndAlumniId(studentId, alumniId);
        if (existingChatRoom.isPresent()) {
            return buildChatRoomResponse(false, existingChatRoom.get(), "Chat room already exists", userId);
        }

        try (Response response = createChatRoomInEngine(loggedInUser, matchedUser)) {
            if (response.isSuccessful() && response.body() != null) {
                Integer chatRoomId = new JSONObject(response.body().string()).getInt("id");
                addChatMemberInChatRoom(chatRoomId, loggedInUser.getEmailId(), matchedUser.getEmailId(), alumniId, studentId);
                return buildChatRoomResponse(true, chatRoomId, "Chat room created successfully", userId);
            } else {
                return buildChatRoomResponse(false, 0, "Chat room could not be created in Chat Engine", userId);
            }
        } catch (IOException ex) {
            return buildChatRoomResponse(false, 0, "Server Error: Chat room could not be created.", userId);
        }
    }

    /**
     * Adds a chat member to a specified chat room.
     *
     * @param chatRoomId        The ID of the chat room to which the member will be added.
     * @param loggedInUserEmail The email address of the logged-in user.
     * @param matchedUserEmail  The email address of the matched user.
     * @param alumniId          The ID of the alumni.
     * @param studentId         The ID of the student.
     * @throws IOException if an I/O error occurs while executing the HTTP request.
     */
    private void addChatMemberInChatRoom(Integer chatRoomId, String loggedInUserEmail, String matchedUserEmail, long alumniId, long studentId) throws IOException {
        String jsonBody = "{\"username\": \"" + matchedUserEmail + "\"}";
        RequestBody requestBody = RequestBody.create(jsonBody, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(AlumConstants.CHAT_ENGINE_URL + chatRoomId + "/people/")
                .addHeader(PROJECT_ID, AlumConstants.CHAT_ENGINE_PROJECT_ID)
                .addHeader(USER_NAME, loggedInUserEmail)
                .addHeader(USER_SECRET, loggedInUserEmail)
                .post(requestBody)
                .build();

        try (Response ignored = httpClient.newCall(request).execute()) {
            ChatRoom chatRoom = ChatRoom.builder()
                    .chatRoomID(chatRoomId)
                    .alumniID(alumniId)
                    .studentID(studentId)
                    .build();
            chatRoomRepository.save(chatRoom);
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * Creates a chat room in the chat engine service.
     *
     * @param loggedInUser The loggedIn user.
     * @param matchedUser The matched user.
     * @return The response from the chat engine service.
     * @throws IOException if an I/O error occurs while executing the HTTP request.
     */
    private Response createChatRoomInEngine(User loggedInUser, User matchedUser) throws IOException {
        RequestBody requestBody = RequestBody.create(
                String.format(
                        "{\"title\": \"%s & %s\", \"isDirectChat\": true}",
                        loggedInUser .getFirstName(), matchedUser.getFirstName()
                ),
                MediaType.get("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url(AlumConstants.CHAT_ENGINE_URL)
                .addHeader(PROJECT_ID, AlumConstants.CHAT_ENGINE_PROJECT_ID)
                .addHeader(PRIVATE_KEY, AlumConstants.CHAT_ENGINE_PRIVATE_KEY)
                .addHeader(USER_NAME, loggedInUser.getEmailId())
                .addHeader(USER_SECRET, loggedInUser.getEmailId())
                .post(requestBody)
                .build();

        return httpClient.newCall(request).execute();
    }

    private ACResponse<Object> buildChatRoomResponse(boolean isSuccess, Object data, String message, Long userId) {
        return ACResponse.builder()
                .success(isSuccess)
                .message(message)
                .userId(userId)
                .data(data)
                .build();
    }

}