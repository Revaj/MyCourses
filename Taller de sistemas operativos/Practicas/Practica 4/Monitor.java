
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
	//Cola de terminados
	public Vector <Proceso> colaTerminados = new Vector<Proceso>();
	
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
	//Se usa para seguir la secuencia de los id de los procesos para el momento en que se genere uno nuevo
	private int numeroID;
	
	private JLabel idBloqueado;
	private JLabel tTB; //Tiempo transcurrido de bloqueado
	
	private int contGlobal;

	
	private boolean interrumpe = false;
	private boolean pausa = false;
	private boolean continua = false;
	private boolean error = false;
	private boolean tablaB;
	private boolean nuevo;
	
	//Tablas para JTable, son para procesos listos, terminados y bloqueados
	private DefaultTableModel md = new DefaultTableModel();
	private DefaultTableModel md2 = new DefaultTableModel();
	private DefaultTableModel md3 = new DefaultTableModel();
	
	public Monitor(Vector<Proceso> listaNuevos) {
		numeroID = listaNuevos.size() + 1;
		ventanaMonitor = new JFrame("Practica 3");
		ventanaMonitor.setLayout(null);
		tablaB = false;
		nuevo = false;
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
				else if(evt.getKeyChar() == 'M' || evt.getKeyChar() == 'm'){
					if(!tablaB){
						generaProcesoNuevo();
						nuevo = true;
					}
				}
				else if(evt.getKeyChar() == 'B' || evt.getKeyChar() == 'b'){
					if(!pausa)
						tablaB = true;
				}
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
	//Agrega un nuevo proceso
	public void generaProcesoNuevo(){
		colaNuevos.add(nuevoProceso());
		numeroID++;
		numeroProcesosNuevos.setText("Procesos en Estado Nuevo: "+colaNuevos.size());
	}
	//Entrega un proceso
	public Proceso nuevoProceso(){
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
	
	//Evento de la tecla B donde visualiza todos los procesos y sus resultados
	//Prepara la tabla BCP y comenzara agregar datos al siguiente metodo continuo
	public void visualizaBCP(){
		final JFrame bcp = new JFrame("BCP");
		JTable tabla = new JTable();
		DefaultTableModel modelo = new DefaultTableModel();
		JScrollPane corredor = new JScrollPane(tabla);
		bcp.setLayout(null);
		bcp.addKeyListener(new KeyListener(){ 
			public void keyPressed(KeyEvent evt){
				if(evt.getKeyChar() == 'C' || evt.getKeyChar() == 'c'){
					continua = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		bcp.add(corredor);
		corredor.setBounds(400, 0, 800, 500);
		modelo.addColumn("ID");
		modelo.addColumn("Estado");
		modelo.addColumn("Primer Dato");
		modelo.addColumn("Operacion");
		modelo.addColumn("Segundo dato");
		modelo.addColumn("Resultado");
		modelo.addColumn("Llegada");
		modelo.addColumn("Finaliza");
		modelo.addColumn("Retorno");
		modelo.addColumn("Espera");
		modelo.addColumn("Servicio");
		modelo.addColumn("Restante");
		modelo.addColumn("Respuesta");
		
		//Despues de preparar la tabla se usa muestraBCP para agregar todos los procesos y los datos segun sea el caso
		muestraBCP(bcp, tabla, modelo, corredor);
	}
	
	public void muestraBCP(final JFrame bcp, JTable tabla, DefaultTableModel modelo, JScrollPane corredor ){
	String [] fila = new String[13];
	//Verifica los procesos nuevos
		if(colaNuevos.size() > 0){
			for(int i = 0, j = colaNuevos.size(); i < j; i++){
				fila[0] = colaNuevos.elementAt(i).getID();
				fila[1] = "Nuevo";
				//Como es para nuevos, todos los campos seran nulos, por lo que con el if hara los valores restantes null
				for(int k = 2; k < 13; k++)
					fila[k] = "Nulo";
				modelo.addRow(fila);
				tabla.setModel(modelo);
			}
		}
		if(colaListos.size() > 0){
			for(int i = 0, j = colaListos.size(); i < j; i++){
				fila[0] = colaListos.elementAt(i).getID();
				if(colaListos.elementAt(i).ejecutando)
					fila[1] = "Ejecutando";
				else
					fila[1] = "Listo";
				fila[2] = Integer.toString(colaListos.elementAt(i).getDato1());
				fila[3] = Integer.toString(colaListos.elementAt(i).getOperacion());
				fila[4] = Integer.toString(colaListos.elementAt(i).getDato2());
				fila[5] = "Nulo";
				fila[6] = Integer.toString(colaListos.elementAt(i).tiempoLlegada);
				fila[7] = "Nulo";
				fila[8] = "Nulo";
				fila[9] = Integer.toString((contGlobal - colaListos.elementAt(i).tiempoLlegada) - (colaListos.elementAt(i).getTiempo() - colaListos.elementAt(i).tiempoRestante));
				fila[10] = "Nulo";
				if(colaListos.elementAt(i).primeraVez)
					fila[11] = Integer.toString(colaListos.elementAt(i).tiempoRestante);
				else
					fila[11] = "Nulo";
				if(colaListos.elementAt(i).ejecutando)
					fila[12] = Integer.toString(colaListos.elementAt(i).tiempoRespuesta);
				else
					fila[12] = "Nulo";
				modelo.addRow(fila);
				tabla.setModel(modelo);
			}
		}
		if(colaBloqueados.size() > 0){
			for(int i = 0, j = colaBloqueados.size(); i < j; i++){
				fila[0] = colaBloqueados.elementAt(i).getID();
				fila[1] = "Bloqueado";
 				fila[2] = Integer.toString(colaBloqueados.elementAt(i).getDato1());
				fila[3] = Integer.toString(colaBloqueados.elementAt(i).getOperacion());
				fila[4] = Integer.toString(colaBloqueados.elementAt(i).getDato2());
				fila[5] = "Nulo";
				fila[6] = Integer.toString(colaBloqueados.elementAt(i).tiempoLlegada);
				fila[7] = "Nulo";
				fila[8] = "Nulo";
				fila[9] = Integer.toString((contGlobal - colaBloqueados.elementAt(i).tiempoLlegada) - (colaBloqueados.elementAt(i).getTiempo() - colaBloqueados.elementAt(i).tiempoRestante));
				fila[10] = "Nulo";
				//En vez de mostrar el tiempo restante de ejecucion, mostramos el tiempo de bloqueo
				fila[11] = Integer.toString(colaBloqueados.elementAt(i).tiempoBloqueado);
				fila[12] = Integer.toString(colaBloqueados.elementAt(i).tiempoRespuesta);
				modelo.addRow(fila);
				tabla.setModel(modelo);
			}
		}
		if(colaTerminados.size() > 0){
			for(int i = 0, j = colaTerminados.size(); i < j; i++){
				fila[0] = colaTerminados.elementAt(i).getID();
				if(colaTerminados.elementAt(i).error){
					fila[1] = "Error";
					fila[5] = "Error";
					fila[11] = Integer.toString(colaTerminados.elementAt(i).tiempoRestante);
					fila[10] = Integer.toString(colaTerminados.elementAt(i).getTiempo() - colaTerminados.elementAt(i).tiempoRestante);
				}
				else{
					fila[1] = "Normal";
					fila[11] = Integer.toString(0);
					fila[10] = Integer.toString(colaTerminados.elementAt(i).getTiempo());
					switch(colaTerminados.firstElement().getOperacion()){
					case '+':
						fila[5] = Integer.toString(colaTerminados.elementAt(i).getDato1() + colaTerminados.elementAt(i).getDato2());
						break;
					case '-':
						fila[5] = Integer.toString(colaTerminados.elementAt(i).getDato1() - colaTerminados.elementAt(i).getDato2());
						break;
					case '*':
						fila[5] = Integer.toString(colaTerminados.elementAt(i).getDato1() * colaTerminados.elementAt(i).getDato2());
						break;
					case '/':
						fila[5] = Integer.toString(colaTerminados.elementAt(i).getDato1() / colaTerminados.elementAt(i).getDato2());
						break;
					case '%':
						fila[5] = Integer.toString(colaTerminados.elementAt(i).getDato1() % colaTerminados.firstElement().getDato2());
						break;
					case 'r':
						fila[5] = Integer.toString((int) Math.sqrt(colaTerminados.firstElement().getDato1()));
						break;
					}
				}
 				fila[2] = Integer.toString(colaTerminados.elementAt(i).getDato1());
				fila[3] = Integer.toString(colaTerminados.elementAt(i).getOperacion());
				fila[4] = Integer.toString(colaTerminados.elementAt(i).getDato2());
				fila[6] = Integer.toString(colaTerminados.elementAt(i).tiempoLlegada);
				fila[7] = Integer.toString(colaTerminados.elementAt(i).tiempoFinalizacion);
				fila[8] = Integer.toString(colaTerminados.elementAt(i).tiempoRetorno);
				fila[9] = Integer.toString(colaTerminados.elementAt(i).tiempoEspera);
				fila[12] = Integer.toString(colaTerminados.elementAt(i).tiempoRespuesta);
				modelo.addRow(fila);
				tabla.setModel(modelo);
			}
		}
	    bcp.setExtendedState(JFrame.MAXIMIZED_BOTH);
		bcp.setVisible(true);
		bcp.requestFocus();
		//Declaramos que no podemos cerrar la ventana de forma manual, para hacerlo debera dar un teclazo a C para activar continua
		bcp.setDefaultCloseOperation(0); 
		ventanaMonitor.toBack();
		while(!continua){
		}
		//Cerramos la ventana
		bcp.setVisible(false);
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
				colaListos.firstElement().error = true;
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
				colaTerminados.add(colaListos.remove(0));
				
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
			colaListos.firstElement().tiempoBloqueado = (int) (Math.random() * 5 + 1);
			fila[1] = Integer.toString(colaListos.firstElement().tiempoBloqueado = (int) (Math.random() * 5 + 1));
			colaListos.firstElement().ejecutando = false;
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
			if(colaListos.size() == 0 && colaBloqueados.size() == 3){
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
					colaListos.firstElement().ejecutando = true;
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
						else if(tablaB){
							tablaB = false;
							estadoActual.setText("Pausa");
							visualizaBCP();
							estadoActual.setText("Continua");
							continua = false;
							pausa = false;
						}
						else if(nuevo){
							if(colaListos.size() + colaBloqueados.size() != 3){
								nuevoListo();
							}
							nuevo = false;
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
