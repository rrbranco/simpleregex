package sistema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class Run {
	public static final int ADICIONAR_REGRA = 0;
	public static final int ALIMENTAR_SISTEMA = 1;
	public static final int PROCESSAR_UMA_REGRA = 2;
	public static final int PROCESSAR_UM_CICLO = 3;
	public static final int PROCESSAR_TODOS_CICLOS = 4;
	public static final int MENU = 5;
	public static final int SAIR = 6;
	
	private static int estado;
	private static SistemaReescrita sist;
	
	private static Vector<String> stringsReescritas = new Vector<String>();
	
	public static void main(String[] args) {
		sist = new SistemaReescrita();
		
		String str;
		estado = ADICIONAR_REGRA;
		
		while (estado != SAIR) {
			switch (estado) {
				
				case ADICIONAR_REGRA:
					System.out.println("Fornecimento das regras de substituição\n");
					System.out.println("As regras possuem o formato: ER->CADEIA DE SUBSTITUICAO");
					System.out.println("Entre com uma regra e pressione <ENTER>");
					System.out.println("Pressione apenas <ENTER> com a linha vazia para finalizar o fornecimento de regras.");
					while (!(str = lerEntrada(">")).equals("")) {
						addRegra(str, sist);
					}
					
					estado = MENU; break;
					
				case ALIMENTAR_SISTEMA:
					System.out.println("\n\n\nAlimentação do sistema\n");
					System.out.println("Digite um string para alimentar o sistema e pressione <ENTER>");
					System.out.println("Pressione apenas <ENTER> com a linha vazia para finalizar o fornecimento de strings.");
					while (!(str = lerEntrada(">")).equals("")) {
						sist.addAlimentacao(str);
					}
					
					estado = MENU; break;
					
				case PROCESSAR_UMA_REGRA:
					Vector<String> strings = sist.processarUmaRegra();
					stringsReescritas.addAll(strings);
					imprimirStrings(strings);
					if (sist.processandoCiclo()) {
						System.out.println("Ciclo ainda não completo.");
					} else {
						System.out.println("Ciclo completo.");
						sist.limparAlimentacao();
						for (int i = 0; i < stringsReescritas.size(); i++) {
							sist.addAlimentacao(stringsReescritas.elementAt(i));
						}
						stringsReescritas.removeAllElements();
					}
					
					estado = MENU; break;
					
				case PROCESSAR_UM_CICLO:
					imprimirStrings(sist.processarUmCiclo());
					
					estado = MENU; break;
					
				case PROCESSAR_TODOS_CICLOS:
					imprimirStrings(sist.processarTodosCiclos());
					
					estado = MENU; break;
				case MENU:
					System.out.println("\nEscolha uma das seguintes opções:\n");
					System.out.println(ADICIONAR_REGRA + "- Adicionar uma nova regra de reescrita.");
					System.out.println(ALIMENTAR_SISTEMA + "- Adicionar uma nova string de alimentação.");
					System.out.println(PROCESSAR_UMA_REGRA + "- Processar somente uma regra de reescrita.");
					System.out.println(PROCESSAR_UM_CICLO + "- Processar somente um ciclo de regras de reescrita.");
					System.out.println(PROCESSAR_TODOS_CICLOS + "- Processar todos os ciclos de regras de reescrita até o fim.");
					System.out.println("Pressione apenas <ENTER> com a linha vazia para finalizar o sistema.");
					if ((str = lerEntrada("OPCAO>")).equals("")) {
						estado = SAIR;
					} else {
						estado = Integer.parseInt(str);
					}
					break;
				default:
					System.out.println("\nOpção inválida = " + estado);
					estado = MENU;
					break;
			}
		}
		
		System.out.println("\nFIM!");
		System.exit(0);
		
		/*sist.addRegraReescrita("({aa})~", "/0/");
		sist.addRegraReescrita("bde", "abc");
		sist.addRegraReescrita("({b}+{c})*", "/0/a/1/");
		sist.addRegraReescrita("b~/+a+{({b+C})*}", "/0/a/1/");
		sist.addRegraReescrita("a*a*b","a");*/
	}
	
	public static void imprimirStrings(Vector<String> v) {
		System.out.println("\nStrings reescritos:");
		
		for (int i = 0; i < v.size(); i++) {
			System.out.println(v.elementAt(i));
		}
	}

	public static void addRegra(String str, SistemaReescrita sist) {
		// Localizar ->
		int i;
		for (i = 0; i < str.length(); i++) {
			if (str.charAt(i)=='-') {
				break;
			}
		}
		
		if (str.charAt(i) != '-' || str.charAt(i+1) != '>') {
			System.err.println("Erro de sintaxe\nA regra precisa ter o formato AR->STRING");
			return;
		}
		
		String er = str.substring(0, i++);					// Passar para o caractere >
		String subst = str.substring(++i, str.length());	// Passar para o caractere após ->
		
		sist.addRegraReescrita(er, subst);
	}

	public static String lerEntrada(String prompt) {
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(prompt);
		String entrada = "";
		try {
			entrada = buf.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Erro ao receber a entrada.");
			System.exit(2);
		}
		
		return entrada;
	}
}