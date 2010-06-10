package sistema.expressaoRegular.conversao;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Simbolo;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;
import sistema.expressaoRegular.parser.TabelaLL1.ColunasLL1;

public class ParserConversorTemp {
	// Gramática com tabela LL(1) e string a ser casada.
	private Gramatica _G;
	private String _Str;
	
	// Sentença formal da derivação
	private Nodo _FS;
	private Nodo _NodoPai;
	
	// Vetor para BackTracking do parser
	private Vector<Integer> _BT;
	
	public ParserConversorTemp(Gramatica grammatica) {
		_G = grammatica;
	}
	
	/**
	 * Iniciar o parser para encontrar derivações
	 * 
	 * @param str - String a ser interpretada, casada
	 */
	public void iniciar(String str) {
		_Str = str;
		_NodoPai = new Nodo(0, _G._VariavelInicial);
		_FS = _NodoPai;
		_BT = new Vector<Integer>();
	}
	
	/**
	 * Realiza os passos do parser LL(1)
	 */
	private boolean proximoPasso() {
		/**
		 * Verificar se já casou com todos os i elementos da string de entrada
		 */
		if (_FS.pCharACasar >= _Str.length()) {
			
			// Caso existam ainda mais símbolos na forma sentencial
			if (_FS._FormaSentencial.size() > _Str.length()) {
				
				// Todos deverão ser variáveis com epsilon em FIRST
				if (isAllEpsilonVariaveis(_FS._FormaSentencial, _FS.pCharACasar)) {
					return true;					// Conseguiu encontrar uma derivação
				} else {
					return abortarCaminho();		// Abortar caminho de derivação
				}
				
			// Caso não existam mais símbolos
			} else {
				return true;		// Conseguiu encontrar uma derivação
			}
		
		/**
		 * Caso a SENTENÇA gerada seja menor do que a string de entrada
		 */
		} else if (_FS.pCharACasar >= _FS._FormaSentencial.size()) {		// Se for sentença, ocorrerá um estouro no índice pCharCasar
			return abortarCaminho();		// Abortar caminho de derivação
		}
		
		
		// Adquirindo símbolo mais a esquerda à casar e caractere de entrada
		Simbolo sEsq = _FS._FormaSentencial.elementAt(_FS.pCharACasar);
		Character charEntrada = getCharEntrada(_FS.pCharACasar);
		
		/**
		 * Caso o símbolo da forma sentencial for um terminal
		 */
		if (sEsq.isTerminal()) {
			// Se o símbolo da forma sentencial for correto
			if (itMatch((Terminal) sEsq, charEntrada)) {
				_FS.pCharACasar++;
				return proximoPasso();			// Casar próximo caractere
				
			// Se o símbolo da forma sentencial for errado
			} else {
				return abortarCaminho();		// Abortar caminho de derivação
			}
		
		/**
		 * Caso o símbolo da forma sentencial for uma variável
		 */
		} else {
			ColunasLL1 derivacoes = getDerivacoesLL1((Variavel) sEsq, charEntrada);
			
			// Caso não exista derivação para esta variável e terminal
			if (derivacoes == null) {
				return abortarCaminho();	// Abortar caminho atual
			
			// Caso exista, considerando ambiguidade, gerar todos os caminhos possiveis
			} else {
				Vector<Nodo> caminhos = derivacoes.getCaminhos(_FS);	// Indica o nodo pai para os caminhos filhos
				
				_BT.add(0);					// Adicionar primeiro endereço da bifurcação para Backtracking
				_FS.setCaminhos(caminhos);	// Adicionando caminhos do nodo
				
				_FS = _FS.getCaminho(0);	// Pesquisando pelo primeiro caminho
				return proximoPasso();
			}
		}
	}

	private boolean isAllEpsilonVariaveis(Vector<Simbolo> variaveis, int indiceInicial) {
		
		for (; indiceInicial < variaveis.size(); indiceInicial++) {
			Simbolo s = variaveis.elementAt(indiceInicial);
			
			if (!(s instanceof Variavel) || !((Variavel)s).produzEpsilon) {
				return false;
			}
		}
		
		return true;
	}

	private boolean abortarCaminho() {
		// Não existe mais caminhos alternativos
		if (_BT.isEmpty()) {
			return false;
		}
		
		_FS = _FS._Pai;								// Voltar a derivação ao nodo Pai
		_FS.eliminarCaminho(_BT.lastElement());		// Eliminar caminho atual. Aponta o _BT.lastElement() para o próximo caminho.
		
		/**
		 * Caso exista um próximo caminho através do nodo Pai
		 */
		if (_BT.lastElement() < _FS.getMaxBacktracking()) {
			_FS = _FS.getCaminho(_BT.lastElement());		// Pesquisar novo caminho
			return proximoPasso();
			
		/**
		 * Caso não exista um próximo caminho através do nodo Pai
		 */
		} else {
			_BT.remove(_BT.size()-1);		// Eliminar Backtracking do Pai
			return abortarCaminho();		// Pesquisar caminhos alternativos pelo Avô
		}
	}
	
	public Nodo getNextDerivacao() {
		if (proximoPasso()) { return _NodoPai; }
		
		return null;
	}
	
	public void limpar() {
		_Str = null;
		_NodoPai = null;
		_FS = null;
		_BT = null;
		System.gc();
	}
	
	private Character getCharEntrada(int charACasar) {
		return _Str.charAt(charACasar);
	}
	
	private boolean itMatch(Terminal t, Character charEntrada) {
		return t.equals(charEntrada);
	}
	
	private ColunasLL1 getDerivacoesLL1(Variavel linha, Character charFirst) {
		return _G._TabLL1.getDerivacoesLL1(linha, (Character)charFirst);
	}
}