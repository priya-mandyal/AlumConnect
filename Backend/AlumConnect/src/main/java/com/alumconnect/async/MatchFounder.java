package com.alumconnect.async;

import com.alumconnect.dto.MatchRequest;
import com.alumconnect.dto.MatchResponse;
import com.alumconnect.dto.ProfileDTO;
import com.alumconnect.entities.User;
import com.alumconnect.repository.UserRepository;
import com.alumconnect.service.MatchService;
import com.alumconnect.service.interfaces.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class MatchFounder {

    protected static final long WAIT_TIME = 20000;
    private final MatchService matchService;
    private final IEmailService emailService;
    private final UserRepository userRepository;

    private Long userId;
    private MatchRequest request;

    public void setMatchDetails(Long userId, MatchRequest request) {
        this.userId = userId;
        this.request = request;
    }

    @Async
    public void processAsynchronously() {
        while (true) {
            try {
                MatchResponse matchResponse = matchService.initiateMatch(userId, request);
                if (!matchResponse.getMatchedProfiles().isEmpty()) {
                    ProfileDTO profile = matchResponse.getMatchedProfiles().get(0);
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UsernameNotFoundException("No user found with id " + userId));
                    emailService.sendMatchNotificationEmail(user.getEmailId(), profile.getFirstName() + " " + profile.getLastName(), profile.getRole().getName());
                    break;
                }
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ex) {
                break;
            }
        }
    }
}
