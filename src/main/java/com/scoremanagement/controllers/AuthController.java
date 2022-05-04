package com.scoremanagement.controllers;

import com.scoremanagement.config.JwtTokenUtil;
import com.scoremanagement.dto.AccountCustomDTO;
import com.scoremanagement.dto.AccountDTO;
import com.scoremanagement.dto.StudentDTO;
import com.scoremanagement.response.JwtResponse;
import com.scoremanagement.response.ResponseObject;
import com.scoremanagement.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenUtil jwtToken;
    private final String REGEX_FULL_NAME = "^[a-zA-ZaAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴ\\s]+$";

    //    private final String REGEX_USERNAME = "";
    @PostMapping("login")
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody AccountDTO account) {
        if (authService.checkLogin(account)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Login successful!",
                            new JwtResponse(jwtToken.generateToken(account.getUsername())))
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("Username or password is invalid!", null)
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("create-account")
    public String createAccount(@Valid @RequestBody StudentDTO student) {
        if (!Pattern.matches(REGEX_FULL_NAME, student.getFullName())) {
            return "Full name is invalid! It must be letter!";
        }

        return authService.createAccount(student);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("delete/{username}")
    public ResponseEntity<String> deleteAccount(@PathVariable String username){
        return authService.deleteAccount(username);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #studentDTO.username == authentication.principal.username")
    @PutMapping("update")
    public ResponseEntity<ResponseObject> updateStudent(@Valid @RequestBody StudentDTO studentDTO) {
        if (!Pattern.matches(REGEX_FULL_NAME, studentDTO.getFullName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Full name is invalid! It must be letter!", null)
            );
        }
        return authService.update(studentDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #accountCustom.account.username == authentication.principal.username")
    @PutMapping("change-password")
    public ResponseEntity<ResponseObject> changePassword(@RequestBody AccountCustomDTO accountCustom) {
        if (!accountCustom.getNewPassword().equals(accountCustom.getRePassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("New password and re-password are not match!", null)
            );
        }
        return authService.changePassword(accountCustom.getAccount(), accountCustom.getNewPassword());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
