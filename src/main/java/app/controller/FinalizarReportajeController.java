package app.controller;

import java.util.List;

import javax.swing.JOptionPane;

import app.dto.EventoDTO;
import app.dto.MultimediaDTO;
import app.dto.RevisionDTO;
import app.dto.VersionDTO;
import app.model.ReportajeModel;
import app.view.FinalizarReportajeView;
import giis.demo.util.SwingUtil;

public class FinalizarReportajeController {

	private ReportajeModel model;
	private FinalizarReportajeView view;
	private int idReportero;

	private List<EventoDTO> listaEventos;
	private List<RevisionDTO> listaRevisiones;
	private List<MultimediaDTO> listaMultimedia;

	public FinalizarReportajeController(
			ReportajeModel model,
			FinalizarReportajeView view,
			int idReportero) {

		this.model = model;
		this.view = view;
		this.idReportero = idReportero;

		initView();
		initController();
	}

	private void initView() {
		cargarEventosEnRevision();
		view.getFrame().setVisible(true);
	}

	private void initController() {

		view.getTablaEventos().getSelectionModel().addListSelectionListener(e ->
		SwingUtil.exceptionWrapper(() -> seleccionarEvento())
				);

		view.getBtnGuardarCambios().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> guardarCambios())
				);

		view.getBtnFinalizar().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> finalizarReportaje())
				);

		view.getBtnAñadirMultimedia().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> añadirMultimedia())
				);

		view.getBtnCambiarEstado().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> cambiarEstadoMultimedia())
				);

		view.getBtnEliminarMultimedia().addActionListener(e ->
		SwingUtil.exceptionWrapper(() -> eliminarMultimedia())
				);
	}

	private void seleccionarEvento() {

		int fila = view.getTablaEventos().getSelectedRow();
		if (fila < 0) return;

		EventoDTO evento = listaEventos.get(fila);

		// Título desde Reportaje
		String titulo = model.getTituloReportaje(evento.getId());
		view.getTxtTitulo().setText(titulo != null ? titulo : "");

		// Subtítulo y cuerpo desde VersionReportaje
		VersionDTO version = model.getVersionActual(evento.getId());

		if (version != null) {
			view.getTxtSubtitulo().setText(version.getSubtitulo());
			view.getTxtCuerpo().setText(version.getCuerpo());
		} else {
			view.getTxtSubtitulo().setText("");
			view.getTxtCuerpo().setText("");
		}

		cargarRevisiones(evento.getId());
		cargarMultimedia(evento.getId());
	}

	/*
	 * Solo el autor 
	 */
	private void cargarEventosEnRevision() {

		String[] columnas = {"id", "nombre", "fecha"};

		listaEventos = model.getReportajesEnRevisionComoAutor(idReportero);

		view.getTablaEventos().setModel(
				SwingUtil.getTableModelFromPojos(listaEventos, columnas)
				);

		SwingUtil.autoAdjustColumns(view.getTablaEventos());
	}

	private void cargarRevisiones(int idEvento) {

		listaRevisiones = model.getRevisionesReportaje(idEvento);

		String[] columnas = {"idReportero", "estado", "comentario"};

		view.getTablaRevisiones().setModel(
				SwingUtil.getTableModelFromPojos(listaRevisiones, columnas)
				);

		SwingUtil.autoAdjustColumns(view.getTablaRevisiones());
	}

	/*
	 * Guardar cambios (sobrescribe la última versión)
	 */
	private void guardarCambios() {

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

		model.modificarContenidoFinal(
				evento.getId(),
				idReportero,
				titulo,
				subtitulo,
				cuerpo
				);

		JOptionPane.showMessageDialog(view.getFrame(),
				"Cambios guardados correctamente");
	}

	private void finalizarReportaje() {

		int fila = view.getTablaEventos().getSelectedRow();
		if (fila < 0)
			throw new IllegalArgumentException("Debe seleccionar un evento");

		EventoDTO evento = listaEventos.get(fila);

		model.finalizarReportaje(
				evento.getId(),
				idReportero
				);

		JOptionPane.showMessageDialog(view.getFrame(),
				"Reportaje FINALIZADO correctamente");

		cargarEventosEnRevision();
	}

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

	private void cambiarEstadoMultimedia() {

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









