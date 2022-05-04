package com.scoremanagement.services;

import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.dto.ClazzDTO;
import org.springframework.http.ResponseEntity;


public interface ClassService {
    ResponseEntity<String> insertClass(ClazzDTO classesDTO);

    public ResponseEntity<ResponseObject> getClassById(Long classId);

    ResponseEntity<ResponseObject> getAllClasses(String key, Integer page, Integer PAGE_SIZE);

    ResponseEntity<String> deleteById(Long id);

    ResponseEntity<ResponseObject> updateClass(ClazzDTO clazz);

}
