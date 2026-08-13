package ma.otaku.data.admin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "admin_role")
public class AdminRoleDB {

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long adminRoleID;
	private String code;
	private String role;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAdminRoleID() {
		return adminRoleID;
	}
	public void setAdminRoleID(Long adminRoleID) {
		this.adminRoleID = adminRoleID;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
