package ma.otaku.data.client;

public class ClientFacebook {

    String id;
    String email;
    String nom;
    String accessToken;
    String image;
    
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	@Override
	public String toString() {
		return "{ id :" + id + " nom :" + nom + " email :" + email + " accessToken :" + accessToken + " } " ;
	}
}
