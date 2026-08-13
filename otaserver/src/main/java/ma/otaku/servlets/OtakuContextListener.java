package ma.otaku.servlets;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ma.otaku.acces.admin.AccesAdminLogin;
import ma.otaku.acces.admin.AccesAdminRole;
import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.acces.produit.AccesTheme;
import ma.otaku.data.admin.AdminRoleDB;
import ma.otaku.data.type.CategorieDB;
import ma.otaku.data.type.ThemeDB;
import ma.otaku.utils.Build;


public class OtakuContextListener implements ServletContextListener {

    public static ServletContext context;
	
    public OtakuContextListener() {}

    public void contextDestroyed(ServletContextEvent e)  { 
    	EntityManagerFactory factory = (EntityManagerFactory) context.getAttribute("factory");
    	factory.close();
    }

    public void contextInitialized(ServletContextEvent e)  { 
    	try {
            context = e.getServletContext();
    		EntityManagerFactory factory = Persistence.createEntityManagerFactory(Build.PERSISTENCE);
    		
    		context.setAttribute("factory",factory);

    		/* ****** cache thèmes et catégories ****** */
    		List<ThemeDB> themes = new AccesTheme(AccesTheme.READONLY).getList();
    		List<CategorieDB> categories = new AccesCategorie(AccesCategorie.READONLY).getList();

    		context.setAttribute("categories", getCategoriesIDMap(categories));
    		context.setAttribute("themes", getThemesIDMap(themes));

    		context.setAttribute("categoriesnom", getCategoriesNomMap(categories));
    		context.setAttribute("themesnom", getThemesNomMap(themes));

    		/* ****** cache droits admin ****** */
    		
    		AccesAdminRole acces = new AccesAdminRole();
    		List<AdminRoleDB> roles = acces.getList();

    		context.setAttribute("roles", acces.getRolesMap(roles));
    		context.setAttribute("droits", new AccesAdminLogin().rolesDroitsMap(roles));
    	    
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }

    

	private ConcurrentHashMap<String, ThemeDB> getThemesNomMap(List<ThemeDB> themes) {
		
		ConcurrentHashMap<String,ThemeDB> thememap = new ConcurrentHashMap<>();
		themes.forEach(theme -> thememap.put(theme.getNom(), theme) );
		
		return thememap;
	}

	private ConcurrentHashMap<String, CategorieDB> getCategoriesNomMap(List<CategorieDB> categories) {
		
		ConcurrentHashMap<String, CategorieDB> categmap = new ConcurrentHashMap<>();
		categories.forEach(cat -> categmap.put(cat.getNom(), cat));
		
		return categmap;
	}

	private ConcurrentHashMap<Long, ThemeDB> getThemesIDMap(List<ThemeDB> themes) {
		
		ConcurrentHashMap<Long,ThemeDB> thememap = new ConcurrentHashMap<>();
		themes.forEach(theme -> thememap.put(theme.getThemeID(), theme) );
		
		return thememap;
	}

	private ConcurrentHashMap<Long, CategorieDB> getCategoriesIDMap(List<CategorieDB> categories) {
		
		ConcurrentHashMap<Long,CategorieDB> categmap = new ConcurrentHashMap<>();
		categories.forEach(cat -> categmap.put(cat.getCategorieID(), cat) );
			
		return categmap;
	}
	
}
