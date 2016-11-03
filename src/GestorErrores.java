import java.io.IOException;

public class GestorErrores{

	private String errores="";
	private Lexico lexico;
	
	public void setLexico(Lexico lexico) {
		this.lexico = lexico;
	}
	public String getErrores(){
		return errores;
	}
	public void errorLexico(Lexico lexico){
		try {
			lexico.getBuffer().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		errores=errores+"Error en generacion de tokens linea " + lexico.contlinea+" debido a caracter no admitido.\n";
	
	}
	public void errorSintactico(Sintactico sintactico, Token token){
		errores=errores+ "Error sintactico por Token " + token.getTipoToken() + "  no esperado en linea " + lexico.contlinea +"\n";
		try {
			sintactico.bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void errorSintactico(Token token){
		errores=errores+ "Error sintactico por Token " + token.getTipoToken() + "  no esperado en linea " + lexico.contlinea +"\n";
	}
	
	public void errorSintactico(Sintactico sintactico){
		errores=errores+ "Error sintactico por Objeto en pila no admitido en linea "+ lexico.contlinea + "\n";
		try {
			sintactico.bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void errorSemantico(){
		errores=errores+ "Error semantico por tipos erroneos en linea " + lexico.contlinea + "\n";
	}
	
}
