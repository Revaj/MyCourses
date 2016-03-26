
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*En esta clase pasa la segunda parte del programa para simular los procesos
 *
 */
public class Monitor extends Thread {
	
	//Colas de nuevos, listos y bloqueados
	public Vector <Proceso> colaNuevos = new Vector<Proceso>();
	public Vector <Proceso> colaListos = new Vector<Proceso>();
	public Vector <Proceso> colaBloqueados = new Vector<Proceso>();
	
	private JFrame ventanaMonitor;
	private JLabel numeroProcesosNuevos;
	private JLabel relojGlobal;
	private JLabel idProgram;
	private JLabel tiempoMaximo;
	private JLabel tiempoRest;

	private JLabel operacionProcEje;
	private JLabel tiempoMaxProcEje;
	private JLabel tiempoTransEje;
	private JLabel tiempoRestEje;
	private JLabel idProcEje;
	private JLabel estadoActual;
	private JTable lista1;
	private JTable lista2;
	private JTable lista3;
	private JScrollPane corredor;
	
	private JLabel idBloqueado;
	private JLabel tTB; //Tiempo transcurrido de bloqueado
	
	private int contGlobal;

	
	private boolean interrumpe = false;
	private boolean pausa = false;
	private boolean continua = false;
	private boolean error = false;
	
	//Tablas para JTable, son para procesos listos, terminados y bloqueados
	private DefaultTableModel md = new DefaultTableModel();
	private DefaultTableModel md2 = new DefaultTableModel();
	private DefaultTableModel md3 = new DefaultTableModel();
	
