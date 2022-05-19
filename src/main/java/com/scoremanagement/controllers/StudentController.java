package com.scoremanagement.controllers;

import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.regex.Pattern;


@RestController
@RequestMapping("api/students")
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final Integer PAGE_SIZE = 2;
    private final String PATTERN = "^[a-zA-ZaAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴ\\s\\d]+$";
    private final String REGEX_FULL_NAME = "^[a-zA-ZaAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴ\\s]+$";

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllStudents(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        if (Pattern.matches(PATTERN, key)) {
            return studentService.getAllStudents(false, key, page, PAGE_SIZE);
        }
        return ResponseEntity.status(400).body(
                new ResponseObject("Search content is not valid! Please choose another content!", null)
        );
    }

    @PreAuthorize("hasAuthority('ADMIN') or #studentDTO.username == authentication.principal.username")
    @PutMapping("update")
    public ResponseEntity<ResponseObject> updateStudent(@Valid @RequestBody StudentDTO studentDTO) {
        if (!Pattern.matches(REGEX_FULL_NAME, studentDTO.getFullName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Full name is invalid! It must be letter!", null)
            );
        }
        return studentService.updateStudent(studentDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("create-account")
    public ResponseEntity<ResponseObject> createAccount(@Valid @RequestBody StudentDTO student) {
        if (student.getPassword() == null || student.getPassword().trim().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Password is not blank or empty!", null));
        }
        if (!Pattern.matches(REGEX_FULL_NAME, student.getFullName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Full name is invalid! It must be letter!", null));
        }

        return studentService.createAccount(student);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #username == authentication.principal.username")
    @GetMapping("/{username}")
    public ResponseEntity<ResponseObject> getStudentById(@PathVariable String username) {
        return studentService.getStudentByUsername(username);
    }

}
