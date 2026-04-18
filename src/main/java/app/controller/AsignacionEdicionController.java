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
 * Controlador para la asignación/modificación de reporteros a eventos.
 * Cubre HU #33537, #33543, #34426, #34430 y #34437 (freelances).
 */
public class AsignacionEdicionController {
    private AsignacionModel model;
    private AsignacionEdicionView view;
    private AgenciaDTO agencia;
    private int idEventoSeleccionado = -1;
    private boolean eventoFinalizado = false;
    private boolean modoFreelance = false;

    public AsignacionEdicionController(AsignacionModel model, AsignacionEdicionView view, AgenciaDTO agencia) {
        this.model = model;
        this.view = view;
        this.agencia = agencia;
        this.initView();
        this.initController();
    }

    public void initView() {
        cargarEventos();
        view.getFrame().setVisible(true);
    }

    public void initController() {
        view.getTablaEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                onEventoSeleccionado();
            }
        });

        view.getCbFiltro().addActionListener(e -> onFiltroCambiado());
        view.getBtnEliminar().addActionListener(e -> onEliminarReportero());
        view.getBtnDesignarRR().addActionListener(e -> onDesignarRR());
        view.getBtnFinalizar().addActionListener(e -> onFinalizar());
        view.getBtnAsignar().addActionListener(e -> onAsignar());
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());
        view.getChkFiltroTematica().addActionListener(e -> cargarReporterosDisponibles());
        view.getCbFiltroTipo().addActionListener(e -> cargarReporterosDisponibles());

        // Listeners para radio buttons de fuente (HU #34437)
        view.getRbAgencia().addActionListener(e -> {
            modoFreelance = false;
            cargarReporterosDisponibles();
        });
        view.getRbFreelance().addActionListener(e -> {
            modoFreelance = true;
            cargarReporterosDisponibles();
        });

        registrarListenerDisponibles();
    }

    private void registrarListenerDisponibles() {
        view.getTablaDisponibles().getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 0) {
                    onSeleccionCambiada();
                }
            }
        });
    }

    private void cargarEventos() {
        int filtroIndex = view.getCbFiltro().getSelectedIndex();
        List<EventoDTO> eventos;

        if (filtroIndex == 0) {
            eventos = model.getEventosSinAsignar(agencia.getId());
        } else {
            eventos = model.getEventosConAsignadosConEstado(agencia.getId());
        }

        view.setEventos(eventos);

        idEventoSeleccionado = -1;
        eventoFinalizado = false;
        view.setRangoFechasEvento(null, null);
        view.setDisponibles(new java.util.ArrayList<>());
        view.setAsignadosConRR(new java.util.ArrayList<>());
        view.setEdicionHabilitada(true);
        registrarListenerDisponibles();
    }

    private void onEventoSeleccionado() {
        int idEvento = view.getIdEventoSeleccionado();
        if (idEvento == -1) {
            view.getLblTematicasEvento().setText("Temáticas del evento: -");
            view.setRangoFechasEvento(null, null);
            return;
        }

        idEventoSeleccionado = idEvento;
        eventoFinalizado = model.isAsignacionFinalizada(idEvento);

        EventoDTO eventoDTO = model.getEventoById(idEvento);
        if (eventoDTO != null) {
            view.setRangoFechasEvento(eventoDTO.getFechaInicio(), eventoDTO.getFechaFin());
        }

        List<app.dto.TematicaDTO> tematicas = model.getTematicasEvento(idEvento);
        view.setTematicas(tematicas);

        List<Object[]> asignadosConRR = model.getReporterosAsignadosConRR(idEvento);
        view.setAsignadosConRR(asignadosConRR);

        view.setEdicionHabilitada(!eventoFinalizado);

        // Bloquear Finalizar si hay freelance DUDOSO asignado (HU #34437)
        if (!eventoFinalizado) {
            actualizarEstadoBotonFinalizar();
        }

        registrarListenerDisponibles();

        if (!eventoFinalizado) {
            cargarReporterosDisponibles();
        } else {
            view.setDisponibles(new java.util.ArrayList<>());
        }
    }

    private void cargarReporterosDisponibles() {
        if (idEventoSeleccionado == -1 || eventoFinalizado) return;

        boolean filtrarTematica = view.getChkFiltroTematica().isSelected();
        String tipo = view.getSelectedTipo();
        List<ReporteroDTO> disponibles;

        if (modoFreelance) {
            if (filtrarTematica && tipo != null) {
                disponibles = model.getFreelancesDisponiblesPorTematicaYTipo(idEventoSeleccionado, tipo);
            } else if (filtrarTematica) {
                disponibles = model.getFreelancesDisponiblesPorTematica(idEventoSeleccionado);
            } else if (tipo != null) {
                disponibles = model.getFreelancesDisponiblesPorTipo(idEventoSeleccionado, tipo);
            } else {
                disponibles = model.getFreelancesDisponibles(idEventoSeleccionado);
            }
        } else {
            if (filtrarTematica && tipo != null) {
                disponibles = model.getReporterosDisponiblesPorTematicaYTipo(
                        idEventoSeleccionado, agencia.getId(), tipo);
            } else if (filtrarTematica) {
                disponibles = model.getReporterosDisponiblesPorTematica(
                        idEventoSeleccionado, agencia.getId());
            } else if (tipo != null) {
                disponibles = model.getReporterosDisponiblesPorTipo(
                        idEventoSeleccionado, agencia.getId(), tipo);
            } else {
                disponibles = model.getReporterosDisponibles(
                        idEventoSeleccionado, agencia.getId());
            }
        }

        view.setDisponibles(disponibles);
    }

    private void onFiltroCambiado() {
        cargarEventos();
    }

    private void onEliminarReportero() {
        if (eventoFinalizado) {
            view.showError("La asignación está finalizada. No se pueden realizar cambios.");
            return;
        }

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
            view.removePendiente(idReportero);
            view.setCheckboxDisponible(idReportero, false);
            view.showInfo("Reportero eliminado de la lista de pendientes.");
        } else if ("Asignado".equals(estado)) {
            if (view.showConfirm("¿Está seguro de que desea eliminar esta asignación?\nEl cambio será inmediato.")) {
                model.eliminarAsignacion(idEventoSeleccionado, idReportero);
                view.showInfo("Asignación eliminada correctamente.");
                recargarDatosEvento();
            }
        }
    }

    private void onDesignarRR() {
        if (eventoFinalizado) {
            view.showError("La asignación está finalizada. No se puede cambiar el RR.");
            return;
        }

        int idReportero = view.getIdReporteroEventoSeleccionado();
        if (idReportero == -1) {
            view.showError("Debe seleccionar un reportero de la lista del evento.");
            return;
        }

        String estado = view.getEstadoReporteroEventoSeleccionado();
        if (!"Asignado".equals(estado)) {
            view.showError("Solo se puede designar como RR a un reportero ya asignado.\nPrimero asigne el reportero al evento.");
            return;
        }

        model.setReporteroResponsable(idEventoSeleccionado, idReportero);
        view.showInfo("Reportero designado como Responsable (RR) correctamente.");
        recargarDatosEvento();
    }

    /**
     * Finaliza la asignación del evento seleccionado.
     * Validaciones:
     * - Debe haber un RR designado.
     * - Debe haber al menos un reportero BASE (el RR puede ser BASE y satisfacer el requisito solo).
     * - No puede haber ningún freelance asignado con decisión DUDOSO (HU #34437).
     */
    private void onFinalizar() {
        if (eventoFinalizado) {
            view.showError("La asignación ya está finalizada.");
            return;
        }
        if (idEventoSeleccionado == -1) {
            view.showError("Debe seleccionar un evento primero.");
            return;
        }

        int idRR = model.getReporteroResponsable(idEventoSeleccionado);
        if (idRR == -1) {
            view.showError("No se puede finalizar: debe designar un Reportero Responsable (RR).");
            return;
        }

        boolean rrEsBase = model.esReporteroResponsableTipoBase(idEventoSeleccionado);
        if (!rrEsBase && !model.tieneReporteroBaseNoRR(idEventoSeleccionado)) {
            view.showError("No se puede finalizar: debe haber al menos un reportero de tipo BASE asignado que no sea el RR.");
            return;
        }

        if (model.tieneFreelanceDudosoAsignado(idEventoSeleccionado)) {
            view.showError("No se puede finalizar: hay un reportero freelance asignado con estado 'En duda'.\nEspere a que confirme su disponibilidad.");
            return;
        }

        if (view.showConfirm("¿Está seguro de que desea finalizar la asignación?\n" +
                "Una vez finalizada, no se podrán añadir ni eliminar reporteros\n" +
                "ni cambiar el Reportero Responsable.")) {
            model.finalizarAsignacion(idEventoSeleccionado);
            eventoFinalizado = true;
            view.showInfo("Asignación finalizada correctamente.");
            recargarDatosEvento();
        }
    }

    private void onSeleccionCambiada() {
        if (eventoFinalizado) return;

        List<Integer> idsSeleccionados = view.getIdsSeleccionadosDisponibles();
        List<Integer> pendientesActuales = view.getIdsPendientes();

        for (int id : idsSeleccionados) {
            if (!pendientesActuales.contains(id)) {
                String nombre = view.getNombreDisponible(id);
                String tipo = view.getTipoDisponible(id);
                if (nombre != null) {
                    view.addPendiente(id, nombre, tipo, modoFreelance);
                }
            }
        }

        for (int id : pendientesActuales) {
            if (!idsSeleccionados.contains(id)) {
                view.removePendiente(id);
            }
        }
    }

    private void onAsignar() {
        if (eventoFinalizado) {
            view.showError("La asignación está finalizada. No se pueden realizar cambios.");
            return;
        }
        if (idEventoSeleccionado == -1) {
            view.showError("Debe seleccionar un evento primero.");
            return;
        }

        List<Integer> idsPendientes = view.getIdsPendientes();
        if (idsPendientes.isEmpty()) {
            view.showError("No hay reporteros pendientes de asignar.\nMarque los reporteros que desee añadir en la tabla de disponibles.");
            return;
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¿Desea asignar los siguientes reporteros al evento?\n\n");
        for (int id : idsPendientes) {
            String nombre = view.getNombreDisponible(id);
            mensaje.append("• ").append(nombre).append("\n");
        }

        if (view.showConfirm(mensaje.toString())) {
            int exitosas = 0;
            for (int idReportero : idsPendientes) {
                try {
                    model.asignarReportero(idEventoSeleccionado, idReportero);
                    exitosas++;

                    // Enviar email de notificación al freelance (HU #34437)
                    if (modoFreelance) {
                        enviarEmailFreelance(idReportero);
                    }
                } catch (Exception e) {
                    // Ignorar errores individuales de asignación
                }
            }

            if (exitosas > 0) {
                view.showInfo("Se asignaron " + exitosas + " reportero(s) correctamente.");
            }

            recargarDatosEvento();
            actualizarEstadoBotonFinalizar();
        }
    }

    private void enviarEmailFreelance(int idReportero) {
        String emailDestino = model.getEmailFreelance(idReportero);
        String destino = (emailDestino != null && !emailDestino.isBlank()) ? emailDestino : "(sin email registrado)";
        System.out.println("[EMAIL] Notificación enviada a freelance id=" + idReportero + " → " + destino);
        view.showInfo("Se ha enviado un email de notificación al reportero freelance:\n" + destino);
    }

    /**
     * Actualiza el estado del botón Finalizar según si hay freelances DUDOSO asignados.
     */
    private void actualizarEstadoBotonFinalizar() {
        if (idEventoSeleccionado == -1 || eventoFinalizado) return;
        boolean hayDudoso = model.tieneFreelanceDudosoAsignado(idEventoSeleccionado);
        view.setFinalizarHabilitado(!hayDudoso);
    }

    private void recargarDatosEvento() {
        if (idEventoSeleccionado == -1) return;

        int filtroIndex = view.getCbFiltro().getSelectedIndex();
        List<EventoDTO> eventos;
        if (filtroIndex == 0) {
            eventos = model.getEventosSinAsignar(agencia.getId());
        } else {
            eventos = model.getEventosConAsignadosConEstado(agencia.getId());
        }
        view.setEventos(eventos);

        boolean eventoEncontrado = false;
        for (EventoDTO e : eventos) {
            if (e.getId() == idEventoSeleccionado) { eventoEncontrado = true; break; }
        }

        if (eventoEncontrado) {
            for (int i = 0; i < view.getTablaEventos().getRowCount(); i++) {
                if (((Number) view.getTablaEventos().getValueAt(i, 0)).intValue() == idEventoSeleccionado) {
                    view.getTablaEventos().setRowSelectionInterval(i, i);
                    break;
                }
            }

            eventoFinalizado = model.isAsignacionFinalizada(idEventoSeleccionado);

            List<Object[]> asignadosConRR = model.getReporterosAsignadosConRR(idEventoSeleccionado);
            view.setAsignadosConRR(asignadosConRR);

            view.setEdicionHabilitada(!eventoFinalizado);
            registrarListenerDisponibles();

            if (!eventoFinalizado) {
                actualizarEstadoBotonFinalizar();
                cargarReporterosDisponibles();
            } else {
                view.setDisponibles(new java.util.ArrayList<>());
            }
        } else {
            idEventoSeleccionado = -1;
            eventoFinalizado = false;
            view.setAsignadosConRR(new java.util.ArrayList<>());
            view.setDisponibles(new java.util.ArrayList<>());
            view.setEdicionHabilitada(true);
            registrarListenerDisponibles();
        }
    }
}
