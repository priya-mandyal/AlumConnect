package com.alumconnect.service;

import com.alumconnect.dto.*;
import com.alumconnect.entities.*;
import com.alumconnect.enums.Role;
import com.alumconnect.exception.ACException;
import com.alumconnect.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AcademicDetailRepository academicDetailRepository;
    @Mock
    private InterestRepository interestRepository;

    @InjectMocks
    private StudentService studentService;

    private User user;
    private Student student;
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmailId("a@a.com");

        student = new Student();
        student.setId(1L);
        student.setUser(user);

        studentDto = new StudentDto();
        studentDto.setEmail("a@a.com");
        studentDto.setAcademicDetails(Arrays.asList(new AcademicDetailDTO("University", "Degree")));
        studentDto.setInterests(Arrays.asList(new InterestDTO("Cooking", "Cooking")));
    }

    @Test
    void saveStudent_UserNotFound_ReturnsFailure() {
        when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());

        ACResponse<Object> response = studentService.saveStudent(studentDto);

        assertEquals("User not found with ID: a@a.com", response.getMessage());
    }

    @Test
    void getStudentProfile_UserNotFound_ReturnsFailure() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ACResponse<Object> response = studentService.getStudentProfile(1L);

        assertEquals("User not found with ID: 1", response.getMessage());
    }

    @Test
    void updateStudentProfile_UserNotFound_ReturnsFailure() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ACResponse<Object> response = studentService.updateStudentProfile(1L, studentDto);

        assertEquals("User not found with ID: 1", response.getMessage());
    }

    @Test
    void updateStudentProfile_Success_ReturnsSuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(anyLong())).thenReturn(Optional.of(student));
        doNothing().when(academicDetailRepository).deleteByStudentId(anyLong());
        doNothing().when(interestRepository).deleteByStudentId(anyLong());

        ACResponse<Object> response = studentService.updateStudentProfile(1L, studentDto);

        assertEquals("Student profile updated for user with id:1", response.getMessage());
    }

}
