package com.scoremanagement.services;

import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.response.ResponseObject;
import org.springframework.http.ResponseEntity;


public interface StudentService {
    ResponseEntity<ResponseObject> getAllStudents(boolean isDelete, String key, Integer page, Integer PAGE_SIZE);

    ResponseEntity<ResponseObject> getStudentByUsername(String username);

    ResponseEntity<ResponseObject> createAccount(StudentDTO student);

    ResponseEntity<ResponseObject> updateStudent(StudentDTO studentDTO);

    ResponseEntity<String> restoreStudent(String[] studentId);
}
