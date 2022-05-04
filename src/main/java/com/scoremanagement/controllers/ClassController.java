package com.scoremanagement.controllers;

import com.scoremanagement.dto.ClazzDTO;
import com.scoremanagement.entities.export_import.BaseExportExcelModel;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.ClassService;
import com.scoremanagement.services.ExportExcelFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/classes")
@AllArgsConstructor
public class ClassController {
    private final ClassService classService;
    private final ExportExcelFileService exportExcelFileService;
    private final Integer PAGE_SIZE = 2;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllClasses(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0){
            page = 1;
        }
        return classService.getAllClasses(key, page, PAGE_SIZE);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getClassById(@PathVariable Long id) {
        return classService.getClassById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("create")
    public ResponseEntity<String> createClass(@Valid @RequestBody ClazzDTO clazzDTO) {
        return classService.insertClass(clazzDTO);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("update")
    public ResponseEntity<ResponseObject> updateClass(@Valid @RequestBody ClazzDTO clazzDTO) {
        return classService.updateClass(clazzDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteClass(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Class ID is invalid!");
        }
        return classService.deleteById(id);
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
