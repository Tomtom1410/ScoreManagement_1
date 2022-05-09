package com.scoremanagement.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoremanagement.dto.ClazzDTO;
import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.entities.Account;
import com.scoremanagement.entities.Clazz;
import com.scoremanagement.entities.Course;
import com.scoremanagement.entities.Helper;
import com.scoremanagement.entities.Score;
import com.scoremanagement.entities.Student;
import com.scoremanagement.repositories.AccountRepository;
import com.scoremanagement.repositories.ClassRepository;
import com.scoremanagement.repositories.CourseRepository;
import com.scoremanagement.repositories.ScoreRepository;
import com.scoremanagement.repositories.StudentRepository;
import com.scoremanagement.services.ImportExcelFileService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class ImportExcelFileServiceImpl implements ImportExcelFileService {
    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final CourseRepository courseRepository;
    private final AccountRepository accountRepository;
    private final ClassRepository classRepository;
    private final Helper helper;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<String> importScoreByClassAndCourse(MultipartFile file) {
        try {
            List<Score> getData = readScoreExcelFile(file.getInputStream());
            if (!getData.isEmpty()) {
                String message = checkScoreValid(getData);
                if (message == null) {
                    scoreRepository.saveAll(getData);
                    return ResponseEntity.ok("Import successful");
                } else {
                    return ResponseEntity.status(400).body(message);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error! Please try again.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error! Please try again.");
    }

    @Override
    public ResponseEntity<String> importStudents(MultipartFile file) {
        try {
            List<StudentDTO> getData = readStudentExcelFile(file.getInputStream());
            if (!getData.isEmpty()) {
                for (StudentDTO studentDTO : getData) {
                    String message = checkStudentListValid(studentDTO);
                    if (message != null) {
                        return ResponseEntity.status(400).body(message);
                    }
                    Account account = new Account();
                    account.setIsAdmin(false);
                    account.setIsDelete(false);
                    account.setUsername(studentDTO.getUsername());
                    account.setPassword(helper.hashPassword(studentDTO.getPassword()));
                    accountRepository.save(account);

                    Student student = objectMapper.convertValue(studentDTO, Student.class);
                    student.setRollNumber(helper.generateRollNumber());
                    Clazz clazz = classRepository.findByClassName(studentDTO.getClazz().getClassName());
                    student.setClazz(clazz);
                    studentRepository.save(student);
                }
                return ResponseEntity.status(200).body("Import Successful!");

            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error! Please try again.");
        }
        return ResponseEntity.status(404).body("File is empty! Check again.");
    }

    private List<Score> readScoreExcelFile(InputStream file) {
        List<Score> scoreList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            for (Sheet sheet : workbook) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
                    if (rowNumber == 0) {
                        rowNumber++;
                        continue;
                    }
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    Score score = null;
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        Student student;
                        switch (cellIdx) {
                            case 1:
                                student = studentRepository.findStudentByRollNumber(currentCell.getStringCellValue());
                                if (student != null) {
                                    Course course = courseRepository.findByCourseCode(sheet.getSheetName());
                                    score = scoreRepository.findByStudentAndAndCourse(student, course);
                                    score.setStudent(student);
                                }
                                break;
                            case 5:
                                Double point = currentCell.getNumericCellValue();
                                score.setScore(point);
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    scoreList.add(score);
                }
            }
        } catch (IOException e) {
            return null;
        }
        return scoreList;
    }

    private List<StudentDTO> readStudentExcelFile(InputStream file) {
        List<StudentDTO> studentDTOList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            for (Sheet sheet : workbook) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
                    if (rowNumber == 0) {
                        rowNumber++;
                        continue;
                    }
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    int cellIdx = 0;
                    StudentDTO student = new StudentDTO();
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        switch (cellIdx) {
                            case 1:
                                student.setUsername(currentCell.getStringCellValue());
                                break;
                            case 2:
                                student.setPassword(currentCell.getStringCellValue());
                                break;
                            case 3:
                                student.setFullName(currentCell.getStringCellValue());
                                break;
                            case 4:
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                String dob = df.format(currentCell.getDateCellValue());
                                student.setDob(Date.valueOf(dob));
                                break;
                            case 5:
                                student.setGender(currentCell.getStringCellValue().equalsIgnoreCase("Male"));
                                break;
                            case 6:
                                ClazzDTO clazzDTO = new ClazzDTO();
                                clazzDTO.setClassName(currentCell.getStringCellValue());
                                student.setClazz(clazzDTO);
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    studentDTOList.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return studentDTOList;
    }

    private String checkScoreValid(List<Score> scoreList) {
        for (Score score : scoreList) {
            return score.getScore() < 0 ? "Score of " + score.getStudent().getFullName() + " must be bigger or equal 0!" : null;
        }
        return null;
    }

    private String checkStudentListValid(StudentDTO studentDTO) {
        if (studentRepository.existsById(studentDTO.getUsername())) {
            return "Username " + studentDTO.getUsername() + " was existed! Please check and try again!";
        }
        return null;
    }
}
