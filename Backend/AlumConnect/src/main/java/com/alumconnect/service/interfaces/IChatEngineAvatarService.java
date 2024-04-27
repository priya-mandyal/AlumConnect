package com.alumconnect.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface IChatEngineAvatarService {
    void uploadAvatar(Long userId, MultipartFile avatarFile) throws IOException;
}

