import java.util.LinkedList;

public class NoTerminal {
	private NoTerminales simbolo;
	private LinkedList<TipoId> tipo;
	private int ancho;
	
	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	NoTerminal(NoTerminales simbolo)
	{
		this.simbolo = simbolo;
		this.tipo = new LinkedList<TipoId>();
		this.ancho = 0;
	}

	public NoTerminales getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(NoTerminales simbolo) {
		this.simbolo = simbolo;
	}

	public LinkedList<TipoId> getTipo() {
		return tipo;
	}

	public void setTipo(LinkedList<TipoId> tipo) {
		this.tipo = tipo;
	}
	
	public void toStrin() {
		System.out.println( "[NoTerminal: " + simbolo + ", tipo: " + tipo.toString() + "]");
	}
}
