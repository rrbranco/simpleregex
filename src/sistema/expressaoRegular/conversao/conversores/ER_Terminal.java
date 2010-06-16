package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produções para ER da forma: terminal
 */
public class ER_Terminal implements Conversor {
	private static final Conversor _c;
	
	static { _c = new ER_Terminal(); }
	
	public static Conversor getInstance() { return _c; }
	
	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		
		Terminal _t = g.getTerminal( p._StrEntradaEscapada.charAt(terminaisLidos++) );
		
		Producao _p = new Producao(varPendente.remove(0), null);
		_p.addSimboloCorpo(_t);
		g.addProducao(_p);
		
		// Incrementar terminaisLidos se possível
		while( terminaisLidos < n._FormaSentencial.size() && n._FormaSentencial.elementAt(terminaisLidos).isTerminal() ) {
			terminaisLidos++;
		}
		
		return terminaisLidos;
	}
}