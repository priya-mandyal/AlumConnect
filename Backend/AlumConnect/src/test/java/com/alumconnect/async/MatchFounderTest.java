package com.alumconnect.async;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.alumconnect.dto.MatchRequest;
import com.alumconnect.dto.MatchResponse;
import com.alumconnect.dto.ProfileDTO;
import com.alumconnect.entities.User;
import com.alumconnect.enums.Role;
import com.alumconnect.repository.UserRepository;
import com.alumconnect.service.MatchService;
import com.alumconnect.service.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MatchFounderTest {

    @Mock
    private MatchService matchService;
    @Mock
    private IEmailService emailService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private MatchFounder matchFounder;

    @Test
    void sendEmail_whenMatchFound() throws MessagingException {
        MatchResponse matchResponse = new MatchResponse();
        ProfileDTO profile = new ProfileDTO();
        profile.setRole(Role.ALUMNI);
        profile.setEmail("p@p.com");
        profile.setFirstName("P");
        profile.setLastName("M");
        List<ProfileDTO> profiles = List.of(profile);
        matchResponse.setMatchedProfiles(profiles);
        User mockUser = mock(User.class);
        when(matchService.initiateMatch(anyLong(), any())).thenReturn(matchResponse);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(mockUser.getEmailId()).thenReturn("test@test.com");
        matchFounder.setMatchDetails(1L, new MatchRequest());
        matchFounder.processAsynchronously();

        verify(emailService, times(1)).sendMatchNotificationEmail(eq("test@test.com"), eq("P M"), eq("Alumni"));
    }


    @Test
    void noEmailSent_whenNoMatchNotFound() throws MessagingException {
        when(matchService.initiateMatch(anyLong(), any(MatchRequest.class))).thenReturn(new MatchResponse());

        matchFounder.setMatchDetails(1L, new MatchRequest());
        matchFounder.processAsynchronously();

        verify(emailService, never()).sendMatchNotificationEmail(anyString(), anyString(), anyString());
    }

    @Test
    void noEmailSent_whenNoMatchFounderThrowsException() throws MessagingException {
        MatchResponse matchResponse = new MatchResponse();
        ProfileDTO profile = new ProfileDTO();
        profile.setRole(Role.ALUMNI);
        profile.setEmail("p@p.com");
        profile.setFirstName("P");
        profile.setLastName("M");
        List<ProfileDTO> profiles = List.of(profile);
        matchResponse.setMatchedProfiles(profiles);
        when(matchService.initiateMatch(eq(1L), any(MatchRequest.class))).thenReturn(matchResponse);
        when(userRepository.findById(anyLong())).thenThrow(UsernameNotFoundException.class);
        matchFounder.setMatchDetails(1L, new MatchRequest());
        matchFounder.processAsynchronously();

        verify(emailService, never()).sendMatchNotificationEmail(anyString(), anyString(), anyString());
    }

    @Test
    void noEmailSent_whenNoMatchFound() throws MessagingException {
        MatchResponse matchResponse = new MatchResponse();
        ProfileDTO profile = new ProfileDTO();
        profile.setRole(Role.ALUMNI);
        profile.setEmail("p@p.com");
        profile.setFirstName("P");
        profile.setLastName("M");
        when(matchService.initiateMatch(eq(1L), any(MatchRequest.class))).thenReturn(matchResponse);
        matchFounder.setMatchDetails(1L, new MatchRequest());
        matchFounder.processAsynchronously();

        verify(emailService, never()).sendMatchNotificationEmail(anyString(), anyString(), anyString());
    }

    @Test
    void noEmailSent_whenNoMatchFounderThrowsInterruptedException() throws Exception {
        when(matchService.initiateMatch(eq(1L), any(MatchRequest.class)))
                .thenAnswer(invocation -> {
                    throw new InterruptedException("Simulated interrupt");
                });

        Thread testThread = new Thread(() -> {
            matchFounder.setMatchDetails(1L, new MatchRequest());
            matchFounder.processAsynchronously();
        });
        testThread.start();
        testThread.join();

        assertTrue(testThread.isInterrupted());
    }


}
