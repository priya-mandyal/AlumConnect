package com.alumconnect.mapper;

import com.alumconnect.dto.UserPost;
import com.alumconnect.entities.UserPostEntity;
import com.alumconnect.mapper.interfaces.MapperObject;

public class UserPostMapper implements MapperObject<UserPostEntity, UserPost> {

    @Override
    public UserPost map(UserPostEntity userPostEntity) {
        UserPost userPost = new UserPost();
        userPost.setUserId(userPostEntity.getUser().getId());
        userPost.setId(userPostEntity.getId());
        userPost.setText(userPostEntity.getText());
        userPost.setTitle(userPostEntity.getTitle());
        userPost.setImageUrl(userPostEntity.getImageUrl());
        userPost.setFirstName(userPostEntity.getUser().getFirstName());
        userPost.setLastName(userPostEntity.getUser().getLastName());
        return userPost;
    }
}
