package ma.otaku.data.stock;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.data.TableWithQte;

@Entity
@Table(name="perte")
public class PerteDB implements TableWithQte{
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long perteID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long produitID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long variationID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Date dateinsertion;
	
	private Long qte;
	private String cause;
	private String etat;
	
	private String textDateInsertion;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getPerteID() {
		return perteID;
	}
	public void setPerteID(Long perteID) {
		this.perteID = perteID;
	}
	public Long getProduitID() {
		return produitID;
	}
	public void setProduitID(Long produitID) {
		this.produitID = produitID;
	}
	public Long getVariationID() {
		return variationID;
	}
	public void setVariationID(Long variationID) {
		this.variationID = variationID;
	}
	public Date getDateinsertion() {
		return dateinsertion;
	}
	public void setDateinsertion(Date dateinsertion) {
		this.dateinsertion = dateinsertion;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
	
	public Long getQte() {
		return qte;
	}
	public void setQte(Long qte) {
		this.qte = qte;
	}
	@Transient
	public String getTextDateInsertion() {
		return textDateInsertion;
	}
	public void setTextDateInsertion(String textDateInsertion) {
		this.textDateInsertion = textDateInsertion;
	}
	
	
}
