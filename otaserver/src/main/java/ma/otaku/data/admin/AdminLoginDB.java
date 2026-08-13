package ma.otaku.data.admin;

import java.security.Principal;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "admin_login")
public class AdminLoginDB implements Principal {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long adminLoginID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long adminID;
	
	private String pseudo;
	private String password;
	private String question;
	private String reponse;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String role;

	@JsonProperty(access = Access.WRITE_ONLY)
	private HashSet<String> droits;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAdminLoginID() {
		return adminLoginID;
	}
	public void setAdminLoginID(Long adminLoginID) {
		this.adminLoginID = adminLoginID;
	}
	public Long getAdminID() {
		return adminID;
	}
	public void setAdminID(Long adminID) {
		this.adminID = adminID;
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getReponse() {
		return reponse;
	}
	public void setReponse(String reponse) {
		this.reponse = reponse;
	}
	@Transient
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Transient
	public HashSet<String> getDroits() {
		return droits;
	}
	public void setDroits(HashSet<String> droits) {
		this.droits = droits;
	}
	@Transient
	@Override
	public String getName() {
		return pseudo;
	}
	
}
