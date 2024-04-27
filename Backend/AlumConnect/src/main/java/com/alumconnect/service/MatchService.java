package com.alumconnect.service;

import com.alumconnect.dto.MatchRequest;
import com.alumconnect.dto.MatchResponse;
import com.alumconnect.dto.ProfileDTO;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.entities.InterestEntity;
import com.alumconnect.entities.Student;
import com.alumconnect.entities.User;
import com.alumconnect.enums.Role;
import com.alumconnect.repository.AlumniProfileRepository;
import com.alumconnect.repository.InterestRepository;
import com.alumconnect.repository.StudentRepository;
import com.alumconnect.repository.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private AlumniProfileRepository alumniProfileRepository;
    @Autowired
    private StudentRepository studentRepository;


    public MatchResponse initiateMatch(Long userId,MatchRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return new MatchResponse();
        }
        User user = userOptional.get();
        Role roleOfUser = Role.valueOf(request.getRole().toUpperCase());

        if (roleOfUser == Role.STUDENT) {
            return matchForStudent(request);
        } else if (user.getRole() == Role.ALUMNI) {
            return matchForAlumni(request);
        }
        return new MatchResponse();
    }

    private MatchResponse matchForStudent(MatchRequest request) {
        List<InterestEntity> interestEntities = interestRepository.findByAlumniProfileIsNotNullAndInterest(request.getInterests().get(0));
        List<AlumniProfileEntity> matchedAlumni = interestEntities.stream()
                .map(InterestEntity::getAlumniProfile)
                .toList();

        List<ProfileDTO> matchedProfiles = matchedAlumni.stream()
                .map(alumniProfile -> createProfileDTO(alumniProfile.getUser().getId(), alumniProfile))
                .collect(Collectors.toList());

        MatchResponse response = new MatchResponse();
        response.setMatchedProfiles(matchedProfiles);
        return response;
    }

    private MatchResponse matchForAlumni(MatchRequest request) {
        List<InterestEntity> studentList = interestRepository.findByStudentIsNotNullAndInterest(request.getInterests().get(0));
        List<Student> matchedStudents = studentList.stream()
                .map(InterestEntity::getStudent)
                .toList();

        List<ProfileDTO> matchedProfiles = matchedStudents.stream()
                .map(student -> createProfileDTO(student.getUser().getId(), student))
                .collect(Collectors.toList());

        MatchResponse response = new MatchResponse();
        response.setMatchedProfiles(matchedProfiles);
        return response;
    }

    private ProfileDTO createProfileDTO(Long userId, Object userObject) {
        ProfileDTO profile = new ProfileDTO();
        if (userObject instanceof AlumniProfileEntity) {
            AlumniProfileEntity alumniProfile = (AlumniProfileEntity) userObject;
            profile.setInterests(new ArrayList<>(alumniProfile.getInterests().stream()
                    .map(InterestEntity::getInterest)
                    .collect(Collectors.toList())));
            profile.setEmail(alumniProfile.getUser().getEmailId());
            profile.setFirstName(alumniProfile.getUser().getFirstName());
            profile.setLastName(alumniProfile.getUser().getLastName());
            profile.setImageUrl(alumniProfile.getProfilePictureUrl());
            profile.setRole(Role.ALUMNI);
        } else if (userObject instanceof Student) {
            Student student = (Student) userObject;
            profile.setInterests(new ArrayList<>(student.getInterests().stream()
                    .map(InterestEntity::getInterest)
                    .collect(Collectors.toList())));

            profile.setEmail(student.getUser().getEmailId());
            profile.setFirstName(student.getUser().getFirstName());
            profile.setLastName(student.getUser().getLastName());
            profile.setImageUrl(student.getProfilePictureUrl());
            profile.setRole(Role.STUDENT);
        }
        profile.setUserId(userId);
        return profile;
    }
}