package ma.otaku.utils;

public final class Build {

	public static final byte TOMCAT   	 = 0;
	public static final byte GAE      	 = 1;

	/* ******************************************************************* */

	public static final byte LOCAL_TYPE = GAE;
	
	/* ******************************************************************* */
	/* *********************** la config a changer *********************** */
	
	/* 	   PERSISTENCE =	LOCAL_MySQL || LOCAL_GSQL || WEB_GSQL		   */

	// public static final String PERSISTENCE = Constantes.WEB_GSQL;
	// public static final String PERSISTENCE = Constantes.LOCAL_MySQL;
	public static final String PERSISTENCE = Constantes.LOCAL_GSQL;
	
	
	/* ******************************************************************* */
	
	
	/* ********* pas besoin de configurer manuellement ******** */

	public static final byte LOCAL_BUILD  = 0;
	public static final byte WEB_BUILD    = 1;
	
	// BUILD : LOCAL_BUILD || WEB_BUILD
	public static final byte BUILD = 
		PERSISTENCE.equals(Constantes.LOCAL_MySQL) || PERSISTENCE.equals(Constantes.LOCAL_GSQL)? LOCAL_BUILD : WEB_BUILD;

	public static final boolean MAN_LOGGING_ON = BUILD == LOCAL_BUILD ? true : false;
	
	
	
	
	
	/* *********************** TODO A enlever *********************** */
	public static final String WEBSITE_LINK = 
			BUILD == LOCAL_BUILD ? (LOCAL_TYPE == TOMCAT ? Constantes.LOCAL_TOMCAT : Constantes.LOCAL_GAE) 
			: Constantes.WEB_LINK;
	
	public static final String BASE_URL = BUILD == LOCAL_BUILD ? 
			(LOCAL_TYPE == TOMCAT ? Constantes.LOCAL_TOMCAT_BASE_URL : Constantes.LOCAL_GAE_BASE_URL) 
			: Constantes.WEB_BASE_URL;

	public static final boolean AUTO_MAIL_ON  = BUILD != LOCAL_BUILD ;
	public static final boolean AUTO_MAIL_OFF = !AUTO_MAIL_ON ;
	
	public static final boolean RECAPTCHA_ON  = BUILD != LOCAL_BUILD ;
	public static final boolean RECAPTCHA_OFF = !RECAPTCHA_ON ;
	
}
