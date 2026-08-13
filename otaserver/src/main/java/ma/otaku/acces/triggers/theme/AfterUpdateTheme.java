package ma.otaku.acces.triggers.theme;

import java.util.List;

import ma.otaku.acces.produit.AccesTheme;
import ma.otaku.acces.triggers.common.Mode;
import ma.otaku.acces.triggers.common.Trigger;
import ma.otaku.data.type.ThemeDB;

@Mode(Mode.AFTER)
public class AfterUpdateTheme extends Trigger<ThemeDB> {

	private AccesTheme acces;
	
	public AfterUpdateTheme(AccesTheme acces) {
		this.acces = acces;
	}
	
	@Override
	public void execute(ThemeDB old, ThemeDB t) throws Exception {

		Long pid = t.getThemeParent();

		if(t == null || pid == null ) 
			return;
		
		List<ThemeDB> themes = acces.getEqualsList("themeParent", pid);
		
		long sum = 0, qte = 0, pendingqte = 0, activeQte = 0;
			
		for(ThemeDB theme : themes) 
		{
			sum += theme.getNombreProduits();
			qte += theme.getQte();
			pendingqte += theme.getPendingQte();
			activeQte += theme.getActiveQte();
		}

		if(sum == t.getNombreProduits() && qte == t.getQte() && pendingqte == t.getPendingQte())
			return;
		
		ThemeDB ptheme = acces.getByID(pid);
			
		ptheme.setNombreProduits(sum);
		ptheme.setQte(qte);
		ptheme.setPendingQte(pendingqte);
		ptheme.setActiveQte(activeQte);
		
		acces.update(ptheme);
	}
}



