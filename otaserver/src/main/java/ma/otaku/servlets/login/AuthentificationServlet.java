package ma.otaku.servlets.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ma.otaku.acces.client.AccesClientLogin;
import ma.otaku.acces.client.AccesClientSession;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.ClientSessionDB;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.RandomString;

/**
 * Servlet implementation class AuthentificationServlet
 */
// @WebServlet("/authentification")
public class AuthentificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthentificationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String sourceURL = (String) request.getParameter("source");

		String remoteip = request.getRemoteAddr();

		String grecaptcha = (String) request.getParameter("g-recaptcha-response");
		String pseudo     = (String) request.getParameter("pseudo");
		String password   = (String) request.getParameter("mdp");
		
		String secret="6LfOHXsUAAAAAOw_QKKYDg0_SXI6LYtmW-Cc9Ojz";
		
		try
		{
			if(grecaptcha == null || !isCaptchaValid(secret, grecaptcha, remoteip)) throw new Exception("Captcha invalide");
			
			/* connect client */
			AccesClientLogin login = new AccesClientLogin();

			ClientDB client = login.connect(pseudo,password);
			if( client == null) throw new Exception("pseudo ou mot de passe incorrecte");
			
			/* ***** verifie si utilisateur a déjà une session disponible ***** */
			
			AccesClientSession accessession = new AccesClientSession();
			
			ClientSessionDB psession = accessession.getEquals("clientID", client.getClientID());

			if(psession != null) accessession.delete(psession);
			
			/* ***** genere cle session **** */
				
			RandomString rand = new RandomString(128);
			String clesession = rand.nextString();

			/* ***** persiste la cle **** */
			accessession.insert(new ClientSessionDB(client.getClientID(), clesession));

			/* *************** setup session cookie ************ */
			addCookie(Constantes.SESSION_NOM, clesession, response);
			
        	addCookie("_nom", client.getNom(), response);
        	addCookie("_prenom", client.getPrenom(), response);
        	addCookie("_paniercount", client.getPanierCount()+"", response);

			/* *************** redirect a la page où le bouton connection a été cliqué ************ */	
		    if(sourceURL != null && !sourceURL.isEmpty() && !sourceURL.contains("connection") && !sourceURL.contains("authentification") && 
		    !sourceURL.contains("deconnecter") && !sourceURL.contains("inscriptionComplete") && !sourceURL.contains("inscription") && 
		    !sourceURL.contains("valideinscription") && !sourceURL.contains("/verifemail/*"))
		    	response.sendRedirect(sourceURL);
		    else
		    	response.sendRedirect(request.getContextPath());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			response.sendRedirect(request.getContextPath()+"/connection?erreur="+ex.getMessage());
		}
		
		
		/*
		Timestamp stamp = new Timestamp(new Utils().now().getTime() - Constantes.SESSION_EXPIRE_TIME * Constantes.MILLIS_TO_SECONDE);
		if(psession.getUpdated().before(stamp)) 
		{

			Cookie cookie = new Cookie(Constantes.SESSION_NOM, psession.getCookie());
			cookie.setMaxAge(Constantes.SESSION_EXPIRE_TIME);
			
			response.addCookie(cookie);

		    if(sourceURL != null && !sourceURL.contains("connection") && !sourceURL.contains("valideinscription"))
				response.sendRedirect(sourceURL);
		    else
				response.sendRedirect(Build.BASE_URL);
		    return;
		}
		else
		*/
		
		
		
	    
	}

    private void addCookie(String param, String value, HttpServletResponse resp) {
		Cookie cookie = new Cookie(param,value);
		cookie.setMaxAge(Constantes.SESSION_EXPIRE_TIME);
		resp.addCookie(cookie);
	}
	
	
	public static boolean isCaptchaValid(String secretKey, String response, String remoteip) {
	    try {
	        String url = "https://www.google.com/recaptcha/api/siteverify?"
	                + "secret=" + secretKey
	                + "&response=" + response
	                + "&remoteip=" + remoteip;
	        
	        InputStream res = new URL(url).openStream();
	        
	        BufferedReader rd = new BufferedReader(new InputStreamReader(res, Charset.forName("UTF-8")));

	        StringBuilder sb = new StringBuilder();
	        
	        int cp;
	        
	        while ((cp = rd.read()) != -1) {
	            sb.append((char) cp);
	        }
	        
	        String jsonText = sb.toString();
	        
	        res.close();

	        JSONObject json = new JSONObject(jsonText);
	        
	        return json.getBoolean("success");
	        
	    } catch (Exception e) { return false; }
	}
	

}



