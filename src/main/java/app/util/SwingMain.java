package app.util;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.FlowLayout;
import app.dto.AgenciaDTO;
import app.dto.ReporteroDTO;
import app.dto.EmpresaComunicacionDTO;
import app.model.LoginModel;
import app.model.AsignacionModel;
import app.model.ReportajeModel;
import app.view.ReportajeView;
import app.controller.ReportajeController;
import app.model.InformeModel;  // HU #33548 - Descomentar cuando implemente - adri
import app.model.DistribuirReportajeModel;
import app.model.OfrecerReportajesModel;
import app.view.AsignacionView;
import app.view.DistribuirReportajeView;
import app.view.AsignacionEdicionView;
import app.view.InformeView;
import app.view.OfrecerReportajesView;
import app.controller.AsignacionController;
import app.controller.AsignacionEdicionController;
import app.controller.DistribuirReportajeController;
import app.controller.InformeController;
import app.controller.OfrecerReportajesController;

public class SwingMain {

	private JFrame frame;
	private LoginModel loginModel;

	// Combos para seleccionar entidad
	private JComboBox<AgenciaDTO> cbAgencias;
	private JComboBox<ReporteroDTO> cbReporteros;
	private JComboBox<EmpresaComunicacionDTO> cbEmpresas;

	public static void main(String[] args) {
		// Inicializar la base de datos automáticamente al arrancar
		try {
			Database db = new Database();
			db.createDatabase(false);
			db.loadDatabase();
		} catch (Exception e) {
			System.err.println("Error al inicializar la base de datos: " + e.getMessage());
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Ahora el punto de entrada es directamente SwingMain (sin LoginView)
					SwingMain main = new SwingMain();
					main.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SwingMain() {
		this.loginModel = new LoginModel();
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Gestión de Reportajes");
		frame.setBounds(100, 100, 550, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		((JPanel)frame.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

		// --- AGENCIA DE PRENSA (ADRIAN / IVAN) ---
		addLabel("AGENCIA DE PRENSA (Adrian / Ivan)");

		// Combo para seleccionar agencia
		JPanel panelAgencia = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelAgencia.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel lblAgencia = new JLabel("Seleccionar agencia: ");
		cbAgencias = new JComboBox<>();
		cargarAgencias();
		panelAgencia.add(lblAgencia);
		panelAgencia.add(cbAgencias);
		frame.getContentPane().add(panelAgencia);

		addButtonAgencia("HU #33537: Asignación de reporteros (ADRIAN)");
		addButtonAgencia("HU #33543: Modificar asignación (ADRIAN)");
		addButtonAgencia("HU #33548: Informe de un evento (ADRIAN)");
		addButtonAgencia("HU #33539: Ofrecer reportajes (IVAN)");
		addButtonAgencia("HU #33544: Modificar ofrecimiento (IVAN)");
		addButtonAgencia("HU #33541: Distribuir reportaje (IVAN)");

		// --- REPORTERO (DIEGO) ---
		addLabel("REPORTERO (Diego)");

		// Combo para seleccionar reportero
		JPanel panelReportero = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelReportero.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel lblReportero = new JLabel("Seleccionar reportero: ");
		cbReporteros = new JComboBox<>();
		cargarReporteros();
		panelReportero.add(lblReportero);
		panelReportero.add(cbReporteros);
		frame.getContentPane().add(panelReportero);

		addButtonReportero("HU #33538: Entrega de reportaje (DIEGO)");
		addButtonReportero("HU #33545: Modificar entrega (DIEGO)");
		addButtonReportero("HU #33547: Restaurar versión previa (DIEGO)");

		// --- EMPRESA DE COMUNICACIÓN (IRENE) ---
		addLabel("EMPRESA DE COMUNICACIÓN (Irene)");

		// Combo para seleccionar empresa
		JPanel panelEmpresa = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelEmpresa.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel lblEmpresa = new JLabel("Seleccionar empresa: ");
		cbEmpresas = new JComboBox<>();
		cargarEmpresas();
		panelEmpresa.add(lblEmpresa);
		panelEmpresa.add(cbEmpresas);
		frame.getContentPane().add(panelEmpresa);

		addButtonEmpresa("HU #33540: Gestionar ofrecimientos (IRENE)");
		addButtonEmpresa("HU #33542: Acceder al reportaje (IRENE)");
		addButtonEmpresa("HU #33546: Modificar decisión (IRENE)");

		// --- MANTENIMIENTO BD ---
		addLabel("MANTENIMIENTO BD");
		JButton btnInit = new JButton("Inicializar Base de Datos en Blanco");
		btnInit.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnInit.addActionListener(e -> {
			new Database().createDatabase(false);
			// Recargar combos después de inicializar
			cargarAgencias();
			cargarReporteros();
			cargarEmpresas();
		});
		frame.getContentPane().add(btnInit);
		frame.getContentPane().add(javax.swing.Box.createRigidArea(new Dimension(0, 5)));

		JButton btnLoad = new JButton("Cargar Datos Iniciales para Pruebas");
		btnLoad.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnLoad.addActionListener(e -> {
			Database db = new Database();
			db.createDatabase(false);
			db.loadDatabase();
			// Recargar combos después de cargar datos
			cargarAgencias();
			cargarReporteros();
			cargarEmpresas();
		});
		frame.getContentPane().add(btnLoad);
	}

	private void cargarAgencias() {
		DefaultComboBoxModel<AgenciaDTO> model = new DefaultComboBoxModel<>();
		for (AgenciaDTO a : loginModel.getAgencias()) {
			model.addElement(a);
		}
		cbAgencias.setModel(model);
		cbAgencias.setSelectedIndex(-1);
	}

	private void cargarReporteros() {
		DefaultComboBoxModel<ReporteroDTO> model = new DefaultComboBoxModel<>();
		for (ReporteroDTO r : loginModel.getReporteros()) {
			model.addElement(r);
		}
		cbReporteros.setModel(model);
		cbReporteros.setSelectedIndex(-1);
	}

	private void cargarEmpresas() {
		DefaultComboBoxModel<EmpresaComunicacionDTO> model = new DefaultComboBoxModel<>();
		for (EmpresaComunicacionDTO e : loginModel.getEmpresas()) {
			model.addElement(e);
		}
		cbEmpresas.setModel(model);
		cbEmpresas.setSelectedIndex(-1);
	}

	private void addLabel(String text) {
		JLabel label = new JLabel(text);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setFont(label.getFont().deriveFont(java.awt.Font.BOLD));
		label.setBorder(new EmptyBorder(15, 0, 5, 0));
		frame.getContentPane().add(label);
	}

	private void addButtonAgencia(String text) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMaximumSize(new Dimension(450, 30));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAgenciaButtonAction(text);
			}
		});
		frame.getContentPane().add(button);
		frame.getContentPane().add(javax.swing.Box.createRigidArea(new Dimension(0, 2)));
	}

