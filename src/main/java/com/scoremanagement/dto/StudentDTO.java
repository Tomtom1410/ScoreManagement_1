package com.scoremanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scoremanagement.entities.Score;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {
    @NotBlank(message = "Username is not empty!")
    @NotNull(message = "Username is mandatory!")
    private String username;
    @NotBlank(message = "Password is not empty!")
    @NotNull(message = "Password is mandatory!")
    private String password;
    private String rollNumber;
    @NotBlank(message = "Full name is not empty!")
    @NotNull(message = "Full name is mandatory!")
    private String fullName;
    private Date dob;
    private Boolean gender;
    private String avatar;
    private ClazzDTO clazz;
    private List<CourseDTO> courseList;
    private List<Score> scoreList;

}
