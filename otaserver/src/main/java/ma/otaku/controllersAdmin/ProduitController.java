package ma.otaku.controllersAdmin;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ma.otaku.acces.produit.AccesProduits;
import ma.otaku.acces.produit.AccesProduitsAdmin;
import ma.otaku.acces.produit.AccesVariations;
import ma.otaku.business.produits.BeanAttributOptions;
import ma.otaku.business.produits.BeanAttributOptionsDB;
import ma.otaku.business.produits.BeanInsertVariation;
import ma.otaku.business.produits.BeanProduitAttributs;
import ma.otaku.business.produits.BeanVariationOptions;
import ma.otaku.business.produits.OptionAttribut;
import ma.otaku.data.produit.AttributDB;
import ma.otaku.data.produit.OptionAttributDB;
import ma.otaku.data.produit.ProduitAdmin;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.produit.VariationDB;
import ma.otaku.utils.Constantes;
import ma.otaku.utils.Parse;
import ma.otaku.utils.Utils;

@Path("/produits")
public class ProduitController 
{
	
	/* ********************************* GET *************************************/

	@GET
	@Path("setpromo")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_ALL")
	public ProduitAdmin setPromo(@QueryParam("code") String code,
			@QueryParam("datedebut") String datedebut,
			@QueryParam("datefin") String datefin) throws Exception{

		return new AccesProduitsAdmin().setPromo(code, datedebut, datefin);
	}
	
	@GET
	@Path("getall")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_ALL")
	public List<ProduitAdmin> getall(@QueryParam("hasvar") Boolean hasvar) throws Exception
	{
		if(hasvar != null && hasvar == false)
			return new AccesProduitsAdmin().selectWhere("hasVariations = false");
		return new AccesProduitsAdmin().getAll();
	}
	
