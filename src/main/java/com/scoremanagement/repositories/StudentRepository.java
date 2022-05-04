package com.scoremanagement.repositories;

import com.scoremanagement.entities.Clazz;
import com.scoremanagement.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Student findStudentByUsername(String username);

    @Query(value = "SELECT roll_number FROM students ORDER BY roll_number DESC LIMIT 1;", nativeQuery = true)
    String getLastRollNumber();

    @Query(value = "select s from Student s join s.account a left join s.clazz c " +
            "where " +
            "(concat(s.fullName,s.rollNumber) like %:key% or c.className like %:key%) AND " +
            "(:isDelete is null or :isDelete = a.isDelete)")
    Page<Student> getStudentsAllByFullNameLikeOrRollNumberLike(Boolean isDelete, String key, Pageable page);

    List<Student> findAllByClazz(Clazz clazz);
}
