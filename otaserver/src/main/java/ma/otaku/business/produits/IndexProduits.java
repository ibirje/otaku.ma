package ma.otaku.business.produits;

import java.util.List;

import ma.otaku.data.produit.ProduitClient;

public class IndexProduits {
	
	private List<ProduitClient> promos;
	private List<ProduitClient> news;
	
	public List<ProduitClient> getPromos() {
		return promos;
	}
	public void setPromos(List<ProduitClient> promos) {
		this.promos = promos;
	}
	public List<ProduitClient> getNews() {
		return news;
	}
	public void setNews(List<ProduitClient> news) {
		this.news = news;
	}
	
}
