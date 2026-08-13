package ma.otaku.controllersAdmin;

import java.util.HashSet;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import ma.otaku.acces.stock.AccesAchatStock;
import ma.otaku.acces.stock.AccesAchatStockItem;
import ma.otaku.acces.stock.AccesStock;
import ma.otaku.business.stock.BeanStock;
import ma.otaku.data.admin.AdminLoginDB;
import ma.otaku.data.stock.AchatStockDB;
import ma.otaku.data.stock.AchatStockItemDB;
import ma.otaku.data.stock.StockDB;

@Path("/stock")
public class StockController {

	@Context SecurityContext security;

	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("SKU_SELECT")
	public List<StockDB> getListe(@QueryParam("code") String code, @QueryParam("page") Integer page)
	{
		try 
		{
			return new AccesStock().getListeByAdmin((AdminLoginDB) security.getUserPrincipal(), code, page);
		}
		catch (Exception e){
			System.err.println("-------- "+e.getMessage()+" --------");
			return null;
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("SKU_SELECT")
	public List<StockDB> getListeTest(@QueryParam("code") String code, @QueryParam("page") Integer page, @QueryParam("isActive") Boolean isActive)
	{
		try
		{
			AdminLoginDB admin = ((AdminLoginDB)security.getUserPrincipal());
			Long adminID = admin.getRole().equals("ALL_MIGHT") ? null : admin.getAdminID();
			return new AccesStock().getListeRecherche(page, code, adminID, null, isActive);
		}
		catch (Exception e) 
		{
			System.err.println("-------- "+e.getMessage()+" --------");
			return null;
		}
	}
	
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("SKU_INSERT")
	public Response insert(BeanStock bs) throws Exception 
	{
		/************** Verifie les données recus *************/
		try 
		{
			List<StockDB> sliste = bs.getStock();
			if(sliste == null || sliste.isEmpty()) throw new Exception("Liste stock à inserer est Vide");
			
			AdminLoginDB login = (AdminLoginDB) security.getUserPrincipal();

			AccesStock accesstock = new AccesStock();
			
			HashSet<StockDB> inserted = new HashSet<>();
			for(StockDB stock : sliste)
			{
				stock.setAuthAdminID(login.getAdminID());
				inserted.add(accesstock.insert(stock));
			}
			
			AccesAchatStockItem accesitem = new AccesAchatStockItem();
			AccesAchatStock accesachat = new AccesAchatStock();
			AchatStockItemDB item = accesitem.getByID(((StockDB) inserted.toArray()[0]).getAchatStockItemID());
			
			AchatStockDB as = accesachat.getByID(item.getAchatStockID());

			as.setAssocie(true);
			
			accesachat.update(as);

			return Response.ok(bs).build();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println("-------- "+e.getMessage()+" --------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
		}
	}
	
	
	@POST
	@Path("/desactiver")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("SKU_UPDATE")
	public Response desactiverStock(StockDB code) throws Exception 
	{
		try 
		{
			return Response.ok().build();
			// return Response.ok(new AccesStock().desactiver(code.getCode())).build();
		}
		catch (Exception e) 
		{
			System.err.println("-------- "+e.getMessage()+" --------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
		}
	}
	
	
	@POST
	@Path("/activer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("SKU_UPDATE")
	public Response activerStock(StockDB code) throws Exception 
	{
		try 
		{
			return Response.ok().build();
			// return Response.ok(new AccesStock().activer(code.getCode())).build();
		}
		catch (Exception e) 
		{
			System.err.println("-------- "+e.getMessage()+" --------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
		}
	}

	
}



