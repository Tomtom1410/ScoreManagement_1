package com.scoremanagement.controllers;

import com.scoremanagement.entities.Score;
import com.scoremanagement.services.ImportExcelFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class ImportExportController {

    private final ImportExcelFileService importExcelFileService;

    @PostMapping("students/import")
    public ResponseEntity<String> importStudent(@RequestBody MultipartFile file) {
        return importExcelFileService.importExcel(file);
    }
}
