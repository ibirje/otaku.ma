package ma.otaku.controllersAdmin;

import java.sql.Date;
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

import ma.otaku.acces.admin.AccesAdmin;
import ma.otaku.acces.stock.AccesAchatStock;
import ma.otaku.acces.stock.AccesAchatStockItem;
import ma.otaku.business.stock.BeanAchatStockItems;
import ma.otaku.data.admin.AdminDB;
import ma.otaku.data.admin.AdminLoginDB;
import ma.otaku.data.stock.AchatStockDB;
import ma.otaku.data.stock.AchatStockItemDB;
import ma.otaku.utils.Parse;

@Path("/achatstock")
public class AchatStockController 
{
	@Context SecurityContext security;
	
	
	@POST
	@Path("insert")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ASKU_INSERT")
	public Response insert(BeanAchatStockItems bean) throws Exception 
	{
		try 
		{
			/************** Verifie les données recus *************/
			if(bean == null || bean.getAchat() == null || bean.getAchatItems() == null) throw new Exception("Données incomplets.");
			if(bean.getAchatItems().isEmpty()) throw new Exception("Achat vide.");
			
			AccesAchatStockItem accesitems = new AccesAchatStockItem();
			
			Long qte = 0L;
			for(AchatStockItemDB item : bean.getAchatItems())
			{
				accesitems.isDonnesValides(item);
				qte += item.getQte();
			}
				
			BeanAchatStockItems newbean = new BeanAchatStockItems();
			
			Long adminid = ((AdminLoginDB)security.getUserPrincipal()).getAdminID();
			bean.getAchat().setAuthAdminID(adminid);
			bean.getAchat().setQte(qte);
			
			newbean.setAchat(new AccesAchatStock().insert(bean.getAchat()));
			
			if(newbean.getAchat() == null) throw new Exception("Echec d'insertion d'achat stock");
			newbean.setAchatItems(new HashSet<AchatStockItemDB>());
			
			for(AchatStockItemDB item : bean.getAchatItems())
			{
				item.setAchatStockCode(newbean.getAchat().getCode());
				item.setAchatStockID(newbean.getAchat().getAchatStockID());
				AchatStockItemDB newitem = accesitems.insert(item);
				newbean.getAchatItems().add(newitem);
			}
			
			return Response.ok(newbean).build();
		}
		catch (Exception e) 
		{
			System.err.println("-------- "+e.getMessage()+" --------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON )
	@RolesAllowed("ASKU_SELECT")
	public Response getListe(@QueryParam("code") String code, @QueryParam("description") String description,
	@QueryParam("debutinsert") String debutinsert, @QueryParam("fininsert") String fininsert, @QueryParam("associe") Boolean associe)
	{        
		Parse parse = new Parse();

		Date debut = parse.ParseDate(debutinsert);
		Date fin   = parse.ParseDate(fininsert);
		List<AchatStockDB> ls;
		if(security.isUserInRole("SKU_INSERT"))
		{
			ls =  new AccesAchatStock().getFullListe(code, description, debut,fin,associe,null);
		}
		else
		{	
			Long adminid = ((AdminLoginDB)security.getUserPrincipal()).getAdminID();
			ls =  new AccesAchatStock().getFullListe(code, description, debut,fin,associe,adminid);
		}
		try {

			return Response.ok(ls).build();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("delete")
	@RolesAllowed("ASKU_DELETE")
	public Response deleteProduit(AchatStockDB achat)
	{
		try 
		{ 
			AccesAchatStock acces = new AccesAchatStock();
			AchatStockDB newachat = acces.getByCode(achat.getCode());
			
			Long adminid = ((AdminLoginDB)security.getUserPrincipal()).getAdminID();
			
			if(newachat.getAuthAdminID() != adminid)
			{
				AdminDB admin = new AccesAdmin().getByID(newachat.getAuthAdminID());
				throw new Exception("Cet achat appartient à l'admin "+admin.getPrenom()+
				" vous n'avez pas le droit de le supprimer");
			}
			AchatStockDB dachat = acces.delete(achat);
			return Response.ok(dachat).build();
		}
		catch(Exception ex)
		{
			System.err.println("---- "+ex.getMessage()+" ---------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}
	}
	
	
	
	@GET
	@Path("/achatitems")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ASKU_SELECT")
	public List<AchatStockItemDB> achatitemliste(@QueryParam("code") String code, 
			@QueryParam("associe") Boolean associe) throws Exception 
	{
		try 
		{
			Long achatid = new AccesAchatStock().getEquals("code", code).getAchatStockID();
			
			List<AchatStockItemDB> ls = new AccesAchatStockItem(AccesAchatStockItem.READONLY).
					getAllInfomationsList("achatStockID", achatid,associe);
			
			return ls;
		}
		catch (Exception e) 
		{
			System.err.println("-------- "+e.getMessage()+" --------");
			return null;
		}
	}
}