	@GET
	@Path("{texte}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_SELECT")
	public ProduitAdmin getProduit(@PathParam("texte") String url) throws NamingException
	{
		String nom = new Parse().URLToNomProduit(url);
		return new AccesProduitsAdmin().getProduitByNom(nom);
	}
	
	
	@GET
	@Path("/list")
	@RolesAllowed("PROD_SELECT")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListe(@QueryParam( "nom" )String nom , @QueryParam("typeprix") String typeprix, 
		@QueryParam("ispromo")   String typepromo  , @QueryParam("trifiltre") String trifiltre,
		@QueryParam("prixmin")   Double prixmin    , @QueryParam("prixmax")   Double prixmax, 
		@QueryParam("qtemin")    Integer qtemin    , @QueryParam("qtemax")    Integer qtemax, 
		@QueryParam("minreduc")  Integer minreduc  , @QueryParam("maxreduc")  Integer maxreduc, 
		@QueryParam("page")      Integer page      , @QueryParam("size")      Integer size, 
		@QueryParam("categorie") String categorie  , @QueryParam("theme")     String theme) {
		try {
			
			nom = nom == null || nom.isEmpty() ? null : nom.trim();
			typeprix = typeprix == null || typeprix.isEmpty() ? null : typeprix;
	
			prixmin = prixmin == null || prixmin <= Constantes.MIN_PRIX  ? null : prixmin;
			prixmax = prixmax == null || (prixmax >= Constantes.MAX_PRIX || prixmax < Constantes.MIN_PRIX ) ? null : prixmax;
			
			qtemin = qtemin == null || qtemin <= Constantes.MIN_QTE_ADMIN  ? null : qtemin;
			qtemax = qtemax == null ||  qtemax < Constantes.MIN_QTE_ADMIN ? null : qtemax;
			
			minreduc = minreduc == null ||  minreduc <= Constantes.MIN_REDUCTION  ? null : minreduc;
			maxreduc = maxreduc == null ||  maxreduc >= Constantes.MAX_REDUCTION  ? null : maxreduc;
			
			page = page == null || page < Constantes.MIN_PAGE ? Constantes.DEFAULT_PAGE : page;
			size = size == null || size < Constantes.MIN_LIST_COUNT ? Constantes.MIN_LIST_COUNT :
				size > Constantes.MAX_LIST_COUNT ?Constantes.MAX_LIST_COUNT: size;
			AccesProduitsAdmin accesProduit = new AccesProduitsAdmin(AccesProduits.READONLY);
			List<ProduitAdmin> ls = accesProduit.getListeProduits(nom, typeprix, typepromo, trifiltre, prixmin, prixmax, 
							qtemin, qtemax, minreduc, maxreduc, page, size, categorie, theme, false);
			return Response.ok(ls).build();
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("-------- "+e.getMessage()+" --------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
		}
	}
	/*
	class A { 
		prop1;
		prop2; hide
		prop3;
	}
	class B extends A{ 
	
		prop1; hide
		prop3;
	}
	class C extends A { 
	
		prop1;
		prop3; hide
	}
	
	*/
	@GET
	@Path("/list_titres")
	@RolesAllowed("PROD_SELECT")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getListeRechercheTitres(@QueryParam( "nom" )String nom , @QueryParam("size") Integer size ) throws NamingException
	{
		if(nom == null || nom.isEmpty())
			return null;
		if(size == null || size < Constantes.MIN_LIST_COUNT )
			size = Constantes.MIN_LIST_COUNT;
		AccesProduitsAdmin accesProduit = new AccesProduitsAdmin();
		List<String> produits = accesProduit.getListeTitres(nom,size);
		return produits;
	}

	
	@GET
	@Path("/variations/{text}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_SELECT")
	public List<BeanVariationOptions> getProduitVariations(@PathParam("text") String code)
	{
		AccesVariations accesVariations = new AccesVariations();
		List<VariationDB> variationsdb = accesVariations.getVariationsDBByProduitCode(code);
		
		return accesVariations.getListeVariationOptions(variationsdb);
	}
	
	
	@GET
	@Path("/Attributs/{text}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_SELECT")
	public List<BeanAttributOptions> getProduitAttributs(@PathParam("text") String url)
	{
		String nom = new Parse().URLToNomProduit(url);

		AccesVariations accesVariations = new AccesVariations();
		
		return accesVariations.getAttributsOptions(nom);
	}
	
	
	/* ***************************************INSERT **************************************** */
	
	/********************INSERT VARIATION *********************/
	
	@POST
	@Path("/insertVariation")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_INSERT")
	public Response insertVariation(BeanInsertVariation bean) throws Exception //BeanInsertVariation
	{
		/************** Verifie les données recus *************/
		try {
		if(bean == null) 
			throw new Exception("requete invalide");
		
		if(bean.getProduitCode() == null || bean.getProduitCode().isEmpty()) 
			throw new Exception("Données produit incomplets");
		if(bean.getVariation() == null || bean.getVariation().getVariation() == null)
			throw new Exception("Données variation incomplets");
		if( bean.getVariation().getOptions() == null || bean.getVariation().getOptions().isEmpty())
			throw new Exception("Données options incomplets");
		
		AccesVariations accesVariation = new AccesVariations();
		AccesProduitsAdmin accesProduit = new AccesProduitsAdmin();

		/**REQ********* Verifie si le produit existe dans la BDD et qu'il supporte des variations ************/

		ProduitAdmin prod = accesProduit.getByCode(bean.getProduitCode());

		if(!accesProduit.isProduitHasVariations(prod))
			throw new Exception("Le produit "+bean.getProduitCode()+" ne supporte pas de variations");
		
		List<String> options = bean.getVariation().getOptions();

		Utils utils = new Utils();
		
		if(options.size() > 1)
		{
			int occurences = 0;
			/**************** verifie que  les options ont des attributs differents*****************/
			occurences = utils.occurences(options, 0 , 10);
			if( occurences > 0 ) throw new Exception("plus qu'une option appartient au meme attribut");
		}
		
		/**************** verifie si les options apartiennent au meme produit*****************/
		String prcmp = prod.getCode().substring(6);
		for(String opt : options)
		{
			if(!opt.substring(0,7).equals(prcmp))
				throw new Exception("Une des options n'appartient pas au produit "+opt.substring(0,7)+" != "+prcmp);
		}
		
		/**REQ************** verifie le nombre d'attributs necessaire*****************/
		Long nbattributs = accesVariation.countAttributsByProduit(prod.getCode());
		if(nbattributs != options.size())
			throw new Exception("Nombre d'options insuffisant ");

		
		/**REQ******** select et verifie si toutes les options existent ********/
		List<OptionAttributDB> optionliste = accesVariation.getOptionsbyCode(options);

		if(optionliste == null || optionliste.isEmpty() || optionliste.size()< options.size())
			throw new Exception("options non valides");

		/**REQ******** verifie si la variation n'existe pas dans la BDD ********/
		if(accesVariation.isVariationExistante(bean))
		{
			System.err.println(bean.getVariation().getOptions().get(0)+" existe");
			throw new Exception("cette Variation existe déjà.");
		}
		
		VariationDB vreturn = accesVariation.insertVariation(bean,optionliste,prod.getProduitID());
		bean.getVariation().setVariation(vreturn);
		
		System.err.println("-------- Variation "+vreturn.getCode()+" inserée --------");
		
		return Response.ok(bean).build();
	
		}catch (Exception e) {
			System.err.println("-------- "+e.getMessage()+" --------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
		}
		
	}
	
	/********************** INSERT PRODUIT *******************/
	
	@POST
	@Path("insert")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_INSERT")
	public Response insert(BeanProduitAttributs bean)throws Exception //BeanProduitAttributs
	{
		try {
			
			
			if(bean == null) throw new Exception("Données invalides");
	
			ProduitAdmin produit = (ProduitAdmin) bean.getProduit();
			
			if( bean.getAttributs() != null && bean.getAttributs().size() > 0)
			{
				if(bean.getAttributs().size() > Constantes.MAX_ATTRIBUTS )throw new Exception("Attributs > "+Constantes.MAX_ATTRIBUTS);
					
				for(BeanAttributOptions options: bean.getAttributs())
					if(options.getOptions().size() > Constantes.MAX_OPTIONS) 
						throw new Exception("Options > "+Constantes.MAX_OPTIONS+" dans "+options.getAttribut().getNom());
				
				produit.setHasVariations(true);
			}
			else produit.setHasVariations(false);
			
			AccesProduitsAdmin accesProduit = new AccesProduitsAdmin();
			ProduitDB produitResultat = accesProduit.insert(produit);
			
			if(produitResultat == null)throw new Exception("Erreur d'insertion du produit.");

			int it = 0;
			
			if( produit.getHasVariations() ) {
				
				if(produitResultat.getProduitID() == null)
					produitResultat = accesProduit.getByCode(produitResultat.getCode());
				
				/* TODO REMOVE CETTE PARTIE => LA PLACER DANS AccesAttribut,AccesOption extends AccesTable... */
				AccesVariations accesVariation = new AccesVariations();
				for(BeanAttributOptions attributopts : bean.getAttributs())
				{
					it++;
					String str = "_"+(it < 10 ? "0":"")+it;
					
					attributopts.getAttribut().setCode(produitResultat.getCode().substring(Constantes.DEBUT_ID_ATTRIBUT)+str);
					
					AttributDB attributResultat = accesVariation.insertAttribut(produitResultat, attributopts.getAttribut());
	
					if(attributResultat == null)throw new Exception("l'attribut "+attributopts.getAttribut().getNom()+" n'as pas pu être inseré");
					accesVariation.insertOptions(attributResultat, attributopts.getOptions());
				}
			}
			
			System.err.println("------- Produit "+produitResultat.getCode()+" Inseré avec "+it+" attributs ---------");
			bean.setProduit(produit);
			
			return Response.ok(bean).build();
		}
		catch(Exception ex) {
			
			System.err.println("-------- res : "+ex.getMessage()+" --------");
			ex.printStackTrace();
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}
	}

	
	/* ******************************************** delete *************************************************/
	
	/********************** DELETE VARIATION *******************/
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("deleteVariation")
	@RolesAllowed("PROD_DELETE")
	public Response deleteVariation( VariationDB variation)
	{
		try 
		{
			return Response.ok(new AccesVariations().delete(variation)).build();
		}
		catch(Exception ex)
		{
			System.err.println("---- "+ex.getMessage()+" ---------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}
	}

	/********************** DELETE PRODUIT *******************/
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("deleteProduit")
	@RolesAllowed("PROD_DELETE")
	public Response deleteProduit(ProduitAdmin produit)
	{
		try
		{
			AccesProduitsAdmin accesproduit = new AccesProduitsAdmin();
			
			return Response.ok(accesproduit.delete(produit)).build();
		}
		catch(Exception ex)
		{
			System.err.println("---- "+ex.getMessage()+" ---------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}
	}
	
	
	/* ******************************************** Update *************************************************/

	
	@GET
	@Path("/isactivechange")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("PROD_UPDATE")
	public ProduitDB isActiveChange(@QueryParam("prodcode") String code, @QueryParam("isenabled") Boolean isenable) throws Exception
	{
		return new AccesProduitsAdmin().isActiveChange(code, isenable);
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateProduit2")
	@RolesAllowed("PROD_UPDATE")
	public Response updateProduit2(BeanProduitAttributs bean)
	{
		try 
		{
			ProduitAdmin produit = (ProduitAdmin) bean.getProduit();
			
			if( produit == null ) throw new Exception("Donnees Insuffisantes");
			
			AccesProduitsAdmin accesProduit = new AccesProduitsAdmin();
			AccesVariations accesVariation = new AccesVariations();
			
			List<BeanAttributOptions> new_attrs = bean.getAttributs();
			List<BeanAttributOptionsDB> old_attrsDB = accesVariation.getAttributsOptionsDBByCode(produit.getCode());
			Utils utils = new Utils();
			
			if(old_attrsDB != null && !old_attrsDB.isEmpty())
			{
				/******** cas de suppression de tout les attributs **********/
				if(new_attrs == null || new_attrs.isEmpty())
				{
					if(produit.getQte() > 0 ) 
						throw new Exception("Les attributs ne peuvent être supprimés si le produit est déjà en stock");
						
					List<VariationDB> varsdb =  accesVariation.getVariationsDBByCode(produit.getCode());
					
					if(varsdb != null && !varsdb.isEmpty())
						for(VariationDB var : varsdb)
						{
							Response response = Response.ok(accesVariation.delete(var)).build();
							if(response.getStatus() != 200) return response;
						}
					
					for(BeanAttributOptionsDB attops : old_attrsDB)
						accesVariation.deleteAttribut(attops.getAttribut());
					
					produit.setHasVariations(false);

				}
				/******** cas de modifications/ajouts/suppressions **********/
				else
				{
					
					List<VariationDB> varsdb =  accesVariation.getVariationsDBByCode(produit.getCode());
					
					for(int ind = 0 ; ind < old_attrsDB.size() ; ind++)
					{
						BeanAttributOptionsDB oldb = old_attrsDB.get(ind);

						/** l'attribut identique nexiste pas dans la nouvelle liste **/
						/******* ( s'il sont identiques, on ne change rien ) *********/
						if(!new_attrs.contains(new BeanAttributOptions(oldb)))
						{
							boolean updated = false;
							
							/******** on itere sur les nouveau attributs ********/
							for(BeanAttributOptions newb : new_attrs)
							{
								/** si le code est identique, on procede à la modification **/
								if(oldb.getAttribut().getCode().equals(newb.getAttribut().getCode()))
								{
									/********** on modifie l'attribut **********/
									accesVariation.updateAttribut(newb.getAttribut());

									/********** on verifie les options **********/
									compareOptions(accesVariation, oldb, newb, utils);

									/** on garde l'attribut traité pour le retirer **/
									updated = true;
									break;
								}
							}
							if(!updated)
							{
								if(produit.getQte() > 0 ) 
									throw new Exception("Les attributs ne peuvent être supprimés si le produit est déjà en stock");
								

								if(varsdb != null && !varsdb.isEmpty())
								{	for(VariationDB var : varsdb)
										accesVariation.delete(var);
									varsdb = null;
								}
								accesVariation.deleteAttribut(oldb.getAttribut());
							}
						}
					}
					/** insertion des attributs qui restent **/
					for(BeanAttributOptions attops : new_attrs)
					{
						if(attops.getAttribut().getCode() == null)
						{
							if(produit.getQte() > 0 ) 
								throw new Exception("Les attributs ne peuvent être ajoutés si le produit est déjà en stock");
							
							if(varsdb != null && !varsdb.isEmpty())
							{	Response response;
								for(VariationDB var : varsdb)
								{
									response = Response.ok(accesVariation.delete(var)).build();
									if(response.getStatus() != 200) return response;
								}
								varsdb = null;
							}
							
							attops.getAttribut().setCode(utils.genereAttributCode(bean));
							accesVariation.insertAttribut(produit, attops);
						}
					}
					produit.setHasVariations(true);
				}
			}
			else if( new_attrs != null & !new_attrs.isEmpty() )
			{
				int it = 0;
				for(BeanAttributOptions attops : new_attrs)
				{
					it++;
					String str = "_"+(it < 10 ? "0":"")+it;
					
					attops.getAttribut().setCode(produit.getCode().substring(Constantes.DEBUT_ID_ATTRIBUT)+str);
					accesVariation.insertAttribut(produit, attops);
				}
				produit.setHasVariations(true);
			}

			accesProduit.updateProduit(produit);	
			return Response.ok(produit).build();
		}
		catch (Exception ex)
		{
			System.err.println("------- "+ex.getMessage()+" ---------");
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}
	}

	private void compareOptions(AccesVariations accesVariation, BeanAttributOptionsDB oldb, BeanAttributOptions newb, Utils utils) throws Exception {
		/********** iteration sur les anciennes options à la bd **********/
		for(OptionAttributDB oldpt : oldb.getOptions())
		{
			if(!newb.getOptions().contains(new OptionAttribut(oldpt)))
			{
				boolean foundpt = false;
				
				for(OptionAttribut newpt : newb.getOptions())
				{
					/***** nouvelle option *****/ 
					if(newpt.getCode() == null)
					{
						OptionAttributDB optiondb = new OptionAttributDB(newpt);
						optiondb .setCode(utils.genereOptionCode(newb));
						optiondb.setAttributID(oldb.getAttribut().getAttributID());
						accesVariation.insertOption(optiondb);
					}
					/** option existante **/
					else if(oldpt.getCode().equals(newpt.getCode()))
					{
						/** option avec code identique et informations diffentes**/
						if(!newpt.equals(new OptionAttribut(oldpt)))
						{
							OptionAttributDB optiondb = new OptionAttributDB(newpt);
							optiondb.setAttributID(oldb.getAttribut().getAttributID());
							accesVariation.updateOption(optiondb);			
						}
						foundpt = true;
						break;
					}
				}
				if(!foundpt)
				{
					List<VariationDB> optvars = accesVariation.getVariationsByOption(oldpt);
					
					if(optvars != null && !optvars.isEmpty())
					{
						for(VariationDB vdb : optvars)
							if(vdb.getQte() > 0)
								throw new Exception("l'option ["+oldpt.getCode()+"]"+oldpt.getNom()+" ne peut être supprimée "
									+ "car elle est utilisée par des variations en stock ");
						for(VariationDB vdb : optvars)
							accesVariation.delete(vdb);
						/*
						 si option appartient a une variation
	            		 	if var.qte == 0 delete var ; else throw error var.qte > 0
						 */
					}
					accesVariation.deleteOption(oldpt);
				}
			}
		}
		
		/** insertion des nouvelles options qui restent **/
		for(OptionAttribut optins : newb.getOptions())
		{	
			if(optins.getCode() == null)
			{
				optins.setCode(utils.genereOptionCode(newb));
				OptionAttributDB optiondb = new OptionAttributDB(optins);
				
				optiondb.setAttributID(oldb.getAttribut().getAttributID());
				accesVariation.insertOption(optiondb);
			
			}
		}
	}
}