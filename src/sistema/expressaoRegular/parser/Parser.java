package sistema.expressaoRegular.parser;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Simbolo;

public class Parser {
	private Gramatica _G;
	private String _Str;
	
	private Vector<Simbolo> _FormaSentencial;
	private int pCharACasar;
	
	public Parser(Gramatica grammatica) {
		_G = grammatica;
	}
	
	/**
	 * Iniciar o parser para encontrar derivações
	 * 
	 * @param str - String a ser interpretada, parseada
	 */
	public void iniciar(String str) {
		_Str = str;
		pCharACasar = 0;
		_FormaSentencial = new Vector<Simbolo>();
		_FormaSentencial.add(_G._VariavelInicial);
	}
	
	/**
	 * @return false - Quando a derivação for errada, deve ser descartada
	 */
	private boolean proximoPasso() {
		// Verificar se chegou no fim da string de entrada
		if (_Str.length()>= pCharACasar) {
			return true;
		}
		
		// Adquirindo caractere de entrada e símbolo mais a esquerda à casar
		char charEntrada = _Str.charAt(pCharACasar);
		Simbolo sEsq = _FormaSentencial.elementAt(pCharACasar);
		
		/**
		 * Caso o símbolo da sentença formal for um terminal
		 */
		if (sEsq.isTerminal()) {
			// Se o símbolo da forma sentencial for correto
			if (sEsq.equals(charEntrada)) {
				return proximoPasso();
				
			// Se o símbolo da forma sentencial for errado
			} else {
				return false;		// Abortar derivação
			}
		
		/**
		 * Caso o símbolo da sentença formal for uma variável
		 */
		} else {
			
		}
		
		return false;
	}
	
	public Nodo getNextDerivacao() {
		
		
		return null;
	}
	
	public void limpar() {
		
	}
}