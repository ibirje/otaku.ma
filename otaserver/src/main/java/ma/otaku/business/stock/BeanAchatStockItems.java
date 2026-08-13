package ma.otaku.business.stock;

import java.util.HashSet;

import ma.otaku.data.stock.AchatStockDB;
import ma.otaku.data.stock.AchatStockItemDB;

public class BeanAchatStockItems 
{
	AchatStockDB achat;
	HashSet<AchatStockItemDB> achatItems;
	
	
	public AchatStockDB getAchat() 
	{
		return achat;
	}
	
	public void setAchat(AchatStockDB achat) 
	{
		this.achat = achat;
	}
	
	public HashSet<AchatStockItemDB> getAchatItems() 
	{
		return achatItems;
	}
	
	public void setAchatItems(HashSet<AchatStockItemDB> achatItems) 
	{
		this.achatItems = achatItems;
	}
}
