package ma.otaku.servlets.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ConnectionServlet
 */
// @WebServlet("/connection")
public class ConnectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public ConnectionServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	String source = req.getHeader("referer");
    	req.setAttribute("source", source);
    	req.getRequestDispatcher("/connection.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	doGet(req, resp);
    }
}
