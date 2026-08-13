package ma.otaku.data.client;

import java.security.Principal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.utils.Validateur;

@Entity
@Table(name="client")
public class ClientDB implements Principal{

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long clientID;
	
	private String nom;
	private String prenom;
	private String email;
	private String pseudo;
	private String telephone1;
	private String telephone2;
	private Date dateNaissance;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String notes; // commentaires sur le client
	private Boolean isActive;
	private String etat;
	private Date dateCreation;
	
	private Long panierCount;
	private String token;

	private Boolean fbassocie;
	
	public ClientDB() {
		// TODO Auto-generated constructor stub
	}
	
	
	public ClientDB(String email,String token) {
		super();
		this.nom    	 = "";
		this.prenom 	 = "";
		this.email 		 = email;
		this.pseudo 	 = "";
		this.telephone1  = "";
		this.telephone2  = "";
		this.dateNaissance = null;
		this.notes 		 = "";
		this.isActive    = true;
		this.etat 		 = "NORMAL";
		this.panierCount = 0l;
		this.token 		 = token;
	}

	public ClientDB(String email, String prenomnom,String token) {
		this(email, token);
		if(prenomnom != null)
		{
			String[] sp = prenomnom.split(" ");
			this.prenom	= sp[0];
			this.nom	= sp[1];
			Validateur v = new Validateur();
			if(!v.isNomValide(nom)) nom = "nom";
			if(!v.isNomValide(prenom)) prenom = "prenom";
		}
	}
	public ClientDB(String email, String nom, String prenom, String token) {
		this(email, token);
		if(nom != null) this.nom = nom;
		if(prenom != null) this.prenom = prenom;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getClientID() {
		return clientID;
	}

	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(insertable = false, updatable = true)
	public Long getPanierCount() {
		return panierCount;
	}

	public void setPanierCount(Long panierCount) {
		this.panierCount = panierCount;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column(insertable = false, updatable = false)
	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	@Column(insertable = false, updatable = true)
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	@Transient
	public Boolean getFbassocie() {
		return fbassocie;
	}


	public void setFbassocie(Boolean fbassocie) {
		this.fbassocie = fbassocie;
	}


	@Column(insertable = false, updatable = true)
	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}
	
	public String getPseudo() {
		return pseudo;
	}


	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	@Transient
	@Override
	public String getName() {
		return nom + " " + prenom;
	}
	
	
}
