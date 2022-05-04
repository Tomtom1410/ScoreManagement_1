package com.scoremanagement.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoremanagement.dto.ScoreDTO;
import com.scoremanagement.entities.Score;
import com.scoremanagement.entities.Student;
import com.scoremanagement.entities.export_import.ScoreExportExcelModel;
import com.scoremanagement.entities.export_import.StudentExportExcelModel;
import com.scoremanagement.repositories.ScoreRepository;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.ScoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ObjectMapper objectMapper;
    private final ScoreRepository scoreRepository;

    private List<ScoreDTO> getScore(String username) {
        Student student = new Student();
        student.setUsername(username);
        List<Score> scoreList = scoreRepository.findAllByStudent(student);
        List<ScoreDTO> scoreDTOList = new ArrayList<>();
        for (Score score : scoreList) {
            scoreDTOList.add(objectMapper.convertValue(score, ScoreDTO.class));
        }
        return scoreDTOList;
    }

    @Override
    public ResponseEntity<List<ScoreDTO>> getScoresOfStudent(String username) {
        return ResponseEntity.ok(getScore(username));
    }

    @Override
    public List<ScoreExportExcelModel> getScoresForExport(String username) {
        List<ScoreExportExcelModel> exportList = new ArrayList<>();
        List<ScoreDTO> scoreDTOList = getScore(username);
        for (ScoreDTO scoreDTO : scoreDTOList) {
            exportList.add(objectMapper.convertValue(scoreDTO, ScoreExportExcelModel.class));
        }
        return exportList;
    }

    @Override
    public List<StudentExportExcelModel> getScoreExportForClass(Long id, String courseCode) {
        List<Score> scoreList = scoreRepository.getScoresByClassAndCourse(id, courseCode);
        List<StudentExportExcelModel> export = new ArrayList<>();
        for (Score score : scoreList) {
            StudentExportExcelModel student = new StudentExportExcelModel();
            student.setFullName(score.getStudent().getFullName());
            student.setRollNumber(score.getStudent().getRollNumber());
            student.setDob(score.getStudent().getDob());
            student.setGender(score.getStudent().getGender() == true ? "Male" : "Female");
            student.setScore(score.getScore());
            export.add(student);
        }
        return export;
    }

    @Override
    public ResponseEntity<String> updateScoreTable(List<ScoreDTO> scoreDTOList) {
        List<Score> scoreList = new ArrayList<>();
        for (ScoreDTO scoreDTO : scoreDTOList) {
            scoreList.add(objectMapper.convertValue(scoreDTO, Score.class));
        }
        scoreRepository.saveAll(scoreList);
        return ResponseEntity.ok("Update successful!");
    }

    @Override
    public ResponseEntity<ResponseObject> getScoreByClassAndCourse(Long id, String courseCode) {
        List<Score> scoreList = scoreRepository.getScoresByClassAndCourse(id, courseCode);
        if (scoreList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not Found Score Table!", null)
            );
        }

        List<ScoreDTO> scoreDTOList = new ArrayList<>();
        for (Score score : scoreList) {
            score.getStudent().setClazz(null);
            score.getStudent().setCourseList(null);
            scoreDTOList.add(objectMapper.convertValue(score, ScoreDTO.class));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Found!", scoreDTOList)
        );
    }
}
