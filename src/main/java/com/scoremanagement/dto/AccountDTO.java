package com.scoremanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    @NotBlank(message = "Username is not empty!")
    @NotNull(message = "Username is mandatory!")
    private String username;
    @NotBlank(message = "Username is not empty!")
    @NotNull(message = "Password is mandatory!")
    private String password;
    private Boolean isAdmin;
    private Boolean isDelete;
}
