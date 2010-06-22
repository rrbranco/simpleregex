package sistema;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.TabelaLL1.ColunasLL1;
import sistema.expressaoRegular.parser.TabelaLL1.LinhaLL1;

public class Debug {
	
	public static void printER(String r) {
		System.out.println("\nExpressão regular:\n" + r);
	}
	
	public static void printFollow(Vector<Variavel> vetorVariaveis) {
		System.out.println("\nGrupo Follow:");
		
		for (int i = 0; i < vetorVariaveis.size(); i++) {
			Vector<Terminal> follow = vetorVariaveis.elementAt(i)._Follow;
			
			System.out.print(vetorVariaveis.elementAt(i).simboloDebug + " -> ");
			for (int j = 0; j < follow.size(); j++) {
				System.out.print(follow.elementAt(j)._caractere);
			}
			System.out.println();
		}
	}
	
	public static void printFirst(Vector<Variavel> vetorVariaveis) {
		System.out.println("\nGrupo First:");
		
		for (int i = 0; i < vetorVariaveis.size(); i++) {
			Vector<Terminal> first = vetorVariaveis.elementAt(i)._First;
			
			System.out.print(vetorVariaveis.elementAt(i).simboloDebug + " -> ");
			for (int j = 0; j < first.size(); j++) {
				System.out.print(first.elementAt(j)._caractere);
			}
			System.out.println();
		}
	}
	
	public static void printGramatica(Gramatica g) {
		System.out.println("\nGramática convertida:");
		
		for (int i = 0; i < g._V.size(); i++) {
			g._V.elementAt(i).simboloDebug = (char)('A'+i);
		}
		
		for (int i = 0; i < g._P.size(); i++) {
			Producao prod = g._P.elementAt(i);
			
			printProducao(prod);
			System.out.println();
		}
	}
	
	private static void printProducao(Producao prod) {
		System.out.print(prod._V.simboloDebug + "->");
		
		for (int j = 0; j < prod._Corpo.size(); j++) {
			if (prod._Corpo.elementAt(j) instanceof Variavel)
				System.out.print(((Variavel)prod._Corpo.elementAt(j)).simboloDebug);
			if (prod._Corpo.elementAt(j) instanceof Terminal)
				System.out.print( prod._Corpo.elementAt(j)._caractere );
		}
	}

	public static void printTabela(Gramatica g) {
		System.out.println("\nTabela de parser:");
		
		// Todas as linhas, variáveis
		for (int l = 0; l < g._TabLL1._Linhas.size(); l++) {
			LinhaLL1 linha = g._TabLL1._Linhas.elementAt(l);
			Variavel var = getVar(l, g);
			
			// Todas as colunas, terminais
			for (int i = 0; i < g._T.size(); i++) {
				Terminal term = g._T.elementAt(i);
				
				if (linha.containsKey(term)) {
					System.out.print("(" + var.simboloDebug + "," + term._caractere + ") -> ");
					ColunasLL1 colunas = linha.get(term);
					
					// Todas as produções
					for (int j = 0; j < colunas.producoes.size(); j++) {
						printProducao(colunas.producoes.elementAt(j));
						System.out.print(',');
					}
					System.out.println();
				}
				
			}
			System.out.println();
		}
	}
	
	private static Variavel getVar(int i, Gramatica g) {
		for (int j = 0; j < g._V.size(); j++) {
			if (g._V.elementAt(j).idLinha == i)
				return g._V.elementAt(j);
		}
		
		return null;
	}
}