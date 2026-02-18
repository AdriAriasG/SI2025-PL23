package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;

public class OfrecimientosRecibidosView {
	
	private JFrame frame;
	private JTable table;
	private DefaultTableModel model;
	private JButton btnAceptar;
	private JButton btnRechazar;
	private JLabel lblMensajeVacio;
	private JScrollPane scrollPane;
	private JScrollPane sP;
	
	
	public OfrecimientosRecibidosView() {
		initialize();
		frame.setVisible(true); //Para que se vea al crearla
	}


	private void initialize() {
		
		frame = new JFrame();
		frame.setTitle("Ofrecimientos recibidos");
		frame.setBounds(100,100,500,350);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null); //Centrar en pantalla
		
		//Configuramos layout inicial
		JPanel contentPane = new JPanel(new BorderLayout(10,10));
		contentPane.setBorder(new EmptyBorder(20,20,20,20));
		frame.setContentPane(contentPane);
		
		//Creamos tabla y modelo
		String[] columnas = {"Evento", "Fecha"};
		model = new DefaultTableModel(columnas,0);
		table = new JTable(model);
		
		table.setRowHeight(25);
		table.setGridColor(Color.BLACK);
		
		sP = new JScrollPane(table);
		frame.getContentPane().add(sP, BorderLayout.CENTER);
		
		
		//Mensaje tabla vacía
		lblMensajeVacio = new javax.swing.JLabel("No hay eventos pendientes de gestionar");
		lblMensajeVacio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblMensajeVacio.setForeground(Color.GRAY);
		
		
		
		//Ahora los botones
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT)); //Botones alineados a la derecha
		
		btnAceptar = new JButton("Aceptar");
		btnRechazar = new JButton("Rechazar");
		
		panelBotones.add(btnAceptar);
		panelBotones.add(btnRechazar);
		
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
	
	public void mostrarMensajeVacio(boolean mostrar){
		if(mostrar) {
			sP.setViewportView(lblMensajeVacio);
		}
		else {
			sP.setViewportView(table);
		}
		
		sP.revalidate();
		sP.repaint();
	}
	
	
	

}
