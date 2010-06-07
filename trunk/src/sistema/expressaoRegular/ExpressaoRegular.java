package sistema.expressaoRegular;

import sistema.expressaoRegular.gramatica.Gramatica;

public class ExpressaoRegular {
	public String _ER;
	public Gramatica _G;
	
	public ExpressaoRegular(String expressaoRegular) {
		_ER = expressaoRegular;
		_G = ConversaoER_Gramatica.criarGramaticaDeExpressaoRegular(this);
	}
}