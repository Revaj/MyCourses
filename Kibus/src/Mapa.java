/**
 * 
 * @author agustin rizo
 * 
 * Clase contenedora de Mapas
 *
 */
public class Mapa {
	private int arreglo[][];
	private int xKibus, yKibus;
	private int xCasa, yCasa;
	
	public Mapa(int arreglo[][], int xKibus, int yKibus, int xCasa, int yCasa){
		this.setArreglo(arreglo);
		this.setxKibus(xKibus);
		this.setyKibus(yKibus);
		this.setxCasa(xCasa);
		this.setyCasa(yCasa);
	}
	
	//Hacemos una copia con el mismo objeto
	public Mapa(Mapa nvo){
		this.setArreglo(nvo.getArreglo());
		this.setxKibus(nvo.getxKibus());
		this.setyKibus(nvo.getyKibus());
		this.setxCasa(nvo.getxCasa());
		this.setyCasa(nvo.getyCasa());
	}
	
	public boolean colisionaKibus(int x, int y){
		boolean colisiona = false;
		int ufoX, ufoY;
		
		ufoX = xKibus + x;
		ufoY = yKibus + y;
		
		if((ufoX >= 0 && ufoX < 20) && (ufoY >= 0 && ufoY < 20)){
			switch(arreglo[ufoX][ufoY]){
			case 3:
			case 4:
				colisiona = true;
				break;
				
			default:
				break;
			}
		}
		else{
			colisiona = true;
		}
		
		return colisiona;
	}

	public int[][] getArreglo() {
		return arreglo;
	}

	public void setArreglo(int arreglo[][]) {
		this.arreglo = arreglo;
	}

	public int getxKibus() {
		return xKibus;
	}

	public void setxKibus(int xKibus) {
		this.xKibus = xKibus;
	}

	public int getyKibus() {
		return yKibus;
	}

	public void setyKibus(int yKibus) {
		this.yKibus = yKibus;
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

	public void actualizaKibus(int i, int j) {
		// TODO Auto-generated method stub
		 modificaCasilla(xKibus, yKibus, 0);
		 modificaCasilla(xCasa, yCasa, 2);

		xKibus += i;
		yKibus += j;
		 modificaCasilla(xKibus, yKibus, 1);
	}
	
	public void modificaCasilla(int i, int j, int valor){
		this.arreglo[i][j] = valor;
	}
	
	public void imprime(){
		System.out.println(arreglo);
	}
	
	public boolean kibusEnCasa(){
		boolean llegoCasa = false;
		
		if(xKibus == xCasa && yKibus == yCasa){
			llegoCasa = true;
		}
		return llegoCasa;
	}

}
