package com.scoremanagement.controllers;

import com.scoremanagement.dto.ScoreDTO;
import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.entities.export_import.BaseExportExcelModel;
import com.scoremanagement.entities.export_import.ScoreExportExcelModel;
import com.scoremanagement.entities.export_import.StudentExportExcelModel;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.ExportExcelFileService;
import com.scoremanagement.services.ScoreService;
import com.scoremanagement.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/grade")
@AllArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;
    private final int PAGE_SIZE = 2;

    @PreAuthorize("hasAuthority('ADMIN') or #username == authentication.principal.username")
    @GetMapping("student")
    public ResponseEntity<ResponseObject> getScoresOfStudent(
            @RequestParam(name = "username") String username) {
        return scoreService.getScoresOfStudent(username);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("class/{id}/course/{courseCode}")
    public ResponseEntity<ResponseObject> getScoreByClassAndCourse(
            @PathVariable Long id,
            @PathVariable String courseCode
    ) {
        return scoreService.getScoreByClassAndCourse(id, courseCode);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("update")
    public ResponseEntity<String> updateScoreOfClass(@RequestBody List<ScoreDTO> scoreDTOList
    ) {
        return scoreService.updateScoreTable(scoreDTOList);
    }
}
