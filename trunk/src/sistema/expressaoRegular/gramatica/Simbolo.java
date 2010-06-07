package sistema.expressaoRegular.gramatica;

public abstract class Simbolo {
	public Character _caractere;
	
	public Simbolo(Character caractere){
		_caractere = caractere;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Variavel) {
			return obj == this;
			
		} else if (obj instanceof Character && this instanceof Terminal) {
			return this._caractere.equals(obj);
			
		} else if (obj instanceof Simbolo) {
			return ((Simbolo)obj)._caractere == this._caractere;
		}
		
		return false;
	}
	
	public boolean isTerminal() {
		return this instanceof Terminal;
	}
	
	public boolean isVariavel() {
		return this instanceof Variavel;
	}
}