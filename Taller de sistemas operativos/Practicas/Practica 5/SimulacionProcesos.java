import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * @author Javier Agustín Rizo Orozco
 * Practica 5: Round Robin
 * Continuacion de la practica 4
 * Se agrego un quantum para simular el round robin decidido por el usuario para ser usado en el algoritmo
 * Se agrego la opcion de simular el carro de listos, es decir, se ira actualizando cada vez que terminen su
 * quantum.
 */
public class SimulacionProcesos {

	public Vector <Proceso> listaNuevos = new Vector<Proceso>();
	
	//Aqui comienza la ejecución del programa, la clase Monitor es la que se encarga de ejecutar los lotes, se le manda
	// desde donde termino el ultimo lote y la lista de todos los lotes.
	
	@SuppressWarnings("deprecation")
	public void simula(int quantum) throws InterruptedException{
		Monitor m = new Monitor(listaNuevos, quantum);
		m.run();
		m.stop();
	}
	
	public void captura(int numero, int quantum) throws InterruptedException{
		for(int i = 1; i <= numero; i++)
			agregaProceso(i);
		simula(quantum);
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
					
					//Aqui preguntamos por el quantum
					String cadenaQuantum = JOptionPane.showInputDialog(
							   null,
							   "Introduce el quantum para los procesos",
							   JOptionPane.QUESTION_MESSAGE);
					if (cadenaQuantum.matches("[0-9]+")){
						int quantum = Integer.parseInt(cadenaQuantum);
						if (quantum > 0){
							practica.captura(numero, quantum);
							break;
						}//Fin de verificar que quantum sea mayor a cero
					}//Fin de verificar que sea quantum
				}//Fin de preguntar ser mayor a cero
			}//Fin de la condicion de cadena correcta
		} //Fin del ciclo de preguntar
	}//Fin del main
}
