package com.otakuma.migration.model;

import lombok.Data;
import lombok.ToString;

@Data
public class AdminRole {
    private Long adminRoleID;
    private String code;
    private String role;
}