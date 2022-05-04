package com.scoremanagement.controllers;

import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/students")
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final Integer PAGE_SIZE = 2;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllStudents(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return studentService.getAllStudents(key, page, PAGE_SIZE);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #username == authentication.principal.username")
    @GetMapping("/{username}")
    public ResponseEntity<ResponseObject> getStudentById(@PathVariable String username) {
        return studentService.getStudentByUsername(username);
    }

}
