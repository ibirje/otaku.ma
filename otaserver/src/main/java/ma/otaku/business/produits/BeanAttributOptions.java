package ma.otaku.business.produits;

import java.util.ArrayList;
import java.util.List;

import ma.otaku.data.produit.OptionAttributDB;

public class BeanAttributOptions {

	private Attribut attribut;
	private List<OptionAttribut> options;
	
	
	public BeanAttributOptions() {
	}
	
	public BeanAttributOptions(BeanAttributOptionsDB bean) 
	{
		attribut = new Attribut(bean.getAttribut());
		options = new ArrayList<>();
		for(OptionAttributDB option : bean.getOptions())  options.add(new OptionAttribut(option));
	}
	
	public BeanAttributOptions(Attribut attribut, List<OptionAttribut> options) 
	{
		super();
		this.attribut = attribut;
		this.options = options;
	}

	public Attribut getAttribut() {
		return attribut;
	}
	public void setAttribut(Attribut attribut) {
		this.attribut = attribut;
	}
	public List<OptionAttribut> getOptions() {
		return options;
	}
	public void setOptions(List<OptionAttribut> options) {
		this.options = options;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		BeanAttributOptions bean = (BeanAttributOptions) obj;
		if(!attribut.equals(bean.getAttribut()))
			return false;
		if(options.size() != bean.getOptions().size())
			return false;
		
		for(OptionAttribut option : options)
			if(!bean.options.contains(option))
				return false;
		
		return true;
	}
	
}
