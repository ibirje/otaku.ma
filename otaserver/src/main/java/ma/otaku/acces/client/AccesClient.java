package ma.otaku.acces.client;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;

import ma.otaku.acces.base.AccesTable;
import ma.otaku.data.client.ClientDB;

public class AccesClient extends AccesTable<ClientDB> {

	public AccesClient() {
		super(ClientDB.class);
	}

	public AccesClient(byte read) {
		super(ClientDB.class, read);
	}

	@Override
	protected Query isInDBQuery(ClientDB t, EntityManager manager) throws Exception {

		return manager.createQuery("SELECT C FROM ClientDB C WHERE email = :email "
				+ "OR ( nom = :nom AND prenom = :prenom )")
				.setParameter("email", t.getEmail())
				.setMaxResults(1);
	}
	@Override
	public ClientDB update(ClientDB t) throws Exception {
		verifierDonnees(t);
		return super.update(t);
	}
	
	@Override
	protected void verifierDonnees(ClientDB t) throws Exception {
		
		if( !v().isEmailValide(t.getEmail())) throw new ValidationException("Email '"+ t.getEmail() +"' invalide");
		if( !v().isNomVideOrValide(t.getNom())) throw new ValidationException("Nom invalide");
		if( !v().isNomVideOrValide(t.getPrenom())) throw new ValidationException("Prénom invalide");
		if( !v().isPseudoVideOrValide(t.getPseudo())) throw new ValidationException("Pseudo invalide");
		if( !v().isTelephoneVideOrValide(t.getTelephone1())) throw new ValidationException("Telephone 1 invalide");
		if( !v().isTelephoneVideOrValide(t.getTelephone2())) throw new ValidationException("Telephone 2 invalide");
		
		/*
			private String notes; // commentaires sur le client
			private Boolean isActive;
			private String etat;
			private Date dateCreation;
			
			private Long panierCount;
			private String token;
		
			private String ville; // doit être transient
		 */
	}

	public ClientDB delete(ClientDB obj) throws Exception {
		return super.delete("email", obj.getEmail());
	}

	@Override
	protected void erreurExistant(ClientDB t) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	protected void erreurInexistant(String t) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	protected void deleteErrors(ClientDB t) throws Exception {
		// TODO Auto-generated method stub
	}

	public ClientDB existe(String email)
	{
		ClientDB clt = new ClientDB();
		clt.setEmail(email);
		return isObjectInDB(clt);
	}

	public ClientDB updateProfile(ClientDB client) throws Exception{
		
		ClientDB cltdb = getEquals("email", client.getEmail());
		if (cltdb == null) 
			throw new NotFoundException("Client introuvable");
		
		if( v().isEqual(client.getNom(),cltdb.getNom()) &&
			v().isEqual(client.getPrenom(),cltdb.getPrenom()) &&
			v().isEqual(client.getTelephone1(),cltdb.getTelephone1()) &&
			v().isEqual(client.getPseudo(),cltdb.getPseudo())) 
		{
			System.err.println("AccesClient -> updateProfile : Données identiques ??? comportement innatendu");
			return cltdb;
		}
		
		if( v().isNullOrEmpty(cltdb.getPseudo()) ) cltdb.setPseudo(client.getPseudo());
		cltdb.setNom(client.getNom());
		cltdb.setPrenom(client.getPrenom());
		cltdb.setTelephone1(client.getTelephone1());
		
		return update(cltdb);
	}

	
}














