package com.scoremanagement.services;

import com.scoremanagement.dto.ScoreDTO;
import com.scoremanagement.entities.export_import.ScoreExportExcelModel;
import com.scoremanagement.entities.export_import.StudentExportExcelModel;
import com.scoremanagement.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ScoreService {
    ResponseEntity<List<ScoreDTO>> getScoresOfStudent(String username);

    List<ScoreExportExcelModel> getScoresForExport(String username);

    List<StudentExportExcelModel> getScoreExportForClass(Long id, String courseCode);

    ResponseEntity<String> updateScoreTable(List<ScoreDTO> scoreDTOList);

    ResponseEntity<ResponseObject> getScoreByClassAndCourse(Long id, String courseCode);
}
