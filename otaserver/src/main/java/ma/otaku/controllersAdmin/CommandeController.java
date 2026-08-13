package ma.otaku.controllersAdmin;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ma.otaku.acces.client.AccesClient;
import ma.otaku.acces.client.AccesCommande;
import ma.otaku.business.client.BeanDetailsCommande;
import ma.otaku.business.stats.ResumeCommandes;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.CommandeDB;

@Path("/commandes")
public class CommandeController {
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	//@RolesAllowed("CMD_SELECT")
	public Response getListe(@QueryParam("option") String option,@QueryParam("email") String email, @QueryParam("code") String code, 
			@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
		try 
		{
			if(option == null || option.isEmpty()) return Response.ok().build();
			
			List<BeanDetailsCommande> commande = new AccesCommande().getCommandesAdmin(email,  option, code, page, size);
			
			AccesClient acces = new AccesClient(AccesClient.READONLY);
			
			HashMap<Long,ClientDB> cltmap = new HashMap<>();
			
			if(commande != null) commande.forEach( c -> {
				if( cltmap.containsKey(c.getCommande().getClientID()) )
					c.setClient(cltmap.get(c.getCommande().getClientID()));
				else {
					ClientDB client = acces.getByID(c.getCommande().getClientID());
					c.setClient(client);
					cltmap.put(client.getClientID(), client);
				}
			});

			return Response.ok(commande).build();
		}
		catch (Exception e) {
	       	e.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }").build();       
	    }
	}

	@GET
	@Path("/resume")
	//@RolesAllowed("CMD_UPDATE")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resume() {
		try {
			ResumeCommandes r = new AccesCommande(AccesCommande.READONLY).resume();
			return Response.ok(r).build();
		}
		catch(Exception ex)
		{
	       	ex.printStackTrace();
	        return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + ex.getMessage() + "\" }").build();  
		}
	}
	
	
	@POST
	@Path("/accepter")
	//@RolesAllowed("CMD_UPDATE")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response accepter(List<List<String>> codes) {
		return nextEtats(codes, CommandeDB.ACCEPTATION);
	}

	@POST
	@Path("/preparer")
	//@RolesAllowed("CMD_UPDATE")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response preparer(List<List<String>> codes) {
		return nextEtats(codes, CommandeDB.PREPARATION);
	}

	@POST
	@Path("/envoyer")
	//@RolesAllowed("CMD_UPDATE")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response envoyer(List<List<String>> codes) {
		return nextEtats(codes, CommandeDB.ENVOI);
	}

	@POST
	@Path("/finaliser")
	//@RolesAllowed("CMD_UPDATE")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finaliser(List<List<String>> codes) {
		return nextEtats(codes, CommandeDB.ENVOYEE);
	}

	@POST
	@Path("/refuser")
	//@RolesAllowed("CMD_UPDATE")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response refuser(List<List<String>> codes) {
		return nextEtats(codes, CommandeDB.REFUSEE);
	}
	
	private Response nextEtats(List<List<String>> codes, String etat) {
		try 
		{ 
			if(codes == null || codes.size() < 1) return Response.ok().build();
			
			List<CommandeDB> commandes = new AccesCommande().nextEtat(codes, etat);

			return Response.ok(commandes).build();
		}
		catch(Exception ex)
		{
			System.err.println("---- "+ex.getMessage()+" ---------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}
	}

	@POST
	@Path("/confirmerpaiement")
	//@RolesAllowed("CMD_UPDATE")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response confirmerPaiement(List<String> codes) {
		try 
		{ 
			if(codes == null || codes.size() < 1) return Response.ok().build();
			List<CommandeDB> commandes = new AccesCommande().confirmerPaiement(codes);
			return Response.ok(commandes).build();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.err.println("---- "+ex.getMessage()+" ---------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}
	}
}
