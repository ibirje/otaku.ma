package ma.otaku.business.produits;

public class BeanInsertVariation {

	
	private String produitCode;
	private BeanVariationOptions variation;
	
	
	
	
	public BeanInsertVariation() {}
	
	public BeanInsertVariation(String produitCode, BeanVariationOptions variation) {
		this.produitCode = produitCode;
		this.variation = variation;
	}
	public String getProduitCode() {
		return produitCode;
	}
	public void setProduitCode(String produitCode) {
		this.produitCode = produitCode;
	}
	public BeanVariationOptions getVariation() {
		return variation;
	}
	public void setVariation(BeanVariationOptions variation) {
		this.variation = variation;
	}
	
	
	
}