	public Monitor(Vector<Proceso> listaNuevos) {
		ventanaMonitor = new JFrame("Practica 3");
		ventanaMonitor.setLayout(null);
		ventanaMonitor.addKeyListener(new KeyListener(){ 
			public void keyPressed(KeyEvent evt){
				if(evt.getKeyChar() == 'e' || evt.getKeyChar() == 'E'){
					if(!pausa)
						interrumpe = true;
				}
				else if(evt.getKeyChar() == 'P' || evt.getKeyChar() == 'p')
					pausa = true;
				else if(evt.getKeyChar() == 'C' || evt.getKeyChar() == 'c')
					continua = true;
				else if(evt.getKeyChar() == 'W' || evt.getKeyChar() == 'w'){
					if(!pausa)
						error = true;
					else if(colaListos.size() > 0)
						error = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent evt) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyTyped(KeyEvent evt) {
			}


		});
	    numeroProcesosNuevos = new JLabel("Procesos en Estado Nuevo: "+(listaNuevos.size()));
	    estadoActual = new JLabel();
	    relojGlobal = new JLabel("Reloj Global: "+contGlobal);
	    ventanaMonitor.add(numeroProcesosNuevos);
	    numeroProcesosNuevos.setBounds(0, 0, 200, 50);
	    ventanaMonitor.add(relojGlobal);
	    relojGlobal.setBounds(1000, 650, 140, 60);
	    ventanaMonitor.add(estadoActual);
	    estadoActual.setBounds(1000,0,200,100);
	    
	    //Bloque izquierdo
	    JPanel procListos = new JPanel();
	    procListos.setLayout(null);
	    procListos.setBorder(BorderFactory.createTitledBorder("Cola de Listos"));
	    
	    idProgram = new JLabel("Numero del Programa");
	    procListos.add(idProgram);
	    idProgram.setBounds(10, 0, 200, 50);
	    
	    tiempoMaximo = new JLabel("TME");
	    procListos.add(tiempoMaximo);
	    tiempoMaximo.setBounds(150, 0, 200, 50);
	    
	    tiempoRest= new JLabel("Tiempo Restante");
	    procListos.add(tiempoRest);
	    tiempoRest.setBounds(260,0,200,50);
	    
	    //Pasamos la cola a la variable global
		colaNuevos = listaNuevos;
		
	    lista1 = new JTable();
	    cargaLista1();
	    procListos.add(lista1);
	    lista1.setBounds(10, 50, 380, 80);
	    
	    //Bloque central
	    JPanel procEjecucion = new JPanel();
	    procEjecucion.setLayout(null);
	    procEjecucion.setBorder(BorderFactory.createTitledBorder("Proceso en Ejecución"));
	    
	    operacionProcEje = new JLabel("Operacion: ");
	    procEjecucion.add(operacionProcEje);
	    operacionProcEje.setBounds(10, 20, 200, 30);
	    
	    tiempoMaxProcEje = new JLabel("Tiempo Maximo Estimado: ");
	    procEjecucion.add(tiempoMaxProcEje);
	    tiempoMaxProcEje.setBounds(10, 40, 200, 50);

	    idProcEje = new JLabel("Numero del Programa: ");
	    procEjecucion.add(idProcEje);
	    idProcEje.setBounds(10, 70, 200, 50);
	    
	    tiempoTransEje = new JLabel("Tiempo transcurrido de Ejecución: ");
	    procEjecucion.add(tiempoTransEje);
	    tiempoTransEje.setBounds(10, 100, 200, 30);
	    
	    tiempoRestEje = new JLabel("Tiempo restante de Ejecución: ");
	    procEjecucion.add(tiempoRestEje);
	    tiempoRestEje.setBounds(10, 130, 200, 30);
	    
	    //Procesos Terminados
	    JPanel terminados = new JPanel();
	    terminados.setLayout(null);
	    terminados.setBorder(BorderFactory.createTitledBorder("Procesos terminados"));
	    
	    lista2 = new JTable();
	    corredor = new JScrollPane(lista2);
	    terminados.add(corredor);
	    corredor.setBounds(10, 30, 630, 250);
	    cargaLista2();
	    
	    //Bloque inferior
	    JPanel bloqueados = new JPanel();
	    bloqueados.setLayout(null);
	    bloqueados.setBorder(BorderFactory.createTitledBorder("Procesos bloqueados"));
	    
	    idBloqueado = new JLabel("ID bloqueado");
	    bloqueados.add(idBloqueado);
	    idBloqueado.setBounds(10,0,80,50);

	    tTB = new JLabel("Tiempo Transicion Bloqueado");
	    bloqueados.add(tTB);
	    tTB.setBounds(100,0, 200, 40);
	    
	    
	    lista3 = new JTable();
	    cargaLista3();
	    bloqueados.add(lista3);
	    lista3.setBounds(10, 50, 380, 80);
	    
	    //Carga los paneles
	    ventanaMonitor.add(procListos);
	    procListos.setBounds(0, 50, 400, 150);
	    
	    ventanaMonitor.add(procEjecucion);
	    procEjecucion.setBounds(410, 50, 300, 170);
	    
	    ventanaMonitor.add(terminados);
	    terminados.setBounds(720, 50, 700, 300);
	    
	    ventanaMonitor.add(bloqueados);
	    bloqueados.setBounds(0,500,450,150);
	    
	    ventanaMonitor.setExtendedState(JFrame.MAXIMIZED_BOTH);
		ventanaMonitor.setVisible(true);
		
	}
	//Cargamos la lista a la tabla izquierda, y ademas pasamos los nuevos a listos
	//Al ser los primeros procesos, su tiempo de llegada sera 0
	public void cargaLista1(){
		md.addColumn("Numero del Programa");
		md.addColumn("TME");
		md.addColumn("Tiempo Restante");
		String [] fila = new String[3];
		//Si la cola tiene mas de 3 o igual procesos agregamos solo 3
		if(colaNuevos.size() >= 3)
			for (int i = 0; i < 3; i++){
				fila[0] = colaNuevos.firstElement().getID();
				fila[1] = Integer.toString(colaNuevos.firstElement().getTiempo());
				fila[2] = Integer.toString(colaNuevos.firstElement().tiempoRestante);
				md.addRow(fila);
				System.out.println(fila[0]);
				colaNuevos.firstElement().tiempoLlegada = 0;
				colaListos.add(colaNuevos.remove(0));
			}
		else
			for (int i = 0, j = colaNuevos.size(); i < j; i++){
				fila[0] = colaNuevos.firstElement().getID();
				fila[1] = Integer.toString(colaNuevos.firstElement().getTiempo());
				fila[2] = Integer.toString(colaNuevos.firstElement().tiempoRestante);
				md.addRow(fila);
				colaNuevos.firstElement().tiempoLlegada = 0;
				colaListos.add(colaNuevos.remove(0));
			}
			
		lista1.setModel(md);
		numeroProcesosNuevos.setText("Procesos en Estado Nuevo: "+(colaNuevos.size()));
	}
	
	public void cargaLista2(){
		md2.addColumn("ID");
		md2.addColumn("Dato1");
		md2.addColumn("Operacion");
		md2.addColumn("Dato2");
		md2.addColumn("Resultado");
		md2.addColumn("T.Llegada");
		md2.addColumn("T.Finaliza");
		md2.addColumn("T.Espera");
		md2.addColumn("T.Servicio");
		md2.addColumn("T.Retorno");
		md2.addColumn("T.Respuesta");
	}
	
	public void cargaLista3(){
		md3.addColumn("ID bloqueo");
		md3.addColumn("Tiempo Transcurrido B");
	}
	
	//Ponemos el proceso terminado
	public void nuevoLista2(int contadorTrans){
		String[] algo = new String[12];
		int res = 0;
		if(colaListos.size() > 0){
			colaListos.firstElement().tiempoFinalizacion = contGlobal - 1;
			algo[0] = colaListos.firstElement().getID();
			algo[1] = Integer.toString(colaListos.firstElement().getDato1());
			algo[2] = Character.toString(colaListos.firstElement().getOperacion());
			algo[3] = Integer.toString(colaListos.firstElement().getDato2());
			if(error){
				algo[4] = "ERROR";
				algo[8] = Integer.toString(colaListos.firstElement().getTiempo() - colaListos.firstElement().tiempoRestante);
				//Calculamos los tiempos espera y retorno aqui
				colaListos.firstElement().tiempoFinalizacion = contGlobal;
				colaListos.firstElement().tiempoRetorno = (colaListos.firstElement().tiempoFinalizacion - colaListos.firstElement().tiempoLlegada);
				colaListos.firstElement().tiempoEspera = (colaListos.firstElement().tiempoRetorno - (colaListos.firstElement().getTiempo() - colaListos.firstElement().tiempoRestante));
				
				error = false;
			}
			else{
				if(!interrumpe)
					contGlobal--;
				switch(colaListos.firstElement().getOperacion()){
					case '+':
						res = colaListos.firstElement().getDato1() + colaListos.firstElement().getDato2();
						break;
					case '-':
						res = colaListos.firstElement().getDato1() - colaListos.firstElement().getDato2();
						break;
					case '*':
						res = colaListos.firstElement().getDato1() * colaListos.firstElement().getDato2();
						break;
					case '/':
						res = colaListos.firstElement().getDato1() / colaListos.firstElement().getDato2();
						break;
					case '%':
						res = colaListos.firstElement().getDato1() % colaListos.firstElement().getDato2();
						break;
					case 'r':
						res = (int) Math.sqrt(colaListos.firstElement().getDato1());
						break;
				}
				algo[4] = Integer.toString(res);
				algo[8] = Integer.toString(colaListos.firstElement().getTiempo());
				colaListos.firstElement().tiempoRetorno = (colaListos.firstElement().tiempoFinalizacion - colaListos.firstElement().tiempoLlegada);
				colaListos.firstElement().tiempoEspera = (colaListos.firstElement().tiempoRetorno - colaListos.firstElement().getTiempo());
			}
			if(!interrumpe){
			
				
				algo[5] = Integer.toString(colaListos.firstElement().tiempoLlegada);
				algo[6] = Integer.toString(colaListos.firstElement().tiempoFinalizacion);
				algo[7] = Integer.toString(colaListos.firstElement().tiempoEspera);
				algo[9] = Integer.toString(colaListos.firstElement().tiempoRetorno);
				algo[10] = Integer.toString(colaListos.firstElement().tiempoRespuesta);
					
				md2.addRow(algo);
				lista2.setModel(md2);
				colaListos.remove(0);
				
			}
			if(colaNuevos.size() > 0 && (colaBloqueados.size() + colaListos.size() < 3))
				nuevoListo();
		}
		interrumpe = false;
	}
	
	//Cargamos un nuevo proceso
	public void nuevoListo(){
		String [] fila = new String[3];

		fila[0] = colaNuevos.firstElement().getID();
		fila[1] = Integer.toString(colaNuevos.firstElement().getTiempo());
		fila[2] = Integer.toString(colaNuevos.firstElement().tiempoRestante);
		md.addRow(fila);
		colaNuevos.firstElement().tiempoLlegada = contGlobal;
		colaListos.add(colaNuevos.remove(0));
		numeroProcesosNuevos.setText("Procesos en Estado Nuevo: "+(colaNuevos.size()));
		lista1.setModel(md);
	}
	//Nuevo listo de los bloqueados
	public void nuevoListoBl(){
		String [] fila = new String[3];

		fila[0] = colaBloqueados.firstElement().getID();
		fila[1] = Integer.toString(colaBloqueados.firstElement().getTiempo());
		fila[2] = Integer.toString(colaBloqueados.firstElement().tiempoRestante);
		md3.removeRow(0);
		md.addRow(fila);
		
		colaListos.add(colaBloqueados.remove(0));
		lista1.setModel(md);
		lista3.setModel(md3);
	}
	
	//Manda a bloqueados el proceso
	public void interrupcionEs(){
		String [] fila = new String[3];
		
		//Bloqueados
		if(colaListos.size() > 0){
			fila[0] = colaListos.firstElement().getID();
			fila[1] = Integer.toString(5);
			colaListos.firstElement().tiempoBloqueado = 5;
			colaBloqueados.add(colaListos.remove(0));
			
			md3.addRow(fila);
			lista3.setModel(md3);
		}
	}
	//Corre los bloqueados, si tienen 0 al iniciar se manda a listo
	public void correBloqueados(){
		String [] fila = new String[3];
		
		//Bloqueados
		if(colaBloqueados.size() > 0){
			//Corremos el contador con los bloqueados ya que no hay ninguno de los listos
			if(colaListos.size() == 0){
				relojGlobal.setText("Reloj Global: "+(++contGlobal));
				error = false; // Por si se nos viene un error entrado cuando los procesos quedaron en bloqueo los 3
				try {
					ventanaMonitor.requestFocus();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int i = 0, j = colaBloqueados.size(); i < j; i++){
				if(colaBloqueados.firstElement().tiempoBloqueado == 0)
					nuevoListoBl();
				else{
					fila[0] = colaBloqueados.firstElement().getID();
					colaBloqueados.firstElement().tiempoBloqueado--;
					fila[1] = Integer.toString(colaBloqueados.firstElement().tiempoBloqueado);
					colaBloqueados.add(colaBloqueados.remove(0));
					md3.removeRow(0);
					md3.addRow(fila);
					lista3.setModel(md3);
				}
			}
		}
	}
	
	
	//El programa principal donde corre la segunda parte
	public void run(){
		int contadorTrans;
		//Iteracion donde cuando terminen las tres colas saldra el programa
		while(colaListos.size() > 0 || colaNuevos.size() > 0 || colaBloqueados.size() > 0){
			contadorTrans = 0;
			if(colaListos.size() == 0)
				correBloqueados();
			//Para el caso en que los tres procesos esten en bloqueado
			if(md.getRowCount() > 0){
				md.removeRow(0);
				lista1.setModel(md);
				if(colaListos.size() > 0){
					if(colaListos.firstElement().getOperacion() == 'r')
						operacionProcEje.setText("Operacion: Raiz");
					else
						operacionProcEje.setText("Operacion: "+colaListos.firstElement().getOperacion());
					tiempoMaxProcEje.setText("Tiempo Maximo: "+Integer.toString(colaListos.firstElement()
							.getTiempo()));
					idProcEje.setText("ID: "+colaListos.firstElement().getID());
					contadorTrans = colaListos.firstElement().getTiempo() - colaListos.firstElement().tiempoRestante;
					while(colaListos.firstElement().tiempoRestante >= 0){
						tiempoTransEje.setText("Tiempo Transcurrido Ejecucion: "+Integer.toString(contadorTrans));
						tiempoRestEje.setText("Tiempo Restante Ejecucion: "+Integer.toString(colaListos.firstElement().tiempoRestante));
						relojGlobal.setText("Reloj Global: "+(contGlobal));
						try {
							ventanaMonitor.requestFocus();
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//Practica 2
						if(interrumpe){
							estadoActual.setText("Interrupcion");
							interrupcionEs();
							break;
						}
						
						else if(pausa){
							estadoActual.setText("Pausa");
							while(!continua){}
							continua = false;
							pausa = false;
							estadoActual.setText("Continua");
						}
						
						else if(error){
							if(colaListos.size() > 0)
								estadoActual.setText("Error");
							else
								error = false;
							break;
						}
						
						else{
							correBloqueados();
							if(!colaListos.firstElement().primeraVez){
								colaListos.firstElement().tiempoRespuesta = contGlobal - colaListos.firstElement().tiempoLlegada;
								colaListos.firstElement().primeraVez = true;
							}
							contadorTrans++;
							colaListos.firstElement().tiempoRestante--;
							contGlobal++;
						}
					}//Fin del while
				}
				nuevoLista2(contadorTrans);
			}
		}
		estadoActual.setText("Terminado");
	}
}
