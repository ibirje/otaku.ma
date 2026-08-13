package ma.otaku.acces.triggers.common;
/**
 * ajouter @Mode(Mode.BEFORE) ou @Mode(Mode.AFTER) pour désigner l'ordre d'execution par rapport à l'operation<br/>
 * @default @Mode(Mode.ALWAYS)
 * 
 */
@Mode(Mode.ALWAYS)
public abstract class Trigger<T>{

	public abstract void execute(T old, T nnew) throws Exception;

	public byte getMode() {
		return getClass().isAnnotationPresent(Mode.class) ?
			getClass().getAnnotation(Mode.class).value() : Mode.ALWAYS;
	}
}
