package sistema.expressaoRegular.parser.TabelaLL1;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;

public class TabelaLL1 {
	public Vector<LinhaLL1> _Linhas = new Vector<LinhaLL1>();
	
	public void addProducao(Variavel variavel, Terminal terminal, Producao producao) {
		// Adicionando o endereço da linha da tabela LL(1) à variável
		if (variavel.idLinha == null) {
			variavel.idLinha = _Linhas.size();
			_Linhas.add(new LinhaLL1());
		}
		LinhaLL1 var = _Linhas.elementAt(variavel.idLinha);
		
		// Adicionando o mapeamento dos terminais da coluna
		if (!var.containsKey(terminal)) {
			var.put(terminal, new ColunasLL1());
		}
		var.get(terminal).addProducao(producao);
	}
	
	public ColunasLL1 getDerivacoesLL1(Variavel v, Terminal t) {
		return _Linhas.elementAt(v.idLinha).get(t);
	}
}