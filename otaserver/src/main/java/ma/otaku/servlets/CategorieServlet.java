package ma.otaku.servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.acces.produit.AccesProduitsClient;
import ma.otaku.business.produits.Categorie;
import ma.otaku.business.produits.Produit;
import ma.otaku.data.type.CategorieDB;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.Parse;


 @WebServlet("/categories/*")
public class CategorieServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    public CategorieServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
    	super.init();
    
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String cat = request.getPathInfo();
		Timestamp reqdate = new Timestamp(System.currentTimeMillis());
		
		System.err.println("- "+reqdate.toString()+"-- "+request.getRemoteAddr()+" categorie"+cat+" ---------------------");
		if(cat != null )
			cat = cat.substring(1).replaceAll("-", " ");
		
		AccesCategorie accesCategorie = new AccesCategorie();
		AccesProduitsClient accesProduit     = new AccesProduitsClient();
		
		CategorieDB categoriedb = accesCategorie.getByNom(cat);
		if(categoriedb == null)
		{
    		request.getRequestDispatcher("/error_categorie_not_found.jsp").forward(request, response);
    		return;
    	}

		Categorie categorie = new Categorie(categoriedb);
		
		List<Categorie> branche = accesCategorie.getBrancheCategories(categoriedb.getCategorieParent());
		Parse parse = new Parse();
		Integer page   = parse.parseInteger(request.getParameter("nbpage") , Constantes.MIN_PAGE);
		Double prixMin = parse.parseDouble(request.getParameter("min")   , Constantes.MIN_PRIX);
		Double prixMax = parse.parseDouble(request.getParameter("max")    , Constantes.MAX_PRIX);
		String filtre  = request.getParameter("filtre");
    	
    	List<Produit> prliste = accesProduit.getListeProduitsByCategorie(categorie.getCode(),prixMin,prixMax,filtre,page, Constantes.TAILLE_PAGE );
    	
    	Long nbprod = accesProduit.getNombreProduitsByCategorie(categoriedb.getCode(), prixMin, prixMax);
		Integer nbpages = (int) ( nbprod / Constantes.TAILLE_PAGE ) + (nbprod % Constantes.TAILLE_PAGE > 0 ? 1 : 0);
		
		// page min = 1 , page max =  nombre de pages
		page = page < 1 ? 1 : page > nbpages ? nbpages : page;
		
		int pagemin = page - Constantes.NOMBRE_PAGES_LISTE / 2 , pagemax = page+Constantes.NOMBRE_PAGES_LISTE /2 ;
		int restemin = pagemin - 1 , restemax = nbpages - pagemax;
		
		// nombrepages >= page >= 1
		pagemin = Math.max(1, restemax < 0 ? pagemin + restemax : pagemin);
		pagemax = Math.min(nbpages, restemin < 0 ? pagemax - restemin : pagemax);
		
    	request.setAttribute("page", page);
    	request.setAttribute("pagemin", pagemin);
    	request.setAttribute("pagemax", pagemax);
		request.setAttribute("prliste",prliste);
		request.setAttribute("branche_categories",branche);
		request.setAttribute("categorie",categorie);
		request.setAttribute("banner",categorie.getLargeImage());
		request.setAttribute("titre",categorie.getNom());
		
		request.getRequestDispatcher("/categorie.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
