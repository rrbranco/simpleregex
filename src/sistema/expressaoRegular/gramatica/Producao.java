package sistema.expressaoRegular.gramatica;

import java.util.Vector;

import sistema.expressaoRegular.conversao.conversores.ConversorER;

public class Producao {
	public Variavel _V;
	public Vector<Simbolo> _Corpo = new Vector<Simbolo>();
	public ConversorER _Conversor;
	
	public Producao(Variavel v, ConversorER c) {
		
		_V = v;
		_Conversor = c;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Producao) {
			Producao prod = (Producao) obj;
			
			// Verificar a variável
			if (_V != prod._V) { return false; }
			
			// Verificar os símbolos da produção
			if (_Corpo.size() != prod._Corpo.size()) { return false; }
			
			for (int i = 0; i < _Corpo.size(); i++) {
				if (_Corpo.elementAt(i) != prod._Corpo.elementAt(i)) { return false; }
			}
			
			return true;
		}
		
		return false;
	}
	
	public void addSimboloCorpo(Simbolo s){
		_Corpo.add(s);
	}
}