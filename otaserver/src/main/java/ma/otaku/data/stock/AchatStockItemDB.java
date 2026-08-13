package ma.otaku.data.stock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.data.TableWithCode;
import ma.otaku.data.TableWithQte;

@Entity
@Table(name = "achatstockitem")
public class AchatStockItemDB implements TableWithCode,TableWithQte{

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long achatStockItemID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long achatStockID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long variationID;
	
	private String code;
	private Long qte;
	private Double prixUnite;
	/** coutTotal = prixTotal + (1 - (as.prixTotal - prixTotal) / as.prixTotal) * as.fraissup */
	private Double coutTotal;
	private Boolean associe;
	private String description;
	
	// ****** transient ******
	private String achatStockCode;
	private String varcode;
	private String image;
	// *********************************



	public Long getAchatStockID() {
		return achatStockID;
	}

	public void setAchatStockID(Long achatStockID) {
		this.achatStockID = achatStockID;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAchatStockItemID() {
		return achatStockItemID;
	}
	
	public void setAchatStockItemID(Long achatStockItemID) {
		this.achatStockItemID = achatStockItemID;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public Long getQte() {
		return qte;
	}
	
	@Override
	public void setQte( Long qte) {
		this.qte = qte;
	}
	
	public Double getPrixUnite() {
		return prixUnite;
	}
	
	public void setPrixUnite(Double prixUnite) {
		this.prixUnite = prixUnite;
	}

	/** coutTotal = prixTotal + (1 - (as.prixTotal - prixTotal) / as.prixTotal) * as.fraissup */
	public Double getCoutTotal() {
		return coutTotal;
	}

	public void setCoutTotal(Double coutTotal) {
		this.coutTotal = coutTotal;
	}

	@Column(insertable = false, updatable = true)
	public Boolean getAssocie() {
		return associe == null ? false : associe;
	}

	public void setAssocie(Boolean associe) {
		this.associe = associe;
	}
	public Long getVariationID() {
		return variationID;
	}

	public void setVariationID(Long variationID) {
		this.variationID = variationID;
	}

	@Transient
	public String getVarcode() {
		return varcode;
	}
	
	public void setVarcode(String varcode) {
		this.varcode = varcode;
	}
	
	@Transient
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	@Transient
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public String getAchatStockCode() {
		return achatStockCode;
	}

	public void setAchatStockCode(String achatStockCode) {
		this.achatStockCode = achatStockCode;
	}

	public Double coutUnite() {
		return getCoutTotal() / getQte();
	}

}
