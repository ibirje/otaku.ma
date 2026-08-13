package ma.otaku.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public final class Parse {

	
	public final Double parseDouble(String s, Double remp) {
		try { Double d = Double.parseDouble(s.trim()); return d; } catch(Exception e) { return remp; }
	}

	public final Integer parseInteger(String s, int remp) { 
		try { Integer d = Integer.parseInt(s.trim()); return d; } catch(Exception e) { return remp; }
	}

	public final Date ParseDate(String dateFinPromo) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date parsed = null;
		
		try {
			if(dateFinPromo == null || dateFinPromo.isEmpty()) return null;
			java.util.Date date = sdf.parse(dateFinPromo);
			parsed = new Date( date.getTime() );
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return parsed;
	}
	public final String dateToString(Date date)
	{
		if(date == null) return null;
		
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
	}

	public final String dateToString(Timestamp date)
	{
		if(date == null) return null;
		
		return new SimpleDateFormat("dd/MM/yyyy à HH:mm").format(date);
	}
	
	
	public final String imageToThumbnail(String image) {
		return image != null ? image.replaceAll("images2", "thumbs2").replaceAll("_o.jpg","_t.jpg") : null;
	}
	public final String thumbnailToImage(String thumb) {
		return thumb != null ? thumb.replaceAll( "thumbs2" , "images2" ).replaceAll("_t.jpg","_o.jpg") : null;
	}

	public final String URLToNomProduit(String url) {
		return url.replaceAll("-", " ");
	}
	public final String NomToURL(String nom) {
		return nom.replaceAll(" ", "-");
	}
	public final String NomProduitToSiteURL(String nom) {
		return Build.BASE_URL+"items/"+NomToURL(nom);
	}
}




