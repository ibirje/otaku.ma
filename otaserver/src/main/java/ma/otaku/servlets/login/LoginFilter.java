package ma.otaku.servlets.login;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.otaku.acces.client.AccesClient;
import ma.otaku.acces.client.AccesClientSession;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.ClientSessionDB;
import ma.otaku.utils.Constantes;


public class LoginFilter implements Filter {
	
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
            FilterChain chain) throws IOException, ServletException {
    	
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getRequestURI();
        if (url.contains("connection") || url.contains("authentification") || url.contains("deconnecter") ||
        	url.contains("inscriptionComplete") || url.contains("inscription") || url.contains("valideinscription")||
        	url.contains("/verifemail/*")) 
            chain.doFilter(request, response);
        else
        {
	        Cookie logincookie = null;
	        
	        if(req.getCookies() != null)
	        	logincookie = Arrays.stream(req.getCookies()).filter(c -> c.getName()
	                .equals(Constantes.SESSION_NOM)).findAny().orElse(null);

	        try 
	        {
	        	if ( logincookie == null || logincookie.getValue().isEmpty()) throw new NullPointerException("session null");

	        	Cookie clientnom = Arrays.stream(req.getCookies()).filter(c -> c.getName()
	                    .equals("_nom")).findAny().orElse(null);
	
	        	if( clientnom != null && !clientnom.getValue().isEmpty()) throw new NullPointerException("client existant"); // pas besoin de rafraichir les données de la bdd

	        	AccesClientSession accessession = new AccesClientSession();
	        	
	        	ClientSessionDB cltsession = accessession.getEquals("cookie", logincookie.getValue());
	        	
	        	if( cltsession == null) throw new NullPointerException("session inexistante");
	        	
	        	/* fetch common data */
	        	
	        	AccesClient accesclient = new AccesClient();
	        	ClientDB client = accesclient.getByID(cltsession.getClientID());
	        	
				/* TODO gerer isActive ( admin ou client peut activer , desactiver son compte )
				 * TODO gerer etat NORMAL, SUSPENDED, BANNED */
	        	
	        	addCookie("_nom", client.getNom(), resp);
	        	addCookie("_prenom", client.getPrenom(), resp);
	        	addCookie("_paniercount", client.getPanierCount()+"", resp);
	        }
	        catch(Exception ex) {
	        	//System.err.println("- (LoginFilterErr)  "+request.getRemoteAddr()+" -- "+ex.getMessage()+" -- at "+url);
	        }
	        finally
	        {
	        	chain.doFilter(request, response);
	        }
        }
    }

    private void addCookie(String param, String value, HttpServletResponse resp) {
		Cookie cookie = new Cookie(param,value);
		cookie.setMaxAge(Constantes.SESSION_EXPIRE_TIME);
		resp.addCookie(cookie);
	}

	@Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
    }


}
