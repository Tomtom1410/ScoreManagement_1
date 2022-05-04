package com.scoremanagement.repositories;

import com.scoremanagement.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select c from Course c where concat(c.courseCode, c.courseName) like %?1%")
    Page<Course> getCoursesByCourseNameOrCourseCode(String key, Pageable pageable);

    void deleteAtId(String id);
}
