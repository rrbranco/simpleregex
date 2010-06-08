package sistema.expressaoRegular.parser;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Simbolo;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.TabelaLL1.ColunasLL1;

public class Parser {
	// Gramática com tabela LL(1) e string a ser casada.
	private Gramatica _G;
	private String _Str;
	
	// Sentença formal da derivação
	private Nodo _FS;
	
	public Parser(Gramatica grammatica) {
		_G = grammatica;
	}
	
	/**
	 * Iniciar o parser para encontrar derivações
	 * 
	 * @param str - String a ser interpretada, casada
	 */
	public void iniciar(String str) {
		_Str = str;
		_FS = new Nodo(0, _G._VariavelInicial);
	}
	
	/**
	 * Realiza os passos do parser LL(1)
	 */
	private boolean proximoPasso() {
		// Verificar se chegou no fim da string de entrada
		if (_Str.length()>= _FS.pCharACasar) {
			return true;
		}
		
		// Adquirindo símbolo mais a esquerda à casar e caractere de entrada
		Simbolo sEsq = _FS._FormaSentencial.elementAt(_FS.pCharACasar);
		char charEntrada = _Str.charAt(_FS.pCharACasar);
		
		/**
		 * Caso o símbolo da forma sentencial for um terminal
		 */
		if (sEsq.isTerminal()) {
			// Se o símbolo da forma sentencial for correto
			if (sEsq.equals(charEntrada)) {
				_FS.pCharACasar++;
				return proximoPasso();		// Casar próximo caractere
				
			// Se o símbolo da forma sentencial for errado
			} else {
				return false;		// Abortar derivação
			}
		
		/**
		 * Caso o símbolo da forma sentencial for uma variável
		 */
		} else {
			ColunasLL1 derivacoes = _G._TabLL1.getDerivacoesLL1((Variavel) sEsq, charEntrada);
			
		}
		
		return false;
	}
	
	public Nodo getNextDerivacao() {
		
		
		return null;
	}
	
	public void limpar() {
		
	}
}