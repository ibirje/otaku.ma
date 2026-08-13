package ma.otaku.acces.produit;
import java.util.HashSet;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import ma.otaku.acces.base.ICodeAcces;
import ma.otaku.data.produit.ProduitClient;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.utils.Constantes;

public class AccesProduitsClient extends AccesProduits<ProduitClient> implements ICodeAcces<ProduitDB> {

	public AccesProduitsClient(){
		
		super(ProduitClient.class);

		/*
		 * TODO add audit triggers
		 */
	}

	public AccesProduitsClient(byte initReadonly) {
		super(ProduitClient.class , initReadonly);
	}

	public List<ProduitClient> getListeProduits(String nom, String typeprix, String typepromo, String trifiltre, Double prixmin, Double prixmax, Integer qtemin,
			Integer qtemax, Integer minreduc, Integer maxreduc, Integer page, Integer size, String categorienom, String themenom, Boolean isActive )
	{
		return (List<ProduitClient>) listeProduits(nom, typeprix, typepromo, trifiltre, prixmin, prixmax, qtemin,
				 qtemax, minreduc, maxreduc, page, size, categorienom, themenom, isActive );
	}

	@Override
	protected void listeProduitsPredicates(boolean isActive , CriteriaBuilder builder, Root<ProduitClient> root, HashSet<Predicate> pr) {
		if(Constantes.MIN_QTE_CLIENT >= 1)
			pr.add(builder.greaterThan(root.get("qte"), root.get("lockedQte")));

		if(isActive) 
			pr.add(builder.equal(root.get("isActive"), true));
	}

	@Override
	protected void adminlisteProduitsPredicates(String nom, CriteriaBuilder builder, Root<ProduitClient> root,
			HashSet<Predicate> pr) {
		// TODO Auto-generated method stub
		
	}

	public List<ProduitClient> findTopSalesByCategorieId(long categorieID) {
		
		return select(4,
			"SELECT distinct prod from ProduitClient prod "+
			"JOIN ProduitStats pstat ON pstat.produitID = prod.produitID "+
			"WHERE prod.qte > 0 and prod.categorieID = :s0 and prod.isActive = true "
			+"ORDER BY pstat.commandePrixTotal desc ,prod.qte desc ", categorieID);
	}

}





