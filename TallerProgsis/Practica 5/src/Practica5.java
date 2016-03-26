import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Practica5:
 * Escribe codigo maquina de los simples
 * @author Javier Agustin Rizo Orozco
 * @version 1.1
 * @
 */



public class Practica5 {
	public String nombreArchivo;
	public String rutaPadre;
	public Arbol tab = new Arbol();
	public int contadorLoc = 0;
	Vector <EtiquetaTabSim> etiquetas = new Vector <EtiquetaTabSim>();
	//La TDA nos dira en cual tiene el error al momento de ir al paso dos
	PriorityQueue <Integer> lineaErronea = new PriorityQueue <Integer>(); 
	
	//Comprobacion si es un asm o no el archivo para analizar
	public  boolean compruebaExtension(String rutaArchivo) throws IOException{
		StringTokenizer separaExtension;
		boolean valor = false;
		File archivoComprobar  = new File(rutaArchivo);
		if(archivoComprobar.getName().toLowerCase().endsWith(".asm")){
			valor = true;
		}
		//Separamos el nombre de la extension y lo otorgamos a nombreArchivo
		separaExtension = new StringTokenizer(archivoComprobar.getName(),".");
		nombreArchivo = separaExtension.nextToken();
		rutaPadre = archivoComprobar.getParent()+"\\";
		return valor;
	}
	
	//La linea busca el error que contuviera y se imprime en el archivo
	public void escribeError(BufferedWriter error,int numeroLinea,Linea nueva)throws IOException{
		error.write("Linea "+numeroLinea+": ");
		error.write(nueva.dameError());
		error.newLine();
		error.write(".................................................");
		error.newLine();
	}
	
