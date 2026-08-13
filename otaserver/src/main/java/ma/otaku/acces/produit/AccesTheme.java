package ma.otaku.acces.produit;

import java.util.ArrayList;
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
import ma.otaku.acces.triggers.theme.AfterUpdateTheme;
import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.stats.ProduitStats;
import ma.otaku.data.type.ThemeDB;

public class AccesTheme extends AccesTable<ThemeDB> implements ICodeAcces<ThemeDB> {

	public AccesTheme() {

		super(ThemeDB.class);
		updateTriggers.add(new AfterUpdateTheme(this));
	}

	public AccesTheme(byte param) {
		super(ThemeDB.class, param);
	}

	@Override
	public ThemeDB insert(ThemeDB t) throws Exception {
		ThemeDB theme = super.insert(t);
		return refreshCacheInsert(theme);
	}

	@Override
	public ThemeDB update(ThemeDB t) throws Exception {
		ThemeDB theme = super.update(t);
		return refreshCacheUpdate(theme);
	}

	public ThemeDB getThemeByNom(String th) {
		return getEquals("nom", th);
	}

	public List<ThemeDB> getBrancheThemes(Long id) {

		List<ThemeDB> branche = new ArrayList<>();
		Long themeid = id;
		while (themeid != null) {
			ThemeDB themedb = getByID(id);
			branche.add(0, themedb);
			themeid = themedb.getThemeParent();
		}

		return branche;
	}

	public List<ThemeDB> getList() {
		return getList(null, null);
	}

	@Override
	public String genereCode(ThemeDB t) {

		if (v().isNullOrEmpty(t.getThemeParentCode()))
			return t.getCode();
		return t.getThemeParentCode() + "_" + t.getCode();
	}

	@Override
	public ThemeDB getByCode(String code) throws Exception {
		return getEquals("code", code);
	}

	@Override
	public boolean isCodeValide(String code) throws Exception {
		return code.matches("(\\w\\w)(_\\w\\w)*");
	}

	@Override
	protected Query isInDBQuery(ThemeDB t, EntityManager manager) throws Exception {
		return manager.createQuery("SELECT C FROM ThemeDB C WHERE nom = :nom OR code = :code")
				.setParameter("code", t.getCode()).setParameter("nom", t.getNom()).setMaxResults(1);
	}

	@Override
	protected void verifierDonnees(ThemeDB t) throws Exception {

		String pcode = t.getThemeParentCode();
		String code = t.getCode().toLowerCase();
		String nom = t.getNom().toLowerCase();

		if (pcode != null && !isCodeValide(pcode))
			throw new ValidationException("Code theme parent invalide.");
		if (v().isNullOrEmpty(nom))
			throw new NullPointerException("Nom theme vide");
		if (v().isNullOrEmpty(code) || code.length() < 2)
			throw new ValidationException("Code theme vide.");

		if (!v().isNullOrEmpty(pcode)) {
			ThemeDB tparent = getByCode(pcode);
			if (tparent == null)
				throw new NullPointerException("theme parent introuvable");
			t.setThemeParent(tparent.getThemeID());
		}

		for (int i = 0; i < code.length(); i++)
			if (!nom.contains(code.charAt(i) + "") && !('0' <= code.charAt(i) && code.charAt(i) <= '9')) {
				// System.out.println(nom + " !contains " + code.charAt(i));
				throw new ValidationException("Code suffixe '" + code + "' invalide.");
			}

		t.setCode(genereCode(t));
	}

	public ThemeDB delete(ThemeDB obj) throws Exception {

		ThemeDB theme = getByCode(obj.getCode());
		if (theme == null)
			erreurInexistant(obj.getCode());
		if (theme.getNombreProduits() > 0)
			throw new Exception("Cette theme est déjà utilisée par " + theme.getNombreProduits()
					+ " produits et ne peut pas être suprimée");

		ThemeDB deleted = super.delete("code", obj.getCode());
		refreshCacheDelete(deleted);
		return deleted;
	}

	@Override
	protected void erreurExistant(ThemeDB t) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void deleteErrors(ThemeDB t) throws Exception {
		// TODO Auto-generated method stub

	}

