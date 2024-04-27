package com.alumconnect.service.interfaces;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.CreateUserPost;

public interface IUserPostService {
    ACResponse<Object> createPost(CreateUserPost createUserPost);
    ACResponse<Object> getAllPosts();
    ACResponse<Object> getPostByUserId(long id);
    ACResponse<Object> updatePost(long id, CreateUserPost updateUserPost);
    ACResponse<Object> deletePost(long id);
}
