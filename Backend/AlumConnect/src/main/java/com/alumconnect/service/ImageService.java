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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.Optional;

/**
 * Service class for managing profile images of users.
 */
@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final FirebaseService firebaseService;
    private final AlumniProfileRepository alumniRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public ACResponse<Object> uploadImage(MultipartFile file, long userId) throws IOException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()){
            return ACResponse.builder()
                    .success(false)
                    .message(String.format("User not found with email: "+ userId))
                    .build();
        }
        User user = userOptional.get();
        String imageUrl = firebaseService.uploadToFirebase(file);

        if (user.getRole() == Role.ALUMNI) {
            AlumniProfileEntity alumni = alumniRepository.findByUserId(userId)
                    .orElseThrow(() -> new ACException("Alumni profile not found"));
            alumni.setProfilePictureUrl(imageUrl);
            alumniRepository.save(alumni);
            return ACResponse.builder()
                    .success(true)
                    .message("Profile image uploaded successfully.")
                    .role(Role.ALUMNI)
                    .userId(userId)
                    .emailId(user.getEmailId())
                    .build();
        } else if (user.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUserId(userId)
                    .orElseThrow(() -> new ACException("Student profile not found"));
            student.setProfilePictureUrl(imageUrl);
            studentRepository.save(student);
            return ACResponse.builder()
                    .success(true)
                    .message("Profile image uploaded successfully.")
                    .role(Role.STUDENT)
                    .userId(userId)
                    .emailId(user.getEmailId())
                    .build();
        } else {
            throw new ACException("Invalid user role");
        }
    }


    public byte[] downloadImage(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ACException("User not found"));

        String profilePictureUrl = null;
        if (user.getRole() == Role.ALUMNI) {
            AlumniProfileEntity alumni = alumniRepository.findByUserId(userId)
                    .orElseThrow(() -> new ACException("Alumni profile not found"));
            profilePictureUrl = alumni.getProfilePictureUrl();
        } else if (user.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUserId(userId)
                    .orElseThrow(() -> new ACException("Student profile not found"));
            profilePictureUrl = student.getProfilePictureUrl();
        }

        if (profilePictureUrl != null) {
            return firebaseService.getImageFromFirebase(profilePictureUrl);
        } else {
            return new byte[0];
        }
    }

}
