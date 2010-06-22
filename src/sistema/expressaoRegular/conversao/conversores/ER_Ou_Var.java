package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produções para ER da forma: +A
 */
public class ER_Ou_Var implements ConversorER {
	private static final ConversorER _c;
	
	static { _c = new ER_Ou_Var(); }
	
	public static ConversorER getInstance() { return _c; }
	
	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		Variavel X = ouPendente.remove(0);
		
		Variavel varA = new Variavel();
		g.addVariavel(varA);
		varPendente.add(0, varA);
		
		// Produção X->A
		Producao _p = new Producao(X, null);
		_p.addSimboloCorpo(varA);
		g.addProducao(_p);
		
		return ++terminaisLidos;	// Referente ao +
	}
}