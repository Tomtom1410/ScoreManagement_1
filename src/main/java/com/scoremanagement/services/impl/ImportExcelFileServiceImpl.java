package com.scoremanagement.services.impl;

import com.scoremanagement.entities.Course;
import com.scoremanagement.entities.Score;
import com.scoremanagement.entities.Student;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class ImportExcelFileServiceImpl implements ImportExcelFileService {
    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final CourseRepository courseRepository;

    @Override
    public ResponseEntity<String> importExcel(MultipartFile file) {
        try {
            List<Score> getData = readScoreExcelFile(file.getInputStream());
            scoreRepository.saveAll(getData);
            return ResponseEntity.ok("Import successful");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Import successful");
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
                                score.setScore((double) currentCell.getNumericCellValue());
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

        }
        return scoreList;
    }
}
