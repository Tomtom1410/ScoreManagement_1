package com.scoremanagement.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoremanagement.dto.CourseDTO;
import com.scoremanagement.entities.Course;
import com.scoremanagement.repositories.CourseRepository;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<String> insertCourse(CourseDTO courseDTO) {
        Course course = objectMapper.convertValue(courseDTO, Course.class);
        courseRepository.save(course);
        return course.getId() != null ?
                ResponseEntity.status(HttpStatus.CREATED).body("Create course successful!")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create failed!");
    }

    @Override
    public ResponseEntity<ResponseObject> getAllCourses(String key, Integer page, Integer PAGE_SIZE) {
        List<Course> courseList = courseRepository.getCoursesByCourseNameOrCourseCode(key, false,
                PageRequest.of(page - 1, PAGE_SIZE,
                        Sort.by("courseCode").ascending().and(Sort.by("courseName").ascending()))
        ).getContent();
        if (courseList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Don't have course(s) in system", null)
            );
        } else {
            List<CourseDTO> courseDTOList = new ArrayList<>();
            for (Course course : courseList) {
                courseDTOList.add(objectMapper.convertValue(course, CourseDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("There are courses in system", courseDTOList)
            );
        }
    }

    @Override
    public ResponseEntity<String> deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null || course.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found course with id = " + id);
        } else {
            course.setIsDelete(true);
            courseRepository.save(course);
            return ResponseEntity.status(HttpStatus.OK).body("Delete successful!");
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null || course.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not found course with id = " + id, null)
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("This is course information:", objectMapper.convertValue(course, CourseDTO.class))
            );
        }
    }

    @Override
    public ResponseEntity<ResponseObject> updateCourse(CourseDTO courseDTO) {
        Course course = courseRepository.findById(courseDTO.getId()).orElse(null);
        if (course == null || course.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not found course with id = " + courseDTO.getCourseCode(), null)
            );
        } else {
            course = courseRepository.save(course);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Update done!", course)
            );
        }
    }

}
