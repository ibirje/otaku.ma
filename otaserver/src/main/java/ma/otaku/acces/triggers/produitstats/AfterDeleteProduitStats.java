package ma.otaku.acces.triggers.produitstats;

import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.produit.AccesProduitsAdmin;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.stats.ProduitStats;
@Mode(Mode.AFTER)
public class AfterDeleteProduitStats extends Trigger<ProduitStats> {
	
	AccesProduitStats acces;

	public AfterDeleteProduitStats(AccesProduitStats acc) {
		acces = acc;
	}
	
	@Override
	public void execute(ProduitStats old, ProduitStats nnew) throws Exception {
		
		if(nnew.getVariationID() != null) 
		{
			acces.sumVariationStatsToProduitStats(old);
			return;
		}


		AccesProduitsAdmin accesprod = new AccesProduitsAdmin();

		ProduitAdmin prod  = accesprod.getByID(nnew.getProduitID());
		
		prod.setQte(nnew.activeStock());
		prod.setPendingQte(nnew.getPendingQte());
		prod.setLockedQte(nnew.getLockedQte());
		accesprod.update(prod);
	}

}
