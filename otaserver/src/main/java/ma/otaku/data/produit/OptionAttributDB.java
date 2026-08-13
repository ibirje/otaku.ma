package ma.otaku.data.produit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.business.produits.OptionAttribut;
import ma.otaku.utils.Utils;
import ma.otaku.utils.Validateur;

@Entity
@Table(name = "optionattribut")
public class OptionAttributDB {
	

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long optionAttributID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long AttributID;
	
	private String code;
	private String nom;

	private String couleur;
	private String Description;

	private Boolean isActive;
	private int ordre;
	
	

	public OptionAttributDB() {
	}
	public OptionAttributDB(OptionAttribut opt) {
		
		this.code = opt.getCode();
		this.nom = opt.getNom();
		this.couleur = opt.getCouleur();
		Description = opt.getDescription();
		this.isActive = opt.getIsActive();
		this.ordre = opt.getOrdre();
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getOptionAttributID() {
		return optionAttributID;
	}
	public void setOptionAttributID(Long optionAttributID) {
		this.optionAttributID = optionAttributID;
	}
	public Long getAttributID() {
		return AttributID;
	}
	public void setAttributID(Long attributID) {
		AttributID = attributID;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getCouleur() {
		return couleur;
	}
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public int getOrdre() {
		return ordre;
	}
	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}
	
	@Override
	public boolean equals(Object obj) {
		OptionAttributDB option = (OptionAttributDB)obj;
		Validateur vlad = new Validateur();
		if(!vlad.isEqual(code, option.getCode()) || 
				!vlad.isEqual(nom, option.getNom()) || 
				!vlad.isEqual(couleur, option.getCouleur()))
			return false;
		return true;
	}
}
