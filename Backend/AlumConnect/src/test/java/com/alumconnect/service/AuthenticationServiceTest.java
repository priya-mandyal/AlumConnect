package com.alumconnect.service;

import com.alumconnect.config.JwtService;
import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.LoginResponse;
import com.alumconnect.dto.RegisterRequest;
import com.alumconnect.entities.User;
import com.alumconnect.enums.Gender;
import com.alumconnect.enums.Role;
import com.alumconnect.exception.ACException;
import com.alumconnect.repository.AlumniProfileRepository;
import com.alumconnect.repository.StudentRepository;
import com.alumconnect.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AlumniProfileRepository alumniRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private ChatService chatService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthenticationService authenticationService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userRepository, alumniRepository, studentRepository, jwtService, chatService);
    }

    @Test
    public void returnSuccessTrue_whenStudentRegistered() {
        RegisterRequest registerRequest = new RegisterRequest("P", "M", "a@dal.ca", "1", "1", "Pass@123", Gender.FEMALE, Role.STUDENT);
        when(userRepository.findByEmailId("a@a.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        ACResponse<Object> response = authenticationService.register(registerRequest);

        assertTrue(response.isSuccess());
        verify(userRepository, times(1)).save(any(User.class));
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    public void returnSuccessTrue_whenAlumniRegistered() {
        RegisterRequest registerRequest = new RegisterRequest("P", "M", "a@a.com", "1", "1", "Pass@123", Gender.FEMALE, Role.ALUMNI);
        when(userRepository.findByEmailId("a@a.com")).thenReturn(Optional.empty());
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        ACResponse<Object> response = authenticationService.register(registerRequest);

        assertTrue(response.isSuccess());
        verify(userRepository, times(1)).save(any(User.class));
        verify(alumniRepository, times(1)).save(any());
    }

    @Test
    public void returnAlreadyRegisteredMessage_whenAlreadyRegisteredUser() {
        RegisterRequest registerRequest = new RegisterRequest("P", "M", "a@a.com", "1", "1", "Pass@123", Gender.FEMALE, Role.ALUMNI);
        when(userRepository.findByEmailId("a@a.com")).thenReturn(Optional.of(new User()));

        ACResponse<Object> response = authenticationService.register(registerRequest);

        assertEquals("Alumni Already Registered", response.getMessage());
    }

    @Test
    public void successfullyAuthenticateUser() {
        User user = new User();
        String encodedPassword = new BCryptPasswordEncoder().encode("password");
        user.setPassword(encodedPassword);
        when(userRepository.findByEmailId("a@a.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        when(passwordEncoder.matches(eq("password"), eq(encodedPassword))).thenReturn(true);

        LoginResponse<Object> response = authenticationService.authenticate("a@a.com", "password");

        assertTrue(response.isSuccess());
    }

    @Test(expected = ACException.class)
    public void authenticateWithIncorrectPassword() {
        User user = new User();
        String encodedPassword = new BCryptPasswordEncoder().encode("correctPassword");
        user.setPassword(encodedPassword);
        when(userRepository.findByEmailId("a@a.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        authenticationService.authenticate("a@a.com", "wrongPassword");
    }

}
