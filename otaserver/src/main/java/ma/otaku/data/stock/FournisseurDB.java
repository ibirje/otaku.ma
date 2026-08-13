package ma.otaku.data.stock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.data.TableWithCode;

@Entity
@Table(name = "fournisseur")
public class FournisseurDB implements TableWithCode
{
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long   fournisseurID;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long   categorie1;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long   categorie2;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long   categorie3;
	
	private String code;
	private String titre;
	private String nom;
	private String service;
	private String description;
	private String telephone;
	private String watsapp;
	private String adresse;
	private String site;
	private Boolean hasLivraison;
	private String email;
	private Double prix;
	private Long   MOQ;
	private Long   maxOQ;

	/* ******* transient ******* */
	private String categorie1Code;
	private String categorie2Code;
	private String categorie3Code;
	private String source; //maroc/ax
	
	
	public FournisseurDB() {}
	
	public FournisseurDB(Long fournisseurID, String code, String titre, String nom, Long categorie1, Long categorie2,
			Long categorie3, String service, String description, String telephone, String watsapp, String adresse,
			String site, Boolean hasLivraison, String email, Double prix, Long mOQ, Long maxOQ) 
	{
		this.fournisseurID = fournisseurID;
		this.code = code;
		this.titre = titre;
		this.nom = nom;
		this.categorie1 = categorie1;
		this.categorie2 = categorie2;
		this.categorie3 = categorie3;
		this.service = service;
		this.description = description;
		this.telephone = telephone;
		this.watsapp = watsapp;
		this.adresse = adresse;
		this.site = site;
		this.hasLivraison = hasLivraison;
		this.email = email;
		this.prix = prix;
		MOQ = mOQ;
		this.maxOQ = maxOQ;
	}
	
	
	@Transient
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Transient
	public String getCategorie1Code() {
		return categorie1Code;
	}

	public void setCategorie1Code(String categorie1Code) {
		this.categorie1Code = categorie1Code;
	}

	@Transient
	public String getCategorie2Code() {
		return categorie2Code;
	}

	public void setCategorie2Code(String categorie2Code) {
		this.categorie2Code = categorie2Code;
	}

	@Transient
	public String getCategorie3Code() {
		return categorie3Code;
	}

	public void setCategorie3Code(String categorie3Code) {
		this.categorie3Code = categorie3Code;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getFournisseurID() {
		return fournisseurID;
	}
	public void setFournisseurID(Long fournisseurID) {
		this.fournisseurID = fournisseurID;
	}
	@Override
	public String getCode() {
		return code;
	}
	@Override
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Long getCategorie1() {
		return categorie1;
	}
	public void setCategorie1(Long categorie1) {
		this.categorie1 = categorie1;
	}
	public Long getCategorie2() {
		return categorie2;
	}
	public void setCategorie2(Long categorie2) {
		this.categorie2 = categorie2;
	}
	public Long getCategorie3() {
		return categorie3;
	}
	public void setCategorie3(Long categorie3) {
		this.categorie3 = categorie3;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getWatsapp() {
		return watsapp;
	}
	public void setWatsapp(String watsapp) {
		this.watsapp = watsapp;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public Boolean getHasLivraison() {
		return hasLivraison;
	}
	public void setHasLivraison(Boolean hasLivraison) {
		this.hasLivraison = hasLivraison;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Double getPrix() {
		return prix;
	}
	public void setPrix(Double prix) {
		this.prix = prix;
	}
	public Long getMOQ() {
		return MOQ;
	}
	public void setMOQ(Long mOQ) {
		MOQ = mOQ;
	}

	public Long getMaxOQ() {
		return maxOQ;
	}

	public void setMaxOQ(Long maxOQ) {
		this.maxOQ = maxOQ;
	}
}
