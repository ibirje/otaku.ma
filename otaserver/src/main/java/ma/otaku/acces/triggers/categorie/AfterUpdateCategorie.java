package ma.otaku.acces.triggers.categorie;

import java.util.List;

import ma.otaku.acces.produit.AccesCategorie;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.type.CategorieDB;

@Mode(Mode.AFTER)
public class AfterUpdateCategorie extends Trigger<CategorieDB> {

	private AccesCategorie acces;

	public AfterUpdateCategorie(AccesCategorie acces) {
		this.acces = acces;
	}

	@Override
	public void execute(CategorieDB old, CategorieDB t) throws Exception {
		
		Long pid = t.getCategorieParent();
		
		if(t == null || pid == null ) return;

		List<CategorieDB> categs = acces.getEqualsList("categorieParent", pid);
		long sum = 0, qte = 0, pendingqte = 0, activeQte = 0 ;
		for(CategorieDB cat : categs) 
		{
			sum += cat.getNombreProduits();
			qte += cat.getQte();
			pendingqte += cat.getPendingQte();
			activeQte += cat.getActiveQte();
		}

		if(sum == t.getNombreProduits() && qte == t.getQte() && pendingqte == t.getPendingQte())
			return;
		CategorieDB pcategorie = acces.getByID(pid);
		
		pcategorie.setNombreProduits(sum);
		pcategorie.setQte(qte);
		pcategorie.setPendingQte(pendingqte);
		pcategorie.setActiveQte(activeQte);
		
		acces.update(pcategorie);
		
	}

}
