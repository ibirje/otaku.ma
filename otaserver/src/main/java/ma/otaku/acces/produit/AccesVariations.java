package ma.otaku.acces.produit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.core.Response;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.acces.base.ICodeAcces;
import ma.otaku.acces.triggers.variation.AfterDeleteVariation;
import ma.otaku.acces.triggers.variation.AfterInsertVariation;
import ma.otaku.acces.triggers.variation.AfterUpdateVariation;
import ma.otaku.business.produits.Attribut;
import ma.otaku.business.produits.BeanAttributOptions;
import ma.otaku.business.produits.BeanAttributOptionsDB;
import ma.otaku.business.produits.BeanInsertVariation;
import ma.otaku.business.produits.BeanVariationOptions;
import ma.otaku.business.produits.OptionAttribut;
import ma.otaku.business.produits.Variation;
import ma.otaku.data.produit.AttributDB;
import ma.otaku.data.produit.OptionAttributDB;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.produit.VariationDB;
import ma.otaku.data.produit.VariationOptionDB;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.Parse;
import ma.otaku.utils.Utils;

public class AccesVariations extends AccesTable<VariationDB> implements ICodeAcces<VariationDB> {

	public AccesVariations() {
		super(VariationDB.class);

		insertTriggers.add(new AfterInsertVariation());
		deleteTriggers.add(new AfterDeleteVariation());
		updateTriggers.add(new AfterUpdateVariation());
	}

	public AccesVariations(byte param) {

		super(VariationDB.class, param);
	}

