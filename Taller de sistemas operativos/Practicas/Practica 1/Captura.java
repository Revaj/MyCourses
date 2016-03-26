import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 */

/**
 * @author Javier Agustín Rizo Orozco
 * Clase que permite capturar datos >:v
 *
 */
public class Captura extends JDialog{
	//listaLotes sera un vector que almacenara arreglos de 3 para guardar los procesos simulando
	//como si fuera un lote
	private static Frame ventana;
	public static Button guarda;
	public JLabel etiquetaNombreProg;
	public JTextField campoProgram;
	public JLabel etiquetaOperaciones;
	public JButton suma;
	public JButton resta;
	public JButton multiplicacion;
	public JButton division;
	public JButton residuo;
	public JButton raiz;
	public char valorOperacion = ' ';
	public JLabel etiqueta1Dato;
	public JTextField primerDato;
	public JLabel etiqueta2Dato;
	public JTextField segundoDato;
	public JLabel etiquetaTiempo;
	public JTextField campoTiempo;
	public JLabel etiquetaId;
	public JTextField campoId;
	public final static long serialVersionUID = 1;
	public int activado = 1;
	public  Vector<String> listaId;
	
	//Aunque no lo ocupemos  ve bien que tenga un constructor XF
	public Captura(int numero, Vector<String> repetido){
		setVentana(new Frame("Captura Proceso: "+numero ));
		guarda = new Button("Guarda ");
		guarda.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent evt){
				if(!tengoErrores())
					getVentana().setVisible(false);
			}
		});
		//Panel del formulario
		JPanel panelForm = new JPanel();
		//Panel de las operaciones
		JPanel panelOperacion = new JPanel();
		etiquetaNombreProg = new JLabel("Nombre del Programador");
		etiquetaNombreProg.setSize(20,20);
		campoProgram = new JTextField();
		etiquetaOperaciones = new JLabel("Operaciones Validas");
		suma = new JButton("+");
		suma.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				valorOperacion = '+';
			}
		});
		resta = new JButton("-");
		resta.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				valorOperacion = '-';
			}
		});
		multiplicacion = new JButton("*");
		multiplicacion.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				valorOperacion = '*';
			}
		});
		division = new JButton("/");
		division.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				valorOperacion = '/';
			}
		});
		residuo = new JButton("%");
		residuo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				valorOperacion = '%';
			}
		});
		raiz = new JButton("raiz");
		raiz.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				valorOperacion = 'r';
			}
		});
		etiqueta1Dato = new JLabel("Primer Dato");
		primerDato = new JTextField();
		primerDato.addKeyListener(new KeyAdapter()
		{
			   public void keyTyped(KeyEvent e)
			   {
			      char caracter = e.getKeyChar();

			      // Verificar si la tecla pulsada no es un digito
			      if(((caracter < '0') ||
			         (caracter > '9')) &&
			         (caracter != '-') ||
			         (caracter == '-' && primerDato.getText().length() >= 1) &&
			         (caracter != '\b' /*corresponde a BACK_SPACE*/))
			      {
			         e.consume();  // ignorar el evento de teclado
			      }
			   }
		});
		etiqueta2Dato = new JLabel("Segundo Dato");
		segundoDato = new JTextField();
		segundoDato.addKeyListener(new KeyAdapter()
		{
			   public void keyTyped(KeyEvent e)
			   {
			      char caracter = e.getKeyChar();

			      // Verificar si la tecla pulsada no es un digito
			      if(((caracter < '0') ||
			         (caracter > '9')) &&
			         (caracter != '-') ||
			         (caracter == '-' && segundoDato.getText().length() >= 1) &&
			         (caracter != '\b' /*corresponde a BACK_SPACE*/))
			      {
			         e.consume();  // ignorar el evento de teclado
			      }
			   }
		});
		etiquetaTiempo = new JLabel("Tiempo del Programa");
		campoTiempo = new JTextField();
		campoTiempo.addKeyListener(new KeyAdapter(){
			   public void keyTyped(KeyEvent e)
			   {
			      char caracter = e.getKeyChar();

			      // Verificar si la tecla pulsada no es un digito
			      if(((caracter < '0') ||
			         (caracter > '9')) &&
			         (caracter != '-') ||
			         (caracter == '-' && primerDato.getText().length() >= 1) &&
			         (caracter != '\b' /*corresponde a BACK_SPACE*/))
			      {
			         e.consume();  // ignorar el evento de teclado
			      }
			   }
		});
		etiquetaId = new JLabel("Número del Programa");
		campoId = new JTextField();
		panelForm.setLayout(new GridLayout (0,2,10,10));
		panelForm.add(etiquetaNombreProg,0,0);
		panelForm.add(campoProgram,1,1);
		panelForm.add(etiquetaOperaciones,4,1);
		panelOperacion.setLayout(new GridLayout(1,0,4,4));
		panelOperacion.add(suma);
		panelOperacion.add(resta);
		panelOperacion.add(multiplicacion);
		panelOperacion.add(division);
		panelOperacion.add(residuo);
		panelOperacion.add(raiz);
		panelForm.add(panelOperacion);
		panelForm.add(etiqueta1Dato,-1);
		panelForm.add(etiqueta2Dato,-1);
		panelForm.add(primerDato,-1);
		panelForm.add(segundoDato,-1);
		panelForm.add(etiquetaTiempo,-1);
		panelForm.add(etiquetaId,-1);
		panelForm.add(campoTiempo,-1);
		panelForm.add(campoId,-1);
		panelForm.add(new JLabel(),-1);
		panelForm.add(guarda,-1);
		
		getVentana().setVisible(true);
		getVentana().add(panelForm,BorderLayout.CENTER);
		getVentana().pack();
		//Cerramos la ventana :B
		getVentana().addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		listaId = repetido;
	}
	
	
   //Verifica que todo sea correcto en el proceso, sino mandaremos errores
   public boolean tengoErrores(){
	   boolean conError = false;
	   if(campoProgram.getText().length() == 0){
		   JOptionPane.showMessageDialog( null, "No ha escrito el nombre del programador",
				   "Error campos",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
		   
	   }
	   else if(valorOperacion == ' '){
		   JOptionPane.showMessageDialog( null, "No ha elegido una operacion matematica",
				   "Error seleccion",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
	   }
	   else if(primerDato.getText().equals("") || (segundoDato.getText().equals("") && valorOperacion != 'r' )){
		   JOptionPane.showMessageDialog( null, "No ha introducido algun dato para hacer las operaciones",
				   "Error campo",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
	   }
	   else if(valorOperacion == '/' &&  segundoDato.getText().equals("0")){
		   JOptionPane.showMessageDialog( null, "No se permite dividir entre cero",
				   "Error operacion",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
	   }
	   else if(valorOperacion == '%' &&  segundoDato.getText().equals("0")){
		   JOptionPane.showMessageDialog( null, "No se permite el modulo sobre cero",
				   "Error operacion",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
	   }
	   else if(valorOperacion == 'r' &&  segundoDato.getText().startsWith("-")){
		   JOptionPane.showMessageDialog( null, "No se permite sacar raiz a un negativo",
				   "Error operacion",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
	   }
	   else if(campoTiempo.getText().equals("")){
		   JOptionPane.showMessageDialog( null, "No ha puesto un tiempo maximo",
				   "Error campo",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
	   }
	   else if(Integer.parseInt(campoTiempo.getText()) <= 0){
		   JOptionPane.showMessageDialog( null, "El tiempo maximo debe ser mayor a cero",
				   "Error campo",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
	   }
	   else if(!campoId.getText().matches("[0-9]+")){
		   JOptionPane.showMessageDialog( null, "ID no valido o vacio",
				   "Error ID",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
	   }
	   else if(busquedaIdRepetido(listaId)){
		   JOptionPane.showMessageDialog( null, "Ya existe un id parecido, por favor escriba uno no repetido",
				   "Error ID",
				   JOptionPane.ERROR_MESSAGE );
		   conError = true;
	   }
	   
	   return conError;
   }
//Permite que no se repita la identificacion
public boolean busquedaIdRepetido( Vector<String> listaId){
	boolean repetido = false;
	//For mejorado, es la primera vez asi que :P
	for(String e: listaId){
		if(campoId.getText().equals(e)){
			repetido = true;
			break;
		}
	}
	return repetido;
	
}

public static Frame getVentana() {
	return ventana;
}

public static void setVentana(Frame ventana) {
	Captura.ventana = ventana;
}
}
