package com.alumconnect.service;

import com.alumconnect.config.JwtService;
import com.alumconnect.dto.LoginResponse;
import com.alumconnect.dto.RegisterRequest;
import com.alumconnect.dto.ACResponse;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.entities.Student;
import com.alumconnect.entities.User;
import com.alumconnect.enums.Role;
import com.alumconnect.exception.ACException;
import com.alumconnect.repository.AlumniProfileRepository;
import com.alumconnect.repository.StudentRepository;
import com.alumconnect.repository.UserRepository;
import com.alumconnect.service.interfaces.IAuthenticationService;
import com.alumconnect.util.AlumConstants;
import com.alumconnect.util.CommonUtility;
import com.alumconnect.util.ValidationUtility;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class responsible for user authentication and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final UserRepository userRepository;
    private final AlumniProfileRepository alumniRepository;
    private final StudentRepository studentRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;
    private final ChatService chatService;

    /**
     * Validates and registers a new user.
     *
     * @return For a new user, returns success with a successful registration message.
     *         If the user is already registered, returns a success response with a message
     *         indicating that the specified role is already registered.
     */
    public ACResponse<Object> register(@NotNull RegisterRequest request) {
        ValidationUtility.validateRegisterRequest(request);

        Optional<User> existingUser = userRepository.findByEmailId(request.getEmailId());
        if (existingUser.isPresent()) {
            return ACResponse.builder()
                    .success(false)
                    .message(String.format("%s Already Registered", request.getRole().getName()))
                    .build();
        }

        if(request.getRole() != Role.STUDENT) request.setStudentId("");

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailId(request.getEmailId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .studentId(request.getStudentId())
                .gender(request.getGender())
                .build();
        this.userRepository.save(user);

        switch (user.getRole()) {
            case ALUMNI -> {
                var alumni = AlumniProfileEntity.builder().user(user).build();
                this.alumniRepository.save(alumni);
            }
            case STUDENT -> {
                var student = Student.builder().user(user).build();
                this.studentRepository.save(student);
            }

        }

        String responseBody = chatService.signUpAsChatUser(request);
        if (responseBody != null) chatService.saveChatUserDetails(responseBody, user.getId());
        return ACResponse.builder()
                .success(true)
                .message(String.format("%s Registration Successful", request.getRole().getName()))
                .data(jwtService.generateToken(user))
                .role(user.getRole())
                .emailId(user.getEmailId())
                .userId(user.getId())
                .build();
    }

    public LoginResponse<Object> authenticate(String email, String password) {
        boolean isInvalidCred = CommonUtility.isNullOrEmpty(email) || CommonUtility.isNullOrEmpty(password);
        if (isInvalidCred) {
            throw new ACException("Email and password cannot be empty");
        }

        User existingUser = userRepository.findByEmailId(email)
                .orElseThrow(() -> new ACException("User not found for email: " + email));

        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new ACException("Incorrect password for user: " + email);
        }

        return LoginResponse.builder()
                .success(true)
                .message("User has successfully logged in.")
                .data(jwtService.generateToken(existingUser))
                .role(existingUser.getRole())
                .emailId(existingUser.getEmailId())
                .firstName(existingUser.getFirstName())
                .lastName(existingUser.getLastName())
                .userId(existingUser.getId())
                .projectID(AlumConstants.CHAT_ENGINE_PROJECT_ID)
                .build();
    }

}
