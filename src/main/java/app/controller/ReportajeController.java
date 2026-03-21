package app.controller;

import java.util.List;

import javax.swing.JOptionPane;

import app.dto.EventoDTO;
import app.dto.MultimediaDTO;
import app.dto.VersionDTO;
import app.model.ReportajeModel;
import app.view.ReportajeView;
import giis.demo.util.SwingUtil;

public class ReportajeController {

	private ReportajeModel model;
	private ReportajeView view;
	private int idReportero;

	private List<EventoDTO> listaEventos;
	private List<MultimediaDTO> listaMultimedia;

	public ReportajeController(ReportajeModel model, ReportajeView view, int idReportero) {
		this.model = model;
		this.view = view;
		this.idReportero = idReportero;

		initView();
		initController();
	}

	private void initView() {
		cargarEventosAsignados();
		view.getFrame().setVisible(true);
	}

	private void initController() {

		// Selección evento
		view.getTablaEventos().getSelectionModel().addListSelectionListener(e ->
		SwingUtil.exceptionWrapper(() -> seleccionarEvento())
				);

		// Entregar
		view.getBtnEntregar().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> entregarReportaje())
				);

		// Modificar
		view.getBtnModificar().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> modificarReportaje())
				);

		// Añadir multimedia
		view.getBtnAñadirMultimedia().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> añadirMultimedia())
				);

		// Cambiar estado
		view.getBtnCambiarEstado().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> cambiarEstado())
				);

		// Eliminar multimedia
		view.getBtnEliminar().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> eliminarMultimedia())
				);
	}

	private void cargarEventosAsignados() {

		String[] columnas = {"id", "nombre", "fecha"};

		listaEventos = model.getEventosAsignados(idReportero, true);

		view.getTablaEventos().setModel(
				SwingUtil.getTableModelFromPojos(listaEventos, columnas));

		SwingUtil.autoAdjustColumns(view.getTablaEventos());
	}

	private void seleccionarEvento() {

		int fila = view.getTablaEventos().getSelectedRow();
		if (fila < 0) return;

		EventoDTO evento = listaEventos.get(fila);

		boolean existe = model.existeReportaje(evento.getId());

		if (existe) {

			VersionDTO version = model.getVersionActual(evento.getId());

			view.getTxtTitulo().setEditable(false);
			view.getBtnEntregar().setVisible(false);
			view.getBtnModificar().setVisible(true);

			if (version != null) {
				view.getTxtSubtitulo().setText(version.getSubtitulo());
				view.getTxtCuerpo().setText(version.getCuerpo());
			}

			view.habilitarMultimedia(true);
			cargarMultimedia(evento.getId());

		} else {

			view.getTxtTitulo().setEditable(true);
			view.getBtnEntregar().setVisible(true);
			view.getBtnModificar().setVisible(false);

			view.limpiarFormulario();
			view.habilitarMultimedia(false);
		}
	}

	private void entregarReportaje() {

		int fila = view.getTablaEventos().getSelectedRow();
		if (fila < 0)
			throw new IllegalArgumentException("Debe seleccionar un evento");

		EventoDTO evento = listaEventos.get(fila);

		String titulo = view.getTxtTitulo().getText().trim();
		String subtitulo = view.getTxtSubtitulo().getText().trim();
		String cuerpo = view.getTxtCuerpo().getText().trim();

		if (titulo.isEmpty())
			throw new IllegalArgumentException("El título no puede estar vacío");

		if (cuerpo.isEmpty())
			throw new IllegalArgumentException("El cuerpo no puede estar vacío");

		model.entregarReportaje(
				evento.getId(),
				idReportero,
				titulo,
				subtitulo,
				cuerpo
				);

		seleccionarEvento();

		JOptionPane.showMessageDialog(view.getFrame(),
				"Reportaje entregado correctamente");
	}

	private void modificarReportaje() {

		int fila = view.getTablaEventos().getSelectedRow();
		if (fila < 0)
			throw new IllegalArgumentException("Debe seleccionar un evento");

		EventoDTO evento = listaEventos.get(fila);

		model.modificarReportaje(
				evento.getId(),
				idReportero,
				view.getTxtSubtitulo().getText().trim(),
				view.getTxtCuerpo().getText().trim()
				);

		seleccionarEvento();

		JOptionPane.showMessageDialog(view.getFrame(),
				"Modificación realizada correctamente");
	}

	// =========================
	// MULTIMEDIA
	// =========================

	private void cargarMultimedia(int idEvento) {

		listaMultimedia = model.getMultimedia(idEvento);

		String[] columnas = {"id", "ruta", "tipo", "estado"};

		view.getTablaMultimedia().setModel(
				SwingUtil.getTableModelFromPojos(listaMultimedia, columnas)
				);

		SwingUtil.autoAdjustColumns(view.getTablaMultimedia());
	}

	private void añadirMultimedia() {

		int fila = view.getTablaEventos().getSelectedRow();
		if (fila < 0)
			throw new IllegalArgumentException("Debe seleccionar un evento");

		EventoDTO evento = listaEventos.get(fila);

		String ruta = view.getTxtRuta().getText().trim();

		if (ruta.isEmpty())
			throw new IllegalArgumentException("Debe indicar una ruta");

		String tipo = view.getRbImagen().isSelected() ? "IMAGEN" : "VIDEO";

		model.añadirMultimedia(
				evento.getId(),
				idReportero,
				ruta,
				tipo
				);

		cargarMultimedia(evento.getId());
		view.getTxtRuta().setText("");
	}

	private void cambiarEstado() {

		int fila = view.getTablaMultimedia().getSelectedRow();
		if (fila < 0)
			throw new IllegalArgumentException("Debe seleccionar un multimedia");

		MultimediaDTO multimedia = listaMultimedia.get(fila);

		model.cambiarEstadoMultimedia(
				multimedia.getId(),
				idReportero
				);

		seleccionarEvento();
	}

	private void eliminarMultimedia() {

		int fila = view.getTablaMultimedia().getSelectedRow();
		if (fila < 0)
			throw new IllegalArgumentException("Debe seleccionar un multimedia");

		MultimediaDTO multimedia = listaMultimedia.get(fila);

		model.eliminarMultimedia(
				multimedia.getId(),
				idReportero
				);

		seleccionarEvento();
	}
}