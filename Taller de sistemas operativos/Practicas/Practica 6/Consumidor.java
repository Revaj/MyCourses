/**Clase Consumidor que tiene los metodos necesarios para consumir elementos
 * 
 * @author Javier
 *
 *La clase heredara de Thread o hilo para correr en paralelo con el consumidor
 */
public class Consumidor extends Thread{
	public boolean estadoDormido;
	public int posicion;
	public int consumi;
	
	public Consumidor(){
		estadoDormido = true;
		posicion = 0;
		consumi = 0;
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep((int) (Math.random() * 5000 + 500));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			estadoDormido = true;
		}

	}


	//Recibe un producto
	public void consumeProducto(char producto){
		char basura;
		basura = producto;
	}
	
	
}


