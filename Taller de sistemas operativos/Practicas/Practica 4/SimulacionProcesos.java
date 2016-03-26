import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * 
 */

/**
 * @author Javier Agust�n Rizo Orozco
 * Practica 4: Continuacion FCFS
 * Continuacion de la practica 3
 * Se agregaron los siguientes eventos de teclado
 * M para generar un proceso nuevo para la cola de nuevos
 * B para mostrar el BCP con los datos de todos los procesos
 */
public class SimulacionProcesos {

	public Vector <Proceso> listaNuevos = new Vector<Proceso>();
	
	//Aqui comienza la ejecuci�n del programa, la clase Monitor es la que se encarga de ejecutar los lotes, se le manda
	// desde donde termino el ultimo lote y la lista de todos los lotes.
	
	@SuppressWarnings("deprecation")
	public void simula() throws InterruptedException{
		Monitor m = new Monitor(listaNuevos);
		m.run();
		m.stop();
	}
	
	public void captura(int numero) throws InterruptedException{
		for(int i = 1; i <= numero; i++)
			agregaProceso(i);
		simula();
	}

	//Agrega un proceso nuevo a la cola con el metodo nuevoProceso y un ID
	public void agregaProceso(int numeroID){
		Proceso  nuevoProc = null;
		nuevoProc = nuevoProceso(numeroID);
		listaNuevos.add(nuevoProc);
	}
	//Proceso repetitivo de crear una tarea, es usado en los casos de agregaProceso
	public Proceso nuevoProceso(int numeroID){
		Proceso  nuevoProc = null;
		int operacionRuleta;
		char tipoOperacion;
		nuevoProc = new Proceso();
		int dato1, dato2;
		operacionRuleta = (int) ( Math.random() * 6);
		switch(operacionRuleta){
			case 0:
				tipoOperacion = '+';
				break;
			case 1:
				tipoOperacion = '-';
				break;
			case 2:
				tipoOperacion = '*';
				break;
			case 3:
				tipoOperacion = '/';
				break;
			case 4:
				tipoOperacion = '%';
				break;
			default:
				tipoOperacion = 'r';
				break;
		}
		nuevoProc.setOperacion(tipoOperacion);
		//No agregamos -1 por las operaciones de raiz
		dato1 = (int) (Math.random() * 10000);
	    //Agregamos 1 para no tener que validar las operaciones de modulo y division 
		dato2 = (int) (Math.random() * 10000 + 1);
			
		nuevoProc.setDato1(dato1);
		nuevoProc.setDato2(dato2);
		//Agregamos mas 1 para no validar tiempo mayor a cero
		nuevoProc.setTiempo((int) (Math.random() * 20 + 1));
		nuevoProc.tiempoRestante = nuevoProc.getTiempo();
		nuevoProc.setNumeroID(Integer.toString(numeroID));
		return nuevoProc;
	}
	public static void main(String[] args) throws InterruptedException {
		SimulacionProcesos practica = new SimulacionProcesos();
		while(true){
			String cadenaNumero = JOptionPane.showInputDialog(
					   null,
					   "Cuantos procesos vas a capturar?",
					   JOptionPane.QUESTION_MESSAGE);
			if (cadenaNumero.matches("[0-9]+")){
				int numero = Integer.parseInt(cadenaNumero);
				if (numero > 0){
					practica.captura(numero);
					break;
				}
				else
					break;
			}
			else
				break;
		}
		
	}

}
