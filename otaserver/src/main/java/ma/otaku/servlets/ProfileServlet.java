package ma.otaku.servlets;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.otaku.utils.Constantes;



// @WebServlet("/profile/*")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public ProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getCookies() != null)
		{
			Cookie logincookie = Arrays.stream(request.getCookies()).filter(c -> c.getName()
				.equals(Constantes.SESSION_NOM)).findAny().orElse(null);
			if(logincookie == null)
			{
				response.sendRedirect(request.getContextPath()+"/connection");
				return;
			}
			
		}
        
		
		
		request.getRequestDispatcher("/profile.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