	private void addButtonReportero(String text) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMaximumSize(new Dimension(450, 30));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleReporteroButtonAction(text);
			}
		});
		frame.getContentPane().add(button);
		frame.getContentPane().add(javax.swing.Box.createRigidArea(new Dimension(0, 2)));
	}

	private void addButtonEmpresa(String text) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMaximumSize(new Dimension(450, 30));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleEmpresaButtonAction(text);
			}
		});
		frame.getContentPane().add(button);
		frame.getContentPane().add(javax.swing.Box.createRigidArea(new Dimension(0, 2)));
	}

	/**
	 * Maneja las acciones de los botones de Agencia
	 */
	private void handleAgenciaButtonAction(String buttonText) {
		AgenciaDTO agenciaSeleccionada = (AgenciaDTO) cbAgencias.getSelectedItem();
		if (agenciaSeleccionada == null) {
			javax.swing.JOptionPane.showMessageDialog(frame, 
					"Debe seleccionar una agencia para continuar.", 
					"Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		// HU #33537: Asignación de reporteros (ADRIAN)
		if (buttonText.contains("#33537")) {
			AsignacionModel model = new AsignacionModel();
			AsignacionView view = new AsignacionView(false);
			new AsignacionController(model, view, agenciaSeleccionada);
		}
		// HU #33543: Modificar asignación (ADRIAN)
		else if (buttonText.contains("#33543")) {
			AsignacionModel model = new AsignacionModel();
			AsignacionEdicionView view = new AsignacionEdicionView();
			new AsignacionEdicionController(model, view, agenciaSeleccionada);
		}
		// HU #33548: Informe de un evento (ADRIAN)
		else if (buttonText.contains("#33548")) {
			InformeModel model = new InformeModel();
			InformeView view = new InformeView();
			new InformeController(model, view, agenciaSeleccionada);
		}




		// HU #33539: Ofrecer reportajes (IVAN)
		else if (buttonText.contains("#33539")) {
			OfrecerReportajesModel model = new OfrecerReportajesModel();
			OfrecerReportajesView view = new OfrecerReportajesView();
			new OfrecerReportajesController(model, view, agenciaSeleccionada);
		}
		// HU #33541: Ofrecer reportajes (IVAN)
		else if (buttonText.contains("#33541")) {
			DistribuirReportajeModel model = new DistribuirReportajeModel();
			DistribuirReportajeView view = new DistribuirReportajeView();
			new DistribuirReportajeController(model, view, agenciaSeleccionada);
		}
		// Resto de botones no implementados
		else {
			javax.swing.JOptionPane.showMessageDialog(frame, "Acción no implementada: " + buttonText);
		}
	}

	/**
	 * Maneja las acciones de los botones de Reportero
	 */
	private void handleReporteroButtonAction(String buttonText) {
		ReporteroDTO reporteroSeleccionado = (ReporteroDTO) cbReporteros.getSelectedItem();
		if (reporteroSeleccionado == null) {
			javax.swing.JOptionPane.showMessageDialog(frame, 
					"Debe seleccionar un reportero para continuar.", 
					"Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		// HU #33538: Entrega de reportaje
		if (buttonText.contains("#33538")) {
			ReportajeModel model = new ReportajeModel();
			ReportajeView view = new ReportajeView();

			int idReportero = reporteroSeleccionado.getId();

			new ReportajeController(model, view, idReportero);
		}
		else {
			// HU #33545, #33547 - Por implementar
			javax.swing.JOptionPane.showMessageDialog(frame, 
					"Acción no implementada: " + buttonText + "\nReportero seleccionado: " + reporteroSeleccionado.getNombre());
		}
	}

	/**
	 * Maneja las acciones de los botones de Empresa
	 */
	private void handleEmpresaButtonAction(String buttonText) {
		EmpresaComunicacionDTO empresaSeleccionada = (EmpresaComunicacionDTO) cbEmpresas.getSelectedItem();
		if (empresaSeleccionada == null) {
			javax.swing.JOptionPane.showMessageDialog(frame, 
					"Debe seleccionar una empresa para continuar.", 
					"Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		// HU #33540: Gestionar ofrecimientos (IRENE)
		if (buttonText.contains("#33540")) {
			app.model.OfrecimientosRecibidosModel modelOfr = new app.model.OfrecimientosRecibidosModel();
			app.view.OfrecimientosRecibidosView viewOfr = new app.view.OfrecimientosRecibidosView();
			app.controller.OfrecimientosRecibidosController controllerOfr =
					new app.controller.OfrecimientosRecibidosController(modelOfr, viewOfr, empresaSeleccionada.getId());
			controllerOfr.initController();
		}
		// HU #33542: Acceder al reportaje (IRENE)
		else if (buttonText.contains("#33542")) {
			app.model.AccesoReportajeModel modelRep = new app.model.AccesoReportajeModel();
			app.view.AccesoReportajeView viewRep = new app.view.AccesoReportajeView();
			app.controller.AccesoReportajeController controllerRep = 
					new app.controller.AccesoReportajeController(viewRep, modelRep, empresaSeleccionada.getId());    
			controllerRep.mostrarVista();
		}
		// HU #33546 y otros no implementados
		else {
			javax.swing.JOptionPane.showMessageDialog(frame, 
					"Acción no implementada: " + buttonText + "\nEmpresa seleccionada: " + empresaSeleccionada.getNombre());
		}
	}

	public JFrame getFrame() { return this.frame; }
}