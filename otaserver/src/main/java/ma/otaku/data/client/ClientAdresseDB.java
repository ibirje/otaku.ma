package ma.otaku.data.client;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "client_adresse")
public class ClientAdresseDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long adresseclientID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long clientID;
	private String etat;
    private String nom;
    private String prenom;
    private String organisation;
    private String adresse1;
    private String adresse2;
    private String ville;
    private String codePostal;
    private String telephone1;
    private String telephone2;
    private String email;
	@JsonProperty(access = Access.WRITE_ONLY)
    private String extra;
	@JsonProperty(access = Access.WRITE_ONLY)
    private String notes;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAdresseclientID() {
		return adresseclientID;
	}
	public void setAdresseclientID(Long adresseclientID) {
		this.adresseclientID = adresseclientID;
	}
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
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
	public String getOrganisation() {
		return organisation;
	}
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	public String getAdresse1() {
		return adresse1;
	}
	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}
	public String getAdresse2() {
		return adresse2;
	}
	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
}
