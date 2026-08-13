package ma.otaku.acces.triggers.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * valeurs : <br/>
 * Mode.BEFORE<br/>
 * Mode.AFTER<br/>
 * Mode.ALWAYS ( s'execute dans BEFORE & AFTER )
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Mode {

	 public static final byte BEFORE = 0;
	 public static final byte AFTER = 1;
	 public static final byte ALWAYS = 2;
	 
	/**
	 * valeurs : <br/>
	 * Mode.BEFORE<br/>
	 * Mode.AFTER<br/>
	 * Mode.ALWAYS ( s'execute dans BEFORE & AFTER )
	 */
	 byte value() default ALWAYS;
}