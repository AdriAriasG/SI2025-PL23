package app.view;

import javax.swing.*;
import java.awt.*;

public class FinalizarReportajeView {

	private JFrame frame;

	private JTable tablaEventos;
	private JTable tablaRevisiones;

	private JTextField txtTitulo;
	private JTextField txtSubtitulo;
	private JTextArea txtCuerpo;

	private JButton btnGuardarCambios;
	private JButton btnFinalizar;

	private JTable tablaMultimedia;

	private JTextField txtRuta;
	private JRadioButton rbImagen;
	private JRadioButton rbVideo;

	private JButton btnAñadirMultimedia;
	private JButton btnCambiarEstado;
	private JButton btnEliminarMultimedia;

	public FinalizarReportajeView() {
		initialize();
	}

	private void initialize() {

		frame = new JFrame();
		frame.setTitle("Finalizar Reportaje");
		frame.setBounds(100, 100, 900, 750);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().setLayout(
				new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)
				);

		// =========================
		// REPORTAJES EN REVISIÓN
		// =========================

		JPanel panelEventos = new JPanel(new BorderLayout());
		panelEventos.setBorder(
				BorderFactory.createTitledBorder(
						"Reportajes en revisión (Reportero responsable)"
						)
				);

		tablaEventos = new JTable();
		panelEventos.add(new JScrollPane(tablaEventos), BorderLayout.CENTER);

		panelEventos.setPreferredSize(new Dimension(800, 90));
		panelEventos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		frame.add(panelEventos);

		// =========================
		// DATOS DEL REPORTAJE
		// =========================

		JPanel panelDatos = new JPanel();
		panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
		panelDatos.setBorder(
				BorderFactory.createTitledBorder("Datos del reportaje (Editable)")
				);

		txtTitulo = new JTextField();
		txtSubtitulo = new JTextField();
		txtCuerpo = new JTextArea(8, 20);

		panelDatos.add(crearCampo("Título:", txtTitulo));
		panelDatos.add(crearCampo("Subtítulo:", txtSubtitulo));
		JScrollPane scrollCuerpo = new JScrollPane(txtCuerpo);
		scrollCuerpo.setPreferredSize(new Dimension(800, 120));
		panelDatos.add(scrollCuerpo);
		
		btnGuardarCambios = new JButton("Guardar cambios");
		panelDatos.add(btnGuardarCambios);
		
		panelDatos.setPreferredSize(new Dimension(800, 220));
		panelDatos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

		frame.add(panelDatos);

		// =========================
		// CONTENIDO MULTIMEDIA
		// =========================

		JPanel panelMultimedia = new JPanel(new BorderLayout());
		panelMultimedia.setBorder(
				BorderFactory.createTitledBorder("Contenido multimedia (Editable)")
				);

		tablaMultimedia = new JTable();
		panelMultimedia.add(new JScrollPane(tablaMultimedia),
				BorderLayout.CENTER);

		JPanel panelAccionesMulti = new JPanel();

		txtRuta = new JTextField(15);

		rbImagen = new JRadioButton("Imagen", true);
		rbVideo = new JRadioButton("Video");

		ButtonGroup bg = new ButtonGroup();
		bg.add(rbImagen);
		bg.add(rbVideo);

		btnAñadirMultimedia = new JButton("Añadir");
		btnCambiarEstado = new JButton("Cambiar estado");
		btnEliminarMultimedia = new JButton("Eliminar");

		panelAccionesMulti.add(new JLabel("Ruta:"));
		panelAccionesMulti.add(txtRuta);
		panelAccionesMulti.add(rbImagen);
		panelAccionesMulti.add(rbVideo);
		panelAccionesMulti.add(btnAñadirMultimedia);
		panelAccionesMulti.add(btnCambiarEstado);
		panelAccionesMulti.add(btnEliminarMultimedia);

		panelMultimedia.add(panelAccionesMulti, BorderLayout.SOUTH);

		panelMultimedia.setPreferredSize(new Dimension(800, 200));

		frame.add(panelMultimedia);

		// =========================
		// REVISIONES
		// =========================

		JPanel panelRevisiones = new JPanel(new BorderLayout());
		panelRevisiones.setBorder(
				BorderFactory.createTitledBorder(
						"Revisiones de reporteros asignados"
						)
				);

		tablaRevisiones = new JTable();
		panelRevisiones.add(new JScrollPane(tablaRevisiones),
				BorderLayout.CENTER);

		panelRevisiones.setPreferredSize(new Dimension(800, 150));

		frame.add(panelRevisiones);

		// =========================
		// BOTÓN FINALIZAR
		// =========================

		btnFinalizar = new JButton("FINALIZAR REPORTAJE");
		btnFinalizar.setAlignmentX(Component.CENTER_ALIGNMENT);

		frame.add(btnFinalizar);
	}

	private JPanel crearCampo(String label, JTextField field) {

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel(label), BorderLayout.NORTH);
		panel.add(field, BorderLayout.CENTER);
		return panel;
	}

	// =========================
	// GETTERS (usados por el controlador)
	// =========================

	public JFrame getFrame() {
		return frame;
	}

	public JTable getTablaEventos() {
		return tablaEventos;
	}

	public JTable getTablaRevisiones() {
		return tablaRevisiones;
	}

	public JTextField getTxtTitulo() {
		return txtTitulo;
	}

	public JTextField getTxtSubtitulo() {
		return txtSubtitulo;
	}

	public JTextArea getTxtCuerpo() {
		return txtCuerpo;
	}

	public JButton getBtnGuardarCambios() {
		return btnGuardarCambios;
	}

	public JButton getBtnFinalizar() {
		return btnFinalizar;
	}

	public JTable getTablaMultimedia() { 
		return tablaMultimedia; 
	}

	public JTextField getTxtRuta() { 
		return txtRuta; 
	}

	public JRadioButton getRbImagen() { 
		return rbImagen; 
	}

	public JRadioButton getRbVideo() { 
		return rbVideo; 
	}

	public JButton getBtnAñadirMultimedia() { 
		return btnAñadirMultimedia; 
	}

	public JButton getBtnCambiarEstado() { 
		return btnCambiarEstado; 
	}

	public JButton getBtnEliminarMultimedia() { 
		return btnEliminarMultimedia; 
	}
}