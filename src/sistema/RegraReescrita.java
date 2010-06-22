package sistema;

import java.util.Vector;

import sistema.expressaoRegular.ExpressaoRegular;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Grupo;
import sistema.expressaoRegular.gramatica.Simbolo;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.parser.Nodo;

public class RegraReescrita {
	public ExpressaoRegular _ER;
	public CadeiaSustituicao _CadeiaSubstituicao;
	
	public Vector<RegraReescrita> regrasComInterseccao = new Vector<RegraReescrita>();
	
	public RegraReescrita(String expressaoRegular, String cadeiaSubstituicao) {
		_ER = new ExpressaoRegular(expressaoRegular);
		_CadeiaSubstituicao = new CadeiaSustituicao(cadeiaSubstituicao, _ER._P._G._MaxIndice);
	}
	
	public Vector<String> processarEntrada(String texto) {
		Vector<String> retorno = new Vector<String>();
		
		// Iniciando o parser com o string de entrada
		_ER._P.iniciar(texto);
		
		Nodo derivacao = null;
		
		// Processar todas as derivações possíveis
		System.out.println("\nDerivações encontradas para ER: " + _ER._ER + " com entrada: " + texto);
		while ((derivacao = _ER._P.getNextDerivacao()) != null) {
			retorno.add( getCadeiaSubstituida(derivacao, _ER._P._G) );
			System.out.println();
		}
		
		_ER._P.limpar();
		return retorno;
	}
	
	private String getCadeiaSubstituida(Nodo derivacao, Gramatica gramatica) {
		
		Vector<MarcacaoSubstring> substring = new Vector<MarcacaoSubstring>();
		Vector<MarcacaoSubstring> pendentes = new Vector<MarcacaoSubstring>();
		Nodo n_Atual = derivacao.getCaminho(0);
		Nodo n_Anterior = n_Atual;
		
		do {
			/**
			 * Adicionando uma nova substrig
			 */
			if (n_Atual.derivacao._V instanceof Grupo) {
				Grupo g = (Grupo) n_Atual.derivacao._V;
				
				// Procurar a Variável=Grupo que está mais a esquerda no nodo Anterior que foi derivado no nodo Atual
				int e;
				for ( e = 0; e < n_Anterior._FormaSentencial.size(); e++) {
					if (n_Anterior._FormaSentencial.elementAt(e) instanceof Grupo)
						break;
				}
				
				/**
				 * Adicionar o grupo com quantidade de terminais a esquerda
				 */
				pendentes.add( new MarcacaoSubstring(g, e, 1) );	// adicionando um terminal para a substring
			}
			
			/**
			 * Atualizar a terminação das substrings
			 */
			if (n_Atual.derivacao._Corpo.size() > 1) {
				/**
				 * Somente variáveis
				 */
				int terminaisAMais = n_Atual.derivacao._Corpo.size() - 1;		// Quantidade de variáveis menos 1 já considerado
				
				/*
				 * Aumentar a quantidade de termianis das substring para todas as substrings pendentes
				 */
				for (int i = 0; i < pendentes.size(); i++) {
					pendentes.elementAt(i).qtdeTerminaisPendentes += terminaisAMais;
					pendentes.elementAt(i).qtdeTerminais++;
				}
			/**
			 * É apenas um terminal
			 */
			} else if (n_Atual.derivacao._Corpo.firstElement() instanceof Terminal){
				/*
				 * Caso a quantidade de terminais seja 0, retirar de pendente
				 * Caso o terminal seja epsilon, diminuir a quantidade de terminais e terminais pendentes
				 */
				for (int i = 0; i < pendentes.size(); i++) {
					if (n_Atual.derivacao._Corpo.firstElement()._caractere == Terminal.epsilon) {
						pendentes.elementAt(i).qtdeTerminais--;
						pendentes.elementAt(i).qtdeTerminaisPendentes--;
					}
					
					if (--pendentes.elementAt(i).qtdeTerminaisPendentes <= 0) {	// Diminui em 1 a quantidade de terminais pendentes
						substring.add(pendentes.remove(i--));					// Decrementa para pesquisar a próxima substring
					}
				}
			}
			
			n_Anterior = n_Atual;
		} while ((n_Atual = n_Atual.getProxNodoDerivado()) != null);
		
		// Limpando substring dos grupos
		for (int i = 0; i < gramatica._V.size(); i++) {
			if (gramatica._V.elementAt(i) instanceof Grupo) {
				((Grupo)gramatica._V.elementAt(i)).limparSubstrings();
			}
		}
		
		// Adicionando as substrings em seus respectivos grupos
		for (int i = 0; i < substring.size(); i++) {
			substring.elementAt(i).addSubstring(n_Anterior._FormaSentencial);
		}
		
		return _CadeiaSubstituicao.gerarString(gramatica);
	}
	
	public class MarcacaoSubstring{
		public Grupo g;
		public int terminaisAEsquerda;
		public int qtdeTerminaisPendentes;
		public int qtdeTerminais;
		
		public MarcacaoSubstring(Grupo g, int terminaisAEsquerda, int qtdeTerminaisPendentes) {
			this.g = g;
			this.terminaisAEsquerda = terminaisAEsquerda;
			this.qtdeTerminaisPendentes = qtdeTerminaisPendentes;
			this.qtdeTerminais = qtdeTerminaisPendentes;
		}
		
		public void addSubstring(Vector<Simbolo> sentenca) {
			String result = "";
			for (int i=0;  i < qtdeTerminais; i++) {
				result += sentenca.elementAt(terminaisAEsquerda + i)._caractere;
			}
			
			g.addSubstring(result);
		}
	}
}