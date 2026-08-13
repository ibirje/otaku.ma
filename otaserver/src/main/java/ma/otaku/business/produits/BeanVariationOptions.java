package ma.otaku.business.produits;

import java.util.List;

import ma.otaku.data.produit.VariationDB;

public class BeanVariationOptions {

	private VariationDB variation;
	private List<String> options;
	
	
	public BeanVariationOptions(VariationDB variation, List<String> options) {
		super();
		this.variation = variation;
		this.options = options;
	}

	public BeanVariationOptions() {}

	public VariationDB getVariation() {
		return variation;
	}
	public void setVariation(VariationDB variation) {
		this.variation = variation;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	
	
}
