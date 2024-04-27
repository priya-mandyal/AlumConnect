package com.alumconnect.controller;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.AlumniProfile;
import com.alumconnect.service.AlumniProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumni-profiles")
@RequiredArgsConstructor
public class AlumniProfileController {

    private final AlumniProfileService alumniProfileService;

    @PostMapping
    public ResponseEntity<ACResponse<Object>> createAlumniProfile(@RequestBody AlumniProfile alumniProfileDTO) {
        return ResponseEntity.ok(alumniProfileService.createAlumniProfile(alumniProfileDTO));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ACResponse<Object>> updateAlumniProfile(@PathVariable String userId, @RequestBody AlumniProfile alumniProfileDTO) {
        return ResponseEntity.ok(alumniProfileService.updateAlumniProfile(Long.parseLong(userId), alumniProfileDTO));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ACResponse<Object>>  getAlumniProfile(@PathVariable String userId) {
        return ResponseEntity.ok(alumniProfileService.getAlumniProfile(Long.valueOf(userId)));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

