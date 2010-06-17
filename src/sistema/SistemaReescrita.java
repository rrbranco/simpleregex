package sistema;

import java.util.Vector;

public class SistemaReescrita {
	private Vector<RegraReescrita> regras = new Vector<RegraReescrita>();
	
	public int addRegraReescrita(String expressaoRegular, String cadeiaSubstituicao) {
		regras.add(new RegraReescrita(expressaoRegular, cadeiaSubstituicao));
		return regras.size()-1;
	}
	
	public void removeRegraReescrita(int i) {
		regras.removeElementAt(i);
	}
}