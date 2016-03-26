
import java.util.StringTokenizer;

public class Linea {
	String codop, etiqueta, operando, linea;
	public boolean errorEtiqueta;
	public boolean errorCodop;
	public boolean errorOperando;
	public boolean errorLinea;
	public int tipoError;
	public int numErrorEtiqueta;
	public int numErrorCodop;
	public int numErrorOperando;
	public boolean esComentarioCompleto;
	public boolean lineaVacia;
	public boolean directivaConstante ;
	
	public Nodo instruccion;
	
	//Constantes para los errores
	public final int masTokens = 1;
	public final int posicionCodop = 2;
	public final int etiquetaGramatica = 3;
	public final int longitudEtiqueta = 4;
	public final int codopGramatica = 5;
	public final int longitudCodop = 6;
	public final int posicionTokens = 7;
	public final int masPuntosCodop = 8;
	public final int codopInexistente = 9;
	public final int llevaOperando = 10;
	public final int noLlevaOperando = 11;

	
	//Constructor
	public Linea(){
		codop = "NULL";
		etiqueta = "NULL";
		operando = "NULL";
	}
	
	//Valida Etiqueta por su expresion o longitud
	public void validarEtiqueta(){
		if(!etiqueta.matches("^[A-Za-z][A-Za-z0-9_]*") && !etiqueta.equalsIgnoreCase("null")){
			numErrorEtiqueta = 3;
			errorEtiqueta = true;
		}
		else if(etiqueta.length()>=9){
			numErrorEtiqueta = 4;
			errorEtiqueta = true;
		}
	}
	
	//Valida Codop segun su expresion,longitud o si tiene mas de un punto permitido
	public void validarCodop(Arbol tab){
		int buscaPunto, buscaMasPuntos;
		buscaPunto = codop.indexOf(".");
		if(!codop.matches("^[A-Za-z][.?A-Za-z]*") && !codop.equalsIgnoreCase("null")){
			numErrorCodop = 5;
			errorCodop = true;
		}
		else if(codop.length()>=6){
			numErrorCodop = 6;
			errorCodop = true;	
		}
		else if(buscaPunto>0 && buscaPunto<=codop.length()){
				buscaMasPuntos = codop.indexOf(".", buscaPunto+1);
				if(buscaMasPuntos>0){
					numErrorCodop = 8;
					errorCodop = true;
				}
		}
		else
			buscaTab(tab);
	}		
	
	//Practica 2
	public void buscaTab(Arbol tab){
		Nodo tabBuscar = null;
		//Directivas por default
		if(codop.equalsIgnoreCase("ORG") || codop.equalsIgnoreCase("END") || codop.equalsIgnoreCase("DB") || codop.equalsIgnoreCase("DC.B") || codop.equalsIgnoreCase("FCB") ||
		   codop.equalsIgnoreCase("DW") || codop.equalsIgnoreCase("DC.W") || codop.equalsIgnoreCase("FDB")|| codop.equalsIgnoreCase("FCC") || codop.equalsIgnoreCase("DS") || 
		   codop.equalsIgnoreCase("DS.B") || codop.equalsIgnoreCase("RMB") || codop.equalsIgnoreCase("DS.W") || codop.equalsIgnoreCase("RMW") || codop.equalsIgnoreCase("EQU")){
			//Las demas operaciones con excepcion de end, deben llevar operando por fuerzas
			if(!codop.equalsIgnoreCase("END") && operando.equalsIgnoreCase("null")){
				errorCodop = true;
				numErrorCodop = 11;
			}
			else{
				directivaConstante = true;
			}
		}
		else{
			//Buscamos en el arbol la directiva, sino se encuentra es un codop invalido
			tabBuscar = tab.buscaDirectiva(codop);
			if(tabBuscar == null){
				errorCodop = true;
				numErrorCodop = 9;
			}
			//No debe llevar operando
			else if(!operando.equalsIgnoreCase("null") && tabBuscar.modos.firstElement().getBytesCalcular() == 0){
				errorCodop = true;
				numErrorCodop = 10;
				
			}
			//Debe llevar operando
			else if(operando.equalsIgnoreCase("null") && tabBuscar.modos.firstElement().getBytesCalcular() >0){
				errorCodop = true;
				numErrorCodop = 11;
			}
			else
				instruccion = tabBuscar ;
		}
			
	}
	
	
	public void validarOperando(){
		//Este metodo sera construido para practicas posteriores
	}
	
