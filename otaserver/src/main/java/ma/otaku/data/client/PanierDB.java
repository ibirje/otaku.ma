package ma.otaku.data.client;

import javax.persistence.Column;
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
@Table(name="panier")
public class PanierDB implements TableWithQte{

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long panierID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long clientID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long produitID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long variationID;
	private Long qte;
	private String code; // code du produit envoyé par le client 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getPanierID() {
		return panierID;
	}
	public void setPanierID(Long panierID) {
		this.panierID = panierID;
	}
	@Column( insertable = true, updatable = false)
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	@Column( insertable = true, updatable = false)
	public Long getProduitID() {
		return produitID;
	}
	public void setProduitID(Long produitID) {
		this.produitID = produitID;
	}
	@Column( insertable = true, updatable = false)
	public Long getVariationID() {
		return variationID;
	}
	public void setVariationID(Long variationID) {
		this.variationID = variationID;
	}
	public Long getQte() {
		return qte;
	}
	public void setQte(Long qte) {
		this.qte = qte;
	}
	@Transient
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
