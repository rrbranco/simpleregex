package sistema.expressaoRegular;

import sistema.expressaoRegular.conversao.ConversaoER_Gramatica;
import sistema.expressaoRegular.gramatica.Gramatica;

public class ExpressaoRegular {
	public String _ER;
	public Parser _P;
	
	public ExpressaoRegular(String expressaoRegular) {
		_ER = expressaoRegular;
		Gramatica _G = ConversaoER_Gramatica.criarGramaticaDeExpressaoRegular(this);
		System.out.println("Verificar se os indices dos grupos batem com os numeros da cadeia de substituicao");
		
		// Criando parser para a expressão regular
		_P = new Parser(_G);
	}
}