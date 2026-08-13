package ma.otaku.acces.triggers.produit;

import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.produit.ProduitDB;

@Mode(Mode.AFTER)
public class AfterDeleteProduit extends Trigger<ProduitDB>{

	@Override
	public void execute(ProduitDB old, ProduitDB nnew) throws Exception {
		// refresh nombre produits in theme / categorie
		
	}

}
