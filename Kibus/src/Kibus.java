import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	
	private Mapa arregloMapa = null;
	private Vector<Mapa> listaMapas = new Vector<Mapa>();
	public HandlerKeys handler = new HandlerKeys();
	private Stack<Movimiento> movimientos = new Stack<Movimiento>();
	
	private boolean mapa1Activo, mapa2Activo, mapa3Activo, mapa4Activo, mapaRandomActivo;
	private boolean kibusYaPuesto, casaYaPuesta;
	
	


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
		
	    //agrega handler
	    this.addKeyListener(handler);
	    
	    //agrega handler del mouse
	  		
		mapa1Activo = true;
		mapa2Activo = mapa3Activo = mapa4Activo = mapaRandomActivo = false;
		
		kibusYaPuesto = true;
		casaYaPuesta = true;

	    
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
	
	//Moveremos a Kibus verificando antes sino colisiona
	public class HandlerKeys implements KeyListener{
		public void keyPressed(KeyEvent event) {
			if(isKibusYaPuesto()){
			    switch (event.getKeyCode()) {
			        case KeyEvent.VK_LEFT:
			            // up arrow
			        	if(!arregloMapa.colisionaKibus(0, -1)){
			        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 0);
			        		arregloMapa.actualizaKibus(0, -1);
			        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 1);
			        		movimientos.push(new Movimiento(4));
			        	}
			            break;
			        case KeyEvent.VK_RIGHT:
			            // down arrow
			        	if(!arregloMapa.colisionaKibus(0, +1)){
			        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 0);
			        		arregloMapa.actualizaKibus(0, +1);
			        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 1);
			        		movimientos.push(new Movimiento(2));
			        	}
			            break;
			        case KeyEvent.VK_DOWN:
			            // right arrow
			        	if(!arregloMapa.colisionaKibus(+1, 0)){
			        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 0);
			        		arregloMapa.actualizaKibus(+1, 0);
			        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 1);
			        		movimientos.push(new Movimiento(3));
			        	}
			            break;
			        case KeyEvent.VK_UP:
			            // left arrow
			        	if(!arregloMapa.colisionaKibus(-1, 0)){
			        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 0);
			        		arregloMapa.actualizaKibus(-1, 0);
			        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 1);
			        		movimientos.push(new Movimiento(1));
			        	}
			            break;
			    }

			    //Verifica si la posicion de Kibus es la misma de la casa
			    if(arregloMapa.getxCasa() == arregloMapa.getxKibus() &&
			       arregloMapa.getyCasa() == arregloMapa.getyKibus()){
			    	mapa.setKibusEnCasa(true, arregloMapa.getxCasa(), arregloMapa.getyCasa());
			    }
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent event) {
			
			
		}
	}
	
	//Funcion principal de Kibus
	public void ejecutaKibus(){
		while(true){
			cambioMapa();
			cambiaObjeto();
			
			
			if(menuAnimacion.isEmpiezaAnimacion()){
				menuAnimacion.setEmpiezaAnimacion(false);
				usaPila();
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
	
	public void cargaMapaHecho(){
		int fila = -1;
		
		fila = menuObjetos.tablaMapas.getSelectedRow();
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
	
	public void usaPila(){
		int velocidad = 0;
		velocidad = menuAnimacion.getVelocidad().getValue();
		int usoVel = 0;
		if(velocidad == 0){
			usoVel = 100;
		}
		else if(velocidad == 1){
			usoVel = 500;
		}
		else if(velocidad == 2){
			usoVel = 1000;
		}
		for(int i = movimientos.size(); i > 0; i--){
			int nvoMovimiento = movimientos.pop().getValorContrario();
			switch(nvoMovimiento){
				case 1:
	        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 0);
	        		arregloMapa.actualizaKibus(-1, 0);
	        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 1);
	        	break;
				case 2:
	        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 0);
	        		arregloMapa.actualizaKibus(0, +1);
	        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 1);
	        	break;
				case 3:
	        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 0);
	        		arregloMapa.actualizaKibus(+1, 0);
	        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 1);
	        	break;
				case 4:
	        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 0);
	        		arregloMapa.actualizaKibus(0, -1);
	        		mapa.actualizaMapa(arregloMapa.getxKibus(), arregloMapa.getyKibus(), 1);
	        	break;
	        	default:
	        	break;
			}
			try {
				Thread.sleep(usoVel);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(arregloMapa.kibusEnCasa()){
				movimientos = new Stack<Movimiento>();
				JOptionPane.showMessageDialog(
				        null, "Kibus Llego a Casa", "Felicidades!", JOptionPane.INFORMATION_MESSAGE);
				mapa.setKibusEnCasa(true, arregloMapa.getxCasa(), arregloMapa.getyCasa());
				break;
			}
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
					break;
				case 2:
					arregloMapa.setxCasa(0);
					arregloMapa.setyCasa(0);
					setCasaYaPuesta(false);
					arregloMapa.setxKibus(0);
					arregloMapa.setyKibus(0);
					setKibusYaPuesto(false);
					arregloMapa.modificaCasilla(x, y, 0);
					mapa.setKibusEnCasa(false, -1, -1);
					mapa.actualizaMapa(x, y, 0);
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
				case 2:
					arregloMapa.setxCasa(0);
					arregloMapa.setyCasa(0);
					setCasaYaPuesta(false);
					arregloMapa.modificaCasilla(x, y, 3);
					mapa.actualizaMapa(x, y, 3);
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
				case 2:
					arregloMapa.setxCasa(0);
					arregloMapa.setyCasa(0);
					setCasaYaPuesta(false);
					arregloMapa.modificaCasilla(x, y, 4);
					mapa.actualizaMapa(x, y, 4);
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
				case 2:
					arregloMapa.setxKibus(x);
					arregloMapa.setyKibus(y);
					setKibusYaPuesto(true);
					arregloMapa.modificaCasilla(x, y, 1);
					mapa.actualizaMapa(x, y, 1);
					mapa.setKibusEnCasa(true, arregloMapa.getxCasa(), arregloMapa.getyCasa());
					break;
				case 0:
				case 3:
				case 4:
					arregloMapa.setxKibus(x);
					arregloMapa.setyKibus(y);
					setKibusYaPuesto(true);
					arregloMapa.modificaCasilla(x, y, 1);
					mapa.actualizaMapa(x, y, 1);
					break;
				default:
				break;
				}
			//Agrega Casa
			}else if(menuObjetos.isCasaActivo() && !this.isCasaYaPuesta()){
				switch(tipoObjeto){
				case 1:
					arregloMapa.setxCasa(x);
					arregloMapa.setyCasa(y);
					arregloMapa.setxKibus(0);
					arregloMapa.setyKibus(0);
					setCasaYaPuesta(true);
					setKibusYaPuesto(false);
					arregloMapa.modificaCasilla(x, y, 2);
					mapa.actualizaMapa(x, y, 2);
					break;
				case 0:
				case 3:
				case 4:
					arregloMapa.setxCasa(x);
					arregloMapa.setyCasa(y);
					arregloMapa.setxKibus(x);
					arregloMapa.setyKibus(y);
					setCasaYaPuesta(true);
					setKibusYaPuesto(true);
					arregloMapa.modificaCasilla(x, y, 2);
					mapa.actualizaMapa(x, y, 2);
					mapa.setKibusEnCasa(true, x, y);
					break;
				default:
				break;
				}
			}
		}
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
					/**Por lo pronto no es necesario
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
					**/
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
