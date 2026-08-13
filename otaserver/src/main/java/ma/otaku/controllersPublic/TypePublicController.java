package ma.otaku.controllersPublic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.acces.produit.AccesProduits;
import ma.otaku.acces.produit.AccesProduitsClient;
import ma.otaku.data.dto.CategorieProduitDTO;
import ma.otaku.data.produit.ProduitClient;
import ma.otaku.data.type.CategorieDB;
import ma.otaku.data.type.ThemeDB;

@Path("public/type")
public class TypePublicController {


	@Context ServletContext context;
	

	@GET
	@Path("/categlist")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<CategorieDB> getCategoriesListe() throws NamingException
	{
		return getCategoriesCache().values();
	}	
	
	@GET
	@Path("/suggestionSection")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CategorieProduitDTO> getSuggestionSectionCategoriesAndProducts() {
		

		AccesCategorie accateg = new AccesCategorie(AccesCategorie.READONLY);
		List<CategorieDB> cats =  accateg.getSuggestionSectionCategories();

		List<CategorieProduitDTO> categprods = new ArrayList<>();
		if( cats == null || cats.isEmpty())
			return categprods;

		AccesProduitsClient accesProduit = new AccesProduitsClient(AccesProduits.READONLY);
		for(CategorieDB cat : cats) {
			List<ProduitClient> prods = accesProduit.findTopSalesByCategorieId(cat.getCategorieID());
			
			categprods.add(new CategorieProduitDTO(cat, prods));
		}
	
		return categprods;
	}
	
	
	
	@GET
	@Path("/themelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ThemeDB> getThemesListe() throws NamingException
	{	
		return getThemesCache().values();
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<Long,ThemeDB> getThemesCache() {

		return (ConcurrentHashMap<Long, ThemeDB>) context.getAttribute("themes");
	}
	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<Long,CategorieDB> getCategoriesCache() {

		return (ConcurrentHashMap<Long, CategorieDB>) context.getAttribute("categories");
	}
}
