package com.otakuma.migration.model;

import lombok.Data;
import java.util.Date;

@Data
public class SKU {
    private Long skuID;
    private Long variationID;
    private Long achatStockItemID;
    private String description;
    private Integer qte;
    private Boolean isActive;
    private String code;
    private Date dateInsertion;
    private String motif;
    private Long authAdminID;
}