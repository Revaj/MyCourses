
public class Movimiento {
	public final int ARRIBA  = 1;
	public final int DERECHA  = 2;
	public final int ABAJO  = 3;
	public final int IZQUIERDA  = 4;
	private int valorActual;
	private int valorContrario;
	
	public Movimiento(){
		setValorActual(0);
	}
	
	public Movimiento(int valorActual){
		this.setValorActual(valorActual);
	}

	public int getValorActual() {
		return valorActual;
	}

	public void setValorActual(int valorActual) {
		this.valorActual = valorActual;
		switch(valorActual){
			case 1://Arriba entonces abajo
			valorContrario = 3;
			break;
			case 2://Derecha entonces izquierda
			valorContrario = 4;
			break;
			case 3://Abajo entonces arriba
			valorContrario = 1;
			break;
			case 4://Izquierda entonces derecha
			valorContrario = 2;
			break;
			default:
			break;
		}
	}

	public int getValorContrario() {
		return valorContrario;
	}

	public void setValorContrario(int valorContrario) {
		this.valorContrario = valorContrario;
	}
	

}
