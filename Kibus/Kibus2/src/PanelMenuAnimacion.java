import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;


public class PanelMenuAnimacion extends JPanel{

	private static final long serialVersionUID = 1L;
	private JButton botonRegreso;
	private JSlider velocidad;
	private JLabel etiquetaVelocidad;
	private boolean empiezaAnimacion;
	
	public PanelMenuAnimacion(){
		botonRegreso = new JButton("Inicia");
	    this.add(botonRegreso);
	    
	    //Velocidad seran 3, baja, media y alta. Dependiendo del valor sera la
	    //velocidad de la animacion
	    etiquetaVelocidad = new JLabel("Velocidad");
	    this.add(etiquetaVelocidad);
	    velocidad = new JSlider(JSlider.HORIZONTAL, 0, 2, 0);
	    this.add(velocidad);
	    
	    empiezaAnimacion = false;
	    
	    this.setFocusable(false);
	    botonRegreso.setFocusable(false);
	    velocidad.setFocusable(false);
	    velocidad.setMinorTickSpacing(1);
	    velocidad.setMajorTickSpacing(3);
	    velocidad.setPaintTicks(true);
	    velocidad.setPaintLabels(true);
	    velocidad.setLabelTable(velocidad.createStandardLabels(1));
	    
	    botonRegreso.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				empiezaAnimacion = true;
			}
	    	
	    });
	    botonRegreso.setFocusable(false);

	}

	public JButton getBotonRegreso() {
		return botonRegreso;
	}

	public void setBotonRegreso(JButton botonRegreso) {
		this.botonRegreso = botonRegreso;
	}

	public JSlider getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(JSlider velocidad) {
		this.velocidad = velocidad;
	}

	public boolean isEmpiezaAnimacion() {
		return empiezaAnimacion;
	}

	public void setEmpiezaAnimacion(boolean empiezaAnimacion) {
		this.empiezaAnimacion = empiezaAnimacion;
	}


}
