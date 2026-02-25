package app.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JSeparator;
import javax.swing.JTable;

public class DistribuirReportajeView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DistribuirReportajeView frame = new DistribuirReportajeView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DistribuirReportajeView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Agencia de Prensa: Mi Agencia");
		lblNewLabel.setBounds(21, 11, 170, 14);
		panel.add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 37, 404, 2);
		panel.add(separator);
		
		JLabel lblNewLabel_1 = new JLabel("Eventos");
		lblNewLabel_1.setBounds(20, 47, 60, 14);
		panel.add(lblNewLabel_1);
		
		table = new JTable();
		table.setBounds(21, 72, 106, 72);
		panel.add(table);
		
		JLabel lblNewLabel_2 = new JLabel("Empresas");
		lblNewLabel_2.setBounds(163, 47, 60, 14);
		panel.add(lblNewLabel_2);

	}
}
