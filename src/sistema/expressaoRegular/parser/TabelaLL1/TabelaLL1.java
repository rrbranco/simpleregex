package sistema.expressaoRegular.parser.TabelaLL1;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;

public class TabelaLL1 {
	public Vector<LinhaLL1> _Linhas = new Vector<LinhaLL1>();
	
	public void addProducao(Variavel variavel, Terminal terminal, Producao producao) {
		// Adicionando o endere�o da linha da tabela LL(1) � vari�vel
		if (variavel.idLinha == null) {
			variavel.idLinha = _Linhas.size();
			_Linhas.add(new LinhaLL1());
		}
		LinhaLL1 var = _Linhas.elementAt(variavel.idLinha);
		
		// Adicionando o mapeamento dos terminais da coluna
		ColunasLL1 term;
		if (!var.containsKey(terminal)) {
			term = var.put(terminal, new ColunasLL1());
		} else {
			term = var.get(terminal);
		}
		
		term.producoes.add(producao);
	}
}