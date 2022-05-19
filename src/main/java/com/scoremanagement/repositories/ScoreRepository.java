package com.scoremanagement.repositories;

import com.scoremanagement.entities.Course;
import com.scoremanagement.entities.Score;
import com.scoremanagement.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findAllByStudent_Username(String username);

//    @Query("select s from Score s where s.student.account.isDelete =:isDelete " +
//            "and s.student.clazz.id =:id and s.course.courseCode =:courseCode ")
//    List<Score> getScoresByClassAndCourse(boolean isDelete, Long id, String courseCode);

    Score findByStudentAndAndCourse(Student student, Course course);

    List<Score> findAllByStudent_Account_IsDeleteAndStudent_Clazz_IdAndCourse_CourseCode(
            boolean isDelete, Long id, String courseCode);
}
