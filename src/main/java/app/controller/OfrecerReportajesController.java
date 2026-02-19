package app.controller;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import app.dto.AgenciaDTO;
import app.dto.EventoDTO;
import app.model.OfrecerReportajesModel;
import app.view.OfrecerReportajesView;

public class OfrecerReportajesController {

    private final OfrecerReportajesModel model;
    private final OfrecerReportajesView view;
    private final AgenciaDTO agencia;

    public OfrecerReportajesController(OfrecerReportajesModel model, OfrecerReportajesView view, AgenciaDTO agencia) {
        this.model = model;
        this.view = view;
        this.agencia = agencia;

        initView();
        initController();
    }

    private void initView() {
    	cargarEventos();
        view.setVisible(true);

    }
    
    private void cargarEventos() {
    	List<EventoDTO> eventos = model.getEventosConAsignados(agencia.getId());

        DefaultTableModel tm = (DefaultTableModel) view.getTablaEventos().getModel();
        tm.setRowCount(0); // limpia tabla

        for (EventoDTO e : eventos) {
            tm.addRow(new Object[] { e.getId(), e.getNombre(), e.getFecha() });
        }
    }
    
    private void initController() {

    	
    }
}
