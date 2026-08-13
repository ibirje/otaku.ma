package ma.otaku.acces.produit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.validation.ValidationException;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.acces.base.ICodeAcces;
import ma.otaku.acces.triggers.categorie.AfterUpdateCategorie;
import ma.otaku.business.produits.Categorie;
import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.stats.ProduitStats;
import ma.otaku.data.type.CategorieDB;

public class AccesCategorie extends AccesTable<CategorieDB> implements ICodeAcces<CategorieDB> {

	public AccesCategorie() {
		super(CategorieDB.class);
		updateTriggers.add(new AfterUpdateCategorie(this));
	}

	public AccesCategorie(byte param) {
		super(CategorieDB.class, param);
	}

	@Override
	public boolean isCodeValide(String code) throws Exception {
		return code.matches("(\\w\\w)(_\\w\\w)*");
	}

	@Override
	public CategorieDB update(CategorieDB t) throws Exception {
		CategorieDB cat = super.update(t);
		return refreshCacheUpdate(cat);
	}

	@Override
	protected Query isInDBQuery(CategorieDB t, EntityManager manager) throws Exception {
		String where, code;
		if (t.getCategorieParentCode() == null) {
			code = t.getCode();
			where = "nom = :nom OR code = :code";
		} else {
			code = t.getCategorieParentCode() + "___";
			where = "nom = :nom AND code like :code";
		}
		return manager.createQuery("SELECT C FROM CategorieDB C WHERE " + where).setParameter("code", code)
				.setParameter("nom", t.getNom()).setMaxResults(1);
	}

	@Override
	protected void verifierDonnees(CategorieDB t) throws Exception {
		String pcode = t.getCategorieParentCode();
		String code = t.getCode().toLowerCase();
		String nom = t.getNom().toLowerCase();

		if (pcode != null && !isCodeValide(pcode))
			throw new ValidationException("Code categorie parent invalide.");
		if (v().isNullOrEmpty(nom))
			throw new NullPointerException("Nom catégorie vide");
		if (v().isNullOrEmpty(code) || code.length() < 2)
			throw new ValidationException("Code categorie vide.");

		if (!v().isNullOrEmpty(pcode)) {
			CategorieDB catparent = getByCode(t.getCategorieParentCode());
			if (catparent == null)
				throw new NullPointerException("Catégorie parent introuvable");
			t.setCategorieParent(catparent.getCategorieID());
		}

		for (int i = 0; i < code.length(); i++)
			if (!nom.contains(code.charAt(i) + "") && !('0' <= code.charAt(i) && code.charAt(i) <= '9')) {
				// System.out.println(nom + " !contains " + code.charAt(i));
				throw new ValidationException("Code suffixe '" + code + "' invalide.");
			}

		t.setCode(genereCode(t));
	}

	public CategorieDB delete(CategorieDB obj) throws Exception {
		CategorieDB cat = getByCode(obj.getCode());
		if (cat == null)
			erreurInexistant(obj.getCode());
		if (cat.getNombreProduits() > 0)
			throw new Exception("Cette catégorie est déjà utilisée par " + cat.getNombreProduits()
					+ " produits et ne peut pas être suprimée");

		CategorieDB deleted = super.delete("code", obj.getCode());
		refreshCacheDelete(deleted);
		return deleted;
	}

	@Override
	protected void erreurExistant(CategorieDB t) throws Exception {

		throw new Exception("Categorie " + t.getNom() + " existe déjà");
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		throw new Exception("Categorie " + t + " introuvable");
	}

	@Override
	protected void deleteErrors(CategorieDB t) throws Exception {
	}

	@Override
	public String genereCode(CategorieDB t) {
		if (v().isNullOrEmpty(t.getCategorieParentCode()))
			return t.getCode();
		return t.getCategorieParentCode() + "_" + t.getCode();
	}

	public List<Categorie> getBrancheCategories(Long id) {
		List<Categorie> branche = new ArrayList<>();
		Long categid = id;
		while (categid != null) {
			CategorieDB categdb = getByID(categid);
			branche.add(0, new Categorie(categdb));
			categid = categdb.getCategorieParent();
		}
		return branche;
	}

	public CategorieDB getByNom(String nom) {
		return getEquals("nom", nom);
	}

	public List<CategorieDB> getList() {
		return getList(null, null);
	}

	@Override
	public CategorieDB getByCode(String code) throws Exception {
		return getEquals("code", code);
	}

