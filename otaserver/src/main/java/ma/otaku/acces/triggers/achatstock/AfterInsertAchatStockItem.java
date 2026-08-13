package ma.otaku.acces.triggers.achatstock;

import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stats.ProduitStats;
import ma.otaku.data.stock.AchatStockItemDB;

@Mode(Mode.AFTER)
public class AfterInsertAchatStockItem extends Trigger<AchatStockItemDB> {

	public AfterInsertAchatStockItem() {
		
	}

	@Override
	public void execute(AchatStockItemDB old, AchatStockItemDB nnew) throws Exception {

		AccesProduitStats acces = new AccesProduitStats();
		ProduitStats pstats = acces.getEquals(nnew.getCode().startsWith("VR")?"variationID":"ProduitID", nnew.getVariationID());

		if (pstats == null) 
			acces.addNewProduitStats(nnew);
		else
			acces.addAchatStockToProduitStats(pstats, nnew);
	}
	
}










