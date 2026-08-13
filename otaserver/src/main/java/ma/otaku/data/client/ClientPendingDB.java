package ma.otaku.data.client;

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
@Table(name ="client_pending")
public class ClientPendingDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long clientpID;
	private String email;
	private String motdepasse;
	private String emailkey;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Timestamp dateInsertion;

	private String facebookuid;
	private String nom;
	private String prenom;
	
	
	public ClientPendingDB() { }
	
	public ClientPendingDB(String email, String motdepasse, String uid, String username, String emailkey) {
		this.email = email;
		this.motdepasse = motdepasse;
		this.emailkey = emailkey;
		this.facebookuid = uid;
		if(username != null)
		{
			String[] sp = username.split(" ");
			this.prenom	= sp[0];
			this.nom	= sp[1];
		}
		dateInsertion = new Timestamp(new Utils().now().getTime() + Constantes.PENDING_KEY_EXPIRE_TIME );
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getClientpID() {
		return clientpID;
	}
	public void setClientpID(Long clientpID) {
		this.clientpID = clientpID;
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
	public Timestamp getDateInsertion() {
		return dateInsertion;
	}
	public void setDateInsertion(Timestamp dateInsertion) {
		this.dateInsertion = dateInsertion;
	}
	public String getMotdepasse() {
		return motdepasse;
	}
	public void setMotdepasse(String motdepasse) {
		this.motdepasse = motdepasse;
	}
	public String getEmailkey() {
		return emailkey;
	}
	public void setEmailkey(String emailkey) {
		this.emailkey = emailkey;
	}

	public String getFacebookuid() {
		return facebookuid;
	}

	public void setFacebookuid(String facebookuid) {
		this.facebookuid = facebookuid;
	}
	
	
	
   
}
