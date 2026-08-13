package com.otakuma.migration.model;

import lombok.Data;
import lombok.ToString;

@Data
public class Categorie {
    private Long categorieID;
    private String code;
    private String nom;
    private Integer nombreProduits;
    private Integer qte;
    private Integer pendingQte;
    private Integer activeQte;
    private Long categorieParent;
    private String keywords;
    private String description;
    private Boolean isActive;
    private String smallImage;
    private String mediumImage;
    private String largeImage;
    private String extra1;
    private String extra2;
    private String extra3;
}