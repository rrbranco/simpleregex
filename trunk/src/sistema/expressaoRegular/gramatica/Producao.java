package sistema.expressaoRegular.gramatica;

import java.util.Vector;

import sistema.expressaoRegular.conversao.conversores.Conversor;


public class Producao {
	public Variavel _V;
	public Vector<Simbolo> _Corpo = new Vector<Simbolo>();
	public Conversor _Conversor;
	
	public Producao(Variavel v, Conversor c) {
		_V = v;
		_Conversor = c;
	}
	
	public void addSimboloCorpo(Simbolo s){
		_Corpo.add(s);
	}
}