package app.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import app.dto.AgenciaDTO;
import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
import app.model.DistribuirReportajeModel;
import app.util.ApplicationException;
import app.view.DistribuirReportajeView;

public class DistribuirReportajeController {

    private static final String FILTRO_SIN_ACCESO = "Empresas Sin Acceso";
    private static final String FILTRO_CON_ACCESO = "Empresas Con Acceso";

    private static final String FILTRO_TODOS_REPORTAJES = "Todos los reportajes";
    private static final String FILTRO_REPORTAJES_CON_EMBARGO = "Reportajes con embargo";

    private DistribuirReportajeModel model;
    private DistribuirReportajeView view;
    private AgenciaDTO agencia;

    private Integer idEventoActual = null;

    // Empresas movidas de izquierda a derecha pendientes de guardar (acceso normal)
    private Map<Integer, String> pendientesConceder = new LinkedHashMap<>();

    // Empresas movidas de izquierda a derecha pendientes de guardar (acceso especial)
    private Map<Integer, String> pendientesEspecial = new LinkedHashMap<>();

    // Empresas movidas de derecha a izquierda pendientes de guardar
    private Map<Integer, String> pendientesQuitar = new LinkedHashMap<>();

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
        view.setTextoEmpresas("Empresas Sin Acceso");
        view.setTextoSeleccionadas("Empresas Con Acceso");
        cargarEventos();
        view.limpiarEmpresas();
        view.limpiarSeleccionadas();
        view.getFrame().setVisible(true);
    }

    private void initController() {

        view.getTablaEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actualizarTablasSegunFiltro();
            }
        });

        view.getComboFiltro().addActionListener(e -> actualizarTablasSegunFiltro());

        view.getComboFiltroReportajes().addActionListener(e -> {
            idEventoActual = null;
            limpiarPendientes();
            cargarEventos();
            view.limpiarEmpresas();
            view.limpiarSeleccionadas();
        });

        view.getBtnConceder().addActionListener(e -> moverIzquierdaADerecha());

        view.getBtnAccesoEspecial().addActionListener(e -> moverIzquierdaADerechaEspecial());

        view.getBtnQuitar().addActionListener(e -> moverDerechaAIzquierda());

        view.getBtnAceptar().addActionListener(e -> guardarCambios());

        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());
    }

    private void cargarEventos() {
        boolean soloConEmbargo =
                FILTRO_REPORTAJES_CON_EMBARGO.equals(view.getFiltroReportajesSeleccionado());

        List<EventoDTO> eventos = model.getEventosConReportaje(agencia.getId(), soloConEmbargo);
        view.setEventos(eventos);
    }

    private boolean esFiltroSinAcceso() {
        return FILTRO_SIN_ACCESO.equals(view.getFiltroSeleccionado());
    }

    @SuppressWarnings("unused")
    private boolean esFiltroTodosLosReportajes() {
        return FILTRO_TODOS_REPORTAJES.equals(view.getFiltroReportajesSeleccionado());
    }

    private void actualizarTablasSegunFiltro() {
        int idEvento = view.getIdEventoSeleccionado();

        if (idEvento == -1) {
            idEventoActual = null;
            limpiarPendientes();
            view.limpiarEmpresas();
            view.limpiarSeleccionadas();
            return;
        }

        if (idEventoActual == null || idEventoActual.intValue() != idEvento) {
            idEventoActual = idEvento;
            limpiarPendientes();
        }

        view.setTextoEmpresas("Empresas Sin Acceso");
        view.setTextoSeleccionadas("Empresas Con Acceso");

        if (esFiltroSinAcceso()) {
            cargarModoSinAcceso(idEvento);
        } else {
            cargarModoConAcceso(idEvento);
        }
    }

    private void cargarModoSinAcceso(int idEvento) {
        List<EmpresaComunicacionDTO> izquierda = model.getEmpresasAceptadasSinAcceso(idEvento);

        Set<Integer> idsConAccesoPendiente = new LinkedHashSet<>();
        idsConAccesoPendiente.addAll(pendientesConceder.keySet());
        idsConAccesoPendiente.addAll(pendientesEspecial.keySet());

        izquierda = quitarEmpresasPorId(izquierda, idsConAccesoPendiente);
        izquierda = anadirEmpresasDesdeMapa(izquierda, pendientesQuitar);

        Map<Integer, String> accesosPendientes = new LinkedHashMap<>();
        accesosPendientes.putAll(pendientesConceder);
        accesosPendientes.putAll(pendientesEspecial);

        List<EmpresaComunicacionDTO> derecha = construirListaDesdeMapa(accesosPendientes);

        view.setEmpresas(izquierda);
        view.setSeleccionadas(derecha);
    }

    private void cargarModoConAcceso(int idEvento) {
        List<EmpresaComunicacionDTO> derecha = model.getEmpresasAceptadasConAcceso(idEvento);

        derecha = quitarEmpresasPorId(derecha, pendientesQuitar.keySet());

        Map<Integer, String> accesosPendientes = new LinkedHashMap<>();
        accesosPendientes.putAll(pendientesConceder);
        accesosPendientes.putAll(pendientesEspecial);

        derecha = anadirEmpresasDesdeMapa(derecha, accesosPendientes);

        List<EmpresaComunicacionDTO> izquierda = construirListaDesdeMapa(pendientesQuitar);

        view.setEmpresas(izquierda);
        view.setSeleccionadas(derecha);
    }

    private void moverIzquierdaADerecha() {
        int row = view.getTablaEmpresas().getSelectedRow();

        if (row == -1) {
            view.showError("Debe seleccionar una empresa de la tabla izquierda.");
            return;
        }

        int idEvento = view.getIdEventoSeleccionado();
        if (idEvento == -1) {
            view.showError("Debe seleccionar un evento.");
            return;
        }

        DefaultTableModel modelIzquierda =
                (DefaultTableModel) view.getTablaEmpresas().getModel();

        DefaultTableModel modelDerecha =
                (DefaultTableModel) view.getTablaSeleccionadas().getModel();

        int idEmpresa = (int) modelIzquierda.getValueAt(row, 0);
        String nombre = (String) modelIzquierda.getValueAt(row, 1);

        // Deshacer una retirada pendiente
        if (pendientesQuitar.containsKey(idEmpresa)) {
            pendientesQuitar.remove(idEmpresa);

            if (!existeEmpresaEnTabla(idEmpresa, modelDerecha)) {
                modelDerecha.addRow(new Object[] { idEmpresa, nombre });
            }

            modelIzquierda.removeRow(row);
            return;
        }

        // Ahora se permite acceso normal también si hay embargo vigente
        pendientesEspecial.remove(idEmpresa);
        pendientesConceder.put(idEmpresa, nombre);

        if (!existeEmpresaEnTabla(idEmpresa, modelDerecha)) {
            modelDerecha.addRow(new Object[] { idEmpresa, nombre });
        }

        modelIzquierda.removeRow(row);
    }

    private void moverIzquierdaADerechaEspecial() {
        int row = view.getTablaEmpresas().getSelectedRow();

        if (row == -1) {
            view.showError("Debe seleccionar una empresa de la tabla izquierda.");
            return;
        }

        int idEvento = view.getIdEventoSeleccionado();
        if (idEvento == -1) {
            view.showError("Debe seleccionar un evento.");
            return;
        }

        DefaultTableModel modelIzquierda =
                (DefaultTableModel) view.getTablaEmpresas().getModel();

        DefaultTableModel modelDerecha =
                (DefaultTableModel) view.getTablaSeleccionadas().getModel();

        int idEmpresa = (int) modelIzquierda.getValueAt(row, 0);
        String nombre = (String) modelIzquierda.getValueAt(row, 1);

        // Deshacer una retirada pendiente
        if (pendientesQuitar.containsKey(idEmpresa)) {
            pendientesQuitar.remove(idEmpresa);

            if (!existeEmpresaEnTabla(idEmpresa, modelDerecha)) {
                modelDerecha.addRow(new Object[] { idEmpresa, nombre });
            }

            modelIzquierda.removeRow(row);
            return;
        }

        if (!model.eventoTieneEmbargoVigente(idEvento)) {
            view.showError("Solo se puede conceder acceso especial si el embargo sigue vigente.");
            return;
        }

        if (!model.empresaAceptaEmbargo(idEmpresa)) {
            view.showError("La empresa seleccionada no acepta embargo.");
            return;
        }

        pendientesConceder.remove(idEmpresa);
        pendientesEspecial.put(idEmpresa, nombre);

        if (!existeEmpresaEnTabla(idEmpresa, modelDerecha)) {
            modelDerecha.addRow(new Object[] { idEmpresa, nombre });
        }

        modelIzquierda.removeRow(row);
    }

    private void moverDerechaAIzquierda() {
        int row = view.getTablaSeleccionadas().getSelectedRow();

        if (row == -1) {
            view.showError("Debe seleccionar una empresa de la tabla derecha.");
            return;
        }

        int idEvento = view.getIdEventoSeleccionado();
        if (idEvento == -1) {
            view.showError("Debe seleccionar un evento.");
            return;
        }

        DefaultTableModel modelIzquierda =
                (DefaultTableModel) view.getTablaEmpresas().getModel();

        DefaultTableModel modelDerecha =
                (DefaultTableModel) view.getTablaSeleccionadas().getModel();

        int idEmpresa = (int) modelDerecha.getValueAt(row, 0);
        String nombre = (String) modelDerecha.getValueAt(row, 1);

        if (pendientesConceder.containsKey(idEmpresa)) {
            pendientesConceder.remove(idEmpresa);

        } else if (pendientesEspecial.containsKey(idEmpresa)) {
            pendientesEspecial.remove(idEmpresa);

        } else {
            if (model.empresaHaDescargadoReportaje(idEvento, idEmpresa)) {
                view.showError("No se puede quitar el acceso porque la empresa ya ha descargado el reportaje.");
                return;
            }
            pendientesQuitar.put(idEmpresa, nombre);
        }

        if (!existeEmpresaEnTabla(idEmpresa, modelIzquierda)) {
            modelIzquierda.addRow(new Object[] { idEmpresa, nombre });
        }

        modelDerecha.removeRow(row);
    }

    private void guardarCambios() {
        int idEvento = view.getIdEventoSeleccionado();

        if (idEvento == -1) {
            view.showError("Debe seleccionar un evento.");
            return;
        }

        if (pendientesConceder.isEmpty() && pendientesEspecial.isEmpty() && pendientesQuitar.isEmpty()) {
            view.showError("No hay cambios que guardar.");
            return;
        }

        try {

            for (Integer idEmpresa : pendientesConceder.keySet()) {
                model.concederAcceso(idEvento, idEmpresa);
            }

            for (Integer idEmpresa : pendientesEspecial.keySet()) {
                model.concederAccesoEspecial(idEvento, idEmpresa);
            }

            for (Integer idEmpresa : pendientesQuitar.keySet()) {
                model.quitarAcceso(idEvento, idEmpresa);
            }

            limpiarPendientes();
            view.showInfo("Cambios guardados correctamente.");
            actualizarTablasSegunFiltro();

        } catch (ApplicationException ex) {

            view.showError(ex.getMessage());

            limpiarPendientes();
            actualizarTablasSegunFiltro();
        }
    }

    private void limpiarPendientes() {
        pendientesConceder.clear();
        pendientesEspecial.clear();
        pendientesQuitar.clear();
    }

    private boolean existeEmpresaEnTabla(int idEmpresa, DefaultTableModel modelTabla) {
        for (int i = 0; i < modelTabla.getRowCount(); i++) {
            int idExistente = (int) modelTabla.getValueAt(i, 0);
            if (idExistente == idEmpresa) {
                return true;
            }
        }
        return false;
    }

    private List<EmpresaComunicacionDTO> quitarEmpresasPorId(
            List<EmpresaComunicacionDTO> empresas,
            Set<Integer> idsAQuitar) {

        List<EmpresaComunicacionDTO> resultado = new ArrayList<>();

        for (EmpresaComunicacionDTO empresa : empresas) {
            if (!idsAQuitar.contains(empresa.getId())) {
                resultado.add(empresa);
            }
        }

        return resultado;
    }

    private List<EmpresaComunicacionDTO> anadirEmpresasDesdeMapa(
            List<EmpresaComunicacionDTO> base,
            Map<Integer, String> mapaEmpresas) {

        List<EmpresaComunicacionDTO> resultado = new ArrayList<>(base);
        Set<Integer> idsExistentes = new LinkedHashSet<>();

        for (EmpresaComunicacionDTO empresa : resultado) {
            idsExistentes.add(empresa.getId());
        }

        for (Map.Entry<Integer, String> entry : mapaEmpresas.entrySet()) {
            if (!idsExistentes.contains(entry.getKey())) {
                EmpresaComunicacionDTO dto = new EmpresaComunicacionDTO();
                dto.setId(entry.getKey());
                dto.setNombre(entry.getValue());
                resultado.add(dto);
            }
        }

        return resultado;
    }

    private List<EmpresaComunicacionDTO> construirListaDesdeMapa(Map<Integer, String> mapaEmpresas) {
        List<EmpresaComunicacionDTO> resultado = new ArrayList<>();

        for (Map.Entry<Integer, String> entry : mapaEmpresas.entrySet()) {
            EmpresaComunicacionDTO dto = new EmpresaComunicacionDTO();
            dto.setId(entry.getKey());
            dto.setNombre(entry.getValue());
            resultado.add(dto);
        }

        return resultado;
    }
}