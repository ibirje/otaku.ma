package ma.otaku.acces.admin;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.admin.AdminRoleDB;

public class AccesAdminRole extends AccesTable<AdminRoleDB>{

	public AccesAdminRole() {
		super(AdminRoleDB.class);
	}

	@Override
	protected Query isInDBQuery(AdminRoleDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(AdminRoleDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void erreurExistant(AdminRoleDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(AdminRoleDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}
	public ConcurrentHashMap<Long,AdminRoleDB> getRolesMap(List<AdminRoleDB> roles) 
	{
		if(v().isNullOrEmpty(roles)) return null;
		ConcurrentHashMap<Long,AdminRoleDB> map = new ConcurrentHashMap<>();
		roles.forEach(r -> map.put(r.getAdminRoleID(), r));
		return map;
	}
}
