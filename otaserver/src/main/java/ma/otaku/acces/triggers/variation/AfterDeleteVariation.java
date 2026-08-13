package ma.otaku.acces.triggers.variation;

import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.produit.VariationDB;
@Mode(Mode.AFTER)
public class AfterDeleteVariation extends Trigger<VariationDB> {
	
	@Override
	public void execute(VariationDB old, VariationDB nnew) throws Exception {
		// TODO Auto-generated method stub
		
}
}
