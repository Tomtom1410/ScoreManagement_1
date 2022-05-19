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
    public ResponseEntity<String> createCourse(CourseDTO courseDTO) {
        if (!courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
            Course course = objectMapper.convertValue(courseDTO, Course.class);
            course.setIsDelete(false);
            courseRepository.save(course);
            return course.getId() != null ?
                    ResponseEntity.status(HttpStatus.CREATED).body("Create course successful!")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create failed!");
        }
        return ResponseEntity.status(400).body("Course \""+ courseDTO.getCourseCode()+"\" was existed!");
    }

    @Override
    public ResponseEntity<ResponseObject> getAllCourses(boolean isDelete, String key, Integer page, Integer PAGE_SIZE) {
        List<Course> courseList = courseRepository.getCoursesByCourseNameOrCourseCode(key, isDelete,
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

        if (courseRepository.existsByIdAndIsDelete(courseDTO.getId(), true)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not found course with id = " + courseDTO.getCourseCode(), null)
            );
        } else {
            if (!courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
                Course course = objectMapper.convertValue(courseDTO, Course.class);
                course.setIsDelete(false);
                courseRepository.save(course);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Update done!", courseDTO)
                );
            }
            return  ResponseEntity.status(400).body(
                    new ResponseObject("Course \""+ courseDTO.getCourseCode()+"\" was existed!", null)
            );
        }
    }



    @Override
    public ResponseEntity<String> restoreCourse(Long[] courseIdList) {
        if (courseIdList.length <= 0) {
            return ResponseEntity.status(400).body("You don't choose course(s) to restore!");
        }
        for (Long courseId : courseIdList) {
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course != null) {
                course.setIsDelete(false);
                courseRepository.save(course);
            }
        }
        return ResponseEntity.status(200).body("Restore successful!");
    }

}
