package sistema.expressaoRegular;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;
import sistema.expressaoRegular.parser.Parser;
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
 * Tabela LL(1)
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
 */

/*
S->A|B|C|D|T
A->F+F|F+FX
X->+FX|+F
B->GG|GGY
Y->G|GY
C->(A)*|(B)*|(C)*|(D)*|T*
D->(A)~|(B)~|(C)~|(D)~|T~
F->B|C|D|T
G->(A)|C|D|T
T->a
*/
public class ConversaoER_Gramatica {
	
	public static Gramatica conversor;
	
	public static Gramatica criarGramaticaDeExpressaoRegular(ExpressaoRegular expressaoRegular) {
		Parser p = new Parser(conversor);
		p.iniciar(expressaoRegular._ER);
		Nodo derivacao = p.getNextDerivacao();
		p.limpar();
		Gramatica _g = new Gramatica();
		
		/**
		 * Algorítmo de conversão de ER para gramática
		 */
		
		return _g;
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
		_g.addTerminal(new Terminal('+'));
		_g.addTerminal(new Terminal('a'));
		
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
		Producao A1 = new Producao(A);A1.addSimboloCorpo(F);A1.addSimboloCorpo(_g.getTerminal('+'));A1.addSimboloCorpo(F);
		Producao A2 = new Producao(A);A2.addSimboloCorpo(F);A2.addSimboloCorpo(_g.getTerminal('+'));A2.addSimboloCorpo(F);A2.addSimboloCorpo(X);
		Producao X1 = new Producao(X);X1.addSimboloCorpo(_g.getTerminal('+'));X1.addSimboloCorpo(F);X1.addSimboloCorpo(X);
		Producao X2 = new Producao(X);X2.addSimboloCorpo(_g.getTerminal('+'));X2.addSimboloCorpo(F);
		Producao B1 = new Producao(B);B1.addSimboloCorpo(G);B1.addSimboloCorpo(G);
		Producao B2 = new Producao(B);B2.addSimboloCorpo(G);B2.addSimboloCorpo(G);B2.addSimboloCorpo(Y);
		Producao Y1 = new Producao(Y);Y1.addSimboloCorpo(G);
		Producao Y2 = new Producao(Y);Y2.addSimboloCorpo(G);Y2.addSimboloCorpo(Y);
		Producao C1 = new Producao(C);C1.addSimboloCorpo(_g.getTerminal('('));C1.addSimboloCorpo(A);C1.addSimboloCorpo(_g.getTerminal(')'));C1.addSimboloCorpo(_g.getTerminal('*'));
		Producao C2 = new Producao(C);C2.addSimboloCorpo(_g.getTerminal('('));C2.addSimboloCorpo(B);C2.addSimboloCorpo(_g.getTerminal(')'));C2.addSimboloCorpo(_g.getTerminal('*'));
		Producao C3 = new Producao(C);C3.addSimboloCorpo(_g.getTerminal('('));C3.addSimboloCorpo(C);C3.addSimboloCorpo(_g.getTerminal(')'));C3.addSimboloCorpo(_g.getTerminal('*'));
		Producao C4 = new Producao(C);C4.addSimboloCorpo(_g.getTerminal('('));C4.addSimboloCorpo(D);C4.addSimboloCorpo(_g.getTerminal(')'));C4.addSimboloCorpo(_g.getTerminal('*'));
		Producao C5 = new Producao(C);C5.addSimboloCorpo(T);C5.addSimboloCorpo(_g.getTerminal('*'));
		/*Producao D1 = new Producao(D);
		Producao D2 = new Producao(D);
		Producao D3 = new Producao(D);
		Producao D4 = new Producao(D);
		Producao D5 = new Producao(D);
		Producao F1 = new Producao(F);
		Producao F2 = new Producao(F);
		Producao F3 = new Producao(F);
		Producao F4 = new Producao(F);
		Producao G1 = new Producao(G);
		Producao G2 = new Producao(G);
		Producao G3 = new Producao(G);
		Producao G4 = new Producao(G);
		Producao T1 = new Producao(T);
		*/
		_g.addProducao(S1);_g.addProducao(S2);_g.addProducao(S3);/* ... */
		
		/**
		 * Criando a tabela LL(1)
		 */
		TabelaLL1 _t = new TabelaLL1();
		// Adicionando as produções na tabela LL1
		_t.addProducao(S, _g.getTerminal('('), S1);
		_t.addProducao(S, _g.getTerminal('('), S2);
		_t.addProducao(S, _g.getTerminal('('), S3);
		_t.addProducao(S, _g.getTerminal('('), S4);
		_t.addProducao(S, _g.getTerminal('a'), S1);
		_t.addProducao(S, _g.getTerminal('a'), S2);
		_t.addProducao(S, _g.getTerminal('a'), S3);
		_t.addProducao(S, _g.getTerminal('a'), S4);
		_t.addProducao(S, _g.getTerminal('a'), S5);/* .. */
		
		_g._TabLL1 = _t;
		conversor = _g;
	}
}