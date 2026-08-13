package ma.otaku.controllersAdmin;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import ma.otaku.acces.admin.AccesAdminLogin;
import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.data.admin.AdminLoginDB;
import ma.otaku.utils.Utils;

@Path("/authentication")
public class AuthenticationController {

	@Context
	SecurityContext securityContext;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public String checkServer() throws NamingException {
		return "oki doki";
	}

	@GET
	@Path("/godopawaprod/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void refreshit(@PathParam("id") Long id) throws Exception {
		new AccesProduitStats().refreshStatsProduits();
	}

	@GET
	@Path("/godopawavar")
	@Produces(MediaType.APPLICATION_JSON)
	public void refreshito() throws Exception {
		new Schedules().refreshStatsVariation();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticateUser(AdminLoginDB credentials) {

		try {
			String username = credentials.getPseudo();
			String password = credentials.getPassword();

			AccesAdminLogin acceslogin = new AccesAdminLogin();
			Long id = authenticate(username, password, acceslogin);
			String token = acceslogin.addToken(username, id);
			if (servletRequest != null)
				System.err.print("[" + servletRequest.getRemoteAddr() + "]  ");
			System.err.println(new Utils().shortTime() + "  " + "---- " + username + "  " + " connecté -----");
			return Response.ok(token).build();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	private Long authenticate(String username, String password, AccesAdminLogin acceslogin) throws Exception {
		Long id = acceslogin.loginAdmin(username, password);
		if (id == null) {
			System.err.println("---ids incorrects---");
			throw new Exception("ids incorrects");
		}
		return id;
	}

}