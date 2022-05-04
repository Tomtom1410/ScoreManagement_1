package com.scoremanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "roll_number")
    private String rollNumber;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "dob")
    private Date dob;
    @Column(name = "gender")
    private Boolean gender;
    @Column(name = "avatar")
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Clazz clazz;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courseList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    private List<Score> scoreList;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private Account account;

}
