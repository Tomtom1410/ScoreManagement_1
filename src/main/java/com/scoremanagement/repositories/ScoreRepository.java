package com.scoremanagement.repositories;

import com.scoremanagement.entities.Score;
import com.scoremanagement.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findAllByStudent(Student student);

    @Query("select s from Score s join s.student st join s.course sc " +
            " where  st.clazz.id = :id and sc.courseCode =:courseCode")
    List<Score> getScoresByClassAndCourse(@Param("id") Long id, @Param("courseCode") String courseCode);
}
