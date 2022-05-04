package com.scoremanagement.services;

import com.scoremanagement.dto.AccountDTO;
import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.response.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    boolean checkLogin(AccountDTO account);

    String registerAccount(StudentDTO student);

    ResponseEntity<ResponseObject> update(StudentDTO studentDTO);

    ResponseEntity<ResponseObject> changePassword(AccountDTO account, String newPassword);

}
