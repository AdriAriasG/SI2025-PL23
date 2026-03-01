package app.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

public class ModificarOfrecimientoView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	private JComboBox<String> comboFiltro;
	private JButton btnOfrecer;
	private JButton btnQuitar;
	private JButton btnAceptar;
	private JButton btnCancelar;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModificarOfrecimientoView frame = new ModificarOfrecimientoView();
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
	public ModificarOfrecimientoView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Agencia de Prensa: Mi Agencia");
		lblNewLabel.setBounds(10, 11, 181, 14);
		contentPane.add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 36, 404, 2);
		contentPane.add(separator);
		
		JLabel lblNewLabel_1 = new JLabel("Eventos\r\n");
		lblNewLabel_1.setBounds(10, 82, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		comboFiltro = new JComboBox<>();
		comboFiltro.setModel(new DefaultComboBoxModel(new String[] {"Empresas de comunicación sin reportajes ofrecidos", "Empresas de comunicación con reportajes ofrecidos"}));
		comboFiltro.setBounds(107, 49, 216, 22);
		contentPane.add(comboFiltro);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 107, 102, 80);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nombre", "Fecha"
			}
		));
		scrollPane.setViewportView(table);
		
		JLabel lblNewLabel_2 = new JLabel("Empresas disponibles\r\n");
		lblNewLabel_2.setBounds(145, 82, 102, 14);
		contentPane.add(lblNewLabel_2);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(145, 107, 119, 80);
		contentPane.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Nombre"
			}
		));
		scrollPane_1.setViewportView(table_1);
		
		JLabel lblNewLabel_3 = new JLabel("Empresas con ofrecimiento\r\n");
		lblNewLabel_3.setBounds(295, 82, 129, 14);
		contentPane.add(lblNewLabel_3);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(295, 107, 129, 80);
		contentPane.add(scrollPane_2);
		
		table_2 = new JTable();
		table_2.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Nombre"
			}
		));
		scrollPane_2.setViewportView(table_2);
		
		btnOfrecer = new JButton("Ofrecer\r\n");
		btnOfrecer.setBounds(158, 198, 89, 14);
		contentPane.add(btnOfrecer);
		
		btnQuitar = new JButton("Quitar Ofrecimiento\r\n");
		btnQuitar.setBounds(295, 198, 129, 14);
		contentPane.add(btnQuitar);
		
		btnAceptar = new JButton("Aceptar\r\n");
		btnAceptar.setBounds(217, 227, 89, 23);
		contentPane.add(btnAceptar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(325, 227, 89, 23);
		contentPane.add(btnCancelar);
		

	}
	
	public JTable getTablaEventos() {
	    return table;
	}

	public JTable getTablaDisponibles() {
	    return table_1;
	}

	public JTable getTablaOfrecidas() {
	    return table_2;
	}

	public JComboBox<String> getComboFiltro() {
	    return comboFiltro;
	}

	public JButton getBtnOfrecer() {
	    return btnOfrecer;
	}

	public JButton getBtnQuitar() {
	    return btnQuitar;
	}

	public JButton getBtnAceptar() {
	    return btnAceptar;
	}

	public JButton getBtnCancelar() {
	    return btnCancelar;
	}
	
	
}
