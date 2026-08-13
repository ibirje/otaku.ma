package com.otakuma.migration.model;

import lombok.Data;
import lombok.ToString;

@Data
public class Admin {
    private Long adminID;
    private Long adminRoleID;
    private String nom;
    private String prenom;
    private String email;
    private String CIN;
}