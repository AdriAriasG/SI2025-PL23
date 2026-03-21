package app.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class FreelanceDecisionView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableEventos;
	private JComboBox<String> cbDecision;
	private JButton btnAceptar;
	private JButton btnCancelar;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FreelanceDecisionView frame = new FreelanceDecisionView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FreelanceDecisionView() {
		setTitle("Elegir los reportajes a realizar siendo un Reportero Freelance");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 560, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Reportero Freelance");
		lblTitulo.setBounds(20, 15, 180, 14);
		contentPane.add(lblTitulo);

		JLabel lblEventos = new JLabel("Eventos disponibles");
		lblEventos.setBounds(20, 50, 140, 14);
		contentPane.add(lblEventos);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 75, 260, 180);
		contentPane.add(scrollPane);

		tableEventos = new JTable();
		tableEventos.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Nombre", "Fecha" }
		) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		scrollPane.setViewportView(tableEventos);

		cbDecision = new JComboBox<>();
		cbDecision.setModel(new javax.swing.DefaultComboBoxModel<>(
			new String[] { "INTERESADO", "NO_INTERESADO", "DUDOSO" }
		));
		cbDecision.setBounds(340, 130, 150, 30);
		contentPane.add(cbDecision);

		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(300, 285, 100, 25);
		contentPane.add(btnAceptar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(415, 285, 100, 25);
		contentPane.add(btnCancelar);
	}

	public JTable getTablaEventos() {
		return tableEventos;
	}

	public JComboBox<String> getCbDecision() {
		return cbDecision;
	}

	public JButton getBtnAceptar() {
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}
}
