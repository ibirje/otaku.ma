package ma.otaku.acces.stock;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.acces.base.ICodeAcces;
import ma.otaku.acces.produit.AccesProduitsClient;
import ma.otaku.acces.produit.AccesVariations;
import ma.otaku.acces.triggers.achatstock.AfterDeleteAchatStockItem;
import ma.otaku.acces.triggers.achatstock.AfterInsertAchatStockItem;
import ma.otaku.acces.triggers.achatstock.AfterUpdateAchatStockItem;
import ma.otaku.acces.triggers.achatstock.BeforeDeleteAchatStockItem;
import ma.otaku.acces.triggers.achatstock.BeforeInsertAchatStockItem;
import ma.otaku.acces.triggers.achatstock.BeforeUpdateAchatStockItem;
import ma.otaku.data.produit.ProduitDB;
import ma.otaku.data.produit.VariationDB;
import ma.otaku.data.stock.AchatStockItemDB;
import ma.otaku.utils.Utils;

public class AccesAchatStockItem extends AccesTable<AchatStockItemDB> implements ICodeAcces<AchatStockItemDB> {

	public AccesAchatStockItem() {

		super(AchatStockItemDB.class);

		insertTriggers.add(new BeforeInsertAchatStockItem());
		insertTriggers.add(new AfterInsertAchatStockItem());
		
		updateTriggers.add(new BeforeUpdateAchatStockItem());
		updateTriggers.add(new AfterUpdateAchatStockItem());

		deleteTriggers.add(new BeforeDeleteAchatStockItem());
		deleteTriggers.add(new AfterDeleteAchatStockItem());
	}
	public AccesAchatStockItem(byte param) {

		super(AchatStockItemDB.class, param);
	}

	@Override
	public String genereCode(AchatStockItemDB t) {
		String suffix = t.getCode()+"_"+t.getAchatStockCode();
		return new Utils().nextCode(suffix, getLike("code", suffix+"%"));
	}

	@Override
	public AchatStockItemDB getByCode(String code) throws Exception
	{ 
		if(!isCodeValide(code)) throw new Exception(" code achat stock item invalide") ; 
	
		return getEquals("code", code); 
	}

	@Override
	public boolean isCodeValide(String code) { return code.matches("\\w\\w_\\d{4}_(\\w\\w_){3}\\d+_\\d+"); }
	
	/** PAS BESOIN DE VERIFIER LA REDONDANCE LORS DE L'INSERTION A CAUSE DE LA SIMILARITE*/
	@Deprecated @Override 
	protected Query isInDBQuery(AchatStockItemDB t, EntityManager manager) throws Exception { return null; }

	/** PAS BESOIN DE VERIFIER LA REDONDANCE LORS DE L'INSERTION A CAUSE DE LA SIMILARITE*/
	@Deprecated @Override 
	public boolean isInDB(AchatStockItemDB t) {return false;}

	/** PAS BESOIN DE VERIFIER LA REDONDANCE LORS DE L'INSERTION A CAUSE DE LA SIMILARITE*/
	@Deprecated @Override 
	protected void erreurExistant(AchatStockItemDB t) throws Exception {}

	@Override
	protected void verifierDonnees(AchatStockItemDB t) throws Exception 
	{
		isDonnesValides(t);
		String code = genereCode(t);
		if( !isCodeValide(code))  throw new Exception("Le code d'achat item generé n'est pas valide");
		t.setCode(code);
	}
	
	public boolean isDonnesValides(AchatStockItemDB t) throws Exception 
	{
		if( t.getVarcode() == null || t.getVarcode().isEmpty()) throw new Exception("Code variation vide");
		if( t.getQte() < 1 ) throw new Exception("Quantité d'un Item inférieure à 1");
		if( t.getPrixUnite() < 1 ) throw new Exception("prixTotal d'un Item inférieur à 1");

		AccesVariations accesvar   = new AccesVariations();
		AccesProduitsClient accesproduit = new AccesProduitsClient();
		
		if(accesvar.isCodeValide(t.getVarcode()))
		{
			VariationDB var = accesvar.getByCode(t.getVarcode());
			if(var == null) throw new Exception("Variation "+t.getVarcode()+" inexistante");
			t.setVariationID(var.getVariationID());
			t.setCode("VR");
		}
		else if(accesproduit.isCodeValide(t.getVarcode())) 
		{
			ProduitDB var = accesproduit.getByCode(t.getVarcode());
			if(var == null) throw new Exception("Produit "+t.getVarcode()+" inexistante");
			t.setVariationID(var.getProduitID());
			t.setCode("PR");
		}
		else 
			throw new Exception("Code variation invalide");
		return true;
	}
	

	@Override
	protected void erreurInexistant(String t) throws Exception {
		throw new Exception("L'achat item "+t+" inexistant.");
	}

	@Override
	protected void deleteErrors(AchatStockItemDB t) throws Exception {
		if( t.getAssocie() ) throw new Exception(t.getCode()+" Cet achat item est deja associé à un stock \nIl ne peut pas être supprimé.");
	}

	public AchatStockItemDB delete(AchatStockItemDB t) throws Exception {
		return super.delete("code", t.getCode());
	}
	public List<AchatStockItemDB> getAllInfomationsList(String param, Object valeur, Boolean associe)
	{
		List<AchatStockItemDB> liste = associe == null ? dynamiqueEgalSelect(param,valeur):
			dynamiqueEgalSelect(param,valeur,"associe", associe);

		AccesVariations accesvar = new AccesVariations();
		AccesProduitsClient accesprod = new AccesProduitsClient();
		
		for(AchatStockItemDB achat : liste)
		{
			if(achat.getCode().substring(0,2).equals("PR"))
			{
				ProduitDB prod = accesprod.getByID(achat.getVariationID());
				achat.setVarcode(prod.getCode());
				achat.setImage(prod.getThumbnail());
				achat.setDescription(prod.getNom());
			}
			else
			{
				VariationDB var = accesvar.getByID(achat.getVariationID());
				ProduitDB prod = accesprod.getByID(var.getProduitID());
				achat.setVarcode(var.getCode());
				achat.setImage(var.getThumbnail());
				achat.setDescription(prod.getNom()+" : "+var.getNom());
			}
		}
		return liste;
	}

}
