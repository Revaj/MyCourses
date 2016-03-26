/**
 * Nodo usado para llevar la directiva y sus iguales por medio de un vector
 */

import java.util.Vector;


public class Nodo{
	public Nodo hijoDerecho;
	public Nodo hijoIzquierdo;
	public  Vector <Tabop> modos;
	
	public Nodo(Tabop instruccion){
		hijoDerecho = null;
		hijoIzquierdo = null;
		modos = new Vector<Tabop>();
		modos.add(instruccion);
	}

	public Nodo getHijoDerecho(){
		return hijoDerecho;
	}
	
	public Nodo getHijoIzquierdo(){
		return hijoIzquierdo;
	}
	
	public void setHijoIzquierdo(Nodo nuevoIzquierdo){
		hijoIzquierdo = nuevoIzquierdo;
	}
	
	public void setHijoDerecho(Nodo nuevoDerecho){
		hijoDerecho = nuevoDerecho;
	}
	
}