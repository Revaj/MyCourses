
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
	public static Vector <String> idRepetido = new Vector<String>();
	public int contadorLote = 0;

	
	//Aqui comienza la ejecución del programa, la clase Monitor es la que se encarga de ejecutar los lotes, se le manda
	// desde donde termino el ultimo lote y la lista de todos los lotes.
	
	public void simula() throws InterruptedException{
		Monitor m = new Monitor(listaLotes.size(), contadorLote,listaLotes);
		m.corre();
	}
	
	public void captura(int numero) throws InterruptedException{
		Captura captura = null;
		captura = new Captura(1, idRepetido);
		for(int i = 1; i <= numero; i++){
			Captura.getVentana().setTitle("Captura Proceso: "+i);
			Captura.getVentana().setVisible(true);
			while(Captura.getVentana().isShowing()){
				
			}
			agregaProceso(captura);
		}
		simula();
	}
	//Agrega un proceso, lote y lo comienza si es necesario
	public void agregaProceso(Captura datos){
		Proceso[] lote = null;
		Proceso  nuevoProc = null;
		switch(contadorLote){
		 //Iniciamos un nuevo lote y lo agregamos al vector
			case 0:
				lote = new Proceso[3];
				nuevoProc = nuevoProceso(datos);
				idRepetido.add(nuevoProc.getID());
				lote[0] = nuevoProc;
				listaLotes.add(lote);
				contadorLote++;
				break;
			case 1:
			case 2:
				nuevoProc = nuevoProceso(datos);
				idRepetido.add(nuevoProc.getID());
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
	public Proceso nuevoProceso(Captura datos){
		Proceso  nuevoProc = null;
		nuevoProc = new Proceso();
		nuevoProc.setNombreProg(datos.campoProgram.getText());
		nuevoProc.setOperacion(datos.valorOperacion);
		nuevoProc.setDato1(Integer.parseInt(datos.primerDato.getText()));
		nuevoProc.setDato2(Integer.parseInt(datos.segundoDato.getText()));
		nuevoProc.setTiempo(Integer.parseInt(datos.campoTiempo.getText()));
		nuevoProc.setNumeroID(datos.campoId.getText());
		return nuevoProc;
	}
	public static void main(String[] args) throws InterruptedException {
		SimulacionLotes practica = new SimulacionLotes();
		while(true){
			String cadenaNumero = JOptionPane.showInputDialog(
					   null,
					   "Cuantos procesos vas a capturar?",
					   JOptionPane.QUESTION_MESSAGE);
			if (cadenaNumero.matches("[0-9]*")){
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
