package app.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import app.dto.AgenciaDTO;
import app.dto.EventoDTO;
import app.dto.ReporteroDTO;
import app.model.AsignacionModel;
import app.view.AsignacionView;

/**
 * Controlador para la asignación de reporteros a eventos.
 * Cubre las HU #33537 y #33543.
 */
public class AsignacionController {
    private AsignacionModel model;
    private AsignacionView view;
    private AgenciaDTO agencia;
    private int idEventoSeleccionado = -1;

    public AsignacionController(AsignacionModel model, AsignacionView view, AgenciaDTO agencia) {
        this.model = model;
        this.view = view;
        this.agencia = agencia;
        this.initView();
        this.initController();
    }

    /**
     * Inicializa la vista con los datos
     */
    public void initView() {
        cargarEventos();
        view.getFrame().setVisible(true);
    }

    /**
     * Inicializa los controladores de eventos
     */
    public void initController() {
        // Evento: selección de evento en la tabla
        view.getTablaEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                onEventoSeleccionado();
            }
        });

        // Evento: cambio de filtro (solo modo edición)
        if (view.isModoEdicion() && view.getCbFiltro() != null) {
            view.getCbFiltro().addActionListener(e -> onFiltroCambiado());
        }

        // Evento: eliminar asignación (solo modo edición)
        if (view.isModoEdicion() && view.getBtnEliminar() != null) {
            view.getBtnEliminar().addActionListener(e -> onEliminarAsignacion());
        }

        // Evento: cambio en los checkboxes de selección
        view.getTablaDisponibles().getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 0) { // Solo cuando cambia la columna de selección
                    onSeleccionCambiada();
                }
            }
        });

        // Evento: botón asignar
        view.getBtnAsignar().addActionListener(e -> onAsignar());

        // Evento: botón cancelar/cerrar
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());
    }

    /**
     * Carga los eventos según el modo y filtro
     */
    private void cargarEventos() {
        List<EventoDTO> eventos;
        
        if (view.isModoEdicion()) {
            // En modo edición, cargar según filtro
            int filtroIndex = view.getCbFiltro().getSelectedIndex();
            if (filtroIndex == 0) {
                eventos = model.getEventosSinAsignar(agencia.getId());
            } else {
                eventos = model.getEventosConAsignados(agencia.getId());
            }
        } else {
            // En modo normal, solo eventos sin asignar
            eventos = model.getEventosSinAsignar(agencia.getId());
        }

        view.setEventos(eventos);
        
        // Actualizar número de asignados para cada evento
        for (EventoDTO e : eventos) {
            int num = model.getNumeroAsignados(e.getId());
            view.actualizarAsignadosEnTabla(e.getId(), num);
        }

        // Limpiar selección y datos relacionados
        idEventoSeleccionado = -1;
        view.setDisponibles(new java.util.ArrayList<>());
        view.clearParaAsignar();
        if (view.isModoEdicion()) {
            view.setAsignados(new java.util.ArrayList<>());
        }
    }

    /**
     * Maneja la selección de un evento
     */
    private void onEventoSeleccionado() {
        int idEvento = view.getIdEventoSeleccionado();
        if (idEvento == -1) {
            return;
        }
        
        idEventoSeleccionado = idEvento;

        // Cargar reporteros disponibles
        List<ReporteroDTO> disponibles = model.getReporterosDisponibles(idEvento, agencia.getId());
        view.setDisponibles(disponibles);

        // En modo edición, cargar también los asignados
        if (view.isModoEdicion()) {
            List<ReporteroDTO> asignados = model.getReporterosAsignados(idEvento);
            view.setAsignados(asignados);
        }

        // Limpiar la lista de "a asignar"
        view.clearParaAsignar();
    }

    /**
     * Maneja el cambio de filtro (solo modo edición)
     */
    private void onFiltroCambiado() {
        cargarEventos();
    }

    /**
     * Maneja la eliminación de una asignación (solo modo edición)
     */
    private void onEliminarAsignacion() {
        int idReportero = view.getIdAsignadoSeleccionado();
        if (idReportero == -1) {
            view.showError("Debe seleccionar un reportero de la lista de asignados.");
            return;
        }

        if (idEventoSeleccionado == -1) {
            view.showError("No hay un evento seleccionado.");
            return;
        }

        // Confirmar eliminación
        if (view.showConfirm("¿Está seguro de que desea eliminar esta asignación?")) {
            model.eliminarAsignacion(idEventoSeleccionado, idReportero);
            view.showInfo("Asignación eliminada correctamente.");
            
            // Recargar datos
            onEventoSeleccionado();
            
            // Actualizar número de asignados en la tabla de eventos
            int num = model.getNumeroAsignados(idEventoSeleccionado);
            view.actualizarAsignadosEnTabla(idEventoSeleccionado, num);
        }
    }

    /**
     * Maneja el cambio en los checkboxes de selección de reporteros
     */
    private void onSeleccionCambiada() {
        // Actualizar la lista de "a asignar" basándose en los checkboxes
        view.clearParaAsignar();
        List<Integer> idsSeleccionados = view.getIdsSeleccionados();
        for (int id : idsSeleccionados) {
            String nombre = view.getNombreDisponible(id);
            if (nombre != null) {
                view.addParaAsignar(nombre);
            }
        }
    }

    /**
     * Maneja la acción de asignar reporteros
     */
    private void onAsignar() {
        if (idEventoSeleccionado == -1) {
            view.showError("Debe seleccionar un evento primero.");
            return;
        }

        List<Integer> idsSeleccionados = view.getIdsSeleccionados();
        if (idsSeleccionados.isEmpty()) {
            view.showError("Debe seleccionar al menos un reportero para asignar.");
            return;
        }

        // Confirmar asignación
        StringBuilder nombres = new StringBuilder();
        for (int id : idsSeleccionados) {
            String nombre = view.getNombreDisponible(id);
            if (nombres.length() > 0) nombres.append(", ");
            nombres.append(nombre);
        }

        if (view.showConfirm("¿Desea asignar los siguientes reporteros al evento?\n" + nombres.toString())) {
            // Realizar las asignaciones
            int exitosas = 0;
            for (int idReportero : idsSeleccionados) {
                try {
                    model.asignarReportero(idEventoSeleccionado, idReportero);
                    exitosas++;
                } catch (Exception e) {
                    // Ignorar errores individuales (ya asignado, etc.)
                }
            }

            if (exitosas > 0) {
                view.showInfo("Se asignaron " + exitosas + " reportero(s) correctamente.");
            }

            // Recargar datos
            onEventoSeleccionado();
            
            // Actualizar número de asignados en la tabla de eventos
            int num = model.getNumeroAsignados(idEventoSeleccionado);
            view.actualizarAsignadosEnTabla(idEventoSeleccionado, num);
        }
    }
}