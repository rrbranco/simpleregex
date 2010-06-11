package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produções para ER da forma: AB
 */
public class ER_VarVar implements Conversor {
	private static final Conversor _c;
	
	static { _c = new ER_VarVar(); }

	public static Conversor getInstance() { return _c; }
	
	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		Variavel varB = new Variavel();
		g.addVariavel(varB);
		varPendente.add(0, varB);
		
		Variavel varA = new Variavel();
		g.addVariavel(varA);
		varPendente.add(0, varA);
		
		Producao _p = new Producao(varPendente.remove(2), null);
		_p.addSimboloCorpo(varA);_p.addSimboloCorpo(varB);
		g.addProducao(_p);
		
		return terminaisLidos;
	}
}