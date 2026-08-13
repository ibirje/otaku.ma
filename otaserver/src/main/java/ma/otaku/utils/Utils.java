package ma.otaku.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.jasypt.util.text.BasicTextEncryptor;

import ma.otaku.business.produits.BeanAttributOptions;
import ma.otaku.business.produits.BeanProduitAttributs;
import ma.otaku.business.produits.OptionAttribut;
import ma.otaku.data.TableWithCode;

public class Utils {

	
	
	
	public String crypter(String text) {

		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

		textEncryptor.setPassword(INSCRIPTION_KEY);

		String encryptedText = textEncryptor.encrypt(text);

		String encryptedTextAndEncoded = new String(java.util.Base64.getEncoder().encode(encryptedText.getBytes()));

		return encryptedTextAndEncoded;

	}

	
	
	
	public String decripter(String text) {

		String decoded = new String(java.util.Base64.getDecoder().decode(text.getBytes()));

		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(INSCRIPTION_KEY);
		return textEncryptor.decrypt(decoded);
	}

	
	
	

	public int occurences(List<String> options, int debut, int fin) {

		int occurences = 0;
		for (int i = 0; i < options.size(); i++) {
			String opt1 = options.get(i).substring(debut, fin);
			for (int j = 0; j < options.size(); j++) {
				if (j == i)
					continue;
				String opt2 = options.get(j).substring(debut, fin);
				if (opt1.equals(opt2)) {
					occurences++;
					break;
				}
			}
			if (occurences > 0)
				break;
		}
		return occurences;
	}

	
	
	
	public String nextCode(String suffix, List<? extends TableWithCode> codes) {

		suffix += "_";
		return no_NextCode(suffix, codes);

	}

	
	
	
	public String no_NextCode(String suffix, List<? extends TableWithCode> codes) {
		String code = null;
		boolean found = false;

		if (codes == null || codes.isEmpty())
			return suffix + "01";

		for (int i = 1; i <= codes.size() + 1; i++) {
			code = suffix + (i < 10 ? "0" + i : i);
			found = false;

			for (TableWithCode cd : codes) {
				if (code.equals(cd.getCode())) {
					found = true;
					break;
				}
			}
			if (!found)
				return code;
		}
		return suffix + (codes.size() + 2 < 10 ? "0" + codes.size() + 2 : codes.size() + 2);
	}

	
	
	
	public String genereOptionCode(BeanAttributOptions newb) {

		List<OptionAttribut> options = newb.getOptions();

		String atcode = newb.getAttribut().getCode() + "_";
		String temp_gen = null;

		boolean found = false;

		for (int i = 1; i <= options.size() + 1; i++) {
			temp_gen = atcode + (i < 10 ? "0" + i : i);
			found = false;

			for (OptionAttribut option : options) {
				if (temp_gen.equals(option.getCode())) {
					found = true;
					break;
				}
			}
			if (!found)
				return temp_gen;
		}
		return atcode + (options.size() + 2 < 10 ? "0" + options.size() : options.size());
	}

	
	
	
	public String genereAttributCode(BeanProduitAttributs bean) {

		List<BeanAttributOptions> attributs = bean.getAttributs();
		String prodcode = bean.getProduit().getCode().substring(Constantes.DEBUT_ID_ATTRIBUT) + "_";

		String temp_gen = null;
		boolean found = false;

		for (int i = 1; i <= attributs.size() + 1; i++) {
			temp_gen = prodcode + (i < 10 ? "0" + i : i);
			found = false;

			for (BeanAttributOptions batt : attributs) {
				if (temp_gen.equals(batt.getAttribut().getCode())) {
					found = true;
					break;
				}
			}
			if (!found)
				return temp_gen;
		}
		return prodcode + (attributs.size() + 2 < 10 ? "0" + attributs.size() : attributs.size());
	}
	
	
	

	private static final String INSCRIPTION_KEY = "HykBF20i3r2Li9GbmQdTmiibeUvl8HOa";

	
	
	
	public long calculCountdown(Date dateFinPromo) {
		if (dateFinPromo == null)
			return 0;
		long secs = (dateFinPromo.getTime() - now().getTime()) / 1000;
		return secs < 0 ? 0 : secs;
	}

	
	
	
	public long calculCountdown(Timestamp dateFinPromo) {
		if (dateFinPromo == null)
			return 0;
		long secs = (dateFinPromo.getTime() - now().getTime()) / 1000;
		return secs < 0 ? 0 : secs;
	}

	
	
	
	public Timestamp now() {
		return Timestamp.from(zonedDate().toInstant());
	}

	
	
	public ZonedDateTime zonedDate() {
		return ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Africa/Casablanca"));
	}
	
	

	public String shortTime() {
		return now().toString().split("\\.")[0];
	}

	
	
}




