package ma.otaku.acces.client;

import java.security.AccessControlException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ValidationException;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.ClientLoginDB;
import ma.otaku.utils.RandomString;

public class AccesClientLogin extends AccesTable<ClientLoginDB>{

	public AccesClientLogin() {
		super(ClientLoginDB.class);
	}

	public AccesClientLogin( byte mode ) {
		super(ClientLoginDB.class, mode);
	}

	@Override
	protected Query isInDBQuery(ClientLoginDB t, EntityManager manager) throws Exception {
		return manager.createQuery("SELECT C FROM ClientLoginDB C WHERE clientID = :clientid")
				.setParameter("clientid", t.getClientID())
				.setMaxResults(1);
	}
	
	@Override
	protected void verifierDonnees(ClientLoginDB t) throws Exception {
		if( t == null ) throw new NullPointerException("Client Login NULL");
		if( v().isNotSupZero(t.getClientID()) ) throw new NullPointerException("ClientID de client login NULL");
		if(!v().isEmailValide(t.getEmail())) throw new NullPointerException("Email client login NULL");
		if( v().isNullOrEmpty(t.getMotdepasse())) throw new NullPointerException("mot de passe client login NULL");
	}

	@Override
	public ClientLoginDB update(ClientLoginDB t) throws Exception {
		verifierDonnees(t);
		return super.update(t);
	}

	@Override
	protected void erreurExistant(ClientLoginDB t) throws Exception {
		throw new Exception("Login client existant.");
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(ClientLoginDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}
	public ClientDB connect(String pseudo, String password) throws Exception
	{
		ClientLoginDB login = getEquals("email", pseudo);
		
		if(login == null || !login.getMotdepasse().equals(password))
		{
			throw new AccessControlException("Identifiants incorrects");
		}
		
		AccesClient accesClient = new AccesClient();
		ClientDB client =  accesClient.getByID(login.getClientID());
		
		if( client == null ) return null;
		
		String token = new RandomString().nextString();
		client.setToken(token);
		accesClient.update(client);
		
		return client;
	}

	public ClientLoginDB updateLogin(ClientLoginDB client) throws Exception {
		
		ClientLoginDB login;
		try {
			
			login = getEquals("email", client.getEmail());

			if( !v().isMotdePasseValide(client.getNouveau())) 
				throw new ValidationException("Nouveau mot de passe invalide");
			
			if( !client.getMotdepasse().equals(login.getMotdepasse())) 
				throw new ValidationException("Mot de passe erroné " + 
			client.getMotdepasse() + " " + login.getMotdepasse());
			
			login.setMotdepasse(client.getNouveau());
			return update(login);
		} 
		catch (ValidationException v) {
			/*
			 * 3 essaies de changement de mot de passe par jour
			 * update login.updatecount-- {refresh 3 at 12:00 or persist date & refresh at login ? } 
			 */
			throw v;
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	public void clean() {
		// TODO Auto-generated method stub
		
	}
	
	
}
