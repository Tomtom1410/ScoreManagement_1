package com.scoremanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreDTO {
    private Long id;
    private Double score;
    private StudentDTO student;
    private CourseDTO course;
}
