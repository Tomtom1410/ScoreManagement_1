package com.scoremanagement.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoremanagement.dto.AccountDTO;
import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.entities.Account;
import com.scoremanagement.entities.CustomUserDetail;
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


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return null;
        }
        return new CustomUserDetail(account);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkLogin(AccountDTO accountDTO) {
        UserDetails user = loadUserByUsername(accountDTO.getUsername());
        String passwordEncoding = hashPassword(accountDTO.getPassword());
        if (user != null && passwordEncoding.equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public String createAccount(StudentDTO studentDTO) {
        if (accountRepository.existsByUsername(studentDTO.getUsername())) {
            return "Username existed!";
        } else {
            String passwordEncoding = hashPassword(studentDTO.getPassword());
            Account account = new Account();
            if (studentDTO.getIsAdmin() == null) {
                account.setIsAdmin(false);
            } else if (studentDTO.getIsAdmin()) {
                account.setIsAdmin(true);
            }
            account.setUsername(studentDTO.getUsername());
            account.setPassword(passwordEncoding);
            accountRepository.save(account);
            Student student = objectMapper.convertValue(studentDTO, Student.class);
            student.setRollNumber(generateRollNumber());
            studentRepository.save(student);
            return "Create account successful!";
        }
    }

    private String generateRollNumber() {
        String lastRoll = studentRepository.getLastRollNumber().substring(2);
        int roll = Integer.parseInt(lastRoll.toString()) + 1;
        String pre = "SV";
        while (pre.length() + Integer.toString(roll).length() < 8) {
            pre += "0";
        }
        return pre + roll;
    }

    @Override
    public ResponseEntity<ResponseObject> update(StudentDTO studentDTO) {
        Student student = objectMapper.convertValue(studentDTO, Student.class);
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Change information successful!",
                        objectMapper.convertValue(studentRepository.findStudentByUsername(student.getUsername()), StudentDTO.class))
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
            account.setPassword(hashPassword(newPassword));
            account.setIsAdmin(accountDTO.getIsAdmin());
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
