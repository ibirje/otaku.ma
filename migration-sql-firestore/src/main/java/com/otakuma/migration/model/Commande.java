package com.otakuma.migration.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Data
public class Commande {
    private Long commandeID;
    private Long clientID;
    private Integer itemCount;
    private String code;
    private String etat;
    private BigDecimal prixPieces;
    private BigDecimal prixLivraison;
    private BigDecimal coutLivraison;
    private Date dateCommande;
    private Date dateAccepte;
    private Date datePrepare;
    private Date dateEnvoi;
    private Date dateFin;
    private Boolean paye;
    private String notes;
    private String prenom;
    private String nom;
    private String adresse1;
    private String adresse2;
    private String ville;
    private String telephone1;
    private String codePostal;
    private String livraison;
    
    // For NoSQL structure - denormalized items
    private List<CommandeItem> items = new ArrayList<>();
}