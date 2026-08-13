package com.otakuma.migration.model;

import lombok.Data;

@Data
public class ClientAddress {
    private Long adresseclientID;
    private Long clientID;
    private String etat;
    private String prenom;
    private String nom;
    private String organisation;
    private String adresse1;
    private String adresse2;
    private String ville;
    private String codePostal;
    private String telephone1;
    private String telephone2;
    private String email;
    private String extra;
    private String notes;
}