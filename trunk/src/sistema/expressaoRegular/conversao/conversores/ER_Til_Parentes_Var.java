package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produções para ER da forma: (A)~
 */
public class ER_Til_Parentes_Var implements Conversor {
	private static final Conversor _c;
	
	static { _c = new ER_Til_Parentes_Var(); }

	public static Conversor getInstance() { return _c; }
	
	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		// Incrementa + 1 para o (
		return ER_Til_Var.getInstance().converter(g, varPendente, ouPendente, p, n, terminaisLidos) + 1;
	}
}