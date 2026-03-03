package app.controller;

import java.util.List;

import app.dto.EventoDTO;
import app.model.ReportajeModel;
import app.view.ReportajeView;
import giis.demo.util.SwingUtil;

public class ReportajeController {

	private ReportajeModel model;
	private ReportajeView view;
	private int idReportero;
	
	private List<EventoDTO> listaEventos;

	public ReportajeController(ReportajeModel model, ReportajeView view, int idReportero) {
		this.model = model;
		this.view = view;
		this.idReportero = idReportero;

		initView();
		initController();
	}

	private void initView() {
		cargarEventosPendientes();
		view.getFrame().setVisible(true);
	}
	
	private void initController() {
		view.getBtnEntregar().addActionListener(e -> {
			SwingUtil.exceptionWrapper(() -> entregarReportaje());
		});
		
	}

	private void cargarEventosPendientes() {
	    String[] columnas = {"id", "nombre", "fecha"};
	    
	    listaEventos = model.getEventosAsignados(idReportero, true);
	    
	    view.getTablaEventos().setModel(
	            SwingUtil.getTableModelFromPojos(listaEventos, columnas));
	    
	    SwingUtil.autoAdjustColumns(view.getTablaEventos());
	}
	
	private void entregarReportaje() {
		int fila = view.getTablaEventos().getSelectedRow();
		
		if (fila < 0) 
			throw new IllegalArgumentException("Debe seleccionar un evento");
		
		int idEvento = listaEventos.get(fila).getId();
		
		String titulo = view.getTxtTitulo().getText().trim();
		String subtitulo = view.getTxtSubtitulo().getText().trim();
		String cuerpo = view.getTxtCuerpo().getText().trim();
		
		if (titulo.isEmpty())
			throw new IllegalArgumentException("El título no puede estar vacío");
		
		if (cuerpo.isEmpty())
			throw new IllegalArgumentException("El cuerpo no puede estar vacío");
		
		model.entregarReportaje(idEvento, idReportero, titulo, subtitulo, cuerpo);
		
		cargarEventosPendientes();
		limpiarFormulario();
	}

	private void limpiarFormulario() {
		view.getTxtTitulo().setText("");
		view.getTxtSubtitulo().setText("");
		view.getTxtCuerpo().setText("");
	}

}
