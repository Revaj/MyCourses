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
public class Monitor {
	public Vector <Proceso []> listaLotes = new Vector<Proceso[]>();
	private JFrame ventanaMonitor;
	private JLabel numeroLotes;
	private JLabel relojGlobal;
	private JLabel nombreProgram;
	private JLabel tiempoMaximo;
	private JLabel nombreProcEje;
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
	private JTable lista1;
	private JTable lista2;
	private JScrollPane corredor;
	
	private int contGlobal;
	private int loteRestante;
	private int loteActual = 1;
	private int procLoteUsados = 0;
	private int contadorLote;
	
	private DefaultTableModel md = new DefaultTableModel();
	private DefaultTableModel md2 = new DefaultTableModel();
	
	public Monitor(int cantLotes, int numProc, Vector <Proceso []> totalLotes) throws InterruptedException{
		ventanaMonitor = new JFrame("Practica 1");
		ventanaMonitor.setLayout(null);
	    numeroLotes = new JLabel("Lotes Restantes: "+(--cantLotes));
	    loteRestante = cantLotes;
	    relojGlobal = new JLabel("Reloj Global: "+contGlobal);
	    ventanaMonitor.add(numeroLotes);
	    numeroLotes.setBounds(0, 0, 120, 50);
	    ventanaMonitor.add(relojGlobal);
	    relojGlobal.setBounds(1000, 650, 140, 60);
	    
	    //Bloque izquierdo
	    JPanel loteEjecucion = new JPanel();
	    loteEjecucion.setLayout(null);
	    loteEjecucion.setBorder(BorderFactory.createTitledBorder("Lote en Ejecución"));
	    
	    nombreProgram = new JLabel("Nombre del Programador");
	    loteEjecucion.add(nombreProgram);
	    nombreProgram.setBounds(10, 0, 200, 50);
	    
	    tiempoMaximo = new JLabel("Tiempo Maximo Estimado");
	    loteEjecucion.add(tiempoMaximo);
	    tiempoMaximo.setBounds(200, 0, 200, 50);
	    
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
	    
	    nombreProcEje = new JLabel("Nombre del Programador: ");
	    procEjecucion.add(nombreProcEje);
	    nombreProcEje.setBounds(10, 20, 200, 30);
	    
	    operacionProcEje = new JLabel("Operacion: ");
	    procEjecucion.add(operacionProcEje);
	    operacionProcEje.setBounds(10, 50, 200, 30);
	    
	    tiempoMaxProcEje = new JLabel("Tiempo Maximo Estimado: ");
	    procEjecucion.add(tiempoMaxProcEje);
	    tiempoMaxProcEje.setBounds(10, 75, 200, 50);

	    idProcEje = new JLabel("Numero del Programa: ");
	    procEjecucion.add(idProcEje);
	    idProcEje.setBounds(10, 110, 200, 50);
	    
	    tiempoTransEje = new JLabel("Tiempo transcurrido de Ejecución: ");
	    procEjecucion.add(tiempoTransEje);
	    tiempoTransEje.setBounds(10, 145, 200, 30);
	    
	    tiempoRestEje = new JLabel("Tiempo restante de Ejecución: ");
	    procEjecucion.add(tiempoRestEje);
	    tiempoRestEje.setBounds(10, 180, 200, 30);
	    
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
	    procEjecucion.setBounds(410, 50, 400, 250);
	    
	    ventanaMonitor.add(terminados);
	    terminados.setBounds(850, 50, 550, 300);
	    
	    ventanaMonitor.setExtendedState(JFrame.MAXIMIZED_BOTH);
		ventanaMonitor.setVisible(true);
		procLoteUsados = 0;
		
	}
	
	public void cargaLista1() throws InterruptedException{
		md.addColumn("Nombre del Programador");
		md.addColumn("Tiempo Maximo Estimado");
		String [] fila = new String[2];
		if(contadorLote == 2 || loteRestante >= 1)
			for (int i = 0; i < 3; i++){
				fila[0] = listaLotes.elementAt(0)[i].getNombreProg();
				fila[1] = Integer.toString(listaLotes.elementAt(0)[i].getTiempo());
				md.addRow(fila);
			}
		else
			for (int i = 0; i <= contadorLote; i++){
				fila[0] = listaLotes.elementAt(0)[i].getNombreProg();
				fila[1] = Integer.toString(listaLotes.elementAt(0)[i].getTiempo());
				md.addRow(fila);
			}
		lista1.setModel(md);
		Thread.sleep(300);	
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
		algo[5] = Integer.toString(loteActual);
		md2.addRow(algo);
	}
	
