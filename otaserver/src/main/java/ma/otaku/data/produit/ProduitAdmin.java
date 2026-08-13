package ma.otaku.data.produit;

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

import ma.otaku.utils.Parse;
import ma.otaku.utils.Utils;

@Entity
@Table(name = "produit")
public class ProduitAdmin extends ProduitDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	protected long produitID;
	@JsonProperty(access = Access.WRITE_ONLY)
	protected Long categorieID;
	@JsonProperty(access = Access.WRITE_ONLY)
	protected Long themeID;
	
	protected String code;
	protected String nom;
	protected String keywords;
	protected String shortDescription;
	protected String description;
	protected Boolean isActive;
	
	/* images */
	protected String thumbnail;
	protected String image1;
	protected String image2;
	protected String image3;
	protected String extra1;
	protected String extra2;
	protected String extra3;
	
	/* prix */
	protected Double prixUnite;
	protected Double prixPromo;

	@JsonProperty(access = Access.WRITE_ONLY)
	protected Date dateDebutPromo;
	@JsonProperty(access = Access.WRITE_ONLY)
	protected Date dateFinPromo;
	
	protected String stringDateDebutPromo;
	protected String stringDateFinPromo;

	
	protected String categorie;
	protected String theme;
	
	protected Boolean hasVariations;
	
	protected Long QTE;
	protected Long pendingQte;
	protected Long lockedQte;

	protected Integer stars;
	protected Integer avis;
	protected Integer commandes;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getProduitID() {
		return produitID;
	}

	@Column(updatable = true, insertable = false)
	@Override
	public Long getQte() {
		return QTE;
	}
	
	@Override
	public void setQte(Long qTE	) {
		QTE = qTE;
	}
	@Column(updatable = true, insertable = false)
	public Long getLockedQte() {
		return lockedQte;
	}


	public void setLockedQte(Long lockedQte) {
		this.lockedQte = lockedQte;
	}


	@Transient
	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	@Transient
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Column( insertable = false)
	public Long getPendingQte() {
		return pendingQte;
	}


	public void setPendingQte(Long pendingQte) {
		this.pendingQte = pendingQte;
	}
	@Transient
	public String getStringDateDebutPromo() {
		return stringDateDebutPromo;
	}

	public void setStringDateDebutPromo(String stringDateDebutPromo) 
	{
		this.stringDateDebutPromo = stringDateDebutPromo;
		
		if(dateDebutPromo == null && stringDateDebutPromo != null)
			setDateDebutPromo(new Parse().ParseDate(stringDateDebutPromo) );
	}

	@Transient
	public String getStringDateFinPromo() {
		return stringDateFinPromo;
	}

	public void setStringDateFinPromo(String stringDateFinPromo) 
	{
		this.stringDateFinPromo = stringDateFinPromo;

		if(dateFinPromo == null && stringDateFinPromo != null)
			setDateFinPromo(new Parse().ParseDate(stringDateFinPromo) );
	}
	
	public Date getDateDebutPromo() {
		return dateDebutPromo;
	}

	public void setDateDebutPromo(Date dateDebutPromo) {
		this.dateDebutPromo = dateDebutPromo;
		if(dateDebutPromo != null)
			stringDateDebutPromo = new Parse().dateToString(dateDebutPromo);
	}
	
	
	public int Reduc()
	{
		return (int)((1-getPrixPromo()/getPrixUnite())*100);
	}
	
	public Date getDateFinPromo() {
		return dateFinPromo;
	}

	public void setDateFinPromo(Date dateFinPromo) {
		this.dateFinPromo = dateFinPromo;
		if(dateFinPromo != null)
			stringDateFinPromo = new Parse().dateToString(dateFinPromo);
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

	public String getDescription() {
		if(description == null) return null;
		return description.replace("\r\n", "<br/>");
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

	public Long getCategorieID() {
		return categorieID;
	}

	public void setCategorieID(Long categorieID) {
		this.categorieID = categorieID;
	}

	public Long getThemeID() {
		return themeID;
	}

	public void setThemeID(Long themeID) {
		this.themeID = themeID;
	}

	public void setProduitID(long produitID) {
		this.produitID = produitID;
	}
	
	public Boolean getHasVariations() {
		return hasVariations;
	}

	public void setHasVariations(Boolean hasVariations) {
		this.hasVariations = hasVariations;
	}

	public long countdown()
	{
		return new Utils().calculCountdown(dateFinPromo);
	}

	public boolean enpromo()
	{
		if(dateDebutPromo == null) return false;
		long secs = new Utils().calculCountdown(dateDebutPromo);
		System.out.println(dateDebutPromo + " " + secs);
		if(secs > 0 ) return false;
		return countdown() > 0;
	}
	
}
