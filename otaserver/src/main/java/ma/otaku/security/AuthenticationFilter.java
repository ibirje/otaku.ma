package ma.otaku.security;

import java.io.IOException;

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

import ma.otaku.acces.admin.AccesAdminLogin;
import ma.otaku.data.admin.AdminLoginDB;
import ma.otaku.data.admin.TokenDB;
import ma.otaku.utils.Build;
import ma.otaku.utils.Utils;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String REALM = "example";
    private static final String AUTH_SCHEME = "Bearer";
    
    @Context private HttpServletRequest servletRequest;
	
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

    	String req = requestContext.getUriInfo().getPath(true);
    	String method = requestContext.getMethod();
    	
        if( Build.MAN_LOGGING_ON && servletRequest != null && !method.equals("OPTIONS") 
        	&& ( req.equals("public.*") || req.equals("authentication.*") || req.equals("schedule.*") ) )
        {
        	System.err.println("[ "+servletRequest.getRemoteAddr()+" ]  "+new Utils().shortTime()+"  "+req);
        }

    	if( method.equals("OPTIONS")) return;
    	if( req.matches("public.*")) return;
    	if( req.matches("authentication.*")) return;
    	if( req.matches("schedule.*")) return;
    	// if( req.contains("stock/test")) return;
    	
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authorizationHeader.substring(AUTH_SCHEME.length()).trim();

        TokenDB tokendb = null;
        try {
        	tokendb = validateToken(token);
        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
        
        requestContext.setSecurityContext(new OtakuSecurityContext(getLogin(tokendb.getAdminID()), AUTH_SCHEME));
        
        System.err.println("["+tokendb.getPseudo()+":"+servletRequest.getRemoteAddr()+"] "+req+"  "+ new Utils().shortTime());
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
        	.startsWith(AUTH_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) 
    {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, AUTH_SCHEME + " realm=\"" + REALM + "\"")
                .build());
    }

    private TokenDB validateToken(String token) throws Exception 
    {
        TokenDB tokendb = new AccesAdminLogin().isTokenValide(token);
    	if(tokendb == null) throw new Exception("Acces Interdit");
    	return tokendb;
    }
    private AdminLoginDB getLogin(Long id)
    {
    	return new AccesAdminLogin().getAdmin(id);
    }
}









