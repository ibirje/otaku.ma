package ma.otaku.utils;

public class Constantes {

	
/******************** build constantes ********************/

	/* ********* URLs ***********/
	
	public static final String WEB_LINK       	 	 = "https://www.otaku.ma";     // google app engine production
	public static final String LOCAL_GAE        	 = "localhost:8080/"; // gae local testing
	public static final String LOCAL_TOMCAT        	 = "localhost:7902/otaserver"; // tomcat local testing
	
	public static final String WEB_BASE_URL      	 = "/";           // google app engine production
	public static final String LOCAL_GAE_BASE_URL    = "/"; // gae local testing
	public static final String LOCAL_TOMCAT_BASE_URL = "/otaserver/"; // tomcat local testing

	/* ********* persistence units ***********/
	
	public static final String LOCAL_MySQL       = "localhost_localMySQL";  // tomcat local avec base de données locale
	public static final String LOCAL_GSQL        = "localhost_GoogleMySQL"; // tomcat local avec base de données google
	public static final String WEB_GSQL          = "web_GoogleMySQL";  // google app engine avec base de données google
	

	public static final long HEURE_TO_MILLIS   	 = 3600000;
	public static final long MINUTE_TO_MILLIS  	 =   60000;
	public static final long SECONDE_TO_MILLIS 	 =    1000;
	
	public static final long DAY_TO_SECONDE   	 =  86400;
	public static final long HEURE_TO_SECONDE    =   3600;
	public static final long MINUTE_TO_SECONDE	 =     60;
	
/*************** client application admin ******************/
	
	public static final int MIN_PAGE       	   = 1;
	public static final int DEFAULT_PAGE   	   = 1;
	public static final int MIN_LIST_COUNT 	   = 5;
	public static final int DEFAULT_LIST_COUNT = 24;
	public static final int MAX_LIST_COUNT     = 100;
	
	public static final double MIN_PRIX 	= 0D;
	public static final double MAX_PRIX 	= 9999999D; 
	public static final int MIN_REDUCTION	= 0; 
	public static final int MAX_REDUCTION	= 100;
	
	public static final int MAX_FOURNISSEURS_POPUP = 10;

	public static final long ADMIN_TOKEN_EXPIRE_TIME = 60 * MINUTE_TO_MILLIS;
	public static final long RECUP_MDP_EXPIRE_TIME   = 30 * MINUTE_TO_MILLIS;

/******************* commandes (livraison, paiement) ***********************/
	
	public static final String LIV_VB  = "VB";  // Virement banquaire
	public static final String LIV_PAL = "PAL"; // Paiement à la livraison
	
	public static final String LIV_LD  = "LD";  // Livraison à domicile
	public static final String LIV_PR = "PR"; // Point de relais
	
	public static final double FRAIS_EMBALLAGE = 5;
	
/******************* client site web ***********************/

	public static final long PENDING_KEY_EXPIRE_TIME = 2 * HEURE_TO_MILLIS;

	public static final int SESSION_EXPIRE_TIME =  (int) (12 * HEURE_TO_SECONDE);

	public static final String SESSION_NOM = "USER_TOKEN";
	
	public static final long MIN_QTE_CLIENT = 1;
	public static final long MIN_QTE_ADMIN = 0;
	public static final long MIN_QTE = 0L;
	public static final int NOMBRE_PAGES_LISTE = 5; //nombre de pages max dans la liste de pages ( impaire )
	public static final int TAILLE_PAGE = 24;
	public static final int TAILLE_PAGE_MAX = 100;
	public static final int MAX_RELATED_PRODUCTS = 4;
	public static final int MAX_VARIATIONS = 200;

	public static final int MIN_PASSWORD_SIZE = 6;
	public static final int MIN_NOM_SIZE = 4;

	public static final int MIN_LENGTH_RECHERCHE = 3;
	
/******************** produit ***********************/
	
	public static final int MAX_OPTIONS   = 20; //MAX dans la BDD
	public static final int MAX_ATTRIBUTS = 10; //MAX dans la BDD

/******** index utilisés pour la generation des codes ********/ //TODO a enlever
	
	public static final int DEBUT_ID_CATEGORIE = 0;
	public static final int FIN_ID_CATEGORIE   = 8;
	public static final int DEBUT_ID_THEME     = 9;
	public static final int FIN_ID_THEME       = 11;
	public static final int FIN_ID_TYPE        = 11;
	public static final int DEBUT_ID_ATTRIBUT  = 6;

/*******************  ***********************/
	public static final String AUTO_MAIL_DEFAULT_EMETTEUR = "donotreply@otaku.ma"; 
	public static final String AUTO_MAIL_VERIFICATION_EMETTEUR = "verification@otaku.ma"; 
	// ou legit email assistance.otaku@gmail.com
	public static final String AUTO_MAIL_VERIFICATION_SUJET = "Confirmation d'adresse email";
	public static final String AUTO_MAIL_RECUPPWD_SUJET = "Modification du mot de passe";
	
	public static final String AUTO_MAIL_VERIFICATION_MESSAGE = 
		"Pour compléter votre inscription sur Otaku.ma, clickez sur le lien dessous pour confirmer votre adresse e-mail<br/><br/>" +
	    "Ce message a été généré automatiquement<br/>" + 
	    "Veuillez ne pas répondre à ce message.";

	public static final String AUTO_MAIL_RECUPPWD_MESSAGE = 
		"Pour modifier votre mot de passe, clickez sur le lien dessous pour saisir votre nouveau mot de passe.<br/><br/>"+
	    "Ce message a été généré automatiquement<br/>" + 
	    "Veuillez ne pas répondre à ce message.";
	
/*******************  ***********************/

	
	
	
	
	
	
}
