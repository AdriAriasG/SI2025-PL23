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
import app.model.ModificarOfrecimientoModel;
import app.model.AsignacionModel;
import app.model.ReportajeModel;
import app.view.ReportajeView;
import app.view.RestaurarVersionView;
import app.controller.ReportajeController;
import app.controller.RestaurarVersionController;
import app.model.InformeModel;  
import app.model.DistribuirReportajeModel;
import app.model.FreelanceDecisionModel;
import app.model.OfrecerReportajesModel;
import app.view.AsignacionView;
import app.view.DistribuirReportajeView;
import app.view.FreelanceDecisionView;
import app.view.AsignacionEdicionView;
import app.view.InformeView;
import app.view.ModificarOfrecimientoView;
import app.view.OfrecerReportajesView;
import app.controller.AsignacionController;
import app.controller.AsignacionEdicionController;
import app.controller.DistribuirReportajeController;
import app.controller.FreelanceDecisionController;
import app.controller.InformeController;
import app.controller.ModificarOfrecimientoController;
import app.controller.OfrecerReportajesController;
import app.model.RevisionReportajeModel;
import app.view.RevisionReportajeView;
import app.controller.RevisionReportajeController;

public class SwingMain {

	private JFrame frame;
	private LoginModel loginModel;

	// Combos para seleccionar entidad
	private JComboBox<AgenciaDTO> cbAgencias;
	private JComboBox<ReporteroDTO> cbReporteros;
	private JComboBox<EmpresaComunicacionDTO> cbEmpresas;
	private JComboBox<ReporteroDTO> cbReporterosFreelance;

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
		addButtonAgencia("Modificar ofrecimiento (IVAN)");
		addButtonAgencia("Distribuir reportaje (IVAN)");

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

		addButtonReportero("HU #34061: Gestión de reportaje (DIEGO)");
		addButtonReportero("HU #33547: Restaurar versión previa (DIEGO)");
		addButtonReportero("HU #34066: Revisar reportajes (DIEGO)");

		// --- REPORTERO FREELANCE (IVAN) ---
		addLabel("REPORTERO FREELANCE (Ivan)");

		JPanel panelReporteroFreelance = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelReporteroFreelance.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel lblReporteroFreelance = new JLabel("Seleccionar reportero freelance: ");
		cbReporterosFreelance = new JComboBox<>();
		cargarReporterosFreelance();
		panelReporteroFreelance.add(lblReporteroFreelance);
		panelReporteroFreelance.add(cbReporterosFreelance);
		frame.getContentPane().add(panelReporteroFreelance);

		addButtonReporteroFreelance("Elegir reportajes a realizar (IVAN)");

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

		addButtonEmpresa("Acceso a reportajes");
		addButtonEmpresa("Gestionar Ofrecimientos");

