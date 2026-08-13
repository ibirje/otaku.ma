package ma.otaku.acces.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.client.ClientAdresseDB;

public class AccesClientAdresse extends AccesTable<ClientAdresseDB>{

	public AccesClientAdresse() { super(ClientAdresseDB.class); }

	@Override
	protected Query isInDBQuery(ClientAdresseDB t, EntityManager manager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void verifierDonnees(ClientAdresseDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurExistant(ClientAdresseDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteErrors(ClientAdresseDB t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public ClientAdresseDB update(ClientAdresseDB old, ClientAdresseDB t, Long clientID) throws Exception {
		verifierDonnees(t);
		List<ClientAdresseDB> addrs = getEqualsList("clientID", clientID);

		if(addrs == null) throw new Exception("Adresse introuvable");
		
		for(ClientAdresseDB ad : addrs)
		{
			if(v().isEqual(ad.getNom(), old.getNom()) && v().isEqual(ad.getPrenom(), old.getPrenom()) &&
			   v().isEqual(ad.getAdresse1(), old.getAdresse1()) && v().isEqual(ad.getAdresse2(), old.getAdresse2()) &&
			   v().isEqual(ad.getVille(), old.getVille()) && v().isEqual(ad.getTelephone1(), old.getTelephone1())
			   && v().isEqual(ad.getCodePostal(), old.getCodePostal()))
			{
				ad.setNom(t.getNom());
				ad.setPrenom(t.getPrenom());
				ad.setAdresse1(t.getAdresse1());
				ad.setAdresse2(t.getAdresse2());
				ad.setVille(t.getVille());
				ad.setTelephone1(t.getTelephone1());
				ad.setCodePostal(t.getCodePostal());
				return super.update(ad);
			}
		}
		return null; 
	}
	

	public void selectAdresse(ClientAdresseDB adresse) throws Exception{

		List<ClientAdresseDB> addrs = getEqualsList("clientID", adresse.getClientID());
		if(addrs != null)
			for(ClientAdresseDB ad : addrs)
			{
				if( v().isEqual(ad.getNom(), adresse.getNom()) &&
					v().isEqual(ad.getPrenom(), adresse.getPrenom()) &&
					v().isEqual(ad.getAdresse1(), adresse.getAdresse1()) &&
					v().isEqual(ad.getAdresse2(), adresse.getAdresse2()) &&
					v().isEqual(ad.getVille(), adresse.getVille()) &&
					v().isEqual(ad.getTelephone1(), adresse.getTelephone1()) &&
					v().isEqual(ad.getCodePostal(), adresse.getCodePostal()))
				{
					ad.setEtat("SELECTED");
				}
				else
				{
					ad.setEtat("NORMAL");
				}
				update(ad);
			}
	}

}
