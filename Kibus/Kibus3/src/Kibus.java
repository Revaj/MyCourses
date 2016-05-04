import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Kibus extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel menu;
	private PanelMapaMenu menuMapa;
	private PanelMenuAnimacion menuAnimacion;
	private PanelMenuObjetos menuObjetos;
	private PanelMapa mapa;
	private int xJugador, yJugador;
	private int xCasa, yCasa;
	final public int ALTOMAPA = 20;
	final public int ANCHOMAPA = 20;
	private CapaAbejas abejas = new CapaAbejas();
	
	private Mapa arregloMapa = null;
	private Vector<Mapa> listaMapas = new Vector<Mapa>();
	private Stack<Movimiento> movimientos = new Stack<Movimiento>();
	
	private boolean mapa1Activo, mapa2Activo, mapa3Activo, mapa4Activo, mapaRandomActivo;
	private boolean kibusYaPuesto, casaYaPuesta;
	
	//Practica 2, guardamos la ultima posicion que visitamos
	int ultimaCasillaX;
	int ultimaCasillaY;
	public int usoVel = 0;

	public Kibus(){
		
		//Prepara menu del Oeste
		menu = new JPanel(new BorderLayout());
		this.setLayout(new BorderLayout());
		this.add(menu, BorderLayout.WEST);
		
		//Agrega Menu Mapas
		menuMapa = new PanelMapaMenu();
		menu.add(menuMapa, BorderLayout.SOUTH);
		
		//Agrega Boton Regreso en un Panel inutil
		//Despues vere la manera de reacomodarlo :P
		menuAnimacion = new PanelMenuAnimacion();
	    menu.add(menuAnimacion, BorderLayout.NORTH);
	    
	    //Panel de Objetos
	    menuObjetos = new PanelMenuObjetos();
	    menu.add(menuObjetos, BorderLayout.CENTER);
	    
		verificaArchivoMapas();
		cargaMapa();
	    
	    //Panel del Mapa
		//Por default carga el mapa 1
	    mapa = new PanelMapa(listaMapas.firstElement());
	    this.add(mapa, BorderLayout.CENTER);
		
	    //agrega handler del mouse
	  		
		mapa1Activo = true;
		mapa2Activo = mapa3Activo = mapa4Activo = mapaRandomActivo = false;
		
		kibusYaPuesto = false;
		casaYaPuesta = false;

	    
		this.setSize(550, 550);
		this.setTitle("KIBUS 1");
		this.setFocusable(true);
		this.setVisible(true);
		this.setExtendedState(this.getExtendedState() | Kibus.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		verificaArchivoMapas();
	}
	
	public boolean verificaArchivoMapas(){
		boolean encuentra = true;
		System.out.println("File path: " + new File("mapa.txt").getAbsolutePath());
		return encuentra;
	}
	
	//Formato
	//Mapa
	//Posicion Kibus
	//Posicion Casa
	public void cargaMapa(){
		try {
			int linea = 0;
			int arregloTmp[][] = new int[ANCHOMAPA][ALTOMAPA];
			BufferedReader lector =new BufferedReader(new FileReader("mapa.txt"));
			String lineaArchivo = null;
			StringTokenizer tokenizer;
			try {
				while((lineaArchivo = lector.readLine()) != null){
					if(lineaArchivo.isEmpty())
						continue;
					else{
						tokenizer = new StringTokenizer(lineaArchivo, ",");
						for(int j = 0; j < ANCHOMAPA; j++){
							arregloTmp[linea][j] = Integer.parseInt(tokenizer.nextToken());
						}
						if(++linea == ALTOMAPA){//Lee las posiciones de kibus y la casa
							lineaArchivo = lector.readLine();
							tokenizer = new StringTokenizer(lineaArchivo, ",");
							xJugador = Integer.parseInt(tokenizer.nextToken());
							yJugador = Integer.parseInt(tokenizer.nextToken());
							xCasa = Integer.parseInt(tokenizer.nextToken());
							yCasa = Integer.parseInt(tokenizer.nextToken());
							linea = 0;
							listaMapas.add(new Mapa(arregloTmp, xJugador, yJugador, xCasa, yCasa));
							arregloTmp = new int[ANCHOMAPA][ALTOMAPA];
						}
					}
					
				}
			arregloMapa =  new Mapa(listaMapas.firstElement());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error al leer el archivo mapa.txt, no se encuentra o esta"
					+ "daniado");
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Kibus n1 = new Kibus();
		n1.ejecutaKibus();
	}
		
	//Funcion principal de Kibus
	public void ejecutaKibus(){
		while(true){
			cambioMapa();
			cambiaObjeto();
			
			
			if(menuAnimacion.isEmpiezaAnimacion()){
				menuAnimacion.setEmpiezaAnimacion(false);
				empiezanAbejas();
			}
			else if(menuObjetos.isGuardando()){
				guardaMapa();
				menuObjetos.setGuardando(false);
			}else if(menuObjetos.isCargando()){
				cargaMapaHecho();
				menuObjetos.setCargando(false);
			}
			
		}
	}
	
	/**
	 * Practica 3:
	 * Por medio de las abejas desarrollamos las rutas, la mejor ruta
	 * se guardara automaticamente y la elegira kibus.
	 * Cuando dos o mas abejas se crucen se colocara un simbolo correspondiente
	 * a este hecho. Si kibus se encima sobre ellas no hay problema. Las abejas
	 * siguen ahi y no tendran instintos asesinos de odio.
	 * 
	 * Cuando lleguen a puntos de bloque las abejas enfriaran los lugares
	 * Cuando kibus llegue a la casa, lo "sabra". En cada movimiento que haga analiza si
	 * llega o no. Sus movimientos son generados ahora por las abejas.
	 */
	private void empiezanAbejas() {
		// TODO Auto-generated method stub
		boolean kibusEncuentraCasa = false;
		Vector<Movimiento> mejorRuta = null;
		while(!kibusEncuentraCasa){
			/**
			 * Con las abejas desplegadas empezaremos turnos de 5 pasos
			 * 
			 *	Para cada abeja hasta que sea 5
			 *	
			 *	Comienzo mi ruta
			 *	Al final de mi ruta Elije posición al azar valida
			 *	Si me topo con una zona invalida vuelvo hacia mi ultima posición sana,
			 *	En esa posición enfrio mi zona
			 *	Repite hasta tener zona mas calidad o igual a la que tuve
			 *  Muévete a esa zona y agrega el movimiento
			 *		Vuelve a tu zona
			 *	Si la ruta de una es mejor que la mia la copio  
			 *	Sino sigo igual
			 *
			 *	Verifico mejor ruta de la abeja
			 *	Sigo Abeja
			 *	Repito hasta llegar a casa
			 *
			 */
			
			mejorRuta = dameMejorRuta(abejas);
			while(mejorRuta.size() > 0 ){
				//Borra la casilla de Kibus
				arregloMapa.modificaCasilla(arregloMapa.getxKibus(), arregloMapa.getyKibus() ,0);
				//Actualiza su casilla
				mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus() ,0);
				arregloMapa.actualizaKibus(mejorRuta.firstElement().getValorActualX(),
	                    mejorRuta.firstElement().getValorActualY());
				mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus() , 1);
				mejorRuta.remove(0);
				usoVel = asignaVelocidad();
				try {
					Thread.sleep(usoVel);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(arregloMapa.kibusEnCasa()){
					kibusEncuentraCasa = true;
					break;
				}else{//Mis abejas se murieron :( es necesario hacer otras abejas 
					mapa.liberaPosiciones(abejas);
					abejas = dameNuevasAbejas(arregloMapa.getxKibus(), arregloMapa.getyKibus());
				}
			}
		}
		JOptionPane.showMessageDialog(null, "Kibus encontro la casa!");
	}
	
	private Vector<Movimiento> dameMejorRuta(CapaAbejas abejas2) {
		// TODO Auto-generated method stub
		Vector<Movimiento> mejorRuta = null;
		horaDanza();
		Abeja mejorAbeja = null;
		//Escogemos la primer abeja
		mejorAbeja = abejas2.arreglo[0];
		for(int j = 0; j < 5; j++){
			if(mejorAbeja.getPosicionCalor() < abejas2.arreglo[j].getPosicionCalor()){
				mejorAbeja = abejas2.arreglo[j];
			}
		}
		mejorRuta = mejorAbeja.getRuta();
		return mejorRuta;
	}
	
	public void actualizaAbeja(int x, int y, int posicionAbeja){
		if(estoyConOtras(x, y, posicionAbeja)){
			mapa.eliminaAbejas(abejas.arreglo[posicionAbeja].getPosicionX(), abejas.arreglo[posicionAbeja].getPosicionY());
		}
		else{
			mapa.actualizaMapa(abejas.arreglo[posicionAbeja].getPosicionX(), abejas.arreglo[posicionAbeja].getPosicionY(), 0);
		}
		
		abejas.arreglo[posicionAbeja].setPosicionX(x);
		abejas.arreglo[posicionAbeja].setPosicionY(y);
	
		if(estoyConOtras(abejas.arreglo[posicionAbeja].getPosicionX(), abejas.arreglo[posicionAbeja].getPosicionY(), posicionAbeja)){
			mapa.agregaMasAbejas(abejas.arreglo[posicionAbeja].getPosicionX(), abejas.arreglo[posicionAbeja].getPosicionY());
		}else{
			mapa.actualizaMapa(abejas.arreglo[posicionAbeja].getPosicionX(), abejas.arreglo[posicionAbeja].getPosicionY(), 5);
		}
	}
	
	public void sigueRuta(int j){
		for(int i = 0; i < abejas.arreglo[j].getRuta().size(); i++){
			//Elimina la posicion actual
			if(estoyConOtras(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY(), j)){
				mapa.eliminaAbejas(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY());
			}
			else{
				mapa.actualizaMapa(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY(), 0);
			}
			
			abejas.arreglo[j].setPosicionX(abejas.arreglo[j].getRuta().elementAt(i).getValorActualX());
			abejas.arreglo[j].setPosicionY(abejas.arreglo[j].getRuta().elementAt(i).getValorActualY());
		
			if(estoyConOtras(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY(), j)){
				mapa.agregaMasAbejas(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY());
			}else{
				mapa.actualizaMapa(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY(), 5);
			}
		}
		
		usoVel = asignaVelocidad();
		try {
			Thread.sleep(usoVel);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Solo ocupo borrar mi posicion Actual y colocarme en la ruta de inicio
	 */
	public void vuelvoZonaDanza(int j){
		//Elimina la posicion actual
		if(estoyConOtras(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY(), j)){
			mapa.eliminaAbejas(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY());
		}
		else{
			mapa.actualizaMapa(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY(), 0);
		}
		
		abejas.arreglo[j].setPosicionX(abejas.arreglo[j].getRuta().elementAt(0).getValorActualX());
		abejas.arreglo[j].setPosicionY(abejas.arreglo[j].getRuta().elementAt(0).getValorActualY());
	
		if(estoyConOtras(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY(), j)){
			mapa.agregaMasAbejas(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY());
		}else{
			mapa.actualizaMapa(abejas.arreglo[j].getPosicionX(), abejas.arreglo[j].getPosicionY(), 5);
		}
		
		usoVel = asignaVelocidad();
		try {
			Thread.sleep(usoVel);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public boolean estoyConOtras(int i, int j, int posicion){
    	boolean estoyConOtras = false;
    	for(int k = 0; k < 5; k++){
    		if(k != posicion &&
    		   abejas.arreglo[k].getPosicionX() == i &&
    		   abejas.arreglo[k].getPosicionY() == j){
    			
    			estoyConOtras = true;
    			break;
    		}
    	}
    	return estoyConOtras;
    }
	
	/**
	 * Funcion principal, para cada abeja elige posicion al azar,
	 */
	private void horaDanza() {
		// TODO Auto-generated method stub
		final int CANT_ABEJAS = 5;
		final int CANT_CICLOS = 5;
		boolean encuentroPosicion = false;
		boolean estoyAtorada = false;
		
		for(int i = 1; i <= CANT_CICLOS; i++){
			for(int j = 0; j < CANT_ABEJAS; j++){
				encuentroPosicion = false;
				while(!encuentroPosicion){
					int posicion = (int) (Math.random() * 8);
					sigueRuta(j);
					int xAbeja = abejas.arreglo[j].getPosicionX();
					int yAbeja = abejas.arreglo[j].getPosicionY();
					switch(posicion){
						case 0:
							xAbeja--;
							yAbeja--;
						break;
						case 1:
							xAbeja--;
						break;
						case 2:
							xAbeja--;
							yAbeja++;
						break;
						case 3:
							yAbeja--;
						break;
						case 4:
							yAbeja++;
						break;
						case 5:
							xAbeja++;
							yAbeja--;
						break;
						case 6:
							xAbeja++;
						break;
						case 7:
							xAbeja++;
							yAbeja++;
						break;
						default:
						break;
					}
					
					/*
					 * Aqui empezamos a tomar en cuenta los detalles
					 * como el bloqueo, si es la mejor o peor o si debe seguir una la ruta
					 */
					
					if(arregloMapa.colisionaKibus(xAbeja, yAbeja)){
						//Enfriamos la zona actual y realizamos el paso nuevamente
						mapa.enfriaZona(abejas.arreglo[j].getPosicionX(),
								        abejas.arreglo[j].getPosicionY());
						
						//Busca zona disponible para ir
						estoyAtorada = true;
						while(estoyAtorada){
							posicion = (int) (Math.random() * 8);
							xAbeja = abejas.arreglo[j].getPosicionX();
							mapa.enfriaZona(abejas.arreglo[j].getPosicionX(),
							        abejas.arreglo[j].getPosicionY());
							yAbeja = abejas.arreglo[j].getPosicionY();
							switch(posicion){
								case 0:
									xAbeja--;
									yAbeja--;
								break;
								case 1:
									xAbeja--;
								break;
								case 2:
									xAbeja--;
									yAbeja++;
								break;
								case 3:
									yAbeja--;
								break;
								case 4:
									yAbeja++;
								break;
								case 5:
									xAbeja++;
									yAbeja--;
								break;
								case 6:
									xAbeja++;
								break;
								case 7:
									xAbeja++;
									yAbeja++;
								break;
								default:
								break;
							}
							
							//Esta abeja logro salir!!
							if(!arregloMapa.colisionaKibus(xAbeja, yAbeja)){
								abejas.arreglo[j].setPosicionX(xAbeja);
								abejas.arreglo[j].setPosicionY(yAbeja);
								abejas.arreglo[j].setPosicionCalor(mapa.dameCalor(xAbeja, yAbeja));
								estoyAtorada = false;
								abejas.arreglo[j].agregaMovimiento(new Movimiento(xAbeja, yAbeja));
								encuentroPosicion = true;
							}//Fin condicion
						}//Fin del ciclo de atorada
					}//Fin del Si colisiona
					else{
						mapa.enfriaZona(abejas.arreglo[j].getPosicionX(),
						        abejas.arreglo[j].getPosicionY());
						abejas.arreglo[j].setPosicionX(xAbeja);
						abejas.arreglo[j].setPosicionY(yAbeja);
						abejas.arreglo[j].agregaMovimiento(new Movimiento(xAbeja, yAbeja));
						abejas.arreglo[j].setPosicionCalor(mapa.dameCalor(xAbeja, yAbeja));
						encuentroPosicion = true;
					}
				}//Fin del encuentro posicion
				//vuelvoZonaDanza(j);
			}//Fin del turno de las abejas
			
			//Comparas el ciclo de las demas abejas, la mejor ruta que la tuya la copias,
			//caso contrario sigues igual
			//Obten primero la mejor abeja
			Abeja mejorAbeja = abejas.arreglo[0];
			for(int k = 0; k < 5; k++){
				if(abejas.arreglo[k].getPosicionCalor() > mejorAbeja.getPosicionCalor()){
					mejorAbeja = abejas.arreglo[k];
				}
			}
			
			//Con la mejor abeja buscamos quien tiene menor calor, la que este en una zona con menos
			//calor le copiamos la posicion y ruta
			for(int k = 0; k < 5; k++){
				if(abejas.arreglo[k].getPosicionCalor() < mejorAbeja.getPosicionCalor()){
					abejas.arreglo[k].setPosicionCalor(mejorAbeja.getPosicionCalor());
					actualizaAbeja(mejorAbeja.getPosicionX(), mejorAbeja.getPosicionY(), k);
					
					
					//Copia movimientos
					abejas.arreglo[k].copiaRuta(mejorAbeja.getRuta());
				}
			}
		}//Fin del ciclo de busqueda
	}//Fin de la funcion

	public int asignaVelocidad(){
		int velocidad = menuAnimacion.getVelocidad().getValue();
		int vel = 0;
		if(velocidad == 0){
			vel = 100;
			vel = 50;
		}
		else if(velocidad == 1){
			vel = 500;
		}
		else if(velocidad == 2){
			vel = 1000;
		}
		return vel;
	}
	
	public void cargaMapaHecho(){
		int fila = -1;
		
		fila = menuObjetos.tablaMapas.getSelectedRow();
		setKibusYaPuesto(false);
		setCasaYaPuesta(false);
		if(fila >= 0){
			String mapaUsar = (String) menuObjetos.tablaMapas.getValueAt(fila, 0);
			try {
				BufferedReader mapa1 =new BufferedReader(new FileReader(mapaUsar + ".txt"));
				String lineaArchivo = null;
				StringTokenizer tokenizer;
				int arregloTmp[][] = new int[ANCHOMAPA][ALTOMAPA];
				int linea = 0;
				try {
					while((lineaArchivo = mapa1.readLine()) != null){
						if(lineaArchivo.isEmpty())
							continue;
						else{
							tokenizer = new StringTokenizer(lineaArchivo, ",");
							for(int j = 0; j < ANCHOMAPA; j++){
								arregloTmp[linea][j] = Integer.parseInt(tokenizer.nextToken());
							}
							if(++linea == ALTOMAPA){//Lee las posiciones de kibus y la casa
								lineaArchivo = mapa1.readLine();
								tokenizer = new StringTokenizer(lineaArchivo, ",");
								xJugador = Integer.parseInt(tokenizer.nextToken());
								yJugador = Integer.parseInt(tokenizer.nextToken());
								xCasa = Integer.parseInt(tokenizer.nextToken());
								yCasa = Integer.parseInt(tokenizer.nextToken());
								linea = 0;
								arregloMapa =  new Mapa(arregloTmp, xJugador, yJugador, xCasa, yCasa);
								mapa.limpiaMapa();
								mapa.cargaMapa(arregloMapa);
							}
						}
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	}
	
	public void guardaMapa(){
		try {
			BufferedWriter anotaLista =new BufferedWriter(new FileWriter("listaMapas.txt", true));
			anotaLista.newLine();
			anotaLista.write("mapa" + menuObjetos.tablaMapas.getRowCount() + 1);
			BufferedWriter mapa = new BufferedWriter(new FileWriter("mapa" 
			+ menuObjetos.tablaMapas.getRowCount() + 1 + ".txt"));
			
			for(int i = 0; i < 20; i++){
				for(int j = 0; j < 20; j++){
					mapa.write(Integer.toString(arregloMapa.getArreglo()[i][j]));
					if(j < 19)
						mapa.write(",");
				}
				mapa.newLine();
			}
			
			mapa.write(Integer.toString(arregloMapa.getxKibus()));
			mapa.write(",");
			mapa.write(Integer.toString(arregloMapa.getyKibus()));
			mapa.write(",");
			mapa.write(Integer.toString(arregloMapa.getxCasa()));
			mapa.write(",");
			mapa.write(Integer.toString(arregloMapa.getyCasa()));
			
			anotaLista.close();
			mapa.close();
			
			menuObjetos.actualizaModelo("mapa"+ menuObjetos.tablaMapas.getRowCount() + 1 );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void cambiaObjeto(){
		if(mapa.isModificaCasilla()){
			mapa.setModificaCasilla(false);
			
			//Procede a modificar Casilla
			int x, y, tipoObjeto;
			x = mapa.getPosicionObjetoX();
			y = mapa.getPosicionObjetoY();


			
			tipoObjeto = arregloMapa.getArreglo()[x][y];
			if(menuObjetos.isEliminarActivo()){
				switch(tipoObjeto){
				case 1:
					arregloMapa.setxKibus(0);
					arregloMapa.setyKibus(0);
					setKibusYaPuesto(false);
					arregloMapa.modificaCasilla(x, y, 0);
					mapa.actualizaMapa(x, y, 0);
					abejas.cantidad = 0;
					mapa.eliminaAbejas(abejas);
					abejas = new CapaAbejas();
					break;
				case 2:
					arregloMapa.setxCasa(0);
					arregloMapa.setyCasa(0);
					setCasaYaPuesta(false);
					arregloMapa.modificaCasilla(x, y, 0);
					mapa.actualizaMapa(x, y, 0);
					mapa.eliminaCalor();
					break;
				case 3:
				case 4:
					arregloMapa.modificaCasilla(x, y, 0);
					mapa.actualizaMapa(x, y, 0);
					break;
				default:
				break;
				}
			}else if(menuObjetos.isRocaActivo()){
				switch(tipoObjeto){
				case 1:
					arregloMapa.setxKibus(0);
					arregloMapa.setyKibus(0);
					setKibusYaPuesto(false);
					arregloMapa.modificaCasilla(x, y, 3);
					mapa.actualizaMapa(x, y, 3);
					mapa.eliminaAbejas(abejas);
					abejas = new CapaAbejas();
				case 2:
					arregloMapa.setxCasa(0);
					arregloMapa.setyCasa(0);
					setCasaYaPuesta(false);
					arregloMapa.modificaCasilla(x, y, 3);
					mapa.actualizaMapa(x, y, 3);
					mapa.eliminaCalor();
					break;
				case 0:
				case 4:
					arregloMapa.modificaCasilla(x, y, 3);
					mapa.actualizaMapa(x, y, 3);
					break;
				default:
				break;
				}
			}else if(menuObjetos.isArbolActivo()){
				switch(tipoObjeto){
				case 1:
					arregloMapa.setxKibus(0);
					arregloMapa.setyKibus(0);
					setKibusYaPuesto(false);
					arregloMapa.modificaCasilla(x, y, 4);
					mapa.actualizaMapa(x, y, 4);
					mapa.eliminaAbejas(abejas);
					abejas = new CapaAbejas();
				case 2:
					arregloMapa.setxCasa(0);
					arregloMapa.setyCasa(0);
					setCasaYaPuesta(false);
					arregloMapa.modificaCasilla(x, y, 4);
					mapa.actualizaMapa(x, y, 4);
					mapa.eliminaCalor();
					break;
				case 0:
				case 3:
					arregloMapa.modificaCasilla(x, y, 4);
					mapa.actualizaMapa(x, y, 4);
					break;
				default:
				break;
				}
			//AgregaKibus
			}else if(menuObjetos.isKibusActivo() && !this.isKibusYaPuesto()){
				switch(tipoObjeto){
				case 0:
				case 3:
				case 4:
					arregloMapa.setxKibus(x);
					arregloMapa.setyKibus(y);
					setKibusYaPuesto(true);
					arregloMapa.modificaCasilla(x, y, 1);
					mapa.actualizaMapa(x, y, 1);
					abejas = dameNuevasAbejas(x, y);
					break;
				default:
				break;
				}
			//Agrega Casa
			}else if(menuObjetos.isCasaActivo() && !this.isCasaYaPuesta()){
				switch(tipoObjeto){
				case 0:
				case 3:
				case 4:
					arregloMapa.setxCasa(x);
					arregloMapa.setyCasa(y);
					setCasaYaPuesta(true);
					arregloMapa.modificaCasilla(x, y, 2);
					mapa.actualizaMapa(x, y, 2);
					mapa.despliegaCalor(x, y);
					break;
				default:
				break;
				}
			}
		}
	}
	
	private CapaAbejas dameNuevasAbejas(int x, int y) {
		// TODO Auto-generated method stub
		CapaAbejas tmp = new CapaAbejas();
		int xAbeja = x;
		int yAbeja = y;
		int colocadas = 0;
		//GeneraPosicionesAleatorias de las abejas
		while(colocadas < 5){
			int posicion = (int) (Math.random() * 8);
			xAbeja = x;
			yAbeja = y;
			switch(posicion){
				case 0:
					xAbeja--;
					yAbeja--;
				break;
				case 1:
					xAbeja--;
				break;
				case 2:
					xAbeja--;
					yAbeja++;
				break;
				case 3:
					yAbeja--;
				break;
				case 4:
					yAbeja++;
				break;
				case 5:
					xAbeja++;
					yAbeja--;
				break;
				case 6:
					xAbeja++;
				break;
				case 7:
					xAbeja++;
					yAbeja++;
				break;
				default:
				break;
			}
			
			/*
			 * Aqui validamos varias cosas
			 * Primero la posicion debe estar dentro del rango 0-19
			 * Segundo si la posicion esta ocupada cambiamos una imagen
			 * que represente la n cantidad de abejas
			 * Puede haber hasta 4 posiciones que representen la ocupacion
			 * donde por lo menos haya 2 abejas
			 * 
			 */
			if(!arregloMapa.colisionaKibus(xAbeja, yAbeja)){
				Abeja tmp1 = new Abeja(xAbeja, yAbeja);
				tmp1.agregaMovimiento(new Movimiento(xAbeja, yAbeja));
				tmp1.setPosicionCalor(mapa.dameCalor(xAbeja, yAbeja));
				int posicionActual = colocadas;
				tmp.arreglo[colocadas++] = tmp1;
				boolean estaDisponible = true;
				tmp.cantidad++;
				for(int i = 0; i < colocadas; i++){
					if(posicionActual != i &&
					   tmp.arreglo[i].getPosicionX() == xAbeja &&
					   tmp.arreglo[i].getPosicionY() == yAbeja){
						//Copionas D: agregamos mas abejas
						mapa.agregaMasAbejas(xAbeja, yAbeja);
						estaDisponible = false;
						break;
					}
				}
				/**
				 * Como el lugar no hubo una copia entonces agregamos la abeja
				 */
				if(estaDisponible){
					mapa.actualizaMapa(xAbeja, yAbeja, 5);
				}
			}
		}
		return tmp;
	}

	public void cambioMapa(){
		if(menuMapa.isHuboCambio()){
			menuMapa.setHuboCambio(false);

			
			//Procedemos a cambiar el Mapa
			//Verifica tambien si el mapa que quiero cambiar 
			//Ya esta activo para no retrasar el cambio

			
			if(menuMapa.isMapa1Activo() && !this.isMapa1Activo()){
				arregloMapa = new Mapa(listaMapas.firstElement());
				mapa.limpiaMapa();
				mapa.cargaMapa(arregloMapa);
				mapa1Activo = true;
				mapa2Activo = mapa3Activo = mapa4Activo = mapaRandomActivo = false;
			}
			if(menuMapa.isMapa2Activo() && !this.isMapa2Activo()){
				mapa.limpiaMapa();
				arregloMapa = new Mapa(listaMapas.elementAt(1));
				mapa.cargaMapa(arregloMapa);
				mapa2Activo = true;
				mapa1Activo = mapa3Activo = mapa4Activo = mapaRandomActivo = false;
				
			}
			if(menuMapa.isMapa3Activo() && !this.isMapa3Activo()){
				mapa.limpiaMapa();
				arregloMapa = new Mapa(listaMapas.elementAt(2));
				mapa.cargaMapa(arregloMapa);
				mapa3Activo = true;
				mapa2Activo = mapa1Activo = mapa4Activo = mapaRandomActivo = false;
			}
			if(menuMapa.isMapa4Activo() && !this.isMapa4Activo()){
				mapa.limpiaMapa();
				arregloMapa = new Mapa(listaMapas.elementAt(3));
				mapa.cargaMapa(arregloMapa);
				mapa4Activo = true;
				mapa2Activo = mapa3Activo = mapa1Activo = mapaRandomActivo = false;
			}
			if(menuMapa.isMapaRandomActivo() && !this.isMapaRandomActivo()){
				mapa.limpiaMapa();
				hagamosMapa();
				mapa.cargaMapa(arregloMapa);
				mapaRandomActivo = true;
				mapa2Activo = mapa3Activo = mapa1Activo = mapa4Activo = false;
			}
		}
	}
	
	public void hagamosMapa(){
		int arregloNvo[][] = new int [20][20];
		int nvoPosicion;
		int xC, yC, xK, yK;
		boolean kibusPuesto = false;
		boolean casaPuesta = false;
		int obstaculos = (int) (20 * 20 * menuObjetos.getValorObstaculos());
		int obstaculosFila = obstaculos / 20;
		int tmpFila = obstaculosFila;
		
		xC = yC = xK = yK = 0;
		
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 20; j++){
				nvoPosicion = (int) (Math.random() * 5);
				switch(nvoPosicion){
					case 0:
						arregloNvo[i][j] = nvoPosicion;
						break;
					case 3:
					case 4:
						if(tmpFila > 0){
							arregloNvo[i][j] = nvoPosicion;
							tmpFila--;
						}
						else{
							arregloNvo[i][j] = 0;
						}
						break;
					case 1://Kibus
						if(!kibusPuesto){
							xK = i;
							yK = j;
							arregloNvo[i][j] = nvoPosicion;
							kibusPuesto = true;
						}
						else{
							arregloNvo[i][j] = 0;							
						}
						break;
					case 2://Casa
						if(!casaPuesta){
							xC = i;
							yC = j;
							arregloNvo[i][j] = nvoPosicion;
							casaPuesta = true;
							kibusPuesto = true;
						}
						else{
							arregloNvo[i][j] = 0;							
						}
						break;
					default:
						break;
				}
			}
			tmpFila = obstaculosFila;
		}
		arregloMapa = null;
		arregloMapa = new Mapa(arregloNvo, xC, yC, xC, yC);
	}
	

	public PanelMenuObjetos getMenuObjetos() {
		return menuObjetos;
	}

	public void setMenuObjetos(PanelMenuObjetos menuObjetos) {
		this.menuObjetos = menuObjetos;
	}

	public int getyJugador() {
		return yJugador;
	}

	public void setyJugador(int yJugador) {
		this.yJugador = yJugador;
	}

	public int getxJugador() {
		return xJugador;
	}

	public void setxJugador(int xJugador) {
		this.xJugador = xJugador;
	}

	public int getyCasa() {
		return yCasa;
	}

	public void setyCasa(int yCasa) {
		this.yCasa = yCasa;
	}

	public int getxCasa() {
		return xCasa;
	}

	public void setxCasa(int xCasa) {
		this.xCasa = xCasa;
	}

	public Mapa getArregloMapa() {
		return arregloMapa;
	}

	public void setArregloMapa(Mapa arregloMapas) {
		this.arregloMapa = arregloMapas;
	}
	
	public boolean isMapa3Activo() {
		return mapa3Activo;
	}

	public void setMapa3Activo(boolean mapa3Activo) {
		this.mapa3Activo = mapa3Activo;
	}

	public boolean isMapaRandomActivo() {
		return mapaRandomActivo;
	}

	public void setMapaRandomActivo(boolean mapaRandomActivo) {
		this.mapaRandomActivo = mapaRandomActivo;
	}

	public boolean isMapa2Activo() {
		return mapa2Activo;
	}

	public void setMapa2Activo(boolean mapa2Activo) {
		this.mapa2Activo = mapa2Activo;
	}

	public boolean isMapa1Activo() {
		return mapa1Activo;
	}

	public void setMapa1Activo(boolean mapa1Activo) {
		this.mapa1Activo = mapa1Activo;
	}

	public boolean isMapa4Activo() {
		return mapa4Activo;
	}

	public void setMapa4Activo(boolean mapa4Activo) {
		this.mapa4Activo = mapa4Activo;
	}

	public boolean isKibusYaPuesto() {
		return kibusYaPuesto;
	}

	public void setKibusYaPuesto(boolean kibusYaPuesto) {
		this.kibusYaPuesto = kibusYaPuesto;
	}

	public boolean isCasaYaPuesta() {
		return casaYaPuesta;
	}

	public void setCasaYaPuesta(boolean casaYaPuesta) {
		this.casaYaPuesta = casaYaPuesta;
	}

	public Stack<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(Stack<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}
}
