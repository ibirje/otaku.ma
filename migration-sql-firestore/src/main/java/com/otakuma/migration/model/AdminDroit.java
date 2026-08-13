package com.otakuma.migration.model;

import lombok.Data;
import lombok.ToString;

@Data
public class AdminDroit {
    private Long adminDroitID;
    private String code;
    private String droit;
    private String description;
}