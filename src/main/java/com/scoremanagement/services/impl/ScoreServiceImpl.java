package com.scoremanagement.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoremanagement.dto.ScoreDTO;
import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.entities.Score;
import com.scoremanagement.entities.Student;
import com.scoremanagement.entities.export_import.ScoreExportExcelModel;
import com.scoremanagement.entities.export_import.StudentExportExcelModel;
import com.scoremanagement.repositories.ScoreRepository;
import com.scoremanagement.repositories.StudentRepository;
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
    private final StudentRepository studentRepository;

    private List<ScoreDTO> getScore(String username) {
        List<Score> scoreList = scoreRepository.findAllByStudent_Username(username);
        return convertToDTO(scoreList);
    }

    @Override
    public ResponseEntity<ResponseObject> getScoresOfStudent(String username) {
        if (studentRepository.existsByUsernameAndAccount_IsDelete(username, false)) {
            List<ScoreDTO> scoreDTOList = getScore(username);
            if (scoreDTOList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Student " + username + " haven't score!", null)
                );
            }
            return ResponseEntity.ok(new ResponseObject("Found!", scoreDTOList));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Don't have student with username is " + username, null)
        );
    }

    @Override
    public List<ScoreExportExcelModel> getScoresForExport(String username) {
        List<ScoreExportExcelModel> exportList = new ArrayList<>();
        List<ScoreDTO> scoreDTOList = getScore(username);
        for (ScoreDTO scoreDTO : scoreDTOList) {
            ScoreExportExcelModel scoreExport = new ScoreExportExcelModel();
            scoreExport.setScore(scoreDTO.getScore());
            scoreExport.setCourseName(scoreDTO.getCourse().getCourseName());
            exportList.add(scoreExport);
        }
        return exportList;
    }

    @Override
    public List<StudentExportExcelModel> getScoreExportForClass(Long id, String courseCode) {
        List<Score> scoreList = scoreRepository.
                findAllByStudent_Account_IsDeleteAndStudent_Clazz_IdAndCourse_CourseCode(false, id, courseCode);
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
            if (scoreDTO.getScore() < 0) {
                return ResponseEntity.status(400).body("Score must be bigger or equal 0!");
            }
            scoreList.add(objectMapper.convertValue(scoreDTO, Score.class));
        }
        scoreRepository.saveAll(scoreList);
        return ResponseEntity.ok("Update successful!");
    }

    @Override
    public ResponseEntity<ResponseObject> getScoreByClassAndCourse(Long id, String courseCode) {
        List<Score> scoreList = scoreRepository.
                findAllByStudent_Account_IsDeleteAndStudent_Clazz_IdAndCourse_CourseCode(false, id, courseCode);
        if (scoreList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not Found Score Table!", null)
            );
        }
        List<ScoreDTO> scoreDTOList = convertToDTO(scoreList);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Found!", scoreDTOList)
        );
    }

    private List<ScoreDTO> convertToDTO(List<Score> scoreList) {
        List<ScoreDTO> scoreDTOList = new ArrayList<>();
        for (Score score : scoreList) {
            ScoreDTO scoreDTO = objectMapper.convertValue(score, ScoreDTO.class);
            scoreDTO.setStudent(new StudentDTO(score.getStudent()));
            scoreDTOList.add(scoreDTO);
        }
        return scoreDTOList;
    }
}
