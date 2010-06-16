package sistema.expressaoRegular.gramatica;

public class Variavel extends Simbolo{
	
	public char simboloDebug;
	public boolean produzEpsilon = false;
	
	/**
	 * Número que identifica a linha em uma tabela LL1, evita
	 * pesquisa e consome menos memória em TabelaLL1.
	 */
	public Integer idLinha;
	
	public Variavel() {
		super(null);
	}
	
	public Variavel(char simbDebug) {	//Debug
		super(null);
		
		simboloDebug = simbDebug;
	}
	
	@Override
	public Object clone() {
		Variavel v = new Variavel();
		v.simboloDebug = this.simboloDebug;
		v.idLinha = this.idLinha;
		v.produzEpsilon = this.produzEpsilon;
		return v;
	}
}