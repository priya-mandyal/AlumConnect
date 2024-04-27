package com.alumconnect.controller;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.CreateUserPost;
import com.alumconnect.service.interfaces.IUserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/user-posts")
public class UserPostController {

    private final IUserPostService userPostService;

    @PostMapping(value = "/create/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(
            @PathVariable("userId") Long userId,
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        return ResponseEntity.ok(userPostService.createPost(new CreateUserPost(userId, title, text, file)));
    }

    @GetMapping("/")
    public ResponseEntity<ACResponse<Object>> getPosts() {
        return ResponseEntity.ok(userPostService.getAllPosts());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ACResponse<Object>> getPostById(@PathVariable long userId) {
        return ResponseEntity.ok(userPostService.getPostByUserId(userId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ACResponse<Object>> updatePost(@PathVariable long id, @RequestBody CreateUserPost updateUserPost) {
        return ResponseEntity.ok(userPostService.updatePost(id, updateUserPost));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ACResponse<Object>> deletePost(@PathVariable long id) {
        return ResponseEntity.ok(userPostService.deletePost(id));
    }

    /**
     * Exception handler for handling exceptions thrown within the {@code }UserPostController} endpoint.
     *
     * @param e The exception that occurred.
     * @return ResponseEntity with an appropriate error response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ACResponse<Object>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ACResponse.builder()
                .success(false)
                .message("Server Error: Internal error occurred while processing User post APIs.")
                .build());
    }
}
