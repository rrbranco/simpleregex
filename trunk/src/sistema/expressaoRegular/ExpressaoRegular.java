package sistema.expressaoRegular;

import sistema.Debug;
import sistema.expressaoRegular.conversao.ConversaoER_Gramatica;
import sistema.expressaoRegular.gramatica.Gramatica;

public class ExpressaoRegular {
	public String _ER;
	public Parser _P;
	
	public ExpressaoRegular(String expressaoRegular) {
		_ER = expressaoRegular;
		Gramatica _G = ConversaoER_Gramatica.criarGramaticaDeExpressaoRegular(this);
		
		Debug.printER(expressaoRegular);
		Debug.printGramatica(_G);
		
		/**
		 * Criando tabela LL(1) e parser para a expressão regular
		 */
		_P = new Parser(_G);
	}
}