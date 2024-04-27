package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.AcademicDetailDTO;
import com.alumconnect.dto.AlumniProfile;
import com.alumconnect.dto.InterestDTO;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.entities.User;
import com.alumconnect.enums.Role;
import com.alumconnect.exception.ACException;
import com.alumconnect.mapper.*;
import com.alumconnect.repository.AcademicDetailRepository;
import com.alumconnect.repository.AlumniProfileRepository;
import com.alumconnect.repository.InterestRepository;
import com.alumconnect.repository.UserRepository;
import com.alumconnect.service.interfaces.IAlumniProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumniProfileService implements IAlumniProfileService {
    private final AlumniProfileRepository alumniProfileRepository;
    private final UserRepository userRepository;
    private final AcademicDetailRepository academicDetailRepository;
    private final InterestRepository interestRepository;

    @Override
    public ACResponse<Object> createAlumniProfile(AlumniProfile alumniProfile) {
        try {
            Optional<User> userOptional = userRepository.findByEmailId(alumniProfile.getEmail());
            if(!userOptional.isPresent()){
                return ACResponse.builder()
                        .success(false)
                        .message(String.format("User not found with email: "+alumniProfile.getEmail()))
                        .build();
            }
            User user = userOptional.get();
            AlumniProfileEntity alumniProfileEntity = alumniProfileRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ACException("Alumni profile not found for user with email: " + alumniProfile.getEmail()));

            mapAndUpdateAlumniProfile(alumniProfile, alumniProfileEntity);
            saveAcademicDetails(alumniProfile, alumniProfileEntity);
            saveInterests(alumniProfile, alumniProfileEntity);

            return ACResponse.builder()
                    .role(Role.ALUMNI)
                    .message("Alumni profile created for user with id:" + user.getId())
                    .success(true)
                    .userId(user.getId())
                    .emailId(user.getEmailId())
                    .build();
        } catch (ACException e) {
            throw e;
        } catch (Exception e) {
            throw new ACException("Error creating alumni profile: " + e.getMessage());
        }
    }

    private void mapAndUpdateAlumniProfile(AlumniProfile alumniProfile, AlumniProfileEntity alumniProfileEntity) {
        new AlumniProfileEntityMapper(alumniProfileEntity).map(alumniProfile);
        alumniProfileRepository.updateProfileDetailsById(alumniProfileEntity.getId(),
                alumniProfileEntity.getAvailability(),
                alumniProfileEntity.getExpertiseAndSkills(),
                alumniProfileEntity.getProfessionalJourney());
    }

    private void saveAcademicDetails(AlumniProfile alumniProfile, AlumniProfileEntity alumniProfileEntity) {
        try {
            alumniProfile.getAcademicDetails().stream()
                    .map(academicDetailDTO -> new AcademicDetailsEntityMapper(alumniProfileEntity).map(academicDetailDTO))
                    .forEach(academicDetailRepository::save);
        } catch (Exception e) {
            throw new ACException("Error saving academic details: " + e.getMessage());
        }
    }

    private void saveInterests(AlumniProfile alumniProfile, AlumniProfileEntity alumniProfileEntity) {
        try {
            alumniProfile.getInterests().stream()
                    .map(interest -> new InterestEntityMapper(alumniProfileEntity).map(interest))
                    .forEach(interestRepository::save);
        } catch (Exception e) {
            throw new ACException("Error saving interests: " + e.getMessage());
        }
    }


    public ACResponse<Object> getAlumniProfile(Long userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if(!userOptional.isPresent()) {
                return ACResponse.builder()
                        .success(false)
                        .message(String.format("User not found with email: "+ userId))
                        .build();
            }
            User user = userOptional.get();
            Optional<AlumniProfileEntity> alumniProfileEntityOptional = alumniProfileRepository.findByUserId(userId);
            AlumniProfileEntity alumniProfileEntity = alumniProfileEntityOptional.get();
            List<InterestDTO> interests = alumniProfileEntity.getInterests().stream()
                    .map(new InterestDTOMapper()::map)
                    .collect(Collectors.toList());

            List<AcademicDetailDTO> academicDetails = alumniProfileEntity.getAcademicDetails().stream()
                    .map(new AcademicDetailsDTOMapper()::map)
                    .collect(Collectors.toList());

            AlumniProfile alumniProfile = new AlumniProfileDTOMapper().map(alumniProfileEntity);
            alumniProfile.setAcademicDetails(academicDetails);
            alumniProfile.setInterests(interests);
            alumniProfile.setAlumniProfileId(alumniProfileEntity.getId());
            alumniProfile.setImageUrl(alumniProfileEntity.getProfilePictureUrl());


            return ACResponse.builder()
                    .role(Role.ALUMNI)
                    .message("Returned alumni profile user with id:" + userId)
                    .success(true)
                    .userId(userId)
                    .data(alumniProfile)
                    .emailId(user.getEmailId())
                    .build();
        } catch (Exception ex) {
            throw new ACException("Error getting alumni profile: " + ex.getMessage());
        }
    }


    public ACResponse<Object> updateAlumniProfile(long userId, AlumniProfile alumniProfileDTO) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if(!userOptional.isPresent()){
                return ACResponse.builder()
                        .success(false)
                        .message(String.format("User not found with email: "+ userId))
                        .build();
            }
            User user = userOptional.get();

            AlumniProfileEntity alumniProfileEntity = alumniProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new ACException("Alumni profile not found for user with ID: " + userId));

            mapAndUpdateAlumniProfile(alumniProfileDTO, alumniProfileEntity);

            academicDetailRepository.deleteByAlumniProfileId(alumniProfileEntity.getId());
            saveAcademicDetails(alumniProfileDTO, alumniProfileEntity);

            interestRepository.deleteByAlumniProfileId(alumniProfileEntity.getId());
            saveInterests(alumniProfileDTO, alumniProfileEntity);

            return ACResponse.builder()
                    .role(Role.ALUMNI)
                    .message("Alumni profile updated for user with id:" + user.getId())
                    .success(true)
                    .userId(user.getId())
                    .emailId(user.getEmailId())
                    .build();
        } catch (ACException e) {
            throw e;
        } catch (Exception e) {
            throw new ACException("Error updating alumni profile: " + e.getMessage());
        }
    }

}
