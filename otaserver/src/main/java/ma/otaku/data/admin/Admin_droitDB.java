package ma.otaku.data.admin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name ="admin_droit")
public class Admin_droitDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long adminDroitID;
	private String code;
	private String droit;
	private String description;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAdminDroitID() {
		return adminDroitID;
	}
	public void setAdminDroitID(Long adminDroitID) {
		this.adminDroitID = adminDroitID;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDroit() {
		return droit;
	}
	public void setDroit(String droit) {
		this.droit = droit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
