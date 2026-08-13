package ma.otaku.data.client;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ma.otaku.utils.Utils;

//@JsonIgnoreType


@Entity
@Table( name = "client_session")
public class ClientSessionDB 
{

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long clientsessionID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long clientID;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String cookie;
	private Timestamp updated;
	
	
	
	

	public ClientSessionDB() 
	{
	}
	
	public ClientSessionDB(Long clientID, String cookie) 
	{
		this.clientID = clientID;
		this.cookie = cookie;
		updated = new Utils().now();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getClientsessionID() {
		return clientsessionID;
	}
	
	public void setClientsessionID(Long clientsessionID) {
		this.clientsessionID = clientsessionID;
	}
	public Long getClientID() {
		return clientID;
	}
	public void setClientID(Long clientID) {
		this.clientID = clientID;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public Timestamp getUpdated() {
		return updated;
	}
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
	
	
	
}
