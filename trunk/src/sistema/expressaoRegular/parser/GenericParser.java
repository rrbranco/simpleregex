package sistema.expressaoRegular.parser;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Simbolo;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.TabelaLL1.ColunasLL1;

public abstract class GenericParser {
	private static final int ABORTAR_CAMINHO = 0;
	private static final int PROXIMO_PASSO = 1;
	private static final int ENCONTROU_CAMINHO = 2;
	private static final int NAO_ENCONTROU_CAMINHO = 3;
	
	private static int estado;
	
	// Tamanho da string de entrada
	private int tamStringEntrada;
	
	// Sentença formal da derivação
	protected Nodo _FS;
	private Nodo _NodoPai;
	
	// Vetor para BackTracking do parser
	private Vector<Integer> _BT;
	
	/**
	 * Iniciar o parser para encontrar derivações
	 * 
	 * @param str - String a ser interpretada, casada
	 */
	public void iniciar(int tamStringEntrada, Variavel varInicial) {
		this.tamStringEntrada = tamStringEntrada;
		_NodoPai = new Nodo(0, varInicial);
		_FS = _NodoPai;
		_BT = new Vector<Integer>();
		estado = PROXIMO_PASSO;
	}
	
	/**
	 * Realiza os passos do parser LL(1)
	 */
	private int proximoPasso() {
		System.gc();
		/**
		 * Limpando epsilons
		 */
		for (int i = 0; i < _FS._FormaSentencial.size(); i++) {
			if (_FS._FormaSentencial.elementAt(i) instanceof Terminal && _FS._FormaSentencial.elementAt(i)._caractere == Terminal.epsilon) {
				_FS._FormaSentencial.removeElementAt(i--);
			}
		}
		
		/**
		 * Verificar se já casou com todos os i elementos da string de entrada
		 */
		if (_FS.pCharACasar >= tamStringEntrada) {
			
			// Caso existam ainda mais símbolos na forma sentencial
			if (_FS._FormaSentencial.size() > tamStringEntrada) {
				
				// Todos deverão ser variáveis com epsilon em FIRST a partir de pCharACasar
				if (isAllEpsilonVariaveis(_FS._FormaSentencial, _FS.pCharACasar)) {
					return ENCONTROU_CAMINHO;	// Conseguiu encontrar uma derivação
				} else {
					return ABORTAR_CAMINHO;		// Abortar caminho de derivação
				}
				
			// Caso não existam mais símbolos
			} else {
				return ENCONTROU_CAMINHO;		// Conseguiu encontrar uma derivação
			}
		
		/**
		 * Caso a SENTENÇA gerada seja menor do que a string de entrada
		 */
		} else if (_FS.pCharACasar >= _FS._FormaSentencial.size()) {		// Se for sentença, ocorrerá um estouro no índice pCharCasar
			return ABORTAR_CAMINHO;		// Abortar caminho de derivação
		}
		
		DebugFormaSentencial(_FS._FormaSentencial);
		
		/**
		 * Verificar se podem ser podadas as derivações seguintes
		 */
		if (podarDerivacao()) {
			System.out.println("Podado");
			return ABORTAR_CAMINHO;
		}
		
		// Adquirindo símbolo mais a esquerda à casar
		Simbolo sEsq = _FS._FormaSentencial.elementAt(_FS.pCharACasar);
		
		/**
		 * Caso o símbolo da forma sentencial for um terminal
		 */
		if (sEsq.isTerminal()) {
			// Se o símbolo da forma sentencial for correto
			if (itMatch((Terminal) sEsq, _FS.pCharACasar)) {
				_FS.pCharACasar++;
				return PROXIMO_PASSO;			// Casar próximo caractere
				
			// Se o símbolo da forma sentencial for errado
			} else {
				return ABORTAR_CAMINHO;			// Abortar caminho de derivação
			}
		
		/**
		 * Caso o símbolo da forma sentencial for uma variável
		 */
		} else {
			ColunasLL1 derivacoes = getDerivacoesLL1((Variavel) sEsq, _FS.pCharACasar);
			
			// Caso não exista derivação para esta variável e terminal
			if (derivacoes == null) {
				return ABORTAR_CAMINHO;		// Abortar caminho atual
			
			// Caso exista, considerando ambiguidade, gerar todos os caminhos possiveis
			} else {
				Vector<Nodo> caminhos = derivacoes.getCaminhos(_FS);	// Indica o nodo pai para os caminhos filhos
				
				_BT.add(0);					// Adicionar primeiro endereço da bifurcação para Backtracking
				_FS.setCaminhos(caminhos);	// Adicionando caminhos do nodo
				
				_FS = _FS.getCaminho(0);	// Pesquisando pelo primeiro caminho
				return PROXIMO_PASSO;
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

	private int abortarCaminho() {
		System.out.println("Abortando caminho");
		
		// Não existe mais caminhos alternativos
		if (_BT.isEmpty()) {
			return NAO_ENCONTROU_CAMINHO;
		}
		
		_FS = _FS._Pai;								// Voltar a derivação ao nodo Pai
		_FS.eliminarCaminho(_BT.lastElement());		// Eliminar caminho atual. Aponta o _BT.lastElement() para o próximo caminho.
		
		/**
		 * Caso exista um próximo caminho através do nodo Pai
		 */
		if (_BT.lastElement() < _FS.getMaxBacktracking()) {
			_FS = _FS.getCaminho(_BT.lastElement());		// Pesquisar novo caminho
			return PROXIMO_PASSO;
			
		/**
		 * Caso não exista um próximo caminho através do nodo Pai
		 */
		} else {
			_BT.remove(_BT.size()-1);		// Eliminar Backtracking do Pai
			return ABORTAR_CAMINHO;			// Pesquisar caminhos alternativos pelo Avô
		}
	}
	
	public Nodo getNextDerivacao() {
		if (estado == ENCONTROU_CAMINHO){
			estado = ABORTAR_CAMINHO;
		}
		
		do {
			if (estado == ABORTAR_CAMINHO) {
				estado = abortarCaminho();
			}
			if (estado == PROXIMO_PASSO) {
				estado = proximoPasso();
			}
		} while (estado != ENCONTROU_CAMINHO && estado != NAO_ENCONTROU_CAMINHO ); 
		
		return (estado==ENCONTROU_CAMINHO)? _NodoPai:null;
	}
	
	/**
	 * Libera memória
	 */
	public void limpar() {
		_NodoPai = null;
		_FS = null;
		_BT = null;
		System.gc();
	}
	
	protected abstract boolean itMatch(Terminal t, int charACasar);
	
	protected abstract ColunasLL1 getDerivacoesLL1(Variavel linha, int charFirst);
	
	protected abstract boolean podarDerivacao();
	
	private void DebugFormaSentencial(Vector<Simbolo> formaSentencial) {
		for (int i = 0; i < formaSentencial.size(); i++) {
			if (formaSentencial.elementAt(i) instanceof Variavel)
				System.out.print(((Variavel)formaSentencial.elementAt(i)).simboloDebug);
			else {
				char c = ((Terminal)formaSentencial.elementAt(i))._caractere;
				System.out.print((c==Terminal.terminal)?'a':c);
			}
		}
		System.out.println();
	}
}