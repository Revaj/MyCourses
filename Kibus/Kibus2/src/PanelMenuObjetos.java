import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;


public class PanelMenuObjetos extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JButton kibus, casa, arbol, roca, eliminar;
	private boolean kibusActivo, casaActivo, arbolActivo, rocaActivo, eliminarActivo;
	private JSlider deslizadorObjetos;
	public JPanel panelBotones;
	private float valorObstaculos;
	private JButton carga, guarda;
	private boolean cargando, guardando;
	public JPanel panelManejoMapa;
	public JTable tablaMapas;
	public JScrollPane scroll;
	public DefaultTableModel modelo;
	
	public PanelMenuObjetos(){
		
		kibus = new JButton();
		casa = new JButton();
		arbol = new JButton();
		roca = new JButton();
		eliminar = new JButton();
		String [] columna = {"Mapas"};
		String [][] fila = listaMapas();
		modelo = new DefaultTableModel(fila, columna);
		this.setLayout(new BorderLayout());

		
		setKibusActivo(setCasaActivo(setArbolActivo(setRocaActivo(setEliminarActivo(false)))));
		setEliminarActivo(true);
		cargaIconosBotones();
		
		panelBotones = new JPanel();
		panelManejoMapa = new JPanel();
		
		this.add(panelBotones, BorderLayout.NORTH);
		
		carga = new JButton("Carga");
		guarda = new JButton("Guarda");
		
		this.add(panelManejoMapa, BorderLayout.CENTER);
		
		panelManejoMapa.add(carga, BorderLayout.SOUTH);
		panelManejoMapa.add(guarda, BorderLayout.SOUTH);
		
		tablaMapas = new JTable(modelo);
		
		scroll = new JScrollPane(tablaMapas);
		scroll.setFocusable(false);
		panelManejoMapa.add(scroll, BorderLayout.NORTH);
		tablaMapas.setFocusable(false);
		carga.setFocusable(false);
		guarda.setFocusable(false);
		tablaMapas.setAutoResizeMode(0);
		scroll.setPreferredSize(new Dimension(100, 150));
		
		carga.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				cargando = true;
			}
			
		});
		
		guarda.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				guardando = true;
			}
			
		});
		
		panelBotones.add(kibus);
		panelBotones.add(casa);
		panelBotones.add(arbol);
		panelBotones.add(roca);
		panelBotones.add(eliminar);
		
		deslizadorObjetos = new JSlider(JSlider.HORIZONTAL, 20, 80, 20);
		
		valorObstaculos = 0.20f;
		
		deslizadorObjetos.setFocusable(false);
	    deslizadorObjetos.setMinorTickSpacing(1);
	    deslizadorObjetos.setMajorTickSpacing(20);
	    deslizadorObjetos.setPaintTicks(true);
	    deslizadorObjetos.setPaintLabels(true);
	    deslizadorObjetos.setLabelTable(deslizadorObjetos.createStandardLabels(20, 20));
	    
	    deslizadorObjetos.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				valorObstaculos = deslizadorObjetos.getValue() / 100.0f;
			}
	    	
	    });
	    
	    this.add(deslizadorObjetos, BorderLayout.SOUTH);
		
		this.setFocusable(false);
		
		kibus.setFocusable(false);
		casa.setFocusable(false);
		roca.setFocusable(false);
		arbol.setFocusable(false);
		eliminar.setFocusable(false);
		
		
		//Activamos las opciones por el boton correspondiente
		kibus.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setKibusActivo(true);
				setCasaActivo(setArbolActivo(setRocaActivo(setEliminarActivo(false))));
				
			}
		});
				
		casa.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setCasaActivo(true);
				setKibusActivo(setArbolActivo(setRocaActivo(setEliminarActivo(false))));
				
			}
		});
		
		arbol.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setArbolActivo(true);
				setKibusActivo(setCasaActivo(setRocaActivo(setEliminarActivo(false))));
				
			}
		});
		
		roca.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setRocaActivo(true);
				setKibusActivo(setCasaActivo(setArbolActivo(setEliminarActivo(false))));
				
			}
		});
		
		eliminar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setEliminarActivo(true);
				setKibusActivo(setCasaActivo(setRocaActivo(setArbolActivo(false))));
				
			}
		});
	}
	
	private String[][] listaMapas() {
		// TODO Auto-generated method stub
		try {
			BufferedReader lector =new BufferedReader(new FileReader("listaMapas.txt"));
			Vector<String> lineas = new Vector<String>();
			String lineaTemporal = null;
			try {
				while((lineaTemporal = lector.readLine()) != null){
					lineas.add(lineaTemporal);
				}
				String mapas[][] = new String[lineas.size()][1];
				int i = 0;
				while(lineas.size() > 0){
					System.out.println(lineas.firstElement());
					mapas[i][0] = lineas.firstElement();
					lineas.remove(0);
					i++;
				}
				
				lector.close();
				return mapas;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
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
	
	public void cargaIconosBotones(){
		try{
			Image iArbol = ImageIO.read(getClass().getResource("fotos/arbol.png"));
			Image iKibus = ImageIO.read(getClass().getResource("fotos/kibus.png"));
			Image iCasa = ImageIO.read(getClass().getResource("fotos/casa.png"));
			Image iRoca = ImageIO.read(getClass().getResource("fotos/roca.png"));
			Image iEliminar = ImageIO.read(getClass().getResource("fotos/eliminar.png"));



			iArbol = getScaledImage(iArbol, 50, 50);
			iCasa = getScaledImage(iCasa, 50, 50);
			iKibus = getScaledImage(iKibus, 50, 50);
			iRoca = getScaledImage(iRoca, 50, 50);
			iEliminar = getScaledImage(iEliminar, 50, 50);

			
			arbol.setIcon(new ImageIcon(iArbol));
			kibus.setIcon(new ImageIcon(iKibus));
			casa.setIcon(new ImageIcon(iCasa));
			roca.setIcon(new ImageIcon(iRoca));
			eliminar.setIcon(new ImageIcon(iEliminar));
			
		}catch(IOException ex) {
			System.out.println("Error al cargar las imagenes para los botones");
		}
	}

	public JButton getKibus() {
		return kibus;
	}

	public void setKibus(JButton kibus) {
		this.kibus = kibus;
	}

	public JButton getArbol() {
		return arbol;
	}

	public void setArbol(JButton arbol) {
		this.arbol = arbol;
	}

	public JButton getEliminar() {
		return eliminar;
	}

	public void setEliminar(JButton eliminar) {
		this.eliminar = eliminar;
	}

	public JButton getRoca() {
		return roca;
	}

	public void setRoca(JButton roca) {
		this.roca = roca;
	}

	public JButton getCasa() {
		return casa;
	}

	public void setCasa(JButton casa) {
		this.casa = casa;
	}

	public boolean isArbolActivo() {
		return arbolActivo;
	}

	public boolean setArbolActivo(boolean arbolActivo) {
		this.arbolActivo = arbolActivo;
		return arbolActivo;
	}

	public boolean isCasaActivo() {
		return casaActivo;
	}

	public boolean setCasaActivo(boolean casaActivo) {
		this.casaActivo = casaActivo;
		return casaActivo;
	}

	public boolean isEliminarActivo() {
		return eliminarActivo;
	}

	public boolean setEliminarActivo(boolean eliminarActivo) {
		this.eliminarActivo = eliminarActivo;
		return eliminarActivo;
	}

	public boolean isKibusActivo() {
		return kibusActivo;
	}

	public void setKibusActivo(boolean kibusActivo) {
		this.kibusActivo = kibusActivo;
	}

	public boolean isRocaActivo() {
		return rocaActivo;
	}

	public boolean setRocaActivo(boolean rocaActivo) {
		this.rocaActivo = rocaActivo;
		return rocaActivo;
	}

	public JSlider getDeslizadorObjetos() {
		return deslizadorObjetos;
	}

	public void setDeslizadorObjetos(JSlider deslizadorObjetos) {
		this.deslizadorObjetos = deslizadorObjetos;
	}

	public float getValorObstaculos() {
		return valorObstaculos;
	}

	public void setValorObstaculos(float valorObstaculos) {
		this.valorObstaculos = valorObstaculos;
	}

	public JButton getGuarda() {
		return guarda;
	}

	public void setGuarda(JButton guarda) {
		this.guarda = guarda;
	}

	public JButton getCarga() {
		return carga;
	}

	public void setCarga(JButton carga) {
		this.carga = carga;
	}

	public boolean isGuardando() {
		return guardando;
	}

	public void setGuardando(boolean guardando) {
		this.guardando = guardando;
	}

	public boolean isCargando() {
		return cargando;
	}

	public void setCargando(boolean cargando) {
		this.cargando = cargando;
	}
	
	public void actualizaModelo(String mapa){
		modelo.addRow(new String[] {mapa});
	}
}
