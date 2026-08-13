package ma.otaku.business.client;

import java.util.List;

import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.CommandeDB;
import ma.otaku.data.client.CommandeItemDB;

public class BeanDetailsCommande {
	
	CommandeDB commande;
	List<CommandeItemDB> items;
	ClientDB client;
	
	
	public ClientDB getClient() {
		return client;
	}
	public void setClient(ClientDB client) {
		this.client = client;
	}
	public CommandeDB getCommande() {
		return commande;
	}
	public void setCommande(CommandeDB commande) {
		this.commande = commande;
	}
	public List<CommandeItemDB> getItems() {
		return items;
	}
	public void setItems(List<CommandeItemDB> items) {
		this.items = items;
	}
}
