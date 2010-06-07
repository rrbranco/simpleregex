package sistema.expressaoRegular.parser;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Producao;

public class Nodo {
	public Producao nodo;
	public Vector<Producao> folhas = new Vector<Producao>();
	
	public Nodo(Producao obj) {
		nodo = obj;
	}
}
