package com.scoremanagement.controllers;

import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.ClassService;
import com.scoremanagement.services.CourseService;
import com.scoremanagement.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bin")
@AllArgsConstructor
public class RecycleBinController {
    private final int PAGE_SIZE = 2;
    private final StudentService studentService;
    private final ClassService classService;
    private final CourseService courseService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("students")
    public ResponseEntity<ResponseObject> getStudentsDeleted(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return studentService.getAllStudents(true, key, page, PAGE_SIZE);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("students/restore")
    public ResponseEntity<String> restoreStudentDeleted(@RequestBody String[] usernameList) {
        return studentService.restoreStudent(usernameList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("classes")
    public ResponseEntity<ResponseObject> getClassesDeleted(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return classService.getAllClasses(true, key, page, PAGE_SIZE);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("classes/restore")
    public ResponseEntity<String> restoreClassesDeleted(@RequestBody Long[] classIdList) {
        return classService.restoreClass(classIdList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("course")
    public ResponseEntity<ResponseObject> getCoursesDeleted(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return courseService.getAllCourses(true, key, page, PAGE_SIZE);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("courses/restore")
    public ResponseEntity<String> restoreCourseDeleted(@RequestBody Long[] courseIdList) {
        return courseService.restoreCourse(courseIdList);
    }

}
