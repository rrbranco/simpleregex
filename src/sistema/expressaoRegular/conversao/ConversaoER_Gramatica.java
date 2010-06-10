package sistema.expressaoRegular.conversao;

import sistema.expressaoRegular.ExpressaoRegular;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.TerminalCoringa;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;
import sistema.expressaoRegular.parser.TabelaLL1.TabelaLL1;

/**
 * Gramática de expressões regulares válidas
 * 
 * S->A|B|C|D|T
 * A->A+F|F+F
 * B->BG|GG
 * C->(A)*|(B)*|(C)*|(D)*|T*
 * D->(A)~|(B)~|(C)~|(D)~|T~
 * F->B|C|D|T
 * G->(A)|C|D|T
 * T->a				(a = terminal ou epsilon)
 * 
 * Gramática sem recursão à esquerda
 * 
 * S->A|B|C|D|T
 * A->F+F|F+FX
 * X->+FX|+F
 * B->GG|GGY
 * Y->G|GY
 * C->(A)*|(B)*|(C)*|(D)*|T*
 * D->(A)~|(B)~|(C)~|(D)~|T~
 * F->B|C|D|T
 * G->(A)|C|D|T
 * T->a				(a = terminal ou epsilon)
 * 
 * Gramática sem recursão à esquerda com operador {}
 * 
 * S->A|B|C|D|T|{A}|{B}|{C}|{D}
 * A->F+F|F+FX|{F+F}X|F+{FX}
 * X->+FX|+F|+{FX}
 * B->GY|G{Y}
 * Y->G|GY|G{Y}
 * C->(A)*|(B)*|(C)*|(D)*|T*|({A})*|({B})*|({C})*|({D})*
 * D->(A)~|(B)~|(C)~|(D)~|T~|({A})~|({B})~|({C})~|({D})~
 * F->B|C|D|T|{B}|{C}|{D}
 * G->(A)|C|D|T|({A})|{C}|{D}
 * T->a
 * 
 * Tabela LL(1) da gramática sem recursão à esquerda
 * 
 * (S,(): A, B, C, D
 * (S,a): A, B, C, D, T
 * 
 * (A,(): F+F, F+FX
 * (A,a): F+F, F+FX
 * 
 * (X,+): +FX, +F
 * 
 * (B,(): GG, GGY
 * (B,a): GG, GGY
 * 
 * (Y,(): G, GY
 * (Y,a): G, GY
 * 
 * (C,(): (A)*, (B)*, (C)*, (D)*
 * (C,a): T*
 * 
 * (D,(): (A)~, (B)~, (C)~, (D)~
 * (D,a): T~
 * 
 * (F,(): B, C, D
 * (F,a): B, C, D, T
 * 
 * (G,(): (A), C, D
 * (G,a): C, D, T
 * 
 * (T,a): a
 * 
 * Tabela LL(1) da gramática sem recursão à esquerda com o operador {}
 * 
 * (S,(): A, B, C, D
 * (S,a): A, B, C, D, T
 * (S,{): A, B, {A}, {B}, {C}, {D}
 * 
 * (A,(): F+F, F+FX, F+{FX}
 * (A,a): F+F, F+FX, F+{FX}
 * (A,{): F+F, F+FX, {F+F}X, F+{FX}
 * 
 * (X,+): +FX, +F, +{FX}
 * 
 * (B,(): GY, G{Y}
 * (B,a): GY, G{Y}
 * (B,{): GY, G{Y}
 * 
 * (Y,(): G, GY, G{Y}
 * (Y,a): G, GY, G{Y}
 * (Y,{): G, GY, G{Y}
 * 
 * (C,(): (A)*, (B)*, (C)*, (D)*, ({A})*, ({B})*, ({C})*, ({D})*
 * (C,a): T*
 * 
 * (D,(): (A)~, (B)~, (C)~, (D)~, ({A})~, ({B})~, ({C})~, ({D})~
 * (D,a): T~
 * 
 * (F,(): B, C, D
 * (F,a): B, C, D, T
 * (F,{): B, {B}, {C}, {D}
 * 
 * (G,(): (A), C, D, ({A})
 * (G,a): C, D, T, 
 * (G,{): {C}, {D}
 * 
 * (T,a): a
 */
