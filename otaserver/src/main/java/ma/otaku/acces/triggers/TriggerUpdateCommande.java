package ma.otaku.acces.triggers;

public class TriggerUpdateCommande {
/*
	private int mode;
	
	public TriggerUpdateCommande() { setMode(AFTER); }
	
	@Override
	public void execute(CommandeDB old, CommandeDB t) throws Exception {
		AccesCommandeItem accesitems = new AccesCommandeItem();
		if(t.getEtat().equals(CommandeDB.ENVOYEE) || t.getEtat().equals(CommandeDB.REFUSEE) || t.getEtat().equals(CommandeDB.ANNULEE)) {
			
			List<CommandeItemDB> liste = accesitems.getEqualsList("commandeID", t.getCommandeID());
			
			AccesProduitsAdmin accesprod = new AccesProduitsAdmin();
			AccesVariations accesvar = new AccesVariations();
			
			for(CommandeItemDB c : liste)
			{
				if(c.getCode().startsWith("V_")) {
					VariationDB v = accesvar.getEquals("code", c.getCode());

					Long lqte = Math.max(v.getLockedQte() - c.getQte(), 0);
					v.setLockedQte(lqte);
					
					if(t.getEtat().equals(CommandeDB.ENVOYEE) ) 
						v.setQte(Math.max(v.getQte() - c.getQte(), 0));
					
					accesvar.update(v);
				}
				else {
					ProduitAdmin v = accesprod.getEquals("code", c.getCode());

					Long lqte = Math.max(v.getLockedQte() - c.getQte(), 0);
					v.setLockedQte(lqte);
					
					if(t.getEtat().equals(CommandeDB.ENVOYEE) )
						v.setQte(Math.max(v.getQte() - c.getQte(), 0));
					
					accesprod.update(v);
				}
			}
		}
		
	}
*/

}
