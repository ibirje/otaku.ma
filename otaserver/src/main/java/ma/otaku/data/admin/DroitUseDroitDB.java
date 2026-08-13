package ma.otaku.data.admin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name ="droitusedroit")
public class DroitUseDroitDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long dudID;
	private Long adminDroitID;
	private Long usedDroitID;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getDudID() {
		return dudID;
	}
	public void setDudID(Long dudID) {
		this.dudID = dudID;
	}
	public Long getAdminDroitID() {
		return adminDroitID;
	}
	public void setAdminDroitID(Long adminDroitID) {
		this.adminDroitID = adminDroitID;
	}
	public Long getUsedDroitID() {
		return usedDroitID;
	}
	public void setUsedDroitID(Long usedDroitID) {
		this.usedDroitID = usedDroitID;
	}
	
}
