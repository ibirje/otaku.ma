package ma.otaku.acces.base;

public interface ICodeAcces<T> {

	String genereCode(T t);
	T getByCode(String code) throws Exception;
	boolean isCodeValide(String code) throws Exception;
}
