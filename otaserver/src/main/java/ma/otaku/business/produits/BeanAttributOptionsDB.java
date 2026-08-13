package ma.otaku.business.produits;

import java.util.ArrayList;
import java.util.List;

import ma.otaku.data.produit.AttributDB;
import ma.otaku.data.produit.OptionAttributDB;

public class BeanAttributOptionsDB 
{
	private AttributDB attribut;
	private List<OptionAttributDB> options;
	
	public BeanAttributOptionsDB(BeanAttributOptions bean) 
	{
		attribut = new AttributDB(bean.getAttribut());
		options = new ArrayList<>();
		for(OptionAttribut option : bean.getOptions())  options.add(new OptionAttributDB(option));
	}
	
	public BeanAttributOptionsDB() 
	{
		
	}


	public AttributDB getAttribut() {
		return attribut;
	}


	public void setAttribut(AttributDB attribut) {
		this.attribut = attribut;
	}


	public List<OptionAttributDB> getOptions() {
		return options;
	}


	public void setOptions(List<OptionAttributDB> options) {
		this.options = options;
	}
	
	
}
