package sistema.expressaoRegular.conversao;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.GenericParser;
import sistema.expressaoRegular.parser.TabelaLL1.ColunasLL1;

public class ParserConversor extends GenericParser{
	private Gramatica _G;
	
	private String _StrEscapada;			// String já escapada
	private Vector<Integer> _Escapado;		// Vetor com índices escapados de _StrEscapada
	
	
	public ParserConversor(Gramatica g) {
		super();
		
		_G = g;
	}
	
	public void iniciar(String stringEntrada) {
		_Escapado = new Vector<Integer>();
		_StrEscapada = "";
		
		for (int i = 0; i < stringEntrada.length(); i++) {
			
			if (stringEntrada.charAt(i)==ConversaoER_Gramatica.escape) {		// Localizando o caractere de escape \
				_Escapado.add(_StrEscapada.length());
				
				if (++i >= stringEntrada.length()) {							// Verificar erro de sintaxe
					System.err.println("Erro de Sintaxe\nEscape errado: " + stringEntrada);
					System.exit(1);
				}
			}
			
			_StrEscapada += stringEntrada.charAt(i);
		}
		
		// Iniciando o parser
		super.iniciar(_StrEscapada.length(), _G._VariavelInicial);
	}

	@Override
	protected ColunasLL1 getDerivacoesLL1(Variavel linha, int charFirst) {
		return _G._TabLL1.getDerivacoesLL1(linha, getMascaraChar(charFirst));
	}
	
	@Override
	protected boolean itMatch(Terminal t, int charACasar) {
		return t.equals( getMascaraChar(charACasar) );
	}
	
	private char getMascaraChar(int i) {
		return (_Escapado.indexOf(i)==-1)?_StrEscapada.charAt(i):ConversaoER_Gramatica.terminal;
	}
}