	public List<CategorieDB> getSortedList() {

		List<CategorieDB> categories = getList();

		List<CategorieDB> catWProd = categoriesWithProduits();
		HashSet<CategorieDB> toremove = new HashSet<>();

		for (CategorieDB cat : categories) {
			boolean clear = false;

			for (CategorieDB cwp : catWProd)
				if (cwp.getCode().matches(cat.getCode() + "(_\\w\\w)*")) {
					clear = true;
					break;
				}

			if (!clear)
				toremove.add(cat);
		}

		for (CategorieDB rm : toremove)
			categories.remove(rm);

		categories.sort(new Comparator<CategorieDB>() {
			@Override
			public int compare(CategorieDB o1, CategorieDB o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
		});

		String mcode = null;
		String scode = null;

		for (int mcp = 0; mcp < categories.size(); mcp++) {
			mcode = categories.get(mcp).getCode();

			if (mcode.matches("\\w\\w_\\w\\w_\\w\\w"))
				continue;

			for (int scp = mcp + 1; scp < categories.size(); scp++) {
				scode = categories.get(scp).getCode();
				if (scode.matches(mcode + "_\\w\\w"))
					categories.get(scp).setCategorieParentCode(scode);
			}
		}

		return categories;
	}

	@Override
	public CategorieDB insert(CategorieDB t) throws Exception {
		CategorieDB cat = super.insert(t);
		return refreshCacheInsert(cat);
	}

	private CategorieDB refreshCacheInsert(CategorieDB cat) throws Exception {
		if (cat != null) {
			getIDCache().put(cat.getCategorieID(), cat);
			getNomCache().put(cat.getNom(), cat);
		}
		return cat;
	}

	private CategorieDB refreshCacheDelete(CategorieDB cat) throws Exception {
		if (cat != null) {
			getIDCache().remove(cat.getCategorieID());
			getNomCache().remove(cat.getNom());
		}
		return cat;
	}

	private CategorieDB refreshCacheUpdate(CategorieDB cat) throws Exception {
		if (cat != null) {
			getIDCache().remove(cat.getCategorieID());
			getIDCache().put(cat.getCategorieID(), cat);

			getNomCache().remove(cat.getNom());
			getNomCache().put(cat.getNom(), cat);
		}
		return cat;
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<Long, CategorieDB> getIDCache() {
		return (ConcurrentHashMap<Long, CategorieDB>) getContextAttribute("categories");
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, CategorieDB> getNomCache() {
		return (ConcurrentHashMap<String, CategorieDB>) getContextAttribute("categoriesnom");
	}

	@SuppressWarnings("unchecked")
	private List<CategorieDB> categoriesWithProduits() {
		List<CategorieDB> categories = null;
		EntityManager manager = null;
		try {

			manager = getFactory().createEntityManager();

			Query query = manager.createQuery("SELECT c FROM CategorieDB c "
					+ "JOIN ProduitAdmin p ON p.categorieID = c.categorieID " + "group by c.nom");

			categories = query.getResultList();
			
		} catch (NoResultException nores) {
			categories = null;
		} catch (Exception e) {
			categories = null;
			e.printStackTrace();
		} finally {
			if (manager != null && manager.isOpen()) manager.close();
		}
		return categories;
	}

	public void changeImages() throws Exception {

		List<CategorieDB> cats = getList();
		AccesProduitsAdmin acces = new AccesProduitsAdmin(AccesProduits.READONLY);
		for (CategorieDB categorie : cats) {
			List<ProduitAdmin> pl = acces.dynamiqueSelect("categorieID =", categorie.getCategorieID(), "maxresult", 1);

			if (!v().isNullOrEmpty((pl))) {
				if (v().isNullOrEmpty(categorie.getSmallImage()))
					categorie.setSmallImage(pl.get(0).getThumbnail());
				else {
					if (pl.size() <= 1)
						return;

					if (pl.get(pl.size() - 1).getThumbnail().equals(categorie.getSmallImage()))
						categorie.setSmallImage(pl.get(0).getThumbnail());
					else
						for (int i = 0; i < pl.size() - 1; i++)
							if (pl.get(i).getThumbnail().equals(categorie.getSmallImage()))
								categorie.setSmallImage(pl.get(i + 1).getThumbnail());
				}
				update(categorie);
			}
		}
	}
	
	
	public List<CategorieDB> getSuggestionSectionCategories() {
		
		/* -----------------------------------------------------------------------------
		 * --   find categories by available products & that have best sales.	--
		 * --   
		 * 
		 * 	select cat.*
		 *	FROM categorie cat
		 *	JOIN produit prod on prod.categorieID = cat.categorieID
		 *	JOIN produit_stats pstat on pstat.produitID = prod.produitID
		 *	WHERE prod.qte > 0
		 * 	GROUP BY prod.categorieID
		 * 	HAVING COUNT(prod.produitID) >= 6
		 *	ORDER BY SUM(pstat.commandePrixTotal) DESC ,SUM(prod.qte) DESC;
		 * -----------------------------------------------------------------------------*/
		
		String req = "select cat FROM "+ TABLE.getSimpleName() +" cat "+
			"JOIN ProduitClient prod ON prod.categorieID = cat.categorieID "+
			"JOIN ProduitStats pstat ON pstat.produitID = prod.produitID "+
			"WHERE prod.qte > 0 "+
			"GROUP BY prod.categorieID "
			+"HAVING COUNT(distinct prod.produitID) >= 4 "
			+"ORDER BY SUM(pstat.commandePrixTotal) desc ,SUM(prod.qte) desc "
			;
		
		return select(5,req);
	}
	
	
	

	/******** resumé produits par categorie ***** */
	public void resumeCategorie(Long categorieID,AccesProduits<? extends ProduitDB> acces,AccesProduitStats accstats) throws Exception 
	{
		@SuppressWarnings("unchecked")
		List<ProduitDB> p1 = (List<ProduitDB>) acces.getEqualsList("categorieID", categorieID);
		
		long 	 countc = p1 == null ? 0L : p1.size();
		long 		qte = 0;
		long pendingQte = 0;
		long  activeQte = 0;
		
		ProduitStats pstats = null;
		
		for(ProduitDB p : p1)
		{
			pstats = accstats.selectWhereFirst("produitID = :s0 AND variationID is null", p.getProduitID());
				
				if(pstats == null)
					continue;
			pendingQte += pstats.getPendingQte();
			qte += pstats.getStockQte();
			if(p.getIsActive()) activeQte += pstats.getStockQte();
		}
		
		CategorieDB categorie = getByID(categorieID);

		categorie.setQte(qte);
		categorie.setPendingQte(pendingQte);
		categorie.setNombreProduits(countc);
		categorie.setActiveQte(activeQte);
			
		update(categorie);
		
	}
	
	
}
