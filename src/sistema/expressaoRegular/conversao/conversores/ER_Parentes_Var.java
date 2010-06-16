package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produções para ER da forma: (A)
 */
public class ER_Parentes_Var implements Conversor {
	private static final Conversor _c;
	
	static { _c = new ER_Parentes_Var(); }

	public static Conversor getInstance() { return _c; }
	
	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		Variavel var = new Variavel();
		g.addVariavel(var);
		varPendente.add(0, var);
		
		Producao _p = new Producao(varPendente.remove(1), null);
		_p.addSimboloCorpo(var);
		g.addProducao(_p);
		
		return ++terminaisLidos;	// Incrementa um terminal (
	}
}