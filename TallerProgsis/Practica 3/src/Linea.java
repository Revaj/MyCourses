
import java.util.StringTokenizer;

public class Linea {
	String codop, etiqueta, operando, linea, modoActual = "NULL";
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
	//Operando
	public final int inmediatoNormalNoNulo = 12;
	public final int masElementosOperando = 13;
	public final int operandoInherente = 14;
	public final int operandoInvalido = 15;
	public final int operandoFueraRangoExt = 16;
	public final int operandoFueraRangoImm16 = 17;
	public final int formatoIndexError = 18;
	public final int registroErrorIndexado = 19; 
	public final int rangoIndexAuto = 20;
	public final int formatoIndex1Error = 21;
	public final int registroErrorIndexado1 = 22;
	public final int formatoIndex2Error = 23;
	public final int registroErrorIndexado2 = 24;
	public final int rangoIndex2 = 25;
	public final int operandoFueraRangoImm8 = 26;
	public final int formatoIndexDIndirecto = 27;
	public final int registroErrorIndexDIndirecto = 28;
	public final int corchetesIndirecto = 29;
	public final int formatoIndexIndirecto = 30;
	public final int registroErrorIndexIndirecto = 31 ;
	public final int rangoIndexado2Indirecto = 32;
	public final int operandoFueraRangoRel8 = 33;
	public final int formatoRelativo8 = 34;
	public final int operandoFueraRangoRel9 = 35;
	public final int formatoRelativo9 = 36;
	public final int operandoFueraRangoRel16 = 37;
	public final int formatoRelativo16 = 38;
	public final int endConOperando = 39;
	
	
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
				numErrorCodop = 39;
			}
			//Revisar xD
			else if(codop.equalsIgnoreCase("END") && !operando.equalsIgnoreCase("null")){
				errorCodop = true;
				numErrorCodop = 39;
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
	
// Practica 3
	public void validarOperando(){
		/*Revisamos si no es un direccionamiento constante y buscamos sus posibles modos a evaluar
		 *En caso de no encontrarse activamos que existe un error */
		if(!directivaConstante && !tengoErrores() && !lineaVacia){
			for(int i = 0, j = instruccion.modos.size() ; i < j ; i++){
				if(instruccion.modos.get(i).getModoDireccion().equals("INH"))
					validaInherente();
				else if(instruccion.modos.get(i).getModoDireccion().equals("IMM"))
					validaInmediato();
				else if(instruccion.modos.get(i).getModoDireccion().equals("IMM8"))
					validaInmediato8();
				else if(instruccion.modos.get(i).getModoDireccion().equals("IMM16"))
					validaInmediato16();
				else if(instruccion.modos.get(i).getModoDireccion().equals("DIR"))
					validaDirecto();
				else if(instruccion.modos.get(i).getModoDireccion().equals("EXT"))
					validaExtendido();
				else if(instruccion.modos.get(i).getModoDireccion().equals("IDX"))
					validaIndexado();
				else if(instruccion.modos.get(i).getModoDireccion().equals("IDX1"))
					validaIndexado1();
				else if(instruccion.modos.get(i).getModoDireccion().equals("IDX2"))
					validaIndexado2();
				else if(instruccion.modos.get(i).getModoDireccion().equals("[D,IDX]"))
					validaIndexadoDIndirecto();
				else if(instruccion.modos.get(i).getModoDireccion().equals("[IDX2]"))
					validaIndexado2Indirecto();
				else if(instruccion.modos.get(i).getModoDireccion().equals("REL8"))
					validaRelativo8();
				else if(instruccion.modos.get(i).getModoDireccion().equals("REL9"))
					validaRelativo9();
				else if(instruccion.modos.get(i).getModoDireccion().equals("REL16"))
					validaRelativo16();
					
				if(!modoActual.equalsIgnoreCase("null") || tengoErrores())
					break;
			}
			
			//Si no se asigno ningun modo y si en algunas busquedas no se les asigno un error
			//Se asigna el error general
			if(modoActual.equalsIgnoreCase("null") && !errorOperando){
				errorOperando = true;
				numErrorOperando = operandoInvalido; // 15
			}
		}
	}
	
	//Inherente
	private void validaInherente(){
		if(operando.equalsIgnoreCase("NULL"))
			modoActual = "INH";
		else{
			errorOperando = true;
			numErrorOperando = operandoInherente;
		}
	}
	
	//Inmediato normal 
	//REVISAR POR FAVOR!!!!!!!!!!! XD
	private void validaInmediato(){
		if(operando.equalsIgnoreCase("Null"))
			modoActual = "IMM";
		//Si es el modo restante por lo general con los inmediatos que no son de 8 o 16 bytes
		else if(instruccion.modos.size() == 1){
			errorOperando = true;
			numErrorOperando = inmediatoNormalNoNulo; // 12
		}
			
	}
	
	//Inmediato 8 bytes
	private void validaInmediato8(){
		int num;
		String operandoSinGato;
		Convertidor pruebaOperando = new Convertidor();
		if(operando.startsWith("#")){
			operandoSinGato = operando.substring(1);
			if(operandoSinGato.matches("\\$[0-9A-Fa-f]+") || operandoSinGato.matches("\\@[0-7]+") ||
			   operandoSinGato.matches("\\%[0-1]+") || operandoSinGato.matches("[\\+|\\-]?[0-9]+")){
				num = pruebaOperando.regresaConversion(operandoSinGato);
				if(num >= -256 && num <= 255)
					modoActual = "IMM8";
				else{
					errorOperando = true;
					numErrorOperando = operandoFueraRangoImm8; // 26
				}
			}
			
		}
	}
	
	//Inmediato 16 bytes 
	private void validaInmediato16(){
		int num;
		Convertidor pruebaOperando = new Convertidor();
		String operandoSinGato;
		if(operando.startsWith("#")){
			operandoSinGato = operando.substring(1);
			if(operandoSinGato.matches("\\$[0-9A-Fa-f]+") || operandoSinGato.matches("\\@[0-7]+") ||
			   operandoSinGato.matches("\\%[0-1]+") || operandoSinGato.matches("[\\+|\\-]?[0-9]+")){
					num = pruebaOperando.regresaConversion(operandoSinGato);
				if(num >= -32768 && num <= 65535)
					modoActual = "IMM16";
				else{
					errorOperando = true;
					numErrorOperando = operandoFueraRangoImm16; // 17
				}
			}
		}
	}
	
	//Directo
	private void validaDirecto(){
		int num;
		Convertidor pruebaOperando = new Convertidor();
		if(operando.matches("\\$[0-9A-Fa-f]+") || operando.matches("\\@[0-7]+") ||
		   operando.matches("\\%[0-1]+") || operando.matches("[\\+|\\-]?[0-9]+")){
			num = pruebaOperando.regresaConversion(operando);
			if(num >= 0 && num <= 255)
				modoActual = "DIR";
		}
	}
	
	//Extendido
	private void validaExtendido(){
		int num;
		Convertidor pruebaOperando = new Convertidor();
		if(operando.matches("\\$[0-9A-Fa-f]+") || operando.matches("\\@[0-7]+") ||
		   operando.matches("\\%[0-1]+") || operando.matches("[\\+|\\-]?[0-9]+")){
				num = pruebaOperando.regresaConversion(operando);
			if(num >= -32768 && num <= 65535)
				modoActual = "EXT";
			//Si el rango esta fuera del limite ponemos que hay error ya que el limite sera lo mismo para los indexados
			else{
				errorOperando = true;
				numErrorOperando = operandoFueraRangoExt; //16
				
			}
		}
		else if(operando.matches("^[A-Za-z][A-Za-z0-9_]*") && operando.length() <= 8)
			modoActual = "EXT";
	}
	
	//Indexados de 5 bytes, acumulador o auto
	private void validaIndexado(){
		String registro, rangoOperando;
		int numRango;
		Convertidor pruebaOperando = new Convertidor();
		//Validamos primero que exista solo una coma con StringTokenizer
		StringTokenizer separaComa = new StringTokenizer(operando,",");
		if(!operando.startsWith("[") && !operando.endsWith("]")){
			switch(separaComa.countTokens()){
				
				case 1:
				//Verificamos que comience con coma para evaluar el registro, sino mandamos error general
				if(operando.startsWith(",")){
					registro = separaComa.nextToken();
					if(registro.equalsIgnoreCase("X") || registro.equalsIgnoreCase("Y") || registro.equalsIgnoreCase("SP") ||
					   registro.equalsIgnoreCase("PC"))
						modoActual = "IDX";
					else{
						errorOperando = true;
						numErrorOperando = registroErrorIndexado ; //19
					}
				}
				else{
					errorOperando = true;
					numErrorOperando = operandoInvalido; // 15
				}
				break;
				
				case 2:
					rangoOperando = separaComa.nextToken();
					registro = separaComa.nextToken();
					
				//Revisamos primero el caso de acumulador y el de 5 bits
				if(registro.equalsIgnoreCase("X") || registro.equalsIgnoreCase("Y") || registro.equalsIgnoreCase("SP") ||
				   registro.equalsIgnoreCase("PC")){
					if(rangoOperando.equalsIgnoreCase("A") || rangoOperando.equalsIgnoreCase("B") || rangoOperando.equalsIgnoreCase("D"))
						modoActual = "IDX";
					else if(rangoOperando.matches("\\$[0-9A-Fa-f]+") || rangoOperando.matches("\\@[0-7]+") ||
						    rangoOperando.matches("\\%[0-1]+") || rangoOperando.matches("[\\+|\\-]?[0-9]+")){
						numRango = pruebaOperando.regresaConversion(rangoOperando);
						if(numRango >= -16 && numRango <= 15)
							modoActual = "IDX";
					}
					//No cumple la base numerica
					else{
						errorOperando = true;
						numErrorOperando = formatoIndexError; // 18
					}
				}
				else if(registro.equalsIgnoreCase("+X") || registro.equalsIgnoreCase("X+") || registro.equalsIgnoreCase("-X") ||
						registro.equalsIgnoreCase("X-") || registro.equalsIgnoreCase("+Y") || registro.equalsIgnoreCase("Y+") ||
						registro.equalsIgnoreCase("-Y") || registro.equalsIgnoreCase("Y-") || registro.equalsIgnoreCase("+SP")||
						registro.equalsIgnoreCase("SP+") || registro.equalsIgnoreCase("-SP") || registro.equalsIgnoreCase("SP-")){
					if(rangoOperando.matches("\\$[0-9A-Fa-f]+") || rangoOperando.matches("\\@[0-7]+") ||
					   rangoOperando.matches("\\%[0-1]+") || rangoOperando.matches("[\\+|\\-]?[0-9]+")){
						numRango = pruebaOperando.regresaConversion(rangoOperando);
						if(numRango >= 1 && numRango <= 8)
							modoActual = "IDX";
						else{
							errorOperando = true;
							numErrorOperando = rangoIndexAuto ;// 20 rango de la base
						}
					}
					else{
						errorOperando = true;
						numErrorOperando = formatoIndexError;	// 18 formato de la base
					}
				}
				else{
					errorOperando = true;
					numErrorOperando = registroErrorIndexado; // 19 no cumple el registro que debe llevar
				}
				break;
				
				default:
				errorOperando = true;
				numErrorOperando = formatoIndexError;// 18
				break;
				
				
			}
		}
	}
	
	//Indexado 1, 9 bytes
	private void validaIndexado1(){
		String registro, rangoOperando;
		int numRango;
		Convertidor pruebaOperando = new Convertidor();
		//Validamos primero que exista solo una coma con StringTokenizer
		StringTokenizer separaComa = new StringTokenizer(operando,",");
		if(!operando.startsWith("[") && !operando.endsWith("]")){
			switch(separaComa.countTokens()){
				case 2:
					rangoOperando = separaComa.nextToken();
					registro = separaComa.nextToken();
					
					if(registro.equalsIgnoreCase("X") || registro.equalsIgnoreCase("Y") || registro.equalsIgnoreCase("PC") ||
					   registro.equalsIgnoreCase("SP")){
						if(rangoOperando.matches("\\$[0-9A-Fa-f]+") || rangoOperando.matches("\\@[0-7]+") ||
						   rangoOperando.matches("\\%[0-1]+") || rangoOperando.matches("[\\+|\\-]?[0-9]+")){
							numRango = pruebaOperando.regresaConversion(rangoOperando);
							if(numRango >= -256 && numRango <= 255)
								modoActual = "IDX1";
						}
						else{
							errorOperando = true;
							numErrorOperando =formatoIndex1Error; //22
						}
						
					}
					else{
						errorOperando = true;
						numErrorOperando = registroErrorIndexado1; // 22
					}
					break;
					
				default:
					errorOperando = true;
					numErrorOperando = operandoInvalido ; // 15
					break;
			}
		}
	}
	
	//Indexado 2, 16 bytes
	private void validaIndexado2(){
		String registro, rangoOperando;
		int numRango;
		Convertidor pruebaOperando = new Convertidor();
		//Validamos primero que exista solo una coma con StringTokenizer
		StringTokenizer separaComa = new StringTokenizer(operando,",");
		if(!operando.startsWith("[") && !operando.endsWith("]")){
			switch(separaComa.countTokens()){
					
				case 2:
					rangoOperando = separaComa.nextToken();
					registro = separaComa.nextToken();
					
					if(registro.equalsIgnoreCase("X") || registro.equalsIgnoreCase("Y") || registro.equalsIgnoreCase("PC") ||
					   registro.equalsIgnoreCase("SP")){
						if(rangoOperando.matches("\\$[0-9A-Fa-f]+") || rangoOperando.matches("\\@[0-7]+") ||
						   rangoOperando.matches("\\%[0-1]+") || rangoOperando.matches("[\\+|\\-]?[0-9]+")){
							numRango = pruebaOperando.regresaConversion(rangoOperando);
							if(numRango >= 0 && numRango <= 65535)
								modoActual = "IDX2";
							else{
								errorOperando = true;
								numErrorOperando = rangoIndex2; //25
							}
						}
						else{
							errorOperando = true;
							numErrorOperando =formatoIndex2Error; //23
						}
						
					}
					else{
						errorOperando = true;
						numErrorOperando = registroErrorIndexado2; // 24
					}
					break;
					
				default:
					errorOperando = true;
					numErrorOperando = operandoInvalido ; // 15
					break;
					
			}
		}
	}
	
	
//Indexado D Indirecto
	private void validaIndexadoDIndirecto(){
		String registro, rangoOperando, operandoSinCorchetes;
		StringTokenizer separador = new StringTokenizer(operando,",");
		if(operando.startsWith("[") && operando.endsWith("]") && separador.countTokens() == 2){
			//Separamos ambos corchetes
			operandoSinCorchetes = operando.substring(1);
			separador = null;
			separador = new StringTokenizer(operandoSinCorchetes, "]");
			operandoSinCorchetes = separador.nextToken();
			
			separador = null;
			//Realizamos el mismo procedimiento de los indexados
			
			separador = new StringTokenizer(operandoSinCorchetes,",");
			switch(separador.countTokens()){
				case 0:
					errorOperando = true;
					numErrorOperando = operandoInvalido ; // 15
					break;

				case 2:
					rangoOperando = separador.nextToken();
					registro = separador.nextToken();
					if(registro.equalsIgnoreCase("X") || registro.equalsIgnoreCase("Y") || registro.equalsIgnoreCase("PC") ||
					   registro.equalsIgnoreCase("SP")){
						if(rangoOperando.equalsIgnoreCase("D"))
							modoActual = "[D,IDX]";
					}
					else{
						errorOperando = true;
						numErrorOperando = registroErrorIndexDIndirecto; // 28
					}
					
					break;
					
				default:
					errorOperando = true;
					numErrorOperando = formatoIndexDIndirecto; // 27
					break;
			
			}
		}
		else if(separador.countTokens() > 2){
			errorOperando = true;
			numErrorOperando = formatoIndexIndirecto; //30
		}
		else{
			errorOperando = true;
			numErrorOperando = corchetesIndirecto; // 29
		}
		
	}

	
	//Indexado de 16 bytes Indirecto
		private void validaIndexado2Indirecto(){
			String registro, rangoOperando, operandoSinCorchetes;
			int numRango;
			Convertidor pruebaOperando = new Convertidor();
			StringTokenizer separador = new StringTokenizer(operando,",");
			if(operando.startsWith("[") && operando.endsWith("]") && separador.countTokens() == 2){
				//Separamos ambos corchetes
				operandoSinCorchetes = operando.substring(1);
				separador = new StringTokenizer(operandoSinCorchetes, "]");
				operandoSinCorchetes = separador.nextToken();
				
				separador = null;
				//Realizamos el mismo procedimiento de los indexados
				
				separador = new StringTokenizer(operandoSinCorchetes , ",");
				switch(separador.countTokens()){
					case 0:
						errorOperando = true;
						numErrorOperando = operandoInvalido ; // 15
						break;

					case 2:
						rangoOperando = separador.nextToken();
						registro = separador.nextToken();
						if(registro.equalsIgnoreCase("X") || registro.equalsIgnoreCase("Y") || registro.equalsIgnoreCase("PC") ||
						   registro.equalsIgnoreCase("SP")){
						   if(rangoOperando.matches("\\$[0-9A-Fa-f]+") || rangoOperando.matches("\\@[0-7]+") ||
							  rangoOperando.matches("\\%[0-1]+") || rangoOperando.matches("[\\+|\\-]?[0-9]+")){
								numRango = pruebaOperando.regresaConversion(rangoOperando);
								if(numRango >= -32768 && numRango <= 65535)
									modoActual = "[IDX2]";
								else{
									errorOperando = true;
									numErrorOperando = rangoIndexado2Indirecto; // 32
								}
							}
							else{
								errorOperando = true;
								numErrorOperando = formatoIndexIndirecto; //30
							}
						}
						else{
							errorOperando = true;
							numErrorOperando = registroErrorIndexIndirecto; // 31
						}
						
						break;
						
					default:
						errorOperando = true;
						numErrorOperando = formatoIndexIndirecto; // 30
						break;
				
				}
			}
			else if(separador.countTokens() > 2){
				errorOperando = true;
				numErrorOperando = formatoIndexIndirecto; //30
			}
			else{
				errorOperando = true;
				numErrorOperando = corchetesIndirecto; // 29
			}
			
		}
		
	//Relativo de 8 bytes
	private void validaRelativo8(){
		int num;
		Convertidor pruebaOperando = new Convertidor();
		if(operando.matches("\\$[0-9A-Fa-f]+") || operando.matches("\\@[0-7]+") ||
		   operando.matches("\\%[0-1]+") || operando.matches("[\\+|\\-]?[0-9]+")){
				num = pruebaOperando.regresaConversion(operando);
			if(num >= -128 && num <= 127)
				modoActual = "REL8";
			//Si el rango esta fuera del limite ponemos que hay error por los relativos
			else{
				errorOperando = true;
				numErrorOperando = operandoFueraRangoRel8; //33
			}
		}
		else if(operando.matches("^[A-Za-z][A-Za-z0-9_]*") && operando.length() <= 8)
			modoActual = "REL8";
		else{
			errorOperando = true;
			numErrorOperando = formatoRelativo8; //34
		}
	}
	
	//Relativo de 9 bytes
	private void validaRelativo9(){
		int num;
		Convertidor pruebaOperando = new Convertidor();
		if(operando.matches("\\$[0-9A-Fa-f]+") || operando.matches("\\@[0-7]+") ||
		   operando.matches("\\%[0-1]+") || operando.matches("[\\+|\\-]?[0-9]+")){
				num = pruebaOperando.regresaConversion(operando);
			if(num >= -256 && num <= 255)
				modoActual = "REL9";
			//Si el rango esta fuera del limite ponemos que hay error por los relativos
			else{
				errorOperando = true;
				numErrorOperando = operandoFueraRangoRel9; //35
			}
		}
		else if(operando.matches("^[A-Za-z][A-Za-z0-9_]*") && operando.length() <= 8)
			modoActual = "REL9";
		else{
			errorOperando = true;
			numErrorOperando = formatoRelativo9; //36
		}
	}
	
	//Relativo de 16 bytes
	private void validaRelativo16(){
		int num;
		Convertidor pruebaOperando = new Convertidor();
		if(operando.matches("\\$[0-9A-Fa-f]+") || operando.matches("\\@[0-7]+") ||
		   operando.matches("\\%[0-1]+") || operando.matches("[\\+|\\-]?[0-9]+")){
				num = pruebaOperando.regresaConversion(operando);
			if(num >= -32768 && num <= 65535)
				modoActual = "REL16";
			//Si el rango esta fuera del limite ponemos que hay error por los relativos
			else{
				errorOperando = true;
				numErrorOperando = operandoFueraRangoRel16; //37
			}
		}
		else if(operando.matches("^[A-Za-z][A-Za-z0-9_]*") && operando.length() <= 8)
			modoActual = "REL16";
		else{
			errorOperando = true;
			numErrorOperando = formatoRelativo16; //38
		}
	}
	
	
	//Separa si existiera comentarios
	public void separaComentarios(){
		StringTokenizer separador = new StringTokenizer(linea,";");
		//Nos indica si es una linea completa de comentario
		if(linea.startsWith(";"))
			esComentarioCompleto = true;
		//Nos indica si existe un comentario en nuestra linea
		else if(separador.countTokens()>=1)
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
		else if(errorOperando){
			switch(numErrorOperando){
				case inmediatoNormalNoNulo:
					error = "Modo Inmediato Normal esta llevando operando";
					break;
				
				case operandoInherente:
					error = "Instruccion de direccionamento INH esta lleva operando";
					break;
					
				case operandoInvalido:
					error = "Formato de operando no valido para ningun modo de direccionamiento";
					break;
					
				case operandoFueraRangoExt:
					error = "Operando fuera de rango para direccionamiento Extendido";
					break;
					
				case operandoFueraRangoImm16:
					error = "Operando fuera de rango para direccionamiento Inmediato";
					break;
					
				case formatoIndexError:
					error = "Formato de operando no valido para los modos indexados";
					break;
					
				case registroErrorIndexado:
					error = "Registro erroneo para los direccionamientos Indexados ";
					break;
				
				case rangoIndexAuto:
					error = "Operando fuera de rango para direccionamiento Indexado Auto";
					break;
					
				case formatoIndex1Error:
					error = "Formato de operando no valido para los modos indexados de 9 bytes";
					break;
					
				case registroErrorIndexado1:
					error = "Registro erroneo para los direccionamientos Indexados de 9 bytes";
					break;
					
				case formatoIndex2Error:
					error = "Formato de operando no valido para los modos indexados de 16 bytes";
					break;
					
				case registroErrorIndexado2:
					error = "Registro erroneo para los direccionamientos Indexados de 16 bytes";
					break;
					
				case rangoIndex2:
					error = "Operando fuera de rango para direccionamiento Indexado de 16 bytes";
					break;
					
				case formatoIndexDIndirecto:
					error = "Formato de operando no valido para los direccionamientos Indexados D Indirectos";
					break;
					
				case registroErrorIndexDIndirecto:
					error = "Registro erroneo para los direccionamientos Indexados D Indirectos";
					break;
					
				case corchetesIndirecto:
					error = "Falta de corchetes para el direccionamiento Indexado Indirecto";
					break;
					
				case formatoIndexIndirecto:
					error = "Formato de operando no valido para los direccionamientos Indexados Indirectos";
					break;
					
				case registroErrorIndexIndirecto:
					error = "Registro erroneo para los direccionamientos Indexados Indirectos";
					break;
					
				case rangoIndexado2Indirecto:
					error = "Operando fuera de rango para direccionamiento Indexado Indirecto de 16 bytes";
					break;
					
				case operandoFueraRangoRel8:
					error = "Operando fuera de rango para direccionamiento relativo de 8 bytes";
					break;
					
				case formatoRelativo8:
					error = "Formato de operando no valido para direccionamiento relativo de 8 bytes";
					break;
					
				case operandoFueraRangoRel9:
					error = "Operando fuera de rango para direccionamiento relativo de 9 bytes";
					break;
					
				case formatoRelativo9:
					error = "Formato de operando no valido para direccionamiento relativo de 9 bytes";
					break;
					
				case operandoFueraRangoRel16:
					error = "Operando fuera de rango para direccionamiento relativo de 16 bytes";
					break;
					
				case formatoRelativo16:
					error = "Formato de operando no valido para direccionamiento relativo de 16 bytes";
					break;
					
				case operandoFueraRangoImm8:
					error = "Operando fuera de rango para direccionamiento inmediato de 8 bytes";
					break;
					
				case endConOperando:
					error = "Directiva END no debe llevar operando";
					break;
				}
		}
		
		//Regresamos mensaje de error
		return error;
		
	}

}
