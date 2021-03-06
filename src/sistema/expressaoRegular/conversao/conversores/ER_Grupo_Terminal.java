package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produ��es para ER da forma: {terminal}
 */
public class ER_Grupo_Terminal implements ConversorER {
	private static final ConversorER _c;
	
	static { _c = new ER_Grupo_Terminal(); }
	
	public static ConversorER getInstance() { return _c; }	
	
	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		// Adicionando X->Grupo
		terminaisLidos = ER_Grupo_Var.getInstance().converter(g, varPendente, ouPendente, p, n, terminaisLidos);
		
		// Adicionando Grupo->terminal
		Terminal _t = g.getTerminal( p._StrEntradaEscapada.charAt(terminaisLidos++) );
		
		Producao _p = new Producao(varPendente.remove(0), null);
		_p.addSimboloCorpo(_t);
		g.addProducao(_p);
		
		// Incrementar terminaisLidos se poss�vel
		while( terminaisLidos < n._FormaSentencial.size() && n._FormaSentencial.elementAt(terminaisLidos).isTerminal() ) {
			terminaisLidos++;
		}
		
		return terminaisLidos;
	}
}