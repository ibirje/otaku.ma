package ma.otaku.data.produit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.business.produits.Attribut;
import ma.otaku.utils.Utils;
import ma.otaku.utils.Validateur;

@Entity
@Table(name = "attribut")
public class AttributDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long attributID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long produitID;
	
	private String nom;
	private String code;
	private String type ;
	private String description;
	
	
	public AttributDB() {}
	
	public AttributDB(Attribut attribut) 
	{
		this.nom = attribut.getNom();
		this.code = attribut.getCode();
		this.type = attribut.getType();
		this.description = attribut.getDescription();
		if(type == null || type.isEmpty()) type = "particulier";
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAttributID() {
		return attributID;
	}
	public void setAttributID(Long attributID) {
		this.attributID = attributID;
	}
	public Long getProduitID() {
		return produitID;
	}

	public void setProduitID(Long produitID) {
		this.produitID = produitID;
	}

	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		AttributDB attribut = (AttributDB)obj;

		Validateur vlad = new Validateur();
		if(!vlad.isEqual(code, attribut.getCode()) || 
				!vlad.isEqual(nom, attribut.getNom()))
			return false;
		return true;
	}
	
	
	
	/*
	 SELECT T.nom ,O.code, O.couleur, O.nom, O.ordre FROM com.otaku.data.OptionAttributDB O 
	 JOIN com.otaku.data.AttributDB T ON O.attributID = T.attributID 
	 JOIN AttributProduitDB AP ON AP.attributID = T.attributID JOIN com.otaku.data.ProduitDB P ON P.produitID = AP.produitID WHERE P.produitID = :produitid]

	 */
	
	
}
