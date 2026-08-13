package ma.otaku.controllersPublic;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import ma.otaku.acces.client.AccesPanier;
import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.acces.produit.AccesProduits;
import ma.otaku.acces.produit.AccesProduitsClient;
import ma.otaku.acces.produit.AccesTheme;
import ma.otaku.acces.produit.AccesVariations;
import ma.otaku.business.client.BeanPanierProduit;
import ma.otaku.business.produits.BeanProduitInformations;
import ma.otaku.business.produits.Categorie;
import ma.otaku.business.produits.IndexProduits;
import ma.otaku.business.produits.ProduitSuggestion;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.PanierDB;
import ma.otaku.data.dto.CategorieProduitDTO;
import ma.otaku.data.produit.ProduitClient;
import ma.otaku.data.produit.VariationDB;
import ma.otaku.data.type.CategorieDB;
import ma.otaku.data.type.ThemeDB;
import ma.otaku.utils.Constantes;

@Path("public/produits")
public class ProduitPublicController {

	@Context SecurityContext security;
	

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/ajoutpanier")
	public Response ajoutPanierOptions(PanierDB cmd){ return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/ajoutpanier")
	public Response ajoutPanier( PanierDB panier ) {
		
		try {
			
			ClientDB client = (ClientDB) security.getUserPrincipal();
			new AccesPanier().ajoutPanier(client, panier);
			return Response.ok(panier).build();
			
		} catch (Exception e) {
	    	e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(" { message : \""+e.getMessage()+"\" }").build();
	    }
	}


	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deletepanier")
	public Response deletePanierOptions(PanierDB cmd){ return Response.ok().build(); }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deletepanier")
	public Response deletePanier( PanierDB panier ) {
		
		try {
			
			ClientDB client = (ClientDB) security.getUserPrincipal();
			new AccesPanier().delete(panier.getCode(), client.getClientID());
			return getPanier();
			
		} catch (Exception e) {
			
	    	e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(" { message : \""+e.getMessage()+"\" }").build();
	    }
	}



	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getpanier")
	public Response getPanierOptions(){ return Response.ok().build(); }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getpanier")
	public Response getPanier() {
		try {
			ClientDB client = (ClientDB) security.getUserPrincipal();
			// System.out.println("get panier " +client.getEmail());
			List<PanierDB> paniers = new AccesPanier().getEqualsList("clientID", client.getClientID());
			
			if(paniers == null || paniers.isEmpty())
				return Response.ok("{}").build();

			AccesProduitsClient accesprod = new AccesProduitsClient(AccesProduits.READONLY);
			AccesVariations accesvar = new AccesVariations();
			
			List<BeanPanierProduit> liste = new ArrayList<>();
			for( PanierDB panier : paniers ) {
				BeanPanierProduit bean = new BeanPanierProduit();
				bean.setPanier(panier);
				bean.setProduit(accesprod.getByID(panier.getProduitID()));
				if(panier.getVariationID() != null)
					bean.setVariation(accesvar.getByID(panier.getVariationID()));
				liste.add(bean);
			}
			return Response.ok(liste).build();
			
		} catch (Exception e) {
			
	    	e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(" { message : \""+e.getMessage()+"\" }").build();
	    }
	}

	@GET
	@Path("/suggestions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProduitSuggestion> getSuggestions (@QueryParam( "text" ) String  text ) {
		AccesProduitsClient accesProduit = new AccesProduitsClient(AccesProduits.READONLY);
		return accesProduit.getSearchSuggestions(text);
	}


	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProduitClient> getListe (
		@QueryParam(  "nom"   ) String  nom      , @QueryParam(  "page"  ) Integer page,
		@QueryParam("ispromo" ) String  typepromo, @QueryParam("trifiltre") String  trifiltre,
		@QueryParam("prixmin" ) Double  prixmin  , @QueryParam("prixmax"  ) Double  prixmax,
		@QueryParam("categorie") String categorie, @QueryParam(  "theme"  ) String theme ) throws NamingException
	{
		
		if (categorie != null) categorie = categorie.replace('-', ' ').trim();
		if (theme != null) theme = theme.replace('-', ' ').trim();
		
		int size = Constantes.DEFAULT_LIST_COUNT;
		page = page == null || page < Constantes.MIN_PAGE ? Constantes.DEFAULT_PAGE : page;
		
		trifiltre = trifiltre == null ? "TriFinPromo" : // TODO pertinence = tris avis ?
			trifiltre.equals("date") ? null :
				trifiltre.equals("prix") ? "TriPrixUnite" : "TriDateAsc"; // TODO add commandes to produitDB, stats, schedule, then add tricommandes
		
		AccesProduitsClient accesProduit = new AccesProduitsClient(AccesProduits.READONLY);
		
		List<ProduitClient> liste = accesProduit.getListeProduits(nom, null, typepromo, trifiltre, prixmin, 
			prixmax, null, null, null, null, page, size, categorie, theme, true);
		
		// liste.forEach( p ->  System.out.println(p.getCode()) );
		
		return liste;
	}
	

	
	@GET
	@Path("/listcount")
	@Produces(MediaType.APPLICATION_JSON)
	public Long getListeCount (
		@QueryParam( "nom" ) String nom, @QueryParam("prixmin" ) Double  prixmin  , 
		@QueryParam( "prixmax" ) Double prixmax, @QueryParam("categorie") String categorie, 
		@QueryParam( "theme" ) String theme ) throws NamingException
	{
		if (categorie != null) categorie = categorie.replace('-', ' ').trim();
		if (theme != null) theme = theme.replace('-', ' ').trim();
		int qtemin = (int) Constantes.MIN_QTE_CLIENT;

		AccesProduitsClient accesProduit = new AccesProduitsClient( AccesProduits.READONLY);
		return accesProduit.getListeCount(nom, prixmin, prixmax, qtemin, categorie, theme, true);
	}
	
	/* TODO
	 * creer token (change to jwt ) ?
	 * 
	 * TODO
	 */
	
	
	/*
	 * 1 _ STOCK     ( inventaire , arrenger , emmagasiner , preparer produit sur demande)
	 * 2 _ PACKAGING ( verifier produit , preparer emballage , imprimer facture , preparer colis )
	 * 3 _ LIVRAISON ( verifier la facture, deplacement amana avant 12h ? , livrer , ramener le recu )
	 * 
	 * 1 => 2 => 3
	 * 
	 */
	
	@GET
	@Path("/fullproduit")
	@Produces(MediaType.APPLICATION_JSON)
	public BeanProduitInformations getProduitInformations(@QueryParam("nom") String nom) throws UnsupportedEncodingException
	{
		if(nom == null || nom.isEmpty() || nom.length() < Constantes.MIN_NOM_SIZE)
			throw new NullPointerException("Produit nom invalide.");
		AccesProduitsClient accesprod = new AccesProduitsClient(AccesProduits.READONLY);
		ProduitClient produit = accesprod.getProduitByNom(nom.replaceAll("-", " "));
		
		
		if(produit == null) throw new NullPointerException("Produit" + nom + " introuvable.");
		
		List<ProduitClient> suggestions = accesprod.getSuggestionList(produit);
		
		List<Categorie> branche = new AccesCategorie().getBrancheCategories(produit.getCategorieID());
		ThemeDB theme = new AccesTheme().getByID(produit.getThemeID());
		
		BeanProduitInformations data = new BeanProduitInformations();
		data.setProduit(produit);
		data.setCategories(branche);
		data.setTheme(theme);
		data.setSuggestions(suggestions);
		
		if(produit.getHasVariations())
		{ 
			AccesVariations accesVariations = new AccesVariations();
			List<VariationDB> variationsdb = accesVariations.getVariationsDBByProduitCode(produit.getCode());

			Iterator<VariationDB> i = variationsdb.iterator();
			
			VariationDB v;
			while(i.hasNext()) 
			{	
				v = i.next();
				if(v.getQte() <= 0) i.remove();
			}
					
			data.setVariations(accesVariations.getListeVariationOptions(variationsdb));
			data.setAttributs(accesVariations.getAttributsOptionsDBByCode(produit.getCode()));
		}
		
		return data;
	}
	
	@GET
	@Path("/homeproduits")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHomeProduits() {
		try {
			AccesProduitsClient acces = new AccesProduitsClient(AccesProduits.READONLY);

			List<ProduitClient> promos = acces.getListeProduits(null, null, "promo", "TriPromo", null, 
				null, null, null, null, null, Constantes.DEFAULT_PAGE, 4, null, null, true);

			List<ProduitClient> news = acces.getListeProduits(null, null, null, null, null, 
				null, null, null, null, null, Constantes.DEFAULT_PAGE, 16, null, null, true);

			IndexProduits pr = new IndexProduits();
			pr.setPromos(promos);
			pr.setNews(news);
			
			return Response.ok(pr).build();
		} 
		catch (Exception e) {
	    	e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(" { message : \""+e.getMessage()+"\" }").build();
	    }
	}

	@GET
	@Path("/newproduits/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHomeProduits(@PathParam("page") Integer page ) {
		try {
			if(page == null) page = Constantes.DEFAULT_PAGE;
			
			AccesProduitsClient acces = new AccesProduitsClient(AccesProduits.READONLY);

			List<ProduitClient> news = acces.getListeProduits(null, null, null, null, null, 
				null, null, null, null, null, page, 8, null, null, true);
			
			return Response.ok(news).build();
		} 
		catch (Exception e) {
	    	e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(" { message : \""+e.getMessage()+"\" }").build();
	    }
	}
}
