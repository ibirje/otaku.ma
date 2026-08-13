package ma.otaku.mailing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import ma.otaku.utils.Build;
import ma.otaku.utils.Constantes;
public class SendMailUtils {

	public boolean sendVerificationEmail(String email, String nom, String prenom, String key) throws IOException {
		

	    
	    String veriflink  = Build.WEBSITE_LINK+"/verifemail/"+key;

		String from    = Constantes.AUTO_MAIL_VERIFICATION_EMETTEUR;
	    String subject = Constantes.AUTO_MAIL_VERIFICATION_SUJET;
	    /*
	    String content = 
	    "<html>" + 
	    "    <head>" + 
	    "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" + 
	    "        <title></title>" + 
	    "    </head>"+
	    "	<body style=\"background-color:#CEECFF\">" + 
	    "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"bodyTable\">" + 
	    "            <tr>" + 
	    "				<td align=\"center\" valign=\"top\">" + 
	    "                    <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\" id=\"emailContainer\">" + 
	    "                        <tr>" + 
	    "                            <td align=\"center\" valign=\"top\">"+
	    "								<div align=\"left\">" + 
	    "                                  <p>" + 
	    "                                    À "+nom+" "+prenom+",<br/><br/>" + Constantes.AUTO_MAIL_VERIFICATION_MESSAGE+
	    "                                  </p>" + 
	    "                                </div>"+
	    "                                <div>" + 
	    "                                  <a style=\"background-color:#294fc4;border:1px solid #333333;border-color:#28497d;border-radius:7px;border-width:2px;color:#ffffff;display:inline-block;font-family:helvetica,arial,sans-serif;font-size:18px;font-weight:300;letter-spacing:0px;line-height:24px;padding:12px 18px 12px 18px;text-align:center;text-decoration:none\" "
	    									+ "href=\""+veriflink+"\" >Confirmer</a>" + 
		"                                </div>" + 
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
	    */
	    String content = "<html><head></head><body style=\\\"background-color:#CEECFF\\\"><a href=\\\"https://www.otaku.ma\\\">Otaku.ma</a></body></html>";
	    return sendPost(
	    		"{" + 
				"  \"personalizations\": [" + 
				"    {" + 
				"      \"to\":" + 
				"        {" + 
				"          \"email\": \""+email+"\"" + 
				"        }\n" + 
				"      ]," + 
				"      \"subject\": \""+subject+"\"" + 
				"    }" + 
				"  ]," + 
				"  \"from\": {" + 
				"    \"email\": \""+from+"\"" + 
				"  }," + 
				"  \"content\": [" + 
				"    {\n" + 
				"      \"type\": \"text/html\"," + 
				"      \"value\": \""+content+"\"" + 
				"    }" + 
				"  ]" + 
				"}" + 
				"");
	    
	}
	private boolean sendPost(String content) {
		HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead 

		try {

			String basicAuth = "Bearer " + System.getenv("SENDGRID_API_KEY");
			
		    HttpPost request = new HttpPost("https://api.sendgrid.com/v3/mail/send");
		    
		    StringEntity params =new StringEntity(content);
		    request.addHeader("content-type", "application/json");
		    request.addHeader ("Authorization", basicAuth);
		    request.setEntity(params);
		    HttpResponse response = httpClient.execute(request);

		    // System.out.println(response.getEntity());
		    //handle response here...
		    return true;
		}catch (Exception ex) {
			ex.printStackTrace();
		    //handle exception here
			return false;
		} finally {
		}
	}
	
	
	protected boolean sendPOST(String content) throws IOException {
		URL obj = new URL("https://api.sendgrid.com/v3/mail/send");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		String basicAuth = "Bearer " + System.getenv("SENDGRID_API_KEY");

		con.setRequestMethod("POST");
		con.setRequestProperty ("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Content-Language", "en-US");

		con.setDoInput(true);
		con.setDoOutput(true);
		// For POST only - START
		OutputStream os = con.getOutputStream();
		os.write(content.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			
			System.out.println(response.toString());
			return true;
		} else {
			System.out.println(con.getResponseMessage());
			System.out.println("POST request not working");
			return false;
		}
	}
	
}
