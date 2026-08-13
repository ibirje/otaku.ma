package ma.otaku.acces.produit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.acces.client.AccesCommande;
import ma.otaku.acces.client.AccesCommandeItem;
import ma.otaku.acces.client.AccesPanier;
import ma.otaku.acces.stock.AccesAchatStockItem;
import ma.otaku.acces.stock.AccesCasse;
import ma.otaku.acces.stock.AccesPerte;
import ma.otaku.acces.stock.AccesStock;
import ma.otaku.acces.triggers.produitstats.AfterDeleteProduitStats;
import ma.otaku.acces.triggers.produitstats.AfterInsertProduitStats;
import ma.otaku.acces.triggers.produitstats.AfterUpdateProduitStats;
import ma.otaku.business.produits.ProduitAndStats;
import ma.otaku.data.client.CommandeDB;
import ma.otaku.data.client.CommandeItemDB;
import ma.otaku.data.client.PanierDB;
import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.produit.VariationDB;
import ma.otaku.data.stats.ProduitStats;
import ma.otaku.data.stock.AchatStockItemDB;
import ma.otaku.data.stock.CasseDB;
import ma.otaku.data.stock.PerteDB;
import ma.otaku.data.stock.StockDB;

public class AccesProduitStats  extends AccesTable<ProduitStats>{

	
	public AccesProduitStats() {
		super(ProduitStats.class);
		
		insertTriggers.add( new AfterInsertProduitStats(this));
		updateTriggers.add( new AfterUpdateProduitStats(this));
		deleteTriggers.add( new AfterDeleteProduitStats(this));
		
	}
	public AccesProduitStats(byte mode) {
		super(ProduitStats.class, mode);
	}
	
	@Override
	protected Query isInDBQuery(ProduitStats t, EntityManager manager) throws Exception {
		return null;
	}
	

	@Override
	protected void verifierDonnees(ProduitStats t) throws Exception {
		
	}

	
	@Override
	protected void erreurExistant(ProduitStats t) throws Exception {
		
	}
	

	@Override
	protected void erreurInexistant(String t) throws Exception {
		
	}

	
	@Override
	protected void deleteErrors(ProduitStats t) throws Exception {
		
	}
	public List<ProduitAndStats> getRendementChartStats() {
		
		List<ProduitStats> pstats = selectWhere(5," commandeQte > 0 and variationID is null "
			+ "order by commandePrixTotal + stockPrixMoyen * stockQte - commandeQte * fraisSup "
			+ "- (achatstockPrixTotal / achatstockQte) * (stockQte + commandeQte) desc");
		
		if(v().isNullOrEmpty(pstats))
			return null;
		
		List<ProduitAndStats> prodnstats = new ArrayList<>();
		
		AccesProduitsAdmin accespr = new AccesProduitsAdmin(AccesTable.READONLY);
		for(ProduitStats stats: pstats)
		{
			ProduitAdmin pradmin = accespr.getByID(stats.getProduitID());
			prodnstats.add(new ProduitAndStats(stats, pradmin));
		}
		
		return prodnstats;
	}
	
/* --------------------------------- variation stats / produitstats trigger --------------------------------- */

