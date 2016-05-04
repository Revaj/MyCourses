
public class Movimiento {
	/**
	 * 0 - Arriba-Izq
	 * 1 - Arriba-Centro
	 * 2 - Arriba-Derecha
	 * 3 - Centro-Izq
	 * 4 - Centro-Centro
	 * 5 - Centro-Derecha
	 * 6 - Abajo-Izquierda
	 * 7 - Abajo-Centro
	 * 8 - Abajo-Derecha
	 * 
	 * Para esta practica generaremos coordenadas x y y
	 * Esto por el algoritmo de bresenhamm
	 */
	
	private int valorActualX;
	private int valorActualY;
	
	public Movimiento(){
		this.valorActualX = 0;
		this.valorActualY = 0;
	}
	
	public Movimiento(int valorActualX, int valorActualY){
		this.valorActualX = valorActualX;
		this.valorActualY = valorActualY;
	}

	public int getValorActualX() {
		return valorActualX;
	}

	public void setValorActualX(int valorActual) {
		this.valorActualX = valorActual;
	}

	public int getValorActualY() {
		return valorActualY;
	}

	public void setValorActualY(int valorActualY) {
		this.valorActualY = valorActualY;
	}
}
