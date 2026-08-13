package ma.otaku.acces.stock;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import ma.otaku.acces.admin.AccesAdmin;
import ma.otaku.acces.base.AccesTable;
import ma.otaku.acces.base.ICodeAcces;
import ma.otaku.data.admin.AdminDB;
import ma.otaku.data.stock.AchatStockDB;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.Utils;

public class AccesAchatStock extends AccesTable<AchatStockDB> implements ICodeAcces<AchatStockDB> {
	
	public AccesAchatStock() 
	{
		super(AchatStockDB.class);
	}

	public AccesAchatStock(byte param) {

		super(AchatStockDB.class, param);
	}
	
	@SuppressWarnings("unchecked")
	public List<AchatStockDB> getListe(String code, String description, Date debut, Date fin, Boolean associe, Long adminid) 
	{
		List<AchatStockDB> achatdb = null;
		EntityManager manager = null;
		
		try
		{
			manager = getFactory().createEntityManager();

			CriteriaBuilder builder = manager.getCriteriaBuilder();
			CriteriaQuery<AchatStockDB> criteria = builder.createQuery(AchatStockDB.class);
			Root<AchatStockDB> root = criteria.from(AchatStockDB.class);
			
			criteria.select(root);

			HashSet<Predicate> prlist = new HashSet<>();

			stringPredicate("code", code ,builder, root, prlist);
			stringPredicate("description", description ,builder, root, prlist);
			dateSupOuEgalPredicate("dateInsertion", debut, null, builder, root, prlist);
			dateInfOuEgalPredicate("dateInsertion", fin, null, builder, root, prlist);
			egalPredicate("associe", associe, builder, root, prlist);
			egalPredicate("authAdminID", adminid, builder, root, prlist);
			
			criteria.where(builder.and(prlist.toArray(new Predicate[prlist.size()])) );
			Query query = manager.createQuery(criteria).setMaxResults(Constantes.MAX_LIST_COUNT);
			achatdb = (List<AchatStockDB>) query.getResultList();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			achatdb = null;
		}
		finally 
		{
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return achatdb;
	}
	
	@Override
	protected Query isInDBQuery(AchatStockDB t, EntityManager manager) throws Exception
	{
		String req = "SELECT P FROM AchatStockDB P WHERE "
			+ "fournisseurID = :fournisseurid AND "
			+ "type = :type AND "
			+ "prixTotal = :prix AND "
			+ "fraisSupplementaires = :frais AND"
			+ " qte = :qte AND "
			+ "dateCommande = :datecmd AND "
			+ "dateLivraison = :dateliv";
		
		return manager.createQuery(req)
				.setParameter("fournisseurid", t.getFournisseurID())
				.setParameter("type", t.getType())
				.setParameter("prix", t.getPrixTotal())
				.setParameter("frais", t.getFraisSupplementaires())
				.setParameter("qte", t.getQte())
				.setParameter("datecmd", t.getDateCommande())
				.setParameter("dateliv", t.getDateLivraison())
				.setMaxResults(1);
	}

	@Override
	protected void verifierDonnees(AchatStockDB t) throws Exception {

		if( v().isNullOrEmpty(t.getCodeFournisseur()) ) //TODO fournisseur isInDB
			throw new Exception("Code fournisseur invalide.");
		
		if( v().isNotSupZero(t.getPrixTotal())) throw new Exception("Prix Achat Stock invalide.");
		
		if(t.getFraisSupplementaires()!= null && t.getFraisSupplementaires()< 0 ) 
			throw new Exception("Frais supplémentaires Achat Stock invalides. \n(0 minimum ou laissez le champ vide) ");
		/*
		if(t.getDateLivraison() != null || new Utils().calculCountdown(t.getDateLivraison()) > 0) 
			throw new Exception("Date Livraison invalide ou superieure date ajourd'hui.");
		
		if(t.getDateCommande() == null || new Utils().calculCountdown(t.getDateCommande()) > 0) 
			throw new Exception("Date Commande invalide ou superieure date ajourd'hui.");
		*/
		
		if( v().isNotSupZero(t.getQte()) ) throw new Exception("Quantité de l'achat inférieure à 1");
		
		Long fourniid = new AccesFournisseurs().getByCode(t.getCodeFournisseur()).getFournisseurID();
		if(fourniid == null )throw new Exception("Code fournisseur introuvable.");
		
		t.setFournisseurID(fourniid);
		String code = genereCode(t);
		if(!isCodeValide(code)) throw new Exception("Le code d'achat stock generé n'est pas valide");
		t.setCode(code);
		if(t.getType() == null) t.setType("International");
	}
	
	@Override
	protected void erreurExistant(AchatStockDB t) throws Exception 
	{
		throw new Exception("L'achatStock "+t.getCode()+" existe déjà avec des informations identiques.");
	}

	@Override
	protected void erreurInexistant(String t)  throws Exception 
	{
		throw new Exception("L'achatStock "+t+" est inexistant.");
	}

	@Override
	public String genereCode(AchatStockDB t) 
	{
		Calendar cal = Calendar.getInstance();

		String month = (cal.get(Calendar.MONTH)+1) <10 ? "0"+(cal.get(Calendar.MONTH)+1) : ""+(cal.get(Calendar.MONTH)+1);
		String year  = (cal.get(Calendar.YEAR)+"").substring(2);
		String suffix = month+""+year+"_"+t.getCodeFournisseur();//TODO genere code fourni
		return new Utils().nextCode(suffix, getLike("code", suffix+"%"));
	}

	@Override
	public AchatStockDB getByCode(String code) {
		return getEquals("code", code);
	}

	@Override
	public boolean isCodeValide(String code) {
		return code.matches("\\d{4}_(\\w\\w_){3}\\d+");
	}

	public AchatStockDB delete(AchatStockDB t) throws Exception {
		
		return super.delete("code", t.getCode());
	}
	protected void deleteErrors(AchatStockDB t) throws Exception 
	{
		if( t.getAssocie() ) throw new Exception(t.getCode()+" Cet achat stock est deja associé à un stock \nIl ne peut pas être supprimé.");
	}

	public List<AchatStockDB> getFullListe(String code, String description, Date debut, Date fin, Boolean associe, Long adminid) {
		
		List<AchatStockDB> ls = getListe(code, description, debut, fin, associe, adminid);

		AccesAdmin accesadmin = new AccesAdmin();

		for(AchatStockDB s : ls)
		{
			AdminDB admin = accesadmin.getByID(s.getAuthAdminID());
			s.setAuthAdminNom(admin.getNom()+" "+admin.getPrenom());
		}
		return ls;
	}
	

	
}






