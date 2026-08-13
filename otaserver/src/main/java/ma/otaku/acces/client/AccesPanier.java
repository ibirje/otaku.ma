package ma.otaku.acces.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.acces.produit.AccesProduitsClient;
import ma.otaku.acces.produit.AccesVariations;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.PanierDB;
import ma.otaku.data.produit.ProduitClient;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.produit.VariationDB;

public class AccesPanier extends AccesTable<PanierDB>{

	public AccesPanier() {
		
		super(PanierDB.class);
	}

	@Override
	protected void verifierDonnees(PanierDB t) throws Exception {
		
		if(t.getClientID() == null || t.getProduitID() == null) throw new NullPointerException("Données panier invalides");
		if(t.getQte() <= 0 ) throw new Exception("Quantité <= 0");
	}

	public PanierDB delete(PanierDB obj) throws Exception {
		return super.delete("panierID", obj.getPanierID());
	}
	/**
	 * delete par produit code ou variation code
	 * @param code
	 * @param clientID
	 * @return PanierDB l'objet supprimé
	 * @throws Exception
	 */
	public PanierDB delete(String code, Long clientID) throws Exception {
		
		if(v().isNullOrEmpty(code)) return null;
		
		if ( code.startsWith("V_") ) 
		{
			VariationDB var = new AccesVariations(AccesVariations.READONLY).getEquals("code", code);
			if(var != null) 
			{
				List<PanierDB> list = dynamiqueSelect("clientID =", clientID, "variationID =", var.getVariationID(), "maxresult" , 1 );
				if( list != null && !list.isEmpty()) return delete(list.get(0)); 
			}
		} 
		else 
		{
			ProduitClient var = new AccesProduitsClient(AccesVariations.READONLY).getEquals("code", code);
			if(var != null) 
			{
				List<PanierDB> list = dynamiqueSelect("clientID =", clientID, "produitID =" , var.getProduitID(), "maxresult" , 1 );
				if( list != null && !list.isEmpty()) return delete(list.get(0));
			}
		}
		return null;
	}


	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(PanierDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/** PAS BESOIN DE VERIFIER LA REDONDANCE LORS DE L'INSERTION A CAUSE DE LA SIMILARITE*/
	@Deprecated @Override 
	public boolean isInDB(PanierDB t) {return false;}
	@Deprecated @Override 
	protected Query isInDBQuery(PanierDB t, EntityManager manager) throws Exception { return null; }
	@Deprecated @Override
	protected void erreurExistant(PanierDB t) throws Exception {  }

	public void ajoutPanier(ClientDB client, PanierDB panier) throws Exception {

		if( client == null ) 
			throw new NullPointerException("Client introuvable");
		
		panier.setClientID(client.getClientID());
		if ( panier.getCode().startsWith("V_") ) {
			AccesVariations accesvar = new AccesVariations(AccesVariations.READONLY);
			VariationDB var = accesvar.getEquals("code", panier.getCode());
			if(var != null) {
				List<PanierDB> list = dynamiqueSelect( "clientID =", client.getClientID(), "variationID =" , var.getVariationID() , 
					"maxresult" , 1 );
				if ( list != null && !list.isEmpty() )
				{
					PanierDB p = list.get(0);
					p.setQte(panier.getQte() + p.getQte());
					update(p);
					return;
				}
				else {
					panier.setVariationID(var.getVariationID());
					panier.setProduitID(var.getProduitID());
				}
			} 
			else 
				throw new NotFoundException("Variation introuvable");
		} else {
			AccesProduitsClient accesprod = new AccesProduitsClient();
			ProduitDB prod = accesprod.getEquals("code", panier.getCode());
			if(prod != null) {
				List<PanierDB> list = dynamiqueSelect(
					"clientID =", client.getClientID(), 
					"produitID =" , prod.getProduitID() , 
					"maxresult" , 1 );
				if ( list != null && !list.isEmpty() )
				{
					PanierDB p = list.get(0);
					p.setQte(panier.getQte() + p.getQte());
					update(p);
					return;
				}
				panier.setProduitID(prod.getProduitID());
			} 
			else 
				throw new NotFoundException("Produit introuvable");
		}
		insert(panier);
		
	}
	
	

}
