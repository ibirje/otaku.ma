package com.otakuma.migration.model;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class Client {
    private Long clientID;
    private String nom;
    private String prenom;
    private String email;
    private String pseudo;
    private String telephone1;
    private String telephone2;
    private Date dateNaissance;
    private String notes;
    private Boolean isActive;
    private String etat;
    private Integer panierCount;
    private String extra;
    private Date dateCreation;
    private String token;
    private List<ClientAddress> addresses;
}