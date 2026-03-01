package app.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import app.dto.AgenciaDTO;
import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
import app.model.ModificarOfrecimientoModel;
import app.view.ModificarOfrecimientoView;

public class ModificarOfrecimientoController {

    private ModificarOfrecimientoModel model;
    private ModificarOfrecimientoView view;
    private AgenciaDTO agenciaSeleccionada;

    private Integer idEventoSeleccionado = null;


    private Set<Integer> empresasOfrecer = new HashSet<>();
    private Set<Integer> empresasQuitar = new HashSet<>();

    public ModificarOfrecimientoController(
            ModificarOfrecimientoModel model,
            ModificarOfrecimientoView view,
            AgenciaDTO agenciaSeleccionada) {

        this.model = model;
        this.view = view;
        this.agenciaSeleccionada = agenciaSeleccionada;

        initView();
        initController();
    }

    private void initView() {
        limpiarTablasEmpresas();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        cargarEventos();
    }

    private void initController() {

        view.getBtnAceptar().addActionListener(e -> guardarCambios());

        view.getComboFiltro().addActionListener(e -> {
            cargarEmpresas();
        });

        view.getBtnOfrecer().addActionListener(e -> ofrecerEmpresa());

        view.getBtnQuitar().addActionListener(e -> quitarEmpresa());

        view.getBtnCancelar().addActionListener(e -> view.dispose());

        view.getTablaEventos().getSelectionModel()
            .addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    seleccionarEvento();
                }
            });
    }

    private void guardarCambios() {

        for (Integer id : empresasOfrecer) {
            model.ofrecerEmpresa(idEventoSeleccionado, id);
        }

        for (Integer id : empresasQuitar) {
            model.quitarOfrecimiento(idEventoSeleccionado, id);
        }

        empresasOfrecer.clear();
        empresasQuitar.clear();

        view.dispose();
    }

    private void seleccionarEvento() {

        int fila = view.getTablaEventos().getSelectedRow();
        if (fila == -1) return;

        idEventoSeleccionado =
            (int) view.getTablaEventos().getValueAt(fila, 0);

        cargarEmpresas();
    }

    private void cargarEmpresas() {

        if (idEventoSeleccionado == null) {
            limpiarTablasEmpresas();
            return;
        }

        List<EmpresaComunicacionDTO> con =
            model.getEmpresasConOfrecimiento(idEventoSeleccionado);

        List<EmpresaComunicacionDTO> sin =
            model.getEmpresasSinOfrecimiento(idEventoSeleccionado);

        // Aplicar movimientos pendientes

        for (Integer id : empresasOfrecer) {
            EmpresaComunicacionDTO e = model.getEmpresaById(id);
            if (e != null) {
                con.add(e);
                sin.removeIf(emp -> emp.getId() == id);
            }
        }

        for (Integer id : empresasQuitar) {
            EmpresaComunicacionDTO e = model.getEmpresaById(id);
            if (e != null) {
                sin.add(e);
                con.removeIf(emp -> emp.getId() == id);
            }
        }

        String opcion =
            (String) view.getComboFiltro().getSelectedItem();

        boolean modoSin =
            opcion != null && opcion.toLowerCase().contains("sin");

        if (modoSin) {

            // izquierda: TODAS las disponibles
            cargarTabla(view.getTablaDisponibles(), sin);

            // derecha: SOLO las que estén en empresasOfrecer
            List<EmpresaComunicacionDTO> soloMovidas = new ArrayList<>();
            for (EmpresaComunicacionDTO e : con) {
                if (empresasOfrecer.contains(e.getId())) {
                    soloMovidas.add(e);
                }
            }

            cargarTabla(view.getTablaOfrecidas(), soloMovidas);

        } else {

            // derecha: TODAS las ofrecidas
            cargarTabla(view.getTablaOfrecidas(), con);

            // izquierda: SOLO las que estén en empresasQuitar
            List<EmpresaComunicacionDTO> soloMovidas = new ArrayList<>();
            for (EmpresaComunicacionDTO e : sin) {
                if (empresasQuitar.contains(e.getId())) {
                    soloMovidas.add(e);
                }
            }

            cargarTabla(view.getTablaDisponibles(), soloMovidas);
        }
    }

    private void cargarTabla(
            JTable tabla,
            List<EmpresaComunicacionDTO> datos) {

        DefaultTableModel modelTabla =
            (DefaultTableModel) tabla.getModel();

        modelTabla.setRowCount(0);

        for (EmpresaComunicacionDTO e : datos) {
            modelTabla.addRow(new Object[]{ e });
        }
    }

    private void ofrecerEmpresa() {

        int fila = view.getTablaDisponibles().getSelectedRow();
        if (fila == -1) return;

        EmpresaComunicacionDTO empresa =
            (EmpresaComunicacionDTO)
                view.getTablaDisponibles()
                    .getValueAt(fila, 0);

        int idEmpresa = empresa.getId();

        empresasOfrecer.add(idEmpresa);
        empresasQuitar.remove(idEmpresa);


        cargarEmpresas();
    }

    private void quitarEmpresa() {

        int fila = view.getTablaOfrecidas().getSelectedRow();
        if (fila == -1) return;

        EmpresaComunicacionDTO empresa =
            (EmpresaComunicacionDTO)
                view.getTablaOfrecidas()
                    .getValueAt(fila, 0);

        int idEmpresa = empresa.getId();

        if (model.tieneAccesoConcedido(idEventoSeleccionado, idEmpresa)) {

        	javax.swing.JOptionPane.showMessageDialog(
        		    view,
        		    "No se puede quitar el ofrecimiento.\nLa empresa ya tiene acceso concedido al reportaje.",
        		    "Operación no permitida",
        		    javax.swing.JOptionPane.WARNING_MESSAGE
        		);

            return;
        }

        empresasQuitar.add(idEmpresa);
        empresasOfrecer.remove(idEmpresa);

        cargarEmpresas();
    }

    private void limpiarTablasEmpresas() {

        ((DefaultTableModel)
            view.getTablaOfrecidas().getModel())
                .setRowCount(0);

        ((DefaultTableModel)
            view.getTablaDisponibles().getModel())
                .setRowCount(0);
    }

    private void cargarEventos() {

        List<EventoDTO> eventos =
            model.getEventosByAgencia(
                agenciaSeleccionada.getId());

        DefaultTableModel modelTabla =
            (DefaultTableModel)
                view.getTablaEventos().getModel();

        modelTabla.setRowCount(0);

        for (EventoDTO e : eventos) {
            modelTabla.addRow(new Object[]{
                e.getId(),
                e.getNombre(),
                e.getFecha()
            });
        }

        view.getTablaEventos().clearSelection();
    }
}