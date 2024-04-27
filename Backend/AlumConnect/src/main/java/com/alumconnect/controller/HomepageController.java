package com.alumconnect.controller;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.service.HomepageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/homepage")
@RequiredArgsConstructor
public class HomepageController {

    private final HomepageService homepageService;


    @GetMapping("/posts/{userId}")
    public ResponseEntity<ACResponse<Object>> getPostsForHomepage(@PathVariable Long userId) {
        ACResponse<Object> response = homepageService.getPostsForHomepage(userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}