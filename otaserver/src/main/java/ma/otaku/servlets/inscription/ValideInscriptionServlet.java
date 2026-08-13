package ma.otaku.servlets.inscription;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ma.otaku.acces.client.AccesClient;
import ma.otaku.acces.client.AccesClientPending;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.ClientPendingDB;
import ma.otaku.mailing.MailUtils;
import ma.otaku.utils.Build;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.RandomString;
import ma.otaku.utils.Validateur;

/**
 * Servlet implementation class ValideInscriptionServlet
 */
// @WebServlet("/valideinscription")
public class ValideInscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public ValideInscriptionServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String remoteip = request.getRemoteAddr();
		String grecaptcha = (String) request.getParameter("g-recaptcha-response");
		String secret="6Ld8THsUAAAAAB2HFO_2Z4bHId6KBKIJIEafK2ug";
		
		String email = request.getParameter("email");
		String motdepasse = request.getParameter("mdp");
		String confirmemdp = request.getParameter("mdp2");
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		
		//valide captcha
		//valide email
		//valide mot de passe
		//valide nom / prénom ( déjà inscrit ? )
		
		String erreur = null;
		
		if(grecaptcha == null || !isCaptchaValid(secret, grecaptcha, remoteip))
			erreur = "Captcha invalide.";
		else if(!new Validateur().isEmailValide(email))
			erreur = "Adresse email invalide.";
		else if(motdepasse == null || motdepasse.length() < Constantes.MIN_PASSWORD_SIZE || !motdepasse.equals(confirmemdp))
		{
			erreur = "Mot de passe invalide.";
		}
		else if(nom == null || nom.length() < Constantes.MIN_NOM_SIZE)
			erreur = "Nom invalide.";
		else if(prenom == null || prenom.length() < Constantes.MIN_NOM_SIZE)
			erreur = "Prénom invalide.";
		
		if(erreur != null)
		{
			response.sendRedirect(request.getContextPath()+"/inscription?erreur="+erreur);
			return;
		}
		
		/* verifier si email ou nom + prenom déjà utilisés par un client inscrit*/
		
		AccesClient acces = new AccesClient();
		
		ClientDB client = acces.existe(email);
		
		if(client != null)
		{
			erreur = "";
			if ( email.equals(client.getEmail()) ) 
				erreur += "Email déjà utilisé. ";
			else
				erreur += "Nom et prénom déjà utilisés. ";
			response.sendRedirect(request.getContextPath()+"/inscription?erreur="+erreur);
			return;
		}

		/* verifier si email ou nom + prenom déjà utilisés par un client qui n'as pas encore confirmé l'email*/
		
		AccesClientPending accespending = new AccesClientPending();
		
		ClientPendingDB pending = accespending.existe(email, nom, prenom);
		
		if(pending != null)
		{
			erreur = "";
			if ( email.equals(pending.getEmail()) ) 
			{
				StringBuilder hint = new StringBuilder();
				hint.append(email.substring(0,2));
				hint.append("***");
				hint.append(email.substring(email.indexOf("@"),email.length()) );
				erreur += "Email "+hint.toString()+" déjà utilisé. si vous n'avez pas confirmé votre adresse, un mail a été envoyé à cette adresse.<br/>";
			}
			response.sendRedirect(request.getContextPath()+"/inscription?erreur="+erreur);
			return;
		}
		
		String key = new RandomString(64).nextString();
		
		ClientPendingDB temp = accespending.getEquals("emailkey", key);
		if( temp != null)
			key = new RandomString(64).nextString();

		pending = new ClientPendingDB(email, motdepasse , null, null, key);
		try 
		{
			accespending.insert(pending);	
		}
		catch(Exception ex)
		{
			erreur = "Echec d'inscription... reessayez plus tard.";
			response.sendRedirect(request.getContextPath()+"/inscription?erreur="+erreur);
			return;
		}
		
		
		//key = utils.crypter(key);
		// try crypt decript fe nefs lblasa
		// try cript be javax
		
		/* ********************************
		 * try using /path?key=xx instead of /path/key
		 **********************************/
		
		if(Build.BUILD == Build.LOCAL_BUILD && Build.AUTO_MAIL_OFF)
		{
			response.sendRedirect(request.getContextPath()+"/verifemail/"+key);
			return;
		}
		MailUtils mail = new MailUtils();
		
		/*boolean succesmail = mail.sendVerificationEmail(email,nom,prenom, key );
		
		if(!succesmail)
		{
			erreur = "Echec d'envoi du mail de confirmation... reessayez plus tard.";
			response.sendRedirect(request.getContextPath()+"/inscription?erreur="+erreur);
			return;
		}
*/
		response.sendRedirect(request.getContextPath()+"/inscriptionComplete");
		
		/* ****** Genere clé verification version cryptée et version non cryptée ****** */
		/* ****** Enregistre TempClient à la base de données avec la clé non cryptée ****** */
		/* ****** SMTP send email verification à l'email client avec lien qui contient la clé cryptée ******** */
		/* ****** redirect page email envoyé (contient renvoyer email) ******** */
		
		/* *** if link email clicked  goto FinalyzeInscriptionServlet => *** */
		/* => decrypte clé , select tempuser by clé 
		 * if clé nexiste pas renvoi erreur clé expirée ******** */
		/* sinon  insert user. redirect => page inscription reussi + links vers completer profil ou go accueil ******** */
	}
	/* ******** créer table panier ******** */
	
	
	
	public static boolean isCaptchaValid(String secretKey, String response, String remoteip) {
	    try {
	        String url = "https://www.google.com/recaptcha/api/siteverify?"
	                + "secret=" + secretKey
	                + "&response=" + response
	                + "&remoteip=" + remoteip;
	        InputStream res = new URL(url).openStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(res, Charset.forName("UTF-8")));

	        StringBuilder sb = new StringBuilder();
	        int cp;
	        while ((cp = reader.read()) != -1) {
	            sb.append((char) cp);
	        }
	        String jsonText = sb.toString();
	        res.close();

	        JSONObject json = new JSONObject(jsonText);
	        return json.getBoolean("success");
	    } catch (Exception e) {
	        return false;
	    }
	}
	
}
