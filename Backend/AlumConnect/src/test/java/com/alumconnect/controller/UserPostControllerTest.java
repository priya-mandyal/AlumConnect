package com.alumconnect.controller;

import com.alumconnect.config.JwtService;
import com.alumconnect.dto.ACResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alumconnect.dto.CreateUserPost;
import com.alumconnect.service.interfaces.IUserPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserPostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserPostService userPostService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
    }

    @Test
    @WithMockUser
    void verifyGetAllPostsWasCalledOnce_whenHitsAPI() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user-posts/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userPostService, times(1)).getAllPosts();
    }

    @Test
    @WithMockUser
    void verifyGetPostByUserIdWasCalledOnce_whenHitsAPI() throws Exception {
        long userId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user-posts/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userPostService, times(1)).getPostByUserId(userId);
    }

    @Test
    @WithMockUser
    void verifyDeletePostWasCalledOnce_whenHitsAPI() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user-posts/delete/1"))
                .andExpect(status().isOk());

        verify(userPostService, times(1)).deletePost(1L);
    }

    @Test
    @WithMockUser
    void verifyCreatePostWasCalledOnce_whenHitsAPI() throws Exception {
        long userId = 1L;
        String title = "Test Title";
        String text = "Test Text";
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "Some dataset".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/user-posts/create/{userId}", userId)
                        .file(file)
                        .param("title", title)
                        .param("text", text))
                .andExpect(status().isOk());

        verify(userPostService, times(1)).createPost(any(CreateUserPost.class));
    }

    @Test
    @WithMockUser
    void verifyUpdatePostWasCalledOnce_whenHitsAPI() throws Exception {
        long postId = 1L;
        CreateUserPost updateUserPost = new CreateUserPost(postId, "Updated Title", "Updated Text", null);

        when(userPostService.updatePost(eq(postId), any(CreateUserPost.class))).thenReturn(ACResponse.builder().success(true).build());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user-posts/update/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserPost)))
                .andExpect(status().isOk());

        verify(userPostService, times(1)).updatePost(eq(postId), any(CreateUserPost.class));
    }

    @Test
    @WithMockUser
    public void returnInternalServerError_whenExceptionOccurs() throws Exception {
        when(userPostService.getAllPosts()).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user-posts/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


}
