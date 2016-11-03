import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Lexico {
	
	protected TablaSimbolos tablaSimbolos;
	protected int contlinea=1;
	private SortedSet<Integer> setLetras;
	private SortedSet<Integer> setNumeros;
	private SortedSet<Integer> setResto;
	private Set<Integer> del;
	private Set<Integer> finSentencia;
	private int[] arrayLetras={65,66,67,68,69,70,71,72,73,74,75,76,77,
			78,79,80,81,82,83,84,85,86,87,88,89,90,97,98,99,100,101,
			102,103,104,105,106,107,108,109,110,111,112,113,114,115,
			116,117,118,119,120,121,122};
	private int[] arrayNumeros={48,49,50,51,52,53,54,55,56,57};
	private int[] arrayResto={9,10,13,32,33,35,36,37,38,39,40,42,
			43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,91,93,94,95,96,123,
			124,125,126,65,66,67,68,69,70,71,72,73,74,75,76,77,
			78,79,80,81,82,83,84,85,86,87,88,89,90,97,98,99,100,101,
			102,103,104,105,106,107,108,109,110,111,112,113,114,115,
			116,117,118,119,120,121,122};
	private GestorErrores GestorErrores;
	protected Stack<TablaSimbolos> pilaTS;
	protected Stack<Integer> pilaDesp;
	/*
	 * 9	=	tabulacion horizontal
	 * 10	=	salto de linea
	 * 13	=	cr
	 * 32	= 	espacio
	 * 33	=	!
	 * 34	=	"
	 * 35	=	#
	 * 36	=	$
	 * 37	=	%
	 * 38	=	&
	 * 39	=	'
	 * 40	=	(
	 * 41	=	)
	 * 42	=	*
	 * 43	=	+
	 * 44	=	,
	 * 45	=	-
	 * 46	=	.
	 * 47	=	/
	 * 48 to 57	->	0,1, ... ,9
	 * 58	=	:
	 * 59	=	;
	 * 60	=	<
	 * 61	=	=
	 * 62	=	>
	 * 63	=	?
	 * 64	=	@
	 * 65 to 90 -> A,B, ... ,Z
	 * 91	=	[
	 * 92	=	\
	 * 93	=	]
	 * 94	=	^
	 * 95	=	_
	 * 96	=	`
	 * 97 to 122 -> a,b, ... ,z
	 * 123	=	{
	 * 124	=	|
	 * 125	=	}
	 * 126	=	~
	 */
	private File archivo;
	private FileReader fr;
	private BufferedReader br;
	public BufferedReader getBuffer(){
		return br;
	}
	private File fichero = new File ("Lista de Tokens.txt");
	BufferedWriter bw;
	

	Lexico(String path, TablaSimbolos tablaSimbolos,GestorErrores gestor){
		this.tablaSimbolos = tablaSimbolos;
		this.GestorErrores=gestor;
		this.setLetras = new TreeSet<Integer>();
		for(int i=0;i<arrayLetras.length;i++){
			this.setLetras.add(arrayLetras[i]);
		}
		this.setNumeros = new TreeSet<Integer>();
		for(int i=0;i<arrayNumeros.length;i++){
			this.setNumeros.add(arrayNumeros[i]);
		}
		this.setResto = new TreeSet<Integer>();
		for(int i=0;i<arrayResto.length;i++){
			this.setResto.add(arrayResto[i]);
		}


		this.del = new HashSet<Integer>();
		this.del.add(9);this.del.add(32);
		this.finSentencia = new HashSet<Integer>();
		this.finSentencia.add(10);
		this.finSentencia.add(13);
		this.finSentencia.add(59);
		this.pilaTS = new Stack<TablaSimbolos>(); //Creamos la pila de Tabla de simbolos
		this.pilaDesp = new Stack<Integer>(); //Creamos la pila de Desplazamientos
		pilaTS.push(tablaSimbolos); //Introducimos la tabla de simbolos a la pila TS
		pilaDesp.push(0);
	
		
		try {
			 //Creamos el fichero donde introduciremos la secuencia de producciones que se llevan a cabo
			fichero.createNewFile();
			bw = new BufferedWriter(new FileWriter(fichero));
			bw.write("Aqui se muestran la lista de Tokens:\n");
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File (path);
			try {
				fr = new FileReader (archivo);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			br = new BufferedReader(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private int siguienteCaracter(){
		int caracter=-2;
		try {
			caracter=br.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return caracter;
	}

	public Token generarToken(){

		boolean tokenGenerado=false;
		int caracter;
		int estado=0;
		String contenido="";
		int numero = 0;
		Token token=null;
		while(!tokenGenerado){

			caracter=siguienteCaracter();
			switch(estado){
			case 0:
				if (del.contains(caracter)){
					estado=0;
				}
				else if (setLetras.contains(caracter)){
					contenido=contenido + (char)caracter;
					estado=1;
					try {
						br.mark(2);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
				else if (caracter == 34){
					estado=3;
				}
				else if (setNumeros.contains(caracter)){
					contenido = contenido + (char) caracter;
					numero = Integer.parseInt(contenido);
					estado=6;
					try {
						br.mark(2);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if (caracter==47){
					estado=8;
				}
				else if (caracter==33)
					estado=12;
				else if (caracter==38)
					estado=13;
				else if (caracter==123){
					token = new Token (TipoToken.llaves,""+(char)123);
					tokenGenerado=true;
				}
				else if (caracter==125){
					token = new Token (TipoToken.llaves,""+(char)125);
					tokenGenerado=true;
				}
				else if (caracter==40){
					token = new Token (TipoToken.parentesis,""+(char)40);
					tokenGenerado=true;
				}
				else if (caracter==41){
					token = new Token (TipoToken.parentesis,""+(char)41);
					tokenGenerado=true;
				}
				else if (caracter==43){
					token = new Token (TipoToken.operador,""+(char)43);
					tokenGenerado=true;
				}
				else if (caracter==63){
					token = new Token (TipoToken.operador,""+(char)63);
					tokenGenerado=true;
				}
				else if (caracter==58){
					token = new Token (TipoToken.operador,""+(char)58);
					tokenGenerado=true;
				}
				else if (caracter==46){
					token = new Token (TipoToken.operadorAccesoObjeto,""+(char)46);
					tokenGenerado=true;
				}
				else if (caracter==44){
					token = new Token (TipoToken.coma,""+(char)44);
					tokenGenerado=true;
				}
				else if (caracter==61){
					token = new Token (TipoToken.operador,""+(char)61);
					tokenGenerado=true;
				}
				else if (this.finSentencia.contains(caracter)){
					if (caracter == 10 || caracter == 13)
					{
						token = new Token (TipoToken.fin_sentencia,"cr");
						contlinea++;
					}
					else
						token = new Token (TipoToken.fin_sentencia,";");
					tokenGenerado = true;
				}
				else if(caracter==-1){
					token=new Token(TipoToken.eof,null);
					tokenGenerado=true;
				}
				else{
					GestorErrores.errorLexico(this);
					tokenGenerado=true;
				}

				break;
			case 1:
				if (this.setLetras.contains(caracter)
						|| setNumeros.contains(caracter)
						|| caracter == 95){
					try {
						br.mark(2);
						contenido=contenido + (char) caracter;
					} catch (IOException e) {
						e.printStackTrace();
					}
					estado=1;
				} else{
					//case 2:
					Iterator<TablaSimbolos> iteratorPilaTS = pilaTS.iterator();
					boolean auxTS;
					LinkedList<TipoId> listaTipo = new LinkedList<TipoId>();
					listaTipo.add(TipoId.reservada);
					if (contenido.equals("true") ){
						token = new Token(TipoToken.logico,""+1);
					}
					else if (contenido.equals("false")){
						token = new Token(TipoToken.logico,""+0);
					}
					/*else if(pilaTS.iterator()..buscaTS(contenido)){
						if(pilaTS.elementAt(pilaTS.indexOf(pilaTS.peek())-pilaTS.size()+1).buscaTSTipo(contenido).getFirst().equals(TipoId.reservada))
						{
							token=new Token(TipoToken.palabra_clave,contenido);
						}
						else token = new Token(TipoToken.identificador, contenido);
					}*/
					else{
						//while (iteratorPilaTS.hasNext())
						//{
							auxTS = pilaTS.peek().buscaTS(contenido);
							if (auxTS)
							{
								if (pilaTS.peek().buscaTSTipo(contenido).equals(listaTipo))
								{
									token = new Token(TipoToken.palabra_clave,contenido);
								}
								else token = new Token(TipoToken.identificador, contenido);
							}
							else 
							{
								token = new Token(TipoToken.identificador, contenido);
								pilaTS.peek().insertarTS(contenido);
							}
						//}
					}
					try {
						br.reset();
					} catch (IOException e) {
						e.printStackTrace();
					}
					tokenGenerado=true;

				}
				break;

			case 3:
				
				if (caracter == 92){
					estado=4;
				}
				else if (caracter == 34){
					//case 5:
					token = new Token(TipoToken.cadena,contenido);
					tokenGenerado=true;
				}
				else if (caracter!=-1){
					estado=3;
					contenido=contenido + (char) caracter;
					if(caracter==10 || caracter==13)
						contlinea++;
				}
				else {
					GestorErrores.errorLexico(this);
					tokenGenerado=true;
				}
				break;
			case 4:
				if (setResto.contains(caracter)){
					estado=3;
					contenido=contenido + Integer.toString(caracter);
					if(caracter==10 || caracter==13)
						contlinea++;
				}
				else{
					GestorErrores.errorLexico(this);
					tokenGenerado=true;
				}
				break;

			case 6:
				if (setNumeros.contains(caracter)){
					contenido = "" + (char) caracter;
					numero = numero*10 + Integer.parseInt(contenido);
					estado=6;
					try {
						br.mark(2);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else{
					//case 7:
					token = new Token(TipoToken.numero_entero,""+numero);
					tokenGenerado=true;
					try {
						br.reset();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;

			case 8:
				if (caracter == 47)
					estado=9;
				else if (caracter ==42)
					estado=10;
				else{ 
					GestorErrores.errorLexico(this);
					tokenGenerado=true;
				}
				break;
			case 9:
				if (caracter==10 || caracter == 13){
					estado=0;
					token = new Token (TipoToken.fin_sentencia,"cr");
					tokenGenerado=true;
					contlinea++;
				}
				else if (caracter!=-1){
					estado=9;
					if(caracter==10 || caracter==13)
						contlinea++;
				}
				else {
					token=new Token(TipoToken.eof,null);
					tokenGenerado=true;
				}
				break;
			case 10:
				if (caracter ==42)
					estado=11;
				else if (caracter!=-1){
					estado =10;
					if(caracter==10 || caracter==13)
						contlinea++;	
				}
				else{
					token=new Token(TipoToken.eof,null);
					tokenGenerado=true;
				}
				break;
			case 11:
				if (caracter==47){
					estado=0;
				}
				else if(caracter!=-1){
					estado=10;
					if(caracter==10 || caracter==13)
						contlinea++;
				}
				else {
					GestorErrores.errorLexico(this);
					tokenGenerado=true;
				}
				break;
			case 12:
				if (caracter==61){
					token = new Token (TipoToken.operador,""+(char)33+(char)61);
					tokenGenerado=true;}
				else{ 
					GestorErrores.errorLexico(this);
					tokenGenerado=true;
				}
				break;
			case 13:
				if (caracter ==38){
					token = new Token (TipoToken.operador,""+(char)38+(char)38);
					tokenGenerado=true;
				}
				else{
					GestorErrores.errorLexico(this);
					tokenGenerado=true;
				}
				break;
			default:
				GestorErrores.errorLexico(this);
				tokenGenerado=true;
			}

		}
		try {
			bw.write(token.toStrin()+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return token;
	}
	
//	public static void main(String[] args) {
//		String path="/Users/Wei/Desktop/PdL/texto1.txt";
//		Token t=null;
//		TablaSimbolos ts= new TablaSimbolos();
//		GestorErrores g= new GestorErrores();
//		Lexico l=new Lexico(path,ts,g);
//
//		while ((t=l.generarToken()).getTipoToken()!= TipoToken.eof){
//			System.out.println(t.toStrin());
//		}
//		try {
//			l.bw.close();
//			l.br.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("terminado correctamente.");
//	}
	
}
