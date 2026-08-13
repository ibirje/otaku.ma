package ma.otaku.security;

import java.io.IOException;
import java.sql.Timestamp;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import ma.otaku.acces.client.AccesClient;
import ma.otaku.data.client.ClientDB;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class PublicAuthenticationFilter implements ContainerRequestFilter {

    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    
	Timestamp reqdate;
	private static final String[] public_links = {
		"public/produits/list.*",
		"public/produits/fullproduit.*",
		"public/produits/homeproduits",
		"public/produits/newproduits.*",
		"public/produits/suggestions.*",
		"public/type/suggestionSection*",
		
		"public/authentication/connect.*",
		"public/authentication/facebookconnect.*",
		"public/authentication/facebookregister.*",
		"public/authentication/inscription.*",
		"public/authentication/resendEmail.*",
		"public/authentication/confirmEmail.*",
		"public/authentication/recuperepassword.*",
		"public/authentication/validerecupmotdepasse.*",
		"public/authentication/validerecupcode.*",
		
		"public/commandes/resume.*",
		"public/type/categlist",
		"public/type/themelist"
	};
    
    @Context private HttpServletRequest servletRequest;
	
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
   	
    	String req = requestContext.getUriInfo().getPath(true);
    	
    	if( !req.matches("public.*") || requestContext.getMethod().equals("OPTIONS")) return;
    	
    	if( req.matches("schedule.*"))
    	{
    		String appcron = requestContext.getHeaderString("X-Appengine-Cron");
    		if(appcron != null)
    			return;
    	}
        for(String url : public_links)
        	if( req.matches(url)) 
        		return;

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if (!isTokenBasedAuthentication(authorizationHeader)) {
        	System.err.println("isTokenBasedAuthentication");
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        ClientDB client = null;
        try {
        	client = validateToken(token);
        } catch (Exception e) {
        	System.err.println("validateToken");
            abortWithUnauthorized(requestContext);
        }
        
        requestContext.setSecurityContext(new PublicSecurityContext(client, AUTHENTICATION_SCHEME));

    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        return authorizationHeader != null && authorizationHeader.toLowerCase()
                    .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
    	System.err.println("INAUTORISE");
        requestContext.abortWith(
	        Response.status(Response.Status.UNAUTHORIZED)
	        .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
	        .build());
    }

    private ClientDB validateToken(String token) throws Exception {
    	
    	AccesClient acceslogin = new AccesClient();
        ClientDB client = acceslogin.getEquals("token", token);
    	if(client == null) throw new Exception("Acces Interdit");
    	return client;
    }
}









