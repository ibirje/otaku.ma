package ma.otaku.acces.triggers.stock;

import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.stock.AccesAchatStockItem;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stats.ProduitStats;
import ma.otaku.data.stock.AchatStockItemDB;
import ma.otaku.data.stock.StockDB;


@Mode(Mode.AFTER)
public class AfterInsertStock extends Trigger<StockDB> {

	// HashSet<Trigger> sub;

	
	public AfterInsertStock() {
		// sub = new HashSet<>();
	}
	
	
	
	@Override
	public void execute(StockDB old, StockDB nnew) throws Exception {
		
		AchatStockItemDB achatitem = associerAchatStock(nnew);
		updateProduitStats(nnew, achatitem);

		/* for (Trigger t : sub) t.execute(old, nnew); */
	}

	
	
	/**  ajoute les données du nouveau stock, aux statistiques du produit associé  */
	private void updateProduitStats(StockDB nnew, AchatStockItemDB achatitem) throws Exception {
		
		AccesProduitStats accesstats = new AccesProduitStats();
		ProduitStats pstats = accesstats.getEquals(nnew.getCode().startsWith("VR")?"variationID":"ProduitID", nnew.getVariationID());

		if (pstats == null) 
			accesstats.addNewProduitStats(nnew);
		else
			accesstats.addStockToProduitStats(pstats, achatitem, nnew);
	}

	
	
	/*******************  associer l'achat stock au stock  *******************/
	private AchatStockItemDB associerAchatStock(StockDB nnew) throws Exception {
		
		AccesAchatStockItem accesai = new AccesAchatStockItem();
		AchatStockItemDB achatitem = accesai.getByID(nnew.getAchatStockItemID());
		
		if (achatitem == null)
			throw new NullPointerException("Achat item associé inexistant");
		
		achatitem.setAssocie(true);
		accesai.update(achatitem);

		return achatitem;
	}
	
}





