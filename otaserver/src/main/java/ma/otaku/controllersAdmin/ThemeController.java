package ma.otaku.controllersAdmin;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ma.otaku.acces.produit.AccesTheme;
import ma.otaku.data.type.ThemeDB;

@Path("/themes")
public class ThemeController {

	@Context ServletContext context;

	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("CATEG_SELECT")
	public Collection<ThemeDB> getListe() throws NamingException
	{	
		return getCache().values();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("THEME_INSERT")
	@Path("/insert")
	public Response insert( ThemeDB theme ) {
		
		try {
			if( theme == null ) throw new NullPointerException("theme null");
			ThemeDB ntheme = new AccesTheme().insert(theme);
			return Response.ok(ntheme).build();
			
		} catch (Exception e) {
			
	    	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
	    }
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("THEME_DELETE")
	@Path("/delete")
	public Response delete( ThemeDB theme ) {
		
		try {
			if( theme == null ) throw new NullPointerException("theme null ( données à supprimer invalides )");
			ThemeDB deleted = new AccesTheme().delete(theme);
			return Response.ok(deleted).build();
			
		} catch (Exception e) {
			
	    	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
	    }
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("THEME_UPDATE")
	@Path("/update")
	public Response update( ThemeDB theme ) {
		
		try {
			if( theme == null ) throw new NullPointerException("theme null");
			AccesTheme acces = new AccesTheme();
			ThemeDB newc = acces.getByCode(theme.getCode());
			if(newc == null) throw new Exception("Theme " + theme.getCode() + " introuvable");
			if(newc.getNom().equals(theme.getNom()) && newc.getSmallImage()!= null && newc.getSmallImage().equals(theme.getSmallImage()))
				return Response.ok(newc).build();
			
			if(theme.getNom() != null) newc.setNom(theme.getNom());
			if(theme.getSmallImage() != null) newc.setSmallImage(theme.getSmallImage());
			
			ThemeDB updated = acces.update(newc);
			
			return Response.ok(updated).build();
			
		} catch (Exception e) {
			
	    	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
	    }
	}
	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<Long,ThemeDB> getCache() {

		return (ConcurrentHashMap<Long, ThemeDB>) context.getAttribute("themes");
	}
}
