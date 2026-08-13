package com.otakuma.migration.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Fournisseur {
    private Long fournisseurID;
    private String code;
    private String titre;
    private String nom;
    private Long categorie1;
    private Long categorie2;
    private Long categorie3;
    private String service;
    private String description;
    private String telephone;
    private String watsapp;
    private String adresse;
    private String site;
    private Boolean hasLivraison;
    private String email;
    private BigDecimal prix;
    private Integer MOQ;
    private Integer maxOQ;
}