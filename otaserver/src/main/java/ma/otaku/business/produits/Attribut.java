package ma.otaku.business.produits;

import ma.otaku.data.produit.AttributDB;
import ma.otaku.utils.Validateur;

public class Attribut {

	private String nom;
	private String code;
	private String type;
	private String description;
	
	
	
	public Attribut() {}
	public Attribut(String nom, String code, String type, String description) {
		this.nom = nom;
		this.code = code;
		this.type = type;
		this.description = description;
	}
	public Attribut(AttributDB attribut) {
		this(attribut.getNom() , attribut.getCode(), attribut.getType(), attribut.getDescription());
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
		Attribut attribut = (Attribut)obj;
		Validateur vlad = new Validateur();
		if(!vlad.isEqual(code, attribut.getCode()) || 
				!vlad.isEqual(nom, attribut.getNom()))
			return false;
		return true;
	}
	
	
	
}



