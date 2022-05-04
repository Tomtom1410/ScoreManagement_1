package com.scoremanagement.controllers;

import com.scoremanagement.dto.CourseDTO;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/courses")
@AllArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final Integer PAGE_SIZE = 2;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllCourses(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0){
            page = 1;
        }
        return courseService.getAllCourses(key, page, PAGE_SIZE);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping("create")
    public ResponseEntity<String> createCourse(@Valid @RequestBody CourseDTO course) {
        return courseService.insertCourse(course);
    }

    @PutMapping("update")
    public ResponseEntity<ResponseObject> updateCourse(@Valid @RequestBody CourseDTO courseDTO) {
        if (courseDTO.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("ID is not valid!", courseDTO)
            );
        }
        return courseService.updateCourse(courseDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is not valid!");
        }
        return courseService.deleteCourse(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