	//Separa si existiera comentarios
	public void separaComentarios(){
		StringTokenizer separador = new StringTokenizer(linea,";");
		//Nos indica si es una linea completa de comentario
		if(linea.startsWith(";"))
			esComentarioCompleto = true;
		//Nos indica si existe un comentario en nuestra linea
		else if(separador.countTokens()>1)
			linea = separador.nextToken();
	}
	
	//Separamos la linea por tres casos, si existieran de 1-2 tokens o 
	//El caso por default cuando existen tres o mas tokens segun las condiciones de la linea
	public void separaLinea(){
		StringTokenizer separador = new StringTokenizer(linea);
			switch(separador.countTokens()){
			//Un solo token
			case 1:
				if(linea.startsWith(" ") || linea.startsWith("\t"))
					codop = separador.nextToken();
				else{
					errorLinea = true;
					tipoError = 2;
				}
				break;
			//Dos tokens
			case 2:
				if(linea.startsWith(" ") || linea.startsWith("\t")){
					codop = separador.nextToken();
					operando = separador.nextToken();
				}
				else{
					etiqueta = separador.nextToken();
					codop = separador.nextToken();
				}
				break;
				/**Caso default de tres o mas
				 *Si comienza con una letra la procesamos normal, sino y si es con espacio
				 *Revisamos el operando donde debera estar todo incluido con comillas para el caso de cadenas, sino
				 *existiera lo agregamos al archivo de errores o tambien el caso donde no termine la doble
				 *comilla con otra
				 */
			case 0:
				lineaVacia = true;
				break;
			default:
				//Revisamos el operando si fuera cadena
				if(linea.startsWith(" ") || linea.startsWith("\t")){
					codop = separador.nextToken();
					operando = separador.nextToken();
					if(!operando.startsWith("\"")){
						errorLinea = true;
						tipoError = 7;
					}
					else{
						while(separador.hasMoreTokens())
							operando += separador.nextToken();
						if(!operando.endsWith("\""))
							tipoError = 7;
					}
				}
				else{
					etiqueta = separador.nextToken();
					codop = separador.nextToken();
					operando = separador.nextToken();
					//Caso por si el operando fuera cadena
					if(separador.hasMoreTokens()){
						if(operando.startsWith("\"")){
							while(separador.hasMoreTokens())
								operando += separador.nextToken();
							if(!operando.endsWith("\""))
								tipoError = 7;
						}
						else{
							errorLinea = true;
							tipoError = 1;
						}
					}
				}
				break;
		}
	}
	
	//Devuelve si la linea tiene error
	public boolean tengoErrores(){
		if((errorLinea || errorCodop || errorOperando || errorEtiqueta) && !lineaVacia)
			return true;
		else
			return false;
	}
	
	//Devuelve etiqueta
	public String getEtiqueta(){
		return etiqueta;
	}
	
	//Devuelve codop
	public String getCodop(){
		return codop;
	}
	
	//Devuelve operando
	public String getOperando(){
		return operando;
	}	
	
	//Revisamos el error, lo incluimos en la cadena y lo mandamos a la principal 
	//Para escribirlo en el archivo de errores
	public String dameError(){
		String error = "";
		//Error comun de linea
		if(errorLinea){
			switch(tipoError){
				case masTokens:
					error = "Contiene mas tokens de los que deberia tener";
					break;
					
			case posicionCodop:
				error = "No se encuentra el codop";
					break;
					
				case posicionTokens:
					error = "Formato de linea erroneo para tres tokens";
			}
		}
		//Error comun de etiqueta
		else if(errorEtiqueta){
			switch(numErrorEtiqueta){
				case etiquetaGramatica:
					error = "Tu etiqueta no cumple el estandar de creacion";
					break;
				case longitudEtiqueta:
					error = "Etiqueta demasiado larga";
					break;
			
			}
		}
		//Error comun de codigo de operacion
		else if(errorCodop){
			switch(numErrorCodop){
				case codopGramatica:
					error = "Tu codigo de  operacion no cumple el estandar de creacion";
					break;
				case longitudCodop:
					error = "Tu codigo de operacion es demasiado largo";
					break;
				case masPuntosCodop:
					error = "El codop tiene mas de un punto";
					break;
					
				case codopInexistente:
					error = "Codop no valido para constante o Tabop";
					break;
				case llevaOperando:
					error = "Operando invalido para el codop";
					break;
					
				case noLlevaOperando:
					error = "La instruccion debe de llevar un operando";
					break;
				
			}
		}
		/**
		 * Por incluir en practicas posteriores la revision de los errores en el operando
		 */
		//Regresamos mensaje de error
		return error;
		
	}
	

}
