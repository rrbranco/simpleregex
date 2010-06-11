package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produções para ER da forma: A~
 */
public class ER_Til_Var implements Conversor {
	private static final Conversor _c;
	
	static { _c = new ER_Til_Var(); }

	public static Conversor getInstance() { return _c; }
	
	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		Variavel varRecursao = new Variavel();
		g.addVariavel(varRecursao);
		
		Variavel varNormal = new Variavel();
		g.addVariavel(varNormal);
		varPendente.add(0, varNormal);
		
		// Produção de criação de recursão X->R
		Producao _p = new Producao(varPendente.remove(1), null);
		_p.addSimboloCorpo(varRecursao);
		g.addProducao(_p);
		
		// R->A
		_p = new Producao(varRecursao, null);
		_p.addSimboloCorpo(varNormal);
		g.addProducao(_p);
		
		// R->AR
		_p = new Producao(varRecursao, null);
		_p.addSimboloCorpo(varNormal);_p.addSimboloCorpo(varRecursao);
		g.addProducao(_p);
		
		return terminaisLidos;
	}
}