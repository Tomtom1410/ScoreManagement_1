package com.scoremanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scoremanagement.entities.Score;
import com.scoremanagement.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    @NotBlank(message = "Username is not empty!")
    @NotNull(message = "Username is mandatory!")
    private String username;
    private String password;
    private String rollNumber;
    @NotBlank(message = "Full name is not empty!")
    @NotNull(message = "Full name is mandatory!")
    private String fullName;
    @NotNull(message = "Date of birth is mandatory!")
    private Date dob;
    @NotNull(message = "Gender is mandatory!")
    private Boolean gender;
    private String avatar;
    private ClazzDTO clazz;
    private List<CourseDTO> courseList;
    private List<Score> scoreList;

    public StudentDTO(Student student){
        this.rollNumber = student.getRollNumber();
        this.fullName = student.getFullName();
        this.dob = student.getDob();
        this.gender = student.getGender();
    }
}
