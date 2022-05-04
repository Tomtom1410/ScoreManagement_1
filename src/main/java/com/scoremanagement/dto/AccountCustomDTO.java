package com.scoremanagement.dto;

import lombok.Data;

@Data
public class AccountCustomDTO {
    private AccountDTO account;
    private String newPassword;
    private String rePassword;
}
