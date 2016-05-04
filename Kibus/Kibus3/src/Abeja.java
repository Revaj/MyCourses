import java.util.Vector;

/**
 * 
 * @author Javier Rizo
 * 
 * Abeja es una clase que representa una abeja en el mapa de kibus
 * Ahora Kibus tiene 3 capas en 1
 * 1 - Mapa (kibus, casa, laberinto)
 * 2 - Calor (los jlables estan ocupados ahora)
 * 3 - Abejas (si el arreglo esta ocupado y sus jlabels 
 * tambien solo queda tener otra capa que maneje 5 begorros!1
 *
 */
public class Abeja {
	private int posicionX;
	private int posicionY;
	private Vector<Movimiento> ruta;
	private int posicionCalor = 0;
	
	public Abeja(int x, int y){
		setPosicionX(x);
		setPosicionY(y);
		ruta = new Vector<Movimiento>();
	}

	public int getPosicionX() {
		return posicionX;
	}

	public void setPosicionX(int posicionX) {
		this.posicionX = posicionX;
	}

	public int getPosicionY() {
		return posicionY;
	}

	public void setPosicionY(int posicionY) {
		this.posicionY = posicionY;
	}

	public Vector<Movimiento> getRuta() {
		return ruta;
	}

	public void setRuta(Vector<Movimiento> ruta) {
		this.ruta = ruta;
	}
	
	public void agregaMovimiento(Movimiento mov){
		ruta.addElement(mov);
	}
	
	//Dejamos que el java thrasher se encarge de eliminar la lista vieja
	public void vaciaMovimientos(){
		ruta = new Vector<Movimiento>();
	}

	public int getPosicionCalor() {
		return posicionCalor;
	}

	public void setPosicionCalor(int posicionCalor) {
		this.posicionCalor = posicionCalor;
	}

	public void copiaRuta(Vector<Movimiento> ruta2) {
		// TODO Auto-generated method stub
		ruta = new Vector<Movimiento>();
		int x, y;
		for(int i = 0; i < ruta2.size(); i++){
			x = ruta2.elementAt(i).getValorActualX();
			y = ruta2.elementAt(i).getValorActualY();
			ruta.add(new Movimiento(x, y));
		}
		
	}


}
