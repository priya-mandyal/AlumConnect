package com.alumconnect.service;

import com.alumconnect.dto.*;
import com.alumconnect.entities.*;
import com.alumconnect.enums.Role;
import com.alumconnect.exception.ACException;
import com.alumconnect.mapper.*;
import com.alumconnect.repository.*;
import com.alumconnect.service.interfaces.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AcademicDetailRepository academicDetailRepository;
    private final InterestRepository interestRepository;
    private static final String USER_NOT_FOUND = "User not found with ID: %s";


    @Override
    public ACResponse<Object> saveStudent(StudentDto studentDto) {
        try {
            Optional<User> userOptional = userRepository.findByEmailId(studentDto.getEmail());
            if (!userOptional.isPresent()) {
                return ACResponse.builder()
                        .success(false)
                        .message(String.format(USER_NOT_FOUND ,studentDto.getEmail()))
                        .build();
            }
            User user = userOptional.get();
            Student studentEntity = studentRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ACException("Student profile not found for user with email: " + studentDto.getEmail()));

            mapAndUpdateStudentProfile(studentDto, studentEntity);
            saveAcademicDetails(studentDto, studentEntity);
            saveInterests(studentDto, studentEntity);

            return ACResponse.builder()
                    .role(Role.STUDENT)
                    .message("Student profile created for user with id:" + user.getId())
                    .success(true)
                    .userId(user.getId())
                    .emailId(user.getEmailId())
                    .build();
        } catch (ACException e) {
            throw e;
        } catch (Exception e) {
            throw new ACException("Error creating student profile: " + e.getMessage());
        }
    }
    public ACResponse<Object> getStudentProfile(Long userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if(!userOptional.isPresent()){
                return ACResponse.builder()
                        .success(false)
                        .message(String.format(USER_NOT_FOUND, userId))
                        .build();
            }
            User user = userOptional.get();
            Optional<Student> studentOptional = studentRepository.findByUserId(userId);
            Student student = studentOptional.get();

            List<InterestDTO> interests = new ArrayList<>();
            for(InterestEntity interestEntity : student.getInterests()){
                InterestDTO interestDTO = new InterestDTO(interestEntity.getInterest(), interestEntity.getInterest());
                interests.add(interestDTO);

            }

            List<AcademicDetailDTO> academicDetails = new ArrayList<>();
            for(AcademicDetailEntity academicDetail : student.getAcademicDetails()){
                AcademicDetailDTO academicDetailDTO = new AcademicDetailDTO(academicDetail.getUniversity(), academicDetail.getDegree());
                academicDetails.add(academicDetailDTO);
            }

            StudentDto studentDto = new StudentDto();
            studentDto.setStudentSummary(student.getStudentSummary());
            studentDto.setHobbyForm(student.getHobbyForm());
            studentDto.setAcademicDetails(academicDetails);
            studentDto.setInterests(interests);
            studentDto.setId(student.getId());
            studentDto.setEmail(user.getEmailId());
            studentDto.setImageUrl(student.getProfilePictureUrl());

            return ACResponse.builder()
                    .role(Role.STUDENT)
                    .message("Returned student profile user with id:" + userId)
                    .success(true)
                    .userId(userId)
                    .data(studentDto)
                    .emailId(user.getEmailId())
                    .build();
        } catch (Exception ex) {
            throw new ACException("Error getting student profile: " + ex.getMessage());
        }
    }

    public ACResponse<Object> updateStudentProfile(long userId, StudentDto studentDto) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if(!userOptional.isPresent()){
                return ACResponse.builder()
                        .success(false)
                        .message(String.format(USER_NOT_FOUND, userId))
                        .build();
            }
            User user = userOptional.get();
            Student student = studentRepository.findByUserId(userId)
                    .orElseThrow(() -> new ACException("Student profile not found for user with ID: " + userId));

            mapAndUpdateStudentProfile(studentDto, student);
            academicDetailRepository.deleteByStudentId(student.getId());
            saveAcademicDetails(studentDto, student);

            interestRepository.deleteByStudentId(student.getId());
            saveInterests(studentDto, student);

            return ACResponse.builder()
                    .role(Role.STUDENT)
                    .message("Student profile updated for user with id:" + userId)
                    .success(true)
                    .userId(userId)
                    .emailId(user.getEmailId())
                    .build();
        } catch (ACException e) {
            throw e;
        } catch (Exception e) {
            throw new ACException("Error updating student profile: " + e.getMessage());
        }
    }

    private void saveInterests(StudentDto studentDto, Student studentEntity) {
        try {
            for(InterestDTO interestDTO : studentDto.getInterests()){
                InterestEntity interestEntity = new InterestEntity();
                interestEntity.setStudent(studentEntity);
                interestEntity.setInterest(interestDTO.getLabel());
                interestRepository.save(interestEntity);
            }
        } catch (Exception e) {
            throw new ACException("Error saving academic details: " + e.getMessage());
        }
    }

    private void saveAcademicDetails(StudentDto studentDto, Student studentEntity) {
        try {
            for(AcademicDetailDTO academicDetailDTO : studentDto.getAcademicDetails()){
                AcademicDetailEntity academicDetailEntity = new AcademicDetailEntity();
                academicDetailEntity.setStudent(studentEntity);
                academicDetailEntity.setDegree(academicDetailDTO.getDegree());
                academicDetailEntity.setUniversity(academicDetailDTO.getUniversity());
                academicDetailRepository.save(academicDetailEntity);
            }
        } catch (Exception e) {
            throw new ACException("Error saving academic details: " + e.getMessage());
        }
    }

    private void mapAndUpdateStudentProfile(StudentDto studentDto, Student studentEntity) {
        studentEntity.setStudentSummary(studentDto.getStudentSummary());
        studentEntity.setHobbyForm(studentDto.getHobbyForm());
        studentRepository.updateProfileDetailsById(studentEntity.getUser().getId(), studentEntity.getHobbyForm(), studentEntity.getStudentSummary());
    }

}

