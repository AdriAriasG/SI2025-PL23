package app.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import app.dto.EventoDTO;
import app.dto.VersionDTO;
import app.model.ReportajeModel;
import app.view.ModificarEntregaView;
import giis.demo.util.SwingUtil;

public class ModificarEntregaController{
	private ReportajeModel model;
	private ModificarEntregaView view;
	private int idReportero;

	private List<EventoDTO> listaEventos;

	public ModificarEntregaController(ReportajeModel model, ModificarEntregaView view, int idReportero) {
		this.model = model;
		this.view = view;
		this.idReportero = idReportero;

		initView();
		initController();
	}

	private void initView() {
		cargarEventos(false); // por defecto: sin reportaje
		view.getFrame().setVisible(true);
	}

	private void initController() {

		// Filtro: sin reportaje
		view.getRbSinReportaje().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> cargarEventos(false)));

		// Filtro: con reportaje
		view.getRbConReportaje().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> cargarEventos(true)));

		// Selección de evento
		view.getTablaEventos().getSelectionModel().addListSelectionListener(e ->
		SwingUtil.exceptionWrapper(() -> cargarContenidoSiExiste())
				);

		// Botón modificar
		view.getBtnModificar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> modificar());
			}
		});
	}

	private void cargarEventos(boolean soloEntregados) {

		listaEventos = model.getEventosAsignados(idReportero, soloEntregados);

		String[] columnas = {"id", "nombre", "fecha"};

		view.getTablaEventos().setModel(
				SwingUtil.getTableModelFromPojos(listaEventos, columnas));

		SwingUtil.autoAdjustColumns(view.getTablaEventos());
		view.limpiarFormulario();
	}

	private void cargarContenidoSiExiste() {

		int fila = view.getTablaEventos().getSelectedRow();
		if (fila < 0) return;

		EventoDTO evento = listaEventos.get(fila);

		if (!view.getRbConReportaje().isSelected()) {
			view.limpiarFormulario();
			return;
		}

		VersionDTO version = model.getVersionActual(evento.getId());

		if (version != null) {
			view.getTxtSubtitulo().setText(version.getSubtitulo());
			view.getTxtCuerpo().setText(version.getCuerpo());
		}
	}

	private void modificar() {

		int fila = view.getTablaEventos().getSelectedRow();

		if (fila < 0) {
			throw new RuntimeException("Debe seleccionar un evento");
		}

		EventoDTO evento = listaEventos.get(fila);

		String nuevoSubtitulo = view.getTxtSubtitulo().getText().trim();
		String nuevoCuerpo = view.getTxtCuerpo().getText().trim();

		model.modificarReportaje(
				evento.getId(),
				idReportero,
				nuevoSubtitulo,
				nuevoCuerpo
				);

		VersionDTO version = model.getVersionActual(evento.getId());

		if (version != null) {
			view.getTxtSubtitulo().setText(version.getSubtitulo());
			view.getTxtCuerpo().setText(version.getCuerpo());
		}

		JOptionPane.showMessageDialog(view.getFrame(),
				"Modificación realizada correctamente");
	}
}

