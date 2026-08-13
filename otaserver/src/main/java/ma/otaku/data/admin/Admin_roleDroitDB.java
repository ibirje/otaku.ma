package ma.otaku.data.admin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "admin_roledroit")
public class Admin_roleDroitDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long roleDroitID;
	private Long adminRoleID;
	private Long adminDroitID;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getRoleDroitID() {
		return roleDroitID;
	}
	public void setRoleDroitID(Long roleDroitID) {
		this.roleDroitID = roleDroitID;
	}
	public Long getAdminRoleID() {
		return adminRoleID;
	}
	public void setAdminRoleID(Long adminRoleID) {
		this.adminRoleID = adminRoleID;
	}
	public Long getAdminDroitID() {
		return adminDroitID;
	}
	public void setAdminDroitID(Long adminDroitID) {
		this.adminDroitID = adminDroitID;
	}
	
	
	
	
	
	
}
