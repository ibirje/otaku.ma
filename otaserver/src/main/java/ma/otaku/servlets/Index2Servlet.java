package ma.otaku.servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.acces.produit.AccesProduitsClient;
import ma.otaku.acces.produit.AccesTheme;
import ma.otaku.data.type.CategorieDB;
import ma.otaku.data.type.ThemeDB;


// @WebServlet("/index2")
public class Index2Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Index2Servlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* TODO cache data ( liste :  categories / themes , visites ,categories, themes ,
		 * produits, index, connection, inscription, erreurs..)
		 * 
		 * 
		 * liste categories / themes 
		 * quelque produits déjà visités
		 * X lignes de produits en promo / ( bonne qualité, mal vendus ) / nouveautés ? 
		 * | produits se chargent tant que le client descend vers le bas
		 * */

		
		Timestamp reqdate = new Timestamp(System.currentTimeMillis());
		System.err.println("- "+reqdate.toString()+"-- "+request.getRemoteAddr()+" index ---------------------");
		
		AccesProduitsClient accesProduit    = new AccesProduitsClient() ;
    	AccesCategorie accesCategorie = new AccesCategorie();
    	AccesTheme  accesTheme        = new AccesTheme();
    	
    	List<CategorieDB> categories  = accesCategorie.getSortedList();
    	List<ThemeDB> themes 		  = accesTheme.getSortedList();
    	// List<Produit> sampleprods   = accesProduit.getSuggestionList();
    	
    	// TODO prods en promo
		// request.setAttribute("prodrec", sampleprods);
		request.setAttribute("categories", categories);
		request.setAttribute("themes", themes);
		
		request.getRequestDispatcher("index_main.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