		// --- MANTENIMIENTO BD ---
		addLabel("MANTENIMIENTO BD");
		JButton btnInit = new JButton("Inicializar Base de Datos en Blanco");
		btnInit.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnInit.addActionListener(e -> {
			new Database().createDatabase(false);
			// Recargar combos después de inicializar
			cargarAgencias();
			cargarReporteros();
			cargarReporterosFreelance();
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
			cargarReporterosFreelance();
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

	private void cargarReporterosFreelance() {
		DefaultComboBoxModel<ReporteroDTO> model = new DefaultComboBoxModel<>();
		for (ReporteroDTO r : loginModel.getReporterosFreelance()) {
			model.addElement(r);
		}
		cbReporterosFreelance.setModel(model);
		cbReporterosFreelance.setSelectedIndex(-1);
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

	private void addButtonReporteroFreelance(String text) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMaximumSize(new Dimension(450, 30));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleReporteroFreelanceButtonAction(text);
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

		// HU #33541: Ofrecer reportajes (IVAN)
		else if (buttonText.contains("Distribuir")) {
			DistribuirReportajeModel model = new DistribuirReportajeModel();
			DistribuirReportajeView view = new DistribuirReportajeView();
			new DistribuirReportajeController(model, view, agenciaSeleccionada);
		}
		// HU #33544: Modificar ofrecimientos reportajes (IVAN)
		else if (buttonText.contains("Modificar")) {
			ModificarOfrecimientoModel model = new ModificarOfrecimientoModel();
			ModificarOfrecimientoView view = new ModificarOfrecimientoView();
			new ModificarOfrecimientoController(model, view, agenciaSeleccionada);
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

		ReporteroDTO reporteroSeleccionado =
				(ReporteroDTO) cbReporteros.getSelectedItem();

		if (reporteroSeleccionado == null) {
			javax.swing.JOptionPane.showMessageDialog(frame,
					"Debe seleccionar un reportero para continuar.",
					"Aviso",
					javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Gestión unificada (entrega + modificación + multimedia)
		if (buttonText.contains("Gestión de reportaje")) {

			ReportajeModel model = new ReportajeModel();
			ReportajeView view = new ReportajeView();

			new ReportajeController(model, view,
					reporteroSeleccionado.getId());
		}
		
		// Revisar reportajes
		else if (buttonText.contains("Revisar reportajes")) {

		    RevisionReportajeModel model = new RevisionReportajeModel();
		    RevisionReportajeView view = new RevisionReportajeView();

		    new RevisionReportajeController(
		            model,
		            view,
		            reporteroSeleccionado.getId()
		    );
		}

		// Restaurar versión
		else if (buttonText.contains("#33547")) {

			ReportajeModel model = new ReportajeModel();
			RestaurarVersionView view = new RestaurarVersionView();

			new RestaurarVersionController(model, view,
					reporteroSeleccionado.getId());
		}

		else {
			javax.swing.JOptionPane.showMessageDialog(frame,
					"Acción no implementada: " + buttonText);
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

		// HU #33542: Acceder al reportaje (IRENE)
		else if (buttonText.contains("Acceso a reportajes")) {
			app.model.AccesoReportajeModel modelRep = new app.model.AccesoReportajeModel();
			app.view.AccesoReportajeView viewRep = new app.view.AccesoReportajeView();
			app.controller.AccesoReportajeController controllerRep = 
					new app.controller.AccesoReportajeController(viewRep, modelRep, empresaSeleccionada.getId());    
			controllerRep.mostrarVista();
		}
		// HU #33546 y otros no implementados
		else if (buttonText.contains("Gestionar Ofrecimientos")) {
			app.model.ModificacionOfrecimientosRecibidosModel modelMod = new app.model.ModificacionOfrecimientosRecibidosModel();
			app.view.ModificacionOfrecimientosRecibidosView viewMod = new app.view.ModificacionOfrecimientosRecibidosView();
			app.controller.ModificacionOfrecimientosRecibidosController controllerMod = 
					new app.controller.ModificacionOfrecimientosRecibidosController(modelMod, viewMod, empresaSeleccionada.getId());    
			controllerMod.mostrarVista();
		}
		else {
			javax.swing.JOptionPane.showMessageDialog(frame, 
					"Acción no implementada: " + buttonText + "\nEmpresa seleccionada: " + empresaSeleccionada.getNombre());
		}
	}

	private void handleReporteroFreelanceButtonAction(String buttonText) {
		ReporteroDTO reporteroSeleccionado = (ReporteroDTO) cbReporterosFreelance.getSelectedItem();
		if (reporteroSeleccionado == null) {
			javax.swing.JOptionPane.showMessageDialog(frame, 
					"Debe seleccionar un reportero freelance para continuar.", 
					"Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		// HU #34070: Elegir reportajes a realizar siendo un Reportero Freelance
		if (buttonText.contains("Elegir reportajes a realizar")) {
			FreelanceDecisionModel model = new FreelanceDecisionModel();
			FreelanceDecisionView view = new FreelanceDecisionView();
			new FreelanceDecisionController(model, view, reporteroSeleccionado.getId());
		}
		// Resto de botones no implementados
		else {
			javax.swing.JOptionPane.showMessageDialog(frame, "Acción no implementada: " + buttonText);
		}
	}

	public JFrame getFrame() { return this.frame; }
}