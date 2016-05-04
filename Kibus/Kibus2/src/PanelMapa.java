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
    public Image arbol, kibus, casa, roca;
    private boolean kibusEnCasa;
	private int xCasa, yCasa;
	private int posicionObjetoX, posicionObjetoY;
	private boolean modificaCasilla;
    
	public PanelMapa(Mapa nvoMapa){
		this.setLayout(new GridLayout(ALTOMAPA, ANCHOMAPA));
		try {
			backgr = ImageIO.read(getClass().getResource("fotos/pasto.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//this.setFocusable(false);
		
		setKibusEnCasa(true, nvoMapa.getxCasa() , nvoMapa.getyCasa());
		
		posicionObjetoX = posicionObjetoY = 0;
		modificaCasilla = false;
		
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
			arbol = ImageIO.read(getClass().getResource("./fotos/arbol.png"));
			kibus = ImageIO.read(getClass().getResource("fotos/kibus.png"));
			roca = ImageIO.read(getClass().getResource("fotos/roca.png"));
			casa = ImageIO.read(getClass().getResource("fotos/casa.png"));

			setKibusEnCasa(true, nvoMapa.getxCasa(), nvoMapa.getyCasa() );

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
		}catch(IOException ex) {
			System.out.println("Error al cargar las imagenes para los botones");
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
	
	private Image getScaledImage(final Image srcImg, final int w, final int h){
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

	
	public void incrementaBanderin(int x, int y){
		int valor = 0;
		if(verificaBanderin(x, y)){
			valor = Integer.parseInt(arregloMapa[x][y].getText());
			valor++;
			arregloMapa[x][y].setText(Integer.toString(valor));		
		} else{
			arregloMapa[x][y].setText("1");
		}
	}
	
	public void disminuyeTodos(){
		int valor = 0;
		for(int i = 0; i < 20; i++)
			for(int j = 0; j < 20; j++){
				if(verificaBanderin(i, j)){
					valor = Integer.parseInt(arregloMapa[i][j].getText());
					valor--;
					if(valor > 0)
						arregloMapa[i][j].setText(Integer.toString(valor));
					else
						arregloMapa[i][j].setText("");
				}
			}
	}
	
	//Solo necesitamos eliminar alrededor,
	public void disminuyeAlrededor(int x, int y){
		int valor = 0;
		if((x - 1) >= 0 && (y - 1) >= 0){ //Arriba-izquierda
			if(verificaBanderin(x - 1, y - 1)){//Tiene banderin
				valor = Integer.parseInt(arregloMapa[x - 1][y - 1].getText());
				valor--;
				if(valor > 0)
					arregloMapa[x - 1][y - 1].setText(Integer.toString(valor));
				else
					arregloMapa[x - 1][y - 1].setText("");
			}
		}
		if((y - 1) >= 0){//Arriba
			if(verificaBanderin(x, y - 1)){//Tiene banderin
				valor = Integer.parseInt(arregloMapa[x][y - 1].getText());
				valor--;
				if(valor > 0)
					arregloMapa[x][y - 1].setText(Integer.toString(valor));
				else
					arregloMapa[x][y - 1].setText("");
			}
		}
		if((x + 1) < 20 && (y - 1) >= 0){ //Arriba derecha
			if(verificaBanderin(x + 1, y - 1)){//Tiene banderin
				valor = Integer.parseInt(arregloMapa[x + 1][y - 1].getText());
				valor--;
				if(valor > 0)
					arregloMapa[x + 1][y - 1].setText(Integer.toString(valor));
				else
					arregloMapa[x + 1][y - 1].setText("");					
			}
		}
		if((x - 1) >= 0){//Centro izquierda
			if(verificaBanderin(x - 1, y)){//Tiene banderin
				valor = Integer.parseInt(arregloMapa[x - 1][y].getText());
				valor--;
				if(valor > 0)
					arregloMapa[x - 1][y].setText(Integer.toString(valor));
				else
					arregloMapa[x - 1][y].setText("");
			}
		}
		if((x + 1) < 20){//Centro derecha
			if(verificaBanderin(x + 1, y)){//Tiene banderin
				valor = Integer.parseInt(arregloMapa[x + 1][y].getText());
				valor--;
				if(valor > 0)
					arregloMapa[x + 1][y].setText(Integer.toString(valor));
				else
					arregloMapa[x + 1][y].setText("");
			}
		}
		if((x - 1) >= 0 && (y + 1) < 20){//Abajo izquierda
			if(verificaBanderin(x - 1, y + 1)){//Tiene banderin
				valor = Integer.parseInt(arregloMapa[x - 1][y + 1].getText());
				valor--;
				if(valor > 0)
					arregloMapa[x - 1][y + 1].setText(Integer.toString(valor));
				else
					arregloMapa[x - 1][y + 1].setText("");
			}
		}
		if((y + 1) < 10){//Abajo
			if(verificaBanderin(x, y + 1)){//Tiene banderin
				valor = Integer.parseInt(arregloMapa[x][y + 1].getText());
				valor--;
				if(valor > 0)
					arregloMapa[x][y + 1].setText(Integer.toString(valor));
				else
					arregloMapa[x][y + 1].setText("");
			}
		}
		if((x + 1) < 20 && (y + 1) < 20){//Centro izquierda
			if(verificaBanderin(x + 1, y + 1)){//Tiene banderin
				valor = Integer.parseInt(arregloMapa[x + 1][y + 1].getText());
				valor--;
				if(valor > 0)
					arregloMapa[x + 1][y + 1].setText(Integer.toString(valor));
				else
					arregloMapa[x + 1][y + 1].setText("");					
			}
		}
		

	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * 
	 * Si verificamos los caminos bloqueados, verificamos los banderines
	 * Los banderines en kibus son manejados por medio de un numero
	 * entre 0-5
	 * 
	 * 1-4 No bloqueado
	 * 5 Bloqueado
	 * 
	 * El banderin incrementara de valor por cada vez que Kibus vuelva a pasar sobre el
	 * Al llegar a 5 kibus NO debera pasar por ahi
	 * 
	 */
	public boolean verificaValorBanderin(int x, int y){
		boolean bloqueado = false;
		int valor = 0;
		
		if(verificaBanderin(x, y)){
			valor = Integer.parseInt(arregloMapa[x][y].getText());
			
			switch(valor){
			case 1:
			case 2:
			case 3:
			case 4:
				bloqueado = false;
				break;
			case 5:
				bloqueado = true;
				break;
			}
		}
		else {
			return false;
		}
		return bloqueado;
		
	}
	
	//Existe banderin
	public boolean verificaBanderin(int x, int y){
		if(arregloMapa[x][y].getText().isEmpty())
			return false;
		else
			return true;		
	}
	
	public void actualizaMapa(int getxKibus, int getyKibus, int i) {
		// TODO Auto-generated method stub
		switch(i){
			case 1:
				arregloMapa[getxKibus][getyKibus].setIcon(new ImageIcon(kibus));
			break;
			case 2: //Casa
				arregloMapa[getxKibus][getyKibus].setIcon(new ImageIcon(casa));
			break;
			case 3: //Roca
				arregloMapa[getxKibus][getyKibus].setIcon(new ImageIcon(roca));
			break;
			case 4: //Arbol
				arregloMapa[getxKibus][getyKibus].setIcon(new ImageIcon(arbol));
			break;
			default://Remueve la posicion actual
				arregloMapa[getxKibus][getyKibus].setIcon(null);	
			break;
		}
	}

	public boolean isKibusEnCasa() {
		return kibusEnCasa;
	}

	public void setKibusEnCasa(boolean kibusEnCasa, int xCasa, int yCasa) {
		this.kibusEnCasa = kibusEnCasa;
		this.xCasa = xCasa;
		this.yCasa = yCasa;
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

}
