package app.view;

import javax.swing.*;
import java.awt.*;

public class ModificarEntregaView extends JFrame {

	private JTable tablaEventos;

	private JRadioButton rbSinReportaje;
	private JRadioButton rbConReportaje;

	private JTextField txtTitulo;
	private JTextField txtSubtitulo;
	private JTextArea txtCuerpo;

	private JButton btnModificar;

	public ModificarEntregaView() {

		setTitle("Modificar Entrega de Reportaje");
		setSize(900, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());

		// ==========================
		// PANEL NORTE (Filtro)
		// ==========================
		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));

		rbSinReportaje = new JRadioButton("Sin reportaje");
		rbConReportaje = new JRadioButton("Con reportaje");

		ButtonGroup grupo = new ButtonGroup();
		grupo.add(rbSinReportaje);
		grupo.add(rbConReportaje);

		rbSinReportaje.setSelected(true); // Seleccionado por defecto

		panelFiltro.add(new JLabel("Filtro: "));
		panelFiltro.add(rbSinReportaje);
		panelFiltro.add(rbConReportaje);

		add(panelFiltro, BorderLayout.NORTH);

		// ==========================
		// CENTRO (Tabla)
		// ==========================
		tablaEventos = new JTable();
		JScrollPane scrollTabla = new JScrollPane(tablaEventos);
		add(scrollTabla, BorderLayout.CENTER);

		// ==========================
		// SUR (Formulario)
		// ==========================
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
		panelFormulario.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		// Título
		panelFormulario.add(new JLabel("Título (no editable):"));
		txtTitulo = new JTextField();
		txtTitulo.setEditable(false);
		panelFormulario.add(txtTitulo);

		panelFormulario.add(Box.createRigidArea(new Dimension(0,10)));

		// Subtítulo
		panelFormulario.add(new JLabel("Subtítulo:"));
		txtSubtitulo = new JTextField();
		panelFormulario.add(txtSubtitulo);

		panelFormulario.add(Box.createRigidArea(new Dimension(0,10)));

		// Cuerpo
		panelFormulario.add(new JLabel("Cuerpo:"));
		txtCuerpo = new JTextArea(6, 20);
		txtCuerpo.setLineWrap(true);
		txtCuerpo.setWrapStyleWord(true);
		JScrollPane scrollCuerpo = new JScrollPane(txtCuerpo);
		panelFormulario.add(scrollCuerpo);

		panelFormulario.add(Box.createRigidArea(new Dimension(0,10)));

		// Botón
		btnModificar = new JButton("Modificar");
		btnModificar.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelFormulario.add(btnModificar);

		add(panelFormulario, BorderLayout.SOUTH);
	}

	// ==========================
	// GETTERS
	// ==========================

	public JTable getTablaEventos() { return tablaEventos; }

	public JRadioButton getRbSinReportaje() { return rbSinReportaje; }
	public JRadioButton getRbConReportaje() { return rbConReportaje; }

	public JTextField getTxtTitulo() { return txtTitulo; }
	public JTextField getTxtSubtitulo() { return txtSubtitulo; }
	public JTextArea getTxtCuerpo() { return txtCuerpo; }

	public JButton getBtnModificar() { return btnModificar; }

	public JFrame getFrame() { return this; }

	public void limpiarFormulario() {
		txtTitulo.setText("");
		txtSubtitulo.setText("");
		txtCuerpo.setText("");
	}
}