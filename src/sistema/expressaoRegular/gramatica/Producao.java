package sistema.expressaoRegular.gramatica;

import java.util.Vector;


public class Producao {
	public Variavel _V;
	public Vector<Simbolo> _corpo = new Vector<Simbolo>();
	
	public Producao(Variavel v) {
		_V = v;
	}
	
	public void addSimboloCorpo(Simbolo s){
		_corpo.add(s);
	}
}