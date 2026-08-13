package ma.otaku.controllersPublic;

import java.util.List;

import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import ma.otaku.acces.client.AccesCommande;
import ma.otaku.acces.client.AccesCoursierClient;
import ma.otaku.business.client.BeanCreerCommande;
import ma.otaku.business.client.BeanDetailsCommande;
import ma.otaku.business.client.CommandeSansUtilisateurDTO;
import ma.otaku.business.stats.ResumeCommandes;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.CommandeDB;

@Path("public/commandes")
public class CommandePublicController {

	@Context
	SecurityContext security;

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/coursiers")
	public Response CoursiersOptions() {
		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/coursiers")
	public Response getCoursiers() {
		return Response.ok(new AccesCoursierClient().getList()).build();
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/resume")
	public Response resumeOptions() {
		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/resume")
	public Response resume() {
		try {
			ResumeCommandes r = new AccesCommande(AccesCommande.READONLY).resume();
			return Response.ok(r).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + ex.getMessage() + "\" }")
					.build();
		}
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getcommandes")
	public Response getCommandesOptions(@QueryParam("option") String option, @QueryParam("code") String code,
			@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getcommandes")
	public Response getCommandes(@QueryParam("option") String option, @QueryParam("code") String code,
			@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
		try {
			ClientDB user = (ClientDB) security.getUserPrincipal();

			if (user == null || user.getClientID() == null)
				throw new Exception("non connecté");

			List<BeanDetailsCommande> commande = new AccesCommande().getCommandesClient(user.getClientID(), option,
					code, page, size);

			return Response.ok(commande).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }")
					.build();
		}
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/passercommande")
	public Response passerCommandeOptions(BeanCreerCommande cmd) {
		return Response.ok().build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/passercommande")
	public Response passerCommande(BeanCreerCommande cmd) {
		try {
			if (cmd == null)
				throw new NullPointerException("Commande vide");
			if (cmd.getItems() == null)
				throw new NullPointerException("Liste produits vide");
			if (cmd.getLivraison() == null)
				throw new NullPointerException("type de livraison vide");
			if (cmd.getPaiement() == null)
				throw new NullPointerException("type de paiement vide");

			ClientDB user = (ClientDB) security.getUserPrincipal();
			BeanDetailsCommande commande = new AccesCommande().passerCommande(cmd, user.getClientID());
			return Response.ok(commande).build();
		} catch (ValidationException ne) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("{ message : \"" + ne.getMessage() + "\" }")
					.build();
		} catch (NotAcceptableException ne) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{ message : \"" + ne.getMessage() + "\" }")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }")
					.build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/passercommandesansuser")
	public Response passerCommandeSansUtilisateur(CommandeSansUtilisateurDTO cmd) {

		try {
			if (cmd == null)
				throw new NullPointerException("Commande vide");
			if (cmd.getItems() == null)
				throw new NullPointerException("Liste produits vide");
			if (cmd.getLivraison() == null)
				throw new NullPointerException("type de livraison vide");
			if (cmd.getPaiement() == null)
				throw new NullPointerException("type de paiement vide");
			
			BeanDetailsCommande commande = new AccesCommande().passerCommande(cmd);

			return Response.ok(commande).build();
			
		} catch (ValidationException ne) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("{ message : \"" + ne.getMessage() + "\" }")
					.build();
		} catch (NotAcceptableException ne) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{ message : \"" + ne.getMessage() + "\" }")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }")
					.build();
		}
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/annulercommande")
	public Response annulerCommandeOptions(CommandeDB cmd) {
		return Response.ok().build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/annulercommande")
	public Response annulerCommande(CommandeDB cmd) {
		try {
			if (cmd == null || cmd.getCode() == null)
				throw new NullPointerException("Commande invalide");
			CommandeDB commande = new AccesCommande().annuler(cmd.getCode());
			return Response.ok(commande).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).entity("{ message : \"" + e.getMessage() + "\" }")
					.build();
		}
	}
}
