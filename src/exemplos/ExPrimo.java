package exemplos;

import java.util.Vector;

import sistema.Run;
import sistema.SistemaReescrita;

public class ExPrimo {
	
	/**
	 * Exemplo de sistema auto construtivo para geração de números.
	 * 
	 * O sistema pega como entrada uma cadeia de 'a's, por exemplo: aaaa?
	 * 
	 * 
	 */
	public static void main(String[] args) {
		SistemaReescrita sist = new SistemaReescrita();
		Vector<String> primosEncontrados = new Vector<String>();
		int ate = 27;
		
		/**
		 * Regra do Resultado 
		 */
		// Regra próximo primo após 2: a({aa})~? -> a({/0,1/a})~?->a({//0,1//a})~
		//sist.addRegraReescrita("a({aa})~?", "a({/0,1/a})~?->a({//0,1//a})~");
		//sist.addRegraReescrita("a({aa})~?", "a({/0,1/a})~?->({/0,1/a})~?->DIVIDE POR /0,1/a");
		sist.addRegraReescrita("a({aa})~?", "a({/0/a})~?->({/0/a})~?->DIVIDE POR /0/a");
		sist.addRegraReescrita("({aa})~?", "DIVIDE POR aa");
		
		
		
		/**
		 * Ficar alimentando o sistema com novas entradas
		 */
		Vector<String> result;
		String entrada = "aa?";
		primosEncontrados.add("aa?");
		
		for (int i = 3; i <= ate; i++) {
			sist.limparAlimentacao();
			sist.addAlimentacao(entrada = "a" + entrada);
			System.out.println("Processando a entrada: " + entrada);
			
			result = sist.processarUmCiclo();
			Run.imprimirStrings(result);
			
			// Verificar se alguma regra divide
			boolean divide = false;
			for (int j = 0; j < result.size(); j++) {
				if (result.elementAt(j).charAt(0) == 'D') {
					divide = true;
					break;
				}
			}
			
			if (!divide) {
				System.out.println("\nPrimos encontrados até agora:\n");
				primosEncontrados.add(entrada);
				for (int j = 0; j < primosEncontrados.size(); j++) {
					System.out.println(primosEncontrados.elementAt(j));
				}
				Vector<String> regras = dividirRegras(result.elementAt(0));
				//System.out.println("Adicionando nova regra: " + regras.elementAt(0) + "->a({/0,1/a})~?->({/0,1/a})~?->DIVIDE POR /0,1/a");
				System.out.println("Adicionando nova regra: " + regras.elementAt(0) + "->a({/0/a})~?->({/0/a})~?->DIVIDE POR /0/a");
				System.out.println("Adicionando nova regra: " + regras.elementAt(1));
				//Run.addRegra(regras.elementAt(0) + "a({/0,1/a})~?->({/0,1/a})~?->DIVIDE POR /0,1/a", sist);
				Run.addRegra(regras.elementAt(0) + "a({/0/a})~?->({/0/a})~?->DIVIDE POR /0/a", sist);
				Run.addRegra(regras.elementAt(1), sist);
				
			} else {
				System.out.println("\n" + entrada + " não é primo.");
			}
		}
		
		System.out.println("\nPrimos encontrados pelo programa até " + ate + ":\n");
		primosEncontrados.add(entrada);
		for (int j = 0; j < primosEncontrados.size(); j++) {
			System.out.println(primosEncontrados.elementAt(j));
		}
	}
	
	private static Vector<String> dividirRegras(String s) {
		int i;
		for (i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '-')
				break;
		}
		
		Vector<String> v = new Vector<String>();
		v.add( s.substring(0, i++) );				// Pular -
		v.add( s.substring(++i, s.length()) );		// Pular >
		
		return v;
	}
}