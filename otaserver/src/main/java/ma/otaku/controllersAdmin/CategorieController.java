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

import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.data.type.CategorieDB;

@Path("/categories")
public class CategorieController {
	
	@Context ServletContext context;

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("CATEG_SELECT")
	public Collection<CategorieDB> getListe() throws NamingException
	{
		return getCategoriesCache().values();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("CATEG_INSERT")
	@Path("/insert")
	public Response insert( CategorieDB cat ) {
		
		try {
			if( cat == null ) throw new NullPointerException("categorie null");
			CategorieDB ncat = new AccesCategorie().insert(cat);
			return Response.ok(ncat).build();
			
		} catch (Exception e) {
			
	    	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
	    }
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("CATEG_DELETE")
	@Path("/delete")
	public Response delete( CategorieDB cat ) {
		
		try {
			if( cat == null ) throw new NullPointerException("categorie null ( données à supprimer invalides )");
			CategorieDB deleted = new AccesCategorie().delete(cat);
			return Response.ok(deleted).build();
			
		} catch (Exception e) {
			
	    	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
	    }
	}
	
	
	
	
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("CATEG_UPDATE")
	@Path("/update")
	public Response update( CategorieDB cat ) {
		
		try {
			if( cat == null ) throw new NullPointerException("categorie null");
			AccesCategorie acces = new AccesCategorie();
			CategorieDB newc = acces.getByCode(cat.getCode());
			if(newc == null) throw new Exception("categorie " + cat.getCode() + " introuvable");
			if(newc.getNom().equals(cat.getNom()) && newc.getLargeImage() !=null && newc.getLargeImage().equals(cat.getLargeImage()))
				return Response.ok(newc).build();
			
			if(cat.getNom() != null) newc.setNom(cat.getNom());
			if(cat.getLargeImage() != null) newc.setLargeImage(cat.getLargeImage());
			
			CategorieDB updated = acces.update(newc);
			
			return Response.ok(updated).build();
			
		} catch (Exception e) {
			
	    	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
	    }
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<Long,CategorieDB> getCategoriesCache() {

		return (ConcurrentHashMap<Long, CategorieDB>) context.getAttribute("categories");
	}
}