	public void addVariationStatsToProduitStats(ProduitStats old, ProduitStats nnew) throws Exception{ 
		
		if(nnew == null) 
			throw new Exception("variation stats invalide");
		if( nnew.getProduitID() == null )
			throw new Exception("variation stats produitID null");
		
		ProduitStats pstats = selectWhereFirst("produitID = :s0 AND variationID is null", nnew.getProduitID());
		if(pstats == null) {
			sumVariationStatsToProduitStats(nnew.getProduitID());
			return;
		}
	 	Long   achatstockQte = pstats.getAchatstockQte() 	   + nnew.getAchatstockQte() 	   - (old == null ? 0 : old.getAchatstockQte());
		Double askuPrixTotal = pstats.getAchatstockPrixTotal() + nnew.getAchatstockPrixTotal() - (old == null ? 0 : old.getAchatstockPrixTotal());
		Long 	 commandeQte = pstats.getCommandeQte() 		   + nnew.getCommandeQte() 		   - (old == null ? 0 : old.getCommandeQte());
		Double  cmdPrixTotal = pstats.getCommandePrixTotal()   + nnew.getCommandePrixTotal()   - (old == null ? 0 : old.getCommandePrixTotal());
		Long 	    stockQte = pstats.getStockQte() 		   + nnew.getStockQte()			   - (old == null ? 0 : old.getStockQte());
		
		Double  skuPrixTotal = pstats.getStockPrixMoyen() * pstats.getStockQte() + nnew.getStockPrixMoyen()*nnew.getStockQte()
								- (old == null ? 0 : old.getStockPrixMoyen()*old.getStockQte());
		Double skuPrixMoyen = stockQte > 0 ? skuPrixTotal / stockQte : 0;
		System.err .println("AccesProduitStats.addVariationStatsToProduitStats() :" + skuPrixMoyen);
		
		Long 	  pendingQte = pstats.getPendingQte() 	       + nnew.getPendingQte() 		   - (old == null ? 0 : old.getPendingQte());
		Long 	   panierQte = pstats.getPanierQte() 		   + nnew.getPanierQte()		   - (old == null ? 0 : old.getPanierQte());
		Long 	    casseQte = pstats.getCasseQte() 		   + nnew.getCasseQte() 		   - (old == null ? 0 : old.getCasseQte());
		Long 		perteQte = pstats.getPerteQte() 		   + nnew.getPerteQte() 		   - (old == null ? 0 : old.getPerteQte());
		Long 	   lockedQte = pstats.getLockedQte()		   + nnew.getLockedQte() 		   - (old == null ? 0 : old.getLockedQte());

		update(pstats, nnew.getProduitID(), achatstockQte, askuPrixTotal, commandeQte, cmdPrixTotal, stockQte,
				skuPrixMoyen, pendingQte, panierQte, casseQte, perteQte, lockedQte);
	}
	
	
	
	public void addVariationStatsToProduitStats(ProduitStats nnew) throws Exception{

		addVariationStatsToProduitStats(null, nnew);
	}
	

	public void sumVariationStatsToProduitStats(ProduitStats old) throws Exception {

		if(old == null) 
			throw new Exception("variation stats invalide");
		sumVariationStatsToProduitStats(old.getProduitID());
	}

	public void sumVariationStatsToProduitStats(Long pid) throws Exception {

		/** Somme les stats variations into produit stats */
		if( pid == null )
			throw new Exception("variation stats produitID null");
		/** change to dynselect add varid is null */
		List<ProduitStats> produitst = selectWhere("produitID = :s0 and variationID is not null", pid);
		
		if(v().isNullOrEmpty(produitst))
			throw new Exception("Aucune variation stats trouvée pour le produitID " + pid);
		
		ProduitStats produitstats = null;

	 	Long 		 achatstockQte = 0l;
		Double achatstockPrixTotal = 0d;
		Long 		   commandeQte = 0l;
		Double 	 commandePrixTotal = 0d;
		Long 			  stockQte = 0l;
		Double 		stockPrixTotal = 0d;
		Double 		stockPrixMoyen = 0d;
		Long 			pendingQte = 0l;
		Long 			 panierQte = 0l;
		Long 			  casseQte = 0l;
		Long 			  perteQte = 0l;
		Long 			 lockedQte = 0l;
		
		for (ProduitStats p : produitst)
		{
			if(p.getVariationID() == null)
				produitstats = p;
			else 
			{
				achatstockQte 		+= 	p.getAchatstockQte();
				achatstockPrixTotal += 	p.getAchatstockPrixTotal();
				commandeQte 		+= 	p.getCommandeQte();
				commandePrixTotal 	+= 	p.getCommandePrixTotal();
				stockQte 			+= 	p.getStockQte();
				stockPrixTotal 		+= 	p.getStockPrixMoyen()* p.getStockQte();
				pendingQte 			+= 	p.getPendingQte();
				panierQte 			+= 	p.getPanierQte();
				casseQte 			+= 	p.getCasseQte();
				perteQte 			+= 	p.getPerteQte();
				lockedQte			+= p.getLockedQte();
			}
		}
		stockPrixMoyen = stockQte > 0 ? stockPrixTotal / stockQte : 0;
		System.err.println("sumVariationStatsToProduitStats : " + stockPrixMoyen);
		
		if(produitstats == null)
		{
			produitstats = new ProduitStats(pid, achatstockQte, achatstockPrixTotal, commandeQte, commandePrixTotal, stockQte, 
					stockPrixMoyen, pendingQte, panierQte, casseQte, perteQte, lockedQte);
			insert(produitstats);
		}
		else	
			update(produitstats, pid, achatstockQte, achatstockPrixTotal, commandeQte, commandePrixTotal, stockQte,
					stockPrixMoyen, pendingQte, panierQte, casseQte, perteQte, lockedQte);
	}

	
	
