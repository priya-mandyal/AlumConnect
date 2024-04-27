package com.alumconnect.service;

import com.alumconnect.entities.ChatUser;
import com.alumconnect.repository.ChatUserRepository;
import com.alumconnect.service.interfaces.IChatEngineAvatarService;
import com.alumconnect.util.AlumConstants;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service class for managing image uploads to the chat engine service.
 */
@Service
@RequiredArgsConstructor
public class ChatEngineAvatarService implements IChatEngineAvatarService {

    private final ChatUserRepository chatUserRepository;
    private final OkHttpClient client;

    /**
     * Uploads the avatar for a user to the chat engine.
     *
     * @param userId      The ID of the user whose avatar is being uploaded.
     * @param avatarFile  The avatar file to be uploaded.
     * @throws IOException If an I/O error occurs while processing the avatar file.
     */
    @Override
    public void uploadAvatar(Long userId, MultipartFile avatarFile) throws IOException {
        ChatUser chatUser = chatUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        String extension = FilenameUtils.getExtension(avatarFile.getOriginalFilename());
        String fileName = "avatar." + extension;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar", fileName, RequestBody.create(avatarFile.getBytes(), MediaType.get("image/*")))
                .build();

        Request request = new Request.Builder()
                .url(String.format(AlumConstants.CHAT_ENGINE_URL_USER, chatUser.getChatId()))
                .addHeader("PRIVATE-KEY", AlumConstants.CHAT_ENGINE_PRIVATE_KEY)
                .patch(requestBody)
                .build();

        client.newCall(request).execute();
    }
}
