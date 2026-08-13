package ma.otaku.acces.triggers.stock;

import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stock.StockDB;

@Mode(Mode.BEFORE)
public class BeforeUpdateStock extends Trigger<StockDB> {


	//private AccesStock acces;
	public BeforeUpdateStock() {
		//this.acces = acces;
	}
	
	/*
	 * if
	 * insert sku + qte += newsku.qte  x
	 * delete sku qte -= oldsku.qte
	 * update sku qte +-= qte += newsku.qte x
	 * 
	 */
	
	@Override
	public void execute(StockDB old, StockDB t) throws Exception {

	}
}
