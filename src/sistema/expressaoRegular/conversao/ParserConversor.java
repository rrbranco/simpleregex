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
	protected ColunasLL1 getDerivacoesLL1(Variavel linha, int charFirst) {
		return _G._TabLL1.getDerivacoesLL1(linha, getParserChar(charFirst));
	}
	
	@Override
	protected boolean itMatch(Terminal t, int charACasar) {
		return t.equals( getParserChar(charACasar) );
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

	@Override
	protected boolean podarDerivacao() {
		int charToMatch = 0;
		
		// Para todos os terminais na forma sentencial
		for (int i = 0; i < _FS._FormaSentencial.size(); i++) {
			if (_FS._FormaSentencial.elementAt(i) instanceof Variavel) {
				continue;
			}
			
			Terminal t = (Terminal) _FS._FormaSentencial.elementAt(i);
			
			// Localizar a primeira ocorrência do terminal t na string de entrada, a partir do restante a ser casado
			for (; charToMatch < _StrEntradaEscapada.length(); charToMatch++) {
				if (getParserChar(charToMatch).equals(t))
					break;
			}
			
			// Caso tenha percorrido a string de entrada inteira e não encontrado t, abortar caminho de derivação
			if (charToMatch >= _StrEntradaEscapada.length())
				return true;
		}
		
		return false;
	}
}