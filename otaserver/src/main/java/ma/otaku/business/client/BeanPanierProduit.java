package ma.otaku.business.client;

import ma.otaku.data.client.PanierDB;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.produit.VariationDB;

public class BeanPanierProduit {

	private ProduitDB   produit;
	private PanierDB    panier;
	private VariationDB variation;
	
	public ProduitDB getProduit() {
		return produit;
	}
	public void setProduit(ProduitDB produit) {
		this.produit = produit;
	}
	public PanierDB getPanier() {
		return panier;
	}
	public void setPanier(PanierDB panier) {
		this.panier = panier;
	}
	public VariationDB getVariation() {
		return variation;
	}
	public void setVariation(VariationDB variation) {
		this.variation = variation;
	}
}
