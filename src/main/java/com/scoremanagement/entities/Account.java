package com.scoremanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "is_admin")
    private Boolean isAdmin;
    @Column(name = "is_delete")
    private Boolean isDelete;
}
