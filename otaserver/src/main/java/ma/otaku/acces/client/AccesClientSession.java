package ma.otaku.acces.client;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.client.ClientSessionDB;

public class AccesClientSession extends AccesTable<ClientSessionDB>{

	public AccesClientSession() 
	{
		super(ClientSessionDB.class);
	}
	
	@Override
	protected Query isInDBQuery(ClientSessionDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(ClientSessionDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public ClientSessionDB delete(ClientSessionDB obj) throws Exception {
		return super.delete("cookie", obj.getCookie());
	}

	@Override
	protected void erreurExistant(ClientSessionDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(ClientSessionDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
