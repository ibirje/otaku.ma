package ma.otaku.data.admin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "admin")
public class AdminDB {
	

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long adminID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long adminRoleID;
	private String nom;
	private String prenom;
	private String email;
	private String CIN;
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAdminID() {
		return adminID;
	}
	public void setAdminID(Long adminID) {
		this.adminID = adminID;
	}
	public Long getAdminRoleID() {
		return adminRoleID;
	}
	public void setAdminRoleID(Long adminRoleID) {
		this.adminRoleID = adminRoleID;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCIN() {
		return CIN;
	}
	public void setCIN(String cIN) {
		CIN = cIN;
	}
	
	
	
}
