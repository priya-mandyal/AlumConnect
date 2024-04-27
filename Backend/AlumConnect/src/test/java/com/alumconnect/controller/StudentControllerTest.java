package com.alumconnect.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.alumconnect.controller.StudentController;
import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.StudentDto;
import com.alumconnect.enums.Role;
import com.alumconnect.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class StudentControllerTest {
    private MockMvc mockMvc;
    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void testGetStudent() {
        long userId = 1L;
        StudentDto studentDto = new StudentDto();
        when(studentService.getStudentProfile(anyLong())).thenReturn(
                ACResponse.builder()
                        .role(Role.STUDENT)
                        .message("Returned student profile user with id:" + userId)
                        .success(true)
                        .userId(userId)
                        .data(studentDto)
                        .build()
        );
        ResponseEntity<?> responseEntity = studentController.getStudent(userId);
        assert responseEntity.getStatusCode().equals(HttpStatus.OK);
        assert responseEntity.getBody() != null;
    }

    @Test
    public void testSaveStudent() {
        StudentDto studentDto = new StudentDto();
        when(studentService.saveStudent(studentDto)).thenReturn(
                ACResponse.builder()
                        .role(Role.STUDENT)
                        .message("Student saved successfully")
                        .success(true)
                        .data(studentDto)
                        .build()
        );
        ResponseEntity<?> responseEntity = studentController.saveStudent(studentDto);
        assert responseEntity.getStatusCode().equals(HttpStatus.OK);
        assert responseEntity.getBody() != null;
    }

    @Test
    public void testUpdateStudent() {
        Long userId = 1L;
        StudentDto studentDto = new StudentDto();
        when(studentService.updateStudentProfile(userId, studentDto)).thenReturn(
                ACResponse.builder()
                        .role(Role.STUDENT)
                        .message("Student profile updated successfully")
                        .success(true)
                        .data(studentDto)
                        .build()
        );

        ResponseEntity<?> responseEntity = studentController.updateStudent(userId, studentDto);
        assert responseEntity.getStatusCode().equals(HttpStatus.OK);
        assert responseEntity.getBody() != null;
    }
}
