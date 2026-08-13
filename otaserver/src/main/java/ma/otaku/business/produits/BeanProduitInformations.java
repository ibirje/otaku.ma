package ma.otaku.business.produits;

import java.util.List;

import ma.otaku.data.produit.ProduitClient;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.type.ThemeDB;

public class BeanProduitInformations {

	private ProduitClient produit;
	private ThemeDB theme;
	private List<BeanVariationOptions> variations;
	private List<BeanAttributOptionsDB> attributs;
	private List<Categorie> categories;
	private List<ProduitClient> suggestions;
	
	public List<Categorie> getCategories() {
		return categories;
	}
	public void setCategories(List<Categorie> categories) {
		this.categories = categories;
	}
	
	public ProduitDB getProduit() {
		return produit;
	}
	public void setProduit(ProduitClient produit) {
		this.produit = produit;
	}
	
	public List<BeanVariationOptions> getVariations() {
		return variations;
	}
	public void setVariations(List<BeanVariationOptions> variations) {
		this.variations = variations;
	}
	
	public List<BeanAttributOptionsDB> getAttributs() {
		return attributs;
	}
	public void setAttributs(List<BeanAttributOptionsDB> attributs) {
		this.attributs = attributs;
	}
	public ThemeDB getTheme() {
		return theme;
	}
	public void setTheme(ThemeDB theme) {
		this.theme = theme;
	}
	public List<ProduitClient> getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(List<ProduitClient> suggestions) {
		this.suggestions = suggestions;
	}
}
