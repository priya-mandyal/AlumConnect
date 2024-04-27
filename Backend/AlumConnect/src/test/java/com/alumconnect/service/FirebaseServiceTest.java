package com.alumconnect.service;

import com.alumconnect.util.AlumConstants;
import com.google.cloud.storage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FirebaseServiceTest {

    @Mock
    private Storage storage;

    @InjectMocks
    private FirebaseService firebaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void uploadToFirebaseTest() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenReturn(new byte[0]);

        Blob blob = mock(Blob.class);
        when(storage.create(any(BlobInfo.class), any())).thenReturn(blob);
        when(blob.getContent()).thenReturn(new byte[0]);


        String imageUrl = firebaseService.uploadToFirebase(file), imgU = "";
        int cnt = 0;
        for (char x: imageUrl.toCharArray()) {
            if (x == 'm') cnt++;
            imgU = imgU + x;
            if (cnt == 4) break;
        }
        imgU = imgU + "/";

        String expectedUrl = String.format("https://storage.googleapis.com/%s/%s",
                AlumConstants.FIREBASE_BUCKET_NAME, "");
        assertEquals(expectedUrl, imgU);
    }

    @Test
    void deleteFromFirebaseTest() {
        when(storage.delete(any(BlobId.class))).thenReturn(true);

        firebaseService.deleteFromFirebase("https://storage.googleapis.com/bucket/test.jpg");

        verify(storage, times(1)).delete(any(BlobId.class));
    }

    @Test
    void getImageFromFirebaseTest() {
        Blob blob = mock(Blob.class);
        when(storage.get(any(BlobId.class))).thenReturn(blob);
        when(blob.getContent()).thenReturn(new byte[0]);

        byte[] imageBytes = firebaseService.getImageFromFirebase("https://storage.googleapis.com/bucket/test.jpg");

        assertEquals(0, imageBytes.length);
    }
}