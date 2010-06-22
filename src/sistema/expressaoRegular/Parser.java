package sistema.expressaoRegular;

import sistema.Debug;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.GenericParser;
import sistema.expressaoRegular.parser.TabelaLL1.ColunasLL1;
import sistema.expressaoRegular.parser.TabelaLL1.First;
import sistema.expressaoRegular.parser.TabelaLL1.Follow;
import sistema.expressaoRegular.parser.TabelaLL1.TabelaLL1;

public class Parser extends GenericParser{
	public Gramatica _G;
	
	private String stringEstrada;
	
	public Parser(Gramatica _g) {
		super();
		
		_G = _g;
		
		_G._VariavelInicial._Follow.add(_G.getTerminal(Terminal.finalCadeia));
		First.calcFirst(_G);
		Follow.calcFollow(_G);
		Debug.printFirst(_G._V);
		Debug.printFollow(_G._V);
		
		_G._TabLL1 = new TabelaLL1(_G);
		Debug.printTabela(_G);
	}
	
	public void iniciar(String stringEntrada) {
		this.stringEstrada = stringEntrada;
		
		super.iniciar(stringEntrada.length(), _G._VariavelInicial);
	}
	
	@Override
	protected ColunasLL1 getDerivacoesLL1(Variavel linha, int charFirst) {
		return _G._TabLL1.getDerivacoesLL1(linha, new Terminal(stringEstrada.charAt(charFirst)));
	}
	
	@Override
	protected boolean itMatch(Terminal t, int charACasar) {
		return t.equals(stringEstrada.charAt(charACasar));
	}
	
	@Override
	protected boolean podarDerivacao() {return false;}
}