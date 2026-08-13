package ma.otaku.servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.otaku.acces.produit.AccesProduitsClient;
import ma.otaku.acces.produit.AccesTheme;
import ma.otaku.business.produits.Produit;
import ma.otaku.data.type.ThemeDB;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.Parse;

// @WebServlet("/themes/*")
public class ThemeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
    public ThemeServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
    	super.init();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String cat = request.getPathInfo();
		Timestamp reqdate = new Timestamp(System.currentTimeMillis());
		
		System.err.println("- "+reqdate.toString()+"-- "+request.getRemoteAddr()+" theme"+cat+" ---------------------");
		if(cat != null )
			cat = cat.substring(1).replaceAll("-", " ");
		
		AccesTheme accesTheme     = new AccesTheme();
		AccesProduitsClient accesProduit = new AccesProduitsClient();
		
		ThemeDB themedb = accesTheme.getThemeByNom(cat);
		if(themedb == null)
		{
    		request.getRequestDispatcher("/error_theme_not_found.jsp").forward(request, response);
    		return;
    	}

		
		List<ThemeDB> branche = accesTheme.getBrancheThemes(themedb.getThemeParent());
		Parse parse = new Parse();
		Double prixMin = parse.parseDouble(request.getParameter("min"),Constantes.MIN_PRIX), 
			   prixMax = parse.parseDouble(request.getParameter("max"),Constantes.MAX_PRIX);
		String filtre  = request.getParameter("filtre");
		
		Integer page = parse.parseInteger(request.getParameter("nbpage"),1);
		List<Produit> prliste = accesProduit.getListeProduitsByTheme(themedb.getCode(),prixMin,prixMax,filtre,page,Constantes.TAILLE_PAGE);
		
		Long nbprod = accesProduit.getNombreProduitsByTheme(themedb.getCode(), prixMin, prixMax);
		Integer nbpages = (int) ( nbprod / Constantes.TAILLE_PAGE ) + (nbprod % Constantes.TAILLE_PAGE > 0 ? 1 : 0);
		
		// page min = 1 , page max =  nombre de pages
		page = page < Constantes.MIN_PAGE ? Constantes.MIN_PAGE : page > nbpages ? nbpages : page;
		int pagemin = page - Constantes.NOMBRE_PAGES_LISTE / 2 , pagemax = page + Constantes.NOMBRE_PAGES_LISTE / 2 ;
		int restemin = pagemin - Constantes.MIN_PAGE , restemax = nbpages - pagemax;
				
		// 1 <= page <= nombrepages
		pagemin = Math.max(Constantes.MIN_PAGE, restemax < 0 ? pagemin + restemax : pagemin);
		pagemax = Math.min(nbpages, restemin < 0 ? pagemax - restemin : pagemax);
		
		// 1 2 3 4 5 6 7 8 9 10 11 12 13
    	request.setAttribute("page", page);
    	request.setAttribute("pagemin", pagemin);
    	request.setAttribute("pagemax", pagemax);
		request.setAttribute("prliste",prliste);
		request.setAttribute("branche_themes",branche);
		request.setAttribute("theme",themedb);
		request.setAttribute("banner",themedb.getLargeImage());
		request.setAttribute("titre",themedb.getNom());
		
		request.getRequestDispatcher("/theme.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
