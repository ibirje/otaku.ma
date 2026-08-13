package ma.otaku.acces.stock;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.acces.base.ICodeAcces;
import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.data.stock.FournisseurDB;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.Utils;

public class AccesFournisseurs extends AccesTable<FournisseurDB>  implements ICodeAcces<FournisseurDB> 
{

	public AccesFournisseurs() 
	{
		super(FournisseurDB.class);
	}

	
	@SuppressWarnings("unchecked")
	public List<FournisseurDB> getFournisseursDB(String titre, String tel, Integer moq, Integer maxoq) 
	{
		List<FournisseurDB> fourniDB = null;
		EntityManager manager = null;
		
		try 
		{
			manager = getFactory().createEntityManager();

			CriteriaBuilder builder = manager.getCriteriaBuilder();
			CriteriaQuery<FournisseurDB> criteria = builder.createQuery(FournisseurDB.class);
			Root<FournisseurDB> root = criteria.from(FournisseurDB.class);
			
			criteria.select(root);
			
			HashSet<Predicate> pr = new HashSet<>();
			textPredicate("titre", titre,builder, root, pr);
			stringPredicate("telephone", tel, builder, root, pr);
			supOuEgalPredicate("MOQ", moq, (int)Constantes.MIN_QTE_ADMIN, builder, root, pr);
			infOuEgalPredicate("maxOQ", maxoq, (int)Constantes.MIN_QTE_ADMIN, builder, root, pr);

			criteria.where(builder.and(pr.toArray(new Predicate[pr.size()])) );
			
			Query query = manager.createQuery(criteria)
				.setParameter("titre", "+" + titre.trim().replaceAll(" ", " +"))
				.setMaxResults(Constantes.MAX_FOURNISSEURS_POPUP);
			fourniDB = (List<FournisseurDB>) query.getResultList();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fourniDB = null;
		}
		finally { 
			if(manager!= null && manager.isOpen()) manager.close(); }
		return fourniDB;
	}
	
	@Override
	public void verifierDonnees(FournisseurDB t) throws Exception{

		if( t.getTitre() == null || t.getTitre().isEmpty()) 
			throw new Exception("Titre vide");
		if((t.getSite() == null || t.getService().isEmpty()) && (t.getSource() == null || "AX".equals(t.getSource())) )
			throw new Exception("Lien de site invalide");
		
		AccesCategorie accescateg = new AccesCategorie();
		
		if(t.getCategorie1Code() == null || t.getCategorie1Code().isEmpty())
			throw new Exception("Categorie principale vide");

		if(!accescateg.isCodeValide(t.getCategorie1Code()))
			throw new Exception("Code categorie principale invalide");

		if(t.getCategorie2Code() != null && !t.getCategorie2Code().isEmpty() && !accescateg.isCodeValide(t.getCategorie2Code()))
			throw new Exception("Code categorie 2 invalide");
		if(t.getCategorie3Code() != null && !t.getCategorie3Code().isEmpty() && !accescateg.isCodeValide(t.getCategorie3Code()))
			throw new Exception("Code categorie 3 invalide");
		
		t.setCategorie1(accescateg.getByCode(t.getCategorie1Code()).getCategorieID());	

		if(accescateg.isCodeValide(t.getCategorie2Code()))
			t.setCategorie2(accescateg.getByCode(t.getCategorie2Code()).getCategorieID());	
		
		if(accescateg.isCodeValide(t.getCategorie3Code()))
			t.setCategorie3(accescateg.getByCode(t.getCategorie3Code()).getCategorieID());	
		
		accescateg.getByCode(t.getCategorie1Code());
		
		t.setCode(genereCode(t));
	}
	
	@Override
	protected Query isInDBQuery(FournisseurDB t, EntityManager manager) throws Exception {
		return manager.createQuery
			(
				"SELECT F FROM FournisseurDB F "
				+ "WHERE F.categorie1ID = :cid "
				+ "AND (F.titre = :titre OR F.nom = :nom) "
				+ "AND (F.telephone = :telephone OR F.site = :site) "
			).setParameter("cid", t.getCategorie1())
			.setParameter("titre", t.getTitre())
			.setParameter("nom", t.getNom())
			.setParameter("telephone", t.getTelephone())
			.setParameter("site", t.getSite());
		
		/* ************* TODO transform to criteria car titre, nom,telephone,site nullable ************** */
	}


	@Override
	protected void erreurExistant(FournisseurDB t) throws Exception {
		throw new Exception("Fournisseur avec les memes données déjà existant");
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		throw new Exception("Le Fournisseur "+t+" est introuvable.");
		
	}

	@Override
	public String genereCode(FournisseurDB t) 
	{
		String c1code = t.getCategorie1Code(), 
		suffix = ( "MA".equals(t.getSource()) ? "MA_" : "AX_" ) + c1code.substring(c1code.length()-2, c1code.length());
		return new Utils().nextCode(suffix, getLike("code", suffix+"%"));
	}

	@Override
	public FournisseurDB getByCode(String code) 
	{
		return getEquals("code", code );
	}

	@Override
	public boolean isCodeValide(String code) 
	{
		return code.matches("\\w\\w_\\w\\w_\\d+");
	}
	
	@Override
	protected void deleteErrors(FournisseurDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

}






















