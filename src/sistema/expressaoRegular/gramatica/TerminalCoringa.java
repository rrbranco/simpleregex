package sistema.expressaoRegular.gramatica;


public class TerminalCoringa extends Terminal {
	
	public TerminalCoringa(char caractere) {
		super(caractere);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Character) {
			Character c = (Character) obj;
			
			if (_caractere == 'a') {							// Coringa a (terminal)
				if (	c != '(' &&								// Não aceita (, ), {, }, +, *, ~
						c != ')' &&
						c != '{' &&
						c != '}' &&
						c != '*' &&
						c != '+' &&
						c != '~'	) { return true; }
				return false;
			}
			
			// Casamento normal
			return this._caractere.equals(obj);
			
		} else if (obj instanceof Terminal) {
			return ((Terminal)obj)._caractere == this._caractere;
		}
		
		return false;
	}
}