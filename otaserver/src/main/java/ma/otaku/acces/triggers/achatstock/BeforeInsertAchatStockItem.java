package ma.otaku.acces.triggers.achatstock;

import ma.otaku.acces.stock.AccesAchatStock;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.stock.AchatStockDB;
import ma.otaku.data.stock.AchatStockItemDB;

@Mode(Mode.BEFORE)
public class BeforeInsertAchatStockItem  extends Trigger<AchatStockItemDB>{

	@Override
	public void execute(AchatStockItemDB old, AchatStockItemDB nnew) throws Exception {
		// maybe move verifications ici ??
		calculeCoutTotal(nnew);
	}


	private void calculeCoutTotal(AchatStockItemDB nnew) {
		AccesAchatStock accesachat = new AccesAchatStock();
		AchatStockDB achat = accesachat.getByID(nnew.getAchatStockID());
		
/** https://docs.google.com/spreadsheets/d/1CRbZbI1ddfbux5FchtfxHs8AF0a23HlroMKN4SITzPw/edit#gid=0 */
		Double prixTotal = nnew.getPrixUnite() * nnew.getQte();
		Double coutTotal = prixTotal + (prixTotal / achat.getPrixTotal()) * achat.getFraisSupplementaires();
		nnew.setCoutTotal(coutTotal);
	}
}