	private void update(ProduitStats produitstats,Long oldID,  Long achatstockQte, Double achatstockPrixTotal,
			Long commandeQte, Double commandePrixTotal, Long stockQte, Double stockPrixTotal, Long pendingQte,
			Long panierQte, Long casseQte, Long perteQte, Long lockedQte) throws Exception {
		if(produitstats == null)
		{
			produitstats = new ProduitStats(oldID, achatstockQte, achatstockPrixTotal, commandeQte, 
					commandePrixTotal, stockQte, stockPrixTotal, pendingQte, panierQte, casseQte, perteQte, lockedQte);
			insert(produitstats);
		}
		else
		{
			produitstats.setAchatstockQte(achatstockQte);
			produitstats.setAchatstockPrixTotal(achatstockPrixTotal);
			produitstats.setCommandeQte(commandeQte);
			produitstats.setCommandePrixTotal(commandePrixTotal);
			produitstats.setStockQte(stockQte);
			produitstats.setStockPrixMoyen(stockPrixTotal);
			produitstats.setPendingQte(pendingQte);
			produitstats.setPendingQte(pendingQte);
			produitstats.setCasseQte(casseQte);
			produitstats.setPerteQte(perteQte);
			produitstats.setLockedQte(lockedQte);

			update(produitstats);
		}
	}
	

	public void addNewProduitStats(AchatStockItemDB nnew) throws Exception {
		addNewProduitStats( nnew.getCode().startsWith("VR"), nnew.getVariationID());
	}
	

	public void addNewProduitStats(StockDB nnew) throws Exception {
		addNewProduitStats( nnew.getCode().startsWith("VR"), nnew.getVariationID());
	}
	
	public ProduitStats recalculeProduitStats(ProduitStats pstats, boolean isvariation, Long varid ) throws Exception {

		calculeAllStats(isvariation, varid, pstats);
		//pstats.log();
		return update(pstats);
	}

	public ProduitStats addNewProduitStats( boolean isvariation, Long varid) throws Exception {

		ProduitStats pstats = new ProduitStats();
		calculeAllStats(isvariation, varid, pstats);
		return insert(pstats);
	}
	private void calculeAllStats(boolean isvariation, Long varid, ProduitStats pstats) throws Exception{
		
		AccesCommandeItem accescomitem = new AccesCommandeItem();
		AccesCommande accescom  = new AccesCommande();
		AccesStock accessku     = new AccesStock();
		AccesAchatStockItem accesaskui = new AccesAchatStockItem();
		AccesCasse accescasse   = new AccesCasse();
		AccesPerte accesperte   = new AccesPerte();
		AccesPanier accespanier = new AccesPanier();

		List<CommandeItemDB> cmditems = null;
		List<StockDB> 		   stocks = null;
		List<AchatStockItemDB> askuis = null;
		List<CasseDB> 		   casses = null;
		List<PerteDB> 		   pertes = null;
		List<PanierDB> 		  paniers = null;

		/** Cas de variation de produit - Extraction des données necessaires */
		if (isvariation) {
			AccesVariations accesvar = new AccesVariations();
			VariationDB var = accesvar.getByID(varid);

			if (var == null)
				throw new NotFoundException("Variation " + varid + " introuvable");

			pstats.setVariationID(var.getVariationID());
			pstats.setProduitID(var.getProduitID());

			casses  = accescasse.selectWhere("variationID = :s0", var.getVariationID());
			pertes  = accesperte.selectWhere("variationID = :s0", var.getVariationID());
			paniers = accespanier.selectWhere("variationID = :s0", var.getVariationID());
			
			cmditems = accescomitem.dynamiqueSelect("variationID = ", var.getVariationID(), "code like ", "V\\_%");
			askuis = accesaskui.dynamiqueSelect("variationID =", var.getVariationID(), "code like ", "VR\\_%");
			stocks = accessku.dynamiqueSelect("variationID = ", var.getVariationID(), "code like ", "VR\\_%");
			
		} 
		/** Cas de produit - Extraction des données necessaires */
		else {

			AccesProduitsAdmin accesprod = new AccesProduitsAdmin();
			ProduitDB prod = accesprod.getByID(varid);

			if (prod == null)
				throw new NotFoundException("Produit " + varid + " introuvable");

			pstats.setProduitID(prod.getProduitID());

			casses  = accescasse.selectWhere("produitID = :s0 and variationID is null", prod.getProduitID());
			pertes  = accesperte.selectWhere("produitID = :s0 and variationID is null", prod.getProduitID());
			paniers = accespanier.selectWhere("produitID = :s0 and variationID is null", prod.getProduitID());
			
			cmditems = accescomitem.dynamiqueSelect("variationID = ", prod.getProduitID(), "code not like ","V\\_%");
			askuis = accesaskui.dynamiqueSelect("variationID =", prod.getProduitID(), "code like ", "PR\\_%");
			stocks = accessku.dynamiqueSelect("variationID = ", prod.getProduitID(), "code not like ", "VR\\_%");
		}
		/** calculs */
		calculeCommandesStats(pstats, accescom, cmditems);
		calculeCasseStats(pstats, casses);
		calculePerteStats(pstats, pertes);
		calculePanierStats(pstats, paniers);
		calculeAchatStockStats(pstats, askuis);
		calculeStockStats(pstats, accesaskui, stocks);

		/** TODO add
		 * private Long favorisQte; nope 
		 * private Long casseQte; ? 
		 * private Long perteQte; ?
		 * private Long visiteCount;
		 */
	}

	

