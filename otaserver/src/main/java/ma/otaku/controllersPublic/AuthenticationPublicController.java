package ma.otaku.controllersPublic;

import java.security.AccessControlException;
import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;

import ma.otaku.acces.client.AccesClient;
import ma.otaku.acces.client.AccesClientAdresse;
import ma.otaku.acces.client.AccesClientLogin;
import ma.otaku.acces.client.AccesClientPending;
import ma.otaku.data.client.ClientAdresseDB;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.ClientFacebook;
import ma.otaku.data.client.ClientFacebookMdp;
import ma.otaku.data.client.ClientLoginDB;
import ma.otaku.data.client.ClientPendingDB;
import ma.otaku.mailing.MailUtils;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.RandomString;
import ma.otaku.utils.Utils;

@Path("public/authentication")
public class AuthenticationPublicController {

	@Context SecurityContext security;

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/connect")
	public Response connecterOptions( ClientLoginDB login) { return Response.ok().build(); }
    
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/connect")
	public Response connecter( ClientLoginDB login) 
	{
		try {

			ClientDB client = new AccesClientLogin(AccesClientLogin.READONLY).connect(login.getEmail(), login.getMotdepasse() ); // READONLY
			if(client == null)
			{
				if( new AccesClientPending(AccesClientPending.READONLY).getEquals("pseudo", login.getEmail()) == null) // READONLY
					return Response.status(Response.Status.NOT_FOUND).entity("{}").build(); 
				return Response.status(Response.Status.FOUND).entity("{}").build();
			}
			if(login.getFacebookuid() != null && !login.getFacebookuid().isEmpty())
				client.setFbassocie(true);
			else
				client.setFbassocie(false);
				
			return Response.ok(client).build();
		}
		catch ( AccessControlException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{}").build();
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
        	// e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity("{}").build();
        }
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/facebookconnect")
	public Response facebookConnecterOptions( ClientLoginDB login) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/facebookconnect")
	public Response facebookConnecter( ClientFacebook fb) 
	{
		try {
			if (  fb  == null ) throw new NullPointerException("Données facebook invalides");

			FacebookClient client = new DefaultFacebookClient(fb.getAccessToken(), Version.LATEST);

			User user = client.fetchObject("me", User.class, Parameter.with("fields","id,name,email")); 

			if(user == null) throw new AccessControlException("Données facebook invalides");
			
			AccesClientLogin acces = new AccesClientLogin(AccesClientLogin.READONLY);
			
			ClientLoginDB login = acces.getEquals("facebookuid", user.getId());
			
			if( login == null ) throw new NotAcceptableException("Non inscrit");
			
			return connecter(login);
		}
		catch ( NotAcceptableException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{}").build();
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
        	// e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity("{}").build();
        }
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/facebookassocier")
	public Response facebookassocierOptions( ClientLoginDB login) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/facebookassocier")
	public Response facebookAssocier( ClientFacebook fb)  {
		try {
			ClientDB user = (ClientDB) security.getUserPrincipal();
			
			if ( user == null ) throw new NotAcceptableException("Non connecté");
			if (  fb  == null ) throw new NotAcceptableException("Données facebook invalides");
			
			AccesClientLogin acces = new AccesClientLogin();
			
			ClientLoginDB login = acces.getEquals("clientID", user.getClientID());
			if(!acces.v().isNullOrEmpty(login.getFacebookuid())) throw new Exception("Facebook déjà associé");
			// TODO add facebook table 
			login.setFacebookuid(fb.getId());
			acces.update(login);
			
			return Response.ok(fb).build();
		
		} catch (Exception e) {
	    	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();
	    }
	}
	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/facebookregister")
	public Response facebookInscriptionOptions( ClientLoginDB login) { return Response.ok().build(); }
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/facebookregister")
	public Response facebookInscription( ClientFacebookMdp fb )  {
		try {
			if (  fb  == null || fb.getFb() == null ) throw new NotAcceptableException("Données facebook invalides");
			
			FacebookClient client = new DefaultFacebookClient(fb.getFb().getAccessToken(), Version.LATEST);

			User user = client.fetchObject("me", User.class, Parameter.with("fields","id,name,email")); 

			if(user == null) throw new AccessControlException("Données facebook invalides");
			
			AccesClient accesclient = new AccesClient();
			AccesClientLogin acceslogin = new AccesClientLogin();

			ClientLoginDB checklogin = acceslogin.getEquals("facebookuid", user.getId());
			if( checklogin != null )
				return connecter(checklogin);

			ClientLoginDB checklogin2 = acceslogin.getEquals("email", user.getEmail());
			if( checklogin2 != null ) throw new NotAcceptableException("Email '"+ user.getEmail() +"' déjà inscrit");
			
			String token = new RandomString().nextString();
			ClientDB newclient = accesclient.insert(new ClientDB(user.getEmail(), user.getName(), token));
			
			ClientLoginDB clientlogin = new ClientLoginDB();
			
			clientlogin.setClientID(newclient.getClientID());
			clientlogin.setEmail(user.getEmail());
			clientlogin.setMotdepasse(fb.getMotdepasse());
			clientlogin.setFacebookuid(user.getId());
			
			acceslogin.insert(clientlogin);
			newclient.setFbassocie(true);
			
			System.err.println("client "+newclient.getNom()+" "+newclient.getPrenom() + " inscrit avec l'email " + newclient.getEmail());
			
			return Response.ok(newclient).build();
		
		} catch (NotAcceptableException e) {

	    	e.printStackTrace();
	        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{ message : \"" + e.getMessage() + "\" }").build();
		}
		catch (Exception e) {
	    	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();
	    }
	}
	//
	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/facebookregisterwithoutmail")
	public Response facebookInscriptionNoMailOptions( ClientLoginDB login) { return Response.ok().build(); }
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/facebookregisterwithoutmail")
	public Response facebookInscriptionNoMail( ClientFacebookMdp fb )  {
		try {
			if (  fb  == null || fb.getFb() == null || fb.getEmail() == null ) 
				throw new NotAcceptableException("Données facebook invalides");
			
			FacebookClient client = new DefaultFacebookClient(fb.getFb().getAccessToken(), Version.LATEST);

			User user = client.fetchObject("me", User.class, Parameter.with("fields","id,name,email")); 

			if(user == null) throw new AccessControlException("Données facebook invalides");
			
			AccesClientLogin acceslogin = new AccesClientLogin();

			ClientLoginDB checklogin = acceslogin.getEquals("facebookuid", user.getId());
			if( checklogin != null )
				return connecter(checklogin);
			
			ClientLoginDB cldb = new ClientLoginDB();
			cldb.setEmail(fb.getEmail());
			cldb.setFacebookuid(fb.getFb().getId());
			cldb.setMotdepasse(fb.getMotdepasse());
			
			inscription(cldb, user.getName());
			
			return Response.ok().build();
		
		} catch (NotAcceptableException e) {

	    	e.printStackTrace();
	        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{ message : \"" + e.getMessage() + "\" }").build();
		}
		catch (Exception e) {
	    	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();
	    }
	}
	
	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/inscription")
	public Response inscriptionOptions( ClientLoginDB login) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/inscription")

	public Response inscription( ClientLoginDB login) {
		return inscription(login, null);
	}
	
	public Response inscription( ClientLoginDB login , String username) 
	{
		try {
			
			AccesClientLogin acceslogin = new AccesClientLogin();
			AccesClientPending accespending = new AccesClientPending();
			
			if( acceslogin.getEquals("email", login.getEmail()) != null ||
				accespending.getEquals("email", login.getEmail()) != null )
	            return Response.status(Response.Status.FOUND).entity("{ message : \"Email déjà utilisé\" }").build();
			
			String key = new RandomString(254).nextString();
			
			ClientPendingDB temp = accespending.getEquals("emailkey", key);
			
			if( temp != null) key = new RandomString(254).nextString();

			ClientPendingDB pending = new ClientPendingDB(login.getEmail(), login.getMotdepasse(), login.getFacebookuid(),username, key);
			
			accespending.insert(pending);
			
			boolean succesmail = new MailUtils().sendVerificationEmail(login.getEmail(), null, null, key );
			
			if(!succesmail)
				System.err.println("Echec d'envoi du mail de confirmation... reessayez plus tard.");
			
			return Response.ok().build();
			
		} catch (Exception e) {
        	e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();
        }
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Response updateOptions( ClientDB login) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Response update (ClientDB client ) {
		try {
			ClientDB user = (ClientDB) security.getUserPrincipal();
			if ( user == null || client == null || !user.getEmail().equals(client.getEmail()))
				throw new Exception("Acces interdit. ["+client+"] ["+user+"]");
			
			AccesClient acces = new AccesClient();
			ClientDB cltdb = acces.updateProfile(client);
			
			return Response.ok(cltdb).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).entity("{ message :\"" + e.getMessage() + "\" }").build();
		}
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updatepassword")
	public Response updatepasswordOptions( ClientLoginDB login) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updatepassword")
	public Response updatepassword (ClientLoginDB client ) {
		try {
			ClientDB user = (ClientDB) security.getUserPrincipal();
			if ( user == null || client == null || !user.getEmail().equals(client.getEmail()))
				throw new Exception("Acces interdit. ["+client+"] ["+user+"]");
			
			AccesClientLogin acces = new AccesClientLogin();
			acces.updateLogin(client);
			
			return Response.ok("{}").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).entity("{ message :\"" + e.getMessage() + "\" }").build();
		}
	}
	

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/resendEmail")
	public Response resendMailOptions( ClientLoginDB login) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/resendEmail")
	public Response resendMail( ClientLoginDB login ) 
	{
		try {
			AccesClientPending accespending = new AccesClientPending();
			String key = new RandomString(254).nextString();
			
			ClientPendingDB temp = accespending.getEquals("emailkey", key);
			
			if( temp != null)
				key = new RandomString(254).nextString();
			
			boolean succesmail = new MailUtils().sendVerificationEmail(login.getEmail(), null, null, key );
			
			if(!succesmail)
			{
				System.err.println("echec d'envoi du mail de confirmation { email : " + login.getEmail() + " , key : " + key);
				return Response.status(Response.Status.EXPECTATION_FAILED).entity("{ message : \"Echec d'envoi du mail de confirmation... reessayez plus tard.\" }").build();
			}
			return Response.ok("mail envoyé").build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).entity("{ message :\"" + e.getMessage() + "\" }").build();
		}
	}
	

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/confirmEmail/{key}")
	public Response confirmEmailOptions(@PathParam(value = "key") String key ) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/confirmEmail/{key}")
	public Response confirmEmail(@PathParam(value = "key") String key )
	{
		try {
			AccesClientPending accespending = new AccesClientPending(); //TODO READONLY
			ClientPendingDB pending = accespending.getEquals("emailkey", key);
			if(pending == null)
				throw new Exception("Verification email expirée. réessayez l'inscription");
			
			AccesClient accesclient = new AccesClient();
			AccesClientLogin acceslogin = new AccesClientLogin();
			
			String token = new RandomString().nextString();
			ClientDB client = accesclient.insert(new ClientDB(pending.getEmail(), pending.getNom(), pending.getPrenom(), token));
				
			ClientLoginDB login = new ClientLoginDB();
			
			login.setClientID(client.getClientID());
			login.setEmail(pending.getEmail());
			login.setMotdepasse(pending.getMotdepasse());
			if(pending.getFacebookuid() != null) {
				login.setFacebookuid(pending.getFacebookuid());
				client.setFbassocie(true);
			}
			
			acceslogin.insert(login);
			accespending.delete(pending);
			
			return Response.ok(client).build();
		} 
		catch (Exception e) {
			e.printStackTrace();
			return Response.status( Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();
		}
			
	}

	/* TODO 
	 * add table pendingRecupPassword 30min
	 * clean tokens , pendings after operation succes || expire
	 * reconnect after token expired
	 */

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/recuperepassword")
	public Response recuperePwdOptions( ClientLoginDB login) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/recuperepassword")
	public Response recuperePwd( ClientLoginDB login )
	{
		String email = login.getEmail();
		try {
			if(email == null || email.isEmpty())
	            return Response.status(Response.Status.NOT_FOUND).entity("{ message : \"Email introuvable\" }").build();
			
			AccesClientLogin acces = new AccesClientLogin();
			ClientLoginDB client = acces.getEquals("email", email); // READONLY
			
			if(client == null)
			{
				// System.out.println(email);
				return Response.status(Response.Status.NOT_FOUND).entity("{ message : \"Email non inscrit\" }").build();
			}
			
			String key = new RandomString(254).nextString();
			ClientLoginDB temp = acces.getEquals("recupkey", key);
			
			if( temp != null)
				key = new RandomString(254).nextString();
			
			
			boolean succesmail = new MailUtils().sendRecupPassword( email, null, null, key );
			
			if(!succesmail)
			{
				System.err.println("echec d'envoi du mail de confirmation { email : " + email + " , key : " + key);

				if(new Utils().calculCountdown(client.getRecupkeydate()) <= 0)
				{
					client.setRecupkey(null);
					client.setRecupkeydate(null);

					acces.update(client);
				}
				return Response.status(Response.Status.EXPECTATION_FAILED).
						entity("{ message : \"Echec d'envoi du mail de confirmation... reessayez plus tard.\" }").build();
			}

			client.setRecupkey(key);
			client.setRecupkeydate(new Utils().now());

			acces.update(client);
			
			return Response.ok().build();
			
		} catch (Exception e) {
			
	       	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\"}").build();       
	    }
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/validerecupmotdepasse")
	public Response isRecupCodeValideOptions( ClientLoginDB login) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/validerecupmotdepasse")
	public Response isRecupCodeValide(ClientLoginDB pwd )
	{
		try {
			AccesClientLogin acces = new AccesClientLogin();
			
			Timestamp stamp = new Timestamp(new Utils().now().getTime() - Constantes.RECUP_MDP_EXPIRE_TIME);
			List<ClientLoginDB> lold = acces.dynamiqueSelect("recupkey = ", pwd.getRecupkey(), "recupkeydate > ", stamp);
			if(lold == null || lold.isEmpty())
		        return Response.status(Response.Status.NOT_FOUND).entity("").build();
			ClientLoginDB old = lold.get(0);
			
			/*
			 * TODO add validation du mot de passe
			 	verif derniere modification de mdp once every 30min
			 */
			old.setMotdepasse(pwd.getMotdepasse());
			old.setRecupkeydate(null);
			old.setRecupkey(null);
			acces.update(old);
		} catch (Exception e) {
			
	       	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message :\"" + e.getMessage() + "\" }").build();       
	    }
		return Response.ok().build();
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/validerecupcode/{key}")
	public Response isRecupCodeValideOptions(@PathParam(value = "key") String key ) { return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/validerecupcode/{key}")
	public Response isRecupCodeValide(@PathParam(value = "key") String key )
	{
		try {
			AccesClientLogin acces = new AccesClientLogin();

			Timestamp stamp = new Timestamp(new Utils().now().getTime() - Constantes.RECUP_MDP_EXPIRE_TIME);
			List<ClientLoginDB> lold = acces.dynamiqueSelect("recupkey = ", key, "recupkeydate > ", stamp);
			if(lold == null || lold.isEmpty())
		        return Response.status(Response.Status.NOT_FOUND).entity("{ message : \"compte introuvable\" }").build();
			
		} catch (Exception e) {
			
	       	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();       
	    }
		return Response.ok().build();
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/saveadresse")
	public Response saveAdresseOptions( ClientAdresseDB login) { return Response.ok().build(); }

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/saveadresse")
	public Response saveAdresse(ClientAdresseDB adresse)
	{
		try {
			if(adresse == null) throw new Exception("Adresse vides");
			
			ClientDB user = (ClientDB) security.getUserPrincipal();
			if(user == null) throw new Exception("Non connecté");
			
			AccesClientAdresse acces = new AccesClientAdresse();
			List<ClientAdresseDB> addrs = acces.getEqualsList("clientID", user.getClientID());
			
			if(addrs != null && addrs.size() > 2 ) 
				throw new Exception("{message : 'Nombre maximum d'adresses atteint'}");
			
			adresse.setClientID(user.getClientID());
			acces.insert(adresse);
			
			return Response.ok("{}").build();
			
		} catch (Exception e) {
			
	       	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();       
	    }
    }

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateadresse")
	public Response updateAdresseOptions(List<ClientAdresseDB> adresses){ return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateadresse")
	public Response updateAdresse(List<ClientAdresseDB> adresses)
	{
		try {
			if(adresses == null || adresses.isEmpty())
				throw new Exception("Adresses vides");
			
			ClientDB user = (ClientDB) security.getUserPrincipal();
			if(user == null) throw new Exception("Non connecté");
			
			AccesClientAdresse acces = new AccesClientAdresse();

			if(adresses.size() > 1) {
				acces.update(adresses.get(0), adresses.get(1), user.getClientID());
			}
			return Response.ok("{}").build();
			
		} catch (Exception e) {
			
	       	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();       
	    }
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/selectadresse")
	public Response selectAdresseOptions(ClientAdresseDB adresses){ return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/selectadresse")
	public Response selectAdresse(ClientAdresseDB adresse)
	{
		try {
			if(adresse == null) throw new Exception("Adresse vide");
			ClientDB user = (ClientDB) security.getUserPrincipal();
			
			AccesClientAdresse acces = new AccesClientAdresse();
			adresse.setClientID(user.getClientID());
			acces.selectAdresse(adresse);

			return Response.ok("{}").build();
			
		} catch (Exception e) {
			
	       	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();       
	    }
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getadresses")
	public Response getAdressesOptions(){ return Response.ok().build(); }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getadresses")
	public Response getAdresses() 	
	{
		try {
			ClientDB user = (ClientDB) security.getUserPrincipal();
			
			AccesClientAdresse acces = new AccesClientAdresse();
			List<ClientAdresseDB> addrs = acces.getEqualsList("clientID", user.getClientID());
			return Response.ok(addrs).build();
			
		} catch (Exception e) {
			
	       	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();       
	    }
	}	


}
