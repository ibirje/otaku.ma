package ma.otaku.acces.triggers.achatstock;

import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stats.ProduitStats;
import ma.otaku.data.stock.AchatStockItemDB;

@Mode(Mode.AFTER)
public class AfterDeleteAchatStockItem extends Trigger<AchatStockItemDB> {

	@Override
	public void execute(AchatStockItemDB old, AchatStockItemDB nnew) throws Exception {
		// if( stats doesnt exist, create new
		// if exist sum all or remove numbers ??

		AccesProduitStats acces = new AccesProduitStats();
		ProduitStats pstats = acces.getEquals(nnew.getCode().startsWith("VR")?"variationID":"ProduitID", nnew.getVariationID());

		if (pstats == null) 
			acces.addNewProduitStats(nnew);
		else
			acces.removeAchatStockToProduitStats(pstats, nnew);
	}

}
