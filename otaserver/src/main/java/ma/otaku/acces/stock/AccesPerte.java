package ma.otaku.acces.stock;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.stock.PerteDB;

public class AccesPerte extends AccesTable<PerteDB> {

	public AccesPerte() {
		super(PerteDB.class);
		/*
		 * triggers
		 */
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Query isInDBQuery(PerteDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(PerteDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurExistant(PerteDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(PerteDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
