import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @author Javier Agustín Rizo Orozco
 * Practica 6: Productor / Consumidor
 * Practica que simula la concurrencia con el modelo del productor / consumidor
 */
public class SimuladorConcurrencia {
	//Contenedor de elementos
	char contenedor[];
	//Soporte del contenedor de elementos para la tabla visual
	String contenedorTabla[];
	String numeracion[];
	int cantidad;
	//Productor y consumidor
	public Productor p;
	public Consumidor c;
	public JLabel productorEtiqueta;
	public JLabel consumidorEtiqueta;
	
	private JFrame ventana;
	private DefaultTableModel md;
	private JPanel elementos;
	private JTable tablaElementos;
	
	public SimuladorConcurrencia(){
		contenedor = new char[40];
		contenedorTabla = new String[40];
		numeracion = new String[40];
		ventana = new JFrame ("Practica 6");
		ventana.setLayout(null);
	
		ventana.addKeyListener(new KeyListener(){ 
			public void keyPressed(KeyEvent evt){
				if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
					ventana.setVisible(false);
			}
	
			@Override
			public void keyReleased(KeyEvent evt) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyTyped(KeyEvent evt) {
			}


		});
		
		//Prepara contenedores
		llenaVacio();
		llenaTabla();
		
		//Prepara los objetos para consumir / producir
		p = new Productor();
		p.setPriority(Thread.MAX_PRIORITY);
		c = new Consumidor();
		c.setPriority(Thread.MAX_PRIORITY);
		
		//Bloque central
		elementos = new JPanel();
	    elementos.setLayout(null);
	    elementos.setBorder(BorderFactory.createTitledBorder("Contenedor"));
	    
	    md = new DefaultTableModel();
	    md.addColumn("Numero", numeracion);
	    md.addColumn("Elementos", contenedorTabla);
	    
	    tablaElementos = new JTable(md);
	    elementos.add(tablaElementos);
	    tablaElementos.setBounds(30, 20, 100, 640);
	    
	    ventana.add(elementos);
	    elementos.setBounds(600, 0, 160, 700);
		
	    consumidorEtiqueta = new JLabel("Consumidor Dormido");
	    ventana.add(consumidorEtiqueta);
	    consumidorEtiqueta.setBounds(50,100,200,50);
	    
	    productorEtiqueta = new JLabel("Productor Dormido");
	    ventana.add(productorEtiqueta);
	    productorEtiqueta.setBounds(800,100,200,50);
	    
	    ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
		ventana.setVisible(true);
			
		cantidad = 0;
	}
	
	/*
	 * La funcion llena vacio nos permitira dar un caracter de espacio a cada posicion del contenedor en vacio,
	 * esto con el fin de poder mostrar por completo el contenedor en la tabla y no tene problemas. Ademas llenara aparte una lista de numeracion del 1-40 para la tabla
	 */
	public void llenaVacio(){
		for(int i = 0; i < 40; i++){
			contenedor[i] = ' ';
			numeracion[i] = Integer.toString(i + 1);
		}
	}
	
	/*
	 * La funcion llena tabla nos permitira llenar la tabla que se mostrara la ventana con el contenido de nuestro arreglo,
	 * esto con el fin de poder mostrar su contenido. Se usara tambien un arreglo de cadenas, este arreglo sera la base del modelo
	 * de la tabla, con el fin de poder estar actualizandolo en base al contenedor.
	 */
	public void llenaTabla(){
		for(int i = 0; i < 40; i++)
			contenedorTabla[i] = Character.toString(contenedor[i]);
	}
	
	//Aqui sucede la accion principal de consumir o producir
	public void correSimulacion(){
		p.start();
		c.start();

		while(true){
			//Comienza primero el productor
			productorEtiqueta.setText("Productor Intentando entrar");
			consumidorEtiqueta.setText("Consumidor Intentando entrar");
			p.estadoDormido = true;
			if(p.estadoDormido){
				if(cantidad < 40){
					consumidorEtiqueta.setText("Consumidor Durmiendo");
					while(cantidad < 40 && p.produci < 6){
						//Vuelve al inicio a producir
						if(p.posicion == 40)
							p.posicion = 0;
						productorEtiqueta.setText("Productor Trabajando");
						for(int i = p.posicion; i < 40; i++){
							ventana.requestFocus();
							p.posicion = i + 1;
							c.estadoDormido = false;
							try {
								Thread.sleep((int) (500));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(contenedor[i] == ' '){
								contenedor[i] = p.dameProducto();
							
								//Corregimos los espacios en blanco
								if(contenedor[i] == ' ')
									contenedor[i] = '4';
								
								cantidad++;
								//Envia los objetos a la tabla
								llenaTabla();
								md = new DefaultTableModel();
								md.addColumn("Numero", numeracion);
								md.addColumn("Elementos", contenedorTabla);
								tablaElementos.setModel(md);
								if(++p.produci == 6)
									break;
								else if(cantidad >= 40)
									break;
								if(c.estadoDormido){
									p.produci = 6;
									break;
								}
							}
						}
					}
					p.produci = 0;
				}
			}
			p.estadoDormido = false;
			productorEtiqueta.setText("Productor Durmiendo");
			consumidorEtiqueta.setText("Consumidor Intentando Trabajar");
			try {
				ventana.requestFocus();
				Thread.sleep((int) (2000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c.estadoDormido = true;
			if(c.estadoDormido){
				if(cantidad > 0){
					while(cantidad > 0 && c.consumi < 6){
						//Vuelve al inicio de consumir
						if(c.posicion == 40)
							c.posicion = 0;
						consumidorEtiqueta.setText("Consumidor Trabajando");
						for(int i = c.posicion; i < 40; i++){
							p.estadoDormido = false;
							ventana.requestFocus();
							c.posicion = i + 1;
							try {
								Thread.sleep((int) (500));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(contenedor[i] != ' '){
								c.consumeProducto(contenedor[i]);
								contenedor[i] = ' ';
								cantidad--;
								
								//Envia los objetos a la tabla
								llenaTabla();
								md = new DefaultTableModel();
								md.addColumn("Numero", numeracion);
								md.addColumn("Elementos", contenedorTabla);
								tablaElementos.setModel(md);
								if(++c.consumi == 6)
									break;
								else if(cantidad <= 0)
									break;
								
								if(p.estadoDormido){
									c.consumi = 6;
									break;
								}	
							}
						}
					}
					c.consumi = 0;
					c.estadoDormido = false;
				}
				
			}
			consumidorEtiqueta.setText("Consumidor Durmiendo");
			try {
				ventana.requestFocus();
				Thread.sleep((int) (1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) {
		SimuladorConcurrencia sc = new SimuladorConcurrencia();
		sc.correSimulacion();
	}

}
