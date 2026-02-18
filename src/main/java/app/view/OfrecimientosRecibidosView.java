package app.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class OfrecimientosRecibidosView {
	
	private JFrame frame;
	private JTable table;
	private DefaultTableModel model;
	private JButton btnAceptar;
	private JButton btnRechazar;
	
	
	public OfrecimientosRecibidosView() {
		initialize();
		frame.setVisible(true); //Para que se vea al crearla
	}


	private void initialize() {
		
		frame = new JFrame();
		frame.setTitle("Ofrecimientos recibidos");
		frame.setBounds(100,100,500,350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); //Centrar en pantalla
		
		//Configuramos layout inicial
		frame.getContentPane().setLayout(new BorderLayout(10,10));
		
		//Creamos tabla y modelo
		String[] columnas = {"Evento", "Fecha"};
		model = new DefaultTableModel(columnas,0);
		table = new JTable(model);
		
		JScrollPane sP = new JScrollPane(table);
		frame.getContentPane().add(sP, BorderLayout.CENTER);
		
		//Ahora los botones
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT)); //Botones alineados a la derecha
		
		btnAceptar = new JButton("Aceptar");
		btnRechazar = new JButton("Rechazar");
		
		frame.getContentPane().add(panelBotones, BorderLayout.SOUTH);
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
	
	public JButton getBtnRechazar() {
		return btnRechazar;
	}
	
	public DefaultTableModel getTableModel() {
		return model;
	}
	
	public JTable getTable() {
		return table;
	}
	
	
	

}
