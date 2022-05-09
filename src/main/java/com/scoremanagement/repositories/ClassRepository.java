package com.scoremanagement.repositories;

import com.scoremanagement.entities.Clazz;
import com.scoremanagement.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Clazz, Long> {
    Page<Clazz> findAllByClassNameLikeAndIsDelete(String key, boolean isDelete, Pageable pageable);

    Clazz findByClassName(String className);

    boolean existsByClassName(String className);
}
