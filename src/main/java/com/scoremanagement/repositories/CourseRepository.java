package com.scoremanagement.repositories;

import com.scoremanagement.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select c from Course c where concat(c.courseCode, c.courseName) like %:key% " + "and c.isDelete =:isDelete")
    Page<Course> getCoursesByCourseNameOrCourseCode(String key, boolean isDelete, Pageable pageable);

    Course findByCourseCode(String sheetName);
}
