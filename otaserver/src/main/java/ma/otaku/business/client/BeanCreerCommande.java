package ma.otaku.business.client;

import java.util.List;

import ma.otaku.data.client.PanierDB;

public class BeanCreerCommande {

	List<PanierDB> items;
	String paiement;
	String livraison;
	
	public List<PanierDB> getItems() {
		return items;
	}
	public void setItems(List<PanierDB> items) {
		this.items = items;
	}
	public String getPaiement() {
		return paiement;
	}
	public void setPaiement(String paiement) {
		this.paiement = paiement;
	}
	public String getLivraison() {
		return livraison;
	}
	public void setLivraison(String livraison) {
		this.livraison = livraison;
	}
	
	
}
