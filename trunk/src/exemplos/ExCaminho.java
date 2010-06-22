package exemplos;

import java.util.Vector;

import sistema.Run;
import sistema.SistemaReescrita;

public class ExCaminho {
	
	/**
	 * Qual é o menor caminho de x à y
	 * 
	 * Caminho exemplo:
	 * 1 -> 2
	 * 1 -> 4
	 * 1 -> 3
	 * 2 -> 3
	 * 2 -> 5
	 * 3 -> 4
	 * 3 -> 5
	 * 4 -> 5
	 * 
	 * A alimentação ao sistema é 5de1? que indica se existe
	 * um caminho que partindo de 1 é possível chegar a 5
	 */
	public static void main(String[] args) {
		SistemaReescrita sist = new SistemaReescrita();
		
		String _Cidades = "1+2+3+4+5";
		String _Tudo = "-+" + _Cidades;
		String _TudoFecho = "("+_Tudo+")*";
		String _CidadesOU = "("+_Cidades+")";
		
		sist.addRegraReescrita(_CidadesOU + "de{"+_TudoFecho+"}1?", "1de/0/1-2");
		sist.addRegraReescrita(_CidadesOU + "de{"+_TudoFecho+"}1?", "1de/0/1-4");
		sist.addRegraReescrita(_CidadesOU + "de{"+_TudoFecho+"}1?", "1de/0/1-3");
		
		sist.addRegraReescrita(_CidadesOU + "de{"+_TudoFecho+"}2?", "1de/0/2-3");
		sist.addRegraReescrita(_CidadesOU + "de{"+_TudoFecho+"}2?", "1de/0/2-5");
		
		sist.addRegraReescrita(_CidadesOU + "de{"+_TudoFecho+"}3?", "1de/0/3-4");
		sist.addRegraReescrita(_CidadesOU + "de{"+_TudoFecho+"}3?", "1de/0/3-5");
		
		sist.addRegraReescrita(_CidadesOU + "de{"+_TudoFecho+"}4?", "1de/0/4-5");
		
		sist.addRegraReescrita("1de{"+_TudoFecho+"}1?", "Sim /0/1");
		sist.addRegraReescrita("2de{"+_TudoFecho+"}2?", "Sim /0/2");
		sist.addRegraReescrita("3de{"+_TudoFecho+"}3?", "Sim /0/3");
		sist.addRegraReescrita("4de{"+_TudoFecho+"}4?", "Sim /0/4");
		sist.addRegraReescrita("5de{"+_TudoFecho+"}5?", "Sim /0/5");
		
		String entrada = Run.lerEntrada("Digite o caminho de pesquisa no formato 'nden?': ");
		sist.limparAlimentacao();
		sist.addAlimentacao(entrada);
		
		Vector<String> result = null;
		
		// Pesquisar se encontrou um caminho
		boolean encontrouCaminho = false;
		
		while (!((result = sist.processarUmCiclo()).size() < 1) && !encontrouCaminho) {
			// Imprimir os caminhos
			for (int i = 0; i < result.size(); i++) {
				if (result.elementAt(i).charAt(0) == 'S') {
					System.out.println(result.elementAt(i));
					encontrouCaminho = true;
				}
			}
			
			// Novas entradas
			sist.limparAlimentacao();
			for (int i = 0; i < result.size(); i++) {
				sist.addAlimentacao(result.elementAt(i));
			}
		}
		
		System.out.println("FIM!");
		System.exit(-1);
	}
}