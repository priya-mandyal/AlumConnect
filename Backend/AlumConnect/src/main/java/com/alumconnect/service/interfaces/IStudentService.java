package com.alumconnect.service.interfaces;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.StudentDto;
import com.alumconnect.entities.Student;

import java.util.List;

public interface IStudentService {
    ACResponse<Object> saveStudent(StudentDto studentDto);
    ACResponse<Object> getStudentProfile(Long userId);
    ACResponse<Object> updateStudentProfile(long userId, StudentDto studentDto);
}
