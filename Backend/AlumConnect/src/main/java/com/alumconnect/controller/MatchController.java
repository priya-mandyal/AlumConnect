package com.alumconnect.controller;
import com.alumconnect.async.MatchFounder;
import com.alumconnect.dto.MatchRequest;
import com.alumconnect.dto.MatchResponse;
import com.alumconnect.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/match")
public class MatchController {


    @Autowired
    private MatchService matchService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MatchFounder matchFounder;


    @PostMapping("/initiate/{userId}")
    public ResponseEntity<Object> initiateMatch(@PathVariable Long userId, @RequestBody MatchRequest request) {
        MatchResponse response = matchService.initiateMatch(userId, request);

        if (response != null && !response.getMatchedProfiles().isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            matchFounder.setMatchDetails(userId, request);
            matchFounder.processAsynchronously();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You have been put into the waiting list. You'll be notified when we find a match for you!");
        }
    }
}
