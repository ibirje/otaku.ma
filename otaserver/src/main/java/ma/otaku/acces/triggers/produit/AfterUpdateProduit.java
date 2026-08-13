package ma.otaku.acces.triggers.produit;

import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.produit.AccesProduits;
import ma.otaku.acces.produit.AccesTheme;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.produit.ProduitDB;

@Mode(Mode.AFTER)
public class AfterUpdateProduit extends Trigger<ProduitDB>{

	AccesProduits<? extends ProduitDB> acces;

	public AfterUpdateProduit(AccesProduits<? extends ProduitDB> acces) {
		this.acces = acces;
	}
	
	@Override
	public void execute(ProduitDB old, ProduitDB nnew) throws Exception {

		
		AccesProduitStats accesstats = new AccesProduitStats();
		
		new AccesCategorie().resumeCategorie(nnew.getCategorieID(), acces, accesstats);
		new AccesTheme().resumeTheme(nnew.getThemeID(), acces, accesstats);
	}
}