package ma.otaku.acces.stock;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.stock.CasseDB;

public class AccesCasse extends AccesTable<CasseDB> {

	public AccesCasse() {
		super(CasseDB.class);
		/*
		 * add casse triggers
		 */
	}

	@Override
	protected Query isInDBQuery(CasseDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(CasseDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurExistant(CasseDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(CasseDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
