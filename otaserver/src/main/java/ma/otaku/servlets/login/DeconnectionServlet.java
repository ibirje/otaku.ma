package ma.otaku.servlets.login;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.otaku.acces.client.AccesClientSession;
import ma.otaku.data.client.ClientSessionDB;
import ma.otaku.utils.Constantes;

/**
 * Servlet implementation class DeconnectionServlet
 */
// @WebServlet("/deconnecter")
public class DeconnectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeconnectionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie logincookie = Arrays.stream(request.getCookies()).filter(c -> c.getName()
                .equals(Constantes.SESSION_NOM)).findAny().orElse(null);

		AccesClientSession accessession = new AccesClientSession();
		ClientSessionDB cltsession = accessession.getEquals("cookie", logincookie.getValue());

		try { accessession.deleteObject(cltsession); } catch( Exception ex ) { }
		
		HashSet<String> stdcookies = new HashSet<>();
		stdcookies.add(Constantes.SESSION_NOM);
		stdcookies.add("_nom");
		stdcookies.add("_prenom");
		stdcookies.add("_paniercount");
		
		for(String nom : stdcookies)
		{
			Cookie ck = new Cookie(nom, "");
			ck.setMaxAge(0);
			response.addCookie(ck);
		}



    	String source = request.getHeader("referer");
    	if( source != null )
    		response.sendRedirect(source);
    	else
    		response.sendRedirect("/");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