	private void calculePerteStats(ProduitStats pstats, List<PerteDB> pertes) {
		// calcule perteQte
		if(pertes == null)
			return;
		Long perteqte = 0l;
		
		for (PerteDB perte : pertes)
			perteqte += perte.getQte();
		
		pstats.setPerteQte(perteqte);
	}

	
	
	private void calculeCasseStats(ProduitStats pstats, List<CasseDB> casses) {
		// calcule casseQte
		if(casses == null)
			return;
		Long casseqte = 0l;
		
		for (CasseDB casse : casses)
			casseqte += casse.getQte();
		
		pstats.setCasseQte(casseqte);
	}

	
	
	private void calculePanierStats(ProduitStats pstats, List<PanierDB> panitems) {
		// calcule panierQte
		if(panitems == null)
			return;
		Long panierqte = 0l;
		
		for (PanierDB pan : panitems) 
			panierqte += pan.getQte();
		
		pstats.setPanierQte(panierqte);
		
	}

/* --------------------------------- commande stats --------------------------------- */
	

	public void addCommandeStats(CommandeDB cmd) throws Exception {
		
		List<CommandeItemDB> items = new AccesCommandeItem().getEqualsList("commandeID", cmd.getCommandeID());

		ProduitStats pstats = null;
		
		AccesProduitsAdmin accesp = new AccesProduitsAdmin(AccesTable.READONLY);
		AccesVariations    accesv = new AccesVariations(AccesTable.READONLY);
		
		for(CommandeItemDB item : items) 
		{
			String code = item.getCode();
			
			long produitid;
			Long variationid = null;
			
			if(code.startsWith("V_"))
			{
				VariationDB vr = accesv.getEquals("code", code);
				produitid   = vr.getProduitID();
				variationid = vr.getVariationID();
			}
			else 
				produitid = accesp.getEquals("code", code).getProduitID(); 
			
			pstats = code.startsWith("V_") ? getEquals("variationID", variationid) :
				selectWhereFirst("produitID = :s0 AND variationID is null", produitid );

			if(pstats == null)
			{
				pstats = new ProduitStats();
				pstats.setLockedQte(item.getQte());
				pstats.setProduitID(produitid);
				if(variationid != null) 
					pstats.setVariationID(variationid);
				insert(pstats);
			}
			else {
				pstats.setLockedQte(pstats.getLockedQte() + item.getQte());
				update(pstats);
			}
			
		}
	}
	
	
	public void updateCommandeStats(CommandeDB old, CommandeDB cmd) throws Exception {
		/** if etat changes data
		    get items
		    foreach getstats, add data */
		if(old.getEtat().equals(cmd.getEtat())) // etat identique
			return;
		if(v().isLockedCommandeEtat(cmd.getEtat()))
			return;
		
		List<CommandeItemDB> items = new AccesCommandeItem().getEqualsList("commandeID", cmd.getCommandeID());

		ProduitStats pstats = null;
		AccesProduitsAdmin accesp = new AccesProduitsAdmin(AccesTable.READONLY);
		AccesVariations accesv = new AccesVariations(AccesTable.READONLY);
		
		if(items == null)
			return;
		
		for(CommandeItemDB item : items) 
		{
			String code = item.getCode();

			long produitid;
			Long variationid = null;
			
			if(code.startsWith("V_"))
			{
				VariationDB vr = accesv.getEquals("code", code);
				produitid   = vr.getProduitID();
				variationid = vr.getVariationID();
			}
			else 
				produitid = accesp.getEquals("code", code).getProduitID(); 
			
			pstats = code.startsWith("V_") ? getEquals("variationID", variationid) :
				selectFirst("SELECT P FROM "+TABLE.getSimpleName()+" P "+
				"WHERE P.produitID = :s0 AND P.variationID is null", produitid );

			if(pstats == null)
			{
				pstats = new ProduitStats();
				pstats.setProduitID(produitid);
				
				if(variationid != null) 
					pstats.setVariationID(variationid);
				
				if (cmd.getEtat().equals(CommandeDB.COMPLETE)) {
					
					pstats.setCommandeQte(item.getQte());
					pstats.setCommandePrixTotal(item.getPrixUnite() * item.getQte());
					pstats.setFraisSup(cmd.surplusLivraison(item.getPrixUnite()));
				} 
				else if (v().isLockedCommandeEtat(cmd.getEtat())) {
					pstats.setLockedQte(item.getQte());
				}
				
				insert(pstats);
			}
			else {

				//if(v().isLockedCommandeEtat(old.getEtat()))
				//	pstats.setLockedQte(pstats.getLockedQte() + item.getQte());
				if (cmd.getEtat().equals(CommandeDB.COMPLETE)) {
					Double newfraissup = pstats.getFraisSup() * pstats.getCommandeQte() + 
							cmd.surplusLivraison(item.getPrixUnite()) * item.getQte();
					
					pstats.setCommandeQte(pstats.getCommandeQte() + item.getQte());
					pstats.setCommandePrixTotal(item.getPrixUnite()* item.getQte() + pstats.getCommandePrixTotal());
					pstats.setFraisSup(newfraissup / pstats.getCommandeQte());
				}
				pstats.setLockedQte(pstats.getLockedQte() - item.getQte());
				
				update(pstats);
			}
		}
	}
	
