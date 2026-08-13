package ma.otaku.acces.client;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.client.CommandeItemDB;

public class AccesCommandeItem extends AccesTable<CommandeItemDB> {


	public AccesCommandeItem() {
		super(CommandeItemDB.class);
	}

	public AccesCommandeItem(byte param) {
		super(CommandeItemDB.class, param);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected Query isInDBQuery(CommandeItemDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(CommandeItemDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurExistant(CommandeItemDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(CommandeItemDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
