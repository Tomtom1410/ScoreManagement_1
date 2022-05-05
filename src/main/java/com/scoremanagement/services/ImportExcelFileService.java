package com.scoremanagement.services;

import com.scoremanagement.entities.Score;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImportExcelFileService {
    ResponseEntity<String> importExcel(MultipartFile file);
}
