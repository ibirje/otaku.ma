package ma.otaku.controllersAdmin;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ma.otaku.acces.stock.AccesFournisseurs;
import ma.otaku.data.stock.FournisseurDB;

@Path("/fournisseurs")
public class FournisseurController 
{
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ASKU_SELECT")
	public List<FournisseurDB> getListe(@QueryParam("note") String note, @QueryParam("tel") String tel, 
			@QueryParam("moq") Integer moq, @QueryParam("maxoq") Integer maxoq)
	{
		AccesFournisseurs accesfourni = new AccesFournisseurs();
		return accesfourni.getFournisseursDB(note, tel, moq, maxoq);
	}
}
