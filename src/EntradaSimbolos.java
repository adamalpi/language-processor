import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;


public class EntradaSimbolos {
	private String id;
	private LinkedList<TipoId> tipo;
	private int numeroParametros;
	private LinkedList<TipoId> tiposParametros;
	private TipoModoPaso[] modoPaso;
	private LinkedList<TipoId> valorDevuelto;
	private int desplazamiento;
	private TablaSimbolos alcance;


	EntradaSimbolos(String id, LinkedList<TipoId> tipo, int numeroParametros, LinkedList<TipoId> tiposParametros, TipoModoPaso[] modoPaso, LinkedList<TipoId> valorDevuelto, int desplazamiento, TablaSimbolos alcance){
		this.id = id;
		this.tipo = tipo;
		this.numeroParametros = numeroParametros;
		this.tiposParametros = tiposParametros;
		this.modoPaso = modoPaso;
		this.valorDevuelto = valorDevuelto;
		this.desplazamiento = desplazamiento;
		this.alcance = alcance;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public LinkedList<TipoId> getTipo() {
		return tipo;
	}


	public void setTipo(LinkedList<TipoId> tipo) {
		this.tipo = tipo;
	}


	public int getNumeroParametros() {
		return numeroParametros;
	}


	public void setNumeroParametros(int numeroParametros) {
		this.numeroParametros = numeroParametros;
	}


	public LinkedList<TipoId> getTiposParametros() {
		return tiposParametros;
	}


	public void setTiposParametros(LinkedList<TipoId> tiposParametros) {
		this.tiposParametros = tiposParametros;
	}


	public TipoModoPaso[] getModoPaso() {
		return modoPaso;
	}


	public void setModoPaso(TipoModoPaso[] modoPaso) {
		this.modoPaso = modoPaso;
	}


	public LinkedList<TipoId> getValorDevuelto() {
		return valorDevuelto;
	}


	public void setValorDevuelto(LinkedList<TipoId> valorDevuelto) {
		this.valorDevuelto = valorDevuelto;
	}


	public int getDesplazamiento() {
		return desplazamiento;
	}


	public void setDesplazamiento(int desplazamiento) {
		this.desplazamiento = desplazamiento;
	}


	public TablaSimbolos getAlcance() {
		return alcance;
	}


	public void setAlcance(TablaSimbolos alcance) {
		this.alcance = alcance;
	}

	public String tipoToString()
	{
		String cadena = "";
		if(this.tipo != null){
			ListIterator<TipoId> iterador = this.tipo.listIterator();
			while (iterador.hasNext())
			{
				cadena = cadena + iterador.next().toString() + ", ";
			}
		}
		return cadena;
	}

	public String tiposParametrosToString()
	{
		String cadena = "";
		ListIterator<TipoId> iterador = this.tiposParametros.listIterator();
		while (iterador.hasNext())
		{
			cadena = cadena + iterador.next().toString() + ", ";
		}
		return cadena;
	}

	public String valorDevueltoToString()
	{
		String cadena = "";
		ListIterator<TipoId> iterador = this.valorDevuelto.listIterator();
		while (iterador.hasNext())
		{
			cadena = cadena + iterador.next().toString() + ", ";
		}
		return cadena;
	}

	public String toStrin() {
		String cadena = "";
		LinkedList<TipoId> aux = new LinkedList<TipoId>();
		LinkedList<TipoId> aux1 = new LinkedList<TipoId>();
		aux.add(TipoId.funcion);
		aux1.add(TipoId.objeto);
		if (tipo == aux || tipo == aux1)
		{
			cadena = "[id=" + id + ", tipo=" + tipoToString() + ", "
					+ "numeroParametros=" + numeroParametros
					+ ", tiposParametros=" + tiposParametrosToString()
					+ "modoPaso=" + Arrays.toString(modoPaso)
					+ ", valorDevuelto=" + valorDevueltoToString() + "desplazamiento="
					+ desplazamiento +  "]\n";
		}
		else
		{
			cadena = "[id=" + id + ", tipo=" + tipoToString() + ", "
					+ "numeroParametros=" + numeroParametros
					+ ", desplazamiento="
					+ desplazamiento +  "]\n";
		}
		return cadena;
	}
	public void toStrin2() {
		System.out.println( "[id=" + id+ "]");
	}

	public static void main(String[] args) {
		EntradaSimbolos entrada;
		LinkedList<TipoId> tipo = new LinkedList<TipoId>();
		LinkedList<TipoId> tiposParametros = new LinkedList<TipoId>();
		TipoModoPaso[] modoPaso = new TipoModoPaso[1];
		LinkedList<TipoId> valorDevuelto = new LinkedList<TipoId>();
		TablaSimbolos alcance = new TablaSimbolos();
		tipo.add(TipoId.cadena);
		tiposParametros.add(TipoId.cadena);
		modoPaso[0] = TipoModoPaso.referencia;
		valorDevuelto.add(TipoId.cadena);
		entrada = new EntradaSimbolos("Pedro", tipo, 1, tiposParametros,modoPaso, valorDevuelto, 0, alcance);
		System.out.println(entrada.toStrin());
	}
}
