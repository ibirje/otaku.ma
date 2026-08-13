package ma.otaku.data.client;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "client_login")
public class ClientLoginDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long clientloginID;
	private Long clientID;
	private String email;
	private String motdepasse;
	private String nouveau;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String facebookuid;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String recupkey;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Timestamp recupkeydate;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getClientloginID() {
		return clientloginID;
	}
	public void setClientloginID(Long clientloginID) {
		this.clientloginID = clientloginID;
	}
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String pseudo) {
		this.email = pseudo;
	}
	public String getMotdepasse() {
		return motdepasse;
	}
	public void setMotdepasse(String motdepasse) {
		this.motdepasse = motdepasse;
	}
	public String getFacebookuid() {
		return facebookuid;
	}
	public void setFacebookuid(String facebookuid) {
		this.facebookuid = facebookuid;
	}
	public String getRecupkey() {
		return recupkey;
	}
	public void setRecupkey(String recupkey) {
		this.recupkey = recupkey;
	}
	public Timestamp getRecupkeydate() {
		return recupkeydate;
	}
	public void setRecupkeydate(Timestamp stamp) {
		this.recupkeydate = stamp;
	}
	@Transient
	public String getNouveau() {
		return nouveau;
	}
	public void setNouveau(String nouveau) {
		this.nouveau = nouveau;
	}
}
