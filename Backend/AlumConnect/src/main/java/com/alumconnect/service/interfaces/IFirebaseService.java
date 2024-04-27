package com.alumconnect.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFirebaseService {

    /**
     * Uploads a file to Firebase Storage.
     *
     * @param file The file to upload.
     * @return The URL of the uploaded file on FireBase.
     * @throws IOException If an I/O error occurs during the upload process.
     */
    String uploadToFirebase(MultipartFile file) throws IOException;

    /**
     * Deletes the file at the given URL from Firebase Storage.
     *
     * @param imageUrl The URL of the file to delete.
     */
    void deleteFromFirebase(String imageUrl);

    /**
     * Gets the content of the file at the given URL from Firebase Storage.
     *
     * @param imageUrl The URL of the file to retrieve.
     * @return The content of the file as a byte array.
     */
    byte[] getImageFromFirebase(String imageUrl);
}

