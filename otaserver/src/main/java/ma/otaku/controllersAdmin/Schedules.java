package ma.otaku.controllersAdmin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import ma.otaku.acces.admin.AccesToken;
import ma.otaku.acces.client.AccesClientLogin;
import ma.otaku.acces.client.AccesClientPending;
import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.acces.produit.AccesProduitStats;

@Path("/schedule")
public class Schedules {
	@GET
	@Path("/stats_minute")
	public void updateStatsMinute() throws Exception
	{
		/** jobs for time dependent variables ( stats par heure ) */
	}
	
	@GET
	@Path("/stats_heure")
	public void updateStatsHeure() throws Exception
	{
		/** jobs for time dependent variables ( stats par heure ) */
	}

	@GET
	@Path("/stats_jour")
	public void updateStatsJour() throws Exception
	{
		/** jobs for time dependent variables ( stats par Jour ) */
	}

	@GET
	@Path("/stats_week")
	public void updateStatsSemaine() throws Exception
	{
		/** jobs for time dependent variables ( stats par semaine ) */
	}

	@GET
	@Path("/stats_mois")
	public void updateStatsMois() throws Exception
	{
		/** jobs for time dependent variables ( stats par Mois ) */
	}
/*
	@GET
	@Path("/app/schedule/refresh_stats_produit")
	public void refresh_stats_produit() throws Exception {
		new AccesProduitStats().refreshStatsProduits();
	}
*/
	@GET
	@Path("/app/schedule/refresh_stats_var")
	public void refreshStatsVariation() throws Exception {
		new AccesProduitStats().refreshStatsVariations();
	}
	

	@GET
	@Path("/cleanuptokens")
	public void cleanup() throws Exception
	{
		try 
		{
			cleanupAdminTokens();
			cleanupClientTokens();
			cleanupPendingClient();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@GET
	@Path("/type_images")
	public void refreshTypeImages() throws Exception
	{
		try 
		{
			new AccesCategorie().changeImages();
			// new AccesTheme().changeImages();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void cleanupAdminTokens()   throws Exception { new AccesToken().cleanTokens(); }
	private void cleanupClientTokens()  throws Exception { new AccesClientLogin().clean(); } /* TODO */
	private void cleanupPendingClient() throws Exception { new AccesClientPending().clean();}
}
