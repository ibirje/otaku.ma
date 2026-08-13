package ma.otaku.acces.admin;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.admin.AdminDB;
import ma.otaku.data.admin.AdminLoginDB;
import ma.otaku.data.admin.AdminRoleDB;
import ma.otaku.data.admin.TokenDB;
import ma.otaku.utils.RandomString;

public class AccesAdminLogin extends AccesTable<AdminLoginDB>{

	public AccesAdminLogin() {
		super(AdminLoginDB.class);
	}

	public Long loginAdmin(String username, String password)
	{
		Long s = null;
		EntityManager manager = null;
		try {
			
			manager = getFactory().createEntityManager();
			
			Query query = manager.createQuery("SELECT adminID FROM AdminLoginDB where pseudo = :username AND password = :pwd")
				.setParameter("username", username)
				.setParameter("pwd", password)
				.setMaxResults(1);

			s = (Long) query.getSingleResult();
			
		}catch(Exception e)
		{
			if (manager != null && manager.getTransaction().isActive()) manager.getTransaction().rollback();
			e.printStackTrace();
			s = null;
		}
		finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return s;
	}
	
	public String addToken(String username,Long id)
	{
		RandomString rand = new RandomString();
		String s = rand.nextString();
		EntityManager manager = null;
		
		try {
			manager = getFactory().createEntityManager();
			manager.getTransaction().begin();
			
			Query query = manager.createQuery("SELECT T FROM TokenDB T WHERE adminID = :id")
				.setParameter("id", id);
			
			@SuppressWarnings("unchecked")
			List<TokenDB> tokenlist = (List<TokenDB>) query.getResultList();
			for(TokenDB t : tokenlist)
				manager.remove(t);
			
			TokenDB tokendb = new TokenDB(id, username, s);
			manager.persist(tokendb);
			
			manager.getTransaction().commit();
			
		}catch(Exception e)
		{
			if (manager != null && manager.getTransaction().isActive()) manager.getTransaction().rollback();
			e.printStackTrace();
			s = null;
		}
		finally {
			if(manager!= null && manager.isOpen()) manager.close();
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
		}catch(Exception e)
		{
			if (manager != null && manager.getTransaction().isActive()) manager.getTransaction().rollback();
			e.printStackTrace();
			s = null;
		}
		finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		
		return s;
	}
	
	
	@SuppressWarnings("unchecked")
	public HashSet<String> getRoles(Long id)
	{
		HashSet<String> dump = new HashSet<>();
		List<String> ls = null;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();
			manager.getTransaction().begin();
			
			String reqparent = 
				"SELECT DISTINCT parent.code FROM Admin_droitDB parent " + 
				"JOIN Admin_roleDroitDB ard ON ard.adminDroitID = parent.adminDroitID " + 
				"JOIN AdminDB ad ON ad.adminRoleID = ard.adminRoleID "+
				"where ad.adminID = :id ";
			Query querydroitsp = manager.createQuery(reqparent).setParameter("id", id);
			
			ls =(List<String>) querydroitsp.getResultList();
			
			manager.getTransaction().commit();
			
			dump.addAll(ls);
			
		while(ls != null && !ls.isEmpty())
		{	
			List<String> enf = getSousDroits(ls,dump,manager);
			if( enf != null && !enf.isEmpty()) dump.addAll(enf);
			ls = enf;
		}
		
		}catch(Exception e)
		{
			if (manager != null && manager.getTransaction().isActive()) manager.getTransaction().rollback();
			e.printStackTrace();
			dump = null;
		}
		finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		
		return dump;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getSousDroits(List<String> oldls, HashSet<String> dump,EntityManager manager) {
		
		List<String> ls = null;
		try 
		{
			String req = "SELECT DISTINCT fils.code FROM Admin_droitDB fils " + 
					"JOIN DroitUseDroitDB dud ON dud.usedDroitID = fils.adminDroitID " + 
					"JOIN Admin_droitDB parent ON parent.adminDroitID = dud.adminDroitID "+
					"WHERE parent.code IN (:list) AND fils.code NOT IN (:dump)";
			Query query = manager.createQuery(req)
					.setParameter("list", oldls)
					.setParameter("dump", dump);
			ls = (List<String>) query.getResultList();
		}catch(Exception e)
		{
			ls = null;
		}

		return ls;
	}

