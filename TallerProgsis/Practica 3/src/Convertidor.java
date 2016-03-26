

public class Convertidor {
	
	public Convertidor(){
		
	}
	
	//Decimales vuelven normal, demas bases se analizan si son negativos o no
	public int regresaConversion(String cadena){
		int num = 0;
		if(!cadena.startsWith("%") && !cadena.startsWith("@") && !cadena.startsWith("$") )
			num = Integer.parseInt(cadena);
		else{
			switch(cadena.charAt(0)){
			case '%':
				num = conversionBases(cadena.substring(1),"1", 2);
				break;
				
			case '$':
				num = conversionBases(cadena.substring(1),"F", 16);
				break;
				
			case '@':
				num = conversionBases(cadena.substring(1),"7", 8);
				break;
				
			default:
				num = Integer.parseInt(cadena);
				break;
			}
		}
		
		return num;
		
	}
	
	private static int conversionBases(String cadena, String base, int numBase){
		int num = 0;
		//Posicion libre del bit significativo
		int sinBitSigno = 0;
		//Buscamos donde eliminar los bits
		if(cadena.startsWith(base)){
			for(int i = 0; i<cadena.length(); i++){
				if(cadena.charAt(i) == base.charAt(0))
					sinBitSigno = i;
				else
					break;
			}
			cadena = cadena.substring(sinBitSigno);
			num = Integer.parseInt(cadena, numBase);
			//Hacemos complemento a dos
			num = ~ num + 1;
		}
		else
			//Numeros positivos
			num = Integer.parseInt(cadena, numBase);
		return num;
	}
}
