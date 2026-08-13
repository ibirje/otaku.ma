package ma.otaku.business.produits;

import ma.otaku.data.produit.VariationDB;

public class Variation {

	private String code;
	private String nom;
	private Boolean isActive;
	private String thumbnail;
	private String image;
	private Double prixUnite;
	private Double prixPromo;
	private Double coutMoyen;
	private Long qte;
	
	
	
	public Variation() {}
	
	public Variation(VariationDB vardb) 
	{
		this.code = vardb.getCode();
		this.nom = vardb.getNom();
		this.isActive = vardb.getIsActive();
		this.thumbnail = vardb.getThumbnail();
		this.image = vardb.getImage();
		this.coutMoyen = vardb.getCoutMoyen();
		this.qte = vardb.getQte();
	}
	
	public Double getPrixPromo() {
		return prixPromo;
	}
	
	public void setPrixPromo(Double prixPromo) {
		this.prixPromo = prixPromo;
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
	
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}
	
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public Double getPrixUnite() {
		return prixUnite;
	}
	
	public void setPrixUnite(Double prixUnite) {
		this.prixUnite = prixUnite;
	}
	
	public Double getCoutMoyen() {
		return coutMoyen;
	}
	
	public void setCoutMoyen(Double coutMoyen) {
		this.coutMoyen = coutMoyen;
	}
	
	public Long getQTE() {
		return qte;
	}
	
	public void setQte(Long qTE) {
		qte = qTE;
	}
	
	
	
}
