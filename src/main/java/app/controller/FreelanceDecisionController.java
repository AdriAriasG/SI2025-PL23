package app.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import app.model.FreelanceDecisionModel;
import app.view.FreelanceDecisionView;

public class FreelanceDecisionController {

	private final FreelanceDecisionModel model;
	private final FreelanceDecisionView view;
	private final int idReportero;

	public FreelanceDecisionController(FreelanceDecisionModel model, FreelanceDecisionView view, int idReportero) {
		this.model = model;
		this.view = view;
		this.idReportero = idReportero;

		initView();
		initController();
	}

	private void initView() {
		cargarEventos();

		if (view.getTablaEventos().getRowCount() > 0) {
			view.getTablaEventos().setRowSelectionInterval(0, 0);
			cargarDecisionEventoSeleccionado();
		}

		view.setVisible(true);
	}

	private void initController() {
		view.getTablaEventos().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				cargarDecisionEventoSeleccionado();
			}
		});

		view.getBtnAceptar().addActionListener(e -> onAceptar());
		view.getBtnCancelar().addActionListener(e -> view.dispose());
	}

	private void cargarEventos() {
		List<Object[]> eventos = model.getEventosDisponibles(idReportero);

		DefaultTableModel tm = (DefaultTableModel) view.getTablaEventos().getModel();
		tm.setRowCount(0);

		for (Object[] fila : eventos) {
			tm.addRow(new Object[] {
				fila[0],
				fila[1],
				fila[2]
			});
		}
	}

	private void cargarDecisionEventoSeleccionado() {
		int row = view.getTablaEventos().getSelectedRow();
		if (row == -1) {
			return;
		}

		int idEvento = ((Number) view.getTablaEventos().getValueAt(row, 0)).intValue();
		String decision = model.getDecisionActual(idEvento, idReportero);

		if (decision == null || decision.isBlank()) {
			view.getCbDecision().setSelectedItem("INTERESADO");
		} else {
			view.getCbDecision().setSelectedItem(decision);
		}
	}

	private void onAceptar() {
		int row = view.getTablaEventos().getSelectedRow();

		if (row == -1) {
			javax.swing.JOptionPane.showMessageDialog(view, "Debe seleccionar un evento.");
			return;
		}

		int idEvento = ((Number) view.getTablaEventos().getValueAt(row, 0)).intValue();
		String decision = (String) view.getCbDecision().getSelectedItem();

		if (decision == null || decision.isBlank()) {
			javax.swing.JOptionPane.showMessageDialog(view, "Debe seleccionar una decisión.");
			return;
		}

		model.guardarDecision(idEvento, idReportero, decision);

		javax.swing.JOptionPane.showMessageDialog(view, "Decisión guardada correctamente.");
		cargarDecisionEventoSeleccionado();
	}
}