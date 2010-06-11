package sistema.expressaoRegular.gramatica;

public class Terminal extends Simbolo {
	
	/**
	 * Símbolos especiais
	 */
	public static final char escape = '/';
	public static final char terminal = 1;
	public static final char epsilon = 0;
	
	public Terminal(char caractere) {
		super(caractere);
	}
	
	public static boolean isEpsilon(Terminal t) { return t._caractere==epsilon; }
	
	public static Terminal getEpsisolon() { return new Terminal(epsilon); }
	
	@Override
	public int hashCode(){
		return _caractere.hashCode();
	}
	
	@Override
	public Object clone() {
		return new Terminal(this._caractere);		// Não utilizado
	}
}