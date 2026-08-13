package ma.otaku.servlets.inscription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.otaku.acces.client.AccesClient;
import ma.otaku.acces.client.AccesClientLogin;
import ma.otaku.acces.client.AccesClientPending;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.ClientLoginDB;
import ma.otaku.data.client.ClientPendingDB;

/**
 * Servlet implementation class VerifierEmailServlet
 */
// @WebServlet("/verifemail/*")
public class VerifierEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifierEmailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String key = request.getPathInfo() != null ? request.getPathInfo().substring(1) : null;
		//Utils utils = new Utils();
		if(key == null)
		{
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		//key = utils.decripter(key);
		
		AccesClientPending accespending = new AccesClientPending();
		
		ClientPendingDB pending = accespending.getValidPending(key);
		
		if(pending != null)
		{
			// verifier si key a expiré + add Constantes.INSCRIPTION_KEY_EXPIRE_DURATION 
			/**** creer client , client login ****/
			AccesClient accesclient = new AccesClient();
			
			ClientDB client = new ClientDB();
			client.setEmail(pending.getEmail());
			
			AccesClientLogin acceslogin = new AccesClientLogin();
			
			ClientLoginDB login = new ClientLoginDB();
			
			try {
				client = accesclient.insert(client);
				
				if(client == null) throw new NullPointerException("Echec d'insertion client");
				

				accespending.delete(pending);
				
				login.setClientID(client.getClientID());
				login.setEmail(client.getEmail());
				login.setMotdepasse(pending.getMotdepasse());
				
				acceslogin.insert(login);

				response.sendRedirect(request.getContextPath()+"/connection");
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				
					try {
						if( client != null && client.getClientID() != null ) accesclient.delete(client);
						if( login != null && login.getClientloginID() != null ) acceslogin.deleteObject(login);
					} catch (Exception e) { }
				
				response.sendRedirect(request.getContextPath());
			}
		}
		else
			response.sendRedirect(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
