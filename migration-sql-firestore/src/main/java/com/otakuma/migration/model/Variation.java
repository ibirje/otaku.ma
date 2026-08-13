package com.otakuma.migration.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
public class Variation {
    private Long variationID;
    private Long produitID;
    private String code;
    private String nom;
    private String description;
    private BigDecimal prixUnite;
    private BigDecimal prixPromo;
    private Integer qte;
    private Integer pendingQte;
    private Integer lockedQte;
    private String thumbnail;
    private String image1;
    private String image2;
    private String image3;
    private Boolean isActive;
    private String extra1;
    private String extra2;
    private String extra3;
}