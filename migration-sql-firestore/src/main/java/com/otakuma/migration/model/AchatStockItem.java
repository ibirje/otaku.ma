package com.otakuma.migration.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AchatStockItem {
    private Long achatStockItemID;
    private String code;
    private Long variationID;
    private Long achatStockID;
    private BigDecimal prixUnite;
    private Integer qte;
    private Boolean associe;
    private BigDecimal coutTotal;
}