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

    @Query("select s from Student s where s.account.isDelete =:isDelete AND " +
            " (concat(s.fullName,s.rollNumber) like %:key% or s.clazz.className like %:key%) ")
    Page<Student> getStudentsAllByFullNameLikeOrRollNumberLike(Boolean isDelete, String key, Pageable page);

    List<Student> findAllByClazzAndAccount_IsDelete(Clazz clazz, boolean accountIsDelete);

    Student findStudentByRollNumber(String rollNumber);

    boolean existsByUsernameAndAccount_IsDelete(String username, Boolean isDelete);
}
