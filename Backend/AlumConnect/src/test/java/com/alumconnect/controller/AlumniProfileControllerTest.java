package com.alumconnect.controller;

import com.alumconnect.config.JwtService;
import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.AlumniProfile;
import com.alumconnect.service.AlumniProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AlumniProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AlumniProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlumniProfileService alumniProfileService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;


    @Test
    @WithMockUser
    void verifyCreateAlumniProfileWasCalledOnce_whenHitsAPI() throws Exception {
        AlumniProfile alumniProfileDTO = new AlumniProfile();
        ACResponse<Object> response = ACResponse.builder().success(true).build();

        when(alumniProfileService.createAlumniProfile(any(AlumniProfile.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/alumni-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alumniProfileDTO)))
                .andExpect(status().isOk());

        verify(alumniProfileService, times(1)).createAlumniProfile(any(AlumniProfile.class));
    }

    @Test
    @WithMockUser
    void verifyUpdateAlumniProfileWasCalledOnce_whenHitsAPI() throws Exception {
        String userId = "1";
        AlumniProfile alumniProfileDTO = new AlumniProfile();
        ACResponse<Object> response = ACResponse.builder().success(true).build();

        when(alumniProfileService.updateAlumniProfile(eq(Long.parseLong(userId)), any(AlumniProfile.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/alumni-profiles/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alumniProfileDTO)))
                .andExpect(status().isOk());

        verify(alumniProfileService, times(1)).updateAlumniProfile(eq(Long.parseLong(userId)), any(AlumniProfile.class));
    }

    @Test
    @WithMockUser
    void verifyGetAlumniProfileWasCalledOnce_whenHitsAPI() throws Exception {
        String userId = "1";
        ACResponse<Object> response = ACResponse.builder().success(true).build();

        when(alumniProfileService.getAlumniProfile(eq(Long.valueOf(userId)))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/alumni-profiles/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(alumniProfileService, times(1)).getAlumniProfile(eq(Long.valueOf(userId)));
    }

    @Test
    @WithMockUser
    void returnInternalServerError_whenExceptionOccurs() throws Exception {
        String userId = "1";

        when(alumniProfileService.getAlumniProfile(eq(Long.valueOf(userId)))).thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/alumni-profiles/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
