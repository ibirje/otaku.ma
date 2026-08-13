package ma.otaku.mailing;

import java.io.IOException;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Email;
import com.sendgrid.SendGridException;

import ma.otaku.utils.Constantes;

public class MailUtils {
	
	//public static final String URL = "https://www.otaku.ma";
	public static final String FRONT_URL = "https://www.otaku.ma/user/";
	public static final String BACK_URL = "https://otaku-221716.appspot.com/app/public/";
	
	public boolean sendVerificationEmail(String recepteur, String nom, String prenom, String verificationkey) throws IOException
	{
	    String veriflink  = FRONT_URL + "verifemail/"+verificationkey;
	    String sujet = Constantes.AUTO_MAIL_VERIFICATION_SUJET;
	    String emetteur = Constantes.AUTO_MAIL_VERIFICATION_EMETTEUR;
		String content =
		    "<div align=\"left\">" + 
			    "<p>" + 
			    	nom!= null && prenom != null ?" À "+nom+" "+prenom+",<br/><br/>":"" + 
			    	Constantes.AUTO_MAIL_VERIFICATION_MESSAGE+
			    "</p>" + 
			"</div>"+
			"<div>" + 
			    "<a style=\"background-color:#294fc4;border:1px solid #333333;border-color:#28497d;border-radius:7px;border-width:2px;color:#ffffff;display:inline-block;font-family:helvetica,arial,sans-serif;font-size:18px;font-weight:300;letter-spacing:0px;line-height:24px;padding:12px 18px 12px 18px;text-align:center;text-decoration:none\" "
			    + "href=\""+veriflink+"\" >Confirmer</a>" + 
		    "</div>"; 		
	    
	    return mailDefault(recepteur, sujet, emetteur, content);
	}
	
	public boolean sendRecupPassword(String recepteur, String nom, String prenom, String verificationkey) throws IOException
	{
	    String veriflink  = FRONT_URL+"modifiermotdepasse/"+verificationkey;
	    String sujet = Constantes.AUTO_MAIL_RECUPPWD_SUJET;
	    String emetteur = Constantes.AUTO_MAIL_DEFAULT_EMETTEUR;
		String content =
		    "<div align=\"left\">" + 
			    "<p>" + 
			    	nom!= null && prenom != null ?" À "+nom+" "+prenom+",<br/><br/>":"" + 
			    	Constantes.AUTO_MAIL_RECUPPWD_MESSAGE+
			    "</p>" + 
			"</div>"+
			"<div>" + 
			    "<a style=\"background-color:#294fc4;border:1px solid #333333;border-color:#28497d;border-radius:7px;border-width:2px;color:#ffffff;display:inline-block;font-family:helvetica,arial,sans-serif;font-size:18px;font-weight:300;letter-spacing:0px;line-height:24px;padding:12px 18px 12px 18px;text-align:center;text-decoration:none\" "
			    + "href=\""+veriflink+"\" >Modifier</a>" + 
		    "</div>"; 		
	    
	    return mailDefault(recepteur, sujet, emetteur, content);
	}
	
	
	
	private boolean mailDefault(String recepteur, String sujet, String emetteur, String content) {
		
		SendGrid sendgrid = new SendGrid(System.getenv("SENDGRID_API_KEY"));
		
	    Email mail = new Email();

	    mail.addTo(recepteur);
	    
	    mail.setFrom(emetteur);
	    
	    mail.setSubject(sujet);
	    
	    mail.setHtml( getHtml(content));
	    
	    try {

		    SendGrid.Response response = sendgrid.send(mail);
		    if(!response.getStatus()) 
		    {
		    	System.err.println(response.getMessage());
		    	return false;
		    }
		    return true;
	    }catch(SendGridException ex)
	    {
	    	ex.printStackTrace();
	    	return false;
	    }
	}
	
	private String getHtml(String content)
	{
		return 
				"<html>" + 
		    	"    <head>" + 
		    	"        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" + 
		    	"        <title></title>" + 
		    	"    </head>"+
		    	"	<body>" + 
		    	"        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"bodyTable\">" + 
		    	"            <tr>" + 
		    	"				<td align=\"center\" valign=\"top\">" + 
		    	"                    <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\" id=\"emailContainer\">" + 
		    	"                        <tr>" + 
		    	"                            <td align=\"center\" valign=\"top\">"+
		    									content+ 
		    	"                                <br/><br/><br/><br/>"+
		    	"                                <div>" + 
		    	"                                  <a href=\"https://www.otaku.ma\">" + 
		    	"                                    <img style=\"display:block;color:#000000;text-decoration:none;font-family:Helvetica, arial, sans-serif;font-size:16px;max-width:60% !important;width:80%;height:auto !important;\" src=\"https://marketing-image-production.s3.amazonaws.com/uploads/064fea01f02618a9403855ecc958d1db9c736d457f5a8194bc63105a57dbcaa59fd2637b815121d5a5e021c6fc60d3b49cb63c86c355ecafb089ba89c3e869f6.png\" alt=\"\" width=\"480\" border=\"0\">" + 
		    	"                                  </a>" + 
		    	"                                </div>" + 
		    	"                                <br/>" + 
		    	"                                <div>&nbsp;Otaku.ma</div>" +
		    	"                                <div class=\"Unsubscribe--addressLine\" data-end-index=\"7842\" data-start-index=\"7804\">" + 
		    	"                                <p data-end-index=\"8059\" data-start-index=\"7975\" style=\"font-family:Arial, Helvetica, sans-serif;font-size:12px;line-height:20px\">Belvédère, <span class=\"Unsubscribe--senderCity\" data-end-index=\"8163\" data-start-index=\"8125\">Casablanca</span>, 61008</p>" + 
		    	"                                </div>" + 
		    	"                            </td>" + 
		    	"                        </tr>" + 
		    	"                    </table>" + 
		    	"                </td>" + 
		    	"            </tr>" + 
		    	"        </table>" + 
		    	"    </body>" + 
		    	"</html>";
	}
}
