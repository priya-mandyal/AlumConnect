package com.alumconnect.controller;

import com.alumconnect.async.MatchFounder;
import com.alumconnect.config.JwtService;
import com.alumconnect.dto.MatchRequest;
import com.alumconnect.dto.MatchResponse;
import com.alumconnect.dto.ProfileDTO;
import com.alumconnect.service.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MatchController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatchFounder matchFounder;
    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void verifyInitiateMatch_SuccessfulMatch() throws Exception {
        Long userId = 1L;
        MatchRequest request = new MatchRequest();
        MatchResponse response = new MatchResponse();
        response.setMatchedProfiles(List.of(mock(ProfileDTO.class)));
        when(matchService.initiateMatch(eq(userId), any(MatchRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/match/initiate/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(matchService, times(1)).initiateMatch(eq(userId), any(MatchRequest.class));
    }

    @Test
    @WithMockUser
    void verifyInitiateMatch_UserAddedToWaitingList() throws Exception {
        Long userId = 1L;
        MatchRequest request = new MatchRequest();
        MatchResponse response = new MatchResponse();
        response.setMatchedProfiles(Collections.emptyList());

        when(matchService.initiateMatch(eq(userId), any(MatchRequest.class))).thenReturn(response);
        doNothing().when(matchFounder).processAsynchronously();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/match/initiate/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("You have been put into the waiting list. You'll be notified when we find a match for you!"));

        verify(matchService, times(1)).initiateMatch(eq(userId), any(MatchRequest.class));
        verify(matchFounder, times(1)).setMatchDetails(any(), any(MatchRequest.class));
    }

}