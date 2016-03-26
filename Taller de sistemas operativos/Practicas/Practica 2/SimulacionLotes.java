
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * 
 */

/**
 * @author Javier Agustín Rizo Orozco
 * Practica 1: Simulación por lotes
 * Este programa permite simular el comportamiento del procesamiento por lotes
 * donde se capturaban los procesos y se agrupaban para poder ser procesados juntos
 * La simulacion incluye parte grafica para captura de datos y para la simulacion.
 * Esta clase principal junta la clase Captura y la clase Simula
 */
public class SimulacionLotes {

	public Vector <Proceso []> listaLotes = new Vector<Proceso[]>();
	public int contadorLote = 0;

	
	//Aqui comienza la ejecución del programa, la clase Monitor es la que se encarga de ejecutar los lotes, se le manda
	// desde donde termino el ultimo lote y la lista de todos los lotes.
	
	@SuppressWarnings("deprecation")
	public void simula() throws InterruptedException{
		Monitor m = new Monitor(listaLotes.size(), contadorLote,listaLotes);
		m.run();
		m.stop();
	}
	
	public void captura(int numero) throws InterruptedException{
		for(int i = 1; i <= numero; i++){
			agregaProceso(i);
		}
		simula();
	}

	//Agrega un proceso, lote y lo comienza si es necesario
	public void agregaProceso(int numeroID){
		Proceso[] lote = null;
		Proceso  nuevoProc = null;
		switch(contadorLote){
		 //Iniciamos un nuevo lote y lo agregamos al vector
			case 0:
				lote = new Proceso[3];
				nuevoProc = nuevoProceso(numeroID);
				lote[0] = nuevoProc;
				listaLotes.add(lote);
				contadorLote++;
				break;
			case 1:
			case 2:
				nuevoProc = nuevoProceso(numeroID);
				lote = listaLotes.lastElement();
				lote[contadorLote] = nuevoProc;
				listaLotes.removeElementAt(listaLotes.size()-1);
				listaLotes.add(lote);
				contadorLote++;
				if(contadorLote >= 3)
					contadorLote = 0;
				break;
				
				
		}
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
		SimulacionLotes practica = new SimulacionLotes();
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
