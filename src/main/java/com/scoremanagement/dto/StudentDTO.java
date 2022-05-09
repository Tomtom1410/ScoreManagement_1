package com.scoremanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scoremanagement.entities.Account;
import com.scoremanagement.entities.Score;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {
    private String username;
    private String password;
    private Boolean isAdmin;
    private String rollNumber;
    private String fullName;
    private Date dob;
    private Boolean gender;
    private String avatar;
    private ClazzDTO clazz;
    private Account account;
    private List<CourseDTO> courseList;
    private List<Score> scoreList;

}
