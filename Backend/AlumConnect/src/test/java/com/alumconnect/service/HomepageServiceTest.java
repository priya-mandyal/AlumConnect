package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.entities.*;
import com.alumconnect.enums.Role;
import com.alumconnect.repository.AlumniProfileRepository;
import com.alumconnect.repository.HomepagePostRepository;
import com.alumconnect.repository.StudentRepository;
import com.alumconnect.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomepageServiceTest {

    @Mock
    private HomepagePostRepository homepagePostRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AlumniProfileRepository alumniProfileRepository;

    @InjectMocks
    private HomepageService homepageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void returnsSuccess_WhenPostsExistForStudent() {
        Long userId = 1L;
        User user = mock(User.class);
        InterestEntity interestEntity = mock(InterestEntity.class);
        UserPostEntity userPostEntity = mock(UserPostEntity.class);
        Student mockStudent = mock(Student.class);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Role.STUDENT);
        when(studentRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(mockStudent));
        when(mockStudent.getInterests()).thenReturn(List.of(interestEntity));
        when(interestEntity.getInterest()).thenReturn("demo interest");
        when(homepagePostRepository.findByInterestsOrAllPosts(any())).thenReturn(Collections.singletonList(userPostEntity));

        ACResponse<Object> response = homepageService.getPostsForHomepage(userId);

        assertTrue(response.isSuccess());
    }

    @Test
    void returnsSuccess_WhenPostsExistFroAlumni() {
        Long userId = 1L;
        User user = mock(User.class);
        InterestEntity interestEntity = mock(InterestEntity.class);
        UserPostEntity userPostEntity = mock(UserPostEntity.class);
        AlumniProfileEntity mockAlumni = mock(AlumniProfileEntity.class);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Role.ALUMNI);
        when(alumniProfileRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(mockAlumni));
        when(mockAlumni.getInterests()).thenReturn(List.of(interestEntity));
        when(interestEntity.getInterest()).thenReturn("demo interest");
        when(homepagePostRepository.findByInterestsOrAllPosts(any())).thenReturn(Collections.singletonList(userPostEntity));

        ACResponse<Object> response = homepageService.getPostsForHomepage(userId);

        assertTrue(response.isSuccess());
    }

    @Test
    void returnsAllPosts_WhenNoInterestsMatch() {
        Long userId = 1L;
        User user = mock(User.class);
        AlumniProfileEntity mockAlumni = mock(AlumniProfileEntity.class);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Role.ALUMNI);
        when(alumniProfileRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(mockAlumni));
        when(mockAlumni.getInterests()).thenReturn(Collections.emptyList());
        when(homepagePostRepository.findAll()).thenReturn(Collections.emptyList());

        ACResponse<Object> response = homepageService.getPostsForHomepage(userId);

        verify(homepagePostRepository, never()).findByInterestsOrAllPosts(anyString());
        verify(homepagePostRepository, atLeastOnce()).findAll();
        assertTrue(response.isSuccess());
    }

    @Test
    void returnsAllPosts_WhenRoleAdmin() {
        Long userId = 1L;
        User user = mock(User.class);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn(Role.ADMIN);
        when(homepagePostRepository.findAll()).thenReturn(Collections.emptyList());

        ACResponse<Object> response = homepageService.getPostsForHomepage(userId);

        verify(homepagePostRepository, never()).findByInterestsOrAllPosts(anyString());
        verify(homepagePostRepository, atLeastOnce()).findAll();
        assertTrue(response.isSuccess());
    }

    @Test
    void returnsUserNotFound_WhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        ACResponse<Object> response = homepageService.getPostsForHomepage(userId);

        assertFalse(response.isSuccess());
    }
}