package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


public class ModificacionOfrecimientosRecibidosView {
	
	private JFrame frame;
	private JTable table;
	private DefaultTableModel model;
	private JComboBox<String> cbFiltro;
	private JButton btnFiltrar;
	private JButton btnAceptar;
	private JButton btnRechazar;
	private JButton btnEliminar;
	private JScrollPane scrollTabla;
	private JLabel lblMensajeVacio;

	public ModificacionOfrecimientosRecibidosView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Gestión y Modificación de Decisiones");
		frame.setBounds(100, 100, 700, 450);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		frame.setContentPane(contentPane);

		// --- PANEL SUPERIOR: FILTRO ---
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelNorte.add(new JLabel("Mostrar ofrecimientos:"));

		cbFiltro = new JComboBox<>(new String[] { 
				"Pendientes de decidir", 
				"Ya decididos (Aceptados/Rechazados)" 
		});
		panelNorte.add(cbFiltro);

		btnFiltrar = new JButton("Filtrar");
		btnFiltrar.setBackground(new Color(230, 230, 230));
		panelNorte.add(btnFiltrar);

		contentPane.add(panelNorte, BorderLayout.NORTH);

		// --- PANEL CENTRAL: TABLA ---
		String[] columnas = { "Evento", "Fecha", "Estado Actual" };
		model = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Tabla no editable manualmente
			}
		};
		table = new JTable(model);
		table.setRowHeight(25);
		scrollTabla = new JScrollPane(table);
		contentPane.add(scrollTabla, BorderLayout.CENTER);

		// Mensaje para cuando no haya datos (invisible por defecto)
		lblMensajeVacio = new JLabel("No hay registros que coincidan con el filtro");
		lblMensajeVacio.setHorizontalAlignment(JLabel.CENTER);
		lblMensajeVacio.setForeground(Color.GRAY);
		lblMensajeVacio.setFont(new Font("Segoe UI", Font.ITALIC, 14));

		// --- PANEL INFERIOR: ACCIONES ---
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		btnAceptar = new JButton("Aceptar");
		btnRechazar = new JButton("Rechazar");
		btnEliminar = new JButton("Eliminar Decisión");
    
		// Color sugerente para el botón de eliminar
		btnEliminar.setForeground(new Color(150, 0, 0));

		panelSur.add(btnAceptar);
		panelSur.add(btnRechazar);
		panelSur.add(btnEliminar);

		contentPane.add(panelSur, BorderLayout.SOUTH);
	}

	// Métodos para controlar la interfaz
	public void mostrarMensajeVacio(boolean mostrar) {
		if (mostrar) {
			scrollTabla.setViewportView(lblMensajeVacio);
		} else {
			scrollTabla.setViewportView(table);
		}
		scrollTabla.revalidate();
		scrollTabla.repaint();
	}

	// Getters para el controlador
	public JFrame getFrame() { return frame; }
	public JTable getTable() { return table; }
	public DefaultTableModel getModel() { return model; }
	public JComboBox<String> getCbFiltro() { return cbFiltro; }
	public JButton getBtnFiltrar() { return btnFiltrar; }
	public JButton getBtnAceptar() { return btnAceptar; }
	public JButton getBtnRechazar() { return btnRechazar; }
	public JButton getBtnEliminar() { return btnEliminar; }

}	
