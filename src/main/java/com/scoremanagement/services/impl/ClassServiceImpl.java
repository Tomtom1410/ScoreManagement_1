package com.scoremanagement.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoremanagement.entities.Student;
import com.scoremanagement.repositories.StudentRepository;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.dto.ClazzDTO;
import com.scoremanagement.entities.Clazz;
import com.scoremanagement.repositories.ClassRepository;
import com.scoremanagement.services.ClassService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<String> insertClass(ClazzDTO classesDTO) {
        if (!classRepository.existsByClassName(classesDTO.getClassName())) {
            classesDTO.setIsDelete(false);
            Clazz clazz = classRepository.save(objectMapper.convertValue(classesDTO, Clazz.class));
            if (clazz.getId() > 0) {
                return ResponseEntity.ok("Created successful!");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Created failed!");
        }
        return ResponseEntity.status(400).body("Class \"" + classesDTO.getClassName() + "\" was existed!");
    }

    @Override
    public ResponseEntity<ResponseObject> getClassById(Long classId) {
        ClazzDTO clazzDTO = objectMapper.convertValue(classRepository.findById(classId).orElse(null), ClazzDTO.class);
        if (clazzDTO == null || clazzDTO.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not found class with id " + classId, null)
            );
        } else {
            Clazz clazz = new Clazz();
            clazz.setId(classId);
            List<Student> studentList = studentRepository.findAllByClazzAndAccount_IsDelete(clazz, false);
            if (!studentList.isEmpty()) {
                clazzDTO.setStudentList(studentList);
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Found class with id " + classId, clazzDTO)
            );
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getAllClasses(boolean isDelete, String key, Integer page, Integer PAGE_SIZE) {
        List<Clazz> classes = classRepository.findAllByClassNameLikeAndIsDelete("%" + key + "%", isDelete,
                PageRequest.of(page - 1, PAGE_SIZE
                        , Sort.by("className").ascending()
                )).getContent();
        if (classes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Don't have classes in system", null)
            );
        } else {
            List<ClazzDTO> classesDTOList = new ArrayList<>();
            for (Clazz c : classes) {
                classesDTOList.add(objectMapper.convertValue(c, ClazzDTO.class));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("This list classes", classesDTOList)
            );
        }
    }

    @Override
    public ResponseEntity<String> deleteById(Long id) {
        Clazz clazz = classRepository.findById(id).orElse(null);
        if (clazz != null && !clazz.getIsDelete()) {
            clazz.setIsDelete(true);
            classRepository.save(clazz);
            return ResponseEntity.ok("Delete Successful!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found class with id = " + id);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> updateClass(ClazzDTO clazz) {
        Clazz clazz1 = classRepository.findById(clazz.getId()).orElse(null);
        if (clazz1 == null || clazz1.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not found " + clazz.getClassName(), null)
            );
        } else {
            if (!classRepository.existsByClassName(clazz.getClassName())) {
                classRepository.save(objectMapper.convertValue(clazz, Clazz.class));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Update successful!", clazz)
                );
            }
            return ResponseEntity.status(400).body(
                    new ResponseObject("Class \"" + clazz.getClassName() + "\" was existed!", null)
            );
        }
    }

    @Override
    public ResponseEntity<String> restoreClass(Long[] classIdList) {
        if (classIdList.length <= 0) {
            return ResponseEntity.status(400).body("You don't choose class(es) to restore!");
        }
        for (Long classId : classIdList) {
            Clazz clazz = classRepository.findById(classId).orElse(null);
            if (clazz != null) {
                clazz.setIsDelete(false);
                classRepository.save(clazz);
            }
        }
        return ResponseEntity.ok("Restore successful!");
    }
}