	private void calculeCommandesStats(ProduitStats pstats, AccesCommande accescom, List<CommandeItemDB> cmditems) {
		/* calcul de la quantité des pièces commandées & le prix total des commandes */
		Double surplusLivraison = 0d;
		Long cmdqte    = 0l;
		Long lockedqte = 0l;
		Double cmdPrixTotal = 0d;
		CommandeDB cmd = null;
		
		HashMap<Long,CommandeDB> map = new HashMap<>();

		if(cmditems != null)
		for (CommandeItemDB i : cmditems) {
			
			if((cmd = map.get(i.getCommandeID())) == null) 
				map.put(i.getCommandeID(), (cmd = accescom.getByID(i.getCommandeID())));
			
					
			if (cmd.getEtat().equals(CommandeDB.COMPLETE)) {
				surplusLivraison += cmd.surplusLivraison(i.getPrixUnite()) * i.getQte();
				cmdqte += i.getQte();
				cmdPrixTotal += i.getPrixUnite() * i.getQte();
				
			}
			else if (v().isLockedCommandeEtat(cmd.getEtat())) {
				lockedqte +=i.getQte();
			}
		}
		
		pstats.setLockedQte(lockedqte);
		pstats.setCommandeQte(cmdqte);
		pstats.setCommandePrixTotal(cmdPrixTotal);
		surplusLivraison = cmdqte!= null && cmdqte!= 0 ? surplusLivraison / cmdqte : 0;
		pstats.setFraisSup(surplusLivraison);
	}
/* --------------------------------- achat stock stats --------------------------------- */
	
	
	private void calculeAchatStockStats(ProduitStats pstats, List<AchatStockItemDB> askuis) {
		
		Long askuqte = 0l;
		Long pendingQte = 0l;
		Double askuprixtotal = 0d;
		/* calcul achat stock stuff */
		if(askuis != null)
		for (AchatStockItemDB ai : askuis) {
			askuqte += ai.getQte();
			askuprixtotal += ai.getCoutTotal();
			if (!ai.getAssocie())
				pendingQte += ai.getQte();
		}

		pstats.setAchatstockPrixTotal(askuprixtotal);
		pstats.setAchatstockQte(askuqte);
		pstats.setPendingQte(pendingQte);
	}

