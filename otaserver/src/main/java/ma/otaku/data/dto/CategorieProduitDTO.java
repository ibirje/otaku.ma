package ma.otaku.data.dto;

import java.util.Collection;

import ma.otaku.data.produit.ProduitClient;
import ma.otaku.data.type.CategorieDB;

public class CategorieProduitDTO {

	private CategorieDB categorie;
	private Collection<ProduitClient> produits;
	
	
	
	
	public CategorieProduitDTO(CategorieDB categorie, Collection<ProduitClient> produits) {
		super();
		this.categorie = categorie;
		this.produits = produits;
	}



	public CategorieProduitDTO() {
	}
	
	

	public CategorieDB getCategorie() {
		return categorie;
	}
	public void setCategorie(CategorieDB categorie) {
		this.categorie = categorie;
	}
	public Collection<ProduitClient> getProduits() {
		return produits;
	}
	public void setProduits(Collection<ProduitClient> produits) {
		this.produits = produits;
	}
}
