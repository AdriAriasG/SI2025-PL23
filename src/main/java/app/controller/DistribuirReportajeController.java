package app.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import app.dto.AgenciaDTO;
import app.dto.EventoDTO;
import app.dto.EmpresaComunicacionDTO;
import app.model.DistribuirReportajeModel;
import app.view.DistribuirReportajeView;

public class DistribuirReportajeController {

    private DistribuirReportajeModel model;
    private DistribuirReportajeView view;
    private AgenciaDTO agencia;

    public DistribuirReportajeController(
            DistribuirReportajeModel model,
            DistribuirReportajeView view,
            AgenciaDTO agencia) {

        this.model = model;
        this.view = view;
        this.agencia = agencia;

        initView();
        initController();
    }

    private void initView() {
        view.setNombreAgencia(agencia.getNombre());
        cargarEventos();
        view.getFrame().setVisible(true);
    }

    private void initController() {

        // Selección de evento
        view.getTablaEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarEmpresas();
            }
        });

        
        view.getBtnConceder().addActionListener(e -> moverEmpresa());
        
        view.getBtnAceptar().addActionListener(e -> guardarAccesos());
        
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());
    }

    private void cargarEventos() {
        List<EventoDTO> eventos = model.getEventosConReportaje(agencia.getId());
        view.setEventos(eventos);
    }

    private void cargarEmpresas() {
        int idEvento = view.getIdEventoSeleccionado();
        if (idEvento == -1) return;

        List<EmpresaComunicacionDTO> empresas =
                model.getEmpresasAceptadasSinAcceso(idEvento);

        view.setEmpresas(empresas);
    }

    private void concederAcceso() {
        int idEvento = view.getIdEventoSeleccionado();
        List<Integer> empresasSeleccionadas = view.getEmpresasSeleccionadasIds();

        if (idEvento == -1 || empresasSeleccionadas.isEmpty()) {
            view.showError("Debe seleccionar un evento y al menos una empresa.");
            return;
        }

        for (Integer idEmpresa : empresasSeleccionadas) {
            model.concederAcceso(idEvento, idEmpresa);
        }

        view.showInfo("Acceso concedido correctamente.");
        cargarEmpresas();
    }
    
    private void guardarAccesos() {

        int idEvento = view.getIdEventoSeleccionado();

        if (idEvento == -1) {
            view.showError("Debe seleccionar un evento.");
            return;
        }

        JTable tablaSeleccionadas = view.getTablaSeleccionadas();
        DefaultTableModel modelTabla =
                (DefaultTableModel) tablaSeleccionadas.getModel();

        if (modelTabla.getRowCount() == 0) {
            view.showError("No hay empresas seleccionadas.");
            return;
        }

        for (int i = 0; i < modelTabla.getRowCount(); i++) {

            int idEmpresa = (int) modelTabla.getValueAt(i, 0);

            model.concederAcceso(idEvento, idEmpresa);
        }

        view.showInfo("Acceso concedido correctamente.");
        view.getFrame().dispose();
        // Recargar empresas
        cargarEmpresas();

        // Limpiar tabla derecha
        modelTabla.setRowCount(0);
    }
    
    private void moverEmpresa() {

        int row = view.getTablaEmpresas().getSelectedRow();

        if (row == -1) {
            view.showError("Debe seleccionar una empresa.");
            return;
        }

        DefaultTableModel modelEmpresas =
                (DefaultTableModel) view.getTablaEmpresas().getModel();

        DefaultTableModel modelSeleccionadas =
                (DefaultTableModel) view.getTablaSeleccionadas().getModel();

        Object id = modelEmpresas.getValueAt(row, 0);
        Object nombre = modelEmpresas.getValueAt(row, 1);

        modelSeleccionadas.addRow(new Object[]{id, nombre});
        modelEmpresas.removeRow(row);
    }
}