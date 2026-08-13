package ma.otaku.data.produit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.business.produits.Variation;
import ma.otaku.data.TableWithCode;
import ma.otaku.data.TableWithQte;


@Entity
@Table(name = "variation")
public class VariationDB implements TableWithCode, TableWithQte {


	@JsonProperty(access = Access.WRITE_ONLY)
	private Long variationID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long produitID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String produitCode;
	
	private String code;
	private String nom;
	private Boolean isActive;
	private String thumbnail;
	private String image;
	
	private Double coutMoyen;
	private Double prixAchatMoyen;
	
	private Long QTE;
	private Long pendingQte;
	private Long lockedQte;
	
	
	public VariationDB() {}



	public VariationDB(Variation variation) 
	{
		nom = variation.getNom();
		isActive = variation.getIsActive();
		thumbnail = variation.getThumbnail();
		image = variation.getImage();
		coutMoyen = variation.getCoutMoyen();
		QTE = 0L;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getVariationID() {
		return variationID;
	}
	public void setVariationID(Long variationID) {
		this.variationID = variationID;
	}
	public Long getProduitID() {
		return produitID;
	}
	public void setProduitID(Long produitID) {
		this.produitID = produitID;
	}

	@Column( insertable = false)
	public Long getPendingQte() {
		return pendingQte;
	}
	public void setPendingQte(Long pendingQte) {
		this.pendingQte = pendingQte;
	}

	public Long getLockedQte() {
		return lockedQte;
	}



	public void setLockedQte(Long lockedQte) {
		this.lockedQte = lockedQte;
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
	public Double getCoutMoyen() {
		return coutMoyen;
	}
	public void setCoutMoyen(Double coutMoyen) {
		this.coutMoyen = coutMoyen;
	}

	@Column(insertable = false , updatable = true)
	public Double getPrixAchatMoyen() {
		return prixAchatMoyen;
	}

	public void setPrixAchatMoyen(Double prixAchatMoyen) {
		this.prixAchatMoyen = prixAchatMoyen;
	}



	@Override
	public Long getQte() {
		return QTE;
	}
	@Override
	public void setQte(Long qTE) {
		QTE = qTE;
	}


	@Transient
	public String getProduitCode() {
		return produitCode;
	}
	public void setProduitCode(String produitCode) {
		this.produitCode = produitCode;
	}
	
	
	

	
	
}