	public List<ThemeDB> getSortedList() {

		List<ThemeDB> themes = getList();
		List<ThemeDB> themeWProd = themesWithProduits();
		HashSet<ThemeDB> toremove = new HashSet<>();

		for (ThemeDB cat : themes) {
			boolean clear = false;

			for (ThemeDB cwp : themeWProd)
				if (cwp.getCode().matches(cat.getCode() + "(_\\w\\w)*")) {
					clear = true;
					break;
				}

			if (!clear)
				toremove.add(cat);
		}

		for (ThemeDB rm : toremove)
			themes.remove(rm);

		themes.sort(new Comparator<ThemeDB>() {
			@Override
			public int compare(ThemeDB o1, ThemeDB o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
		});

		String mcode = null;
		String scode = null;

		for (int mcp = 0; mcp < themes.size(); mcp++) {
			mcode = themes.get(mcp).getCode();
			if (mcode.matches("\\w\\w_\\w\\w"))
				continue;

			for (int scp = mcp + 1; scp < themes.size(); scp++) {
				scode = themes.get(scp).getCode();
				if (scode.matches(mcode + "_\\w\\w"))
					themes.get(scp).setThemeParentCode(scode);
			}
		}

		return themes;
	}

	private ThemeDB refreshCacheInsert(ThemeDB theme) throws Exception {
		if (theme != null) {
			getIDCache().put(theme.getThemeID(), theme);
			getNomCache().put(theme.getNom(), theme);
		}
		return theme;
	}

	private ThemeDB refreshCacheDelete(ThemeDB theme) throws Exception {
		if (theme != null) {
			getIDCache().remove(theme.getThemeID());
			getNomCache().remove(theme.getNom());
		}
		return theme;
	}

	private ThemeDB refreshCacheUpdate(ThemeDB theme) throws Exception {
		if (theme != null) {
			getIDCache().remove(theme.getThemeID());
			getIDCache().put(theme.getThemeID(), theme);

			getNomCache().remove(theme.getNom());
			getNomCache().put(theme.getNom(), theme);
		}
		return theme;
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<Long, ThemeDB> getIDCache() {
		return (ConcurrentHashMap<Long, ThemeDB>) getContextAttribute("themes");
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, ThemeDB> getNomCache() {
		return (ConcurrentHashMap<String, ThemeDB>) getContextAttribute("themesnom");
	}

	@SuppressWarnings("unchecked")
	private List<ThemeDB> themesWithProduits() {
		List<ThemeDB> themes = null;
		EntityManager manager = null;
		try {

			manager = getFactory().createEntityManager();

			Query query = manager.createQuery(
					"SELECT c FROM ThemeDB c " + "JOIN ProduitAdmin p ON p.themeID = c.themeID " + "group by c.nom");

			themes = query.getResultList();
		} 
		catch (NoResultException nores) { themes = null; } 
		catch (Exception e) { themes = null; e.printStackTrace(); } 
		finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return themes;
	}

	public void changeImages() throws Exception {

		List<ThemeDB> cats = getList();
		AccesProduitsAdmin acces = new AccesProduitsAdmin(AccesProduits.READONLY);
		for (ThemeDB theme : cats) {
			List<ProduitAdmin> pl = null;

			if (!v().isNullOrEmpty(theme.getSmallImage()))
				pl = acces.dynamiqueSelect("themeID =", theme.getThemeID(), "thumbnail != ", theme.getSmallImage(), "maxresult", 1);
			else
				pl = acces.dynamiqueSelect("themeID =", theme.getThemeID(), "maxresult", 1);
			
			if (!v().isNullOrEmpty((pl))) {
				theme.setSmallImage(pl.get(0).getThumbnail());
				update(theme);
			}
		}
	}

	/******** resumé produits par theme ***** */
	public void resumeTheme(Long themeID, AccesProduits<? extends ProduitDB> acces,AccesProduitStats accstats) throws Exception 
	{
		@SuppressWarnings("unchecked")
		List<ProduitDB> p2 = (List<ProduitDB>) acces.getEqualsList("themeID", themeID);
		
		long countth = p2 == null ? 0L : p2.size();
		long qte = 0 , pendingQte = 0, activeQte = 0;

		ProduitStats pstats = null;
		
		for(ProduitDB p : p2)
		{
			pstats = accstats.selectWhereFirst("produitID = :s0 AND variationID is null", p.getProduitID());
			
			if(pstats == null)
				continue;
		
			pendingQte += pstats.getPendingQte();
			qte += pstats.activeStock();
			if(p.getIsActive()) activeQte += pstats.activeStock();
		}
		
		AccesTheme accesth = new AccesTheme();
		ThemeDB theme = accesth.getByID(themeID);

		theme.setQte(qte);
		theme.setPendingQte(pendingQte);
		theme.setNombreProduits(countth);
		theme.setActiveQte(activeQte);
			
		accesth.update(theme);
	}
}