	public void cargaTab(){
		StringTokenizer lineaTabop = null;
		Tabop tmp = new Tabop();
		try {
			BufferedReader cargador = new BufferedReader(new FileReader ( new File ("TABOP.txt")));
			String linea;
			while((linea = cargador.readLine())!= null){
				lineaTabop = new StringTokenizer(linea,"|");
				tmp.setNombreInstruccion(lineaTabop.nextToken());
				if(Integer.parseInt(lineaTabop.nextToken()) == 0)
					tmp.setLlevaOperando(false);
				else
					tmp.setLlevaOperando(true);
				tmp.setModoDireccion(lineaTabop.nextToken());
				tmp.setCodigoMaquina(lineaTabop.nextToken());
				tmp.setBytesCalculados(Byte.valueOf(lineaTabop.nextToken()));
				tmp.setBytesCalcular(Byte.valueOf(lineaTabop.nextToken()));
				tmp.setTotalBytes(Byte.valueOf(lineaTabop.nextToken()));
				tab.insertar(tmp);
				tmp = null;
				tmp = new Tabop();
			}
			
			cargador.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Tabop no existe, carguelo!!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Rellena los 0 que les falten
	private String convierteHexa(int contador){
		String nuevo = "";
		nuevo = Integer.toHexString(contador);
		int relleno = 0;
		if(nuevo.length() < 4){
			relleno = 4-nuevo.length();
			switch(relleno){
				case 1:
					nuevo = "0" + nuevo;
					break;
					
				case 2:
					nuevo = "00" + nuevo;
					break;
					
				case 3:
					nuevo = "000" + nuevo;
					break;
				default:
					break;
			}
			
		}
			
		
		return nuevo;
		
	}
	private String convierteHexa(){
		String nuevo = "";
		nuevo = Integer.toHexString(contadorLoc);
		int relleno = 0;
		if(nuevo.length() < 4){
			relleno = 4-nuevo.length();
			switch(relleno){
				case 1:
					nuevo = "0" + nuevo;
					break;
					
				case 2:
					nuevo = "00" + nuevo;
					break;
					
				case 3:
					nuevo = "000" + nuevo;
					break;
				default:
					break;
			}
			
		}
			
		
		return nuevo;
	}
	
	//Agregamos etiquetas sino se repiten
	public boolean masEtiquetas(Linea nueva, Vector<EtiquetaTabSim> etiq){
		boolean repetida = false;
		Convertidor conv = new Convertidor();
		for(int i = 0, j= etiq.size(); i < j; i ++)
			if(etiq.elementAt(i).etiqueta.equals(nueva.etiqueta)){
				repetida = true;
			}
		if(!repetida){
		if(!nueva.codop.equalsIgnoreCase("EQU"))
			etiq.add(new EtiquetaTabSim(nueva.etiqueta, convierteHexa()));
		else
			etiq.add(new EtiquetaTabSim(nueva.etiqueta,convierteHexa(conv.regresaConversion(nueva.operando))));
		}
		return repetida;
	}
	
	
	//Con este metodo comprobamos que el contloc no sea superado y sumamos
	private boolean noExcedeRango(int operando){
		boolean sinExceder= false;
		int resultado;
		resultado = contadorLoc + operando;
		if(resultado >= 0 && resultado <= 65535)
			sinExceder = true;
		return sinExceder;
	}
	
	/**
	 * Aqui trabaja casi todo el programa, despues de comprobar, en caso de que si fuera asm y extrajimos
	 * la ruta y el nombre. Creamos tres Buffers, 1 lector y dos escritores, el primer escritor en caso
	 * de que no tenga ningun error y la linea no sea comentario completo o vacia, escribe en el nuevo archivo 
	 * donde se encuentra el archivo a leer el contenido 
	 * de la linea; caso contrario el segundo escritor procesa cual fue el error para incluirlo
	 * Al final despues del ciclo final, revisamos si la ultima linea contuvo end o no, en caso contrario el escritor
	 * de errores se encarga de procesarlo
	 * @param fuente
	 */
	
	public  void analizaArchivo(String fuente){
		Linea nueva = new Linea();
		int contador;
		String codigoContador = "";
		boolean orgEncontrado = false;
		boolean etiquetaRepetida = false;
		boolean igualar;
		Convertidor conv = new Convertidor();
		Vector <EtiquetaTabSim> etiq = new Vector<EtiquetaTabSim>();
		contador = 1;
		cargaTab();
		
		//Un try para evitar errores en los buffers
		try {
			//Instanciamos y creamos los buffers para comenzar a trabajar
			BufferedReader lector = new BufferedReader( new FileReader ( new File (fuente)));
			BufferedWriter inst = new BufferedWriter(new FileWriter(new File(rutaPadre+nombreArchivo+".inst")));
			BufferedWriter error = new BufferedWriter(new FileWriter(new File(rutaPadre+nombreArchivo+".err")));
			//Se inicia el archivo inst para las lineas sin errores
			inst.write("Linea		ContLoc		Etiqueta	Codop		Operando		Modos");
			inst.newLine();
			inst.write("...........................................................................................................................................................................................");
			inst.newLine();
			//Recibimos la linea mientras no sea fin y ademas buscamos si encontramos la directiva end
			while((nueva.linea = lector.readLine())!= null ){
				igualar = false;
				nueva.separaComentarios();
				nueva.separaLinea();
				nueva.validarCodop(tab);
				
				if(!nueva.esComentarioCompleto && !nueva.lineaVacia)
					if(nueva.codop.equalsIgnoreCase("ORG") && !orgEncontrado){
						if(nueva.cumpleOperando()){
							orgEncontrado = true;
							contadorLoc = conv.regresaConversion(nueva.operando);
							if(contadorLoc < 0 || contadorLoc > 65536){
								error.write("Linea "+contador+": Error de rango con el Org, se iniciara en 0 ");
								error.newLine();
								error.write(".................................................");
								error.newLine();
								//Procesamos la siguiente linea y realizamos un continue
								contador++;
								nueva = null;
								nueva = new Linea();
								contadorLoc = 0;
								continue;
							}
						}
						else{
							error.write("Linea "+contador+": Formato erroneo, se iniciara en 0 ");
							error.newLine();
							error.write(".................................................");
							error.newLine();
							//Procesamos la siguiente linea y realizamos un continue
							contador++;
							nueva = null;
							nueva = new Linea();
							continue;
							
						}
						
					}
					else if(!nueva.codop.equalsIgnoreCase("EQU") && !orgEncontrado){
							error.write("Linea "+contador+": No se puede iniciar el contador de localidades al no existir un ORG");
							error.newLine();
							error.write(".................................................");
							error.newLine();
							//Procesamos la siguiente linea y realizamos un continue
							contador++;
							nueva = null;
							nueva = new Linea();
							continue;
						}
					else if(nueva.codop.equalsIgnoreCase("ORG") && orgEncontrado){
						error.write("Linea "+contador+": No se permiten dos ORGS");
						error.newLine();
						error.write(".................................................");
						error.newLine();
						//Procesamos la siguiente linea y realizamos un continue
						contador++;
						nueva = null;
						nueva = new Linea();
						continue;
					}
					else if(nueva.codop.equalsIgnoreCase("EQU") && nueva.etiqueta.equalsIgnoreCase("null")){
						error.write("Linea "+contador+": El EQU no tiene etiqueta");
						error.newLine();
						error.write(".................................................");
						error.newLine();
						//Procesamos la siguiente linea y realizamos un continue
						contador++;
						nueva = null;
						nueva = new Linea();
						continue;
						
					}
	
				
				nueva.validarEtiqueta();
				nueva.validarOperando();
				if(!nueva.tengoErrores() && nueva.directivaConstante)
					nueva.validaDirectiva();
				if(!nueva.etiqueta.equalsIgnoreCase("null") && !nueva.esComentarioCompleto && !nueva.lineaVacia &&!nueva.tengoErrores()){
					//Agregamos la linea completa para el caso de equ
					etiquetaRepetida = masEtiquetas(nueva, etiq);
					if(etiquetaRepetida){
						error.write("Linea "+contador+" Esta etiqueta ya esta repetida");
						error.newLine();
						error.write(".......................................................");
						error.newLine();
						contador ++ ;
						nueva = null;
						nueva = new Linea();
						continue;
					}
					//Si es verdadero escribimos el valor actual del equ
					else if(nueva.codop.equalsIgnoreCase("EQU")){
						igualar = true;
					}
				}
				if(!nueva.operando.equalsIgnoreCase("NULL") && !nueva.operando.startsWith("\"") && !nueva.tengoErrores() && !nueva.operando.matches("^[A-Za-z][A-Za-z0-9_]*"))
					if(!nueva.esComentarioCompleto && !nueva.lineaVacia  &&!nueva.codop.equalsIgnoreCase("fcc") &&  !noExcedeRango(conv.regresaConversion(nueva.operando))) {
						error.write("Linea "+ contador + "El contador se excede del rango permitido");
						error.newLine();
						error.write("............................................................");
						error.newLine();
						contador++;
						nueva = null;
						nueva = new Linea();
						continue;
					}
				
				if(!nueva.tengoErrores() && !nueva.esComentarioCompleto && !nueva.lineaVacia)
					codigoContador = convierteHexa();
				//Comprobacion de que no tenga error la linea
				if(nueva.tengoErrores() && !nueva.esComentarioCompleto)
					escribeError(error,contador,nueva);
				//Comprobaciones finales
				else if(!nueva.esComentarioCompleto && !nueva.lineaVacia){
					inst.write(contador+"\t\t");
					if(!nueva.operando.equalsIgnoreCase("null") && !nueva.codop.equalsIgnoreCase("fcc") && !nueva.tengoErrores()  && !nueva.operando.matches("^[A-Za-z][A-Za-z0-9_]*") && !noExcedeRango(conv.regresaConversion(nueva.operando)) && !nueva.codop.equalsIgnoreCase("EQU")){
						error.write("Linea "+ contador + "El contador se excede del rango permitido");
						error.newLine();
						error.write("............................................................");
						error.newLine();
						contador++;
						nueva = null;
						nueva = new Linea();
						continue;
					}
					if(nueva.codop.equalsIgnoreCase("EQU"))
						inst.write(convierteHexa(nueva.sumaContDirectiva));
					else
						inst.write(codigoContador);
					inst.write("\t\t"+nueva.getEtiqueta()+"\t\t"+nueva.getCodop()+"\t\t"+nueva.getOperando()+"\t\t");
					if(!nueva.directivaConstante){
						inst.write(nueva.modoActual);
						contadorLoc += nueva.regresaBytes();
					}
					else if(!igualar)
						contadorLoc += nueva.sumaContDirectiva;
					inst.newLine();
				}
				//Rompemos la lectura del archivo si encontramos end
				if(nueva.getCodop().equalsIgnoreCase("end"))
					break;
				contador++;
				//Procesamos una nueva linea para seguir con el  ciclo
				nueva = null;
				nueva = new Linea();
			}
			//Comprobamos si el ciclo termino por el end o por el final del archivo
			if(!nueva.getCodop().equalsIgnoreCase("end"))
				error.write("El archivo no contuvo el codigo de operacion end");
			//Cerramos bufferes 
			lector.close();
			inst.close();
			error.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Tu archivo no existe, intenta de nuevo");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Escribe las etiquetas que existan
		escribeTabSim(etiq);
	}
	
	public void escribeTabSim(Vector <EtiquetaTabSim> eti){
		//Validamos que existan etiquetas
		if(eti.size()>0){
			try {
				BufferedWriter tab = new BufferedWriter(new FileWriter(new File(rutaPadre+nombreArchivo+".tds")));
				tab.write("ETIQUETA           VALOR");
				tab.newLine();
				tab.write("................................................");
				tab.newLine();
				for(int i = 0, j = eti.size(); i < j; i++){
					tab.write(eti.elementAt(i).etiqueta + "\t\t");
					tab.write(eti.elementAt(i).valorContLoc + "\t\t");
					tab.newLine();
				}
				
				tab.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	
	//Ultima verifiacion,  donde las Lineas son evaluadas su operando, en caso de encontrarse con un error, la quitamos y recalculamos el contloc para el archivo de prueba.
	public void verificaPasoDos() throws IOException{
		boolean encontrada = false;
		StringTokenizer elementos = null;
		String linea, numLinea,etiqueta = "";
		int i, j;
		try {
			BufferedReader archivo = new BufferedReader(new FileReader(new File(rutaPadre+nombreArchivo+".tds")));
			BufferedReader archivoInst = new BufferedReader(new FileReader(new File(rutaPadre+nombreArchivo+".inst")));
			archivo.readLine();
			//Cargamos el vector de tabsim
			archivo.readLine();
			while((linea = archivo.readLine())!= null){
				elementos = new StringTokenizer(linea);
				etiquetas.add(new EtiquetaTabSim(elementos.nextToken(),elementos.nextToken()));
				elementos = null;
			}
			archivoInst.readLine();
			archivoInst.readLine();
			while((linea = archivoInst.readLine()) != null){
				elementos = new StringTokenizer(linea);
				//Recibimos el numero de linea
				numLinea = elementos.nextToken();
				for(i = 2; i<=5 ; i++)
					etiqueta = elementos.nextToken();
				if(etiqueta.matches(("^[A-Za-z][A-Za-z0-9_]*")) && !etiqueta.equalsIgnoreCase("null")){
					for( i = 0, j = etiquetas.size() ; i < j ; i++){
						if(etiqueta.equalsIgnoreCase(etiquetas.elementAt(i).etiqueta)){
							encontrada = true;
							break;
						}
					}
					if(!encontrada){
						lineaErronea.add(Integer.parseInt(numLinea));
						encontrada = false;
					}
				}
			}
			archivo.close();
			archivoInst.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//El tabsim no existe por lo que se realizan los mismos procedimientos pero
			BufferedReader archivoInst = new BufferedReader(new FileReader(new File(rutaPadre+nombreArchivo+".inst")));
			archivoInst.readLine();
			archivoInst.readLine();
			while((linea = archivoInst.readLine()) != null){
				elementos = new StringTokenizer(linea);
				//Recibimos el numero de linea
				numLinea = elementos.nextToken();
				for(i = 2; i<=5 ; i++)
					etiqueta = elementos.nextToken();
				if(etiqueta.matches(("^[A-Za-z][A-Za-z0-9_]*")) && !etiqueta.equalsIgnoreCase("null"))
					lineaErronea.add(Integer.parseInt(numLinea));
			}
			
			archivoInst.close();
		}
	}
	
	private boolean noErronea(String contadorCadena){
		boolean error =  true;
		PriorityQueue <Integer> linea = lineaErronea;
		for(int i = 0, j = linea.size(); i < j ; i++){
			if(linea.peek() == Integer.parseInt(contadorCadena)){
				//Retiramos la linea erronea
				linea.poll();
				error = false;
				break;
			}
			
		}
		return error;
	}
	
	public void pasoDos() throws IOException{
		String linea = "";
		Linea objeto = null;
		contadorLoc = 0;
		String temporal, temporal2, contadorCadena = "";
		temporal = temporal2 = "";
		boolean recalcular = false;
		StringTokenizer elementos  = null;
		try {
			BufferedReader prueba = new BufferedReader(new FileReader(new File(rutaPadre+nombreArchivo+".inst")));
			BufferedWriter prueba2 = new BufferedWriter(new FileWriter(new File(rutaPadre+"PRUEBA.inst")));
			BufferedReader errorViejo = new BufferedReader(new FileReader(new File(rutaPadre+nombreArchivo+".err")));
			BufferedWriter errorNuevo = new BufferedWriter(new FileWriter(new File(rutaPadre+"Temporal.err")));
			
			//Sobreescribe el error nuevo
			while((temporal2 = errorViejo.readLine())!= null){
				errorNuevo.write(temporal2);
				errorNuevo.newLine();
			}
			errorNuevo.write("..........................................................................................");
			errorNuevo.newLine();
			
			prueba2.write("Linea		ContLoc		Etiqueta		Codop		Operando		Modos	CODMAQ");
			prueba2.newLine();
			prueba2.write("...........................................................................................................................................................................................");
			prueba2.newLine();
			prueba.readLine();
			prueba.readLine();
			while((linea = prueba.readLine())!= null){
				elementos = new StringTokenizer(linea);
				linea = elementos.nextToken();
				if(!noErronea(linea)){
					recalcular = true;
					errorNuevo.write("Linea " + linea + "llamado a etiqueta que no existe o no declarada");
					errorNuevo.newLine();
					errorNuevo.write("..........................................................................................");
					errorNuevo.newLine();
					//Obtenemos el valor del contador puesto que es lo unico correcto
					contadorCadena = elementos.nextToken();
					continue;
				}
				if(recalcular){
					//Recibe el ultimo valor de la cadena
					temporal = contadorCadena;
					//Saltamos el valor de todos los contadores
					elementos.nextToken();
				}
				else
					contadorCadena = elementos.nextToken();
				objeto = new Linea();
				objeto.etiqueta = elementos.nextToken();
				objeto.codop = elementos.nextToken();
				objeto.operando = elementos.nextToken();
				if(elementos.hasMoreElements())
					objeto.modoActual = elementos.nextToken();		
				prueba2.write(linea+"\t\t");
				prueba2.write(contadorCadena+"\t\t");
				//Recalculamos
				if(recalcular){
					//Los dos nos daran los bytes a sumar sea uno u otro
					objeto.validaDirectiva();
					//Queremos que fucione regresaBytes para poder sumar a las instrucciones normales
					objeto.validarCodop(tab);
					if(objeto.modoActual.equalsIgnoreCase("null"))
						contadorLoc =  Integer.parseInt(temporal, 16)  + objeto.sumaContDirectiva;
					else
						contadorLoc = Integer.parseInt(temporal, 16)  + objeto.regresaBytes();
					contadorCadena = convierteHexa();
					//Recibimos el nuevo valor para seguir recalculando
					temporal = contadorCadena;
				}
				prueba2.write(objeto.etiqueta+"\t\t");
				prueba2.write(objeto.codop+"\t\t");
				prueba2.write(objeto.operando+"\t\t");
				if(objeto.modoActual.equalsIgnoreCase("null"))
					prueba2.write("\t\t\t");
				else
					prueba2.write(objeto.modoActual+"\t\t");
				objeto.calculaCodigoMaquina(tab,etiquetas);
				prueba2.write(objeto.codMaq);
				prueba2.newLine();
			}
			prueba.close();
			prueba2.close();
			errorViejo.close();
			errorNuevo.close();
			//Renombramos los archivos
			File original = new File(rutaPadre+nombreArchivo+".inst");
			File nuevo = new File(rutaPadre+"PRUEBA.inst");
			original.delete();
			nuevo.renameTo(original);
			
			//Tambien los de errores
			File originalE = new File(rutaPadre+nombreArchivo+".err");
			File nuevoE = new File(rutaPadre+"Temporal.err");
			originalE.delete();
			nuevoE.renameTo(originalE);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Principal
	public static void main(String args[]) throws IOException{
		//Analizador del archivo
		Practica5 p = new Practica5();
		//Comprobamos que termine en asm
		if(p.compruebaExtension(args[0])){
			System.out.println("Felicidades, tu archivo si es ASM");
			p.analizaArchivo(args[0]);
			p.verificaPasoDos();
			p.pasoDos();
		}
		else{
			System.out.println("Tu archivo no existe, o no es ASM");
		}
	}
}