/**Clase Productor que tiene los metodos necesarios para producir elementos
 * 
 * @author Javier
 *
 *La clase heredara de Thread o hilo para correr en paralelo con el consumidor
 */

public class Productor extends Thread{
	
	public boolean estadoDormido;
	public int posicion;
	public int produci;
	
	public Productor(){
		estadoDormido = true;
		posicion = 0;
		produci = 0;
	}
	
	public void run(){

		while(true){
			try {
				Thread.sleep((int) (Math.random() * 5000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			estadoDormido = true;
		}

	}


	//Genera un producto y lo entrega 
	public char dameProducto(){
		return  (char) (Math.random() * 130 + 33);
	}
	

}
