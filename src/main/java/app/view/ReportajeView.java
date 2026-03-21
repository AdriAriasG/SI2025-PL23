package app.view;

import javax.swing.*;
import java.awt.*;

public class ReportajeView {

	private JFrame frame;
	private JTable tablaEventos;

	private JTextField txtTitulo;
	private JTextField txtSubtitulo;
	private JTextArea txtCuerpo;

	private JButton btnEntregar;
	private JButton btnModificar;
	
	private JButton btnSolicitarRevision;
	private JLabel lblEstado;

	// Multimedia
	private JTextField txtRuta;
	private JRadioButton rbImagen;
	private JRadioButton rbVideo;
	private JButton btnAñadirMultimedia;
	private JTable tablaMultimedia;
	private JButton btnCambiarEstado;
	private JButton btnEliminar;

	public ReportajeView() {
		initialize();
	}

	private void initialize() {

		frame = new JFrame();
		frame.setTitle("Gestión de Reportaje");
		frame.setBounds(100, 100, 900, 750);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setLayout(new BorderLayout());

		// ===============================
		// TÍTULO
		// ===============================
		JLabel lblTituloVentana = new JLabel("GESTIÓN DE REPORTAJES");
		lblTituloVentana.setFont(new Font("Arial", Font.BOLD, 18));
		lblTituloVentana.setHorizontalAlignment(SwingConstants.CENTER);
		lblTituloVentana.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		frame.add(lblTituloVentana, BorderLayout.NORTH);

		// ===============================
		// PANEL PRINCIPAL SCROLLABLE
		// ===============================
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		// ===============================
		// TABLA EVENTOS
		// ===============================
		panelPrincipal.add(new JLabel("Eventos asignados"));

		tablaEventos = new JTable();
		JScrollPane scrollEventos = new JScrollPane(tablaEventos);
		scrollEventos.setPreferredSize(new Dimension(800, 120));
		panelPrincipal.add(scrollEventos);

		panelPrincipal.add(Box.createVerticalStrut(15));

		// ===============================
		// FORMULARIO REPORTAJE
		// ===============================
		JPanel panelReportaje = new JPanel();
		panelReportaje.setLayout(new BoxLayout(panelReportaje, BoxLayout.Y_AXIS));
		panelReportaje.setBorder(BorderFactory.createTitledBorder("Datos del reportaje"));

		txtTitulo = new JTextField();
		txtSubtitulo = new JTextField();
		txtCuerpo = new JTextArea(5, 20);

		lblEstado = new JLabel("Estado: -");
		lblEstado.setFont(new Font("Arial", Font.BOLD, 13));
		lblEstado.setAlignmentX(Component.LEFT_ALIGNMENT);

		panelReportaje.add(new JLabel("Título (no editable si existe reportaje)"));
		panelReportaje.add(txtTitulo);

		panelReportaje.add(new JLabel("Subtítulo"));
		panelReportaje.add(txtSubtitulo);

		panelReportaje.add(new JLabel("Cuerpo"));
		panelReportaje.add(new JScrollPane(txtCuerpo));
		
		panelReportaje.add(Box.createVerticalStrut(8));
		panelReportaje.add(lblEstado);

		JPanel panelBotones = new JPanel();
		btnEntregar = new JButton("Entregar");
		btnModificar = new JButton("Modificar");
		btnSolicitarRevision = new JButton("Solicitar revisión");

		btnModificar.setVisible(false);
		btnSolicitarRevision.setVisible(false);

		panelBotones.add(btnEntregar);
		panelBotones.add(btnModificar);
		panelBotones.add(btnSolicitarRevision);

		panelReportaje.add(Box.createVerticalStrut(10));
		panelReportaje.add(panelBotones);

		panelPrincipal.add(panelReportaje);
		panelPrincipal.add(Box.createVerticalStrut(20));

		// ===============================
		// PANEL MULTIMEDIA
		// ===============================
		JPanel panelMultimedia = new JPanel();
		panelMultimedia.setLayout(new BoxLayout(panelMultimedia, BoxLayout.Y_AXIS));
		panelMultimedia.setBorder(BorderFactory.createTitledBorder("Contenido multimedia"));

		txtRuta = new JTextField();

		rbImagen = new JRadioButton("Imagen");
		rbVideo = new JRadioButton("Video");

		ButtonGroup grupo = new ButtonGroup();
		grupo.add(rbImagen);
		grupo.add(rbVideo);

		JPanel panelRuta = new JPanel(new BorderLayout());
		panelRuta.add(new JLabel("Ruta: "), BorderLayout.WEST);
		panelRuta.add(txtRuta, BorderLayout.CENTER);

		JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelTipo.add(new JLabel("Tipo: "));
		panelTipo.add(rbImagen);
		panelTipo.add(rbVideo);

		btnAñadirMultimedia = new JButton("Añadir");

		panelMultimedia.add(panelRuta);
		panelMultimedia.add(panelTipo);
		panelMultimedia.add(btnAñadirMultimedia);

		panelMultimedia.add(Box.createVerticalStrut(10));

		tablaMultimedia = new JTable();
		JScrollPane scrollMultimedia = new JScrollPane(tablaMultimedia);
		scrollMultimedia.setPreferredSize(new Dimension(800, 150));
		panelMultimedia.add(scrollMultimedia);

		JPanel panelAcciones = new JPanel();
		btnCambiarEstado = new JButton("Cambiar estado");
		btnEliminar = new JButton("Eliminar contenido");

		panelAcciones.add(btnCambiarEstado);
		panelAcciones.add(btnEliminar);

		panelMultimedia.add(panelAcciones);

		panelPrincipal.add(panelMultimedia);

		JScrollPane scrollGeneral = new JScrollPane(panelPrincipal);
		frame.add(scrollGeneral, BorderLayout.CENTER);

		habilitarMultimedia(false);
	}

