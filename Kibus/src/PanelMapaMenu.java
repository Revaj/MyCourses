import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class PanelMapaMenu extends JPanel{

	private static final long serialVersionUID = 1L;
	private JButton mapa1, mapa2, mapa3, mapa4, mapaRandom;
	private boolean mapa1Activo, mapa2Activo, mapa3Activo, mapa4Activo, mapaRandomActivo;
	private boolean huboCambio;
	
	public PanelMapaMenu(){
		//Prepara Botones Mapa
		mapa1 = new JButton("MAP1");
		mapa2 = new JButton("MAP2");
		mapa3 = new JButton("MAP3");
		mapa4 = new JButton("MAP4");
		mapaRandom = new JButton("RANDOM");
		
		add(mapa1);
		add(mapa2);
		add(mapa3);
		add(mapa4);
		add(mapaRandom);
		
		mapa1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setMapa1Activo(true);
				setMapa2Activo(false);
				setMapa3Activo(false);
				setMapa4Activo(false);
				setMapaRandomActivo(false);
				
				setHuboCambio(true);

			}
		});
		
		mapa2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setMapa1Activo(false);
				setMapa2Activo(true);
				setMapa3Activo(false);
				setMapa4Activo(false);
				setMapaRandomActivo(false);
				
				setHuboCambio(true);

			}
		});
		
		mapa3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setMapa1Activo(false);
				setMapa2Activo(false);
				setMapa3Activo(true);
				setMapa4Activo(false);
				setMapaRandomActivo(false);
				
				setHuboCambio(true);

			}
		});
		
		mapa4.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setMapa1Activo(false);
				setMapa2Activo(false);
				setMapa3Activo(false);
				setMapa4Activo(true);
				setMapaRandomActivo(false);
				
				setHuboCambio(true);

			}
		});
		
		mapaRandom.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setMapa1Activo(false);
				setMapa2Activo(false);
				setMapa3Activo(false);
				setMapa4Activo(false);
				setMapaRandomActivo(true);
				
				setHuboCambio(true);

			}
		});
		
		setHuboCambio(false);
		
		mapa1.setFocusable(false);
		mapa2.setFocusable(false);
		mapa3.setFocusable(false);
		mapa4.setFocusable(false);
		mapaRandom.setFocusable(false);
		
		this.setFocusable(false);

	}

	public JButton getMapa1() {
		return mapa1;
	}

	public void setMapa1(JButton mapa1) {
		this.mapa1 = mapa1;
	}

	public JButton getMapaRandom() {
		return mapaRandom;
	}

	public void setMapaRandom(JButton mapaRandom) {
		this.mapaRandom = mapaRandom;
	}

	public JButton getMapa3() {
		return mapa3;
	}

	public void setMapa3(JButton mapa3) {
		this.mapa3 = mapa3;
	}

	public JButton getMapa4() {
		return mapa4;
	}

	public void setMapa4(JButton mapa4) {
		this.mapa4 = mapa4;
	}

	public JButton getMapa2() {
		return mapa2;
	}

	public void setMapa2(JButton mapa2) {
		this.mapa2 = mapa2;
	}
	
	public boolean isMapa3Activo() {
		return mapa3Activo;
	}

	public void setMapa3Activo(boolean mapa3Activo) {
		this.mapa3Activo = mapa3Activo;
	}

	public boolean isMapaRandomActivo() {
		return mapaRandomActivo;
	}

	public void setMapaRandomActivo(boolean mapaRandomActivo) {
		this.mapaRandomActivo = mapaRandomActivo;
	}

	public boolean isMapa2Activo() {
		return mapa2Activo;
	}

	public void setMapa2Activo(boolean mapa2Activo) {
		this.mapa2Activo = mapa2Activo;
	}

	public boolean isMapa1Activo() {
		return mapa1Activo;
	}

	public void setMapa1Activo(boolean mapa1Activo) {
		this.mapa1Activo = mapa1Activo;
	}

	public boolean isMapa4Activo() {
		return mapa4Activo;
	}

	public void setMapa4Activo(boolean mapa4Activo) {
		this.mapa4Activo = mapa4Activo;
	}

	public boolean isHuboCambio() {
		return huboCambio;
	}

	public void setHuboCambio(boolean huboCambio) {
		this.huboCambio = huboCambio;
	}
}