public class ConversaoER_Gramatica {
	
	private static ParserConversor parserConversor;
	
	public static final char terminal = 'a';
	public static final char escape = '/';
	
	
	public static Gramatica criarGramaticaDeExpressaoRegular(ExpressaoRegular expressaoRegular) {
		parserConversor.iniciar(expressaoRegular._ER);
		
		Nodo derivacao = parserConversor.getNextDerivacao();
		// Caso não exista uma derivação
		if (derivacao == null) {
			System.err.println("Erro de Sintaxe\nER errada: " + expressaoRegular._ER);
			System.exit(1);
		}
		
		/**
		 * Algorítmo de conversão de ER para gramática
		 */
		Gramatica gramaticaER = new Gramatica();
		
		
		
		parserConversor.limpar();			// Limpar último parseamento
		return gramaticaER;
	}
	
	/**
	 * Criação da gramática e tabela LL(1) para conversão de expressão regular
	 */
	static {
		
		/**
		 * Criando a gramática para a expressão regular
		 */
		Gramatica _g = new Gramatica();
		_g.addTerminal(new Terminal('('));
		_g.addTerminal(new Terminal(')'));
		_g.addTerminal(new Terminal('{'));
		_g.addTerminal(new Terminal('}'));
		_g.addTerminal(new Terminal('+'));
		_g.addTerminal(new Terminal('*'));
		_g.addTerminal(new Terminal('~'));
		_g.addTerminal(new TerminalCoringa(terminal));
		
		Variavel S = new Variavel();
		Variavel A = new Variavel();
		Variavel X = new Variavel();
		Variavel B = new Variavel();
		Variavel Y = new Variavel();
		Variavel C = new Variavel();
		Variavel D = new Variavel();
		Variavel F = new Variavel();
		Variavel G = new Variavel();
		Variavel T = new Variavel();
		_g.addVariavel(S); _g.addVariavel(A);  _g.addVariavel(X); _g.addVariavel(B);  _g.addVariavel(Y); _g.addVariavel(C); _g.addVariavel(D);  _g.addVariavel(F);  _g.addVariavel(G);  _g.addVariavel(T);
		_g._VariavelInicial = S;
		
		Producao S1 = new Producao(S);S1.addSimboloCorpo(A);
		Producao S2 = new Producao(S);S2.addSimboloCorpo(B);
		Producao S3 = new Producao(S);S3.addSimboloCorpo(C);
		Producao S4 = new Producao(S);S4.addSimboloCorpo(D);
		Producao S5 = new Producao(S);S5.addSimboloCorpo(T);
		Producao S6 = new Producao(S);S6.addSimboloCorpo(_g.getTerminal('{'));S6.addSimboloCorpo(A);S6.addSimboloCorpo(_g.getTerminal('}'));
		Producao S7 = new Producao(S);S7.addSimboloCorpo(_g.getTerminal('{'));S7.addSimboloCorpo(B);S7.addSimboloCorpo(_g.getTerminal('}'));
		Producao S8 = new Producao(S);S8.addSimboloCorpo(_g.getTerminal('{'));S8.addSimboloCorpo(C);S8.addSimboloCorpo(_g.getTerminal('}'));
		Producao S9 = new Producao(S);S9.addSimboloCorpo(_g.getTerminal('{'));S9.addSimboloCorpo(D);S9.addSimboloCorpo(_g.getTerminal('}'));
		
		Producao A1 = new Producao(A);A1.addSimboloCorpo(F);A1.addSimboloCorpo(_g.getTerminal('+'));A1.addSimboloCorpo(F);
		Producao A2 = new Producao(A);A2.addSimboloCorpo(F);A2.addSimboloCorpo(_g.getTerminal('+'));A2.addSimboloCorpo(F);A2.addSimboloCorpo(X);
		Producao A3 = new Producao(A);A3.addSimboloCorpo(_g.getTerminal('{'));A3.addSimboloCorpo(F);A3.addSimboloCorpo(_g.getTerminal('+'));A3.addSimboloCorpo(F);A3.addSimboloCorpo(_g.getTerminal('}'));A3.addSimboloCorpo(X);
		Producao A4 = new Producao(A);A4.addSimboloCorpo(F);A4.addSimboloCorpo(_g.getTerminal('+'));A4.addSimboloCorpo(_g.getTerminal('{'));A4.addSimboloCorpo(F);A4.addSimboloCorpo(X);A4.addSimboloCorpo(_g.getTerminal('}'));
		
		Producao X1 = new Producao(X);X1.addSimboloCorpo(_g.getTerminal('+'));X1.addSimboloCorpo(F);X1.addSimboloCorpo(X);
		Producao X2 = new Producao(X);X2.addSimboloCorpo(_g.getTerminal('+'));X2.addSimboloCorpo(F);
		Producao X3 = new Producao(X);X3.addSimboloCorpo(_g.getTerminal('+'));X3.addSimboloCorpo(_g.getTerminal('{'));X3.addSimboloCorpo(F);X3.addSimboloCorpo(X);X3.addSimboloCorpo(_g.getTerminal('}'));
		
		Producao B1 = new Producao(B);B1.addSimboloCorpo(G);B1.addSimboloCorpo(Y);
		Producao B2 = new Producao(B);B2.addSimboloCorpo(G);B2.addSimboloCorpo(_g.getTerminal('{'));B2.addSimboloCorpo(Y);B2.addSimboloCorpo(_g.getTerminal('}'));
		
		Producao Y1 = new Producao(Y);Y1.addSimboloCorpo(G);
		Producao Y2 = new Producao(Y);Y2.addSimboloCorpo(G);Y2.addSimboloCorpo(Y);
		Producao Y3 = new Producao(Y);Y3.addSimboloCorpo(G);Y3.addSimboloCorpo(_g.getTerminal('{'));Y3.addSimboloCorpo(Y);Y3.addSimboloCorpo(_g.getTerminal('}'));
		
		Producao C1 = new Producao(C);C1.addSimboloCorpo(_g.getTerminal('('));C1.addSimboloCorpo(A);C1.addSimboloCorpo(_g.getTerminal(')'));C1.addSimboloCorpo(_g.getTerminal('*'));
		Producao C2 = new Producao(C);C2.addSimboloCorpo(_g.getTerminal('('));C2.addSimboloCorpo(B);C2.addSimboloCorpo(_g.getTerminal(')'));C2.addSimboloCorpo(_g.getTerminal('*'));
		Producao C3 = new Producao(C);C3.addSimboloCorpo(_g.getTerminal('('));C3.addSimboloCorpo(C);C3.addSimboloCorpo(_g.getTerminal(')'));C3.addSimboloCorpo(_g.getTerminal('*'));
		Producao C4 = new Producao(C);C4.addSimboloCorpo(_g.getTerminal('('));C4.addSimboloCorpo(D);C4.addSimboloCorpo(_g.getTerminal(')'));C4.addSimboloCorpo(_g.getTerminal('*'));
		Producao C5 = new Producao(C);C5.addSimboloCorpo(T);C5.addSimboloCorpo(_g.getTerminal('*'));
		Producao C6 = new Producao(C);C6.addSimboloCorpo(_g.getTerminal('('));C6.addSimboloCorpo(_g.getTerminal('{'));C6.addSimboloCorpo(A);C6.addSimboloCorpo(_g.getTerminal('}'));C6.addSimboloCorpo(_g.getTerminal(')'));C6.addSimboloCorpo(_g.getTerminal('*'));
		Producao C7 = new Producao(C);C7.addSimboloCorpo(_g.getTerminal('('));C7.addSimboloCorpo(_g.getTerminal('{'));C7.addSimboloCorpo(B);C7.addSimboloCorpo(_g.getTerminal('}'));C7.addSimboloCorpo(_g.getTerminal(')'));C7.addSimboloCorpo(_g.getTerminal('*'));
		Producao C8 = new Producao(C);C8.addSimboloCorpo(_g.getTerminal('('));C8.addSimboloCorpo(_g.getTerminal('{'));C8.addSimboloCorpo(C);C8.addSimboloCorpo(_g.getTerminal('}'));C8.addSimboloCorpo(_g.getTerminal(')'));C8.addSimboloCorpo(_g.getTerminal('*'));
		Producao C9 = new Producao(C);C9.addSimboloCorpo(_g.getTerminal('('));C9.addSimboloCorpo(_g.getTerminal('{'));C9.addSimboloCorpo(D);C9.addSimboloCorpo(_g.getTerminal('}'));C9.addSimboloCorpo(_g.getTerminal(')'));C9.addSimboloCorpo(_g.getTerminal('*'));
		
		Producao D1 = new Producao(D);D1.addSimboloCorpo(_g.getTerminal('('));D1.addSimboloCorpo(A);D1.addSimboloCorpo(_g.getTerminal(')'));D1.addSimboloCorpo(_g.getTerminal('~'));
		Producao D2 = new Producao(D);D2.addSimboloCorpo(_g.getTerminal('('));D2.addSimboloCorpo(B);D2.addSimboloCorpo(_g.getTerminal(')'));D2.addSimboloCorpo(_g.getTerminal('~'));
		Producao D3 = new Producao(D);D3.addSimboloCorpo(_g.getTerminal('('));D3.addSimboloCorpo(C);D3.addSimboloCorpo(_g.getTerminal(')'));D3.addSimboloCorpo(_g.getTerminal('~'));
		Producao D4 = new Producao(D);D4.addSimboloCorpo(_g.getTerminal('('));D4.addSimboloCorpo(D);D4.addSimboloCorpo(_g.getTerminal(')'));D4.addSimboloCorpo(_g.getTerminal('~'));
		Producao D5 = new Producao(D);D5.addSimboloCorpo(T);D5.addSimboloCorpo(_g.getTerminal('~'));
		Producao D6 = new Producao(D);D6.addSimboloCorpo(_g.getTerminal('('));D6.addSimboloCorpo(_g.getTerminal('{'));D6.addSimboloCorpo(A);D6.addSimboloCorpo(_g.getTerminal('}'));D6.addSimboloCorpo(_g.getTerminal(')'));D6.addSimboloCorpo(_g.getTerminal('~'));
		Producao D7 = new Producao(D);D7.addSimboloCorpo(_g.getTerminal('('));D7.addSimboloCorpo(_g.getTerminal('{'));D7.addSimboloCorpo(B);D7.addSimboloCorpo(_g.getTerminal('}'));D7.addSimboloCorpo(_g.getTerminal(')'));D7.addSimboloCorpo(_g.getTerminal('~'));
		Producao D8 = new Producao(D);D8.addSimboloCorpo(_g.getTerminal('('));D8.addSimboloCorpo(_g.getTerminal('{'));D8.addSimboloCorpo(C);D8.addSimboloCorpo(_g.getTerminal('}'));D8.addSimboloCorpo(_g.getTerminal(')'));D8.addSimboloCorpo(_g.getTerminal('~'));
		Producao D9 = new Producao(D);D9.addSimboloCorpo(_g.getTerminal('('));D9.addSimboloCorpo(_g.getTerminal('{'));D9.addSimboloCorpo(D);D9.addSimboloCorpo(_g.getTerminal('}'));D9.addSimboloCorpo(_g.getTerminal(')'));D9.addSimboloCorpo(_g.getTerminal('~'));
		
		Producao F1 = new Producao(F);F1.addSimboloCorpo(B);
		Producao F2 = new Producao(F);F2.addSimboloCorpo(C);
		Producao F3 = new Producao(F);F3.addSimboloCorpo(D);
		Producao F4 = new Producao(F);F4.addSimboloCorpo(T);
		Producao F5 = new Producao(F);F5.addSimboloCorpo(_g.getTerminal('{'));F5.addSimboloCorpo(B);F5.addSimboloCorpo(_g.getTerminal('}'));
		Producao F6 = new Producao(F);F6.addSimboloCorpo(_g.getTerminal('{'));F6.addSimboloCorpo(C);F6.addSimboloCorpo(_g.getTerminal('}'));
		Producao F7 = new Producao(F);F7.addSimboloCorpo(_g.getTerminal('{'));F7.addSimboloCorpo(D);F7.addSimboloCorpo(_g.getTerminal('}'));
		
		Producao G1 = new Producao(G);G1.addSimboloCorpo(_g.getTerminal('('));G1.addSimboloCorpo(A);G1.addSimboloCorpo(_g.getTerminal(')'));
		Producao G2 = new Producao(G);G2.addSimboloCorpo(C);
		Producao G3 = new Producao(G);G3.addSimboloCorpo(D);
		Producao G4 = new Producao(G);G4.addSimboloCorpo(T);
		Producao G5 = new Producao(G);G5.addSimboloCorpo(_g.getTerminal('('));G5.addSimboloCorpo(_g.getTerminal('{'));G5.addSimboloCorpo(A);G5.addSimboloCorpo(_g.getTerminal('}'));G5.addSimboloCorpo(_g.getTerminal(')'));
		Producao G6 = new Producao(G);G6.addSimboloCorpo(_g.getTerminal('{'));G6.addSimboloCorpo(C);G6.addSimboloCorpo(_g.getTerminal('}'));
		Producao G7 = new Producao(G);G7.addSimboloCorpo(_g.getTerminal('{'));G7.addSimboloCorpo(D);G7.addSimboloCorpo(_g.getTerminal('}'));
		
		Producao T1 = new Producao(T);T1.addSimboloCorpo(_g.getTerminal(terminal));
		
		_g.addProducao(S1);_g.addProducao(S2);_g.addProducao(S3);_g.addProducao(S4);_g.addProducao(S5);_g.addProducao(S6);_g.addProducao(S7);_g.addProducao(S8);_g.addProducao(S9);
		_g.addProducao(A1);_g.addProducao(A2);_g.addProducao(A3);_g.addProducao(A4);
		_g.addProducao(X1);_g.addProducao(X2);_g.addProducao(X3);
		_g.addProducao(B1);_g.addProducao(B2);
		_g.addProducao(Y1);_g.addProducao(Y2);_g.addProducao(Y3);
		_g.addProducao(C1);_g.addProducao(C2);_g.addProducao(C3);_g.addProducao(C4);_g.addProducao(C5);_g.addProducao(C6);_g.addProducao(C7);_g.addProducao(C8);_g.addProducao(C9);
		_g.addProducao(D1);_g.addProducao(D2);_g.addProducao(D3);_g.addProducao(D4);_g.addProducao(D5);_g.addProducao(D6);_g.addProducao(D7);_g.addProducao(D8);_g.addProducao(D9);
		_g.addProducao(F1);_g.addProducao(F2);_g.addProducao(F3);_g.addProducao(F4);_g.addProducao(F5);_g.addProducao(F6);_g.addProducao(F7);
		_g.addProducao(G1);_g.addProducao(G2);_g.addProducao(G3);_g.addProducao(G4);_g.addProducao(G5);_g.addProducao(G6);_g.addProducao(G7);
		_g.addProducao(T1);
		
		/**
		 * Criando a tabela LL(1)
		 */
		TabelaLL1 _t = new TabelaLL1();
		// Adicionando as produções na tabela LL1
		_t.addProducao(S, _g.getTerminal('('), S1); _t.addProducao(S, _g.getTerminal('('), S2); _t.addProducao(S, _g.getTerminal('('), S3); _t.addProducao(S, _g.getTerminal('('), S4);
		_t.addProducao(S, _g.getTerminal(terminal), S1); _t.addProducao(S, _g.getTerminal(terminal), S2); _t.addProducao(S, _g.getTerminal(terminal), S3); _t.addProducao(S, _g.getTerminal(terminal), S4); _t.addProducao(S, _g.getTerminal(terminal), S5);
		_t.addProducao(S, _g.getTerminal('{'), S1); _t.addProducao(S, _g.getTerminal('{'), S2); _t.addProducao(S, _g.getTerminal('{'), S6); _t.addProducao(S, _g.getTerminal('{'), S7); _t.addProducao(S, _g.getTerminal('{'), S8); _t.addProducao(S, _g.getTerminal('{'), S9);
		
		_t.addProducao(A, _g.getTerminal('('), A1); _t.addProducao(A, _g.getTerminal('('), A2); _t.addProducao(A, _g.getTerminal('('), A4);
		_t.addProducao(A, _g.getTerminal(terminal), A1); _t.addProducao(A, _g.getTerminal(terminal), A2); _t.addProducao(A, _g.getTerminal(terminal), A4);
		_t.addProducao(A, _g.getTerminal('{'), A1); _t.addProducao(A, _g.getTerminal('{'), A2); _t.addProducao(A, _g.getTerminal('{'), A3); _t.addProducao(A, _g.getTerminal('{'), A4);
		
		_t.addProducao(X, _g.getTerminal('+'), X1); _t.addProducao(X, _g.getTerminal('+'), X2); _t.addProducao(X, _g.getTerminal('+'), X3);
		
		_t.addProducao(B, _g.getTerminal('('), B1); _t.addProducao(B, _g.getTerminal('('), B2);
		_t.addProducao(B, _g.getTerminal(terminal), B1); _t.addProducao(B, _g.getTerminal(terminal), B2);
		_t.addProducao(B, _g.getTerminal('{'), B1); _t.addProducao(B, _g.getTerminal('('), B2);
		
		_t.addProducao(Y, _g.getTerminal('('), Y1); _t.addProducao(Y, _g.getTerminal('('), Y2); _t.addProducao(Y, _g.getTerminal('('), Y3);
		_t.addProducao(Y, _g.getTerminal(terminal), Y1); _t.addProducao(Y, _g.getTerminal(terminal), Y2); _t.addProducao(Y, _g.getTerminal(terminal), Y3);
		_t.addProducao(Y, _g.getTerminal('{'), Y1); _t.addProducao(Y, _g.getTerminal('{'), Y2); _t.addProducao(Y, _g.getTerminal('{'), Y3);
		
		_t.addProducao(C, _g.getTerminal('('), C1); _t.addProducao(C, _g.getTerminal('('), C2); _t.addProducao(C, _g.getTerminal('('), C3); _t.addProducao(C, _g.getTerminal('('), C4); _t.addProducao(C, _g.getTerminal('('), C6); _t.addProducao(C, _g.getTerminal('('), C7); _t.addProducao(C, _g.getTerminal('('), C8); _t.addProducao(C, _g.getTerminal('('), C9);
		_t.addProducao(C, _g.getTerminal(terminal), C5);
		
		_t.addProducao(D, _g.getTerminal('('), D1); _t.addProducao(D, _g.getTerminal('('), D2); _t.addProducao(D, _g.getTerminal('('), D3); _t.addProducao(D, _g.getTerminal('('), D4); _t.addProducao(D, _g.getTerminal('('), D6); _t.addProducao(D, _g.getTerminal('('), D7); _t.addProducao(D, _g.getTerminal('('), D8); _t.addProducao(D, _g.getTerminal('('), D9);
		_t.addProducao(D, _g.getTerminal(terminal), D5);
		
		_t.addProducao(F, _g.getTerminal('('), F1); _t.addProducao(F, _g.getTerminal('('), F2); _t.addProducao(F, _g.getTerminal('('), F3);
		_t.addProducao(F, _g.getTerminal(terminal), F1); _t.addProducao(F, _g.getTerminal(terminal), F2); _t.addProducao(F, _g.getTerminal(terminal), F3); _t.addProducao(F, _g.getTerminal(terminal), F4);
		_t.addProducao(F, _g.getTerminal('{'), F1); _t.addProducao(F, _g.getTerminal('{'), F5); _t.addProducao(F, _g.getTerminal('{'), F6); _t.addProducao(F, _g.getTerminal('{'), F7);
		
		_t.addProducao(G, _g.getTerminal('('), G1); _t.addProducao(G, _g.getTerminal('('), G2); _t.addProducao(G, _g.getTerminal('('), G3); _t.addProducao(G, _g.getTerminal('('), G5);
		_t.addProducao(G, _g.getTerminal(terminal), G2); _t.addProducao(G, _g.getTerminal(terminal), G3); _t.addProducao(G, _g.getTerminal(terminal), G4);
		_t.addProducao(G, _g.getTerminal('{'), G6); _t.addProducao(G, _g.getTerminal('{'), G7);
		
		_t.addProducao(T, _g.getTerminal(terminal), T1);
		
		_g._TabLL1 = _t;
		parserConversor = new ParserConversor(_g);
	}
}