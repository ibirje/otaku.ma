package ma.otaku.data.admin;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.utils.Constantes;
import ma.otaku.utils.Utils;


@Entity
@Table(name = "token")
public class TokenDB {


	@JsonProperty(access = Access.WRITE_ONLY)
	private Long tokenID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long adminID;
	private String pseudo;
	private String token;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Timestamp debut;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Timestamp fin;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getTokenID() {
		return tokenID;
	}
	public void setTokenID(Long tokenID) {
		this.tokenID = tokenID;
	}
	
	public TokenDB() {
	}
	
	public TokenDB(Long id, String pseudo, String token) {
		this.adminID = id;
		this.pseudo = pseudo;
		this.token = token;
		Utils utils = new Utils();
		debut = utils.now();
		fin = new Timestamp(utils.now().getTime() + Constantes.ADMIN_TOKEN_EXPIRE_TIME);
	}
	
	public TokenDB(String pseudo, String token, Timestamp debut, Timestamp fin) {
		this.pseudo = pseudo;
		this.token = token;
		this.debut = debut;
		this.fin = fin;
	}
	
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Timestamp getDebut() {
		return debut;
	}
	public void setDebut(Timestamp debut) {
		this.debut = debut;
	}
	public Timestamp getFin() {
		return fin;
	}
	public void setFin(Timestamp fin) {
		this.fin = fin;
	}

	public Long getAdminID() {
		return adminID;
	}

	public void setAdminID(Long adminID) {
		this.adminID = adminID;
	}
	
	
}
