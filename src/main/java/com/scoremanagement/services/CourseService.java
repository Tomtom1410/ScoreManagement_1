package com.scoremanagement.services;

import com.scoremanagement.dto.CourseDTO;
import com.scoremanagement.response.ResponseObject;
import org.springframework.http.ResponseEntity;


public interface CourseService {

    ResponseEntity<String> insertCourse(CourseDTO courseDTO);

    ResponseEntity<ResponseObject> getAllCourses(boolean isDelete, String key, Integer page, Integer PAGE_SIZE);

    ResponseEntity<String> deleteCourse(Long id);

    ResponseEntity<ResponseObject> getCourseById(Long id);

    ResponseEntity<ResponseObject> updateCourse(CourseDTO courseDTO);

    ResponseEntity<String> restoreCourse(Long[] courseIdList);
}
