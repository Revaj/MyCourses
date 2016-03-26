
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
	public Vector <Proceso []> listaLotes = new Vector<Proceso[]>();
	private JFrame ventanaMonitor;
	private JLabel numeroLotes;
	private JLabel relojGlobal;
	private JLabel idProgram;
	private JLabel tiempoMaximo;
	private JLabel tiempoRestLote;

	private JLabel operacionProcEje;
	private JLabel tiempoMaxProcEje;
	private JLabel tiempoTransEje;
	private JLabel tiempoRestEje;
	private JLabel idProcEje;
	private JLabel idTerminado;
	private JLabel dato1Terminado;
	private JLabel opTerminado;
	private JLabel dato2Terminado;
	private JLabel resultadTerm;
	private JLabel numLote;
	private JLabel estadoActual;
	private JTable lista1;
	private JTable lista2;
	private JScrollPane corredor;
	
	private int contGlobal;
	private int loteRestante;
	private int loteActual = 1;
	private int procLoteUsados = 0;
	private int contadorLote;
	
	private boolean interrumpe = false;
	private boolean pausa = false;
	private boolean continua = false;
	private boolean error = false;
	
	
	private DefaultTableModel md = new DefaultTableModel();
	private DefaultTableModel md2 = new DefaultTableModel();
	
	public Monitor(int cantLotes, int numProc, Vector <Proceso []> totalLotes) {
		ventanaMonitor = new JFrame("Practica 1");
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
	    numeroLotes = new JLabel("Lotes Restantes: "+(--cantLotes));
	    estadoActual = new JLabel();
	    loteRestante = cantLotes;
	    relojGlobal = new JLabel("Reloj Global: "+contGlobal);
	    ventanaMonitor.add(numeroLotes);
	    numeroLotes.setBounds(0, 0, 120, 50);
	    ventanaMonitor.add(relojGlobal);
	    relojGlobal.setBounds(1000, 650, 140, 60);
	    ventanaMonitor.add(estadoActual);
	    estadoActual.setBounds(1000,0,200,100);
	    
	    //Bloque izquierdo
	    JPanel loteEjecucion = new JPanel();
	    loteEjecucion.setLayout(null);
	    loteEjecucion.setBorder(BorderFactory.createTitledBorder("Lote en Ejecución"));
	    
	    idProgram = new JLabel("Numero del Programa");
	    loteEjecucion.add(idProgram);
	    idProgram.setBounds(10, 0, 200, 50);
	    
	    tiempoMaximo = new JLabel("TME");
	    loteEjecucion.add(tiempoMaximo);
	    tiempoMaximo.setBounds(150, 0, 200, 50);
	    
	    tiempoRestLote = new JLabel("Tiempo Restante");
	    loteEjecucion.add(tiempoRestLote);
	    tiempoRestLote.setBounds(260,0,200,50);
	    
		listaLotes = totalLotes;
		
	    lista1 = new JTable();
	    if(numProc == 0)
	    	contadorLote = 2;
	    else
	    	contadorLote = (numProc - 1);
	    //0 nos indica que esta lleno el ultimo lote :v
	    
	    cargaLista1();

	    loteEjecucion.add(lista1);
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
	    tiempoMaxProcEje.setBounds(10, 65, 200, 50);

	    idProcEje = new JLabel("Numero del Programa: ");
	    procEjecucion.add(idProcEje);
	    idProcEje.setBounds(10, 95, 200, 50);
	    
	    tiempoTransEje = new JLabel("Tiempo transcurrido de Ejecución: ");
	    procEjecucion.add(tiempoTransEje);
	    tiempoTransEje.setBounds(10, 130, 200, 30);
	    
	    tiempoRestEje = new JLabel("Tiempo restante de Ejecución: ");
	    procEjecucion.add(tiempoRestEje);
	    tiempoRestEje.setBounds(10, 160, 200, 30);
	    
	    //Procesos Terminados
	    JPanel terminados = new JPanel();
	    terminados.setLayout(null);
	    terminados.setBorder(BorderFactory.createTitledBorder("Procesos terminados"));
	    
	    idTerminado = new JLabel("ID");
	    terminados.add(idTerminado);
	    idTerminado.setBounds(20,10,20,20);
	    
	    dato1Terminado = new JLabel("Primer Dato");
	    terminados.add(dato1Terminado);
	    dato1Terminado.setBounds(50, 10, 90, 20);

	    opTerminado = new JLabel("Operacion");
	    terminados.add(opTerminado);
	    opTerminado.setBounds(150, 10, 80, 20);
	    
	    dato2Terminado = new JLabel("Segundo Dato");
	    terminados.add(dato2Terminado);
	    dato2Terminado.setBounds(210, 10, 100, 20);
	    
	    resultadTerm = new JLabel("Resultado");
	    terminados.add(resultadTerm);
	    resultadTerm.setBounds(310, 10, 90, 20);
	    
	    numLote = new JLabel("No. Lote");
	    terminados.add(numLote);
	    numLote.setBounds(430, 10, 90, 20);
	    
		
	    lista2 = new JTable();
	    corredor = new JScrollPane(lista2);
	    terminados.add(corredor);
	    corredor.setBounds(10, 50, 480, 240);
	    cargaLista2();
	 
	    //Carga los paneles
	    ventanaMonitor.add(loteEjecucion);
	    loteEjecucion.setBounds(0, 50, 400, 150);
	    
	    ventanaMonitor.add(procEjecucion);
	    procEjecucion.setBounds(410, 50, 300, 230);
	    
	    ventanaMonitor.add(terminados);
	    terminados.setBounds(850, 50, 550, 300);
	    
	    ventanaMonitor.setExtendedState(JFrame.MAXIMIZED_BOTH);
		ventanaMonitor.setVisible(true);
		procLoteUsados = 0;
		
	}
	
	public void cargaLista1(){
		md.addColumn("Numero del Programa");
		md.addColumn("TME");
		md.addColumn("Tiempo Restante");
		String [] fila = new String[3];
		if(contadorLote == 2 || loteRestante >= 1)
			for (int i = 0; i < 3; i++){
				fila[0] = listaLotes.elementAt(0)[i].getID();
				fila[1] = Integer.toString(listaLotes.elementAt(0)[i].getTiempo());
				fila[2] = Integer.toString(listaLotes.elementAt(0)[i].tiempoRestante);
				md.addRow(fila);
			}
		else
			for (int i = 0; i <= contadorLote; i++){
				fila[0] = listaLotes.elementAt(0)[i].getID();
				fila[1] = Integer.toString(listaLotes.elementAt(0)[i].getTiempo());
				fila[2] = Integer.toString(listaLotes.elementAt(0)[i].tiempoRestante);
				md.addRow(fila);
			}
		lista1.setModel(md);
	}
	
	public void cargaLista2(){
		md2.addColumn("ID");
		md2.addColumn("Dato1");
		md2.addColumn("Operacion");
		md2.addColumn("Dato2");
		md2.addColumn("Resultado");
		md2.addColumn("Lote");
	}
	
	public void nuevoLista2(int i, int p){
		String[] algo = new String[6];
		int res = 0;
		algo[0] = listaLotes.elementAt(p)[i].getID();
		algo[1] = Integer.toString(listaLotes.elementAt(p)[i].getDato1());
		algo[2] = Character.toString(listaLotes.elementAt(p)[i].getOperacion());
		algo[3] = Integer.toString(listaLotes.elementAt(p)[i].getDato2());
		if(error){
			algo[4] = "ERROR";
			error = false;
		}
		else{
			if(!interrumpe)
				contGlobal--;
			switch(listaLotes.elementAt(p)[i].getOperacion()){
			case '+':
				res = listaLotes.elementAt(p)[i].getDato1() + listaLotes.elementAt(p)[i].getDato2();
				break;
			case '-':
				res = listaLotes.elementAt(p)[i].getDato1() - listaLotes.elementAt(p)[i].getDato2();
				break;
			case '*':
				res = listaLotes.elementAt(p)[i].getDato1() * listaLotes.elementAt(p)[i].getDato2();
				break;
			case '/':
				res = listaLotes.elementAt(p)[i].getDato1() / listaLotes.elementAt(p)[i].getDato2();
				break;
			case '%':
				res = listaLotes.elementAt(p)[i].getDato1() % listaLotes.elementAt(p)[i].getDato2();
				break;
			case 'r':
				res = (int) Math.sqrt(listaLotes.elementAt(p)[i].getDato1());
				break;
			}
			algo[4] = Integer.toString(res);
		}
		algo[5] = Integer.toString(loteActual);
		if(!interrumpe)
			md2.addRow(algo);
	}
	
	//Cargamos un nuevo lote
	public void cargaNuevoLote(int nuevo){
		loteActual++;
		numeroLotes.setText("Lotes Restantes: "+(--loteRestante));
		String [] fila = new String[3];
		if(contadorLote == 2 || loteRestante >= 1)
			for (int i = 0; i < 3; i++){
				fila[0] = listaLotes.elementAt(nuevo)[i].getID();
				fila[1] = Integer.toString(listaLotes.elementAt(nuevo)[i].getTiempo());
				fila[2] = Integer.toString(listaLotes.elementAt(nuevo)[i].tiempoRestante);
				md.addRow(fila);
			}
		else
			for (int i = 0; i <= contadorLote; i++){
				fila[0] = listaLotes.elementAt(nuevo)[i].getID();
				fila[1] = Integer.toString(listaLotes.elementAt(nuevo)[i].getTiempo());
				fila[2] = Integer.toString(listaLotes.elementAt(nuevo)[i].tiempoRestante);
				md.addRow(fila);
			}
		lista1.setModel(md);
	}
	
	//Metodo activado al hacer interrupcion
	public void interrupcion(){
		interrumpe = true;
	}
	
	public void interrupcionEs(int posicionLote, int loteCurrente){
		String [] fila = new String[3];
		Proceso  tmp = null;
		if(loteCurrente + 1 == listaLotes.size()){
			switch(contadorLote){
			case 0:
				tmp = listaLotes.elementAt(loteCurrente)[0];
				listaLotes.elementAt(loteCurrente)[0] = tmp;
				
				fila[0] = listaLotes.elementAt(loteCurrente)[0].getID();
				fila[1] = Integer.toString(listaLotes.elementAt(loteCurrente)[0].getTiempo());
				fila[2] = Integer.toString(listaLotes.elementAt(loteCurrente)[0].tiempoRestante);
				
				break;
			case 1:
				if(posicionLote == 0){
					tmp = listaLotes.elementAt(loteCurrente)[0];
					listaLotes.elementAt(loteCurrente)[0] = listaLotes.elementAt(loteCurrente)[1];
					listaLotes.elementAt(loteCurrente)[1] = tmp;
					
					fila[0] = listaLotes.elementAt(loteCurrente)[1].getID();
					fila[1] = Integer.toString(listaLotes.elementAt(loteCurrente)[1].getTiempo());
					fila[2] = Integer.toString(listaLotes.elementAt(loteCurrente)[1].tiempoRestante);			
				}
				else{
					tmp = listaLotes.elementAt(loteCurrente)[1];
					listaLotes.elementAt(loteCurrente)[1] = tmp;
					
					fila[0] = listaLotes.elementAt(loteCurrente)[1].getID();
					fila[1] = Integer.toString(listaLotes.elementAt(loteCurrente)[1].getTiempo());
					fila[2] = Integer.toString(listaLotes.elementAt(loteCurrente)[1].tiempoRestante);
				}
				break;
			case 2:
				if(posicionLote == 0){
					tmp = listaLotes.elementAt(loteCurrente)[0];
					listaLotes.elementAt(loteCurrente)[0] = listaLotes.elementAt(loteCurrente)[1];
					listaLotes.elementAt(loteCurrente)[1] = listaLotes.elementAt(loteCurrente)[2];
					listaLotes.elementAt(loteCurrente)[2] = tmp;
					
					fila[0] = listaLotes.elementAt(loteCurrente)[2].getID();
					fila[1] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].getTiempo());
					fila[2] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].tiempoRestante);			
				}
				else if(posicionLote == 1 ){
					tmp = listaLotes.elementAt(loteCurrente)[1];
					listaLotes.elementAt(loteCurrente)[1] = listaLotes.elementAt(loteCurrente)[2];
					listaLotes.elementAt(loteCurrente)[2] = tmp;
					
					fila[0] = listaLotes.elementAt(loteCurrente)[2].getID();
					fila[1] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].getTiempo());
					fila[2] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].tiempoRestante);
				}
				else{
					tmp = listaLotes.elementAt(loteCurrente)[2];
					listaLotes.elementAt(loteCurrente)[2] = tmp;
					
					fila[0] = listaLotes.elementAt(loteCurrente)[2].getID();
					fila[1] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].getTiempo());
					fila[2] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].tiempoRestante);
				}
				break;

			}
		}
		else
			switch(posicionLote){
				case 0:
					tmp = listaLotes.elementAt(loteCurrente)[0];
					listaLotes.elementAt(loteCurrente)[0] = listaLotes.elementAt(loteCurrente)[1];
					listaLotes.elementAt(loteCurrente)[1] = listaLotes.elementAt(loteCurrente)[2];
					listaLotes.elementAt(loteCurrente)[2] = tmp;
					
					fila[0] = listaLotes.elementAt(loteCurrente)[2].getID();
					fila[1] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].getTiempo());
					fila[2] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].tiempoRestante);
					
					
					break;
				case 1:
					tmp = listaLotes.elementAt(loteCurrente)[1];
					listaLotes.elementAt(loteCurrente)[1] = listaLotes.elementAt(loteCurrente)[2];
					listaLotes.elementAt(loteCurrente)[2] = tmp;
					
					fila[0] = listaLotes.elementAt(loteCurrente)[2].getID();
					fila[1] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].getTiempo());
					fila[2] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].tiempoRestante);
					
					break;
				case 2:
					tmp = listaLotes.elementAt(loteCurrente)[2];
					listaLotes.elementAt(loteCurrente)[2] = tmp;
					
					fila[0] = listaLotes.elementAt(loteCurrente)[2].getID();
					fila[1] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].getTiempo());
					fila[2] = Integer.toString(listaLotes.elementAt(loteCurrente)[2].tiempoRestante);
					
					break;
			}
		
		md.addRow(fila);
		lista1.setModel(md);
	}
	
	
	//El programa principal donde corre la segunda parte
	public void run(){
		int contadorTrans = 0;
		boolean sinSal = true;
		for(int i = 0, j = listaLotes.size(); i < j; i++){
			sinSal = true;
			while(sinSal){
				contadorTrans = 0;
				//i significa para el lote
				//procLoteUsados indica el proceso actual del lote
				switch(procLoteUsados){
					case 0:
					case 1:
						md.removeRow(0);
						lista1.setModel(md);
						if(listaLotes.elementAt(i)[procLoteUsados].getOperacion() == 'r')
							operacionProcEje.setText("Operacion: Raiz");
						else
							operacionProcEje.setText("Operacion: "+listaLotes.elementAt(i)[procLoteUsados].getOperacion());
						tiempoMaxProcEje.setText("Tiempo Maximo: "+Integer.toString(listaLotes.elementAt(i)[procLoteUsados].getTiempo()));
						idProcEje.setText("ID: "+listaLotes.elementAt(i)[procLoteUsados].getID());
						contadorTrans = listaLotes.elementAt(i)[procLoteUsados].getTiempo() - listaLotes.elementAt(i)[procLoteUsados].tiempoRestante;
						while(listaLotes.elementAt(i)[procLoteUsados].tiempoRestante >= 0){
							tiempoTransEje.setText("Tiempo Transcurrido Ejecucion: "+Integer.toString(contadorTrans));
							tiempoRestEje.setText("Tiempo Restante Ejecucion: "+Integer.toString(listaLotes.elementAt(i)[procLoteUsados].tiempoRestante));
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
								interrupcionEs(procLoteUsados,i);
								break;
							}
							else if(pausa){
								estadoActual.setText("Pausa");
								while(!continua){
									
								}
								continua = false;
								pausa = false;
								estadoActual.setText("Continua");
							}
							else if(error){
								estadoActual.setText("Error");
								break;
							}
							else{
								contadorTrans++;
								listaLotes.elementAt(i)[procLoteUsados].tiempoRestante--;
								contGlobal++;
							}
						}
						if(i == (j - 1) && contadorLote ==  procLoteUsados){
								nuevoLista2(contadorLote, i);
								if(!interrumpe)
									sinSal = false;
						}
						else{
							nuevoLista2(procLoteUsados, i);
						}
						if(!interrumpe){
							lista2.setModel(md2);
							procLoteUsados++;
						}
						interrumpe = false;
						break;
						case 2:
							md.removeRow(0);
							lista1.setModel(md);
							if(listaLotes.elementAt(i)[2].getOperacion() == 'r')
								operacionProcEje.setText("Operacion: Raiz");
							else
								operacionProcEje.setText("Operacion: "+listaLotes.elementAt(i)[2].getOperacion());
							tiempoMaxProcEje.setText("Tiempo Maximo: "+Integer.toString(listaLotes.elementAt(i)[2].getTiempo()));
							idProcEje.setText("ID: "+listaLotes.elementAt(i)[2].getID());
							contadorTrans = listaLotes.elementAt(i)[procLoteUsados].getTiempo() - listaLotes.elementAt(i)[procLoteUsados].tiempoRestante;
							while(listaLotes.elementAt(i)[procLoteUsados].tiempoRestante >= 0){
								tiempoTransEje.setText("Tiempo Transcurrido Ejecucion: "+contadorTrans);
								tiempoRestEje.setText("Tiempo Restante Ejecucion: "+listaLotes.elementAt(i)[procLoteUsados].tiempoRestante);
								relojGlobal.setText("Reloj Global: "+contGlobal);
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
									interrupcionEs(procLoteUsados,i);
									break;	
								}
								else if(pausa){
									estadoActual.setText("Pausa");
									while(!continua){
										
									}
									continua = false;
									pausa = false;
									estadoActual.setText("Continua");
								}
								else if(error){
									estadoActual.setText("Error");
									break;
								}
								else{
									contadorTrans++;
									listaLotes.elementAt(i)[procLoteUsados].tiempoRestante--;
									contGlobal++;
								}
								
							}
							if(i == (j - 1)){
								nuevoLista2(contadorLote, i);
								if(!interrumpe)
									sinSal = false;
							}
							else{
								nuevoLista2(procLoteUsados, i);
							}
							if(!interrumpe){
								lista2.setModel(md2);
								procLoteUsados = 0;
								sinSal = false;
								break;
							}
							interrumpe = false;
							break;
					}
				}
			//Aqui preguntamos si debemos cargar un nuevo lote
			//Si el lote actual mas uno signf
			if(i != (j - 1))
				cargaNuevoLote(i + 1);

		}
	}

}
