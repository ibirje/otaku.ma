package ma.otaku.controllersAdmin;

import javax.annotation.security.RolesAllowed;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javassist.NotFoundException;
import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.produit.AccesProduitsAdmin;
import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.stats.ProduitStats;

@Path("/produitstats")
public class ProduitStatsController {

	
	@GET
	@Path("/get")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_ALL")
	public Response getProduitStats(@QueryParam("code") String code) throws Exception 
	{

		try {
			if(code == null || code.isEmpty())
				throw new ValidationException("code invalide");
			
			ProduitStats p = new AccesProduitStats().getProduitStatsByProduitCode(code);
			return Response.ok(p).build();
		}
		catch(Exception ex) {

			ex.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}
	

	@GET
	@Path("/rendementchart")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_ALL")
	public Response getRendementStats() throws Exception 
	{
		try {
			return Response.ok(new AccesProduitStats().getRendementChartStats()).build();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}
	
	
	
	@GET
	@Path("/refresh")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_ALL")
	public Response refreshProduitStats(@QueryParam("code") String code) throws Exception 
	{
		try {
			if(code == null || code.isEmpty())
				throw new ValidationException("code invalide");
			
			ProduitAdmin prod = new AccesProduitsAdmin().getByCode(code);
			ProduitStats p = new AccesProduitStats().refreshStatsProduit(prod.getProduitID());
			return Response.ok(p).build();
		}
		catch(NotFoundException | NullPointerException ex) {
			ex.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		catch(Exception ex) {

			ex.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}
}
