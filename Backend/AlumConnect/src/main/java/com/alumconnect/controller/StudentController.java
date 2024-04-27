package com.alumconnect.controller;

import com.alumconnect.dto.StudentDto;
import com.alumconnect.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/student")
public class StudentController {

    private final StudentService studentService;

//    // Endpoint to get all students
//    @GetMapping("/get")
//    public ResponseEntity<?> getStudent() {
//        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudent());
//    }

    // Endpoint to get a specific student by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getStudent(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentProfile(id));
    }

    // Endpoint to save a new student
    @PostMapping("/save")
    public ResponseEntity<?> saveStudent(@RequestBody StudentDto studentDto) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.saveStudent(studentDto));
    }

    // Endpoint to update an existing student
    @PutMapping ("/update/{userId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long userId, @RequestBody StudentDto studentDto) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudentProfile(userId, studentDto));
    }
//
//    // Endpoint to delete a student by ID
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteStudent(@PathVariable long id) {
//        return ResponseEntity.status(HttpStatus.OK).body(studentService.deleteById(id));
//    }

    // Utility method for debugging (optional)
    private void debug(Object...obj) {
        System.err.println(Arrays.deepToString(obj));
    }
}
