package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Grupo;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produções para ER da forma: ({A})*
 */
public class ER_Estrela_Parentes_Grupo_Var implements ConversorER {
	private static final ConversorER _c;
	
	static { _c = new ER_Estrela_Parentes_Grupo_Var(); }

	public static ConversorER getInstance() { return _c; }
	
	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		Grupo varRecursao = new Grupo();
		g.addVariavel(varRecursao);
		
		Variavel varNormal = new Variavel();
		g.addVariavel(varNormal);
		varPendente.add(0, varNormal);
		
		// Produção de criação de recursão X->R
		Producao _p = new Producao(varPendente.remove(1), null);
		_p.addSimboloCorpo(varRecursao);
		g.addProducao(_p);
		
		// R->epsilon
		_p = new Producao(varRecursao, null);
		_p.addSimboloCorpo(g.getTerminal(Terminal.epsilon));
		g.addProducao(_p);
		
		// R->AR
		_p = new Producao(varRecursao, null);
		_p.addSimboloCorpo(varNormal);_p.addSimboloCorpo(varRecursao);
		g.addProducao(_p);
		
		return terminaisLidos + 2;		// Referente aos ( e {
	}
}