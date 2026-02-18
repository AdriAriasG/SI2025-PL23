package app.model;

import java.util.ArrayList;
import java.util.List;
import app.dto.EventoDTO;
import app.dto.ReporteroDTO;
import app.util.Database;

/**
 * Modelo para la gestión de asignaciones de reporteros a eventos.
 * Cubre las HU #33537 y #33543.
 */
public class AsignacionModel {
    private Database db = new Database();

    /**
     * Obtiene los eventos de una agencia SIN reporteros asignados (HU #33537)
     */
    public List<EventoDTO> getEventosSinAsignar(int idAgencia) {
        String sql = "SELECT id, nombre, fecha, id_agencia FROM Evento " +
                     "WHERE id_agencia = ? AND id NOT IN (SELECT DISTINCT id_evento FROM Asignacion) " +
                     "ORDER BY fecha, nombre";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene los eventos de una agencia CON algún reportero asignado (HU #33543)
     */
    public List<EventoDTO> getEventosConAsignados(int idAgencia) {
        String sql = "SELECT DISTINCT e.id, e.nombre, e.fecha, e.id_agencia FROM Evento e " +
                     "JOIN Asignacion a ON e.id = a.id_evento " +
                     "WHERE e.id_agencia = ? " +
                     "ORDER BY e.fecha, e.nombre";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene TODOS los eventos de una agencia (para el filtro combinado)
     */
    public List<EventoDTO> getTodosEventos(int idAgencia) {
        String sql = "SELECT id, nombre, fecha, id_agencia FROM Evento " +
                     "WHERE id_agencia = ? ORDER BY fecha, nombre";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene los reporteros disponibles para asignar a un evento.
     * Un reportero está disponible si:
     * 1. Pertenece a la misma agencia que el evento
     * 2. No está asignado a otro evento en la misma fecha
     */
    public List<ReporteroDTO> getReporterosDisponibles(int idEvento, int idAgencia) {
        String sql = "SELECT r.id, r.nombre, r.id_agencia FROM Reportero r " +
                     "WHERE r.id_agencia = ? " +
                     "AND r.id NOT IN (" +
                     "    SELECT a.id_reportero FROM Asignacion a " +
                     "    JOIN Evento e ON a.id_evento = e.id " +
                     "    WHERE e.fecha = (SELECT fecha FROM Evento WHERE id = ?) " +
                     "    AND a.id_evento != ?" +
                     ") " +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idAgencia, idEvento, idEvento);
    }

    /**
     * Obtiene los reporteros ya asignados a un evento
     */
    public List<ReporteroDTO> getReporterosAsignados(int idEvento) {
        String sql = "SELECT r.id, r.nombre, r.id_agencia FROM Reportero r " +
                     "JOIN Asignacion a ON r.id = a.id_reportero " +
                     "WHERE a.id_evento = ? " +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idEvento);
    }

    /**
     * Verifica si un reportero ya está asignado a un evento
     */
    public boolean estaAsignado(int idEvento, int idReportero) {
        String sql = "SELECT COUNT(*) FROM Asignacion WHERE id_evento = ? AND id_reportero = ?";
        List<Object[]> result = db.executeQueryArray(sql, idEvento, idReportero);
        if (result.isEmpty() || result.get(0)[0] == null) {
            return false;
        }
        return ((Number) result.get(0)[0]).intValue() > 0;
    }

    /**
     * Asigna un reportero a un evento
     */
    public void asignarReportero(int idEvento, int idReportero) {
        // Verificar que no esté ya asignado
        if (!estaAsignado(idEvento, idReportero)) {
            String sql = "INSERT INTO Asignacion (id_evento, id_reportero) VALUES (?, ?)";
            db.executeUpdate(sql, idEvento, idReportero);
        }
    }

    /**
     * Elimina la asignación de un reportero a un evento
     */
    public void eliminarAsignacion(int idEvento, int idReportero) {
        String sql = "DELETE FROM Asignacion WHERE id_evento = ? AND id_reportero = ?";
        db.executeUpdate(sql, idEvento, idReportero);
    }

    /**
     * Obtiene el número de reporteros asignados a un evento
     */
    public int getNumeroAsignados(int idEvento) {
        String sql = "SELECT COUNT(*) FROM Asignacion WHERE id_evento = ?";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty() || result.get(0)[0] == null) {
            return 0;
        }
        return ((Number) result.get(0)[0]).intValue();
    }

    /**
     * Obtiene la fecha de un evento
     */
    public String getFechaEvento(int idEvento) {
        String sql = "SELECT fecha FROM Evento WHERE id = ?";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty()) {
            return null;
        }
        return (String) result.get(0)[0];
    }
}