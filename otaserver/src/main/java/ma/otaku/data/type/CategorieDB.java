package ma.otaku.data.type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.utils.Build;

@Entity
@Table(name="categorie")
public class CategorieDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private long categorieID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long categorieParent;
	
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
	
	private Long nombreProduits;

	private Long qte; 
	private Long pendingQte;
	private Long activeQte;

	/* @Transient */
	private String categorieParentCode;
	private String link;
	/* @Transient */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getCategorieID() {
		return categorieID;
	}
	public void setCategorieID(long categorieID) {
		this.categorieID = categorieID;
	}
	
	@Transient
	public String getCategorieParentCode() {
		return categorieParentCode;
	}
	public void setCategorieParentCode(String categorieParentCode) {
		this.categorieParentCode = categorieParentCode;
	}
	@Column(insertable = false, updatable = true)
	public Long getNombreProduits() {
		return nombreProduits;
	}
	public void setNombreProduits(Long nombreProduits) {
		this.nombreProduits = nombreProduits;
	}
	@Column( insertable = false, updatable = true)
	public Long getQte() {
		return qte;
	}
	public void setQte(Long qte) {
		this.qte = qte;
	}
	@Column( insertable = false, updatable = true)
	public Long getPendingQte() {
		return pendingQte;
	}
	public void setPendingQte(Long pendingQte) {
		this.pendingQte = pendingQte;
	}
	@Column( insertable = false, updatable = true)
	public Long getActiveQte() {
		return activeQte;
	}
	public void setActiveQte(Long activeQte) {
		this.activeQte = activeQte;
	}
	@Column(insertable = true, updatable = false)
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
		setLink(Build.BASE_URL+"categories/"+getNom().replace(" ", "-"));
	}
	@Column(insertable = true, updatable = false)
	public Long getCategorieParent() {
		return categorieParent;
	}
	public void setCategorieParent(Long categorieParent) {
		this.categorieParent = categorieParent;
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
	
	@Override
	public String toString() {
		return "Categorie : "+getNom()+"\ncode : "+getCode();
	}

	@Override
	public boolean equals(Object obj) {
		CategorieDB cbt = (CategorieDB) obj;
		return code.equals(cbt.getCode());
	}

	@Transient
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
}
