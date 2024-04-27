package com.alumconnect.service;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.CreateUserPost;
import com.alumconnect.dto.UserPost;
import com.alumconnect.entities.UserPostEntity;
import com.alumconnect.mapper.UserPostMapper;
import com.alumconnect.repository.UserPostRepository;
import com.alumconnect.repository.UserRepository;
import com.alumconnect.service.interfaces.IUserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPostService implements IUserPostService {
    private final UserPostRepository userPostRepository;
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;

    @Override
    public ACResponse<Object> createPost(CreateUserPost createUserPost) {
        return userRepository.findById(createUserPost.getUserId())
                .map(user -> {
                    String imageUrl = null;
                    if (createUserPost.getFile() != null) {
                        try {
                            imageUrl = firebaseService.uploadToFirebase(createUserPost.getFile());
                        } catch (IOException e) {
                            return ACResponse.builder()
                                    .success(false)
                                    .message("Image could not be uploaded for user with ID: " + createUserPost.getUserId())
                                    .userId(createUserPost.getUserId())
                                    .build();
                        }
                    }
                    UserPostEntity postEntity = UserPostEntity.builder()
                            .title(createUserPost.getTitle())
                            .text(createUserPost.getText())
                            .imageUrl(imageUrl)
                            .user(user)
                            .build();

                    userPostRepository.save(postEntity);

                    return ACResponse.builder()
                            .success(true)
                            .message("Post created successfully for user with ID: " + createUserPost.getUserId())
                            .userId(createUserPost.getUserId())
                            .build();
                })
                .orElseGet(() -> ACResponse.builder()
                        .success(false)
                        .message("User not found with ID: " + createUserPost.getUserId())
                        .userId(createUserPost.getUserId())
                        .build());
    }


    @Override
    public ACResponse<Object> getAllPosts() {
        List<UserPostEntity> posts = userPostRepository.findAll();
        UserPostMapper userPostMapper = new UserPostMapper();
        List<UserPost> userPosts = posts.stream()
                .map(userPostMapper::map)
                .collect(Collectors.toList());
        return ACResponse.builder()
                .success(true)
                .message("Successfully retrieved all posts.")
                .data(userPosts)
                .build();
    }


    public ACResponse<Object> getPostByUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            return ACResponse.builder()
                    .success(false)
                    .message("User not found with ID: " + userId)
                    .build();
        }

        List<UserPostEntity> userPosts = userPostRepository.findByUserId(userId);
        UserPostMapper userPostMapper = new UserPostMapper();

        List<UserPost> postsDto = userPosts.stream()
                .map(userPostMapper::map)
                .collect(Collectors.toList());

        return ACResponse.builder()
                .success(true)
                .message("Posts returned for user with ID: " + userId)
                .data(postsDto)
                .build();
    }

    @Override
    public ACResponse<Object> updatePost(long id, CreateUserPost updateUserPost) {
        UserPostMapper userPostMapper = new UserPostMapper();
        return userPostRepository.findById(id)
                .map(postEntity -> {
                    postEntity.setText(updateUserPost.getText());
                    postEntity.setTitle(updateUserPost.getTitle());
                    userPostRepository.save(postEntity);
                    return ACResponse.builder()
                            .success(true)
                            .message("Post updated successfully.")
                            .data(userPostMapper.map(postEntity))
                            .build();
                })
                .orElseGet(() -> ACResponse.builder()
                        .success(false)
                        .message("Post not found with ID: " + id)
                        .build());
    }
    @Override
    public ACResponse<Object> deletePost(long postId) {
        boolean exists = userPostRepository.existsById(postId);
        if (exists) {
            userPostRepository.deleteByPostId(postId);
            return ACResponse.builder()
                    .success(true)
                    .message("Post deleted successfully.")
                    .build();
        } else {
            return ACResponse.builder()
                    .success(false)
                    .message("Post not found with ID: " + postId)
                    .build();
        }
    }


}

