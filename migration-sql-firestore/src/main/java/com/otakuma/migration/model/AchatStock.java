package com.otakuma.migration.model;

import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Data
public class AchatStock {
    private Long achatStockID;
    private String code;
    private Long fournisseurID;
    private Date dateInsertion;
    private String type;
    private Date dateCommande;
    private Date dateLivraison;
    private Integer qte;
    private BigDecimal prixTotal;
    private BigDecimal fraisSupplementaires;
    private String description;
    private Long authAdminID;
    private Boolean associe;
    
    // For NoSQL structure - denormalized items
    private List<AchatStockItem> items = new ArrayList<>();
}