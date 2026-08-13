package ma.otaku.acces.produit;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.acces.base.ICodeAcces;
import ma.otaku.acces.triggers.produit.AfterDeleteProduit;
import ma.otaku.acces.triggers.produit.AfterInsertProduit;
import ma.otaku.acces.triggers.produit.AfterUpdateProduit;
import ma.otaku.business.produits.Produit;
import ma.otaku.business.produits.ProduitSuggestion;
import ma.otaku.data.produit.AttributDB;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.produit.VariationDB;
import ma.otaku.data.type.CategorieDB;
import ma.otaku.data.type.ThemeDB;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.Utils;

public abstract class AccesProduits<T extends ProduitDB> extends AccesTable<T> implements ICodeAcces<ProduitDB> {

	public AccesProduits(Class<T> table){
		
		super(table);
		
		deleteTriggers.add(new AfterDeleteProduit());
		updateTriggers.add(new AfterUpdateProduit(this));
		insertTriggers.add(new AfterInsertProduit());

		/*
		 * TODO add audit triggers
		 */
	}

	public AccesProduits(Class<T> table , byte param) {

		super(table, param);
	}
	
	public T getProduitByNom(String produitnom) {
		
		return getEquals("nom", produitnom);
	}

	@SuppressWarnings("unchecked")
	public List<Produit> getRelatedProdList(T produitdb) {
		String prodcode = produitdb.getCode();
		Long themeid = produitdb.getThemeID();

		List<Produit> liste   = null;
		EntityManager manager = null;

		try {
			manager = getFactory().createEntityManager();

			String prodtypecode = prodcode.substring(0, Constantes.FIN_ID_TYPE);
			String categcode = prodcode.substring(Constantes.DEBUT_ID_CATEGORIE, Constantes.FIN_ID_CATEGORIE);
			String categcodep1 = prodcode.substring(Constantes.DEBUT_ID_CATEGORIE, Constantes.FIN_ID_CATEGORIE - 3);
			String categcodep2 = prodcode.substring(Constantes.DEBUT_ID_CATEGORIE, Constantes.FIN_ID_CATEGORIE - 6);

			
			String requete = "SELECT * FROM produit WHERE code LIKE '" + prodtypecode + "%' AND code != '" + prodcode+ "' " 
			+ "UNION SELECT * FROM produit WHERE code LIKE '" + categcode + "%' AND code != '" + prodcode + "' " 
			+ "UNION SELECT * FROM produit WHERE code LIKE '" + categcodep1 + "%' AND code != '" + prodcode + "' " 
			+ "UNION SELECT * FROM produit WHERE code LIKE '" + categcodep2 + "%' AND code != '" + prodcode + "' " 
			+ "UNION SELECT * FROM produit WHERE themeID = " + themeid + " AND code != '" + prodcode + "' " 
			+ "UNION SELECT * FROM produit WHERE code != '" + prodcode
					+ "' " + // TODO
					"limit "+Constantes.MAX_RELATED_PRODUCTS;

			Query query = manager.createNativeQuery(requete, TABLE);

			List<T> listedb = ((List<T>) query.getResultList());

			if (listedb!= null && listedb.size() > 0)
			{	
				liste = new ArrayList<Produit>();
			
				ConcurrentHashMap<Long, CategorieDB> categories = 
					(ConcurrentHashMap<Long, CategorieDB>) getContextAttribute("categories");
				ConcurrentHashMap<Long, ThemeDB> themes = 
					(ConcurrentHashMap<Long, ThemeDB>) getContextAttribute("themes");
				
				for (ProduitDB p : listedb) {
					Produit prod = new Produit(p);
					
					CategorieDB cat = categories.get(p.getCategorieID());
					ThemeDB theme = themes.get(p.getThemeID());
					
					prod.setCategorie(cat.getNom());
					prod.setTheme(theme.getNom());
					
					prod.setStars(0); // TODO
					prod.setAvis(0); // TODO
					prod.setCommandes(0); // TODO
					liste.add(prod);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			liste = null;
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return liste;
	}

	public Long getProduitID(String nom)
	{
		T p = getProduitByNom(nom);
		return p == null ?  null : p.getProduitID();
	}
	
	public Integer getProduitNote(T produitDB) {
		return 0;
	}

	public Integer getNombreAvis(T produitDB) {
		return 0;
	}

	public Integer getNombreCommandes(T produitDB) {
		return 0;
	}

	public Long getNombreProduitsByCategorie(String code, Double min, Double max) {
		return getNombreProduits(code,"Categorie", min, max);
	}
	public Long getNombreProduitsByTheme(String code, Double min, Double max) {
		return getNombreProduits(code,"Theme", min, max);
	}
/*
 * TODO REMOVE HAD LFUNCTION
 */
	private Long getNombreProduits(String code, String type, Double min, Double max) {
		
		Long nombre = 0L;
		EntityManager manager = null;
		
		try {
			manager = getFactory().createEntityManager();
			
			String typeid=type.toLowerCase()+"ID";
			Query query = manager.createQuery("SELECT count(P.produitID) "
					+ "FROM "+ TABLE.getSimpleName() + " P "
					+ "JOIN "+type+"DB T on P."+typeid+" = T."+typeid+
						" WHERE T.code LIKE :code AND P.prixPromo >= :prixmin AND P.prixPromo <= :prixmax")
					.setParameter("prixmax", max)
					.setParameter("prixmin", min)
					.setParameter("code", code + "%");
		
			nombre = (Long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return nombre;
		
	}

	public List<Produit> getListeProduitsByCategorie(String code, Double prixmin, Double prixmax, String filtre,
			int page, int size) {

		if (filtre == null)
			return ListeParPertinence("Categorie", code, prixmin, prixmax, page, size);
		else if (filtre.equals("commandes"))
			return ListeParCommandes("Categorie", code, prixmin, prixmax, page, size);
		else if (filtre.equals("prix"))
			return ListeParPrix("Categorie", code, prixmin, prixmax, page, size, "ASC");
		else if (filtre.equals("prixDesc")) {
			return ListeParPrix("Categorie", code, prixmin, prixmax, page, size, "DESC");
		} else
			return ListeParPertinence("categorie", code, prixmin, prixmax, page, size);
	}

	protected List<Produit> ListeParCommandes(String type, String code, Double prixmin, Double prixmax, int page,
			int size) {
		// TODO
		return ListeParPrix(type, code, prixmin, prixmax, page, size, "ASC"); // TODO
		// TODO
	}
	protected List<Produit> ListeParPertinence(String type, String code, Double prixmin, Double prixmax, int page,
			int size) {
		// TODO
		return ListeParPrix(type, code, prixmin, prixmax, page, size, "ASC"); // TODO
		// TODO
	}

	@SuppressWarnings("unchecked")
	protected List<Produit> ListeParPrix(String type, String code, Double prixmin, Double prixmax, int page, int size,
			String prixOrdre) {
		List<Produit> liste;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();
			
			String req = "SELECT P from "+TABLE.getSimpleName()+" P JOIN " + type + "DB t on t."+type.toLowerCase()+"ID = P."+type.toLowerCase()+"ID  WHERE t.code LIKE :code"+
					" AND P.prixPromo >= :prixmin AND P.prixPromo <= :prixmax ORDER BY P.prixPromo "+ prixOrdre;

			Query query = manager.createQuery(req)
					.setParameter("prixmax", prixmax)
					.setParameter("prixmin", prixmin)
					.setParameter("code", code + "%")
					.setFirstResult((page - 1) * size)
					.setMaxResults(size);

			List<ProduitDB> listedb = ((List<ProduitDB>) query.getResultList());
			liste = new ArrayList<>();
			for (ProduitDB p : listedb) {
				Produit produit = new Produit(p);
				produit.setStars(0);
				produit.setAvis(0);
				produit.setCommandes(0);

				liste.add(produit);
			}
		} catch (Exception e) {
			e.printStackTrace();
			liste = null;
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return liste;
	}

	public List<Produit> getListeProduitsByTheme(String code, Double prixMin, Double prixMax, String filtre,
			Integer page, int size) {
		

		if (filtre == null)
			return ListeParPertinence("Theme", code, prixMin, prixMax, page, size);
		else if (filtre.equals("commandes"))
			return ListeParCommandes("Theme", code, prixMin, prixMax, page, size);
		else if (filtre.equals("prix"))
			return ListeParPrix("Theme", code, prixMin, prixMax, page, size, "ASC");
		else if (filtre.equals("prixDesc")) {
			return ListeParPrix("Theme", code, prixMin, prixMax, page, size, "DESC");
		} else
			return ListeParPertinence("Theme", code, prixMin, prixMax, page, size);
	}

	

	@SuppressWarnings("unchecked")
	public List<String> getListeTitres(String nom, int size) {
		List<String> liste;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();
			
			String req = "SELECT nom FROM categorie WHERE nom like '%"+nom+"%' UNION"+
					" SELECT nom FROM theme WHERE nom like '%"+nom+"%' UNION"+
					" SELECT nom from produit WHERE nom like '%"+nom+"%' LIMIT "+size;
			Query query = manager.createNativeQuery(req);

			liste = ((List<String>) query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
			liste = null;
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return liste;
	}

	

	public String getNomCategByProduitNom(String nom) 
	{
		String cnom = null;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();
			
			String req = "SELECT C.nom FROM CategorieDB C "
					+ " JOIN "+ TABLE.getSimpleName() + " P ON C.categorieID = P.categorieID "
					+ " WHERE P.nom = :nom";
			Query query = manager.createQuery(req)
					.setParameter("nom", nom);

			cnom = (String) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(nom+" CATEGORIE NOT FOUND");
			cnom = null;
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return cnom;
	}
	
	public String getNomCategByProduitCode(String code) 
	{
		String cnom = null;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();
			
			String req = "SELECT C.nom FROM CategorieDB C "
					+ " JOIN "+ TABLE.getSimpleName() + " P ON C.categorieID = P.categorieID "
					+ " WHERE P.code = :code";
			Query query = manager.createQuery(req)
					.setParameter("code", code);

			cnom = (String) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(code+" CATEGORIE NOT FOUND ");
			cnom = null;
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return cnom;
	}
	
	public String getNomThemeByProduitNom(String nom) 
	{
		String cnom;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();
			
			String req = "SELECT C.nom FROM ThemeDB C "
					+ " JOIN "+ TABLE.getSimpleName() + " P ON C.themeID = P.themeID "
					+ " WHERE P.nom = :nom";
			Query query = manager.createQuery(req)
					.setParameter("nom", nom);

			cnom = (String) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			cnom = null;
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return cnom;
	}
	public String getNomThemeByProduitCode(String code) 
	{
		String cnom;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();
			
			String req = "SELECT C.nom FROM ThemeDB C "
					+ " JOIN "+ TABLE.getSimpleName() + " P ON C.themeID = P.themeID "
					+ " WHERE P.code = :code";
			Query query = manager.createQuery(req)
					.setParameter("code", code);

			cnom = (String) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			cnom = null;
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return cnom;
	}

	public boolean isProduitHasVariations(T prod) throws Exception
	{
		if(prod == null) throw new Exception("Produit inexistant");
		return prod.getHasVariations();
	}
	
/*************************************** UPDATE UTILISEE PAR ADMIN *********************************************/
	
	@SuppressWarnings("unchecked")
	public Produit updateProduit(T produit) 
	{
		try 
		{
			final T oldproduit =  getByCode(produit.getCode());
			
			if(oldproduit == null ) throw new NotFoundException(produit.getCode() + " produit de base introuvable");

			ConcurrentHashMap<Long, CategorieDB> categories = 
					(ConcurrentHashMap<Long, CategorieDB>) getContextAttribute("categories");
			ConcurrentHashMap<Long, ThemeDB> themes = 
					(ConcurrentHashMap<Long, ThemeDB>) getContextAttribute("themes");

			oldproduit.setExtra1(null);
			
			if(produit.getCategorie() != null)
			{
				String old_categorie = categories.get(oldproduit.getCategorieID()).getCode();
				if(!produit.getCategorie().equals(old_categorie))
					
					categories.forEach((id,c) -> 
					{
						if(c.getCode().equals(produit.getCategorie())) 
						{
							produit.setCategorieID(oldproduit.getCategorieID());
							oldproduit.setCategorieID(id); 
							oldproduit.setExtra1("true");
							return;
						}
					});
				oldproduit.setCategorie(produit.getCategorie());
			}
			
			boolean cat_changed = !v().isNullOrEmpty(oldproduit.getExtra1());
			oldproduit.setExtra1(null);
			
			if(produit.getTheme() != null)
			{
				String old_theme = themes.get(oldproduit.getThemeID()).getCode();
				if(!produit.getTheme().equals(old_theme))
					themes.forEach((id,c) -> { 
						if(c.getCode().equals(produit.getTheme())) 
						{
							produit.setThemeID(oldproduit.getThemeID());
							oldproduit.setThemeID(id); 
							oldproduit.setExtra1("true");
							return;
						}
					});
				oldproduit.setTheme(produit.getTheme());
			}

			boolean th_changed = !v().isNullOrEmpty(oldproduit.getExtra1());
			oldproduit.setExtra1(null);

			if( ( th_changed || cat_changed ) && !oldproduit.getHasVariations()
			&& oldproduit.getCommandes() == 0 && oldproduit.getQte() == 0 )
				oldproduit.setCode(genereCode(oldproduit));

			
			if( v().isTitreValide(produit.getNom()) ) 
				oldproduit.setNom(produit.getNom());
			
			if( !v().isNullOrEmpty(produit.getImage1()) ) 
			{
				oldproduit.setImage1(produit.getImage1());
				oldproduit.setThumbnail(parse().imageToThumbnail(produit.getImage1()));
			}
			
			if( !v().isNotSupZero(produit.getPrixUnite()) )
				oldproduit.setPrixUnite(produit.getPrixUnite());
				
			if( !v().isNotSupZero(produit.getPrixPromo()) ) 
				oldproduit.setPrixPromo(produit.getPrixPromo());
			
			oldproduit.setShortDescription(produit.getShortDescription());
			oldproduit.setDescription(produit.getDescription());
			
			oldproduit.setImage2(produit.getImage2());
			oldproduit.setImage3(produit.getImage3());
			
			oldproduit.setDateDebutPromo(parse().ParseDate(produit.getStringDateDebutPromo())); 
			oldproduit.setDateFinPromo(parse().ParseDate(produit.getStringDateFinPromo()));
			
			T newp = super.update(oldproduit);
			/*
			if( th_changed )
			{
				resumeTheme(produit);
				resumeTheme(newp);
			}
			if( cat_changed )
			{
				resumeTheme(produit);
				resumeCategorie(newp);
			}
			*/
			return new Produit(newp);
		}
		catch(Exception e ) 
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public T update(T t) throws Exception {
		// T oldt = getByID(t.getProduitID());
		T t2 = super.update(t);
		/*
		if( !oldt.getPendingQte().equals(t2.getPendingQte()) || !oldt.getQte().equals(t2.getQte())) 
		{
			resumeTheme(t2);
			resumeCategorie(t2);
		}
		*/
		return t2;
	}

/*
	private void resumeCategorie(T t) throws Exception 
	{
		List<T> p1 = getEqualsList("categorieID", t.getCategorieID());
		long countc = p1 == null ? 0L : p1.size();
		long qte = 0 , pendingQte = 0, activeQte = 0;
		
		for(ProduitDB p : p1)
		{
			pendingQte += p.getPendingQte();
			qte += p.getQte();
			if(p.getIsActive()) activeQte += p.getQte();
		}
		
		AccesCategorie accescat = new AccesCategorie();
		CategorieDB categorie = accescat.getByID(t.getCategorieID());

		if(categorie.getNombreProduits() != countc || categorie.getQte() != qte || 
				categorie.getPendingQte() != pendingQte || categorie.getActiveQte() != activeQte )
		{
			categorie.setQte(qte);
			categorie.setPendingQte(pendingQte);
			categorie.setNombreProduits(countc);
			categorie.setActiveQte(activeQte);
			
			accescat.update(categorie);
		}
	}
	
	

	private void resumeTheme(T t) throws Exception 
	{
		List<T> p2 = getEqualsList("themeID", t.getThemeID());
		
		long countth = p2 == null ? 0L : p2.size();
		long qte = 0 , pendingQte = 0, activeQte = 0;
		
		for(ProduitDB p : p2)
		{
			pendingQte += p.getPendingQte();
			qte += p.getQte();
			if(p.getIsActive()) activeQte += p.getQte();
		}
		
		AccesTheme accesth = new AccesTheme();
		ThemeDB theme = accesth.getByID(t.getThemeID());
		
		if(theme.getNombreProduits() != countth || theme.getQte() != qte || 
				theme.getPendingQte() != pendingQte || theme.getActiveQte() != activeQte)
		{
			theme.setQte(qte);
			theme.setPendingQte(pendingQte);
			theme.setNombreProduits(countth);
			theme.setActiveQte(activeQte);
			
			accesth.update(theme);
		}
	}
	
	*/
	
	@SuppressWarnings("unchecked")
	public Response deleteProduit(Produit produit) {

		EntityManager manager = null;
		Response p = null;
		try {
			
			manager = getFactory().createEntityManager();
			manager.getTransaction().begin();
			
			Query query = manager.createQuery("SELECT V FROM "+ TABLE.getSimpleName() + " V WHERE V.code = :code")
					.setMaxResults(1)
					.setParameter("code", produit.getCode());
			ProduitDB produitdb = (ProduitDB) query.getSingleResult();
			
			if(produitdb == null) throw new Exception("produit inexistant");
			if(produitdb.getQte() > Constantes.MIN_QTE ) throw new Exception("produit toujours en stock");
			
			Query nbvariationsquery = manager.createQuery("SELECT COUNT(V.variationID) FROM VariationDB V "
					+ "WHERE V.produitID = :prid")
					.setParameter("prid", produitdb.getProduitID());
			
			if((Long) nbvariationsquery.getSingleResult() > 0 )
				throw new Exception("produit contient toujours des variations.");
			
			Query attributsquery = manager.createQuery("SELECT AT FROM AttributDB AT "
					+ "JOIN "+ TABLE.getSimpleName() + " P ON P.produitID = AT.produitID "
					+ "WHERE P.produitID = :prid")
					.setParameter("prid", produitdb.getProduitID());
			
			List<AttributDB> attributs = attributsquery.getResultList();
			
			if(attributs != null)
				for(AttributDB attribut : attributs)
					manager.remove(attribut);
			
			manager.remove(produitdb);
			
			manager.getTransaction().commit();
			manager.close();
			
			p = Response.ok(new Produit(produitdb)).build();
		}
		
		catch(Exception ex)
		{
			if (manager != null && manager.getTransaction().isActive()) manager.getTransaction().rollback();
			System.err.println("---- "+ex.getMessage()+" ---------");
			p = Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}
		finally {
			if(manager != null && manager.isOpen()) manager.close();
		}
		return p;
	}


	@Override
	public T getByCode(String code) { 
		return getEquals("code", code); 
	}

	public T isActiveChange(String code, Boolean isactive) throws Exception
	{
		T res = null;
		T produit = getByCode(code);
		
		if( produit != null && isactive != null && produit.getIsActive() != isactive)
		{
			produit.setIsActive(isactive);
			res = update(produit);

			long diff = res.getIsActive() ? res.getQte() : -res.getQte();
			
			AccesTheme acth = new AccesTheme();
			ThemeDB   theme = acth.getByID(res.getThemeID());
			
			theme.setActiveQte( Math.max(theme.getActiveQte() + diff, 0) ) ;
			acth.update(theme);
				
			AccesCategorie accat = new AccesCategorie();
			CategorieDB   cat    = accat.getByID(res.getCategorieID());

			cat.setActiveQte( Math.max(cat.getActiveQte() + diff, 0) ) ;
			accat.update(cat);
			
		}
		return res;
	}
	
	@Override
	protected Query isInDBQuery(T t, EntityManager manager) throws Exception {
		
		String req = "SELECT P FROM "+ TABLE.getSimpleName() + " P WHERE nom = :nom OR"+
				" image1 = :img1 OR image2 = :img1 OR image3 = :img1 OR"+
				" image1 = :img2 OR image2 = :img2 OR image3 = :img2 OR"+
				" image1 = :img3 OR image2 = :img3 OR image3 = :img3";
			
			return manager.createQuery(req.toString())
					.setParameter("nom", t.getNom())
					.setParameter("img1", t.getImage1())
					.setParameter("img2", t.getImage2())
					.setParameter("img3", t.getImage3())
					.setMaxResults(1);
	}


	@Override
	public boolean isCodeValide(String code) { return code.matches("(\\w\\w_){3}\\w\\w\\d+"); }

	@Override
	public String genereCode(ProduitDB t) {

		String suffix = t.getCategorie() + "_" + t.getTheme().substring(3);
		return new Utils().no_NextCode(suffix, getLike("code", suffix+"%"));
	}
	
	@Override
	protected void verifierDonnees(T t) throws Exception {

		if( t == null ) throw new NullPointerException("Données invalides");
		if( v().isNullOrEmpty(t.getNom()) ) throw new NullPointerException("Nom vide ou invalide");
		if( v().isNullOrEmpty(t.getImage1()) ) throw new NullPointerException("Image principale invalide");
		if( v().isNotSupZero(t.getPrixUnite()) ) throw new ValidationException("Prix unite doit être > 0");
		if( t.getPrixPromo() != null && t.getPrixPromo() < 1 ) throw new ValidationException("Prix promo doit être > 0 ou vide");
		//if( t.getQTE() == null || t.getQTE() != 0) t.setQTE(Constantes.MIN_QTE); insertable = false
		if( v().isNullOrEmpty(t.getThumbnail()) ) { t.setThumbnail(parse().imageToThumbnail(t.getImage1())); }

		/********* parse strings to dates *********/
		t.setDateDebutPromo(parse().ParseDate(t.getStringDateDebutPromo())); 
		t.setDateFinPromo(parse().ParseDate(t.getStringDateFinPromo()));
		
		//if( !Validateur().isDateNotNullOrFutur(t.getDateDebutPromo()) ) throw new DateTimeException("Date debut promo doit être vide ou > date aujourdh'ui");
		//if( !Validateur().isDateNotNullOrFutur(t.getDateFinPromo()) ) throw new DateTimeException("Date debut promo doit être vide ou > date aujourdh'ui");
		
		/*************** categ id ****************/
		AccesCategorie accescateg = new AccesCategorie();
		
		if(!accescateg.isCodeValide(t.getCategorie())) throw new ValidationException("Code catégorie invalide");
		CategorieDB categ = accescateg.getByCode(t.getCategorie());
		
		if(categ == null ) throw new NotFoundException("Catégorie introuvable");
		t.setCategorieID(categ.getCategorieID());
		
		/*************** theme id ****************/
		AccesTheme accestheme = new AccesTheme();
		if(!accestheme.isCodeValide(t.getTheme())) throw new ValidationException("Code theme invalide");
		ThemeDB theme = accestheme.getByCode(t.getTheme());

		if(theme == null ) throw new NotFoundException("Theme introuvable");
		t.setThemeID(theme.getThemeID());

		String code = genereCode(t);
		if(!isCodeValide(code)) throw new ValidationException("Code generé invalide");
		t.setCode(code);
		
		/** ***************************************
		 *
		 * finish verification de données x
		 * import données de produit vers produitDB x
		 * implement iscodevalide, gen code, getbycode in produit, categ theme x
		 * @JsonIgnore les données existants dans produitDB et pas dans produit x
		 * complete implementation de AccesTable dans produit et remplace insert dans produitresource x
		 * 
		 * test insert produit 
		 * 
		 * refaire les memes taches pour variation
		 * create accesattribut et associations
		 * create accesoptions et associations
		 *
		 ** ***************************************/
	}

	public T delete(T obj) throws Exception {

		T produit = getByCode(obj.getCode());

		if(produit.getQte() > 0 ) throw new Exception("Produit toujours en stock");
		
		List<VariationDB> variations = new AccesVariations().getEqualsList("produitID", produit.getProduitID());
		
		for(VariationDB var : variations)
			if(var.getQte() > 0 )
				throw new Exception("erreur : produit 0 stock mais variation toujours en stock.");
		return super.delete("code", obj.getCode());
	}

	@Override
	protected void erreurExistant(T t) throws Exception {
		throw new Exception("Produit Existant\n"
			+ "Nom ou images déjà utilisées dans le produit '"+t.getNom()+"'\n\n"
			+ "Notes :\n"
			+ "• Nom de produit doit être unique.\n"
			+ "• Les images doivent être utilisées une fois seulement");
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		throw new Exception("Produit '"+ t +"' Inexistant");
	}

	
	public List<T> getSuggestionList(ProduitDB produit)
	{
		predicates = new HashSet<>();
		addNotEqualsPredicate("produitID", produit.getProduitID());
		if (!v().isNullOrEmpty(produit.getNom()) && produit.getNom().length() >= Constantes.MIN_LENGTH_RECHERCHE ) 
		{
			addTextSearchPredicate("nom", produit.getNom());
			return super.getLikeRecherche(null, null, 1, 4, "nom", produit.getNom());
		}
		if(!v().isNullOrEmpty(produit.getCode())) addStringPredicate("code", produit.getCode().substring(0, 2));
		return super.getRecherche(null, null, 1, 4);

/*		
		List<Produit> liste = null;

		List<T> listedb = dynamiqueSelect("categorieID",produit.getCategorieID(),"themeID",produit.getThemeID(),
			"qte >",0L,"maxresult",4);
		
		if (listedb!= null && listedb.size() > 0)
		{
			liste = new ArrayList<Produit>();

			ConcurrentHashMap<Long, CategorieDB> categories = 
					(ConcurrentHashMap<Long, CategorieDB>) getContextAttribute("categories");
			ConcurrentHashMap<Long, ThemeDB> themes = 
					(ConcurrentHashMap<Long, ThemeDB>) getContextAttribute("themes");
			
			for (ProduitDB p : listedb) {
				Produit prod = new Produit(p);
				
				CategorieDB cat = categories.get(p.getCategorieID());
				ThemeDB theme   = themes.get(p.getThemeID());
				
				prod.setCategorie(cat.getNom());
				prod.setTheme(theme.getNom());
				
				prod.setStars(0); // TODO
				prod.setAvis(0); // TODO
				prod.setCommandes(0); // TODO
				liste.add(prod);
			}
		}
		return liste;
*/
	}


	@Override
	protected void deleteErrors(T t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	protected List<T> listeProduits(String nom, String typeprix, String typepromo, String trifiltre, Double prixmin, Double prixmax, Integer qtemin,
			Integer qtemax, Integer minreduc, Integer maxreduc, Integer page, Integer size, String categorienom, String themenom, Boolean isActive ) 
	{
		List<T> listedb;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();
			
			CriteriaBuilder builder = manager.getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(TABLE);
			Root<T> root = criteria.from(TABLE);
			criteria.select(root);
			typeprix = v().isNullOrEmpty(typeprix) ? "prixUnite":"prixPromo";

			HashSet<Predicate> pr = new HashSet<>(); 
			listeProduitsPredicates(isActive, builder, root, pr);
			
			/* ************* nom filtre ************** */
			if( !v().isNullOrEmpty(nom) && !nom.trim().equals("ta9to9")) 
			{
				if(v().isSubCode(nom)) stringPredicate("code", nom, builder, root, pr);
				else if(nom.trim().length() <= 3)
					adminlisteProduitsPredicates(nom, builder, root, pr);
				else textPredicate("nom", nom, builder, root, pr);
			}

			/* *********** categorie filtre ************* */
			if(!v().isNullOrEmpty(categorienom))
			{
				ConcurrentHashMap<String, CategorieDB> categories = 
					(ConcurrentHashMap<String, CategorieDB>) getContextAttribute("categoriesnom");
				CategorieDB cat = categories.get(categorienom);
				if(cat == null) 
					throw new NotFoundException("Categorie '"+categorienom+"' inexistante");
				likePredicate("code", cat.getCode() + "%", builder, root, pr);
			}
			
			/* ************ theme filtre ************** */
			if(!v().isNullOrEmpty(themenom))
			{
				ConcurrentHashMap<String, ThemeDB> themes = 
					(ConcurrentHashMap<String, ThemeDB>) getContextAttribute("themesnom");
				ThemeDB theme = themes.get(themenom);
				if(theme == null) 
					throw new NotFoundException("theme inexistante");
				
				List<Long> tfils = new ArrayList<>();
				tfils.add(theme.getThemeID());
				
				for(ThemeDB th : themes.values())
					if(th.getThemeParent() != null && th.getThemeParent() == theme.getThemeID())
						tfils.add(th.getThemeID());
				pr.add(root.get("themeID").in(tfils));
			}
			
			/* *********** promo filtres ( admin only ) ************* */
			if(typepromo != null && (typepromo.equals("promo") || typepromo.equals("notpromo") || typepromo.equals("futurpromo")))
			{
				
				if(typepromo.equals("promo")) pr.add( builder.and( 
						builder.lessThanOrEqualTo(root.get("dateDebutPromo"), utils().now()),
						builder.greaterThan(root.get("dateFinPromo"), utils().now()) ));
				else if(typepromo.equals("futurpromo")) pr.add( builder.and( 
						builder.greaterThan(root.get("dateDebutPromo"), utils().now()),
						builder.greaterThan(root.get("dateFinPromo"), utils().now()) ));
						
				else pr.add( builder.not(builder.and( builder.isNotNull(root.get("dateFinPromo")), 
						builder.greaterThan(root.get("dateFinPromo"), utils().now()) )));
			}
			/* *********** prix min filtre  ************* */
			if( ! v().isNullOrNegatif(prixmin) ) pr.add(builder.greaterThanOrEqualTo(root.get(typeprix), prixmin));
			/* *********** prix max filtre ************* */
			if( ! v().isNullOrNegatif(prixmax) ) pr.add(builder.lessThanOrEqualTo(root.get(typeprix), prixmax));

			/* *********** quantité min filtre ( admin only ) ************* */
			if( ! v().isNullOrNegatif(qtemin) ) pr.add(builder.greaterThanOrEqualTo(root.get("qte"), qtemin));
			/* *********** quantité max filtre ( admin only ) ************* */
			if( ! v().isNullOrNegatif(qtemax) ) pr.add(builder.lessThanOrEqualTo(root.get("qte"), qtemax));

			/* *********** promo reduction filtre ************* */

			Expression<Number> quot = builder.quot(  root.get("prixPromo") , root.get("prixUnite") );
			Expression<Number> reduc = builder.diff(1D, quot);
			
			if( minreduc != null || maxreduc != null)
			{
				if( minreduc != null ) pr.add(builder.ge(reduc, (Number)(Double.parseDouble(minreduc+"")/100) )); 
				if( maxreduc != null ) pr.add(builder.le(reduc, (Number)(Double.parseDouble(maxreduc+"")/100) ));
			}

			criteria.where(builder.and(pr.toArray(new Predicate[pr.size()])) );

			if (trifiltre != null && trifiltre.equals("TriNom"))  criteria.orderBy(builder.asc(root.get("nom")));
			else if(trifiltre != null && trifiltre.equals("TriPrixUnite")) criteria.orderBy(builder.asc(root.get("prixUnite"))); // prix
			else if(trifiltre != null && trifiltre.equals("TriPrixPromo")) criteria.orderBy(builder.asc(root.get("prixPromo")));
			else if(trifiltre != null && trifiltre.equals("TriDebutPromo")) criteria.orderBy(builder.desc(root.get("dateDebutPromo")));
			else if(trifiltre != null && trifiltre.equals("TriFinPromo"))criteria.orderBy(builder.desc(root.get("dateFinPromo")));
			else if(trifiltre != null && trifiltre.equals("TriPromo")) criteria.orderBy(builder.desc(reduc));
			/*(prixUnite - prixPromo)/prixUnite desc */
			//else if(trifiltre != null && trifiltre.equals("TriCommandes"))criteria.orderBy(builder.desc(root.get("commandes")));
			//else if(trifiltre != null && trifiltre.equals("TriAvis"))criteria.orderBy(builder.desc(root.get("avis")));
			else if(trifiltre != null && trifiltre.equals("TriDateAsc"))criteria.orderBy(builder.asc(root.get("produitID")));
			else criteria.orderBy(builder.desc(root.get("produitID"))); // date default
			//commandes, pertinence, date, prix
			// if nom && :code
			Query query = manager.createQuery(criteria);
			if( nom != null && nom.length() >= Constantes.MIN_LENGTH_RECHERCHE && !v().isSubCode(nom) && !nom.trim().equals("ta9to9"))
			{
				String tx = "+"+nom.replaceAll("[^-\\wéèàêô\\s]", "").trim().replace(" ", "* +") + "*";
				query.setParameter("nom",tx.replace("+-", "-").replace("- ", "-").replace("+ ", "+"));
			}
			
			query.setFirstResult((page - 1) * size).setMaxResults(size);
			
			listedb = ((List<T>) query.getResultList());
			if( listedb != null && !listedb.isEmpty())
			{
				ConcurrentHashMap<Long, CategorieDB> categories = 
						(ConcurrentHashMap<Long, CategorieDB>) getContextAttribute("categories");
				ConcurrentHashMap<Long, ThemeDB> themes = 
						(ConcurrentHashMap<Long, ThemeDB>) getContextAttribute("themes");
				
				for (ProduitDB p : listedb) {
					CategorieDB cat = categories.get(p.getCategorieID());
					ThemeDB theme = themes.get(p.getThemeID());
					
					p.setCategorie(cat.getNom());
					p.setTheme(theme.getNom());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			listedb = null;
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		return listedb;
	}
	
	
	protected abstract void listeProduitsPredicates(boolean isActive, CriteriaBuilder builder, Root<T> root, HashSet<Predicate> pr);
	protected abstract void adminlisteProduitsPredicates(String nom, CriteriaBuilder builder, Root<T> root, HashSet<Predicate> pr);
	
	@SuppressWarnings("unchecked")
	public Long getListeCount(String nom, Double prixmin, Double prixmax, int qtemin, String categorienom,
			String themenom, boolean isActive) {
		

		Long size;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();
			
			CriteriaBuilder builder = manager.getCriteriaBuilder();
			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
			Root<T> root = criteria.from(TABLE);
			criteria.select(builder.count(root));

			HashSet<Predicate> pr = new HashSet<>(); 
			listeProduitsPredicates(isActive, builder, root, pr);
			


			/* ************* nom filtre ************** */

			if( !v().isNullOrEmpty(nom) && !nom.trim().equals("ta9to9")) 
			{
				if(v().isSubCode(nom)) stringPredicate("code", nom, builder, root, pr);
				else if(nom.trim().length() <= 3)
					adminlisteProduitsPredicates(nom, builder, root, pr);
				else textPredicate("nom", nom, builder, root, pr);
			}
			
			/* *********** categorie filtre ************* */
			if(!v().isNullOrEmpty(categorienom))
			{
				ConcurrentHashMap<String, CategorieDB> categories = 
					(ConcurrentHashMap<String, CategorieDB>) getContextAttribute("categoriesnom");
				CategorieDB cat = categories.get(categorienom);
				if(cat == null) 
					throw new NotFoundException("Categorie inexistante");
				likePredicate("code", cat.getCode() + "%", builder, root, pr);
			}
			
			/* ************ theme filtre ************** */
			if(!v().isNullOrEmpty(themenom))
			{
				ConcurrentHashMap<String, ThemeDB> themes = 
					(ConcurrentHashMap<String, ThemeDB>) getContextAttribute("themesnom");
				ThemeDB theme = themes.get(themenom);
				if(theme == null) 
					throw new NotFoundException("theme inexistante");
				
				List<Long> tfils = new ArrayList<>();
				tfils.add(theme.getThemeID());
				
				for(ThemeDB th : themes.values())
					if(th.getThemeParent() != null && th.getThemeParent() == theme.getThemeID())
						tfils.add(th.getThemeID());
				pr.add(root.get("themeID").in(tfils));
			}
			
			/* *********** prix min filtre  ************* */
			if( !v().isNullOrNegatif(prixmin) ) pr.add(builder.greaterThanOrEqualTo(root.get("prixUnite"), prixmin));
			/* *********** prix max filtre ************* */
			if( !v().isNullOrNegatif(prixmax) ) pr.add(builder.lessThanOrEqualTo(root.get("prixUnite"), prixmax));

			/* *********** quantité min filtre ( admin only ) ************* */
			if( !v().isNullOrNegatif(qtemin) ) pr.add(builder.greaterThanOrEqualTo(root.get("qte"), qtemin));

			criteria.where(builder.and(pr.toArray(new Predicate[pr.size()])) );

			Query query = manager.createQuery(criteria);
			
			if( nom != null && nom.length() >= Constantes.MIN_LENGTH_RECHERCHE && !v().isSubCode(nom) && !nom.trim().equals("ta9to9") )
			{
				String tx = "+"+nom.replaceAll("^\\W", "").trim().replace(" ", "* +") + "*";
				query.setParameter("nom",tx.replace("+-", "-").replace("- ", "-").replace("+ ", "+"));
			}
			
			query.setMaxResults(1);
			size = (Long) query.getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			size = 0l;
		} finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		
		return size;
	}
	@SuppressWarnings("unchecked")
	public List<ProduitSuggestion> getSearchSuggestions(String text) {
		
		EntityManager manager = null;
		List<Object[]> resultat = null;
		final List<ProduitSuggestion> suggestions = new ArrayList<>();
		try {
			text = text == null ? null : "+"+text.replaceAll("^\\W", "").trim().replace(" ", "* +") + "*";
			if(text == null || text.length() < Constantes.MIN_LENGTH_RECHERCHE) 
				return null;

			text = text.replace("+-", "-").replace("- ", "-").replace("+ ", "+");
			
			manager = getFactory().createEntityManager();
			Query q = manager.createNativeQuery(
			"SELECT typenom, type, cmp FROM ("+
				"( SELECT 'categorie' as typenom, p.categorieID as type , COUNT(p.produitID) as cmp "+
				"FROM produit p "+
				"WHERE match(p.nom) against ('"+text+"' in boolean mode) "+
				"AND p.isActive = 1 "+
				"GROUP BY p.categorieID "+
				"LIMIT 10) "+
				"UNION "+
				"( SELECT 'theme' as typenom, p.themeID as type , COUNT(p.produitID) as cmp "+
				"FROM produit p "+
				"WHERE match(p.nom) against ('"+text+"' in boolean mode) "+
				"AND p.isActive = 1 "+
				"GROUP BY p.themeID "+
				"LIMIT 10 )"+
			") as g " + 
			"order by g.cmp desc " +
			"limit 10");

			resultat = q.getResultList();
			
			if (resultat == null || resultat.isEmpty()) return null;
			
			resultat.forEach(obj -> {

				ProduitSuggestion sug = new ProduitSuggestion();

				Long id= ((Integer)obj[1]).longValue();
				
				if(obj[0].equals("categorie")) {

					 
					CategorieDB cat = ((ConcurrentHashMap<Long, CategorieDB>) 
							getContextAttribute("categories")).get(id);
					
					if(cat == null) 
						throw new NotFoundException("Categorie "+obj[1] + " introuvable dans ConcurentMap mais existe in db");
				
					sug.setCode(cat.getCode());
					sug.setCount(((BigInteger) obj[2]).longValue());
					sug.setLink(cat.getLink());
					sug.setNom(cat.getNom());
				} 
				else {
					
					ThemeDB theme = ((ConcurrentHashMap<Long, ThemeDB>) 
							getContextAttribute("themes")).get(id);
					
					if(theme == null) 
						throw new NotFoundException("Theme "+obj[1] + " introuvable dans ConcurentMap mais existe in db");

					sug.setCode(theme.getCode());
					sug.setCount(((BigInteger) obj[2]).longValue());
					sug.setLink(theme.getLink());
					sug.setNom(theme.getNom());
				}
				suggestions.add(sug);
			});
		}
		catch(Exception ex) {
			ex.printStackTrace();
			resultat = null;
		}
		finally {
			if(manager != null && manager.isOpen()) manager.close();
		}
		return suggestions;
	}
}





