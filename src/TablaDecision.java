import java.util.Stack;


public class TablaDecision{
	private Stack<Object> pila;
	private GestorErrores GE;
	protected boolean error = false;

	TablaDecision(GestorErrores GE){
		this.GE = GE;
		this.pila = new Stack<Object>();
	}

	Stack<Object> getPila(){
		return this.pila;
	}
	public Stack<Object> buscarTablaDecisiones(NoTerminal noTerminal, Token token){
		String tipo=null;
		TipoToken tt=token.getTipoToken();
		NoTerminales nt=noTerminal.getSimbolo();
		switch (tt){
		case identificador:
			tipo = "id";
			break;
		case palabra_clave:
			tipo = token.getContenido();
			break;
		case operador:
			tipo = token.getContenido();
			break;
		case fin_sentencia:
			tipo = token.getContenido();
			break;
		case coma:
			tipo = ",";
			break;
		case llaves:
			tipo = token.getContenido();
			break;
		case parentesis:
			tipo = token.getContenido();
			break;
		case operadorAccesoObjeto:
			tipo = ".";
			break;
		case numero_entero:
			tipo = "entero";
			break;
		case logico:
			tipo ="logico";
			break;
		case cadena:
			tipo = "cadena";
			break;
		case eof:
			tipo = "eof";
			break;
		default:
			break;
		}

		switch (nt){
		case P:
			switch(tipo){
			case "const":
				pila.push(new NoTerminal(NoTerminales.D));
				pila.push(AccSem.Acc1);
				pila.push(new NoTerminal(NoTerminales.P));
				pila.push(AccSem.Acc2);
				pila.push(1);
				break;
			case "var":
				pila.push(new NoTerminal(NoTerminales.D));
				pila.push(AccSem.Acc1);
				pila.push(new NoTerminal(NoTerminales.P));
				pila.push(AccSem.Acc2);
				pila.push(1);
				break;
			case "function":
				pila.push(new NoTerminal(NoTerminales.F));
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(new NoTerminal(NoTerminales.P));
				pila.push(AccSem.Acc3);
				pila.push(2);
				break;
			case "id":
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.P1));
				pila.push(AccSem.Acc4);
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(new NoTerminal(NoTerminales.P));
				pila.push(AccSem.Acc5);
				pila.push(3);
				break;
			case "document":
				pila.push(new NoTerminal(NoTerminales.Ll));
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(new NoTerminal(NoTerminales.P));
				pila.push(AccSem.Acc6);
				pila.push(4);
				break;
			case "prompt":
				pila.push(new NoTerminal(NoTerminales.Ll));
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(new NoTerminal(NoTerminales.P));
				pila.push(AccSem.Acc6);
				pila.push(4);
				break;
			case "this":
				pila.push(new NoTerminal(NoTerminales.Ll));
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(new NoTerminal(NoTerminales.P));
				pila.push(AccSem.Acc6);
				pila.push(4);
				break;
			case "eof":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc7);
				pila.push(5);
				break;
			case "cr":
				pila.push(new Token(TipoToken.fin_sentencia, "cr"));
				pila.push(new NoTerminal(NoTerminales.P));
				pila.push(AccSem.Acc8);
				pila.push(6);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case P1:
			switch(tipo){
			case "=":
				pila.push(new NoTerminal(NoTerminales.A));
				pila.push(AccSem.Acc9);
				pila.push(7);
				break;
			case "(":
				pila.push(new NoTerminal(NoTerminales.Ll1));
				pila.push(AccSem.Acc10);
				pila.push(8);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case D:
			switch(tipo){
			case "var":
				pila.push(new Token(TipoToken.palabra_clave,"var"));
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.D1));
				pila.push(AccSem.Acc11);
				pila.push(9);
				break;
			case "const":
				pila.push(new Token(TipoToken.palabra_clave,"const"));
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new Token(TipoToken.operador,"="));
				pila.push(new NoTerminal(NoTerminales.W));
				pila.push(new NoTerminal(NoTerminales.W1));
				pila.push(AccSem.Acc12);
				pila.push(10);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case D1:
			switch (tipo){
			case ",":
				pila.push(new NoTerminal(NoTerminales.D2));
				pila.push(AccSem.Acc13);
				pila.push(11);
				break;
			case ";":
				pila.push(new NoTerminal(NoTerminales.D2));
				pila.push(AccSem.Acc13);
				pila.push(11);
				break;
			case "cr":
				pila.push(new NoTerminal(NoTerminales.D2));
				pila.push(AccSem.Acc13);
				pila.push(11);
				break;
			case "=":
				pila.push(new NoTerminal(NoTerminales.A));
				pila.push(new NoTerminal(NoTerminales.D2));
				pila.push(AccSem.Acc14);
				pila.push(12);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case D2:
			switch (tipo){
			case ",":
				pila.push(new Token(TipoToken.coma,null));
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.D1));
				pila.push(AccSem.Acc15);
				pila.push(13);
				break;
			case "cr":
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(AccSem.Acc16);
				pila.push(14);
				break;
			case ";":
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(AccSem.Acc16);
				pila.push(14);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case W:
			switch(tipo){
			case "entero":
				pila.push(new Token(TipoToken.numero_entero, null));
				pila.push(AccSem.Acc17);
				pila.push(15);
				break;
			case "cadena":
				pila.push(new Token(TipoToken.cadena, null));
				pila.push(AccSem.Acc18);
				pila.push(16);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case W1:
			switch(tipo){
			case ",":
				pila.push(new Token(TipoToken.coma,null));
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new Token(TipoToken.operador,"="));
				pila.push(new NoTerminal(NoTerminales.W));
				pila.push(new NoTerminal(NoTerminales.W1));
				pila.push(AccSem.Acc19);
				pila.push(17);
				break;
			case "cr":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "eof":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "const":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "document":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "function":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "id":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "if":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "while":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "this":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "prompt":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "return":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "var":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			case "}":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc20);
				pila.push(18);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case A:
			switch(tipo){
			case "=":
				pila.push(new Token(TipoToken.operador,"="));
				pila.push(new NoTerminal(NoTerminales.A1));
				pila.push(new NoTerminal(NoTerminales.Y));
				pila.push(AccSem.Acc21);
				pila.push(19);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case A1:
			switch(tipo){
			case "id":
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(AccSem.Acc22);
				pila.push(20);
				break;
			case "entero":
				pila.push(new Token(TipoToken.numero_entero, null));
				pila.push(AccSem.Acc23);
				pila.push(21);
				break;
			case "logico":
				pila.push(new Token(TipoToken.logico,null));
				pila.push(AccSem.Acc24);
				pila.push(22);
				break;
			case "cadena":
				pila.push(new Token(TipoToken.cadena, null));
				pila.push(AccSem.Acc25);
				pila.push(23);
				break;
			case "new":
				pila.push(new Token(TipoToken.palabra_clave,"new"));
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.Par));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(AccSem.Acc26);
				pila.push(24);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case A2:
			switch(tipo){
			case "id":
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(AccSem.Acc27);
				pila.push(25);
				break;
			case "entero":
				pila.push(new Token(TipoToken.numero_entero,null));
				pila.push(AccSem.Acc28);
				pila.push(26);
				break;
			case "logico":
				pila.push(new Token(TipoToken.logico,null));
				pila.push(AccSem.Acc29);
				pila.push(27);
				break;
			case "cadena":
				pila.push(new Token(TipoToken.cadena,null));
				pila.push(AccSem.Acc30);
				pila.push(28);
				break;
			case "new":
				pila.push(new Token(TipoToken.palabra_clave,"new"));
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(AccSem.Acc31);
				pila.push(29);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case A3:
			switch(tipo){
			case ":":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc95);
				pila.push(89);
				break;
			case ";":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc95);
				pila.push(89);
				break;
			case ",":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc95);
				pila.push(89);
				break;
			case ")":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc95);
				pila.push(89);
				break;
			case "?":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc95);
				pila.push(89);
				break;
			case "=":
				pila.push(new Token(TipoToken.operador,"="));
				pila.push(new NoTerminal(NoTerminales.A1));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc94);
				pila.push(88);
				break;
			case "cr":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc95);
				pila.push(89);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Par:
			switch(tipo){
			case "cadena":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc32);
				pila.push(30);
				break;
			case "entero":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc32);
				pila.push(30);
				break;
			case "id":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc32);
				pila.push(30);
				break;
			case "logico":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc32);
				pila.push(30);
				break;
			case "new":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc32);
				pila.push(30);
				break;
			case ")":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc33);
				pila.push(31);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Par1:
			switch(tipo){
			case ",":
				pila.push(new Token(TipoToken.coma,null));
				pila.push(new NoTerminal(NoTerminales.Par2));
				pila.push(AccSem.Acc34);
				pila.push(32);
				break;
			case ")":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc35);
				pila.push(33);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Par2:
			switch(tipo){
			case "cadena":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc36);
				pila.push(34);
				break;
			case "entero":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc36);
				pila.push(34);
				break; 
			case "id":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc36);
				pila.push(34);
				break;
			case "logico":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc36);
				pila.push(34);
				break;
			case "new":
				pila.push(new NoTerminal(NoTerminales.A2));
				pila.push(new NoTerminal(NoTerminales.Par1));
				pila.push(AccSem.Acc36);
				pila.push(34);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Ret:
			switch(tipo){
			case ";":
				pila.push(new Token(TipoToken.fin_sentencia,";"));
				pila.push(AccSem.Acc37);
				pila.push(35);
				break;
			case "cr":
				pila.push(new Token(TipoToken.fin_sentencia,"cr"));
				pila.push(AccSem.Acc38);
				pila.push(36);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Ret1:
			switch(tipo){
			case "cr":
				pila.push(new Token(TipoToken.fin_sentencia,"cr"));
				pila.push(new NoTerminal(NoTerminales.Ret1));
				pila.push(AccSem.Acc39);
				pila.push(37);
				break;
			case "{":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "}":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "var":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "const":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "document":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "prompt":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "this":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "id":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "while":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "if":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			case "return":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc40);
				pila.push(38);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Ll:
			switch(tipo){
			case "document":
				pila.push(new Token(TipoToken.palabra_clave,"document"));
				pila.push(new Token(TipoToken.operadorAccesoObjeto,null));
				pila.push(new Token(TipoToken.palabra_clave,"write"));
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.E));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(AccSem.Acc41);
				pila.push(39);
				break;
			case "prompt":
				pila.push(new Token(TipoToken.palabra_clave,"prompt"));
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.E1));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(AccSem.Acc42);
				pila.push(40);
				break;
			case "this":
				pila.push(new Token(TipoToken.palabra_clave,"this"));
				pila.push(new Token(TipoToken.operadorAccesoObjeto,null));
				pila.push(new NoTerminal(NoTerminales.Ll3));
				pila.push(new NoTerminal(NoTerminales.A3));
				pila.push(AccSem.Acc43);
				pila.push(41);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Ll1:
			switch(tipo){
			case "(":
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.Par));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(AccSem.Acc44);
				pila.push(42);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Ll2:
			switch (tipo){
			case ".":
				pila.push(new Token(TipoToken.operadorAccesoObjeto,null));
				pila.push(new NoTerminal(NoTerminales.Ll3));
				pila.push(AccSem.Acc45);
				pila.push(43);
				break;
			case "(":
				pila.push(new NoTerminal(NoTerminales.Ll1));
				pila.push(AccSem.Acc46);
				pila.push(44);
				break;
			case "!=":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			case ",":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			case "&&":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			case ")":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			case "+":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			case ":":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			case ";":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			case "?":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			case "cr":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			case "=":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc47);
				pila.push(45);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Ll3:
			switch(tipo){
			case "id":
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(AccSem.Acc48);
				pila.push(46);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case F:
			switch (tipo){
			case "function":
				pila.push(new Token(TipoToken.palabra_clave,"function"));
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(AccSem.Acc49);
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.Par));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(new NoTerminal(NoTerminales.Ret1));
				pila.push(new Token(TipoToken.llaves,"{"));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(new NoTerminal(NoTerminales.SR));
				pila.push(AccSem.Acc50);			
				pila.push(new Token(TipoToken.llaves,"}"));
				pila.push(AccSem.Acc51);
				pila.push(47);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case B:
			switch(tipo){
			case "const":
				pila.push(new NoTerminal(NoTerminales.D));
				pila.push(AccSem.Acc52);
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(AccSem.Acc53);
				pila.push(48);
				break;
			case "var":
				pila.push(new NoTerminal(NoTerminales.D));
				pila.push(AccSem.Acc52);
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(AccSem.Acc53);
				pila.push(48);
				break;
			case "document":
				pila.push(new NoTerminal(NoTerminales.Ll));
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(AccSem.Acc54);
				pila.push(49);
				break;
			case "prompt":
				pila.push(new NoTerminal(NoTerminales.Ll));
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(AccSem.Acc54);
				pila.push(49);
				break;
			case "this":
				pila.push(new NoTerminal(NoTerminales.Ll));
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(AccSem.Acc54);
				pila.push(49);
				break;
			case "id":
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.P1));
				pila.push(AccSem.Acc55);
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(AccSem.Acc56);
				pila.push(50);
				break;
			case "while":
				pila.push(new Token(TipoToken.palabra_clave,"while"));
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(AccSem.Acc96);
				pila.push(new NoTerminal(NoTerminales.Ret1));
				pila.push(new Token(TipoToken.llaves,"{"));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(new NoTerminal(NoTerminales.SR));
				pila.push(new Token(TipoToken.llaves,"}"));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(AccSem.Acc57);
				pila.push(51);
				break;
			case "if":
				pila.push(new Token(TipoToken.palabra_clave,"if"));
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(AccSem.Acc97);
				pila.push(new NoTerminal(NoTerminales.Ret1));
				pila.push(new Token(TipoToken.llaves,"{"));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(new NoTerminal(NoTerminales.SR));
				pila.push(new Token(TipoToken.llaves,"}"));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(AccSem.Acc58);
				pila.push(52);
				break;
			case "cr":
				pila.push(new NoTerminal(NoTerminales.B1));
				pila.push(AccSem.Acc59);
				pila.push(53);
				break;
			case "}":
				pila.push(new NoTerminal(NoTerminales.B1));
				pila.push(AccSem.Acc59);
				pila.push(53);
				break;
			case "return":
				pila.push(new NoTerminal(NoTerminales.B1));
				pila.push(AccSem.Acc59);
				pila.push(53);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case B1:
			switch(tipo){
			case "cr":
				pila.push(new Token(TipoToken.fin_sentencia,"cr"));
				pila.push(new NoTerminal(NoTerminales.B));
				pila.push(AccSem.Acc93);
				pila.push(87);
				break;
			case "}":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc60);
				pila.push(54);
				break;
			case "return":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc60);
				pila.push(54);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case E:
			switch(tipo){
			case "id":
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.X));
				pila.push(new NoTerminal(NoTerminales.C));
				pila.push(AccSem.Acc61);
				pila.push(55);
				break;
			case "entero":
				pila.push(new Token(TipoToken.numero_entero,null));
				pila.push(new NoTerminal(NoTerminales.Y));
				pila.push(AccSem.Acc62);
				pila.push(56);
				break;
			case "cadena":
				pila.push(new Token(TipoToken.cadena,null));
				pila.push(new NoTerminal(NoTerminales.Y));
				pila.push(AccSem.Acc63);
				pila.push(57);
				break;
			case "(":
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.Z));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(AccSem.Acc64);
				pila.push(58);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case X:
			switch(tipo){
			case "(":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.Y));
				pila.push(AccSem.Acc65);
				pila.push(59);
				break;
			case ")":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.Y));
				pila.push(AccSem.Acc65);
				pila.push(59);
				break;
			case "+":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.Y));
				pila.push(AccSem.Acc65);;
				pila.push(59);
				break;
			case ".":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.Y));
				pila.push(AccSem.Acc65);
				pila.push(59);
				break;
			case "?":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.Y));
				pila.push(AccSem.Acc65);
				pila.push(59);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Y:
			switch(tipo){
			case "+":
				pila.push(new Token(TipoToken.operador,"+"));
				pila.push(new NoTerminal(NoTerminales.E));
				pila.push(AccSem.Acc66);
				pila.push(60);
				break;
			case ")":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc67);
				pila.push(61);
				break;
			case ",":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc67);
				pila.push(61);
				break;
			case ";":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc67);
				pila.push(61);
				break;
			case "?":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc67);
				pila.push(61);
				break;
			case "cr":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc67);
				pila.push(61);
				break;
			case ":":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc67);
				pila.push(61);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case Z:
			switch(tipo){
			case "id":
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.X));
				pila.push(AccSem.Acc68);
				pila.push(62);
				break;
			case "entero":
				pila.push(new Token(TipoToken.numero_entero,null));
				pila.push(new NoTerminal(NoTerminales.Y));
				pila.push(AccSem.Acc69);
				pila.push(63);
				break;
			case "(":
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.Z));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(AccSem.Acc70);
				pila.push(64);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case E1:
			switch(tipo){
			case "cadena":
				pila.push(new Token(TipoToken.cadena,null));
				pila.push(new Token(TipoToken.coma,null));
				pila.push(new Token(TipoToken.cadena,null));
				pila.push(AccSem.Acc71);
				pila.push(65);
				break;
			case "id":
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.E2));
				pila.push(AccSem.Acc72);
				pila.push(66);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case E2:
			switch(tipo){
			case ".":
				pila.push(new Token(TipoToken.operadorAccesoObjeto,null));
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.E2));
				pila.push(AccSem.Acc73);
				pila.push(67);
				break;
			case ")":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc74);
				pila.push(68);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case S:
			switch(tipo){
			case "id":
				pila.push(new Token(TipoToken.identificador,null));
				pila.push(new NoTerminal(NoTerminales.S1));
				pila.push(AccSem.Acc75);
				pila.push(69);
				break;
			case "entero":
				pila.push(new Token(TipoToken.numero_entero,null));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc76);
				pila.push(70);
				break;
			case "(":
				pila.push(new Token(TipoToken.parentesis,"("));
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(new Token(TipoToken.parentesis,")"));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc77);
				pila.push(71);
				break;
			case "logico":
				pila.push(new Token(TipoToken.logico,null));
				pila.push(new NoTerminal(NoTerminales.T));
				pila.push(AccSem.Acc78);
				pila.push(72);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case S1:
			switch(tipo){
			case "!=":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc79);
				pila.push(73);
				break;
			case "&&":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc79);
				pila.push(73);
				break;
			case "(":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc79);
				pila.push(73);
				break;
			case ")":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc79);
				pila.push(73);
				break;
			case "+":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc79);
				pila.push(73);
				break;
			case ".":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc79);
				pila.push(73);
				break;
				//			case ",":
				//				pila.push(new NoTerminal(NoTerminales.Ll2));
				//				pila.push(new NoTerminal(NoTerminales.S2));
				//				pila.push(AccSem.Acc79);
				//				pila.push(73);
				//				break;
				//			case ":":
				//				pila.push(new NoTerminal(NoTerminales.Ll2));
				//				pila.push(new NoTerminal(NoTerminales.S2));
				//				pila.push(AccSem.Acc79);
				//				pila.push(73);
				//				break;
			case "?":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc79);
				pila.push(73);
				break;
			case "cr":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc79);
				pila.push(73);
				break;
			case ";":
				pila.push(new NoTerminal(NoTerminales.Ll2));
				pila.push(new NoTerminal(NoTerminales.S2));
				pila.push(AccSem.Acc79);
				pila.push(73);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case S2:
			switch(tipo){
			case "+":
				pila.push(new Token(TipoToken.operador,"+"));
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(AccSem.Acc80);
				pila.push(74);
				break;
			case "!=":
				pila.push(new NoTerminal(NoTerminales.T));
				pila.push(AccSem.Acc81);
				pila.push(75);
				break;
			case "&&":
				pila.push(new NoTerminal(NoTerminales.T));
				pila.push(AccSem.Acc81);
				pila.push(75);
				break;
			case ")":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc82);
				pila.push(76);
				break;
			case ",":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc82);
				pila.push(76);
				break;
			case ":":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc82);
				pila.push(76);
				break;
			case "?":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc82);
				pila.push(76);
				break;
			case "cr":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc82);
				pila.push(76);
				break;
			case ";":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc82);
				pila.push(76);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case T:
			switch(tipo){
			case "&&":
				pila.push(new Token(TipoToken.operador,"&&"));
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(AccSem.Acc83);
				pila.push(77);
				break;
			case "!=":
				pila.push(new Token(TipoToken.operador,"!="));
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(AccSem.Acc84);
				pila.push(78);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case SR:
			switch(tipo){
			case "return":
				pila.push(new Token(TipoToken.palabra_clave,"return"));
				pila.push(new NoTerminal(NoTerminales.R));
				pila.push(new NoTerminal(NoTerminales.Ret));
				pila.push(AccSem.Acc85);
				pila.push(79);
				break;
			case "}":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc86);
				pila.push(80);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case R:
			switch(tipo){
			case "(":
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(new NoTerminal(NoTerminales.C));
				pila.push(AccSem.Acc87);
				pila.push(81);
				break;
			case "entero":
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(new NoTerminal(NoTerminales.C));
				pila.push(AccSem.Acc87);
				pila.push(81);
				break;
			case "id":
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(new NoTerminal(NoTerminales.C));
				pila.push(AccSem.Acc87);
				pila.push(81);
				break;
			case "logico":
				pila.push(new NoTerminal(NoTerminales.S));
				pila.push(new NoTerminal(NoTerminales.C));
				pila.push(AccSem.Acc87);
				pila.push(81);
				break;
			case "cadena":
				pila.push(new Token(TipoToken.cadena,null));
				pila.push(AccSem.Acc88);
				pila.push(82);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case C:
			switch(tipo){
			case "?":
				pila.push(new Token(TipoToken.operador,"?"));
				pila.push(new NoTerminal(NoTerminales.C1));
				pila.push(new Token(TipoToken.operador,":"));
				pila.push(new NoTerminal(NoTerminales.C1));
				pila.push(AccSem.Acc89);
				pila.push(83);
				break;
			case "cr":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc90);
				pila.push(84);
				break;
			case ";":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc90);
				pila.push(84);
				break;
			case ")":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc90);
				pila.push(84);
				break;
			case ",":
				pila.push(new Token(TipoToken.lambda,null));
				pila.push(AccSem.Acc90);
				pila.push(84);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		case C1:
			switch(tipo){
			case "document":
				pila.push(new NoTerminal(NoTerminales.Ll));
				pila.push(AccSem.Acc91);
				pila.push(85);
				break;
			case "prompt":
				pila.push(new NoTerminal(NoTerminales.Ll));
				pila.push(AccSem.Acc91);
				pila.push(85);
				break;
			case "this":
				pila.push(new NoTerminal(NoTerminales.Ll));
				pila.push(AccSem.Acc91);
				pila.push(85);
				break;
			case "(":
				pila.push(new NoTerminal(NoTerminales.E));
				pila.push(AccSem.Acc92);
				pila.push(86);
				break;
			case "entero":
				pila.push(new NoTerminal(NoTerminales.E));
				pila.push(AccSem.Acc92);
				pila.push(86);
				break;
			case "id":
				pila.push(new NoTerminal(NoTerminales.E));
				pila.push(AccSem.Acc92);
				pila.push(86);
				break;
			case "cadena":
				pila.push(new NoTerminal(NoTerminales.E));
				pila.push(AccSem.Acc92);
				pila.push(86);
				break;
			default:
				GE.errorSintactico(token);
				error = true;
			}
			break;
		default:
			GE.errorSintactico(token);
			error = true;
		}
		return this.pila;


	} 

}
