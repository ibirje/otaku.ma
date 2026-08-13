package ma.otaku.acces.triggers.stock;

import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stock.StockDB;

@Mode(Mode.BEFORE)
public class BeforeDeleteStock extends Trigger<StockDB>{

	@Override
	public void execute(StockDB old, StockDB nnew) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
