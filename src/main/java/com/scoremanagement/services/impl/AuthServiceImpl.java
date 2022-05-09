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

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;


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
    public ResponseEntity<String> createAccount(StudentDTO studentDTO) {
        if (accountRepository.existsByUsername(studentDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username existed!");
        } else {
            String passwordEncoding = helper.hashPassword(studentDTO.getPassword());
            Account account = new Account();
            if (studentDTO.getIsAdmin() == null) {
                account.setIsAdmin(false);
            }
            account.setIsAdmin(studentDTO.getIsAdmin());
            account.setUsername(studentDTO.getUsername());
            account.setPassword(passwordEncoding);
            accountRepository.save(account);
            if (!account.getIsAdmin()) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Student student = new Student();
//                student.setUsername(studentDTO.getUsername());
//                student.setRollNumber(helper.generateRollNumber());
//                student.setClazz(student.getClazz());
//                student.setFullName(studentDTO.getFullName());
////                String dob = sdf.format(studentDTO.getDob());
////                student.setDob(Date.valueOf(dob));
                Student student = objectMapper.convertValue(studentDTO, Student.class);
                studentRepository.save(student);
            }
            return ResponseEntity.ok("Create account successful!");
        }
    }


    @Override
    public ResponseEntity<ResponseObject> update(StudentDTO studentDTO) {
        if (studentRepository.existsById(studentDTO.getUsername())) {
            Student student = objectMapper.convertValue(studentDTO, Student.class);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Change information successful!",
                            objectMapper.convertValue(studentRepository.save(student), StudentDTO.class))
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Not found student with username: " + studentDTO.getUsername(),
                        null)
        );
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
            account.setIsAdmin(accountDTO.getIsAdmin());
            account.setIsDelete(false);
            accountRepository.save(account);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Change password successful!", null)
            );
        }
    }

    @Override
    public ResponseEntity<String> deleteAccount(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found account with username: " + username);
        }
        account.setIsDelete(true);
        accountRepository.save(account);
        return ResponseEntity.ok("Delete successful!");
    }
}
