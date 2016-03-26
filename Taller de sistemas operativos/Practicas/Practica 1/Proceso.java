

//La clase proceso nos permitira simular los proceso para este proyecto usando sus metodos comunes
public class Proceso {
	private String nombreProgramador;
	private char operacion;
	private int tiempo;
	private int dato1;
	private int dato2;
	private String numeroID;
	
	//Constructor del Proceso
	public Proceso(){
		nombreProgramador = "";
		operacion = ' ';
		tiempo = 0;
		numeroID = "";
	}
	
	//Metodos set, para practicas posteriores podria usar booleanos para asegurar que
	//se capturen corretamente validando los procesos pero las practicas siguientes no lo 
	//requeriran y aparte podria complicar al atar mi programa por ciertos requisitos
	public void setNombreProg(String nvoProgrammer){
		nombreProgramador = nvoProgrammer;
	}
	
	public void setOperacion(char nvoOperacion){
		operacion = nvoOperacion;
	}
	
	public void setDato1(int nvoDato1){
		dato1 = nvoDato1;
	}
	
	public void setDato2(int nvoDato2){
		dato2 = nvoDato2;
	}
	
	public void setTiempo(int nvoTiempo){
		tiempo = nvoTiempo;
	}
	
	public void setNumeroID(String nvoID){
		numeroID = nvoID;
	}
	
	//Metodos get
	public String getNombreProg(){
		return nombreProgramador;
	}
	
	public char getOperacion(){
		return operacion;
	}
	
	public int getTiempo(){
		return tiempo;
	}
	
	public int getDato1(){
		return dato1;
	}
	
	public int getDato2(){
		return dato2;
	}
	
	public String getID(){
		return numeroID;
	}

}
