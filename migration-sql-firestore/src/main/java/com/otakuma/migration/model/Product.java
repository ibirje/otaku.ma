package com.otakuma.migration.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Data
public class Product {
    private Long produitID;
    private String code;
    private String nom;
    private String keywords;
    private String description;
    private String shortDescription;
    private BigDecimal prixUnite;
    private BigDecimal prixPromo;
    private String dateDebutPromo;
    private String dateFinPromo;
    private Integer qte;
    private Integer pendingQte;
    private Integer lockedQte;
    private String thumbnail;
    private String image1;
    private String image2;
    private String image3;
    private Integer categorieId;
    private Integer themeId;
    private Boolean isActive;
    private Boolean hasVariations;
    private String extra1;
    private String extra2;
    private String extra3;
    
    // New fields for NoSQL structure
    private List<Variation> variations = new ArrayList<>();
    private Categorie categorie;  // Denormalized category
    private Theme theme;          // Denormalized theme


    @Override
    public String toString() {
        return "Product{" +
                "produitID=" + produitID +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", hasVariations=" + hasVariations +
                '}';
        
    }

}