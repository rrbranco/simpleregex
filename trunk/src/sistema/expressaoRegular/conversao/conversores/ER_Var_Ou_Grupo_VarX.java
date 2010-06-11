package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Grupo;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produções para ER da forma: A+{BY}, sendo Y variável que produz +alfa
 */
public class ER_Var_Ou_Grupo_VarX implements Conversor {
	private static final Conversor _c;
	
	static { _c = new ER_Var_Ou_Grupo_VarX(); }
	
	public static Conversor getInstance() { return _c; }
	
	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		Variavel X = varPendente.remove(0);
		
		Grupo Y = new Grupo();
		g.addVariavel(Y);
		ouPendente.add(0, Y);
		
		Variavel varB = new Variavel();
		g.addVariavel(varB);
		varPendente.add(0, varB);
		
		Variavel varA = new Variavel();
		g.addVariavel(varA);
		varPendente.add(0, varA);
		
		// Produção X->Y
		Producao _p = new Producao(X, null);
		_p.addSimboloCorpo(Y);
		g.addProducao(_p);
		
		// Produção X->A
		_p = new Producao(X, null);
		_p.addSimboloCorpo(varA);
		g.addProducao(_p);
		
		// Produção Y->B
		_p = new Producao(Y, null);
		_p.addSimboloCorpo(varB);
		g.addProducao(_p);
		
		return terminaisLidos;
	}
}