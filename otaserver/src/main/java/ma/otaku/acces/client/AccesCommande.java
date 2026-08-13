package ma.otaku.acces.client;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.acces.base.ICodeAcces;
import ma.otaku.acces.produit.AccesProduitStats;
import ma.otaku.acces.produit.AccesProduitsClient;
import ma.otaku.acces.produit.AccesVariations;
import ma.otaku.acces.triggers.commande.AfterUpdateCommande;
import ma.otaku.business.client.BeanCreerCommande;
import ma.otaku.business.client.BeanDetailsCommande;
import ma.otaku.business.client.CommandeSansUtilisateurDTO;
import ma.otaku.business.stats.ResumeCommandes;
import ma.otaku.data.client.ClientAdresseDB;
import ma.otaku.data.client.ClientDB;
import ma.otaku.data.client.ClientLoginDB;
import ma.otaku.data.client.CommandeDB;
import ma.otaku.data.client.CommandeItemDB;
import ma.otaku.data.client.CoursierDB;
import ma.otaku.data.client.PanierDB;
import ma.otaku.data.produit.ProduitClient;
import ma.otaku.data.produit.VariationDB;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.RandomString;

public class AccesCommande extends AccesTable<CommandeDB> implements ICodeAcces<CommandeDB>{

	
	public AccesCommande() {
		super(CommandeDB.class);
		// insertTriggers.add( new AfterInsertCommande());
		updateTriggers.add( new AfterUpdateCommande());
		
	}

	public AccesCommande(byte param) {
		super(CommandeDB.class, param);
	}

