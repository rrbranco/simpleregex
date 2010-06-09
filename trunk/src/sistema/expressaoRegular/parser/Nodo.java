package sistema.expressaoRegular.parser;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Simbolo;

public class Nodo {
	public Nodo _Pai;
	
	// Representação da forma sentencial do nodo atual
	public Vector<Simbolo> _FormaSentencial = new Vector<Simbolo>();
	
	// Caractere à casar
	public int pCharACasar;
	
	// Caminhos diferentes de derivação
	private Vector<Nodo> _Caminhos = new Vector<Nodo>();
	
	public Nodo(int charACasar, Simbolo formaSentencial) {
		pCharACasar = charACasar;
		_FormaSentencial.add(formaSentencial);
	}
	
	@SuppressWarnings("unchecked")
	public Nodo(Nodo pai) {
		_Pai = pai;
		pCharACasar = pai.pCharACasar;
		_FormaSentencial = (Vector<Simbolo>) pai._FormaSentencial.clone();
	}
	
	public Nodo getCaminho(int i) { return _Caminhos.elementAt(i); }
	
	public void setCaminhos(Vector<Nodo> caminhos) { _Caminhos.addAll(caminhos); }
	
	public void eliminarCaminho(int i) { _Caminhos.removeElementAt(i); }
	
	public int getMaxBacktracking() { return _Caminhos.size(); }
}