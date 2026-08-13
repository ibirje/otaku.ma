package ma.otaku.acces.triggers.commande;

import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.client.CommandeDB;

@Mode(Mode.AFTER)
public class AfterUpdateCommande extends Trigger<CommandeDB>{

	@Override
	public void execute(CommandeDB old, CommandeDB nnew) throws Exception {

		new AccesProduitStats().updateCommandeStats(old, nnew);
	}

}
