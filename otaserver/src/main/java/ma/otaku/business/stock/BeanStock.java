package ma.otaku.business.stock;

import java.util.List;

import ma.otaku.data.stock.StockDB;

public class BeanStock {

    private List<StockDB> stock;
    private String achatCode;
    
	public List<StockDB> getStock() {
		return stock;
	}
	public void setStock(List<StockDB> stock) {
		this.stock = stock;
	}
	public String getAchatCode() {
		return achatCode;
	}
	public void setAchatCode(String achatCode) {
		this.achatCode = achatCode;
	}
    
    
}
