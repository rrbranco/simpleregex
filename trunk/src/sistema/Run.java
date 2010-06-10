package sistema;

import sistema.expressaoRegular.gramatica.Terminal;

public class Run {
	
	public static void main(String[] args) {
		RegraReescrita regra = new RegraReescrita("a+{({b+C})}*", "/1a/2");
		Terminal t1 = new Terminal((char) 3);
		Terminal t2 = new Terminal((char) 4);
		
		System.out.println(t1.hashCode());
		System.out.println(t2.hashCode());
	}
}