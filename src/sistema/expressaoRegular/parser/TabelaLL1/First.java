package sistema.expressaoRegular.parser.TabelaLL1;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Simbolo;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;

public class First {
	private static Gramatica _G;
	
	public static void calcFirst(Gramatica gramatica) {
		_G = gramatica;
		
		Vector<Producao> listaP = new Vector<Producao>();
		listaP.addAll(_G._P);
		
		/**
		 * Produções simples A->terminal
		 * 
		 * Caso A->epsilon ou A->aXYZ...
		 */
		for (int i = 0; i < listaP.size(); i++) {
			Producao prod = listaP.elementAt(i);
			Simbolo term = prod._Corpo.firstElement();
			
			if (term instanceof Terminal) {
				prod._V._First.add((Terminal) term);
				prod._V.produzEpsilon |= (term._caractere==Terminal.epsilon)?true:false;
				listaP.removeElementAt(i--);
			}
		}
		
		/**
		 * Produções com variáveis A->BCDE...
		 */
		while(calcFirstVar(listaP));
		
		_G = null;	// Limpando memória
	}
	
	private static boolean calcFirstVar(Vector<Producao> listaP) {
		boolean alterado = false;
		
		// Processar todas as produções
		for (int i = listaP.size()-1; i > -1; i--) {
			Producao prod = listaP.elementAt(i);
			
			// Para cada símbolo da produção
			for (int s = 0; s < prod._Corpo.size(); s++) {
				Simbolo simb = prod._Corpo.elementAt(s);
				
				/**
				 * Caso A seja uma variável em _V->ABCD..., First(_V) += First(A)
				 */
				if (simb instanceof Variavel) {
					Variavel var = (Variavel) simb;
					
					// Caso tenha adicionado um novo terminal em First(_V)
					if (adicionarTerminais(prod._V, var._First)) {
						alterado = true;
					}
					
					// Caso A não derive epsilon, cancelar próximos símbolos
					if (!var.produzEpsilon) { break; }
					
					// Caso todos os símbolos em _V->ABCD... forem variáveis que derivem epsilon, _V deriva epsilon
					if (!prod._V.produzEpsilon && s == 0 && todosSimbolosProduzemEpsilon(prod._Corpo)) {
						prod._V.produzEpsilon = true;
						prod._V._First.add(_G.getTerminal(Terminal.epsilon));
						alterado = true;
					}
					
					
				/**
				 * Caso seja um terminal normal
				 */
				} else {
					if (prod._V._First.indexOf(simb)==-1) {
						prod._V._First.add((Terminal) simb);
						alterado = true;
					}
					break;	// Acabou símbolos First
				}
			}
		}
		
		return alterado;
	}
	
	private static boolean todosSimbolosProduzemEpsilon(Vector<Simbolo> corpo) {
		for (int i = 0; i < corpo.size(); i++) {
			if (!(corpo.elementAt(i) instanceof Variavel))
				return false;
			
			if (!((Variavel)corpo.elementAt(i)).produzEpsilon)
				return false;
			
		}
		return true;
	}

	private static boolean adicionarTerminais(Variavel varFirst, Vector<Terminal> terminais) {
		boolean adicionou = false;
		
		for (int i = 0; i < terminais.size(); i++) {
			Simbolo simb = terminais.elementAt(i);
			
			// Verificar se existe algum símbolo não adicionado
			if (simb._caractere != Terminal.epsilon && varFirst._First.indexOf(simb)==-1) {
				varFirst._First.add((Terminal) simb);
				adicionou = true;
			}
			
		}
		
		return adicionou;
	}
}