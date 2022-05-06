package com.scoremanagement.services;

import com.scoremanagement.response.ResponseObject;
import org.springframework.http.ResponseEntity;


public interface StudentService {
    ResponseEntity<ResponseObject> getAllStudents(boolean isDelete, String key, Integer page, Integer PAGE_SIZE);

    ResponseEntity<ResponseObject> getStudentByUsername(String username);

    ResponseEntity<String> restoreStudent(String[] studentId);
}
