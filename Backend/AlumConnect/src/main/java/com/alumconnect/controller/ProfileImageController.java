package com.alumconnect.controller;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.service.ChatEngineAvatarService;
import com.alumconnect.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles Profile Image related APIs.
 */
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ProfileImageController {

    private final ImageService imageService;

    private final ChatEngineAvatarService chatEngineAvatarService;

    @PostMapping(value = "/upload/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> uploadImage(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        try {
            long id = Long.parseLong(userId);

            ACResponse<Object> response = imageService.uploadImage(file, id);
            chatEngineAvatarService.uploadAvatar(response.getUserId(), file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/download/{userId}")
    public ResponseEntity<Object> downloadImage(@PathVariable String userId) {
        try {
            long id = Long.parseLong(userId);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(imageService.downloadImage(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to download image: "+ e.getMessage());
        }
    }
}
