package ma.otaku.business.produits;
import javax.xml.bind.annotation.XmlRootElement;

import ma.otaku.data.produit.ProduitDB;
import ma.otaku.utils.Parse;
import ma.otaku.utils.Utils;
@XmlRootElement
public class Produit {

	private String code;
	private String nom;
	private String link;
	private String keywords;
	private String shortDescription;
	private String description;
	private Boolean isActive;
	
	/* images */
	private String thumbnail;
	private String image1;
	private String image2;
	private String image3;
	private String extra1;
	private String extra2;
	private String extra3;
	
	/* prix */
	private Double prixUnite;
	private Integer intPrixUnite;
	private Double prixPromo;
	private String dateDebutPromo;
	private String dateFinPromo;
	private Integer intPrixPromo;
	private Integer reduc;

	private Long countdown;
	
	private Integer stars;
	private Integer avis;
	private Integer commandes;
	private Long QTE;
	
	private Boolean ispromo;
	private Boolean hasVariations;
	
	private String categorie;
	private String theme;
	

	
	public Produit() { }

	public Produit(ProduitDB produit) {
		
		this.code = produit.getCode();
		this.nom = produit.getNom();
		this.keywords = produit.getKeywords();
		this.shortDescription = produit.getShortDescription();
		this.description = produit.getDescription();
		this.isActive = produit.getIsActive();
		this.thumbnail = produit.getThumbnail();
		this.image1 = produit.getImage1();
		this.image2 = produit.getImage2();
		this.image3 = produit.getImage3();
		this.prixUnite = produit.getPrixUnite();
		this.prixPromo = produit.getPrixPromo();
		this.extra1 = produit.getExtra1();
		this.extra2 = produit.getExtra2();
		this.extra3 = produit.getExtra3();
		this.QTE = produit.getQte();
		this.hasVariations = produit.getHasVariations();

		Parse parse = new Parse();
		setDateDebutPromo(parse.dateToString(produit.getDateDebutPromo()) );
		setDateFinPromo  (parse.dateToString(produit.getDateFinPromo()) );
		setLink          (parse.NomProduitToSiteURL(nom));
		
		setIntPrixUnite(getPrixUnite().intValue());
		setIntPrixPromo(getPrixPromo().intValue());
		setReduc((int)((1-getPrixPromo()/getPrixUnite())*100));
		setCountdown(new Utils().calculCountdown(produit.getDateFinPromo()));
		setIspromo(countdown > 0);
		
		avis = 0;
		commandes = 0;
		stars = 0;
	}

	
	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
	public Long getQTE() {
		return QTE;
	}
	public void setQTE(Long qTE) {
		QTE = qTE;
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

	public String getShortDescription() {
		if(shortDescription == null)
			return "";
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDateFinPromo() {
		return dateFinPromo;
	}

	public void setDateFinPromo(String dateFinPromo) {
		this.dateFinPromo = dateFinPromo;
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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public Boolean getHasVariations() {
		return hasVariations;
	}

	public void setHasVariations(Boolean hasVariations) {
		this.hasVariations = hasVariations;
	}

	public Double getPrixUnite() {
		return prixUnite;
	}

	public void setPrixUnite(Double prixUnite) {
		this.prixUnite = prixUnite;
	}

	public Double getPrixPromo() {
		return prixPromo;
	}

	public void setPrixPromo(Double prixPromo) {
		this.prixPromo = prixPromo;
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
		return "Prod : "+getCode();
	}
	public Integer getReduc() {
		return reduc;
	}
	public void setReduc(Integer reduc) {
		this.reduc = reduc;
	}

	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public Integer getIntPrixPromo() {
		return intPrixPromo;
	}
	public void setIntPrixPromo(Integer intPrixPromo) {
		this.intPrixPromo = intPrixPromo;
	}
	public Integer getIntPrixUnite() {
		return intPrixUnite;
	}
	public void setIntPrixUnite(Integer intPrixUnite) {
		this.intPrixUnite = intPrixUnite;
	}
	public Long getCountdown() {
		return countdown;
	}
	public void setCountdown(Long countdown) {
		this.countdown = countdown;
	}
	public Integer getStars() {
		return stars;
	}
	public void setStars(Integer stars) {
		this.stars = stars;
	}
	public Integer getAvis() {
		return avis;
	}
	public void setAvis(Integer avis) {
		this.avis = avis;
	}
	public Integer getCommandes() {
		return commandes;
	}
	public void setCommandes(Integer commandes) {
		this.commandes = commandes;
	}
	
	public Boolean getIspromo() {
		return ispromo;
	}
	public void setIspromo(Boolean ispromo) {
		this.ispromo = ispromo;
	}

	public String getDateDebutPromo() {
		return dateDebutPromo;
	}

	public void setDateDebutPromo(String dateDebutPromo) {
		this.dateDebutPromo = dateDebutPromo;
	}
	
}