	public AdminLoginDB getAdmin(Long id)
	{
		AdminLoginDB logindb = getEquals("adminID", id);

		AdminDB admin = new AccesAdmin().getByID(id);
		logindb.setDroits(droitsCache().get(admin.getAdminRoleID()));
		logindb.setRole(rolesCache().get(id).getCode());
		return logindb;
		
	}

	@Override
	protected Query isInDBQuery(AdminLoginDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(AdminLoginDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void erreurExistant(AdminLoginDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(AdminLoginDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}
/* ************************** cache stuff *************************** */
	/**
	 * 
	 * @return ConcurrentHashMap : Long adminRoleID, HashSet<String> droits associés
	 */
	public ConcurrentHashMap<Long,HashSet<String>> rolesDroitsMap()
	{
		List<AdminRoleDB> roles = new AccesAdminRole().getList();
		if(v().isNullOrEmpty(roles)) return null;
		
		ConcurrentHashMap<Long,HashSet<String>> map = new ConcurrentHashMap<>();
		
		for(AdminRoleDB role : roles)
			map.put(role.getAdminRoleID(), getRoleDroits(role.getAdminRoleID())); 
		
		return map;
	}
	public ConcurrentHashMap<Long,HashSet<String>> rolesDroitsMap(List<AdminRoleDB> roles)
	{
		if(v().isNullOrEmpty(roles)) return null;
		
		ConcurrentHashMap<Long,HashSet<String>> map = new ConcurrentHashMap<>();
		
		for(AdminRoleDB role : roles)
			map.put(role.getAdminRoleID(), getRoleDroits(role.getAdminRoleID())); 
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private HashSet<String> getRoleDroits(Long id) {
		HashSet<String> dump = new HashSet<>();
		List<String> ls = null;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();

			String reqparent = "SELECT DISTINCT parent.code FROM Admin_droitDB parent " + 
				"JOIN Admin_roleDroitDB ard ON ard.adminDroitID = parent.adminDroitID " + 
				"WHERE ard.adminRoleID = :id ";
			Query querydroitsp = manager.createQuery(reqparent).setParameter("id", id);
			
			ls = querydroitsp.getResultList();
			
			dump.addAll(ls);
			
		while(ls != null && !ls.isEmpty())
		{	
			List<String> fils = getSousDroits(ls,dump,manager);
			if( fils != null && !fils.isEmpty()) dump.addAll(fils);
			ls = fils;
		}
		
		}catch(Exception e)
		{
			if (manager != null && manager.getTransaction().isActive()) manager.getTransaction().rollback();
			e.printStackTrace();
			dump = null;
		}
		finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return dump;
	}
	
	@SuppressWarnings("unchecked")
	public ConcurrentHashMap<Long,AdminRoleDB> rolesCache() {
		return (ConcurrentHashMap<Long,AdminRoleDB>) getContextAttribute("roles");
	}
	
	@SuppressWarnings("unchecked")
	public ConcurrentHashMap<Long,HashSet<String>> droitsCache() {
		return (ConcurrentHashMap<Long,HashSet<String>>) getContextAttribute("droits");
	}
	
	/*
SELECT ad.nom, fils.code as droit, parent.code as papa 
FROM admin_droit fils
JOIN DroitUseDroit dd ON dd.usedDroitID = fils.adminDroitID
JOIN admin_droit parent ON dd.adminDroitID = parent.adminDroitID 
JOIN admin_roledroit ard ON ard.adminDroitID = parent.adminDroitID
JOIN admin ad ON ad.adminRoleID = ard.adminRoleID
GROUP BY droit

	 * 
	 * 
	 */
}








