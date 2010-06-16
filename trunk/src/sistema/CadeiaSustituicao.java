package sistema;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Terminal;

/**
 * Esta classe representa uma cadeia de substituição através de seus strings
 * fixos e índices para strings variados.
 * Tome como exemplo a cadeia de substituição: "ab/1/cd/2/df"
 * Nesta cadeia sempre teremos os strings fixos: "ab","cd","df"
 * Entre estes strings fixos teremos strings variadas dependendo da árvore de derivação
 * encontrada pelo parser que serão inseridos entre estes strings.
 * Guardamos então no vetor _Indice somente o número corresponte à ER para criacao
 * da string final.
 *
 */
public class CadeiaSustituicao {
	public Vector<String> _Fixo = new Vector<String>();
	public Vector<Integer> _Indice = new Vector<Integer>();
	public int _MaxIndice = -1;		// Para verificar se existe até tal índice na expressão regular.
	
	/**
	 * Cria cadeia de substituição já interpretando os índices.
	 * 
	 * @param str - Cadeia de substituição com índices definidos como /nnn/,
	 * com n sendo um dígito, e caracteres definidos como /n com n sendo qualquer
	 * caractere menos dígito.  
	 */
	public CadeiaSustituicao(String str, int _MaxIndiceGramatica) {
		
		for (int i = 0; i < str.length(); i++) {
			
			if (str.charAt(i) == Terminal.escape) {
				// Caso seja um terminal
				if (!Character.isDigit(str.charAt(i+1))) {
					// Não altera o texto, para não ocorrer em erro na entrada de outra regra de reescrita.
					i++;
					continue;
					
				// Caso seja um identificador
				} else {
					_Fixo.add( str.substring(0, i) );		// Adicionando parte fixa
					str = str.substring(i, str.length());	// Alterando para processar o resto da string
					
					i = getIndiceFinal(str); 	// Índice do caractere /
					String numero = str.substring(1, i);
					_Indice.add(new Integer(numero));	// Adicionando o número do índice
					
					// Para verificar se existe tal grupo na experssão regular
					if (_MaxIndice < _Indice.lastElement())
						_MaxIndice = _Indice.lastElement();
					
					// Eliminando sobra da string
					str = str.substring(i+1, str.length());
					i=-1;
				}
			}
		}
		
		if (str.length()>0)
			_Fixo.add( str );
	}

	private int getIndiceFinal(String str) {
		int i;
		for (i = 1; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i)))
				continue;
			break;
		}
		
		if (str.charAt(i) != Terminal.escape) {
			System.err.println("Erro de sintaxe\nSubstring fora do padrão: " + str);
			System.exit(1);
		}
		return i;
	}
}