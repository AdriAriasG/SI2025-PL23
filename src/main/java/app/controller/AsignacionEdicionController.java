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
import app.view.AsignacionEdicionView;

/**
 * Controlador para la modificación de asignación de reporteros a eventos.
 * Cubre la HU #33543 (modo edición).
 * 
 * En este modo, los reporteros seleccionados se añaden visualmente a la tabla
 * del evento como "pendientes", y solo se guardan en BD cuando el usuario
 * pulsa "Asignar".
 */
public class AsignacionEdicionController {
    private AsignacionModel model;
    private AsignacionEdicionView view;
    private AgenciaDTO agencia;
    private int idEventoSeleccionado = -1;

    public AsignacionEdicionController(AsignacionModel model, AsignacionEdicionView view, AgenciaDTO agencia) {
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

        // Evento: cambio de filtro
        view.getCbFiltro().addActionListener(e -> onFiltroCambiado());

        // Evento: eliminar reportero (asignado o pendiente)
        view.getBtnEliminar().addActionListener(e -> onEliminarReportero());

        // Evento: cambio en los checkboxes de selección de disponibles
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

        // Evento: botón cerrar
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());
    }

    /**
     * Carga los eventos según el filtro
     */
    private void cargarEventos() {
        int filtroIndex = view.getCbFiltro().getSelectedIndex();
        List<EventoDTO> eventos;
        
        if (filtroIndex == 0) {
            eventos = model.getEventosSinAsignar(agencia.getId());
        } else {
            eventos = model.getEventosConAsignados(agencia.getId());
        }

        view.setEventos(eventos);

        // Limpiar selección y datos relacionados
        idEventoSeleccionado = -1;
        view.setDisponibles(new java.util.ArrayList<>());
        view.setAsignados(new java.util.ArrayList<>());
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

        // Cargar reporteros ya asignados al evento
        List<ReporteroDTO> asignados = model.getReporterosAsignados(idEvento);
        view.setAsignados(asignados);

        // Cargar reporteros disponibles (excluyendo los ya asignados)
        List<ReporteroDTO> disponibles = model.getReporterosDisponibles(idEvento, agencia.getId());
        view.setDisponibles(disponibles);
    }

    /**
     * Maneja el cambio de filtro
     */
    private void onFiltroCambiado() {
        cargarEventos();
    }

    /**
     * Maneja la eliminación de un reportero (asignado o pendiente)
     */
    private void onEliminarReportero() {
        int idReportero = view.getIdReporteroEventoSeleccionado();
        if (idReportero == -1) {
            view.showError("Debe seleccionar un reportero de la lista del evento.");
            return;
        }

        if (idEventoSeleccionado == -1) {
            view.showError("No hay un evento seleccionado.");
            return;
        }

        String estado = view.getEstadoReporteroEventoSeleccionado();
        
        if ("Pendiente".equals(estado)) {
            // Es un pendiente, solo quitarlo de la lista visual
            view.removePendiente(idReportero);
            view.setCheckboxDisponible(idReportero, false);
            view.showInfo("Reportero eliminado de la lista de pendientes.");
        } else if ("Asignado".equals(estado)) {
            // Es un asignado real, eliminar de la base de datos
            if (view.showConfirm("¿Está seguro de que desea eliminar esta asignación?\nEl cambio será inmediato.")) {
                model.eliminarAsignacion(idEventoSeleccionado, idReportero);
                view.showInfo("Asignación eliminada correctamente.");
                
                // Recargar datos del evento
                recargarDatosEvento();
            }
        }
    }

    /**
     * Maneja el cambio en los checkboxes de selección de reporteros disponibles.
     * Cuando se marca un reportero, se añade como pendiente a la tabla del evento.
     * Cuando se desmarca, se quita de los pendientes.
     */
    private void onSeleccionCambiada() {
        List<Integer> idsSeleccionados = view.getIdsSeleccionadosDisponibles();
        
        // Obtener los IDs que estaban marcados antes (los pendientes actuales)
        List<Integer> pendientesActuales = view.getIdsPendientes();
        
        // Añadir los nuevos seleccionados que no estaban pendientes
        for (int id : idsSeleccionados) {
            if (!pendientesActuales.contains(id)) {
                String nombre = view.getNombreDisponible(id);
                if (nombre != null) {
                    view.addPendiente(id, nombre);
                }
            }
        }
        
        // Quitar los que se desmarcaron
        for (int id : pendientesActuales) {
            if (!idsSeleccionados.contains(id)) {
                view.removePendiente(id);
            }
        }
    }

    /**
     * Maneja la acción de asignar reporteros pendientes
     */
    private void onAsignar() {
        if (idEventoSeleccionado == -1) {
            view.showError("Debe seleccionar un evento primero.");
            return;
        }

        List<Integer> idsPendientes = view.getIdsPendientes();
        if (idsPendientes.isEmpty()) {
            view.showError("No hay reporteros pendientes de asignar.\nMarque los reporteros que desee añadir en la tabla de disponibles.");
            return;
        }

        // Confirmar asignación
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¿Desea asignar los siguientes reporteros al evento?\n\n");
        for (int id : idsPendientes) {
            String nombre = view.getNombreDisponible(id);
            mensaje.append("• ").append(nombre).append("\n");
        }

        if (view.showConfirm(mensaje.toString())) {
            // Realizar las asignaciones
            int exitosas = 0;
            for (int idReportero : idsPendientes) {
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

            // Recargar datos del evento para reflejar los cambios
            recargarDatosEvento();
        }
    }

    /**
     * Recarga los datos del evento actualmente seleccionado.
     */
    private void recargarDatosEvento() {
        if (idEventoSeleccionado == -1) return;
        
        // Recargar la lista de eventos por si cambió el filtro
        int filtroIndex = view.getCbFiltro().getSelectedIndex();
        List<EventoDTO> eventos;
        if (filtroIndex == 0) {
            eventos = model.getEventosSinAsignar(agencia.getId());
        } else {
            eventos = model.getEventosConAsignados(agencia.getId());
        }
        view.setEventos(eventos);
        
        // Verificar si el evento sigue en la lista
        boolean eventoEncontrado = false;
        for (EventoDTO e : eventos) {
            if (e.getId() == idEventoSeleccionado) {
                eventoEncontrado = true;
                break;
            }
        }
        
        if (eventoEncontrado) {
            // Reseleccionar el evento en la tabla
            for (int i = 0; i < view.getTablaEventos().getRowCount(); i++) {
                if ((int) view.getTablaEventos().getValueAt(i, 0) == idEventoSeleccionado) {
                    view.getTablaEventos().setRowSelectionInterval(i, i);
                    break;
                }
            }
            
            // Recargar reporteros
            List<ReporteroDTO> asignados = model.getReporterosAsignados(idEventoSeleccionado);
            view.setAsignados(asignados);
            
            List<ReporteroDTO> disponibles = model.getReporterosDisponibles(idEventoSeleccionado, agencia.getId());
            view.setDisponibles(disponibles);
        } else {
            // El evento ya no está en la lista
            idEventoSeleccionado = -1;
            view.setAsignados(new java.util.ArrayList<>());
            view.setDisponibles(new java.util.ArrayList<>());
        }
    }
}