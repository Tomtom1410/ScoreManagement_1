package com.scoremanagement.controllers;

import com.scoremanagement.dto.ClazzDTO;
import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.entities.export_import.BaseExportExcelModel;
import com.scoremanagement.entities.export_import.ScoreExportExcelModel;
import com.scoremanagement.entities.export_import.StudentExportExcelModel;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.ClassService;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/export")
@AllArgsConstructor
public class ExportController {
    private final StudentService studentService;
    private final ExportExcelFileService exportExcelFileService;
    private final ScoreService scoreService;
    private final ClassService classService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("students")
    public ResponseEntity<ResponseObject> exportExcel(
            @RequestParam("username") String username) {
        if (username == null || username.trim().length() == 0) {
            return ResponseEntity.badRequest().body(new ResponseObject("Error! Please check again!", null));
        }
        StudentDTO studentDTO = (StudentDTO) studentService.getStudentByUsername(username).getBody().getData();
        List<BaseExportExcelModel> list = new ArrayList<>();
        list.addAll(scoreService.getScoresForExport(username));
        File file = exportExcelFileService.exportFile(studentDTO.getFullName(),
                studentDTO.getFullName(), list, ScoreExportExcelModel.class);
        return ResponseEntity.ok().body(new ResponseObject("Export success!", file));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("class/grade")
    public ResponseEntity<ResponseObject> exportGradeByClassAndCourse(
            @RequestParam(name = "classId", defaultValue = "") Long id,
            @RequestParam(name = "courseCode", defaultValue = "") String courseCode
    ) {
        ClazzDTO clazzDTO = (ClazzDTO) classService.getClassById(id).getBody().getData();
        List<StudentExportExcelModel> exportExcelModels = scoreService.getScoreExportForClass(id, courseCode);
        if (!exportExcelModels.isEmpty()) {
            List<BaseExportExcelModel> list = new ArrayList<>();
            list.addAll(exportExcelModels);
            File file = exportExcelFileService.exportFile("Score of " + clazzDTO.getClassName(), courseCode, list, StudentExportExcelModel.class);
            ResponseEntity.status(200).body(new ResponseObject("Export success!", file));
        }
        return ResponseEntity.status(404).body(
                new ResponseObject("Not Score of class with course " + courseCode + " !", null)
        );
    }
}