	public void removeAchatStockToProduitStats( ProduitStats pstats, AchatStockItemDB askui) throws Exception {

		Long askuqte = pstats.getAchatstockQte() - askui.getQte();
		Long pendingQte = pstats.getPendingQte() - ( askui.getAssocie() ? 0 : askui.getQte());
		Double askuprixtotal = pstats.getAchatstockPrixTotal() - askui.getCoutTotal();

		pstats.setAchatstockPrixTotal(askuprixtotal);
		pstats.setPendingQte(pendingQte);
		pstats.setAchatstockQte(askuqte);
		
		update(pstats);
	}
	public void addAchatStockToProduitStats( ProduitStats pstats, AchatStockItemDB askui) throws Exception {

		Long askuqte = pstats.getAchatstockQte() + askui.getQte();
		Long pendingQte = pstats.getPendingQte() + ( askui.getAssocie() ? 0 : askui.getQte());
		Double askuprixtotal = pstats.getAchatstockPrixTotal() + askui.getCoutTotal();

		pstats.setAchatstockPrixTotal(askuprixtotal);
		pstats.setPendingQte(pendingQte);
		pstats.setAchatstockQte(askuqte);
		
		update(pstats);
	}


	public void updateAchatStockStats(ProduitStats pstats, AchatStockItemDB nnew) throws Exception {

		if (nnew.getCode().startsWith("VR"))
			updateVariationAchatStockStats(pstats, nnew.getVariationID());
		else
			updateProduitAchatStockStats(pstats, nnew.getVariationID());
	}
	
	
	
	public void updateVariationAchatStockStats(ProduitStats pstats, Long vid) throws Exception {

		AccesAchatStockItem accesaskui = new AccesAchatStockItem();
		List<AchatStockItemDB> askuis = accesaskui.dynamiqueSelect("variationID =", vid, "code like ", "VR\\_%");
		calculeAchatStockStats(pstats, askuis);
		
		update(pstats);
	}
	
	
	
	public void updateProduitAchatStockStats(ProduitStats pstats, Long pid) throws Exception {
 
		AccesAchatStockItem accesaskui = new AccesAchatStockItem();
		List<AchatStockItemDB> askuis = accesaskui.dynamiqueSelect("variationID = ", pid, "code like ", "PR\\_%");
		calculeAchatStockStats(pstats, askuis);

		update(pstats);
	}
	
/* --------------------------------- stock stats --------------------------------- */
	
	public void addStockToProduitStats( ProduitStats pstats, AchatStockItemDB askui, StockDB stock ) throws Exception {
		
		Long skuqte = stock.getQte() + pstats.activeStock();
		Double skuprixtotal = pstats.getStockPrixMoyen() * pstats.getStockQte() + askui.getCoutTotal();
		pstats.setStockQte(skuqte);
		pstats.setStockPrixMoyen(skuprixtotal / skuqte);
		
		update(pstats);
	}
	
	

	public void updateStockStats(ProduitStats pstats, StockDB nnew) throws Exception {

		if (nnew.getCode().startsWith("VR"))
			updateVariationStockStats(pstats, nnew.getVariationID());
		else
			updateProduitStockStats(pstats, nnew.getVariationID());
	}
	
	
	
	public void updateVariationStockStats(ProduitStats pstats, Long vid) throws Exception {

		AccesStock 			  accessku = new AccesStock();
		AccesAchatStockItem accesaskui = new AccesAchatStockItem();

		List<AchatStockItemDB> askuis = accesaskui.dynamiqueSelect("variationID =", vid, "code like ", "VR\\_%");
		List<StockDB> stocks = accessku.dynamiqueSelect("variationID = ", vid, "code like ", "VR\\_%");

		calculeAchatStockStats(pstats, askuis);
		calculeStockStats(pstats, accesaskui, stocks);
		
		update(pstats);
	}
	
	
	public void updateProduitStockStats(ProduitStats pstats, Long pid) throws Exception {

		AccesStock 			  accessku = new AccesStock();
		AccesAchatStockItem accesaskui = new AccesAchatStockItem();
		
		List<AchatStockItemDB> askuis = accesaskui.dynamiqueSelect("variationID = ", pid, "code like ", "PR\\_%");
		List<StockDB> stocks = accessku.dynamiqueSelect("variationID = ", pid, "code not like ", "VR\\_%");

		calculeAchatStockStats(pstats, askuis);
		calculeStockStats(pstats, accesaskui, stocks);

		update(pstats);
	}


