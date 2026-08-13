package ma.otaku.acces.admin;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.admin.TokenDB;
import ma.otaku.utils.RandomString;

public class AccesToken extends AccesTable<TokenDB> {
	
	public AccesToken() { super(TokenDB.class); }

	@Override
	protected Query isInDBQuery(TokenDB t, EntityManager manager) throws Exception {
		return manager.createQuery("SELECT T FROM TokenDB T WHERE T.pseudo = :ps").setParameter("ps", t.getPseudo());
	}
	
	@Override
	protected void verifierDonnees(TokenDB t) throws Exception {
		
	}
	
	@Override
	protected void erreurExistant(TokenDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void deleteErrors(TokenDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	public String addToken(String username,Long id)
	{
		String s = new RandomString().nextString();
		
		try {
			super.delete("adminID", id);
			
			TokenDB tokendb = new TokenDB(id, username, s);
			
			insert(tokendb); 
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
			s = null;
		}
		
		return s;
	}

	public TokenDB isTokenValide(String token) {
		
		TokenDB s = null;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();

			Query query = manager.createQuery("SELECT T FROM TokenDB T WHERE token = :token and fin > :stamp") 
					.setParameter("stamp", utils().now())
				.setParameter("token", token).setMaxResults(1);
			
			/* 
			 * TODO and fin >= :stamp 
			 * create acces token
			 * at ping , if request with token and token_duration / 2 passed , send time with pong, 
			 * if time with pong reconnect from client 
			*/
			
			s = (TokenDB) query.getSingleResult();
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally { if(manager != null && manager.isOpen()) manager.close(); }
		
		return s;
	}
	
	public boolean cleanTokens() {
		
		try {
			List<TokenDB> tokenlist = dynamiqueSelect("fin <=", utils().now());
			for(TokenDB t : tokenlist)
				deleteObject(t);
			
		} catch ( Exception ex) {
			
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
}
