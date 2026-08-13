package ma.otaku.acces.triggers.produitstats;

import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.produit.AccesProduitsAdmin;
import ma.otaku.acces.produit.AccesVariations;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.produit.VariationDB;
import ma.otaku.data.stats.ProduitStats;

@Mode(Mode.AFTER)
public class AfterInsertProduitStats extends Trigger<ProduitStats> {

	AccesProduitStats acces;

	public AfterInsertProduitStats(AccesProduitStats acc) {
		acces = acc;
	}
	
	@Override
	public void execute(ProduitStats old, ProduitStats nnew) throws Exception {
		
		if(nnew.getVariationID() != null) 
		{
			acces.addVariationStatsToProduitStats(nnew);

			AccesVariations accesvar = new AccesVariations();
			VariationDB var  = accesvar.getByID(nnew.getVariationID());
			
			var.setQte(nnew.activeStock());
			var.setPendingQte(nnew.getPendingQte());
			var.setLockedQte(nnew.getLockedQte());
			accesvar.update(var);
			
			return;
		}

		AccesProduitsAdmin accesprod = new AccesProduitsAdmin();

		ProduitAdmin prod  = accesprod.getByID(nnew.getProduitID());
		
		prod.setQte(nnew.activeStock());
		prod.setPendingQte(nnew.getPendingQte());
		prod.setLockedQte(nnew.getLockedQte());
		accesprod.update(prod);

		// new AccesCategorie().resumeCategorie(prod.getCategorieID(), accesprod, acces, nnew);
		// new AccesTheme().resumeTheme(prod.getThemeID(), accesprod, acces, nnew);
	}
}
