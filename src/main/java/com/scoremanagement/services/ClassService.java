package com.scoremanagement.services;

import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.dto.ClazzDTO;
import org.springframework.http.ResponseEntity;


public interface ClassService {
    ResponseEntity<String> createClass(ClazzDTO classesDTO);

    public ResponseEntity<ResponseObject> getClassById(Long classId);

    ResponseEntity<ResponseObject> getAllClasses(boolean isDelete,String key, Integer page, Integer PAGE_SIZE);

    ResponseEntity<String> deleteById(Long id);

    ResponseEntity<ResponseObject> updateClass(ClazzDTO clazz);

    ResponseEntity<String> restoreClass(Long[] classIdList);
}
