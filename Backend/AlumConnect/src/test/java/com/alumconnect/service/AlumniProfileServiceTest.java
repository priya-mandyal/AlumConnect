package com.alumconnect.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.AcademicDetailDTO;
import com.alumconnect.dto.AlumniProfile;
import com.alumconnect.dto.InterestDTO;
import com.alumconnect.entities.*;
import com.alumconnect.exception.ACException;
import com.alumconnect.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AlumniProfileServiceTest {

    private AlumniProfileRepository alumniProfileRepository = mock(AlumniProfileRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private AcademicDetailRepository academicDetailRepository = mock(AcademicDetailRepository.class);
    private InterestRepository interestRepository = mock(InterestRepository.class);
    private AlumniProfileService alumniProfileService;

    private User user;
    private AlumniProfileEntity alumniProfileEntity;
    private AlumniProfile alumniProfileDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmailId("a@a.com");

        alumniProfileDTO = new AlumniProfile(
                1L,
                "a@a.com",
                List.of(new AcademicDetailDTO("University Name", "Degree Name")),
                "available",
                "All skills",
                List.of(new InterestDTO("Cooking", "Cooking")),
                "Professional Journey",
                "profileImageUrl"
        );
        alumniProfileService = new AlumniProfileService(alumniProfileRepository, userRepository, academicDetailRepository, interestRepository);
    }

    @Test
    void returnSuccessFalse_whenUserNotFoundWhileCreatingAlumniProfile() {
        when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());

        ACResponse<Object> response = alumniProfileService.createAlumniProfile(alumniProfileDTO);

        assertFalse(response.isSuccess());
    }

    @Test
    void throwsACException_whenCreatingAlumniProfileWithNoProfileCreated() {
        when(userRepository.findByEmailId("a@a.com")).thenReturn(Optional.of(user));
        when(alumniProfileRepository.findByUserId(eq(1L))).thenReturn(Optional.empty());

        assertThrows(ACException.class, () -> alumniProfileService.createAlumniProfile(alumniProfileDTO));
    }

    @Test
    void throwsException_whenCreatingAlumniProfileWithNoProfileCreated() {
        when(userRepository.findByEmailId("a@a.com")).thenReturn(Optional.of(user));
        when(alumniProfileRepository.findByUserId(eq(1L))).thenThrow(RuntimeException.class);

        assertThrows(ACException.class, () -> alumniProfileService.createAlumniProfile(alumniProfileDTO));
    }

    @Test
    void returnSuccessTrue_whenCreatingAlumniProfileWithCorrectDetails() {
        when(userRepository.findByEmailId("a@a.com")).thenReturn(Optional.of(user));
        when(alumniProfileRepository.findByUserId(eq(1L))).thenReturn(Optional.of(new AlumniProfileEntity()));

        ACResponse<Object> response = alumniProfileService.createAlumniProfile(alumniProfileDTO);

        assertTrue(response.isSuccess());
    }


    @Test
    void returnSuccessFalse_whenUserNotFoundWhileGettingAlumniProfile() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ACResponse<Object> response = alumniProfileService.getAlumniProfile(1L);

        assertFalse(response.isSuccess());
    }

    @Test
    void throwsACException_whenGetAlumniProfileThrowsException() {
        when(userRepository.findById(any(Long.class))).thenThrow(RuntimeException.class);

        assertThrows(ACException.class, () -> alumniProfileService.getAlumniProfile(1L));
    }


    @Test
    void returnSuccess_whenGettingAlumniProfile() {
        AcademicDetailEntity academicDetailEntity = new AcademicDetailEntity(1L, "University Name", "Degree Name", alumniProfileEntity, null, user);
        InterestEntity interestEntity = new InterestEntity(1L, alumniProfileEntity, null, "Interest Name");

        AlumniProfileEntity alumniProfileEntity = new AlumniProfileEntity(1L,
                List.of(academicDetailEntity), "available", "any", List.of(interestEntity), "2 years", user, "url");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(alumniProfileRepository.findByUserId(anyLong())).thenReturn(Optional.of(alumniProfileEntity));

        ACResponse<Object> response = alumniProfileService.getAlumniProfile(user.getId());

        assertTrue(response.isSuccess());
    }

    @Test
    void throwsACException_whenUpdatingAlumniProfileThrowsException() {
        when(userRepository.findById(any(Long.class))).thenThrow(RuntimeException.class);

        assertThrows(ACException.class, () -> alumniProfileService.updateAlumniProfile(1L, alumniProfileDTO));
    }

    @Test
    void returnSuccessFalse_whenUserNotFoundWhileUpdatingAlumniProfile() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ACResponse<Object> response = alumniProfileService.updateAlumniProfile(1L, alumniProfileDTO);

        assertFalse(response.isSuccess());
    }

    @Test
    void throwsACException_whenUpdatingAlumniProfileWithNoProfileCreated() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(alumniProfileRepository.findByUserId(eq(1L))).thenReturn(Optional.empty());

        assertThrows(ACException.class, () -> alumniProfileService.updateAlumniProfile(1L, alumniProfileDTO));
    }

    @Test
    void returnSuccessTrue_whenUpdatingAlumniProfile() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(alumniProfileRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        AcademicDetailEntity academicDetailEntity = new AcademicDetailEntity(1L, "University Name", "Degree Name", alumniProfileEntity, null, user);
        InterestEntity interestEntity = new InterestEntity(1L, alumniProfileEntity, null, "Interest Name");

        AlumniProfileEntity alumniProfileEntity = new AlumniProfileEntity(1L,
                List.of(academicDetailEntity), "available", "any", List.of(interestEntity), "2 years", user, "url");

        when(alumniProfileRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(alumniProfileEntity));

        ACResponse<Object> response = alumniProfileService.updateAlumniProfile(1L, alumniProfileDTO);

        assertTrue(response.isSuccess());

    }

}
