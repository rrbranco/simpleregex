package sistema.expressaoRegular.conversao;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.GenericParser;
import sistema.expressaoRegular.parser.TabelaLL1.ColunasLL1;

public class ParserConversor extends GenericParser{
	private Gramatica _G;
	
	public String _StrEntradaEscapada;			// String já escapada
	public Vector<Integer> _CharEscapado;		// Vetor com índices escapados de _StrEscapada
	
	
	public ParserConversor(Gramatica g) {
		super();
		
		_G = g;
	}
	
	public void iniciar(String stringEntrada) {
		_CharEscapado = new Vector<Integer>();
		_StrEntradaEscapada = "";
		
		for (int i = 0; i < stringEntrada.length(); i++) {
			
			if (stringEntrada.charAt(i)==Terminal.escape) {				// Localizando o caractere de escape
				_CharEscapado.add(_StrEntradaEscapada.length());
				
				if (++i >= stringEntrada.length()) {					// Verificar erro de sintaxe
					System.err.println("Erro de Sintaxe\nEscape errado: " + stringEntrada);
					System.exit(1);
				}
			}
			
			_StrEntradaEscapada += stringEntrada.charAt(i);
		}
		
		// Iniciando o parser
		super.iniciar(_StrEntradaEscapada.length(), _G._VariavelInicial);
	}
	
	@Override
	public void limpar() {
		_CharEscapado = null;
		_StrEntradaEscapada = null;
		super.limpar();
	}

	@Override
	protected ColunasLL1 getDerivacoesLL1(Variavel linha, int charFirst) {
		return _G._TabLL1.getDerivacoesLL1(linha, getParserChar(charFirst));
	}
	
	@Override
	protected boolean itMatch(Terminal t, int charACasar) {
		return t.equals( getParserChar(charACasar) );
	}
	
	@Override
	protected boolean podarDerivacao() {
		int charToMatch = 0;
		
		// Para todos os terminais na forma sentencial
		int i = 0;
		for (; i < _FS._FormaSentencial.size(); i++) {
			
			if (_FS._FormaSentencial.elementAt(i) instanceof Variavel) {	// Como esta gramática não possui produções epsilon
				charToMatch++;												// Uma variável deve representar pelo menos um símbolo
				
			} else {
				Terminal t = (Terminal) _FS._FormaSentencial.elementAt(i);
				
				// Localizar a primeira ocorrência do terminal t na string de entrada, a partir do restante a ser casado
				while (charToMatch++ < _StrEntradaEscapada.length()) {
					if (getParserChar(charToMatch-1).equals(t))
						break;
				}
			}
			
			// Caso tenha percorrido mais símbolos do que a string de entrada, abortar caminho de derivação
			if (charToMatch > _StrEntradaEscapada.length())
				return true;
		}
		
		return false;
	}
	
	private Terminal getParserChar(int i) {
		
		if (_CharEscapado.indexOf(i) != -1) {
			return new Terminal(Terminal.terminal);
		}
		
		Character c = _StrEntradaEscapada.charAt(i);
		if ( c != '(' &&									// Caso não seja (, ), {, }, +, *, ~
				c != ')' &&
				c != '{' &&
				c != '}' &&
				c != '*' &&
				c != '+' &&
				c != '~'	) { c = Terminal.terminal; }	// É um terminal da nova gramática
		
		return new Terminal( c );
	}
}