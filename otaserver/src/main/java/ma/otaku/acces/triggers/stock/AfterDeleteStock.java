package ma.otaku.acces.triggers.stock;

import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stats.ProduitStats;
import ma.otaku.data.stock.StockDB;

@Mode(Mode.AFTER)
public class AfterDeleteStock extends Trigger<StockDB>{

	@Override
	public void execute(StockDB old, StockDB useless) throws Exception {
		
		updateProduitStats(old);
	}


	/**  ajoute les données du nouveau stock, aux statistiques du produit associé  */
	private void updateProduitStats(StockDB nnew) throws Exception {
		AccesProduitStats accesstats = new AccesProduitStats();
		ProduitStats pstats = accesstats.getEquals(nnew.getCode().startsWith("VR")?"variationID":"ProduitID", nnew.getVariationID());

		if (pstats == null) 
			accesstats.addNewProduitStats(nnew);
		else
			accesstats.updateStockStats(pstats, nnew);
	}

}
