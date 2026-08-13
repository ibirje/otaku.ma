package ma.otaku.acces.triggers.stock;

import ma.otaku.acces.stock.AccesAchatStockItem;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stock.AchatStockItemDB;
import ma.otaku.data.stock.StockDB;

@Mode(Mode.BEFORE)
public class BeforeInsertStock extends Trigger<StockDB> {

	//HashSet<Trigger> sub;
	
	public BeforeInsertStock() {
		
		// sub = new HashSet<>();
		// sub.add(new TriggerQteNegative());
	}
	
	@Override
	public void execute(StockDB old, StockDB nnew) throws Exception {

		if(nnew.getQte() < 0) 
			throw new Exception("La quantité du stock à insérer est negative");
		
		AccesAchatStockItem accesai = new AccesAchatStockItem();
		
		AchatStockItemDB achatitem = accesai.getByID(nnew.getAchatStockItemID());
		
		if(achatitem == null) 
			throw new NullPointerException("Achat item associé inexistant");
		
		if(achatitem.getAssocie())
			throw new Exception("Achat stock déjà associé à un autre stock");
		
		// for(Trigger t : sub) 
		//	t.execute(old, nnew);
	}

}
