package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.CreateUserPost;
import com.alumconnect.entities.User;
import com.alumconnect.entities.UserPostEntity;
import com.alumconnect.repository.UserPostRepository;
import com.alumconnect.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserPostServiceTest {

    private UserPostRepository userPostRepository = mock(UserPostRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private FirebaseService firebaseService = mock(FirebaseService.class);

    private UserPostService userPostService;
    private CreateUserPost createUserPost;

    @Before
    public void setUp() {
         MockitoAnnotations.openMocks(this);
        createUserPost = new CreateUserPost(1L, "title", "body", mock(MultipartFile.class));
        userPostService = new UserPostService(userPostRepository, userRepository, firebaseService);
    }

    @Test
    public void returnUserNotFound_whenInvalidUserIdPassedInCreate() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ACResponse<Object> response = userPostService.createPost(createUserPost);

        assertFalse(response.isSuccess());
    }

    @Test
    public void returnSuccessFalse_whenImageNotUploadedInCreate() throws IOException {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));
        when(firebaseService.uploadToFirebase(any())).thenThrow(IOException.class);

        ACResponse<Object> response = userPostService.createPost(createUserPost);

        assertFalse(response.isSuccess());
    }

    @Test
    public void returnSuccessTrue_WhenPostCreatedSuccessfully() throws Exception {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));
        when(firebaseService.uploadToFirebase(any())).thenReturn("imageUrl");

        ACResponse<Object> response = userPostService.createPost(createUserPost);

        assertTrue(response.isSuccess());
    }

    @Test
    public void returnSuccessTrue_WhenAllPostFetchedSuccessfully() {
        UserPostEntity userPostEntity = mock(UserPostEntity.class);
        User mockUser = mock(User.class);
        when(userPostEntity.getUser()).thenReturn(mockUser);
        when(userPostRepository.findAll()).thenReturn(List.of(userPostEntity));

        ACResponse<Object> response = userPostService.getAllPosts();

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void returnUserNotFound_whenInvalidUserIdPassedInUpdate() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ACResponse<Object> response = userPostService.updatePost(1L, createUserPost);

        assertFalse(response.isSuccess());
    }
    @Test
    public void returnSuccessTrue_WhenPostUpdatedSuccessfully() {
        UserPostEntity userPostEntity = mock(UserPostEntity.class);
        User mockUser = mock(User.class);
        when(userPostEntity.getUser()).thenReturn(mockUser);
        when(userPostRepository.findById(any(Long.class))).thenReturn(Optional.of(userPostEntity));

        ACResponse<Object> response = userPostService.updatePost(1L, createUserPost);

        assertTrue(response.isSuccess());
    }

    @Test
    public void returnUserNotFound_whenInvalidUserIdPassedInGet() {
        when(userRepository.existsById(any(Long.class))).thenReturn(false);

        ACResponse<Object> response = userPostService.getPostByUserId(1L);

        assertFalse(response.isSuccess());
    }

    @Test
    public void returnSuccessTrue_WhenPostFetchedByUserIdSuccessfully() {
        UserPostEntity userPostEntity = mock(UserPostEntity.class);
        User mockUser = mock(User.class);
        when(userPostEntity.getUser()).thenReturn(mockUser);
        when(userPostRepository.findByUserId(any(Long.class))).thenReturn(List.of(userPostEntity));
        when(userRepository.existsById(any(Long.class))).thenReturn(true);

        ACResponse<Object> response = userPostService.getPostByUserId(1L);

        assertTrue(response.isSuccess());
    }

    @Test
    public void returnUserNotFound_whenInvalidUserIdPassedInDelete() {
        when(userRepository.existsById(any(Long.class))).thenReturn(false);

        ACResponse<Object> response = userPostService.deletePost(1L);

        assertFalse(response.isSuccess());
    }

    @Test
    public void returnSuccessTrue_WhenPostDeletedByUserIdSuccessfully() {
        when(userPostRepository.existsById(any(Long.class))).thenReturn(true);
        doNothing().when(userPostRepository).deleteByPostId(any(Long.class));

        ACResponse<Object> response = userPostService.deletePost(1L);

        assertTrue(response.isSuccess());
    }


}