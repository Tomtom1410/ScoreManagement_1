package com.scoremanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "classes")
public class Clazz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String className;
    @Column(name = "is_delete")
    private Boolean isDelete;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clazz")
    private List<Student> studentList;
}
