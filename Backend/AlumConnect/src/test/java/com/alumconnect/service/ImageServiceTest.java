package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.entities.Student;
import com.alumconnect.entities.User;
import com.alumconnect.enums.Role;
import com.alumconnect.exception.ACException;
import com.alumconnect.repository.AlumniProfileRepository;
import com.alumconnect.repository.StudentRepository;
import com.alumconnect.repository.UserRepository;
import com.alumconnect.service.interfaces.IImageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageServiceTest {

    @Mock
    private FirebaseService firebaseService;

    @Mock
    private AlumniProfileRepository alumniRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    void testUploadImage_Alumni_Success() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[]{});
        long userId = 1L;
        User user = User.builder()
                .id(userId)
                .emailId("email@example.com")
                .role(Role.ALUMNI)
                .build();
        AlumniProfileEntity alumniProfileEntity = new AlumniProfileEntity();
        String imageUrl = "https://example.com/image.jpg";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(firebaseService.uploadToFirebase(file)).thenReturn(imageUrl);
        when(alumniRepository.findByUserId(userId)).thenReturn(Optional.of(alumniProfileEntity));

        // Act
        ACResponse<Object> response = imageService.uploadImage(file, userId);

        // Assert
        assertEquals("Profile image uploaded successfully.", response.getMessage());
    }

    @Test
    void testUploadImage_UserNotFound() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[]{});
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        ACResponse<Object> response = imageService.uploadImage(file, userId);

        assertEquals("User not found with email: " + userId, response.getMessage());
    }

    @Test
    void testUploadImage_AlumniProfileNotFound() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[]{});
        long userId = 1L;
        User user = User.builder()
                .id(userId)
                .emailId("email@example.com")
                .role(Role.ALUMNI)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(alumniRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ACException.class, () -> imageService.uploadImage(file, userId));
    }

    @Test
    void testUploadImage_StudentProfileNotFound() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[]{});
        long userId = 1L;
        User user = User.builder()
                .id(userId)
                .emailId("email@dal.ca")
                .role(Role.STUDENT)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ACException.class, () -> imageService.uploadImage(file, userId));
    }

    @Test
    void testDownloadImage_Alumni_Success() {
        // Arrange
        long userId = 1L;
        User user = User.builder()
                .id(userId)
                .emailId("email@example.com")
                .role(Role.ALUMNI)
                .build();        AlumniProfileEntity alumniProfileEntity = new AlumniProfileEntity();
        alumniProfileEntity.setProfilePictureUrl("https://example.com/image.jpg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(alumniRepository.findByUserId(userId)).thenReturn(Optional.of(alumniProfileEntity));
        when(firebaseService.getImageFromFirebase(alumniProfileEntity.getProfilePictureUrl())).thenReturn(new byte[]{1, 2, 3});

        // Act
        byte[] imageBytes = imageService.downloadImage(userId);

        assertEquals(3, imageBytes.length);
    }

    @Test
    void testDownloadImage_Student_Success() {
        // Arrange
        long userId = 1L;
        User user = User.builder()
                .id(userId)
                .emailId("email@dal.ca")
                .role(Role.STUDENT)
                .build();        Student student = new Student();
        student.setProfilePictureUrl("https://example.com/image.jpg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(userId)).thenReturn(Optional.of(student));
        when(firebaseService.getImageFromFirebase(student.getProfilePictureUrl())).thenReturn(new byte[]{1, 2, 3});

        // Act
        byte[] imageBytes = imageService.downloadImage(userId);

        // Assert
        assertEquals(3, imageBytes.length);
    }

    @Test
    void testDownloadImage_UserNotFound() {
        // Arrange
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ACException.class, () -> imageService.downloadImage(userId));
    }

    @Test
    void testDownloadImage_AlumniProfileNotFound() {
        // Arrange
        long userId = 1L;
        User user = User.builder()
                .id(userId)
                .emailId("email@example.com")
                .role(Role.ALUMNI)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(alumniRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ACException.class, () -> imageService.downloadImage(userId));
    }

    @Test
    void testDownloadImage_StudentProfileNotFound() {
        // Arrange
        long userId = 1L;
        User user = User.builder()
                .id(userId)
                .emailId("email@dal.ca")
                .role(Role.STUDENT)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ACException.class, () -> imageService.downloadImage(userId));
    }
}
