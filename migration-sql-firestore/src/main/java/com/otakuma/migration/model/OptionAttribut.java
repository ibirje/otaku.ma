package com.otakuma.migration.model;

import lombok.Data;

@Data
public class OptionAttribut {
    private Long optionAttributID;
    private Long attributID;
    private String code;
    private String nom;
    private String couleur;
    private String description;
    private Boolean isActive;
    private Integer ordre;
}