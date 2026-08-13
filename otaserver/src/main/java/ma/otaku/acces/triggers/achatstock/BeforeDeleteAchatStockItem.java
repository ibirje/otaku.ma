package ma.otaku.acces.triggers.achatstock;

import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stock.AchatStockItemDB;

@Mode(Mode.BEFORE)
public class BeforeDeleteAchatStockItem extends Trigger<AchatStockItemDB>{

	@Override
	public void execute(AchatStockItemDB old, AchatStockItemDB nnew) throws Exception {

		if(old.getAssocie())
			throw new Exception("AchatStockItem ne peut être supprimé parce qu'il est associé à un stock");
	}

}
