package sistema.expressaoRegular.gramatica;

import java.util.Vector;

import sistema.expressaoRegular.parser.TabelaLL1.TabelaLL1;

public class Gramatica {
	public Vector<Variavel> _V = new Vector<Variavel>();
	public Vector<Terminal> _T = new Vector<Terminal>();
	public Vector<Producao> _P = new Vector<Producao>();
	public Variavel _VariavelInicial;
	public TabelaLL1 _TabLL1;
	
	public void addTerminal(Terminal s) {
		if (_T.indexOf(s) != -1) {
			_T.add(s);
		}
	}
	
	public void addVariavel(Variavel v) {
		_V.add(v);
	}
	
	public void addProducao(Producao p) { _P.add(p); }
	
	public Terminal getTerminal(char c) {
		int i = _T.indexOf(new Terminal(c));
		
		if (i == -1) {
			Terminal t = new Terminal(c);
			return t;
		} else {
			return  (i == -1)?null:_T.elementAt(i);
		}
	}
}