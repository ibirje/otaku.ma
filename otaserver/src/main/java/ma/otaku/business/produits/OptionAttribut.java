package ma.otaku.business.produits;

import ma.otaku.data.produit.OptionAttributDB;
import ma.otaku.utils.Utils;
import ma.otaku.utils.Validateur;

/**
 * @author Ismael
 *
 */
public class OptionAttribut {

	//private Long optionAttributID;
	//private Long AttributID;
	
	private String code;
	private String nom;

	private String couleur;
	private String Description;

	private Boolean isActive;
	private int ordre;
	
	public OptionAttribut() {}
	
	public OptionAttribut(String code, String nom, String couleur, String description, Boolean isActive, int ordre) {
		super();
		this.code = code;
		this.nom = nom;
		this.couleur = couleur;
		Description = description;
		this.isActive = isActive;
		this.ordre = ordre;
	}

	public OptionAttribut(OptionAttributDB odb) {
		this(odb.getCode(), odb.getNom(), odb.getCouleur(), odb.getDescription(), odb.getIsActive(), odb.getOrdre());
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
	public String toString() {
		return "OptionAttribut [code=" + code + ", nom=" + nom + ", couleur=" + couleur + ", Description=" + Description
				+ ", isActive=" + isActive + ", ordre=" + ordre + "]";
	}
	
	public void Affiche()
	{
		System.out.println(toString());
	}

	@Override
	public boolean equals(Object obj) {
		OptionAttribut option = (OptionAttribut)obj;
		Validateur vlad = new Validateur();
		if(!vlad.isEqual(code, option.getCode()) || 
				!vlad.isEqual(nom, option.getNom()) || 
				!vlad.isEqual(couleur, option.getCouleur()))
			return false;
		return true;
	}
	
	/*(~A*B)+(~B*A)
	 *
	 * 
	 */
}






