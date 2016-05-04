
public class PosicionesOcupadas {
	private int numeroAbejas;
	private int posicionX;
	private int posicionY;
	
	public PosicionesOcupadas(){
		numeroAbejas = 0;
		posicionX = -1;
		posicionY = -1;
	}

	public int getNumeroAbejas() {
		return numeroAbejas;
	}

	public void setNumeroAbejas(int numeroAbejas) {
		this.numeroAbejas = numeroAbejas;
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
}