	//Cargamos un nuevo lote
	public void cargaNuevoLote(int nuevo) throws InterruptedException{
		loteActual++;
		numeroLotes.setText("Lotes Restantes: "+(--loteRestante));
		String [] fila = new String[2];
		if(contadorLote == 2 || loteRestante >= 1)
			for (int i = 0; i < 3; i++){
				fila[0] = listaLotes.elementAt(nuevo)[i].getNombreProg();
				fila[1] = Integer.toString(listaLotes.elementAt(nuevo)[i].getTiempo());
				md.addRow(fila);
			}
		else
			for (int i = 0; i <= contadorLote; i++){
				fila[0] = listaLotes.elementAt(nuevo)[i].getNombreProg();
				fila[1] = Integer.toString(listaLotes.elementAt(nuevo)[i].getTiempo());
				md.addRow(fila);
			}
		lista1.setModel(md);
		Thread.sleep(300);	
	}
	
	//El programa principal donde corre la segunda parte
	public void corre() throws InterruptedException{
		int contadorTrans = 0;
		int contadorRest = 0;
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
						nombreProcEje.setText("Nombre del Programador:" +listaLotes.elementAt(i)[procLoteUsados].getNombreProg());
						if(listaLotes.elementAt(i)[procLoteUsados].getOperacion() == 'r')
							operacionProcEje.setText("Operacion: Raiz");
						else
							operacionProcEje.setText("Operacion: "+listaLotes.elementAt(i)[procLoteUsados].getOperacion());
						tiempoMaxProcEje.setText("Tiempo Maximo: "+Integer.toString(listaLotes.elementAt(i)[procLoteUsados].getTiempo()));
						idProcEje.setText("ID: "+listaLotes.elementAt(i)[procLoteUsados].getID());
						contadorRest = listaLotes.elementAt(i)[procLoteUsados].getTiempo();
						while(contadorRest >= 0){
							tiempoTransEje.setText("Tiempo Transcurrido Ejecucion: "+Integer.toString(contadorTrans++));
							tiempoRestEje.setText("Tiempo Restante Ejecucion: "+Integer.toString(contadorRest--));
							relojGlobal.setText("Reloj Global: "+(contGlobal++));
							Thread.sleep(1000);	
						}
						if(i == (j - 1) && contadorLote ==  procLoteUsados){
								nuevoLista2(contadorLote, i);
								sinSal = false;
						}
						else{
							nuevoLista2(procLoteUsados, i);
						}
						lista2.setModel(md2);
						procLoteUsados++;
						break;
						case 2:
							md.removeRow(0);
							lista1.setModel(md);
							nombreProcEje.setText("Nombre del Programador:" +listaLotes.elementAt(0)[2].getNombreProg());
							if(listaLotes.elementAt(0)[2].getOperacion() == 'r')
								operacionProcEje.setText("Operacion: Raiz");
							else
								operacionProcEje.setText("Operacion: "+listaLotes.elementAt(0)[2].getOperacion());
							tiempoMaxProcEje.setText("Tiempo Maximo: "+Integer.toString(listaLotes.elementAt(0)[2].getTiempo()));
							idProcEje.setText("ID: "+listaLotes.elementAt(0)[2].getID());
							contadorRest = listaLotes.elementAt(0)[2].getTiempo();
							while(contadorRest >= 0){
								tiempoTransEje.setText("Tiempo Transcurrido Ejecucion: "+Integer.toString(contadorTrans++));
								tiempoRestEje.setText("Tiempo Restante Ejecucion: "+Integer.toString(contadorRest--));
								relojGlobal.setText("Reloj Global: "+(contGlobal++));
								Thread.sleep(1000);
								
							}
							if(i == (j - 1)){
								nuevoLista2(contadorLote, i);
								sinSal = false;
							}
							else{
								nuevoLista2(procLoteUsados, i);
							}
							lista2.setModel(md2);
							procLoteUsados = 0;
							sinSal = false;
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
