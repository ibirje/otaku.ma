package ma.otaku.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import ma.otaku.data.admin.AdminLoginDB;

public class OtakuSecurityContext implements SecurityContext{

	private AdminLoginDB admin;
	private String scheme;
	
	public OtakuSecurityContext(AdminLoginDB admin, String scheme) {
		this.admin = admin;
		this.scheme = scheme;
	}
	
	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

	@Override
	public Principal getUserPrincipal() {
		return this.admin;
	}

	@Override
	public boolean isSecure() {
		return "https".equals(this.scheme);
	}
	

	@Override
	public boolean isUserInRole(String s) {
		if (admin.getDroits() != null) {
            return admin.getDroits().contains(s);
        }
        return false;
	}

}
