package com.scoremanagement.controllers;

import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.services.ImportExcelFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
public class ImportExportController {

    private final ImportExcelFileService importExcelFileService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("grade/import")
    public ResponseEntity<String> importScoreByClassAndCourse(@RequestBody MultipartFile file) {
        return importExcelFileService.importScoreByClassAndCourse(file);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("student/import")
    public ResponseEntity<String> importStudents(@RequestBody MultipartFile file){
        return importExcelFileService.importStudents(file);
    }
}
