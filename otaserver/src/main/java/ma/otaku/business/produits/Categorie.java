package ma.otaku.business.produits;

import ma.otaku.data.type.CategorieDB;
import ma.otaku.utils.Build;

public class Categorie {

	private String code;
	private String nom;
	private String keywords;
	private String description;
	private Boolean isActive;
	private String smallImage;
	private String mediumImage;
	private String largeImage;
	private String extra1;
	private String extra2;
	private String extra3;
	
	private String link;

	public Categorie() {
		
	}
	public Categorie(CategorieDB categ) {
		
		this.nom = categ.getNom();
		this.code = categ.getCode();
		this.keywords = categ.getKeywords();
		this.description = categ.getDescription();
		this.isActive = categ.getIsActive();
		this.smallImage = categ.getSmallImage();
		this.mediumImage = categ.getMediumImage();
		this.largeImage = categ.getLargeImage();
		this.extra1 = categ.getExtra1();
		this.extra1 = categ.getExtra2();
		this.extra1 = categ.getExtra3();
		setLink(link());
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
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getSmallImage() {
		return smallImage;
	}
	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}
	public String getMediumImage() {
		return mediumImage;
	}
	public void setMediumImage(String mediumImage) {
		this.mediumImage = mediumImage;
	}
	public String getLargeImage() {
		return largeImage;
	}
	public void setLargeImage(String largeImage) {
		this.largeImage = largeImage;
	}
	public String getExtra1() {
		return extra1;
	}
	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}
	public String getExtra2() {
		return extra2;
	}
	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}
	public String getExtra3() {
		return extra3;
	}
	public void setExtra3(String extra3) {
		this.extra3 = extra3;
	}
	
	public void affiche()
	{
		System.out.println(toString());
	}
	@Override
	public String toString() {
		return "Categorie : "+getNom()+"\ncode : "+getCode();
	}
	
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String link()
	{
		return Build.BASE_URL+"categories/"+getNom().replace(" ", "-");
	}
	
}
