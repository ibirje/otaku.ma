package ma.otaku.acces.produit;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;

import ma.otaku.acces.base.ICodeAcces;
import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.produit.ProduitDB;

public class AccesProduitsAdmin extends AccesProduits<ProduitAdmin> implements ICodeAcces<ProduitDB> {

	public AccesProduitsAdmin(){
		
		super(ProduitAdmin.class);
		/*
		 * TODO add audit triggers
		 */
	}

	public AccesProduitsAdmin(byte param) {
		super(ProduitAdmin.class, param);
	}
	

	public List<ProduitAdmin> getListeProduits(String nom, String typeprix, String typepromo, String trifiltre, Double prixmin, Double prixmax, Integer qtemin,
			Integer qtemax, Integer minreduc, Integer maxreduc, Integer page, Integer size, String categorienom, String themenom, Boolean isActive ) 
	{
		return (List<ProduitAdmin>) listeProduits(nom, typeprix, typepromo, trifiltre, prixmin, prixmax, qtemin,
		 qtemax, minreduc, maxreduc, page, size, categorienom, themenom, isActive );
	}

	@Override
	protected void listeProduitsPredicates(boolean isActive, CriteriaBuilder builder, Root<ProduitAdmin> root, HashSet<Predicate> pr) {
		
	}

	@Override
	protected Query isInDBQuery(ProduitAdmin t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(ProduitAdmin t) throws Exception {
		super.verifierDonnees(t);
	}

	@Override
	protected void adminlisteProduitsPredicates(String nom, CriteriaBuilder builder, Root<ProduitAdmin> root,
			HashSet<Predicate> pr) {
		stringPredicate("nom", nom, builder, root, pr);
	}
	
	public ProduitAdmin setPromo(String code, String datedebut, String datefin) throws Exception
	{
		if(v().isNullOrEmpty(datefin))
			throw new Exception("date fin promo vide");
		
		ProduitAdmin prod = getByCode(code);
		
		if(prod == null)
			throw new NotFoundException("produit inéxistant");
		
		prod.setDateFinPromo(parse().ParseDate(datefin));
		
		if(prod.getDateFinPromo() == null)
			throw new ValidationException("Date fin promo invalide");
		
		
		if(!v().isNullOrEmpty(datedebut))
			prod.setDateDebutPromo(parse().ParseDate(datedebut));
		
		return update(prod);
		
	}
}





