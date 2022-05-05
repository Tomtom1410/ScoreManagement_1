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
import com.scoremanagement.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<ResponseObject> getAllStudents(String key, Integer page, Integer PAGE_SIZE) {
        List<Student> studentList = studentRepository.getStudentsAllByFullNameLikeOrRollNumberLike(true, key , PageRequest.of(page - 1, PAGE_SIZE)).getContent();
        if (studentList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Don't have students", null)
            );
        } else {
            List<StudentDTO> studentDTOList = new ArrayList<>();
            for (Student student : studentList) {
                studentDTOList.add(objectMapper.convertValue(student, StudentDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("This is list students in system.", studentDTOList)
            );
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getStudentByUsername(String username) {
        Student student = studentRepository.findStudentByUsername(username);
        if (student == null || student.getAccount().getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not found student with username: " + username, null)
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("The student is:", objectMapper.convertValue(student, StudentDTO.class))
            );
        }
    }
}