	/*
	 *  accepted stock = achat stock qte - pending qte
	 *  removed stock = commande qte + locked qte + perte qte + casse qte
	 *  active stock   = accepted stock - removed stock
	 */
	
	private void calculeStockStats(ProduitStats pstats, AccesAchatStockItem accesaskui, List<StockDB> stocks) {

		// Long disabledsku = 0l; 
		/** TODO add disabledsku to produitStats ?? */
		if(pstats.activeStock() == 0)
		{
			pstats.setStockQte(0l);
			pstats.setStockPrixMoyen(0d);
			return;
		}
		Long removedsku = - pstats.removedStock(), subqte;
		Long skuqte = 0l;
		
		Double skuprixtotal = 0d;
		AchatStockItemDB ai = null;
		
		if(stocks != null)
		for (StockDB i : stocks) {

			if (removedsku < 0) 
				removedsku += i.getQte();

			if(removedsku < 0)
				continue;

			ai	   = accesaskui.getByID(i.getAchatStockItemID());
			subqte = removedsku > 0 ? removedsku : i.getQte();
			skuqte += subqte;
			skuprixtotal += ai.coutUnite() * subqte;
			removedsku = 0l;		
		}

		pstats.setStockQte(skuqte);
		double skupm = skuqte == 0 ? 0 :skuprixtotal/skuqte;
		pstats.setStockPrixMoyen(skupm);
	}
	
	@Override
	public ProduitStats update(ProduitStats t) throws Exception {
		t.setStockQte(t.activeStock());
		return super.update(t);
	}
	
	public void refreshStatsProduits() throws Exception{
		AccesProduitsAdmin accesprod = new AccesProduitsAdmin();
		
		List<ProduitAdmin> prods = accesprod.getEqualsList("hasVariations", false);
		if(prods != null)
			for(ProduitAdmin p : prods)
			{
				ProduitStats ps = selectWhereFirst("produitID = :s0 AND variationID is null", p.getProduitID());
				System.err.println(ps == null ? "null" : ps.getProduitID());
				if(ps == null)
					addNewProduitStats(false , p.getProduitID());
				else
					recalculeProduitStats(ps, false, p.getProduitID());
			}
	}
	
	public ProduitStats refreshStatsProduit(Long id) throws Exception {
		
		ProduitStats ps = selectWhereFirst("produitID = :s0 AND variationID is null", id);
		
		if(ps == null)
		{
			ProduitAdmin prod = new AccesProduitsAdmin().getByID(id);
			if(prod == null)
				throw new Exception("produit "+id+" introuvable");
			return addNewProduitStats(false , id);
		}
		else
			return recalculeProduitStats(ps, false, ps.getProduitID());
	}


	public void refreshStatsVariation(Long id) throws Exception {

		ProduitStats ps = getEquals("variationID", id);
		if(ps == null)
		{
			VariationDB prod = new AccesVariations().getByID(id);
			if(prod == null)
				throw new Exception("variation "+id+" introuvable");
			addNewProduitStats(true , id);
		}
		else
			recalculeProduitStats(ps, true, ps.getProduitID());
	}
	
	
	public void refreshStatsVariations() throws Exception{
		AccesVariations accesvar = new AccesVariations();
		
		List<VariationDB> variations = accesvar.getAll();
		
		if(variations != null)
			for(VariationDB v : variations)
			{
				ProduitStats ps = getEquals("variationID", v.getVariationID());
				if(ps == null)
					addNewProduitStats(true , v.getVariationID());
				else
					recalculeProduitStats(ps, true, v.getVariationID());
			}
	}
	public ProduitStats getProduitStatsByProduitCode(String code) {

		ProduitAdmin prod = new AccesProduitsAdmin().getByCode(code);
		return selectWhereFirst("produitID = :s0 AND variationID is null", prod.getProduitID());
	}
}





