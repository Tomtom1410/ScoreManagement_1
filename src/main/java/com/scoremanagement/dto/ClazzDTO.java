package com.scoremanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scoremanagement.entities.Student;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClazzDTO {
    private Long id;
    @NotBlank(message = "Class name is not empty!")
    @NotNull(message = "Class name is mandatory!")
    private String className;
    private List<StudentDTO> studentList;
}
