package ma.otaku.data.client;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.data.TableWithQte;

@Entity
@Table(name="commandeitem")
public class CommandeItemDB implements TableWithQte{

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long commandeitemID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long commandeID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long variationID;
	private String code;
	private String nom;
	private String thumbnail;
	private Double prixUnite;
	private Long qte;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCommandeitemID() {
		return commandeitemID;
	}
	public void setCommandeitemID(Long commandeitemID) {
		this.commandeitemID = commandeitemID;
	}
	public Long getCommandeID() {
		return commandeID;
	}
	public void setCommandeID(Long commandeID) {
		this.commandeID = commandeID;
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
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Double getPrixUnite() {
		return prixUnite;
	}
	public void setPrixUnite(Double prixUnite) {
		this.prixUnite = prixUnite;
	}
	public Long getQte() {
		return qte;
	}
	public void setQte(Long qte) {
		this.qte = qte;
	}
	public Long getVariationID() {
		return variationID;
	}
	public void setVariationID(Long variationID) {
		this.variationID = variationID;
	}
}
