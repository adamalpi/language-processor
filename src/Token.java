
public class Token {
	private TipoToken tipoToken;
	private String contenido;
	Token (TipoToken t) {
		this.tipoToken = t;

	}
	Token (TipoToken t, String c){
		this.tipoToken = t;
		this.contenido = c;
	}
	public TipoToken getTipoToken() {
		return tipoToken;
	}
	public void setTipoToken(TipoToken tipoToken) {
		this.tipoToken = tipoToken;
	}

	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String toStrin() {
		return "Token [tipoToken=" + tipoToken + ", contenido=\""
				+ contenido + "\"]";
	}
	
}
