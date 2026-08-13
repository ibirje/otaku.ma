package ma.otaku.data.client;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="coursier")
public class CoursierDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long coursierID;
	private String coursier;
	private Double prixPL_LD;
	private Double coutPL_LD;
	private Double prixPL_PR;
	private Double coutPL_PR;
	private Double prixVB_LD;
	private Double coutVB_LD;
	private Double prixVB_PR;
	private Double coutVB_PR;
	
	private Double seuilPrixLivraisonGratuite;

	private String ville;
	private String codes;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCoursierID() {
		return coursierID;
	}
	public void setCoursierID(Long coursierID) {
		this.coursierID = coursierID;
	}
	public Double getPrixPL_LD() {
		return prixPL_LD;
	}
	public String getCoursier() {
		return coursier;
	}
	public void setCoursier(String coursier) {
		this.coursier = coursier;
	}
	public void setPrixPL_LD(Double prixPL_LD) {
		this.prixPL_LD = prixPL_LD;
	}
	public Double getCoutPL_LD() {
		return coutPL_LD;
	}
	public void setCoutPL_LD(Double coutPL_LD) {
		this.coutPL_LD = coutPL_LD;
	}
	public Double getPrixPL_PR() {
		return prixPL_PR;
	}
	public void setPrixPL_PR(Double prixPL_PR) {
		this.prixPL_PR = prixPL_PR;
	}
	public Double getCoutPL_PR() {
		return coutPL_PR;
	}
	public void setCoutPL_PR(Double coutPL_PR) {
		this.coutPL_PR = coutPL_PR;
	}
	public Double getPrixVB_LD() {
		return prixVB_LD;
	}
	public void setPrixVB_LD(Double prixVB_LD) {
		this.prixVB_LD = prixVB_LD;
	}
	public Double getCoutVB_LD() {
		return coutVB_LD;
	}
	public void setCoutVB_LD(Double coutVB_LD) {
		this.coutVB_LD = coutVB_LD;
	}
	public Double getPrixVB_PR() {
		return prixVB_PR;
	}
	public void setPrixVB_PR(Double prixVB_PR) {
		this.prixVB_PR = prixVB_PR;
	}
	public Double getCoutVB_PR() {
		return coutVB_PR;
	}
	public void setCoutVB_PR(Double coutVB_PR) {
		this.coutVB_PR = coutVB_PR;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public Double getSeuilPrixLivraisonGratuite() {
		return seuilPrixLivraisonGratuite;
	}
	public void setSeuilPrixLivraisonGratuite(Double seuilPrixLivraisonGratuite) {
		this.seuilPrixLivraisonGratuite = seuilPrixLivraisonGratuite;
	}
	public String getCodes() {
		return codes;
	}
	public void setCodes(String codes) {
		this.codes = codes;
	}
	
	
}
