package ma.otaku.acces.client;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.client.CoursierClient;

public class AccesCoursierClient extends AccesTable<CoursierClient> {

	public AccesCoursierClient() {
		super(CoursierClient.class);
		// TODO Auto-generated constructor stub
	}

	public AccesCoursierClient(byte param) {
		super(CoursierClient.class, param);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Query isInDBQuery(CoursierClient t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(CoursierClient t) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void erreurExistant(CoursierClient t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(CoursierClient t) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
