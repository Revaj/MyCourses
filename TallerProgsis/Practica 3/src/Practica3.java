import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;


/**
 * Practica2 :
 * Siguiendo los requisitos de la practica 1, esta practica revisa en un arbol cargado del archivo TABOP,
 * compara si se encuentra la instruccion en el codop y revisa si deberia tener operando o no, lo cual
 * resultaria escribirlo en el archivo de errores
 * @author Javier Agustin Rizo Orozco
 * @version 1.1
 * @
 */



public class Practica3 {
	public String nombreArchivo;
	public String rutaPadre;
	public Arbol tab = new Arbol();
	
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
		contador = 1;
		cargaTab();
		//Un try para evitar errores en los buffers
		try {
			//Instanciamos y creamos los buffers para comenzar a trabajar
			BufferedReader lector = new BufferedReader( new FileReader ( new File (fuente)));
			BufferedWriter inst = new BufferedWriter(new FileWriter(new File(rutaPadre+nombreArchivo+".inst")));
			BufferedWriter error = new BufferedWriter(new FileWriter(new File(rutaPadre+nombreArchivo+".err")));
			//Se inicia el archivo inst para las lineas sin errores
			inst.write("Linea		Etiqueta	Codop		Operando		Modos");
			inst.newLine();
			inst.write("................................................................................................................................................................");
			inst.newLine();
			//Recibimos la linea mientras no sea fin y ademas buscamos si encontramos la directiva end
			while((nueva.linea = lector.readLine())!= null ){
				nueva.separaComentarios();
				nueva.separaLinea();
				nueva.validarCodop(tab);
				nueva.validarEtiqueta();
				nueva.validarOperando();
				//Comprobacion de que no tenga error la linea
				if(nueva.tengoErrores() && !nueva.esComentarioCompleto)
					escribeError(error,contador,nueva);
				//Comprobaciones finales
				else if(!nueva.esComentarioCompleto && !nueva.lineaVacia){
					inst.write(contador+"\t\t"+nueva.getEtiqueta()+"\t\t"+nueva.getCodop()+"\t\t"+nueva.getOperando()+"\t\t");
					if(!nueva.directivaConstante)
						inst.write(nueva.modoActual);
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
		
	}

	//Principal
	public static void main(String args[]) throws IOException{
		//Analizador del archivo
		Practica3 p = new Practica3();
		//Comprobamos que termine en asm
		if(p.compruebaExtension(args[0])){
			System.out.println("Felicidades, tu archivo si es ASM");
			p.analizaArchivo(args[0]);
		}
		else{
			System.out.println("Tu archivo no existe, o no es ASM");
		}
	}
}