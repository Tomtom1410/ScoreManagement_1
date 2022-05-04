package com.scoremanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CourseDTO {
    private Long id;
    @NotBlank(message = "Course code is not blank!")
    @NotNull(message = "Course code is not null!")
    private String courseCode;
    @NotBlank(message = "Course name is not blank!")
    @NotNull(message = "Course name is not null!")
    private String courseName;
    private Boolean isDelete;
}
