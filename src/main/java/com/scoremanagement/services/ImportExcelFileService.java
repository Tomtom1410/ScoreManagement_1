package com.scoremanagement.services;

import com.scoremanagement.dto.StudentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImportExcelFileService {
    ResponseEntity<String> importScoreByClassAndCourse(MultipartFile file);

    ResponseEntity<String> importStudents(MultipartFile file);
}
