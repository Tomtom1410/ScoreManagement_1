package com.scoremanagement.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoremanagement.dto.StudentDTO;
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
    public ResponseEntity<String> createClass(ClazzDTO classesDTO) {
        if (!classRepository.existsByClassName(classesDTO.getClassName())) {
            Clazz clazz = objectMapper.convertValue(classesDTO, Clazz.class);
            clazz.setIsDelete(false);
            clazz = classRepository.save(clazz);
            if (clazz.getId() > 0) {
                return ResponseEntity.ok("Created successful!");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Created failed!");
        }
        return ResponseEntity.status(400).body("Class " + classesDTO.getClassName() + " was existed!");
    }

    @Override
    public ResponseEntity<ResponseObject> getClassById(Long classId) {
        Clazz clazz = classRepository.findById(classId).orElse(null);
        if (clazz == null || clazz.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not found class with id " + classId, null)
            );
        } else {
            ClazzDTO clazzDTO = objectMapper.convertValue(clazz, ClazzDTO.class);
            List<Student> studentList = studentRepository.findAllByClazzAndAccount_IsDelete(clazz, false);
            List<StudentDTO> studentDTOList = new ArrayList<>();
            if (!studentList.isEmpty()) {
                for (Student student : studentList) {
                    studentDTOList.add(new StudentDTO(student));
                }
                clazzDTO.setStudentList(studentDTOList);
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Found class with id " + classId, clazzDTO));
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getAllClasses(boolean isDelete, String key, Integer page, Integer PAGE_SIZE) {
        List<Clazz> classes = classRepository.findAllByIsDeleteAndClassNameLike(isDelete, "%" + key + "%",
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
    public ResponseEntity<ResponseObject> updateClass(ClazzDTO clazzDTO) {
        if (classRepository.existsByIdAndIsDelete(clazzDTO.getId(), true)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not found " + clazzDTO.getClassName(), null)
            );
        } else {
            if (!classRepository.existsByClassName(clazzDTO.getClassName())) {
                Clazz clazz = objectMapper.convertValue(clazzDTO, Clazz.class);
                clazz.setIsDelete(false);
                classRepository.save(clazz);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Update successful!", clazzDTO)
                );
            }
            return ResponseEntity.status(400).body(
                    new ResponseObject("Class \"" + clazzDTO.getClassName() + "\" was existed!", null)
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