package ma.otaku.business.produits;

import java.util.List;

import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.produit.ProduitDB;

public class BeanProduitAttributs {

	private ProduitAdmin produit;
	private List<BeanAttributOptions> attributs;
	
	
	
	public BeanProduitAttributs(ProduitAdmin produit, List<BeanAttributOptions> attributs) {
		super();
		this.produit = produit;
		this.attributs = attributs;
	}

	public BeanProduitAttributs() {}

	public ProduitDB getProduit() {
		return produit;
	}
	public void setProduit(ProduitAdmin produit) {
		this.produit = produit;
	}

	public List<BeanAttributOptions> getAttributs() {
		return attributs;
	}

	public void setAttributs(List<BeanAttributOptions> attribut) {
		this.attributs = attribut;
	}
	
	
}
