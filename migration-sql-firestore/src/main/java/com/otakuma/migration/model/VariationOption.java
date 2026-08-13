package com.otakuma.migration.model;

import lombok.Data;

@Data
public class VariationOption {
    private Long variationOptionID;
    private Long variationID;
    private Long optionAttributID;
    
    // For NoSQL structure - denormalized relations
    private OptionAttribut optionAttribut;
}