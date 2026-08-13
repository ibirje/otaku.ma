package ma.otaku.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import ma.otaku.data.client.ClientDB;

public class PublicSecurityContext implements SecurityContext{

	private ClientDB client;
	private String scheme;
	
	public PublicSecurityContext(ClientDB client, String scheme) {
		this.client = client;
		this.scheme = scheme;
	}
	
	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

	@Override
	public Principal getUserPrincipal() {
		return this.client;
	}

	@Override
	public boolean isSecure() {
		return "https".equals(this.scheme);
	}
	

	@Override
	public boolean isUserInRole(String s) {
        return true;
	}

}
