
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;

public class Compilador {
	public static void main(String[] args) throws IOException {
		TablaSimbolos tablaSimbolos = new TablaSimbolos();
		GestorErrores gestor=new GestorErrores();
		Lexico lexico=new Lexico(args[0], tablaSimbolos,gestor);
		//Lexico lexico=new Lexico("C:\\Users\\Diego\\Desktop\\Prueba9.txt", tablaSimbolos,gestor);
		
		//Lexico lexico = new Lexico("Prueba5.txt",tablaSimbolos, gestor);
		gestor.setLexico(lexico);
		Sintactico sintactico=new Sintactico(lexico, gestor);
		
		Writer writerErrores=null;
		Writer writerTS=null;

		try {	
			writerTS = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("Tabla de Simbolos.txt")));
			writerTS.write("\nAqui se muestran la tabla de simbolos inicial:\n");
			writerTS.write(tablaSimbolos.toStrin());
			
			writerErrores = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("Errores.txt")));
			writerErrores.write("\nAqui se muestran los errores generados:\n");
			sintactico.Ejecutar();
			
		} catch (IOException ex){
			ex.printStackTrace();
		} 
		finally {
			try{
				writerTS.write("\nAqui se muestran la tabla de simbolos final:\n");
				writerTS.write(tablaSimbolos.toStrin());
				writerErrores.write(gestor.getErrores());
				writerErrores.close();
				writerTS.close();
				lexico.bw.close();
				sintactico.bw.close();
				System.out.println("Compilacion concluida");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}


