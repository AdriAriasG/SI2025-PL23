package app.controller;

import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;

import app.dto.AgenciaDTO;
import app.dto.EventoDTO;
import app.dto.EmpresaComunicacionDTO;

import app.model.OfrecerReportajesModel;
import app.view.OfrecerReportajesView;

public class OfrecerReportajesController {

    private final OfrecerReportajesModel model;
    private final OfrecerReportajesView view;
    private final AgenciaDTO agencia;
    private int idEventoSeleccionado = -1;

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
    	view.getTablaEventos().addMouseListener(new MouseAdapter() {
    	    @Override
    	    public void mouseReleased(MouseEvent e) {
    	        onEventoSeleccionado();
    	    }
    	});
    	
    }
    
    private void onEventoSeleccionado() {
        int row = view.getTablaEventos().getSelectedRow();
        if (row == -1) return;

        idEventoSeleccionado = (int) view.getTablaEventos().getValueAt(row, 0);
        cargarEmpresasDisponibles();
    }
    
    private void cargarEmpresasDisponibles() {
        if (idEventoSeleccionado == -1) return;

        List<EmpresaComunicacionDTO> empresas = model.getEmpresasDisponibles(idEventoSeleccionado);

        DefaultTableModel tm = (DefaultTableModel) view.getTablaDisponibles().getModel();
        tm.setRowCount(0);

        for (EmpresaComunicacionDTO ec : empresas) {
            tm.addRow(new Object[] { ec.getNombre() });
        }
    }
}
