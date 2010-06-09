package sistema.expressaoRegular.parser.TabelaLL1;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.parser.Nodo;

public class ColunasLL1 {
	private Vector<Producao> producoes = new Vector<Producao>();

	public Vector<Nodo> getCaminhos(Nodo pai) {
		Vector<Nodo> caminhos = new Vector<Nodo>();
		
		// Gerando todos os caminhos possíveis de derivação
		for (int i = 0; i < producoes.size(); i++) {
			Nodo n = new Nodo(pai);		// Criando novo nodo a partir do pai
			
			// Retirando a variável derivada e adicionando corpo da produção
			n._FormaSentencial.removeElementAt(n.pCharACasar);
			n._FormaSentencial.addAll(n.pCharACasar, producoes.elementAt(i)._corpo);
			
			caminhos.add(n);
		}
		
		return caminhos;
	}

	public void addProducao(Producao producao) {
		producoes.add(producao);
	}
}