	public Map<String, List<OptionAttribut>> getOptionAttributsByProduit(ProduitDB produit) {
		Map<String, List<OptionAttribut>> attquery = null;
		EntityManager manager = null;

		try {
			Long produitid = produit.getProduitID();

			manager = getFactory().createEntityManager();

			String req = "SELECT T.nom, O.code, O.couleur, O.nom, O.ordre  " + "FROM VariationDB V "
					+ "JOIN VariationOptionDB VO ON V.variationID = VO.variationID  "
					+ "JOIN OptionAttributDB O ON O.optionAttributID = VO.optionAttributID  "
					+ "INNER JOIN AttributDB T ON O.attributID = T.attributID  " + "WHERE V.QTE > "
					+ Constantes.MIN_QTE_CLIENT + " AND V.produitID = :produitid AND V.isActive = 1";

			Query query = manager.createQuery(req).setParameter("produitid", produitid);

			@SuppressWarnings("unchecked")
			List<Object[]> resultList = query.getResultList();

			if (resultList != null && resultList.size() > 0) {
				attquery = new HashMap<String, List<OptionAttribut>>();
				for (Object[] result : resultList) {

					String attribut = (String) result[0];
					String code = (String) result[1], couleur = (String) result[2], nom = (String) result[3];
					int ordre = (int) result[4];

					List<OptionAttribut> options;
					options = attquery.get(attribut);
					if (options == null) {
						options = new ArrayList<OptionAttribut>();
						attquery.put(attribut, options);
					}
					options.add(new OptionAttribut(code, nom, couleur, "", true, ordre));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			attquery = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}

		return attquery;
	}

	public List<VariationDB> getVariationsDB(ProduitDB produit) {

		return dynamiqueSelect("produitID =", produit.getProduitID(), "QTE >", Constantes.MIN_QTE_CLIENT);
	}

	@SuppressWarnings("unchecked")
	public List<VariationDB> getVariationsDBByProduitCode(String code) {

		List<VariationDB> varsDB = null;
		EntityManager manager = null;

		try {

			manager = getFactory().createEntityManager();

			String req = "SELECT V FROM VariationDB V JOIN ProduitAdmin P On V.produitID = P.produitID WHERE P.code = :code ORDER BY V.nom";
			Query query = manager.createQuery(req).setParameter("code", code);
			varsDB = (List<VariationDB>) query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			varsDB = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return varsDB;
	}

	public List<Variation> getVariations(List<VariationDB> varsDB) {
		List<Variation> vars = null;
		if (varsDB != null && !varsDB.isEmpty()) {
			vars = new ArrayList<Variation>();
			for (VariationDB vdb : varsDB)
				vars.add(new Variation(vdb));
		}
		return vars;
	}

	@SuppressWarnings("unchecked")
	public List<String> getVariationOptions(VariationDB vardb) {
		List<String> options = null;
		EntityManager manager = null;

		try {
			manager = getFactory().createEntityManager();
			String req = "SELECT O.nom FROM OptionAttributDB O "
					+ "JOIN VariationOptionDB VO ON O.optionAttributID = VO.optionAttributID "
					+ "JOIN VariationDB V ON V.variationID = VO.variationID " + "WHERE V.variationID = :vid";

			Query query = manager.createQuery(req).setParameter("vid", vardb.getVariationID());

			options = (List<String>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			options = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return options;
	}

	public Map<String, List<String>> VariationsOptionsMap(List<VariationDB> variations) {
		Map<String, List<String>> map = new HashMap<>();
		for (VariationDB v : variations) {
			map.put(v.getCode(), getVariationOptions(v));
		}

		return map;
	}

	public List<BeanVariationOptions> getListeVariationOptions(List<VariationDB> variationsdb) {

		if (variationsdb == null || variationsdb.isEmpty())
			return null;

		List<BeanVariationOptions> liste = new ArrayList<>(); // TODO CHANGE TO HASHSET

		for (VariationDB v : variationsdb)
			liste.add(new BeanVariationOptions(v, getVariationOptions(v)));

		return liste;
	}

	public AttributDB insertAttribut(ProduitDB produit, Attribut attribut) {
		AttributDB newattribut = new AttributDB(attribut);
		newattribut.setProduitID(produit.getProduitID());
		EntityManager manager = null;

		try {
			manager = getFactory().createEntityManager();
			manager.getTransaction().begin();

			manager.persist(newattribut);
			manager.flush();
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager != null && manager.getTransaction().isActive())
				manager.getTransaction().rollback();
			e.printStackTrace();
			newattribut = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}

		manager.close();
		return newattribut;
	}

	public int insertOptions(AttributDB attributResultat, List<OptionAttribut> list) {
		int count = 0;
		int it = 0;
		for (OptionAttribut opt : list) {
			it++;
			String str = "_" + (it < 10 ? "0" : "") + it;

			OptionAttributDB option = new OptionAttributDB(opt);

			option.setAttributID(attributResultat.getAttributID());

			option.setCode(attributResultat.getCode() + str);
			opt.setCode(option.getCode());

			if (!insertOption(option))
				count++;
		}

		return count;
	}

	public boolean insertOption(OptionAttributDB option) {
		EntityManager manager = null;

		try {
			manager = getFactory().createEntityManager();
			manager.getTransaction().begin();

			manager.persist(option);

			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager != null && manager.getTransaction().isActive())
				manager.getTransaction().rollback();
			return false;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}

		return true;
	}

	// TODO CHANGE TO CODE
	public List<BeanAttributOptions> getAttributsOptions(String nom) {

		List<BeanAttributOptions> liste = new ArrayList<>();
		EntityManager manager = null;
		try {

			manager = getFactory().createEntityManager();

			String req = "SELECT T FROM AttributDB T " + "JOIN ProduitAdmin P ON P.produitID = T.produitID "
					+ "WHERE P.nom = :nom";
			Query query = manager.createQuery(req).setParameter("nom", nom).setMaxResults(7);

			@SuppressWarnings("unchecked")
			List<AttributDB> attributs = query.getResultList();

			if (attributs == null || attributs.isEmpty())
				return null;

			for (AttributDB attribut : attributs) {
				List<OptionAttribut> opt = getOptions(attribut.getAttributID());

				if (opt == null || opt.isEmpty())
					continue;

				BeanAttributOptions bean = new BeanAttributOptions();

				bean.setOptions(opt);
				bean.setAttribut(new Attribut(attribut));

				liste.add(bean);
			}

		} catch (Exception ex) {
			if (manager != null && manager.getTransaction().isActive())
				manager.getTransaction().rollback();
			ex.printStackTrace();
			liste = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return liste;
	}

	public List<BeanAttributOptionsDB> getAttributsOptionsDBByCode(String code) {
		List<BeanAttributOptionsDB> liste = new ArrayList<>();
		EntityManager manager = null;
		try {

			manager = getFactory().createEntityManager();

			String req = "SELECT T FROM AttributDB T " + "JOIN ProduitAdmin P ON P.produitID = T.produitID "
					+ "WHERE P.code = :code";
			Query query = manager.createQuery(req).setParameter("code", code).setMaxResults(Constantes.MAX_ATTRIBUTS);

			@SuppressWarnings("unchecked")
			List<AttributDB> attributs = query.getResultList();

			if (attributs == null || attributs.isEmpty())
				return null;

			for (AttributDB attribut : attributs) {
				List<OptionAttributDB> opt = getOptionsDB(attribut.getAttributID());

				if (opt == null || opt.isEmpty())
					continue;

				BeanAttributOptionsDB bean = new BeanAttributOptionsDB();

				bean.setOptions(opt);
				bean.setAttribut(attribut);

				liste.add(bean);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			liste = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return liste;
	}

	public List<BeanAttributOptions> getAttributsOptionsByCode(String code) {

		List<BeanAttributOptions> liste = new ArrayList<>();
		EntityManager manager = null;
		try {

			manager = getFactory().createEntityManager();

			String req = "SELECT T FROM AttributDB T " + "JOIN ProduitAdmin P ON P.produitID = T.produitID "
					+ "WHERE P.code = :code";
			Query query = manager.createQuery(req).setParameter("code", code).setMaxResults(Constantes.MAX_ATTRIBUTS);

			@SuppressWarnings("unchecked")
			List<AttributDB> attributs = query.getResultList();

			if (attributs == null || attributs.isEmpty())
				throw new Exception("Attributs NULL");

			for (AttributDB attribut : attributs) {
				List<OptionAttribut> opt = getOptions(attribut.getAttributID());

				if (opt == null || opt.isEmpty())
					continue;

				BeanAttributOptions bean = new BeanAttributOptions();

				bean.setOptions(opt);
				bean.setAttribut(new Attribut(attribut));

				liste.add(bean);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			liste = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return liste;
	}

	@SuppressWarnings("unchecked")
	public List<OptionAttributDB> getOptionsDB(Long attributID) {
		List<OptionAttributDB> options = null;
		EntityManager manager = null;
		try {
			manager = getFactory().createEntityManager();

			String req = "SELECT O FROM OptionAttributDB O WHERE AttributID = :attributid";
			Query query = manager.createQuery(req).setParameter("attributid", attributID)
					.setMaxResults(Constantes.MAX_OPTIONS);

			options = query.getResultList();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return options;
	}

	public List<OptionAttribut> getOptions(Long attributID) {
		List<OptionAttributDB> optionsDB = getOptionsDB(attributID);
		if (optionsDB == null)
			return null;

		List<OptionAttribut> options = new ArrayList<>();

		for (OptionAttributDB option : optionsDB)
			options.add(new OptionAttribut(option));

		return options;

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getVariationEtatByOptions(BeanInsertVariation bean) {

		List<Object[]> liste = null;
		EntityManager manager = null;

		try {
			manager = getFactory().createEntityManager();
			List<String> opts = bean.getVariation().getOptions();

			String req = "SELECT V.code as varcode, COUNT(OA.optionAttributID) as existe " + "FROM OptionAttributDB OA "
					+ "JOIN VariationOptionDB VO ON VO.optionAttributID = OA.optionAttributID "
					+ "JOIN VariationDB V ON V.variationID = VO.variationID "
					+ "JOIN ProduitAdmin P ON V.produitID = P.produitID "
					+ "WHERE P.code = :prodcode AND OA.code IN (:liste) " + "GROUP BY V.code " + "ORDER BY existe DESC";

			Query query = manager.createQuery(req).setParameter("prodcode", bean.getProduitCode()).setParameter("liste",
					opts);

			liste = query.getResultList();

			if (liste == null || liste.isEmpty())
				throw new Exception("variation inexistante");

			Query attprodquery = manager.createQuery("SELECT COUNT(AT.attributID) FROM AttributDB AT "
				+ "JOIN ProduitAdmin P ON P.produitID = AT.produitID " + "WHERE P.code = :prodcode")
				.setParameter("prodcode", bean.getProduitCode());
			Long count = (Long) attprodquery.getSingleResult();
			Long nbopt = (Long) liste.get(0)[1];
			if (count > nbopt)
				throw new Exception("variation inexistante");
			
		} catch (Exception ex) {
			System.err.println("--------------" + ex.getMessage() + "---------------");
			liste = null;
		} finally {

			if (manager != null && manager.isOpen())
				manager.close();
		}

		return liste;
	}

	public boolean isVariationExistante(BeanInsertVariation bean) {
		List<Object[]> obj = getVariationEtatByOptions(bean);
		return (obj != null && !obj.isEmpty());
	}

	public Long countAttributsByProduit(String code) throws Exception {
		return countAtypeByBtype("Attribut", "Produit", code);
	}

	private Long countAtypeByBtype(String typeA, String typeB, String code) throws Exception {
		if (code == null || code.isEmpty())
			throw new Exception("code invalide");

		Long count = 0L;
		EntityManager manager = null;

		try {

			manager = getFactory().createEntityManager();
			String req = "SELECT COUNT(A." + typeA.toLowerCase() + "ID) FROM " + typeA + "DB A " + "JOIN " + typeB
					+ "DB B ON A." + typeB.toLowerCase() + "ID = B." + typeB.toLowerCase() + "ID WHERE B.code = :val";

			Query query = manager.createQuery(req).setParameter("val", code).setMaxResults(1);

			count = (Long) query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			count = 0L;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	public List<OptionAttributDB> getOptionsbyCode(List<String> options) {

		List<OptionAttributDB> optlist = null;
		EntityManager manager = null;

		try {

			manager = getFactory().createEntityManager();
			String req = "SELECT OA FROM OptionAttributDB OA WHERE OA.code IN :liste";
			Query query = manager.createQuery(req).setParameter("liste", options);

			optlist = query.getResultList();

		} catch (Exception e) {
			optlist = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}

		return optlist;
	}

	@SuppressWarnings("unchecked")
	public VariationDB insertVariation(BeanInsertVariation bean, List<OptionAttributDB> options, long produitID) {

		String varcode = null;
		VariationDB vreturn = null;
		EntityManager manager = null;

		try {

			manager = getFactory().createEntityManager();
			manager.getTransaction().begin();

			/*************** genere variation.code *****************/

			Query query = manager.createQuery("SELECT V.code FROM VariationDB V WHERE V.produitID = :prid ")
					.setParameter("prid", produitID);

			List<String> vcodes = query.getResultList();
			varcode = "V_" + bean.getProduitCode().substring(Constantes.DEBUT_ID_ATTRIBUT);

			if (vcodes == null || vcodes.isEmpty())
				varcode += "_01";
			else
				for (int varnum = 1; varnum <= vcodes.size() + 1; varnum++) {
					String str = null;
					str = varcode + (varnum < 10 ? "_0" : "_") + varnum;
					if (!vcodes.contains(str)) {
						varcode = str;
						break;
					}
				}

			/**************** insert la variation *****************/
			VariationDB vardb = bean.getVariation().getVariation();
			vardb.setCode(varcode);
			if (vardb.getImage() != null)
				vardb.setThumbnail(new Parse().imageToThumbnail(vardb.getImage()));
			vardb.setProduitID(produitID);
			manager.persist(vardb);

			manager.flush();

			/**************** insert les associations variation_option *****************/
			for (OptionAttributDB opt : options)
				manager.persist(new VariationOptionDB(vardb.getVariationID(), opt.getOptionAttributID()));

			manager.getTransaction().commit();

			vreturn = vardb;

		} catch (Exception e) {
			if (manager != null && manager.getTransaction().isActive())
				manager.getTransaction().rollback();
			e.printStackTrace();
			vreturn = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return vreturn;
	}

	public Response deleteAttribut(AttributDB attribut) {

		EntityManager manager = null;
		Response p = null;
		try {

			manager = getFactory().createEntityManager();

			manager.getTransaction().begin();

			Query q = manager.createQuery("SELECT  T FROM AttributDB T WHERE T.code = :code")
					.setParameter("code", attribut.getCode()).setMaxResults(1);

			AttributDB db = (AttributDB) q.getSingleResult();

			manager.remove(db);
			manager.getTransaction().commit();

			manager.close();
		} catch (Exception ex) {
			if (manager != null && manager.getTransaction().isActive())
				manager.getTransaction().rollback();
			System.err.println("--------- " + ex.getMessage() + " ---------");
			p = Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return p;
	}

	public Response insertAttribut(ProduitDB produit, BeanAttributOptions attributoptions) {

		try {
			AccesProduitsAdmin accesprod = new AccesProduitsAdmin();

			ProduitDB produitdb = accesprod.getByCode(produit.getCode());

			Attribut attribut = attributoptions.getAttribut();
			AttributDB attdb = insertAttribut(produitdb, attribut);
			insertOptions(attdb, attributoptions.getOptions());

			return null;
		} catch (Exception ex) {
			System.err.println("---- " + ex.getMessage() + " ---------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}

	}

	public Response updateAttribut(Attribut attribut) {

		EntityManager manager = null;
		Response p = null;
		try {
			manager = getFactory().createEntityManager();
			manager.getTransaction().begin();

			Query query = manager.createQuery("SELECT T FROM AttributDB T WHERE T.code = :code")
					.setParameter("code", attribut.getCode()).setMaxResults(1);

			AttributDB attdb = (AttributDB) query.getSingleResult();

			if (attdb == null)
				throw new Exception("attribut " + attribut.getCode() + " introuvable");

			attdb.setNom(attribut.getNom());
			attdb.setType(attribut.getType());
			attdb.setDescription(attribut.getDescription());

			manager.merge(attdb);

			manager.getTransaction().commit();

			p = Response.ok(attdb).build();
		} catch (Exception ex) {
			if (manager != null && manager.getTransaction().isActive())
				manager.getTransaction().rollback();
			System.err.println("---- " + ex.getMessage() + " ---------");
			p = Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return p;
	}

	public boolean updateOption(OptionAttributDB optiondb) {

		EntityManager manager = null;
		boolean resultat = false;
		try {

			manager = getFactory().createEntityManager();
			manager.getTransaction().begin();
			Query q = manager.createQuery("SELECT O FROM OptionAttributDB O WHERE O.code = :code").setParameter("code",
					optiondb.getCode());
			OptionAttributDB odb = (OptionAttributDB) q.getSingleResult();
			optiondb.setOptionAttributID(odb.getOptionAttributID());
			optiondb.setAttributID(odb.getAttributID());
			manager.merge(optiondb);

			manager.getTransaction().commit();

			resultat = true;
		} catch (Exception ex) {
			if (manager != null && manager.getTransaction().isActive())
				manager.getTransaction().rollback();
			resultat = false;
			ex.printStackTrace();
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}

		return resultat;
	}

	public boolean deleteOption(OptionAttributDB oldpt) {

		EntityManager manager = null;
		boolean resultat = false;
		try {
			manager = getFactory().createEntityManager();
			manager.getTransaction().begin();

			Query q = manager.createQuery("SELECT O FROM OptionAttributDB O WHERE O.code = :code")
					.setParameter("code", oldpt.getCode()).setMaxResults(1);

			OptionAttributDB opt = (OptionAttributDB) q.getSingleResult();
			manager.remove(opt);
			manager.getTransaction().commit();

			resultat = true;
		} catch (Exception ex) {
			if (manager != null && manager.getTransaction().isActive())
				manager.getTransaction().rollback();
			resultat = false;
			ex.printStackTrace();
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return resultat;
	}

	@SuppressWarnings("unchecked")
	public List<VariationDB> getVariationsDBByCode(String code) {
		List<VariationDB> varsDB = null;
		EntityManager manager = null;

		try {
			manager = getFactory().createEntityManager();

			String req = "SELECT V FROM VariationDB V "
					+ "JOIN ProduitAdmin P ON V.produitID = P.produitID WHERE P.code = :code ";
			Query query = manager.createQuery(req).setParameter("code", code);

			varsDB = (List<VariationDB>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			varsDB = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return varsDB;
	}

	@SuppressWarnings("unchecked")
	public List<VariationDB> getVariationsByOption(OptionAttributDB oldpt) {

		EntityManager manager = null;
		List<VariationDB> variations = null;

		try {
			manager = getFactory().createEntityManager();

			Query query = manager.createQuery(
					"SELECT V FROM  VariationDB V " + "JOIN VariationOptionDB VO ON V.variationID = VO.variationID "
							+ "JOIN OptionAttributDB OA ON VO.optionAttributID = OA.optionAttributID "
							+ "WHERE OA.code = :code")
					.setParameter("code", oldpt.getCode());

			variations = query.getResultList();
		} catch (Exception ex) {
			ex.printStackTrace();
			variations = null;
		} finally {
			if (manager != null && manager.isOpen())
				manager.close();
		}
		return variations;
	}

	@Override
	protected Query isInDBQuery(VariationDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(VariationDB t) throws Exception {
		// TODO Auto-generated method stub

	}

	public VariationDB delete(VariationDB t) throws Exception {
		return super.delete("code", t.getCode());
	}

	@Override
	protected void erreurExistant(VariationDB t) throws Exception {
		throw new Exception("La Variation " + t.getCode() + " existe déjà avec des informations identiques.");

	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		throw new Exception("La variation " + t + " est inexistante.");
	}

	@Override
	protected void deleteErrors(VariationDB t) throws Exception {
		if (t.getQte() > 0)
			throw new Exception("variation toujours en stock");
	}

	@Override
	public boolean isCodeValide(String code) {
		return code.matches("V_\\w{2}_\\w{4}_\\d+");
	}

	@Override
	public VariationDB getByCode(String code) {
		return getEquals("code", code);
	}

	@Override
	public String genereCode(VariationDB t) {
		String suffix = "V_" + t.getProduitCode().substring(Constantes.DEBUT_ID_ATTRIBUT);
		return new Utils().nextCode(suffix, getEqualsList("produitID", t.getProduitID()));
	}


}

