package ma.otaku.acces.triggers.achatstock;

import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stats.ProduitStats;
import ma.otaku.data.stock.AchatStockItemDB;

@Mode(Mode.AFTER)
public class AfterUpdateAchatStockItem extends Trigger<AchatStockItemDB> {


	public AfterUpdateAchatStockItem() {
	}

	@Override
	public void execute(AchatStockItemDB old, AchatStockItemDB t) throws Exception {

		AccesProduitStats accesstats = new AccesProduitStats();
		ProduitStats pstats = accesstats.getEquals(t.getCode().startsWith("VR")?"variationID":"ProduitID", t.getVariationID());

		if (pstats == null) 
			accesstats.addNewProduitStats(t);
		else
			accesstats.updateAchatStockStats(pstats, t);
	}
}