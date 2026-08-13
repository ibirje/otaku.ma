package ma.otaku.data.stock;

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

import ma.otaku.data.TableWithCode;
import ma.otaku.data.TableWithQte;
import ma.otaku.utils.Parse;

@Entity
@Table(name = "achatstock")
public class AchatStockDB implements TableWithCode, TableWithQte{

	/* DB uniquement */
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long achatStockID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long fournisseurID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Date dateInsertion;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Date dateCommande;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Date dateLivraison;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long authAdminID;
	/* ************ */
	
	/* REST uniquement */
	private String dateInsertiontext;
	private String dateCommandetext;
	private String dateLivraisontext;
	private String codeFournisseur;
	private String authAdminNom;
	/* ************** */
	
	private String code;
	private String type; 		//National / International
	private Double prixTotal;
	private Double fraisSupplementaires;
	private Long qte;
	private String description;
	private Boolean associe;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAchatStockID() {
		return achatStockID;
	}
	public void setAchatStockID(Long achatStockID) {
		this.achatStockID = achatStockID;
	}
	public Long getFournisseurID() {
		return fournisseurID;
	}
	public void setFournisseurID(Long fournisseurID) {
		this.fournisseurID = fournisseurID;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getFraisSupplementaires() {
		return fraisSupplementaires;
	}
	public void setFraisSupplementaires(Double fraisSupplementaires) 
	{
		this.fraisSupplementaires = fraisSupplementaires;
	}
	
	@Override
	public Long getQte() {
		return qte;
	}
	@Override
	public void setQte(Long qTE) {
		qte = qTE;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getPrixTotal() {
		return prixTotal;
	}
	public void setPrixTotal(Double prixTotal) {
		this.prixTotal = prixTotal;
	}
	
	public Date getDateCommande() {
		
		if(dateCommande == null) dateCommande = new Parse().ParseDate(dateCommandetext);
		return dateCommande;
	
	}
	public void setDateCommande(Date dateCommande) {
		this.dateCommande = dateCommande;
	}
	
	public Date getDateLivraison() {		
		if(dateLivraison == null) dateLivraison = new Parse().ParseDate(dateLivraisontext);
		return dateLivraison;
	}
	public void setDateLivraison(Date dateLivraison) {
		this.dateLivraison = dateLivraison;
	}
	@Column(insertable = false, updatable = false)
	public Date getDateInsertion() {
		return dateInsertion;
	}
	public void setDateInsertion(Date dateInsertion) {
		this.dateInsertion = dateInsertion;
	}
	
	@Transient
	public String getCodeFournisseur() {
		return codeFournisseur;
	}
	public void setCodeFournisseur(String codeFournisseur) {
		this.codeFournisseur = codeFournisseur;
	}

	@Transient
	public String getDateInsertiontext() {
		if(dateInsertiontext == null) dateInsertiontext = new Parse().dateToString(dateInsertion);
		return dateInsertiontext;
	}
	public void setDateInsertiontext(String dateInsertiontext) {
		this.dateInsertiontext = dateInsertiontext;
	}
	
	@Transient
	public String getDateCommandetext() {
		if(dateCommandetext == null) dateCommandetext = new Parse().dateToString(dateCommande);
		return dateCommandetext;
	}
	public void setDateCommandetext(String dateCommandetext) {
		this.dateCommandetext = dateCommandetext;
	}

	@Transient
	public String getAuthAdminNom() {
		return authAdminNom;
	}
	public void setAuthAdminNom(String authAdminNom) {
		this.authAdminNom = authAdminNom;
	}
	@Transient
	public String getDateLivraisontext() {
		if(dateLivraisontext == null) dateLivraisontext = new Parse().dateToString(dateLivraison);
		return dateLivraisontext;
	}
	public void setDateLivraisontext(String dateLivraisontext) {
		this.dateLivraisontext = dateLivraisontext;
	}
	
	@Column(insertable = false, updatable = true)
	public Boolean getAssocie() {
		return associe;
	}
	public void setAssocie(Boolean associe) {
		this.associe = associe;
	}
	public Long getAuthAdminID() {
		return authAdminID;
	}
	public void setAuthAdminID(Long authAdminID) {
		this.authAdminID = authAdminID;
	}
}
