package ma.otaku.acces.client;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.client.ClientPendingDB;
import ma.otaku.utils.Constantes;

public class AccesClientPending extends AccesTable<ClientPendingDB>{

	public AccesClientPending() {
		super(ClientPendingDB.class);
	}
	public AccesClientPending( byte mode ) {
		super(ClientPendingDB.class, mode);
	}

	@Override
	protected Query isInDBQuery(ClientPendingDB t, EntityManager manager) throws Exception {

		return manager.createQuery("SELECT C FROM ClientPendingDB C WHERE email = :email "
				+ "OR ( nom = :nom AND prenom = :prenom )")
				.setParameter("email", t.getEmail())
				.setMaxResults(1);
	}

	@Override
	protected void verifierDonnees(ClientPendingDB t) throws Exception 
	{
		if( t == null ) throw new NullPointerException("client null");
		if( v().isNullOrEmpty(t.getEmail()))throw new NullPointerException("email null");
		if( v().isNullOrEmpty(t.getMotdepasse()))throw new NullPointerException("password null");
		if( v().isNullOrEmpty(t.getEmailkey()))throw new NullPointerException("clé null");
	}

	public ClientPendingDB delete(ClientPendingDB obj) throws Exception {
		return super.delete("emailkey", obj.getEmail());
	}

	@Override
	protected void erreurExistant(ClientPendingDB t) throws Exception {
		throw new Exception("ClientPending existant");
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(ClientPendingDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}
	public ClientPendingDB existe(String email, String nom, String prenom)
	{
		ClientPendingDB clt = new ClientPendingDB();
		
		clt.setEmail(email);
		
		return isObjectInDB(clt);
	}
	
	public ClientPendingDB insert(ClientPendingDB client) throws Exception
	{
		ClientPendingDB pending = super.insert(client);
		
		Timestamp stamp = new Timestamp(utils().now().getTime() - Constantes.PENDING_KEY_EXPIRE_TIME);
		
		List<ClientPendingDB> list = getLessThan("dateInsertion", stamp);
		
		for(ClientPendingDB pend : list)
			deleteObject(pend);
		
		return pending;
		
	}
	public ClientPendingDB getValidPending(String key)
	{
		ClientPendingDB pending = getEquals("emailkey", key);

		Timestamp stamp = new Timestamp(utils().now().getTime() - Constantes.PENDING_KEY_EXPIRE_TIME);

		if(pending != null && pending.getDateInsertion().before(stamp) )
		{
			try { deleteObject(pending); } catch( Exception ex ) { }
			
			return null;
		}
		return pending;
	}
	public void clean() throws Exception {
		Timestamp stamp = new Timestamp(utils().now().getTime() - Constantes.PENDING_KEY_EXPIRE_TIME);
		List<ClientPendingDB> pending = getGreaterThan("dateInsertion", stamp);
		delete(pending);
	}
	
}



