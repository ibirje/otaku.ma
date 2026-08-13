package com.otakuma.migration.model;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class Attribut {
    private Long attributID;
    private Long produitID;
    private String code;
    private String nom;
    private String type;
    private String description;
    
    // For NoSQL structure - denormalized options
    private List<OptionAttribut> options = new ArrayList<>();
}