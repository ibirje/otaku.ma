package ma.otaku.utils;

import java.sql.Date;
import java.util.Collection;

import ma.otaku.data.client.CommandeDB;

public class Validateur {
	
	public static final String REGEX_CIN     = "^[a-zA-Z]{1,3}\\d{4,12}$";
	public static final String REGEX_ADRESSE = "[^\\/\\\\\"]";
	public static final String REGEX_NOM     = "^([a-zA-Zé]+(\\'|\\s){0,1})*$"; 
	public static final String REGEX_PSEUDO  = "^([\\-\\_\\•]){0,1}(\\w{2,}([\\-\\_\\•]){0,1})*$"; 
	public static final String REGEX_PHONE   = "^((\\+212){0,1}([\\s\\-]{0,1}\\d{1,3}){2,5})$";
	public static final String REGEX_SUBCODE = "^\\w\\w(((_\\w\\w){0,3})|((_\\w\\w){3}\\w*))$";
	public static final String REGEX_MOT_DE_PASSE = "^[^\\\\\\/\\\"]*$";

	public static final int TAILLE_MOT_DE_PASSE = 6;

	public boolean isNullOrEmpty(Object[] pl) { return pl == null || pl.length < 1; }
	public boolean isNullOrEmpty(Collection<?> pl) { return pl == null || pl.isEmpty(); }
	public boolean isNullOrEmpty(String str) { return str == null || str.trim().isEmpty(); }

	public boolean isNullOrNegatif( Integer number ) { return number == null || number < 0; }
	public boolean isNullOrNegatif( Long number ) { return number == null || number < 0; }
	public boolean isNullOrNegatif( Double number ) { return number == null || number < 0; }

	public boolean isNotSupZero( Integer number ) { return number == null || number < 1; }
	public boolean isNotSupZero( Long number )    { return number == null || number < 1; }
	public boolean isNotSupZero( Double number )  { return number == null || number < 1; }
	
	public boolean isEqual(String a, String b) {
		return isNullOrEmpty(a) && isNullOrEmpty(b) || ( !isNullOrEmpty(a) && a.equals(b)) ;
	}


	public boolean isTextValide(String string) {
		return !isNullOrEmpty(string) && string.matches(REGEX_ADRESSE);
	}

	/* ************************ titre (produit, categorie, theme) ************************ */

	public boolean isCommandeEtat(String text) {
		return  text.equals(CommandeDB.ACCEPTATION) || 
				text.equals(CommandeDB.PREPARATION) || 
				text.equals(CommandeDB.ENVOI) 		||
				text.equals(CommandeDB.ENVOYEE) 	||
				text.equals(CommandeDB.COMPLETE) 	||
				text.equals(CommandeDB.REFUSEE)		||
				text.equals(CommandeDB.ANNULEE);
	}
	public boolean isLockedCommandeEtat(String text) {
		return  text.equals(CommandeDB.ACCEPTATION) || 
				text.equals(CommandeDB.PREPARATION) || 
				text.equals(CommandeDB.ENVOI) 		||
				text.equals(CommandeDB.ENVOYEE);
	}

	public boolean isFailedCommandeEtat(String text) {
		return  text.equals(CommandeDB.REFUSEE)		||
				text.equals(CommandeDB.ANNULEE);
	}
	
	public boolean isTitreValide(String nom) {
		return !isNullOrEmpty(nom);
	}
	
	/* ********************************** CIN ********************************** */
	public boolean isCINValide(String cin) {
		return !isNullOrEmpty(cin) && cin.matches(REGEX_CIN);
	}

	public boolean isCINVideOrValide(String cin) {
		return isNullOrEmpty(cin) || cin.matches(REGEX_CIN);
	}
	
	/* ********************************* date ********************************* */
	public boolean isDateNullOrFutur(Date date) { 

		if(date == null) return true;
		long secs = (date.getTime()-System.currentTimeMillis())/1000;
		secs = secs < 0 ? 0 : secs;
		return secs >= 0;  
	}
	public boolean isDateNotNullOrFutur(Date date) {
		return date != null && isDateNotNullOrFutur(date);
	}
	/* ******************************* adresse ******************************* */
	
	public boolean isAdresseValide( String adresse ) {
		return !isNullOrEmpty(adresse) && adresse.matches(REGEX_ADRESSE);
	}
	public boolean isAdresseVideOrValide( String adresse ) {
		return isNullOrEmpty(adresse) || adresse.matches(REGEX_ADRESSE);
	}

	/* ****************************** mot de passe ******************************* */
	public boolean isMotdePasseValide(String mdp) {
		return !isNullOrEmpty(mdp) && mdp.length() >= TAILLE_MOT_DE_PASSE;
	}
	
	/* ********************************** Nom ********************************** */
	public boolean isNomValide(String nom) {
		return !isNullOrEmpty(nom) && nom.matches(REGEX_NOM);
	}
	public boolean isNomVideOrValide(String nom) {
		return isNullOrEmpty(nom) || nom.matches(REGEX_NOM);
	}

	/* ********************************** Pseudo ********************************** */
	public boolean isPseudoValide(String pseudo) {
		return !isNullOrEmpty(pseudo) && pseudo.matches(REGEX_PSEUDO);
	}
	public boolean isPseudoVideOrValide(String pseudo) {
		return isNullOrEmpty(pseudo) || pseudo.matches(REGEX_PSEUDO);
	}

	/* ********************************** telephone ********************************** */
	public boolean isTelephoneValide(String telephone) {
		return !isNullOrEmpty(telephone) && telephone.matches(REGEX_PHONE);
	}
	public boolean isTelephoneVideOrValide(String telephone) {
		return isNullOrEmpty(telephone) || telephone.matches(REGEX_PHONE);
	}
	/* *************************** recherche produit is code ************************* */

	public boolean isSubCode(String code) {
		return !isNullOrEmpty(code) && code.matches(REGEX_SUBCODE);
	}
	
	/* ********************************** email ********************************** */
	public boolean isEmailValide(String email)
	{
		return email != null && email.toLowerCase().matches(
		"(?:[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\""
		+ "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x"
		+ "01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
		+ "\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9]"
		+ "[0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])"
		+ "|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]"
		+ "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
	}
	
}
