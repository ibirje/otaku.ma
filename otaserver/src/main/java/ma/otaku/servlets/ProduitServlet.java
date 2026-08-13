package ma.otaku.servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.acces.produit.AccesProduitsClient;
import ma.otaku.acces.produit.AccesTheme;
import ma.otaku.acces.produit.AccesVariations;
import ma.otaku.business.produits.Categorie;
import ma.otaku.business.produits.OptionAttribut;
import ma.otaku.business.produits.Produit;
import ma.otaku.business.produits.Variation;
import ma.otaku.data.produit.ProduitClient;
import ma.otaku.data.produit.VariationDB;
import ma.otaku.data.type.ThemeDB;
import ma.otaku.utils.Constantes;
// @WebServlet("/items/*")
public class ProduitServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    public ProduitServlet() {
        super();
    }
    @Override
    public void init() throws ServletException {
    	
    	super.init();
    }
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	doGet(req, resp);
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String produitnom = request.getPathInfo();
		Timestamp reqdate = new Timestamp(System.currentTimeMillis());
		
		System.err.println("- "+reqdate.toString()+"-- "+request.getRemoteAddr()+" produit"+produitnom+" ---------------------");
		
		if(produitnom != null)
			produitnom = produitnom.substring(1).replaceAll("-", " ");
		
		AccesProduitsClient accesProduit     = new AccesProduitsClient() ;
    	AccesCategorie accesCategorie = new AccesCategorie();
    	AccesTheme  accesTheme         = new AccesTheme();
	    AccesVariations accesVariation = new AccesVariations();
	    
    	
    	ProduitClient produitDB = accesProduit.getProduitByNom(produitnom);

    	if(produitDB == null )
    	{
    		request.getRequestDispatcher("/error_produit_not_found.jsp").forward(request, response);
    		return;
    	}
		List<Categorie> branche = accesCategorie.getBrancheCategories(produitDB.getCategorieID());
		
		Produit produit = new Produit(produitDB);
		
		produit.setStars(accesProduit.getProduitNote(produitDB));
		produit.setAvis(accesProduit.getNombreAvis(produitDB));
		produit.setCommandes(accesProduit.getNombreCommandes(produitDB));
		
    	List<Produit> pliste = accesProduit.getRelatedProdList(produitDB);
    	ThemeDB theme = accesTheme.getByID(produitDB.getThemeID());
    	 
    	if( produit.getQTE() > Constantes.MIN_QTE_CLIENT )
    	{
    		if(produit.getHasVariations())
    		{
		    	List<VariationDB> variationsdb = accesVariation.getVariationsDB(produitDB);
		    	Map<String,List<String>> varoptions = accesVariation.VariationsOptionsMap(variationsdb);
		    	List<Variation> variations = accesVariation.getVariations(variationsdb);
		    	
		    	Map<String, List<OptionAttribut>> options = accesVariation.getOptionAttributsByProduit(produitDB);
		    	
		    	request.setAttribute("variations", variations);
		    	request.setAttribute("varoptions", varoptions);
		    	request.setAttribute("options", options);
	    	}
	    	else
	    		 produit.setQTE(produit.getQTE() - Constantes.MIN_QTE_CLIENT);
		}
    	
		request.setAttribute("prliste", pliste);
		request.setAttribute("produit", produit);
		request.setAttribute("theme", theme);
		request.setAttribute("branche_categories",branche);
		request.setAttribute("banner",theme.getLargeImage());
		
		request.getRequestDispatcher("/produit.jsp").forward(request, response);
	}
}
