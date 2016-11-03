import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;


public class TablaSimbolos {
	private String[] palReservadas={"while", "var", "prompt", "function", "new", "document",
			"write", "return", "if", "this", "const"};
	private Hashtable<Integer, EntradaSimbolos> tablaSimbolos;
	
	TablaSimbolos(){
		this.tablaSimbolos = new Hashtable<Integer, EntradaSimbolos>(); 
		for(String a: palReservadas){
			LinkedList<TipoId> lista = new LinkedList<TipoId>();
			lista.add(TipoId.reservada);
			this.tablaSimbolos.put(a.hashCode(), new EntradaSimbolos(a,lista,0,null,null,null,0,null));
		}
	}
	
	void insertarTS(String lex){
		this.tablaSimbolos.put(lex.hashCode(), new EntradaSimbolos(lex,new LinkedList<TipoId>(),0,new LinkedList<TipoId>(),null,new LinkedList<TipoId>(),0,null));
	}
	boolean buscaTS(String lex){
		return this.tablaSimbolos.containsKey(lex.hashCode());
	}
	LinkedList<TipoId> buscaTSTipo(String lex){
		return this.tablaSimbolos.get(lex.hashCode()).getTipo();
	}
	int buscaAncho(String lex)
	{
		return this.tablaSimbolos.get(lex.hashCode()).getDesplazamiento();
	}
	
	void insertarTipo(String lex, LinkedList<TipoId> tipo){
		EntradaSimbolos aux = this.tablaSimbolos.get(lex.hashCode());
		this.tablaSimbolos.remove(lex.hashCode());
		aux.setTipo(tipo);
		this.tablaSimbolos.put(aux.getId().hashCode(),aux);	
	}
	
	void insertarValorDevuelto(String lex, LinkedList<TipoId> tipo)
	{
		EntradaSimbolos aux = this.tablaSimbolos.get(lex.hashCode());
		this.tablaSimbolos.remove(lex.hashCode());
		aux.setValorDevuelto(tipo);
	}
	
	void insertarDesp(String lex, int desp){
		EntradaSimbolos aux = this.tablaSimbolos.get(lex.hashCode());
		this.tablaSimbolos.remove(lex.hashCode());
		aux.setDesplazamiento(desp);
		this.tablaSimbolos.put(aux.getId().hashCode(),aux);	
	}
	
	void insertarFunc(String lex, int numPar, LinkedList<TipoId> tipos, TipoModoPaso[] tipoPaso,
			LinkedList<TipoId> devuelto, int desp, TablaSimbolos alcance){
		LinkedList<TipoId> lista = new LinkedList<TipoId>();
		lista.add(TipoId.funcion);
		EntradaSimbolos aux = new EntradaSimbolos(lex,lista,numPar,tipos,tipoPaso,devuelto,desp,alcance);
		this.tablaSimbolos.put(aux.getId().hashCode(),aux);	
	}
	
	public Hashtable<Integer, EntradaSimbolos> getTablaSimbolos() {
		return tablaSimbolos;
	}
	


	public String toStrin() {
		String TS="";
		Set<Entry<Integer, EntradaSimbolos>> conjunto = this.getTablaSimbolos().entrySet();
		for (Entry<Integer, EntradaSimbolos> a: conjunto){
			TS=TS+a.getValue().toStrin();
		}
		return TS;
	}
	public void toStrin2() {
		
		Set<Entry<Integer, EntradaSimbolos>> conjunto = this.getTablaSimbolos().entrySet();
		for (Entry<Integer, EntradaSimbolos> a: conjunto){
			a.getValue().toStrin2();
		}
	}

	public static void main(String[] args) {
		TablaSimbolos TS = new TablaSimbolos();
		TS.insertarTS("WEI");
		TS.insertarTS("W_E");
		TS.insertarTS("FUN");
		LinkedList<TipoId> lista = new LinkedList<TipoId>();
		lista.add(TipoId.ERROR);
		TS.insertarTipo("WEI", lista);
		lista.removeAll(lista);
		lista.add(TipoId.cadena);
		TS.insertarTipo("W_E", lista);
		LinkedList<TipoId> tipo= new LinkedList<TipoId>();
		tipo.add(TipoId.cadena);
		tipo.add(TipoId.entero);
		TipoModoPaso[] tipoMP= {TipoModoPaso.valor, TipoModoPaso.valor};
		TablaSimbolos tsFun = new TablaSimbolos();
		LinkedList<TipoId> list = new LinkedList<TipoId>();
		list.add(TipoId.entero);
		TS.insertarFunc("FUN", 2, tipo, tipoMP, list, 8, tsFun);
		TS.insertarDesp("W_E", 4);
		TS.toStrin();
	}
}
