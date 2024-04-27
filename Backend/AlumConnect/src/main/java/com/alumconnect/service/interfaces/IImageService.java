package com.alumconnect.service.interfaces;

import com.alumconnect.dto.ACResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {

    /**
     * Uploads a profile image to firebase for the specified user.
     *
     * @param file   The image file to upload.
     * @param userId The ID of the user.
     * @return ACResponse indicating the success or failure of the operation.
     * @throws IOException If an I/O error occurs during the file upload process.
     */
    ACResponse<Object> uploadImage(MultipartFile file, long userId) throws IOException;

    /**
     * Gets bytes of profile image saved in FireBase
     * for the specified user.
     *
     * @param userId The ID of the user.
     */
    byte[] downloadImage(long userId) throws IOException;

}
