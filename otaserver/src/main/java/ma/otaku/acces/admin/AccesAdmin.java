package ma.otaku.acces.admin;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.admin.AdminDB;

public class AccesAdmin extends AccesTable<AdminDB>{

	public AccesAdmin() { super(AdminDB.class); }

	@Override
	protected Query isInDBQuery(AdminDB t, EntityManager manager) throws Exception {
		
		return manager.createQuery("SELECT A FROM AdminDB A WHERE A.CIN = :cin OR A.email = :mail")
		.setParameter("cin", t.getCIN())
		.setParameter("mail", t.getEmail())
		.setMaxResults(1);
	}

	@Override
	protected void verifierDonnees(AdminDB t) throws Exception {

		if( !v().isCINValide(t.getCIN()))
			throw new Exception("CIN invalide");
		if( !v().isEmailValide(t.getEmail()) ) 
			throw new Exception("email invalide");
	}
	
	@Override
	protected void erreurExistant(AdminDB t) throws Exception {
		throw new Exception("Admin existant. (CIN ou adresses e-mail similaires)");
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		throw new Exception("Admin inéxistant.");
	}

	@Override
	protected void deleteErrors(AdminDB t) throws Exception {
	}

}
