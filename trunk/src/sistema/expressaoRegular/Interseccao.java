package sistema.expressaoRegular;

import java.util.Vector;

import sistema.RegraReescrita;

public class Interseccao {

	public static void atualizar(Vector<RegraReescrita> regras) {
		// Verificar a intersecção de todas com todas
		for (int i = 0; i < regras.size(); i++) {
			RegraReescrita origem = regras.elementAt(i);
			origem.regrasComInterseccao.removeAllElements();		// Limpando a intersecção
			
			for (int j = 0; j < regras.size(); j++) {
				RegraReescrita destino = regras.elementAt(j);
				
				// Caso exista intersecção adicionar na lista de regras com intersecção
				if (existeInterseccao(origem, destino)) {
					origem.regrasComInterseccao.add(destino);
				}
			}
			
		}
	}
	
	/**
	 * Método de intersecção proposto
	 */
	private static boolean existeInterseccao(RegraReescrita regraOriginaria, RegraReescrita regraAlimentada) {
		if (regraOriginaria == null)
			return true;
		
		return true;
	}
}