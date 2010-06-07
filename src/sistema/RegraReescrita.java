package sistema;

import sistema.expressaoRegular.ExpressaoRegular;

public class RegraReescrita {
	public ExpressaoRegular _ER;
	public CadeiaSustituicao _CadeiaSubstituicao;
	
	public RegraReescrita(String expressaoRegular, String cadeiaSubstituicao) {
		_ER = new ExpressaoRegular(expressaoRegular);
		_CadeiaSubstituicao = new CadeiaSustituicao(cadeiaSubstituicao);
		
		System.out.println("Verificar se os índices de cadeia substituição existem");
	}
}