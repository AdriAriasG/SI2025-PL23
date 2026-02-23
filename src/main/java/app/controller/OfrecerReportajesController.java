package app.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import app.dto.AgenciaDTO;
import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
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

        // Selecciona el primer evento para que al abrir ya cargue empresas disponibles
        if (view.getTablaEventos().getRowCount() > 0) {
            view.getTablaEventos().setRowSelectionInterval(0, 0);
            onEventoSeleccionado();
        }

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
        // Selección de evento => recarga empresas disponibles y limpia seleccionadas
        view.getTablaEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                onEventoSeleccionado();
            }
        });

        // Botones
        view.getBtnOfrecer().addActionListener(e -> onOfrecer());
        view.getBtnAceptar().addActionListener(e -> onAceptar());
        view.getBtnCancelar().addActionListener(e -> view.dispose());
    }

    private void onEventoSeleccionado() {
        int row = view.getTablaEventos().getSelectedRow();
        if (row == -1) return;

        idEventoSeleccionado = (int) view.getTablaEventos().getValueAt(row, 0);

        // Limpia la tabla de seleccionadas al cambiar de evento
        ((DefaultTableModel) view.getTablaSeleccionadas().getModel()).setRowCount(0);

        cargarEmpresasDisponibles();
    }

    private void cargarEmpresasDisponibles() {
        if (idEventoSeleccionado == -1) return;

        List<EmpresaComunicacionDTO> empresas = model.getEmpresasDisponibles(idEventoSeleccionado);

        DefaultTableModel tm = (DefaultTableModel) view.getTablaDisponibles().getModel();
        tm.setRowCount(0);

        for (EmpresaComunicacionDTO ec : empresas) {
            tm.addRow(new Object[] { ec.getNombre() }); // solo nombre
        }
    }

    private void onOfrecer() {
        if (idEventoSeleccionado == -1) {
            javax.swing.JOptionPane.showMessageDialog(view, "Selecciona un evento primero.");
            return;
        }

        int[] selectedRows = view.getTablaDisponibles().getSelectedRows();
        if (selectedRows.length == 0) {
            javax.swing.JOptionPane.showMessageDialog(view, "Selecciona al menos una empresa para ofrecer.");
            return;
        }

        DefaultTableModel tmDisp = (DefaultTableModel) view.getTablaDisponibles().getModel();
        DefaultTableModel tmSel  = (DefaultTableModel) view.getTablaSeleccionadas().getModel();

        // Recoger nombres seleccionados
        java.util.List<String> nombres = new java.util.ArrayList<>();
        for (int row : selectedRows) {
            nombres.add((String) tmDisp.getValueAt(row, 0)); // col 0 = Nombre
        }

        // Pasar a seleccionadas (sin duplicados)
        for (String nombre : nombres) {
            if (!yaEstaEnSeleccionadas(tmSel, nombre)) {
                tmSel.addRow(new Object[] { nombre });
            }
        }

        // Eliminar de disponibles (de mayor a menor índice)
        java.util.Arrays.sort(selectedRows);
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            tmDisp.removeRow(selectedRows[i]);
        }
    }

    private void onAceptar() {
        if (idEventoSeleccionado == -1) {
            javax.swing.JOptionPane.showMessageDialog(view, "Selecciona un evento primero.");
            return;
        }

        DefaultTableModel tmSel = (DefaultTableModel) view.getTablaSeleccionadas().getModel();

        // Guardar ofrecimientos PENDIENTES en BD para el evento seleccionado
        for (int i = 0; i < tmSel.getRowCount(); i++) {
            String nombreEmpresa = (String) tmSel.getValueAt(i, 0);

            Integer idEmpresa = model.getIdEmpresaPorNombre(nombreEmpresa);
            if (idEmpresa != null) {
                model.crearOfrecimientoPendiente(idEventoSeleccionado, idEmpresa);
            }
        }

        // Cerrar ventana
        view.dispose();
    }

    private boolean yaEstaEnSeleccionadas(DefaultTableModel tmSel, String nombre) {
        for (int i = 0; i < tmSel.getRowCount(); i++) {
            Object val = tmSel.getValueAt(i, 0); // col 0 = Nombre
            if (val != null && nombre.equals(val.toString())) {
                return true;
            }
        }
        return false;
    }
}