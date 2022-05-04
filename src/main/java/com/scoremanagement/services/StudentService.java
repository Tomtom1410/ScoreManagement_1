package com.scoremanagement.services;

import com.scoremanagement.dto.ScoreDTO;
import com.scoremanagement.entities.export_import.ScoreExportExcelModel;
import com.scoremanagement.entities.export_import.StudentExportExcelModel;
import com.scoremanagement.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface StudentService {
    ResponseEntity<ResponseObject> getAllStudents(String key, Integer page, Integer PAGE_SIZE);

    ResponseEntity<ResponseObject> getStudentByUsername(String username);

//    List<StudentExportExcelModel> getStudentForExport();
//
//    ResponseEntity<List<ScoreDTO>> getScoresOfStudent(String username);

//    List<ScoreExportExcelModel> getScoresForExport(String usernam);
}
