import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

public class Sintactico {
	private Stack<Object> pilaSintactico;
	private Stack<Object> pilaAux;
	//private File fichero = new File (System.getProperty("user.dir") + "\\" + "Parse.txt");
	private File fichero = new File (System.getProperty("user.dir") + "/" + "Parse.txt");
	protected Lexico lexico;
	public BufferedWriter bw;
	private GestorErrores gestorErrores;
	Token siguienteToken; //Token donde almacenaremos el siguiente token generado por el lexico
	private TablaDecision tablaDecisiones;
	private boolean errorSemantico; //Booleano que guarda si ha habido un error semantico o no

	/*Constructor del analizador sint�ctico*/
	Sintactico(Lexico lexico, GestorErrores gestorErrores){
		this.pilaSintactico = new Stack<Object>(); //Creamos la pila del sintactico
		this.pilaAux = new Stack<Object>(); //Creamos la pila auxiliar (sem�ntico)
		this.lexico = lexico;
		this.gestorErrores = gestorErrores;
		this.tablaDecisiones = new TablaDecision(gestorErrores);
		errorSemantico = false;
	}

	/*M�todo que realiza la funcionalidad del analizador sint�ctico*/
	void Ejecutar(){
		pilaSintactico.push(new Token(TipoToken.eof, null)); //Introducimos el token fin de fichero a la pila
		pilaSintactico.push(new NoTerminal(NoTerminales.P)); //Introducimos el axioma a la pila
		siguienteToken = lexico.generarToken(); //Pedimos el primero de los tokens
		Stack<?> expansion; //Cola donde se guardara la expansion de un simbolo no terminal
		try {
			//Creamos el fichero donde introduciremos la secuencia de producciones que se llevan a cabo
			fichero.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(fichero));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bw.write("Des");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Ejecucion del analizador sintactico
		do{
			//Si la cima de la pila es token, equiparamos
			if (siguienteToken.getClass().equals(pilaSintactico.peek().getClass()))
			{
				//Si el token es lambda
				if (((Token)pilaSintactico.peek()).getTipoToken().equals(TipoToken.lambda))
				{
					pilaSintactico.pop();
				}
				//Si la cima de la pila y el siguiente token son del mismo tipo
				else if (siguienteToken.getTipoToken().equals(((Token) pilaSintactico.peek()).getTipoToken()))
				{
					if (siguienteToken.getTipoToken().equals(TipoToken.identificador))
					{
						//Si el token es tipo identificador, sacamos el token de la pila
						//y metemos en la pila auxiliar el siguienteToken
						//Si el token no es del tipo identificador
						pilaSintactico.pop();
						pilaAux.push(siguienteToken);						
					}
					//Si el token es tipo numero, equiparamos
					else if (siguienteToken.getTipoToken().equals(TipoToken.numero_entero))
					{
						//Sacamos de la pila del sintactico y lo metemos en la pila Aux
						pilaSintactico.pop();
						pilaAux.push(siguienteToken);	

					}
					//Si el token es tipo cadena, equiparamos
					else if (siguienteToken.getTipoToken().equals(TipoToken.cadena))
					{
						//Sacamos de la pila del sintactico y lo metemos en la pila Aux
						pilaSintactico.pop();
						pilaAux.push(siguienteToken);	

					}
					//Si el token es tipo logico, equiparamos
					else if (siguienteToken.getTipoToken().equals(TipoToken.logico))
					{
						//Sacamos de la pila del sintactico y lo metemos en la pila Aux
						pilaSintactico.pop();
						pilaAux.push(siguienteToken);	

					}
					//Si el token es tipo coma, equiparamos
					else if (siguienteToken.getTipoToken().equals(TipoToken.coma))
					{
						//Sacamos de la pila del sintactico y lo metemos en la pila Aux
						pilaSintactico.pop();
						pilaAux.push(siguienteToken);	

					}
					//Si el token es tipo operadorAccesoObjeto, equiparamos
					else if (siguienteToken.getTipoToken().equals(TipoToken.operadorAccesoObjeto))
					{
						//Sacamos de la pila del sintactico y lo metemos en la pila Aux
						pilaSintactico.pop();
						pilaAux.push(siguienteToken);	

					}
					//Si el token es cualquiera de los otros tipos
					else
					{
						//Si la cima de la pila y el token son identicos
						if (((Token)pilaSintactico.peek()).getContenido().equals(siguienteToken.getContenido()))
						{
							//Sacamos de la pila del sintactico y lo metemos en la pila Aux
							pilaAux.push(pilaSintactico.pop());							
						} 
						//Si no son el mismo error
						else
						{
							//siguienteToken != cima pila sintactico
							gestorErrores.errorSintactico(this, siguienteToken);
							try {
								bw.close();
								return;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					siguienteToken = lexico.generarToken(); //Pedimos el siguiente de los tokens
				}
				//Si cima de la pila y siguienteToken no son del mismo tipo, error
				else
				{
					//siguienteToken != cima pila sintactico
					gestorErrores.errorSintactico(this, siguienteToken);
					try {
						bw.close();
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}					
			//Si la cima de la pila es un simbolo No Terminal
			else if (esNoTerminal(pilaSintactico.peek()))
			{
				//Sacamos de la pila del sintactico y lo metemos en la pila Aux
				NoTerminal cima = (NoTerminal) pilaSintactico.pop();
				pilaAux.push(cima);
				//Expandimos el simbolo no terminal y lo introducimos a la pila
				expansion = tablaDecisiones.buscarTablaDecisiones(cima, siguienteToken);
				if (tablaDecisiones.error || expansion.isEmpty()) 
				{
					try {
						bw.close();
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					try {
						bw.write(" " + expansion.pop());
						while(!expansion.isEmpty())
						{
							pilaSintactico.push(expansion.pop());
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//Si la cima de la pila es una accion semantica
			else if (esAccion(pilaSintactico.peek()))
			{
				Accion((AccSem) pilaSintactico.pop());
			}
			//Si la cima es un Object no admitido
			else
			{
				gestorErrores.errorSintactico(this);
				try {
					bw.close();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} while (!tablaDecisiones.error && !errorSemantico && (!(pilaSintactico.peek().getClass().equals(siguienteToken.getClass())) ||
				((Token)pilaSintactico.peek()).getTipoToken().equals(TipoToken.lambda) ||
						!siguienteToken.getTipoToken().equals(TipoToken.eof)));
		//Fin de la ejecucion
	}

	/*Metodo para comprobar si un objeto es NoTerminales*/
	private boolean esNoTerminal(Object object)
	{
		boolean retorno = false;
		if (NoTerminal.class.isInstance(object)) retorno = true;
		return retorno;
	}

	/*Metodo para comprobar si es Accion Semantica*/
	private boolean esAccion(Object accion)
	{
		return accion.getClass().equals(AccSem.Acc1.getClass());
	}

	/*Metodo para realizar una accion semantica*/
	private void Accion(AccSem accion)
	{
		int tope = pilaAux.lastIndexOf(pilaAux.peek());
		NoTerminal nT;
		NoTerminal aux;
		NoTerminal aux2;
		LinkedList<TipoId> listaAux;
		switch (accion)
		{
		case Acc1: 
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			break;
		case Acc2:
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc3:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc4:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			break;
		case Acc5:
			for (int i = 1; i<=4; i++) pilaAux.pop();
			break;
		case Acc6:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc7:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc8:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc9:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc10:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc11:
			if (!lexico.pilaTS.peek().buscaTS(((Token) pilaAux.elementAt(tope-2+1)).getContenido()))
			{
				lexico.pilaTS.peek().insertarTS(((Token) pilaAux.elementAt(tope-2+1)).getContenido());
			}
			lexico.pilaTS.peek().insertarTipo(((Token) pilaAux.elementAt(tope-2+1)).getContenido(), 
					((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			lexico.pilaTS.peek().insertarDesp(((Token) pilaAux.elementAt(tope-2+1)).getContenido(), 
					lexico.pilaDesp.peek());
			lexico.pilaDesp.setElementAt((lexico.pilaDesp.peek() + ((NoTerminal)pilaAux.elementAt(tope-1+1)).getAncho()),
					lexico.pilaDesp.lastIndexOf(lexico.pilaDesp.peek()));
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc12:
			if (!lexico.pilaTS.peek().buscaTS(((Token) pilaAux.elementAt(tope-4+1)).getContenido()))
			{
				lexico.pilaTS.peek().insertarTS(((Token) pilaAux.elementAt(tope-4+1)).getContenido());
			}
			lexico.pilaTS.peek().insertarTipo(((Token) pilaAux.elementAt(tope-4+1)).getContenido(), 
					((NoTerminal)pilaAux.elementAt(tope-2+1)).getTipo());
			lexico.pilaTS.peek().insertarDesp(((Token) pilaAux.elementAt(tope-4+1)).getContenido(), 
					lexico.pilaDesp.peek());
			lexico.pilaDesp.setElementAt((lexico.pilaDesp.peek() + ((NoTerminal)pilaAux.elementAt(tope-2+1)).getAncho()),
					lexico.pilaDesp.lastIndexOf(lexico.pilaDesp.peek()));
			listaAux = new LinkedList<TipoId>();
			nT = (NoTerminal) pilaAux.elementAt(tope-6+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope+2+1);
			listaAux.addAll(aux2.getTipo());
			listaAux.addAll(aux.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux2.getAncho());
			for (int i = 1; i<=5; i++) pilaAux.pop();
			break;
		case Acc13:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc14:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux2.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc15:
			lexico.pilaTS.peek().insertarTipo(((Token) pilaAux.elementAt(tope-2+1)).getContenido(), 
					((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			lexico.pilaTS.peek().insertarDesp(((Token) pilaAux.elementAt(tope-2+1)).getContenido(), 
					lexico.pilaDesp.peek());
			lexico.pilaDesp.setElementAt((lexico.pilaDesp.peek() + ((NoTerminal)pilaAux.elementAt(tope-1+1)).getAncho()),
					lexico.pilaDesp.lastIndexOf(lexico.pilaDesp.peek()));
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc16:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc17:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.entero);
			nT.setTipo(listaAux);
			nT.setAncho(4);
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc18:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.cadena);
			nT.setTipo(listaAux);
			nT.setAncho(siguienteToken.getContenido().length());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc19:
			lexico.pilaTS.peek().insertarTipo(((Token) pilaSintactico.elementAt(tope-4+1)).getContenido(), 
					((NoTerminal)pilaSintactico.elementAt(tope-2+1)).getTipo());
			lexico.pilaTS.peek().insertarDesp(((Token) pilaSintactico.elementAt(tope-4+1)).getContenido(), 
					lexico.pilaDesp.peek());
			lexico.pilaDesp.setElementAt((lexico.pilaDesp.peek() + ((NoTerminal)pilaSintactico.elementAt(tope-2+1)).getAncho()),
					lexico.pilaDesp.lastIndexOf(lexico.pilaDesp.peek()));
			nT = (NoTerminal) pilaAux.elementAt(tope-6+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux2.getAncho());
			for (int i = 1; i<=5; i++) pilaAux.pop();
			break;
		case Acc20:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc21:
			listaAux = new LinkedList<TipoId>();
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho()+aux2.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc22:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc23:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.entero);
			nT.setTipo(listaAux);
			nT.setAncho(4);
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc24:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.logico);
			nT.setTipo(listaAux);
			nT.setAncho(4);
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc25:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.cadena);
			nT.setTipo(listaAux);
			nT.setAncho(siguienteToken.getContenido().length());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc26:
			nT = (NoTerminal) pilaAux.elementAt(tope-6+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.objeto);
			lexico.pilaTS.peek().insertarTipo(((Token)pilaAux.elementAt((pilaAux.lastIndexOf((Token)pilaAux.peek()))-4+1)).getContenido(), listaAux);
			nT.setTipo(listaAux);
			nT.setAncho(lexico.pilaTS.peek().buscaAncho(((Token)pilaAux.elementAt((pilaAux.lastIndexOf((Token)pilaAux.peek()))-4+1)).getContenido()));
			for (int i = 1; i<=5; i++) pilaAux.pop();
			break;
		case Acc27:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc28:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.entero);
			nT.setTipo(listaAux);
			nT.setAncho(4);
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc29:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.logico);
			nT.setTipo(listaAux);
			nT.setAncho(4);
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc30:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.cadena);
			nT.setTipo(listaAux);
			nT.setAncho(siguienteToken.getContenido().length());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc31:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(lexico.pilaTS.peek().buscaTSTipo(((Token)pilaAux.elementAt((pilaAux.lastIndexOf((Token)pilaAux.peek()))-1+1)).getContenido()));
			nT.setTipo(listaAux);
			nT.setAncho(lexico.pilaTS.peek().buscaAncho(((Token)pilaAux.elementAt((pilaAux.lastIndexOf((Token)pilaAux.peek()))-1+1)).getContenido()));
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc32:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux2.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc33:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc34:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc35:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc36:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux2.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc37:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			nT.setTipo(listaAux);
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc38:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			nT.setTipo(listaAux);
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc39:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc40:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc41:
			nT = (NoTerminal) pilaAux.elementAt(tope-7+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=6; i++) pilaAux.pop();
			break;
		case Acc42:
			nT = (NoTerminal) pilaAux.elementAt(tope-5+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=4; i++) pilaAux.pop();
			break;
		case Acc43:
			nT = (NoTerminal) pilaAux.elementAt(tope-5+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=4; i++) pilaAux.pop();
			break;
		case Acc44:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc45:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc46:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc47:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc48:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc49:
			TablaSimbolos tablaFun = new TablaSimbolos();
			lexico.pilaTS.push(tablaFun);
			lexico.pilaDesp.push(0);
			break;
		case Acc50:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.funcion);
			int topeDesp = lexico.pilaDesp.lastIndexOf(lexico.pilaDesp.peek());			
			TipoModoPaso[] modoPaso = new TipoModoPaso[((NoTerminal)pilaAux.elementAt(tope-6+1)).getTipo().size()];
			for (int i = 0; i<((NoTerminal)pilaAux.elementAt(tope-6+1)).getTipo().size(); i++) modoPaso[i] = TipoModoPaso.valor;
			lexico.pilaTS.elementAt(lexico.pilaTS.lastIndexOf(lexico.pilaTS.peek())-2+1).insertarTipo(((Token) pilaAux.elementAt(tope-8+1)).getContenido(), listaAux);
			lexico.pilaTS.peek().insertarFunc(((Token) pilaAux.elementAt(tope-8+1)).getContenido(),
					((NoTerminal)pilaAux.elementAt(tope-6+1)).getTipo().size(), 
					((NoTerminal)pilaAux.elementAt(tope-6+1)).getTipo(),modoPaso, 
					((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo(),
					lexico.pilaDesp.elementAt(topeDesp-2+1),
					lexico.pilaTS.peek());
			break;
		case Acc51:
			lexico.pilaTS.pop();
			lexico.pilaDesp.pop();
			for (int i = 1; i<=10; i++) pilaAux.pop();
			break;
		case Acc52: 
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			break;
		case Acc53:
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc54:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc55:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			break;
		case Acc56:
			for (int i = 1; i<=4; i++) pilaAux.pop();
			break;
			//Corregido hasta aqui
		case Acc57:
			nT = (NoTerminal) pilaAux.elementAt(tope-11+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-4+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=10; i++) pilaAux.pop();
			break;
		case Acc58:
			nT = (NoTerminal) pilaAux.elementAt(tope-11+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-4+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=10; i++) pilaAux.pop();
			break;
		case Acc59:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc60:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc61:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(lexico.pilaTS.peek().buscaTSTipo(((Token)pilaAux.elementAt(tope-3+1)).getContenido()));
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-2+1)).getTipo());
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(lexico.pilaTS.peek().buscaAncho(((Token)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-3+1)).getContenido()) 
					+ ((NoTerminal)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-2+1)).getAncho()
					+ ((NoTerminal)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-1+1)).getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc62:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.entero);
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(4 + ((NoTerminal)pilaAux.elementAt(tope-1+1)).getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc63:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.cadena);
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(siguienteToken.getContenido().length() + ((NoTerminal)pilaAux.elementAt(tope-1+1)).getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc64:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc65:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc66:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc67:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc68:
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(lexico.pilaTS.peek().buscaTSTipo(((Token)pilaAux.elementAt(tope-2+1)).getContenido()));
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			nT.setAncho(lexico.pilaTS.peek().buscaAncho(((Token)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-2+1)).getContenido()) + ((NoTerminal)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-1+1)).getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc69:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.entero);
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(4 + ((NoTerminal)pilaAux.elementAt(tope-1+1)).getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc70:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc71:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.cadena);
			listaAux.add(TipoId.cadena);
			nT.setTipo(listaAux);
			nT.setAncho(((Token)pilaAux.elementAt(tope-1+1)).getContenido().length() + ((Token)pilaAux.elementAt(tope-3+1)).getContenido().length());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc72:
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(lexico.pilaTS.peek().buscaTSTipo(((Token)pilaAux.elementAt(tope-2+1)).getContenido()));
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			nT.setTipo(listaAux);
			nT.setAncho(lexico.pilaTS.peek().buscaAncho(((Token)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-2+1)).getContenido()) +
					((NoTerminal)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-1+1)).getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc73:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(lexico.pilaTS.peek().buscaTSTipo(((Token)pilaAux.elementAt(tope-2+1)).getContenido()));
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(lexico.pilaTS.peek().buscaAncho(((Token)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-2+1)).getContenido()) 
					+ ((NoTerminal)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-1+1)).getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc74:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc75:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(lexico.pilaTS.peek().buscaTSTipo(((Token)pilaAux.elementAt(tope-2+1)).getContenido()));
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(lexico.pilaTS.peek().buscaAncho(((Token)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-2+1)).getContenido()) 
					+ ((NoTerminal)pilaAux.elementAt(pilaAux.lastIndexOf(pilaAux.peek())-1+1)).getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc76:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.entero);
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(4 + ((NoTerminal)pilaAux.elementAt(tope-1+1)).getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc77:
			nT = (NoTerminal) pilaAux.elementAt(tope-5+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux.getAncho());
			for (int i = 1; i<=4; i++) pilaAux.pop();
			break;
		case Acc78:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.logico);
			listaAux.addAll(((NoTerminal)pilaAux.elementAt(tope-1+1)).getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(4 + ((NoTerminal)pilaAux.elementAt(tope-1+1)).getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc79:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc80:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc81:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc82:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc83:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc84:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc85:
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc86:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc87:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc88:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.cadena);
			nT.setTipo(listaAux);
			nT.setAncho(siguienteToken.getContenido().length());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc89:
			nT = (NoTerminal) pilaAux.elementAt(tope-5+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux = new LinkedList<TipoId>();
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho() + aux2.getAncho());
			for (int i = 1; i<=4; i++) pilaAux.pop();
			break;
		case Acc90:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc91:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc92:
			nT = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=1; i++) pilaAux.pop();
			break;
		case Acc93:
			nT = (NoTerminal) pilaAux.elementAt(tope-3+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-1+1);
			nT.setTipo(aux.getTipo());
			nT.setAncho(aux.getAncho());
			for (int i = 1; i<=2; i++) pilaAux.pop();
			break;
		case Acc94:
			listaAux = new LinkedList<TipoId>();
			nT = (NoTerminal) pilaAux.elementAt(tope-4+1);
			aux = (NoTerminal) pilaAux.elementAt(tope-2+1);
			aux2 = (NoTerminal) pilaAux.elementAt(tope-1+1);
			listaAux.addAll(aux.getTipo());
			listaAux.addAll(aux2.getTipo());
			nT.setTipo(listaAux);
			nT.setAncho(aux.getAncho()+aux2.getAncho());
			for (int i = 1; i<=3; i++) pilaAux.pop();
			break;
		case Acc95:
			listaAux = new LinkedList<TipoId>();
			listaAux.add(TipoId.OK);
			nT = (NoTerminal) pilaAux.elementAt(tope);
			nT.setTipo(listaAux);
			break;
		case Acc96:
			ListIterator<TipoId> lista1 = ((NoTerminal)pilaAux.elementAt(tope-2+1)).getTipo().listIterator();
			boolean error1;
			TipoId siguiente1;
			do
			{
				siguiente1 = lista1.next();
				if (siguiente1 == TipoId.logico || siguiente1 == TipoId.entero || siguiente1 == TipoId.OK) error1 = false;
				else error1 = true;

			}while(!error1 && lista1.hasNext());
			if (error1)
			{
				gestorErrores.errorSemantico();
				errorSemantico = true;
				try {
					bw.close();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case Acc97:
			ListIterator<TipoId> lista2 = ((NoTerminal)pilaAux.elementAt(tope-2+1)).getTipo().listIterator();
			boolean error2;
			TipoId siguiente2;
			do
			{
				siguiente2 = lista2.next();
				if (siguiente2 == TipoId.logico || siguiente2 == TipoId.entero || siguiente2 == TipoId.OK) error2 = false;
				else error2 = true;

			}while(!error2 && lista2.hasNext());
			if (error2)
			{
				gestorErrores.errorSemantico();
				errorSemantico = true;
				try {
					bw.close();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		}
	}
}

