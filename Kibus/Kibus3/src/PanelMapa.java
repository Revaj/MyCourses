import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class PanelMapa extends JPanel{

	private static final long serialVersionUID = 1L;
	public JLabel arregloMapa[][] = new JLabel[20][20];
	final public int ALTOMAPA = 20;
	final public int ANCHOMAPA = 20;
    public Image backgr;
    public Image arbol, kibus, casa, roca, abejas1, abejas2, abejas3, abejas4, abejas5;
    private boolean kibusEnCasa;
	private int xCasa, yCasa;
	private int xKibus, yKibus;
	private int posicionObjetoX, posicionObjetoY;
	private boolean modificaCasilla;
	public PosicionesOcupadas[] abejasOcupan = new PosicionesOcupadas[2];
    
	public PanelMapa(Mapa nvoMapa){
		this.setLayout(new GridLayout(ALTOMAPA, ANCHOMAPA));
		try {
			backgr = ImageIO.read(getClass().getResource("fotos/pasto.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//this.setFocusable(false);
				
		posicionObjetoX = posicionObjetoY = 0;
		modificaCasilla = false;
		xKibus = yKibus = 0;
		
		abejasOcupan[0] = new PosicionesOcupadas();
		abejasOcupan[1] = new PosicionesOcupadas();	
		//Genera nuevo tablero
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 20; j++){
				arregloMapa[i][j] = new JLabel(){
					private static final long serialVersionUID = 1L;

					public void paintComponent(Graphics g) {
				        g.drawImage(backgr, 0, 0, null);
				        super.paintComponent(g);
				      }
				 };
				 
				 arregloMapa[i][j].addMouseListener(new MouseAdapter(){

						@Override
						public void mouseClicked(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
							posicionObjetoX = (int) arg0.getComponent().getY() / 
									arg0.getComponent().getHeight();
							posicionObjetoY = (int) arg0.getComponent().getX() / 
									arg0.getComponent().getWidth();
							modificaCasilla = true;

						}

						@Override
						public void mouseEntered(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseExited(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mousePressed(MouseEvent arg0) {
							// TODO Auto-generated method stub

							
						}

						@Override
						
						public void mouseReleased(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}});
				 this.add(arregloMapa[i][j]);
			}
		}
		
		//Llena tablero de Objetos
		//Por default estara con el mapa1
		cargaMapa(nvoMapa);
		
		
	}
	
	public void cargaMapa(Mapa nvoMapa){
		try{
			arbol = ImageIO.read(getClass().getResource("fotos/arbol.png"));
			kibus = ImageIO.read(getClass().getResource("fotos/kibus.png"));
			roca = ImageIO.read(getClass().getResource("fotos/roca.png"));
			casa = ImageIO.read(getClass().getResource("fotos/casa.png"));
			
			abejas1 = ImageIO.read(getClass().getResource("fotos/Bee1.png"));
			abejas2 = ImageIO.read(getClass().getResource("fotos/Bee2.png"));
			abejas3 = ImageIO.read(getClass().getResource("fotos/Bee3.png"));
			abejas4 = ImageIO.read(getClass().getResource("fotos/Bee4.png"));
			abejas5 = ImageIO.read(getClass().getResource("fotos/Bee5.png"));
			
			abejas1 = getScaledImage(abejas1, 50, 50);
			abejas2 = getScaledImage(abejas2, 50, 50);
			abejas3 = getScaledImage(abejas3, 50, 50);
			abejas4 = getScaledImage(abejas4, 50, 50);
			abejas5 = getScaledImage(abejas5, 50, 50);



			arbol = getScaledImage(arbol, 50, 50);
			casa = getScaledImage(casa, 50, 50);
			kibus = getScaledImage(kibus, 50, 50);
			roca = getScaledImage(roca, 50, 50);
			for(int i = 0; i < 20; i++){
				for(int j = 0; j < 20; j++){
					//Por cada caso agregaremos el objeto correspondiente, caso contrario
					//no agregamos algun objeto
					switch(nvoMapa.getArreglo()[i][j]){
					case 1: //Kibus
						arregloMapa[i][j].setIcon(new ImageIcon(kibus));
						break;
					case 2: //Casa
						arregloMapa[i][j].setIcon(new ImageIcon(casa));
						break;
					case 3: //Roca
						arregloMapa[i][j].setIcon(new ImageIcon(roca));
						break;
					case 4: //Arbol
						arregloMapa[i][j].setIcon(new ImageIcon(arbol));
						break;
					default://Nada agregamos
							break;
					}
					
					arregloMapa[i][j].setText("");
				}
			}
			
			//Liberamos las casillas
			abejasOcupan[0] = new PosicionesOcupadas();
			abejasOcupan[1] = new PosicionesOcupadas();
		}catch(IOException ex) {
			System.out.println("Error al cargar las imagenes para los botones");
		}
	}
	
	
	public void liberaPosiciones(CapaAbejas nvo){
		
		//Liberamos las casillas
		abejasOcupan[0] = new PosicionesOcupadas();
		abejasOcupan[1] = new PosicionesOcupadas();
		eliminaAbejas(nvo);
		
	}
	
	/**
	 * 
	 * @param getxCasa
	 * @param getyCasa
	 * 
	 * Metodo para desplegar el calor por las coordenadas puestas
	 */
	public void despliegaCalor(int getxCasa, int getyCasa) {
		// TODO Auto-generated method stub
		int value = 100;
		int xI, xS, yI, yS;
		
		
		xI = xS = getxCasa;
		yI = yS = getyCasa;
		arregloMapa[getxCasa][getyCasa].setText("100");

		//Propaga el calor con  4 nodos alrededor que rellenaran el valor
		do{
			
			if(xI > 0){
				xI--;
				rellenaHorizontal(yI, yS, xI, value);
			}
			if(xS < 19){
				xS++;
				rellenaHorizontal(yI, yS, xS, value);
			}
			if(yS < 19){
				yS++;
				rellenaVertical(xI, xS, yS, value);
			}
			if(yI > 0){
				yI--;
				rellenaVertical(xI, xS, yI, value);
			}

			value -= 10;
		}while(xI > 0 || xS < 19 || yI > 0 || yS < 19);
	}

	private void rellenaVertical(int xI, int xS, int lineaActual, int value) {
		// TODO Auto-generated method stub
		for(; xI <= xS; xI++){
			arregloMapa[xI][lineaActual].setText(Integer.toString(value));
		}
		
	}

	private void rellenaHorizontal(int yI, int yS, int lineActual, int value) {
		// TODO Auto-generated method stub
		for(; yI <= yS; yI++){
			arregloMapa[lineActual][yI].setText(Integer.toString(value));
		}
	}

	/**
	 * Conseguido gracias a StackOverflow
	 * http://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
	 * 
	 * Reescala la imagen otorgada, esto servira para crear sets ya hechos a la hora del gridlayout
	 * @param srcImg Imagen fuente
	 * @param w El ancho
	 * @param h La altura
	 * @return la imagen en un nuevo tamanio, util para guardar un arreglo de imagenes tambien :P
	 */
	
	private Image getScaledImage(Image srcImg, final int w, final int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	}

	public JLabel[][] getArregloMapa() {
		return arregloMapa;
	}

	public void limpiaMapa(){
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 20; j++){
				arregloMapa[i][j].setIcon(null);
			}
		}	
	}
	
	public void actualizaMapa(int getxKibus, int getyKibus, int i) {
		// TODO Auto-generated method stub
		switch(i){
			case 1:
				arregloMapa[getxKibus][getyKibus].setIcon(new ImageIcon(kibus));
				xKibus = getxKibus;
				yKibus = getyKibus;
			break;
			case 2: //Casa
				arregloMapa[getxKibus][getyKibus].setIcon(new ImageIcon(casa));
				xCasa = getxKibus;
				yCasa = getyKibus;
			break;
			case 3: //Roca
				arregloMapa[getxKibus][getyKibus].setIcon(new ImageIcon(roca));
			break;
			case 4: //Arbol
				arregloMapa[getxKibus][getyKibus].setIcon(new ImageIcon(arbol));
			break;
			case 5: //Abeja
				//Si abeja interrumpe kibus o casa no dejamos que los borre al cabo
				//en la siguiente ronda se actualizan
				if((getxKibus == xKibus && getyKibus == yKibus)){
					break;
				} else if ((getxKibus == xCasa && getyKibus == yCasa)){
					break;
				}else {
					Image rutaNumAbejas = dameImagenAbejas(1);
					arregloMapa[getxKibus][getyKibus].setIcon(new ImageIcon(rutaNumAbejas));
				}
				break;
			default://Remueve la posicion actual
				arregloMapa[getxKibus][getyKibus].setIcon(null);	
			break;
		}
	}

	public boolean isKibusEnCasa() {
		return kibusEnCasa;
	}

	public int getxCasa() {
		return xCasa;
	}

	public void setxCasa(int xCasa) {
		this.xCasa = xCasa;
	}

	public int getyCasa() {
		return yCasa;
	}

	public void setyCasa(int yCasa) {
		this.yCasa = yCasa;
	}

	public int getPosicionObjetoY() {
		return posicionObjetoY;
	}

	public void setPosicionObjetoY(int posicionObjetoY) {
		this.posicionObjetoY = posicionObjetoY;
	}

	public int getPosicionObjetoX() {
		return posicionObjetoX;
	}

	public void setPosicionObjetoX(int posicionObjetoX) {
		this.posicionObjetoX = posicionObjetoX;
	}

	public boolean isModificaCasilla() {
		return modificaCasilla;
	}

	public void setModificaCasilla(boolean modificaCasilla) {
		this.modificaCasilla = modificaCasilla;
	}

	public void eliminaCalor() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 20; j++){
				arregloMapa[i][j].setText("");
			}
		}
		
	}

	public void eliminaAbejas(CapaAbejas abejas) {
		// TODO Auto-generated method stub
		if(abejas.cantidad > 0){
			for(int i = 0; i < 5; i++){
				arregloMapa[abejas.arreglo[i].getPosicionX()][abejas.arreglo[i].getPosicionY()].setIcon(null);
			}
		}
	}
	
	
	/*
	 * O esta libre y no tiene abejas o tiene las mismas posicion
	 * Si esta libre la casilla agregamos la cantidad de abejas y le damos
	 * las posiciones correspondientes
	 * Si esta ocupada pero tiene la misma posicion entonces solo
	 * agregamos 1 abeja de mas
	 */
	public void agregaMasAbejas(int x, int y){
		for(int i = 0; i < 2; i++){
			if(abejasOcupan[i].getNumeroAbejas() < 2 ||
			   (abejasOcupan[i].getPosicionX() == x &&
			   abejasOcupan[i].getPosicionY() == y &&
			   abejasOcupan[i].getNumeroAbejas() < 5)){
			   int cantAbejas = 0;
			   if(abejasOcupan[i].getNumeroAbejas() < 2){
				   cantAbejas = 2;
				   abejasOcupan[i].setNumeroAbejas(cantAbejas);
				   abejasOcupan[i].setPosicionX(x);
				   abejasOcupan[i].setPosicionY(y);
			   }else{
				   cantAbejas = abejasOcupan[i].getNumeroAbejas() + 1;
				   abejasOcupan[i].setNumeroAbejas(cantAbejas);
			   }
			   Image rutaNumAbejas = dameImagenAbejas(cantAbejas);
			   arregloMapa[x][y].setIcon(null);
			   arregloMapa[x][y].setIcon(new ImageIcon(rutaNumAbejas));		
			   break;
			}	
		}
	}
	
	public void eliminaAbejas(int x, int y){
		for(int i = 0; i < 2; i++){
			if(abejasOcupan[i].getNumeroAbejas() >= 2 &&
			   (abejasOcupan[i].getPosicionX() == x &&
			   abejasOcupan[i].getPosicionY() == y)){
			   int cantAbejas = abejasOcupan[i].getNumeroAbejas() - 1;
			   abejasOcupan[i].setNumeroAbejas(cantAbejas);			   
			   Image rutaNumAbejas = dameImagenAbejas(cantAbejas);
			   arregloMapa[x][y].setIcon(new ImageIcon(rutaNumAbejas));	
			   break;
			}	
		}		
	}

	private Image dameImagenAbejas(int cantAbejas) {
		// TODO Auto-generated method stub
		switch(cantAbejas){
		case 1:
			return abejas1;
		case 2:
			return abejas2;
		case 3:
			return abejas3;
		case 4:
			return abejas4;
		case 5:
			return abejas5;
		default:
			break;
		}
		
		return null;
	}

	public void enfriaZona(int xAbeja, int yAbeja) {
		// TODO Auto-generated method stub
		int valor = Integer.parseInt(arregloMapa[xAbeja][yAbeja].getText());
		arregloMapa[xAbeja][yAbeja].setText(Integer.toString(valor - 5));
	}

	public int dameCalor(int xAbeja, int yAbeja) {
		// TODO Auto-generated method stub
		int valor = Integer.parseInt(arregloMapa[xAbeja][yAbeja].getText());
		return valor;
	}
	
	public boolean esMayorQueMiCalor(int x, int y, int calorAbeja){
		int calor = dameCalor(x, y);
		if(calor > calorAbeja)
			return true;
		else
			return false;
		
	}
}
