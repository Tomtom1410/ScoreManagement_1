package com.scoremanagement.controllers;

import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.entities.export_import.BaseExportExcelModel;
import com.scoremanagement.entities.export_import.ScoreExportExcelModel;
import com.scoremanagement.entities.export_import.StudentExportExcelModel;
import com.scoremanagement.services.ExportExcelFileService;
import com.scoremanagement.services.ScoreService;
import com.scoremanagement.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/export")
@AllArgsConstructor
public class ExportController {
    private final StudentService studentService;
    private final ExportExcelFileService exportExcelFileService;
    private final ScoreService scoreService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("students")
    public ResponseEntity<String> exportExcel(
            @RequestParam("username") String username) {
        if (username == null || username.trim().length() == 0) {
            return ResponseEntity.badRequest().body("Error! Please check again!");
        }
        StudentDTO studentDTO = (StudentDTO) studentService.getStudentByUsername(username).getBody().getData();
        List<BaseExportExcelModel> list = new ArrayList<>();
        list.addAll(scoreService.getScoresForExport(username));
        exportExcelFileService.exportFile(studentDTO.getFullName(),
                studentDTO.getFullName(), list, ScoreExportExcelModel.class);
        return ResponseEntity.ok().body("Export success!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("class/grade")
    public ResponseEntity<String> exportGradeByClassAndCourse(
            @RequestParam(name = "classId", defaultValue = "") Long id,
            @RequestParam(name = "courseCode", defaultValue = "") String courseCode
    ) {
        List<StudentExportExcelModel> exportExcelModels = scoreService.getScoreExportForClass(id, courseCode);
        if (!exportExcelModels.isEmpty()) {
            List<BaseExportExcelModel> list = new ArrayList<>();
            list.addAll(exportExcelModels);
            exportExcelFileService.exportFile("class_test", courseCode, list, StudentExportExcelModel.class);
            return ResponseEntity.ok("Export success!");
        }
        return ResponseEntity.status(404).body("Not Score of class with course " + courseCode + " !");
    }
}
