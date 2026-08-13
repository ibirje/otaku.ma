package com.otakuma.migration.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CommandeItem {
    private Long commandeitemID;
    private Long commandeID;
    private Long variationID;
    private String code;
    private String nom;
    private String thumbnail;
    private BigDecimal prixUnite;
    private Integer qte;
}