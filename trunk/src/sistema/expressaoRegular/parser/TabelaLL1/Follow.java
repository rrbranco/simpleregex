package sistema.expressaoRegular.parser.TabelaLL1;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Simbolo;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;

public class Follow {
	private static Gramatica _G;

	public static void calcFollow(Gramatica gramatica) {
		_G = gramatica;
		
		while(calcFollow());
		
		_G = null;	// Limpando memória
		
	}
	
	private static boolean calcFollow() {
		boolean alterado = false;
		
		// Processar todas as produções
		for (int i = 0; i < _G._P.size(); i++) {
			Producao prod = _G._P.elementAt(i);
			
			// Para cada símbolo da produção
			for (int s = prod._Corpo.size()-1; s > -1; s--) {
				
				/**
				 * Caso seja uma variável A->...BC...
				 */
				if (prod._Corpo.elementAt(s) instanceof Variavel) {
					Variavel varB = (Variavel) prod._Corpo.elementAt(s);
					Simbolo simbC = (s+1 < prod._Corpo.size())?prod._Corpo.elementAt(s+1):null;
					
					/**
					 * Caso C seja um terminal, adicionar C à Follow(B) 
					 */
					if (simbC instanceof Terminal) {
						alterado |= addTerminal(varB, (Terminal) simbC);
						
					/**
					 * Caso C seja uma variável 
					 */
					} else {
						Variavel varC = (Variavel) simbC;
						
						/**
						 * Caso C não seja a última variável em A->...BC..., Follow(B) += First(C)-epsilon
						 */
						if (varC != null) {
							alterado |= addTerminalEmFollowMenosEpsilon(varB, varC._First);
							
							/**
							 * Caso exista epsilon em First(C), Follow(B) += Follow(C)
							 */
							if (varC.produzEpsilon) {
								alterado |= addTerminalEmFollowMenosEpsilon(varB, varC._Follow);
							}
						}
						
						/**
						 * Caso B seja a última variável em A->...B ou First(C) em A->...BC deriva epsilon
						 * Follow(B) += Follow(A)
						 */
						if (todosDerivamEpsilon(prod._Corpo, s+1)) {
							alterado |= addTerminalEmFollowMenosEpsilon(varB, prod._V._Follow);
						}
					}
				}
			}
		}
		
		return alterado;
	}
	
	private static boolean todosDerivamEpsilon(Vector<Simbolo> corpo, int i) {
		
		for (; i < corpo.size(); i++) {
			if (!(corpo.elementAt(i) instanceof Variavel))
				return false;
			
			if (!((Variavel)corpo.elementAt(i)).produzEpsilon)
				return false;
		}
		
		return true;
	}

	private static boolean addTerminalEmFollowMenosEpsilon(Variavel varFollow, Vector<Terminal> vetor) {
		boolean adicionou = false;
		
		for (int i = 0; i < vetor.size(); i++) {
			adicionou |= addTerminal(varFollow, vetor.elementAt(i));
		}
		
		return adicionou;
	}
	
	private static boolean addTerminal(Variavel varFollow, Terminal term) {
		if (varFollow._Follow.indexOf(term)==-1 && term._caractere != Terminal.epsilon) {
			varFollow._Follow.add(term);
			return true;
		}
		
		return false;
	}
}