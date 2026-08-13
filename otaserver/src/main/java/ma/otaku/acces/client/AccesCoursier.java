package ma.otaku.acces.client;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.client.CoursierDB;

public class AccesCoursier extends AccesTable<CoursierDB> {

	public AccesCoursier() {
		super(CoursierDB.class);
		// TODO Auto-generated constructor stub
	}

	public AccesCoursier(byte param) {
		super(CoursierDB.class, param);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Query isInDBQuery(CoursierDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(CoursierDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurExistant(CoursierDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(CoursierDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
