package sistema.expressaoRegular.gramatica;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Variavel;;

public class Grupo extends Variavel {
	public Integer _IndiceGrupo;
	public Vector<String> substrings = new Vector<String>();
	
	public Grupo() {
		super();
	}
	
	public void limparSubstrings() {
		substrings.removeAllElements();
	}
	
	public void addSubstring(String str) {
		substrings.add(str);
	}
}