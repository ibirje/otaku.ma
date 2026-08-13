package com.otakuma.migration.model;

import lombok.Data;
import lombok.ToString;

@Data
public class Theme {
    private Long themeID;
    private String code;
    private String nom;
    private Integer nombreProduits;
    private Integer qte;
    private Integer pendingQte;
    private Integer activeQte;
    private String description;
    private Boolean isActive;
    private String smallImage;
    private String mediumImage;
    private String largeImage;
    private String extra1;
    private String extra2;
    private String extra3;
    private Long themeParent;
}