	// ======================
	// GETTERS
	// ======================

	public JFrame getFrame() { return frame; }
	public JTable getTablaEventos() { return tablaEventos; }
	public JTable getTablaMultimedia() { return tablaMultimedia; }

	public JTextField getTxtTitulo() { return txtTitulo; }
	public JTextField getTxtSubtitulo() { return txtSubtitulo; }
	public JTextArea getTxtCuerpo() { return txtCuerpo; }
	public JTextField getTxtRuta() { return txtRuta; }

	public JButton getBtnEntregar() { return btnEntregar; }
	public JButton getBtnModificar() { return btnModificar; }
	public JButton getBtnAñadirMultimedia() { return btnAñadirMultimedia; }
	public JButton getBtnCambiarEstado() { return btnCambiarEstado; }
	public JButton getBtnEliminar() { return btnEliminar; }
	
	public JButton getBtnSolicitarRevision() { return btnSolicitarRevision; }
	public JLabel getLblEstado() { return lblEstado; }

	public JRadioButton getRbImagen() { return rbImagen; }
	public JRadioButton getRbVideo() { return rbVideo; }

	public void limpiarFormulario() {
		txtTitulo.setText("");
		txtSubtitulo.setText("");
		txtCuerpo.setText("");
	}

	public void habilitarMultimedia(boolean habilitar) {
		txtRuta.setEnabled(habilitar);
		rbImagen.setEnabled(habilitar);
		rbVideo.setEnabled(habilitar);
		btnAñadirMultimedia.setEnabled(habilitar);
		tablaMultimedia.setEnabled(habilitar);
		btnCambiarEstado.setEnabled(habilitar);
		btnEliminar.setEnabled(habilitar);
	}
	
	public void actualizarEstado(String estado) {

	    lblEstado.setText("Estado: " + estado);

	    if ("EN_REVISION".equals(estado)) {
	        lblEstado.setForeground(Color.RED);
	    } else {
	        lblEstado.setForeground(new Color(0, 128, 0)); // verde
	    }
	}
}