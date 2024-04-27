package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.entities.*;
import com.alumconnect.enums.Role;
import com.alumconnect.repository.AlumniProfileRepository;
import com.alumconnect.repository.HomepagePostRepository;

import com.alumconnect.repository.StudentRepository;
import com.alumconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomepageService {
    private final HomepagePostRepository homepagePostRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AlumniProfileRepository alumniProfileRepository;

    private List<InterestEntity> fetchUserInterests(User user) {
        if (user.getRole() == Role.STUDENT) {
            return studentRepository.findByUserId(user.getId())
                    .map(Student::getInterests)
                    .orElse(Collections.emptyList());
        } else if (user.getRole() == Role.ALUMNI) {
            return alumniProfileRepository.findByUserId(user.getId())
                    .map(AlumniProfileEntity::getInterests)
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    private List<UserPostEntity> fetchPostsBasedOnInterests(List<InterestEntity> interests) {
        if (!interests.isEmpty()) {
            return interests.stream()
                    .flatMap(interestEntity -> homepagePostRepository.findByInterestsOrAllPosts(interestEntity.getInterest()).stream())
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            return homepagePostRepository.findAll().stream()
                    .distinct()
                    .collect(Collectors.toList());
        }
    }

    public ACResponse<Object> getPostsForHomepage(Long userId) {
        if (!userRepository.existsById(userId)) {
            return ACResponse.builder()
                    .success(false)
                    .message("User not found with ID: " + userId)
                    .build();
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found."));
        List<InterestEntity> interests = fetchUserInterests(user);
        List<UserPostEntity> posts = fetchPostsBasedOnInterests(interests);

        if (!posts.isEmpty()) {
            return ACResponse.builder()
                    .success(true)
                    .data(posts)
                    .message("Posts fetched successfully")
                    .build();
        } else {
            return ACResponse.builder()
                    .success(true)
                    .message("Posts fetched successfully")

                    .data(homepagePostRepository.findAll().stream()
                            .distinct()
                            .collect(Collectors.toList()))
                    .build();
        }
    }

}