	@Override
	protected Query isInDBQuery(CommandeDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(CommandeDB t) throws Exception 
	{
		if(v().isNotSupZero(t.getItemCount())) throw new ValidationException("Qte commandée < 1");
		if(v().isNullOrNegatif(t.getPrixLivraison())) throw new ValidationException("Prix Livraison negatif");
		if(v().isNotSupZero(t.getPrixPieces())) throw new ValidationException("Prix pieces < 1");
		t.setCode(genereCode(t));
		/* Validés dans adresse : Nom, Prenom, Adresse1, Ville, Telephone1 */
	}

	@Override
	public String genereCode(CommandeDB t) {
		RandomString rand = new RandomString(6);
		String str = null;
		CommandeDB existe = null;
		do { try { str = rand.nextString(); existe = getByCode(str); } catch(Exception ex) { existe = null;}} 
		while( existe != null);
		return str;
	}

	@Override
	public CommandeDB getByCode(String code) throws Exception {
		return getEquals("code", code);
	}

	@Override
	public boolean isCodeValide(String code) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void erreurExistant(CommandeDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(CommandeDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * passer une commande sans utilisateur connecté ( fournir l'adresse uniquement )
	 * @param cmd
	 * @return
	 * @throws Exception 
	 */
	
	public BeanDetailsCommande passerCommande(CommandeSansUtilisateurDTO cmd) throws Exception {
		ClientLoginDB client = new AccesClientLogin().getEquals("email", "default@otaku.ma");
		if( client == null )
			throw new NotFoundException("Default user for non registered orders not found");
		
		return genericCreerCommande(cmd.getItems(),cmd.getPaiement(),cmd.getLivraison(), client.getClientID() , cmd.getAdresse());
	}
	
	/**
		 
	 * passer une commande avec un utilisateur connecté
	 * @param cmd
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	public BeanDetailsCommande passerCommande(BeanCreerCommande cmd, Long clientID) throws Exception{

		List<ClientAdresseDB> adresses = new AccesClientAdresse()
				.dynamiqueSelect("clientID =", clientID, "etat like", "SELECTED");
		
		if(adresses == null || adresses.isEmpty()) throw new NotFoundException("Adresse introuvable");
		
		ClientAdresseDB adresse  = adresses.get(0);
		
		return genericCreerCommande(cmd.getItems(),cmd.getPaiement(),cmd.getLivraison(), clientID, adresse);
	}

	
	/**
	 * 
	 * @param items
	 * @param paiement
	 * @param liv
	 * @param clientID
	 * @param adresse
	 * @return
	 * @throws Exception
	 */
	private BeanDetailsCommande genericCreerCommande( List<PanierDB> items, String paiement, String liv, Long clientID, ClientAdresseDB adresse)
			throws Exception {

		/* -----------------------------------------------------------------------------
		 * --   validation des informations necessaires pour la livraison.	
		 * -----------------------------------------------------------------------------*/
		ClientAdresseDB addr = validateLivraisonInfos(liv, adresse);
		
		/* -----------------------------------------------------------------------------
		 * --   validation du coursier selectionné.	
		 * -----------------------------------------------------------------------------*/
		String crs = liv.split("_")[0];
		CoursierDB coursier = new AccesCoursier().getEquals("coursier", crs);
		if(coursier == null) throw new NotFoundException("Coursier introuvable");
		
		/* -----------------------------------------------------------------------------
		 * --   validation des items de la commande.	
		 * -----------------------------------------------------------------------------*/
		AccesProduitsClient accesprod  = new AccesProduitsClient();
		AccesVariations accesvar = new AccesVariations();
		
		List<CommandeItemDB> cmditems = new ArrayList<>();
		
		CommandeItemDB cmditem = null;
		double prixtotal = 0;
		long   itemcount = 0;
		
		for(PanierDB p : items) {
			
			if(p.getCode().startsWith("V_")) 
				cmditem = prepareVariationItem(accesprod, accesvar, p);
			else 
				cmditem = prepareProduitItem(accesprod, p);
			
			itemcount += cmditem.getQte();
			prixtotal += cmditem.getPrixUnite() * cmditem.getQte();
			cmditems.add(cmditem);
		}

		/* -----------------------------------------------------------------------------
		 * --   calcul du prix de la livraison.	
		 * -----------------------------------------------------------------------------*/
		String methodeliv = liv.split("_")[1];
		
		Double prixLivraison = getPrixLivraison(paiement, coursier, prixtotal, methodeliv);
		Double coutLivraison = getCoutLivraison(paiement, coursier, methodeliv);
			
		String livraison = coursier.getCoursier() + ( methodeliv.equals(Constantes.LIV_LD) ? " livraison à domicile." : "point de relais.") ;
		livraison +=  paiement.equals(Constantes.LIV_VB) ? " ( Virement banquaire )" : " ( Paiement à la livraison )";	

		/* -----------------------------------------------------------------------------
		 * --   création de la commande.	
		 * -----------------------------------------------------------------------------*/
		
		CommandeDB commande = new CommandeDB();
		commande.setClientID(clientID);
		commande.setItemCount(itemcount);
		commande.setPrixPieces(prixtotal);
		
		
		commande.setPrixLivraison(prixLivraison);
		commande.setCoutLivraison(coutLivraison);
		
		commande.setDateCommande(utils().now());
		commande.setNom(addr.getNom());
		commande.setPrenom(addr.getPrenom());
		commande.setAdresse1(addr.getAdresse1());
		commande.setVille(addr.getVille());
		commande.setTelephone1(addr.getTelephone1());
		commande.setCodePostal(addr.getCodePostal());
		commande.setLivraison(livraison);
		CommandeDB newcmd = insert(commande);
		/* -----------------------------------------------------------------------------
		 * --   Ajout des objets selectionnés dans la commande
		 * --   et suppréssion des objets ajoutés du panier.	
		 * -----------------------------------------------------------------------------*/
		AccesCommandeItem accesitem = new AccesCommandeItem();
		AccesPanier accespanier = new AccesPanier();
		for(CommandeItemDB it : cmditems) 
		{
			it.setCommandeID(newcmd.getCommandeID());
			accesitem.insert(it);
			accespanier.delete(it.getCode(), clientID);
		}
		
		new AccesProduitStats().addCommandeStats(newcmd);
		
		BeanDetailsCommande bean = new BeanDetailsCommande();

		bean.setCommande(commande);
		bean.setItems(cmditems);

		return bean;
	}

	
	
	
	/**
	 * 
	 * @param livraison
	 * @param addr
	 * @return
	 */
	private ClientAdresseDB validateLivraisonInfos(String livraison, ClientAdresseDB addr)
		throws NotFoundException, ValidationException {
		
		if( addr == null )
		 throw new NotFoundException("Adresse Introuvable");
		
		if( v().isNullOrEmpty(addr.getNom())  || v().isNullOrEmpty(addr.getPrenom()) ||
		v().isNullOrEmpty(addr.getAdresse1()) || v().isNullOrEmpty(addr.getTelephone1()) ||
		v().isNullOrEmpty(addr.getVille())    || v().isNullOrEmpty(addr.getCodePostal()) ) 
			throw new ValidationException("Adresse incomplete");
		
		if(v().isNullOrEmpty(livraison) || v().isNullOrEmpty(livraison.split("_")))
			throw new ValidationException("Méthode de livraison invalide");
		return addr;
	}
	
	
	
/**
 * 
 * @param accesprod
 * @param accesvar
 * @param p
 * @return
 * @throws NotAcceptableException
 * @throws NotFoundException
 * @throws Exception
 */
	private CommandeItemDB prepareVariationItem(AccesProduitsClient accesprod, AccesVariations accesvar, PanierDB p) 
			throws NotAcceptableException, NotFoundException, Exception {
		
		CommandeItemDB cmditem;
		VariationDB v = accesvar.getEquals("code", p.getCode());
		if( v == null ) throw new NotFoundException("Variation "+p.getCode()+" introuvable");
		if( v.getQte() < p.getQte()) throw new NotAcceptableException(v.getCode()+": La quantité commandée "+p.getQte()+" n'est pas disponible");
		
		ProduitClient prod = accesprod.getByID(v.getProduitID());
		
		cmditem = new CommandeItemDB();
		cmditem.setVariationID(v.getVariationID());
		cmditem.setNom(prod.getNom() + " - " +v.getNom());
		cmditem.setCode(v.getCode());
		cmditem.setThumbnail(v.getThumbnail());
		cmditem.setQte(p.getQte());

		if(prod.enpromo()) 
			cmditem.setPrixUnite(prod.getPrixPromo());
		else 
			cmditem.setPrixUnite(prod.getPrixUnite());
		return cmditem;
	}
/**
 * 
 * @param accesprod
 * @param p
 * @return
 * @throws NotAcceptableException
 * @throws NotFoundException
 * @throws Exception
 */
	private CommandeItemDB prepareProduitItem(AccesProduitsClient accesprod, PanierDB p) throws NotAcceptableException, NotFoundException, Exception {
		CommandeItemDB cmditem;
		ProduitClient v = accesprod.getEquals("code", p.getCode());
		if( v == null ) throw new NotFoundException("Produit "+p.getCode()+" introuvable");
		if( v.getQte() < p.getQte()) throw new NotAcceptableException(v.getCode()+": La quantité commandée "+p.getQte()+" n'est pas disponible");

		cmditem = new CommandeItemDB();
		cmditem.setVariationID(v.getProduitID());
		cmditem.setNom(v.getNom());
		cmditem.setCode(v.getCode());
		cmditem.setThumbnail(v.getThumbnail());
		cmditem.setQte(p.getQte());
		if(v.enpromo()) cmditem.setPrixUnite(v.getPrixPromo());
		else cmditem.setPrixUnite(v.getPrixUnite());
		return cmditem;
	}
/**
 * 
 * @param cmd
 * @param coursier
 * @param methodeliv
 * @return
 */
	private Double getCoutLivraison(String paiement, CoursierDB coursier, String methodeliv) {
		return methodeliv.equals(Constantes.LIV_LD) && paiement.equals(Constantes.LIV_VB) ? coursier.getCoutVB_LD() :
		methodeliv.equals(Constantes.LIV_PR) && paiement.equals(Constantes.LIV_VB) ? coursier.getCoutVB_PR() :
		methodeliv.equals(Constantes.LIV_LD) && paiement.equals(Constantes.LIV_PAL)? coursier.getCoutPL_LD() : 
		coursier.getCoutPL_PR();
	}
/**
 * 
 * @param cmd
 * @param coursier
 * @param prixtotal
 * @param methodeliv
 * @return
 */
	private double getPrixLivraison(String paiement, CoursierDB coursier, double prixtotal, String methodeliv) {
		return prixtotal >= coursier.getSeuilPrixLivraisonGratuite() ? 0 :
			methodeliv.equals(Constantes.LIV_LD) && paiement.equals(Constantes.LIV_VB) ? coursier.getPrixVB_LD() :
			methodeliv.equals(Constantes.LIV_PR) && paiement.equals(Constantes.LIV_VB) ? coursier.getPrixVB_PR() :
			methodeliv.equals(Constantes.LIV_LD) && paiement.equals(Constantes.LIV_PAL)? coursier.getPrixPL_LD() : 
			coursier.getPrixPL_PR();
	}
/**
 * 
 * @param clientid
 * @param option
 * @param code
 * @param page
 * @param size
 * @return
 * @throws Exception
 */

	public List<BeanDetailsCommande> getCommandesClient(Long clientid,String option, String code, Integer page, Integer size) throws Exception {
		Set<Long> s = new HashSet<>();
		s.add(clientid);
		return getCommandes("client", s, option, code, page, size);
	}
	/**
	 * 
	 * @param email
	 * @param option
	 * @param code
	 * @param page
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<BeanDetailsCommande> getCommandesAdmin(String email, String option, String code, Integer page, Integer size) throws Exception 
	{	
		Set<Long> clientids = null;
		if(!v().isNullOrEmpty(email)) {

			List<ClientDB> clients = new AccesClient( AccesClient.READONLY).getLike("email", "%"+email.trim()+"%"); 

			if(v().isNullOrEmpty(clients)) return null;
			 clientids = new HashSet<>();
			for(ClientDB c : clients) clientids.add(c.getClientID());
		}
		return getCommandes("admin", clientids, option, code, page, size);
		
	}
	/**
	 * 
	 * @param type
	 * @param clientid
	 * @param option
	 * @param code
	 * @param page
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<BeanDetailsCommande> getCommandes(String type, Set<Long> clientid,String option, String code, Integer page, Integer size) throws Exception {
		
		if(v().isNotSupZero(page)) page = 1;
		if(v().isNotSupZero(size)) size = 100;

		EntityManager manager = null; 
		List<CommandeDB> cmds = null;
		try {
			manager = getFactory().createEntityManager();
			
			CriteriaBuilder builder = manager.getCriteriaBuilder();
			CriteriaQuery<CommandeDB> criteria = builder.createQuery(CommandeDB.class);
			Root<CommandeDB> root = criteria.from(CommandeDB.class);
			criteria.select(root);

			HashSet<Predicate> pr = new HashSet<>();
			
			if(!v().isNullOrEmpty(code)) likePredicate("code", "%"+code+"%", builder, root, pr);
			if( clientid != null ) {
				pr.add(root.get("clientID").in(clientid));
			}
			
			if(!v().isNullOrEmpty(option) ) 
			{
				if(option.equals("attente denvoi")) {
					pr.add(builder.isNull(root.get("dateEnvoi")));
					pr.add(builder.isNull(root.get("dateFin")));
				}
				
				if(option.equals("attente livraison")) {
					pr.add(builder.isNull(root.get("dateFin")));
					pr.add(builder.isNotNull(root.get("dateEnvoi")));
				}

				/* ************* admin ************* */
				if(v().isEqual(type, "admin")) adminPredicates(option, builder, root, pr);
			}
			
			
			criteria.where(builder.and(pr.toArray(new Predicate[pr.size()])) );
			criteria.orderBy(builder.desc(root.get("commandeID")));

			Query query = manager.createQuery(criteria).setFirstResult((page - 1) * size).setMaxResults(size);
			cmds  = ((List<CommandeDB>) query.getResultList());
			
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if(manager!= null && manager.isOpen()) manager.close();
		}
		
		if(cmds == null || cmds.isEmpty()) return null; 
		
		AccesCommandeItem acces = new AccesCommandeItem();
		List<BeanDetailsCommande> commandes = new ArrayList<>();
		
		for( CommandeDB c : cmds )
		{
			List<CommandeItemDB> items = acces.getEqualsList("commandeID", c.getCommandeID());
			if(items == null || items.isEmpty()) 
				throw new Exception("Commande " + c.getCode() + " sans produits (critique)");
			
			BeanDetailsCommande b = new BeanDetailsCommande();
			b.setCommande(c);
			b.setItems(items); 
			
			commandes.add(b);
		}
		return commandes;
	}
/**
 * 
 * @param option
 * @param builder
 * @param root
 * @param pr
 */
	private void adminPredicates(String option, CriteriaBuilder builder, Root<CommandeDB> root, HashSet<Predicate> pr) {
		
		String[] et = option.split("_");
		Set<String> etats = new HashSet<String>(Arrays.asList(et));
		
		Iterator<String> it = etats.iterator();
		
		while(it.hasNext()) if(!v().isCommandeEtat(it.next())) it.remove();
		pr.add(root.get("etat").in(etats));
	}
/**
 * 
 * @param code
 * @return
 * @throws Exception
 */
	public CommandeDB annuler(String code) throws Exception{
		CommandeDB c = getEquals("code", code);
		if(c == null) throw new NotFoundException("Commande introuvable");
		if(c.getDateEnvoi() != null ) throw new Exception("Commande déjà envoyee ne peut être annulée");
		c.setEtat("ANNULEE");
		c.setDateFin(utils().now());
		return update(c);
	}
/**
 * 
 * @param ls
 * @param etat
 * @return
 * @throws Exception
 */
	public List<CommandeDB> nextEtat(List<List<String>> ls, String etat) throws Exception{

		List<CommandeDB> cmds = null;
		
		HashMap<String, String> codes = new HashMap<>();
		
		for(List<String> cd : ls) codes.put(cd.get(0),cd.get(1));

		/* ************ refus des commandes ************ */
		if(!etat.equals(CommandeDB.REFUSEE))
		{
			cmds = dynamiqueSelect("code in ", codes.keySet(), "etat = ", etat);
			
			cmds.forEach( c -> { 
				CommandeDB cmd = NextCommandeEtat(c, etat);
				// System.out.println(codes.get("progress " + cmd.getCode()+ " " + v().isTextValide(codes.get(cmd.getCode()))) );
				if(v().isTextValide(codes.get(cmd.getCode()))) cmd.setNotes(codes.get(cmd.getCode()));
				if(cmd != null) try { update(cmd); } catch(Exception e) { e.printStackTrace(); }
			});
		}
		/* ***************** next etat **************** */
		else {
			List<String> etatsExclus = new ArrayList<>();
			etatsExclus.add(CommandeDB.REFUSEE);
			etatsExclus.add(CommandeDB.ANNULEE);
			etatsExclus.add(CommandeDB.COMPLETE);

			cmds = dynamiqueSelect("code in ", codes.keySet(), "etat not in ", etatsExclus);
			
			cmds.forEach( c -> { 
				c.setEtat(CommandeDB.REFUSEE);
				c.setDateFin(utils().now());
				// System.out.println(codes.get("refusee " + c.getCode()+ " " + v().isTextValide(codes.get(c.getCode()))) );
				if(v().isTextValide(codes.get(c.getCode()))) c.setNotes(codes.get(c.getCode()));
				try { update(c); } catch(Exception e) { e.printStackTrace(); }
			});
		}
		
		return cmds;
	}
/**
 * 
 * @param cmd
 * @param etat
 * @return
 */
	private CommandeDB NextCommandeEtat(CommandeDB cmd, String etat) {
		if(v().isCommandeEtat(etat))
		{
			if(etat.equals(CommandeDB.ACCEPTATION)) 
			{
				cmd.setEtat(CommandeDB.PREPARATION);
				cmd.setDateAccepte(utils().now());
				return cmd;
			}
			
			else if(etat.equals(CommandeDB.PREPARATION)) 
			{
				cmd.setEtat(CommandeDB.ENVOI);
				cmd.setDatePrepare(utils().now());
				return cmd;
			}
			else if(etat.equals(CommandeDB.ENVOI)) 
			{
				cmd.setEtat(CommandeDB.ENVOYEE);
				cmd.setDateEnvoi(utils().now());
				return cmd;
			}
			else if(etat.equals(CommandeDB.ENVOYEE))
			{
				cmd.setEtat(CommandeDB.COMPLETE);
				cmd.setDateFin(utils().now());
				return cmd;
			}
		}
		return null;
	}
	public ResumeCommandes resume()
	{
		final ResumeCommandes resume = new ResumeCommandes();
		EntityManager manager = null;
		try 
		{

			String req = "SELECT c.etat, COALESCE(count(c.commandeID),0) " + 
			"FROM commande c " + 
			"WHERE c.etat in ('"+CommandeDB.ACCEPTATION+"','"+CommandeDB.PREPARATION+"',"
				+ "'"+CommandeDB.ENVOI+"','"+CommandeDB.ENVOYEE+"') " + 
			"GROUP BY c.etat";
			
			manager = getFactory().createEntityManager();
			Query q = manager.createNativeQuery(req);

			@SuppressWarnings("unchecked")
			List<Object[]> resultat  = q.getResultList();
			
			if(!v().isNullOrEmpty(resultat))
				resultat.forEach(r -> {
					BigInteger bt = (BigInteger) r[1];
					if(r[0].equals(CommandeDB.ACCEPTATION)) resume.setPendingAcceptation( bt.longValue() );
					else if(r[0].equals(CommandeDB.PREPARATION)) resume.setPendingPreparation( bt.longValue() );
					else if(r[0].equals(CommandeDB.ENVOI)) resume.setPendingEnvoi( bt.longValue() );
					else if(r[0].equals(CommandeDB.ENVOYEE)) resume.setPendingLivraison( bt.longValue() );
				});
		}
		catch(Exception ex) {  ex.printStackTrace(); }
		finally { if(manager != null && manager.isOpen()) manager.close(); }
		
		return resume;
	}

	public List<CommandeDB> confirmerPaiement(List<String> codes) {
		if(v().isNullOrEmpty(codes))
			return null;
		List<CommandeDB> cmds = getList("code", codes, "in"); // dynamiqueSelect("code in ", codes);
		if(!v().isNullOrEmpty(codes))
			cmds.forEach(c -> { 
				c.setPaye(true); 
				try { update(c); } catch(Exception e) {}
			});
		return cmds;
	}

}











