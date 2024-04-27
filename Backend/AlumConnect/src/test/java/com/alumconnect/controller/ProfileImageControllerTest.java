package com.alumconnect.controller;

import com.alumconnect.config.JwtService;
import com.alumconnect.dto.ACResponse;
import com.alumconnect.service.ChatEngineAvatarService;
import com.alumconnect.service.ImageService;

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
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProfileImageController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProfileImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @MockBean
    private ChatEngineAvatarService chatEngineAvatarService;

    @MockBean
    private JwtService jwtService;


    @Test
    @WithMockUser
    void verifyServerException_whenErrorOccursInUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.jpg", "image/jpeg", "some-image".getBytes());
        String userId = "1";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/images/upload/{userId}", userId)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    void verifyOkResponse_whenImageUploaded() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.jpg", "image/jpeg", "some-image".getBytes());
        String userId = "1";
        ACResponse<Object> mockResponse = mock(ACResponse.class);

        when(imageService.uploadImage(any(MultipartFile.class), eq(1L))).thenReturn(mockResponse);
        when(mockResponse.getUserId()).thenReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/images/upload/{userId}", userId)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void verifyServerException_whenErrorOccursInDownload() throws Exception {
        String userId = "1";

        when(imageService.downloadImage(eq(1L))).thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/images/download/{userId}", userId)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    void verifyOkResponse_whenImageDownloaded() throws Exception {
        String userId = "1";

        mockMvc.perform(MockMvcRequestBuilders.get("/images/download/{userId}", userId)
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk());

        verify(imageService, times(1)).downloadImage(eq(Long.parseLong(userId)));
    }
}
