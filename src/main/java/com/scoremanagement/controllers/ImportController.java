package com.scoremanagement.controllers;

import com.scoremanagement.services.ImportExcelFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@AllArgsConstructor
public class ImportController {

    private final ImportExcelFileService importExcelFileService;
    public final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("grade/import")
    public ResponseEntity<String> importScoreByClassAndCourse(@RequestBody MultipartFile file) {
        if (TYPE.equals(file.getContentType())) {
            return importExcelFileService.importScoreByClassAndCourse(file);
        }
        return ResponseEntity.status(400).body("You must choose file excel!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("student/import")
    public ResponseEntity<String> importStudents(@RequestBody MultipartFile file) {
        if (TYPE.equals(file.getContentType())) {
            return importExcelFileService.importStudents(file);
        }
        return ResponseEntity.status(400).body("You must choose file excel!");
    }
}
