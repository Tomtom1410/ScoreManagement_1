package com.scoremanagement.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoremanagement.dto.AccountDTO;
import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.entities.Account;
import com.scoremanagement.entities.CustomUserDetail;
import com.scoremanagement.entities.Helper;
import com.scoremanagement.entities.Student;
import com.scoremanagement.repositories.AccountRepository;
import com.scoremanagement.repositories.StudentRepository;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;
    private final Helper helper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null || account.getIsDelete()) {
            return null;
        }
        return new CustomUserDetail(account);
    }


    @Override
    public boolean checkLogin(AccountDTO accountDTO) {
        UserDetails user = loadUserByUsername(accountDTO.getUsername());
        String passwordEncoding = helper.hashPassword(accountDTO.getPassword());
        if (user != null && passwordEncoding.equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<ResponseObject> changePassword(AccountDTO accountDTO, String newPassword) {
        if (!checkLogin(accountDTO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Username or password is invalid!", null)
            );
        } else {
            Account account = new Account();
            account.setUsername(accountDTO.getUsername());
            account.setPassword(helper.hashPassword(newPassword));
            if (accountDTO.getIsAdmin() != null && accountDTO.getIsAdmin()) {
                account.setIsAdmin(true);
            } else {
                account.setIsAdmin(false);
            }
            account.setIsDelete(false);
            accountRepository.save(account);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Change password successful!", null)
            );
        }
    }

    @Override
    public ResponseEntity<ResponseObject> deleteAccount(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Not found account with username: " + username, null));
        }
        account.setIsDelete(true);
        accountRepository.save(account);
        return ResponseEntity.ok(new ResponseObject("Delete successful!", null));
    }
}
