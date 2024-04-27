package com.alumconnect.service.interfaces;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.RegisterRequest;

import okhttp3.ResponseBody;

public interface IChatService {
    String signUpAsChatUser(RegisterRequest request);
    void saveChatUserDetails(String responseBody, long id);
    ACResponse<Object> createChatRoom(boolean isStudent, Long userId, Long matchedUserId);
}

