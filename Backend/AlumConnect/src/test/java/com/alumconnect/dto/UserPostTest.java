package com.alumconnect.dto;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserPostTest {

    @Test
    void testConstructorAndGetters() {
        long id = 1;
        String title = "Test Title";
        String text = "Test Text";
        String imageUrl = "test-image-url.jpg";
        long userId = 123;
        String firstName = "John";
        String lastName = "Doe";

        UserPost userPost = new UserPost(id, title, text, imageUrl, userId, firstName, lastName);

        assertEquals(id, userPost.getId());
        assertEquals(title, userPost.getTitle());
        assertEquals(text, userPost.getText());
        assertEquals(imageUrl, userPost.getImageUrl());
        assertEquals(userId, userPost.getUserId());
        assertEquals(firstName, userPost.getFirstName());
        assertEquals(lastName, userPost.getLastName());
    }

    @Test
    void testSetters() {
        UserPost userPost = new UserPost();

        long id = 1;
        String title = "Test Title";
        String text = "Test Text";
        String imageUrl = "test-image-url.jpg";
        long userId = 123;
        String firstName = "John";
        String lastName = "Doe";

        userPost.setId(id);
        userPost.setTitle(title);
        userPost.setText(text);
        userPost.setImageUrl(imageUrl);
        userPost.setUserId(userId);
        userPost.setFirstName(firstName);
        userPost.setLastName(lastName);

        assertEquals(id, userPost.getId());
        assertEquals(title, userPost.getTitle());
        assertEquals(text, userPost.getText());
        assertEquals(imageUrl, userPost.getImageUrl());
        assertEquals(userId, userPost.getUserId());
        assertEquals(firstName, userPost.getFirstName());
        assertEquals(lastName, userPost.getLastName());
    }

    @Test
    void testToString() {
        long id = 1;
        String title = "Test Title";
        String text = "Test Text";
        String imageUrl = "test-image-url.jpg";
        long userId = 123;
        String firstName = "John";
        String lastName = "Doe";

        UserPost userPost = new UserPost(id, title, text, imageUrl, userId, firstName, lastName);

        String expectedString = "UserPost(id=1, title=Test Title, text=Test Text, imageUrl=test-image-url.jpg, userId=123, firstName=John, lastName=Doe)";
        assertEquals(expectedString, userPost.toString());
    }

    @Test
    void testBuilder() {
        long id = 1;
        String title = "Test Title";
        String text = "Test Text";
        String imageUrl = "test-image-url.jpg";
        long userId = 123;
        String firstName = "John";
        String lastName = "Doe";

        UserPost userPost = UserPost.builder()
                .id(id)
                .title(title)
                .text(text)
                .imageUrl(imageUrl)
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        assertEquals(imageUrl, userPost.getImageUrl());
    }
}