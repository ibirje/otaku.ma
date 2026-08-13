package ma.otaku.data.client;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.data.TableWithCode;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.Parse;

@Entity
@Table(name="commande")
public class CommandeDB implements TableWithCode{

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long commandeID;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long clientID;
	
	private Long itemCount;
	private String code;
	private String etat;
	private Double prixPieces;
	private Double coutLivraison;
	private Double prixLivraison;
	
	private Boolean paye;
	private String notes;

	/* ****** dates ****** */
	@JsonProperty(access = Access.WRITE_ONLY)
	private Timestamp dateCommande;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Timestamp dateAccepte;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Timestamp datePrepare;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Timestamp dateEnvoi;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Timestamp dateFin;
	
	/* ****** adresse ****** */ 
	private String nom;
	private String prenom;
	private String adresse1;
	private String adresse2;
	private String telephone1;
	private String livraison;
	private String codePostal;
	private String ville;
	/* ***** transient text dates ****** */

	private String textDateCommande;
	private String textDateAccepte;
	private String textDatePrepare;
	private String textDateEnvoye;
	private String textDateFin;

	private String clientnom;

	/* *************** differents etats d'une commande ****************** */
	@Transient public static final String ACCEPTATION = "ACCEPTATION";
	@Transient public static final String PREPARATION = "PREPARATION";
	@Transient public static final String 		ENVOI = "ENVOI";
	@Transient public static final String 	  ENVOYEE = "ENVOYEE";
	@Transient public static final String 	 COMPLETE = "COMPLETE";
	@Transient public static final String 	  ANNULEE = "ANNULEE";
	@Transient public static final String 	  REFUSEE = "REFUSEE";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCommandeID() {
		return commandeID;
	}
	public void setCommandeID(Long commandeID) {
		this.commandeID = commandeID;
	}
	@Column(updatable = false, insertable = true)
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	
	@Column( insertable = true, updatable = false)
	public Long getItemCount() {
		return itemCount;
	}
	public void setItemCount(Long itemCount) {
		this.itemCount = itemCount;
	}

	@Column(updatable = false, insertable = true)
	public Double getPrixPieces() {
		return prixPieces;
	}
	public void setPrixPieces(Double prixPieces) {
		this.prixPieces = prixPieces;
	}

	@Column(updatable = false, insertable = true)
	public Double getPrixLivraison() {
		return prixLivraison;
	}
	public void setPrixLivraison(Double prixLivraison) {
		this.prixLivraison = prixLivraison;
	}
	
	@Column( insertable = true, updatable = false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Column( insertable = false, updatable = true)
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
	@Column(insertable = false)
	public Boolean getPaye() {
		return paye;
	}
	public void setPaye(Boolean paye) {
		this.paye = paye;
	}
	@Column(insertable = false)
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	@Column(updatable = false, insertable = true)
	public Timestamp getDateCommande() {
		return dateCommande;
	}
	public void setDateCommande(Timestamp dateCommande) {
		this.dateCommande = dateCommande;
	}

	@Column(updatable = true, insertable = false)
	public Timestamp getDateAccepte() {
		return dateAccepte;
	}
	public void setDateAccepte(Timestamp dateAccepte) {
		this.dateAccepte = dateAccepte;
	}

	@Column(updatable = true, insertable = false)
	public Timestamp getDatePrepare() {
		return datePrepare;
	}
	public void setDatePrepare(Timestamp datePrepare) {
		this.datePrepare = datePrepare;
	}
	
	@Column(updatable = true, insertable = false)
	public Timestamp getDateEnvoi() {
		return dateEnvoi;
	}
	public void setDateEnvoi(Timestamp dateEnvoye) {
		this.dateEnvoi = dateEnvoye;
	}

	@Column(updatable = true, insertable = false)
	public Timestamp getDateFin() {
		return dateFin;
	}
	public void setDateFin(Timestamp dateFin) {
		this.dateFin = dateFin;
	}

	@Transient
	public String getTextDateCommande() {
		if(textDateCommande == null) textDateCommande = new Parse().dateToString(dateCommande);
		return textDateCommande;
	}
	public void setTextDateCommande(String textDateCommande) {
		this.textDateCommande = textDateCommande;
	}

	@Transient
	public String getTextDateAccepte() {
		if(textDateAccepte == null) textDateAccepte = new Parse().dateToString(dateAccepte);
		return textDateAccepte;
	}
	public void setTextDateAccepte(String textDateAccepte) {
		this.textDateAccepte = textDateAccepte;
	}
	
	@Transient
	public String getTextDatePrepare() {
		if(textDatePrepare == null) textDatePrepare = new Parse().dateToString(datePrepare);
		return textDatePrepare;
	}
	public void setTextDatePrepare(String textDatePrepare) {
		this.textDatePrepare = textDatePrepare;
	}
	
	@Transient
	public String getTextDateEnvoye() {
		if(textDateEnvoye == null) textDateEnvoye = new Parse().dateToString(dateEnvoi);
		return textDateEnvoye;
	}
	public void setTextDateEnvoye(String textDateEnvoye) {
		this.textDateEnvoye = textDateEnvoye;
	}
	
	@Transient
	public String getTextDateFin() {
		if(textDateFin == null) textDateFin = new Parse().dateToString(dateFin);
		return textDateFin;
	}
	public void setTextDateFin(String textDateFin) {
		this.textDateFin = textDateFin;
	}
	@Transient
	public String getClientnom() {
		return clientnom;
	}
	public void setClientnom(String clientnom) {
		this.clientnom = clientnom;
	}
	
	@Column( insertable = true, updatable = false)
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Column( insertable = true, updatable = false)
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	@Column( insertable = true, updatable = false)
	public String getAdresse1() {
		return adresse1;
	}
	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}

	@Column( insertable = true, updatable = false)
	public String getAdresse2() {
		return adresse2;
	}
	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}

	public Double getCoutLivraison() {
		return coutLivraison;
	}
	public void setCoutLivraison(Double coutLivraison) {
		this.coutLivraison = coutLivraison;
	}
	@Column( insertable = true, updatable = false)
	public String getTelephone1() {
		return telephone1;
	}
	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	@Column( insertable = true, updatable = false)
	public String getLivraison() {
		return livraison;
	}
	public void setLivraison(String livraison) {
		this.livraison = livraison;
	}

	@Column( insertable = true, updatable = false)
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	@Column( insertable = true, updatable = false)
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	
	public double chargesLivraison() {
		return coutLivraison == 0 ? 0 : coutLivraison - prixLivraison;
	}
	public Double surplusLivraison(Double prixUnite) {
		return (prixUnite / prixPieces) * (chargesLivraison() + Constantes.FRAIS_EMBALLAGE );
	}
}
