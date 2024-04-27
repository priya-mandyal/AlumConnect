package com.alumconnect.service;

import com.alumconnect.service.interfaces.IFirebaseService;
import com.alumconnect.util.AlumConstants;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;


/**
 * Service class for interacting with Firebase Storage.
 */
@Service
@RequiredArgsConstructor
public class FirebaseService implements IFirebaseService {

    private final Storage storage;

    public String uploadToFirebase(MultipartFile file) throws IOException {
        String fileName = Timestamp.from(Instant.now()).getTime() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(AlumConstants.FIREBASE_BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .setAcl(java.util.Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .build();
        storage.create(blobInfo, file.getBytes()); // Uploading image file to Firebase Storage

        return String.format("https://storage.googleapis.com/%s/%s",
                AlumConstants.FIREBASE_BUCKET_NAME,
                URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    public void deleteFromFirebase(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String bucketName = parts[3];
        String blobName = parts[4];
        BlobId blobId = BlobId.of(bucketName, blobName);
        storage.delete(blobId); // Deleting image file from Firebase Storage
    }


    public byte[] getImageFromFirebase(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String bucketName = parts[3];
        String blobName = parts[4];
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        return blob.getContent();
    }
}
