
/**
 * Clase de las instrucciones del manual de motorola
 * @author Javi
 *
 */

public class Tabop {
	private String nombreInstruccion;
	private boolean llevaOperando;
	private String modoDireccion;
	private String codigoMaquina;
	private byte bytesCalculados;
	private byte bytesCalcular;
	private byte totalBytes;
	
	public Tabop(){
		nombreInstruccion = "null";
		llevaOperando = false;
		modoDireccion = "null";
		codigoMaquina = "null";
		bytesCalculados = 0;
		bytesCalcular = 0;
		totalBytes = 0;
	}
	
	public void setNombreInstruccion(String nuevaInstruccion){
		nombreInstruccion = nuevaInstruccion;
	}
	
	public String getNombreInstruccion(){
		return nombreInstruccion;
	}
	
	public void setLlevaOperando(boolean contieneOperando){
		llevaOperando = contieneOperando;
	}
	
	public boolean getLlevaOperando(){
		return llevaOperando;
	}
	
	public void setModoDireccion(String nuevoModo){
		modoDireccion = nuevoModo;
	}
	
	public String getModoDireccion(){
		return modoDireccion;
	}
	
	public void setCodigoMaquina(String nuevoCodMaq){
		codigoMaquina = nuevoCodMaq;
	}
	
	public String getCodigoMaquina(){
		return codigoMaquina;
	}
	
	public void setBytesCalculados(byte nuevosBytesCalculados){
		bytesCalculados = nuevosBytesCalculados;
	}
	
	public byte getBytesCalculados(){
		return bytesCalculados;
	}
	
	public void setBytesCalcular(byte nuevosBytesCalcular){
		bytesCalcular = nuevosBytesCalcular;
	}
	
	public byte getBytesCalcular(){
		return bytesCalcular;
	}
	
	public void setTotalBytes(byte nuevoTotalBytes){
		totalBytes = nuevoTotalBytes;
	}
	
	public byte getTotalBytes(){
		return totalBytes;
	}

}
