package com.alumconnect.controller;

import com.alumconnect.config.JwtService;
import com.alumconnect.dto.ACResponse;
import com.alumconnect.service.HomepageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HomepageController.class)
@AutoConfigureMockMvc(addFilters = false)
public class HomepageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HomepageService homepageService;
    @MockBean
    private JwtService jwtService;

    private static final String API_PATH = "/api/homepage/posts/";

    @Test
    @WithMockUser
    void verifyGetPostsForHomepageSuccess_whenValidUserId() throws Exception {
        Long userId = 1L;
        ACResponse<Object> expectedResponse = ACResponse.builder()
                .success(true)
                .message("Posts fetched successfully")
                .build();

        when(homepageService.getPostsForHomepage(userId)).thenReturn(expectedResponse);

        mockMvc.perform(get(API_PATH + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true,\"message\":\"Posts fetched successfully\"}"));

        verify(homepageService, times(1)).getPostsForHomepage(userId);
    }

    @Test
    @WithMockUser
    void verifyGetPostsForHomepageFailure_whenUserNotFound() throws Exception {
        Long userId = 2L;
        ACResponse<Object> expectedResponse = ACResponse.builder()
                .success(false)
                .message("User not found with ID: " + userId)
                .build();

        when(homepageService.getPostsForHomepage(userId)).thenReturn(expectedResponse);

        mockMvc.perform(get(API_PATH + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"success\":false,\"message\":\"User not found with ID: 2\"}"));

        verify(homepageService, times(1)).getPostsForHomepage(userId);
    }

    @Test
    @WithMockUser
    void verifyGetPostsForHomepageEmptyPosts_whenNoPostsFound() throws Exception {
        Long userId = 3L;
        ACResponse<Object> expectedResponse = ACResponse.builder()
                .success(false)
                .message("No posts found")
                .build();

        when(homepageService.getPostsForHomepage(userId)).thenReturn(expectedResponse);

        mockMvc.perform(get(API_PATH + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"success\":false,\"message\":\"No posts found\"}"));

        verify(homepageService, times(1)).getPostsForHomepage(userId);
    }
}