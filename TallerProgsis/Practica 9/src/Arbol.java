/**
 * Arbol binario usado para no tener que leer del archivo nuestras instrucciones
 * @author Javi
 *
 */

public class Arbol {
	public Nodo raiz;
	
	public Arbol(){
		raiz = null;
	}
	
	public boolean arbolVacio(){
		return raiz== null;
	}
	
	//Recursion
	public void insertar(Tabop nuevaInst){
		if(arbolVacio())
			raiz = new Nodo(nuevaInst);
		else
			insertar(raiz,nuevaInst);
	}
	//Realiza una insercion recursiva
    public void insertar(Nodo principal,Tabop nuevaInst){ 
        if(principal.modos.firstElement().getNombreInstruccion().compareTo(nuevaInst.getNombreInstruccion())<0){ 
        	if(principal.hijoIzquierdo == null)
        		principal.hijoIzquierdo = new Nodo(nuevaInst);
        	else
        		insertar(principal.hijoIzquierdo,nuevaInst);
        } 
        else if(principal.modos.firstElement().getNombreInstruccion().compareTo(nuevaInst.getNombreInstruccion())>0){ 
            if(principal.hijoDerecho == null)
            	principal.hijoDerecho = new Nodo(nuevaInst);
            else
            	insertar(principal.hijoDerecho,nuevaInst); 
        }
        else 
            principal.modos.add(nuevaInst); // Mismas instruccion con diferente modo
    } 
    
    //Impresion recursiva
    public void enOrden(){
    		enOrden(raiz);
    }
    
    public void enOrden(Nodo a){
    	if(a.hijoIzquierdo != null)
            enOrden( a.hijoIzquierdo);
        System.out.println(a.modos.firstElement().getNombreInstruccion());
        if(a.hijoDerecho != null)
            enOrden( a.hijoDerecho);
    	
    }
    
    //Buscamos por medio de un while donde se encuentra y regresamos el nodo.
    public Nodo buscaDirectiva(String codop){
    	Nodo temporal = null;
    	temporal = raiz;
    	while(temporal != null && !temporal.modos.firstElement().getNombreInstruccion().equalsIgnoreCase(codop))
    		if(temporal.modos.firstElement().getNombreInstruccion().compareTo(codop)<0)
    			temporal = temporal.hijoIzquierdo;
    		else if(temporal.modos.firstElement().getNombreInstruccion().compareTo(codop)>0)
    			temporal = temporal.hijoDerecho;
    			
    	
    	return temporal;
    }


}
