import java.util.StringTokenizer;



public class Convertidor {
	
	public Convertidor(){
		
	}
	
	//Decimales vuelven normal, demas bases se analizan si son negativos o no
	public int regresaConversion(String cadena){
		int num = 0;
		if(cadena.startsWith("#"))
			cadena = cadena.substring(1);
		if(cadena.startsWith(","))
			return 0;
		StringTokenizer separaComa = new StringTokenizer(cadena,",");
		cadena = separaComa.nextToken();
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
			if(base.equals("F")){
				cadena = Integer.toBinaryString(Integer.parseInt(cadena,16));
				base = "1";
			}
			else if (base.equals("7")){
				cadena = Integer.toBinaryString(Integer.parseInt(cadena,8));
				base = "1";
				
			}
			for(int i = 0; i<cadena.length(); i++){
				if(cadena.charAt(i) == base.charAt(0))
					sinBitSigno = i;
				else
					break;
			}
			cadena = cadena.substring(sinBitSigno);
			num = ~ Integer.parseInt(cadena, numBase) + 1;
		}
		else
			//Numeros positivos
			num = Integer.parseInt(cadena, numBase);
		return num;
	}
}
