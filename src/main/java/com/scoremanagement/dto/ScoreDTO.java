package com.scoremanagement.dto;

import lombok.Data;

@Data
public class ScoreDTO {
    private Long id;
    private Double score;
    private StudentDTO student;
    private CourseDTO course;
}
