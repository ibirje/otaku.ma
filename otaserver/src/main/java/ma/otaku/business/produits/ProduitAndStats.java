package ma.otaku.business.produits;

import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.stats.ProduitStats;

public class ProduitAndStats {

	private ProduitAdmin produit;
	private ProduitStats stats;


	 
	public ProduitAndStats(ProduitStats stats2, ProduitAdmin pradmin) {
		stats = stats2;
		produit = pradmin;
	}
	public ProduitAdmin getProduit() {
		return produit;
	}
	public void setProduit(ProduitAdmin produit) {
		this.produit = produit;
	}
	public ProduitStats getStats() {
		return stats;
	}
	public void setStats(ProduitStats stats) {
		this.stats = stats;
	}
}
