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
        String sql = "SELECT id, nombre, fecha, fecha_inicio AS fechaInicio, fecha_fin AS fechaFin, id_agencia AS idAgencia FROM Evento " +
                     "WHERE id_agencia = ? AND id NOT IN (SELECT DISTINCT id_evento FROM Asignacion) " +
                     "ORDER BY fecha_inicio, nombre";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene los eventos de una agencia CON algún reportero asignado (HU #33543)
     */
    public List<EventoDTO> getEventosConAsignados(int idAgencia) {
        String sql = "SELECT DISTINCT e.id, e.nombre, e.fecha, e.fecha_inicio AS fechaInicio, e.fecha_fin AS fechaFin, e.id_agencia AS idAgencia FROM Evento e " +
                     "JOIN Asignacion a ON e.id = a.id_evento " +
                     "WHERE e.id_agencia = ? " +
                     "ORDER BY e.fecha_inicio, e.nombre";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene TODOS los eventos de una agencia (para el filtro combinado)
     */
    public List<EventoDTO> getTodosEventos(int idAgencia) {
        String sql = "SELECT id, nombre, fecha, fecha_inicio AS fechaInicio, fecha_fin AS fechaFin, id_agencia AS idAgencia FROM Evento " +
                     "WHERE id_agencia = ? ORDER BY fecha_inicio, nombre";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene los reporteros disponibles para asignar a un evento.
     * Un reportero está disponible si:
     * 1. Pertenece a la misma agencia que el evento
     * 2. No está asignado a otro evento cuyo rango de fechas se solape con el de este evento
     * 3. No está ya asignado a este evento
     * Dos eventos se solapan si inicio_A <= fin_B AND fin_A >= inicio_B (HU #34430)
     */
    public List<ReporteroDTO> getReporterosDisponibles(int idEvento, int idAgencia) {
        String sql = "SELECT r.id, r.nombre, r.tipo, r.id_agencia FROM Reportero r " +
                     "WHERE r.id_agencia = ? " +
                     "AND r.id NOT IN (" +
                     "    SELECT a.id_reportero FROM Asignacion a " +
                     "    JOIN Evento e ON a.id_evento = e.id " +
                     "    WHERE e.fecha_inicio <= (SELECT fecha_fin FROM Evento WHERE id = ?) " +
                     "    AND e.fecha_fin >= (SELECT fecha_inicio FROM Evento WHERE id = ?) " +
                     "    AND a.id_evento != ?" +
                     ") " +
                     "AND r.id NOT IN (" +
                     "    SELECT a2.id_reportero FROM Asignacion a2 " +
                     "    WHERE a2.id_evento = ?" +
                     ") " +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idAgencia, idEvento, idEvento, idEvento, idEvento);
    }

    /**
     * Obtiene los reporteros disponibles filtrados por temática del evento.
     * Disponibilidad por rango de fechas (HU #34430).
     */
    public List<ReporteroDTO> getReporterosDisponiblesPorTematica(int idEvento, int idAgencia) {
        String sql = "SELECT DISTINCT r.id, r.nombre, r.tipo, r.id_agencia " +
                     "FROM Reportero r " +
                     "JOIN ReporteroTematica rt ON r.id = rt.id_reportero " +
                     "JOIN EventoTematica et ON rt.id_tematica = et.id_tematica " +
                     "WHERE r.id_agencia = ? " +
                     "  AND et.id_evento = ? " +
                     "  AND r.id NOT IN (" +
                     "      SELECT a.id_reportero FROM Asignacion a " +
                     "      JOIN Evento e ON a.id_evento = e.id " +
                     "      WHERE e.fecha_inicio <= (SELECT fecha_fin FROM Evento WHERE id = ?) " +
                     "      AND e.fecha_fin >= (SELECT fecha_inicio FROM Evento WHERE id = ?) " +
                     "      AND a.id_evento != ?" +
                     "  ) " +
                     "  AND r.id NOT IN (" +
                     "      SELECT a2.id_reportero FROM Asignacion a2 " +
                     "      WHERE a2.id_evento = ?" +
                     "  ) " +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idAgencia, idEvento, idEvento, idEvento, idEvento, idEvento);
    }

    /**
     * Obtiene los reporteros disponibles filtrados por tipo.
     * Disponibilidad por rango de fechas (HU #34430).
     */
    public List<ReporteroDTO> getReporterosDisponiblesPorTipo(int idEvento, int idAgencia, String tipo) {
        String sql = "SELECT r.id, r.nombre, r.tipo, r.id_agencia FROM Reportero r " +
                     "WHERE r.id_agencia = ? " +
                     "AND r.tipo = ? " +
                     "AND r.id NOT IN (" +
                     "    SELECT a.id_reportero FROM Asignacion a " +
                     "    JOIN Evento e ON a.id_evento = e.id " +
                     "    WHERE e.fecha_inicio <= (SELECT fecha_fin FROM Evento WHERE id = ?) " +
                     "    AND e.fecha_fin >= (SELECT fecha_inicio FROM Evento WHERE id = ?) " +
                     "    AND a.id_evento != ?" +
                     ") " +
                     "AND r.id NOT IN (" +
                     "    SELECT a2.id_reportero FROM Asignacion a2 " +
                     "    WHERE a2.id_evento = ?" +
                     ") " +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idAgencia, tipo, idEvento, idEvento, idEvento, idEvento);
    }

    /**
     * Obtiene los reporteros disponibles filtrados por temática y tipo simultáneamente.
     * Disponibilidad por rango de fechas (HU #34430).
     */
    public List<ReporteroDTO> getReporterosDisponiblesPorTematicaYTipo(int idEvento, int idAgencia, String tipo) {
        String sql = "SELECT DISTINCT r.id, r.nombre, r.tipo, r.id_agencia " +
                     "FROM Reportero r " +
                     "JOIN ReporteroTematica rt ON r.id = rt.id_reportero " +
                     "JOIN EventoTematica et ON rt.id_tematica = et.id_tematica " +
                     "WHERE r.id_agencia = ? " +
                     "  AND et.id_evento = ? " +
                     "  AND r.tipo = ? " +
                     "  AND r.id NOT IN (" +
                     "      SELECT a.id_reportero FROM Asignacion a " +
                     "      JOIN Evento e ON a.id_evento = e.id " +
                     "      WHERE e.fecha_inicio <= (SELECT fecha_fin FROM Evento WHERE id = ?) " +
                     "      AND e.fecha_fin >= (SELECT fecha_inicio FROM Evento WHERE id = ?) " +
                     "      AND a.id_evento != ?" +
                     "  ) " +
                     "  AND r.id NOT IN (" +
                     "      SELECT a2.id_reportero FROM Asignacion a2 " +
                     "      WHERE a2.id_evento = ?" +
                     "  ) " +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idAgencia, idEvento, tipo, idEvento, idEvento, idEvento, idEvento);
    }

    /**
     * Obtiene los reporteros ya asignados a un evento
     */
    public List<ReporteroDTO> getReporterosAsignados(int idEvento) {
        String sql = "SELECT r.id, r.nombre, r.tipo, r.id_agencia FROM Reportero r " +
                     "JOIN Asignacion a ON r.id = a.id_reportero " +
                     "WHERE a.id_evento = ? " +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idEvento);
    }

    /**
     * Obtiene las temáticas de un evento
     */
    public List<app.dto.TematicaDTO> getTematicasEvento(int idEvento) {
        String sql = "SELECT t.id, t.nombre FROM Tematica t " +
                     "JOIN EventoTematica et ON t.id = et.id_tematica " +
                     "WHERE et.id_evento = ? " +
                     "ORDER BY t.nombre";
        return db.executeQueryPojo(app.dto.TematicaDTO.class, sql, idEvento);
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

    // ===== HU #34426: Reportero Responsable y Finalización =====

    /**
     * Designa a un reportero como Reportero Responsable (RR) de un evento.
     * Primero quita el RR actual (si hay) y luego marca al nuevo.
     */
    public void setReporteroResponsable(int idEvento, int idReportero) {
        String sqlQuitar = "UPDATE Asignacion SET es_responsable = FALSE WHERE id_evento = ?";
        db.executeUpdate(sqlQuitar, idEvento);
        String sqlPoner = "UPDATE Asignacion SET es_responsable = TRUE WHERE id_evento = ? AND id_reportero = ?";
        db.executeUpdate(sqlPoner, idEvento, idReportero);
    }

    /**
     * Quita la designación de RR de un evento (ningún reportero es RR).
     */
    public void quitarReporteroResponsable(int idEvento) {
        String sql = "UPDATE Asignacion SET es_responsable = FALSE WHERE id_evento = ?";
        db.executeUpdate(sql, idEvento);
    }

    /**
     * Obtiene el ID del Reportero Responsable de un evento, o -1 si no hay.
     */
    public int getReporteroResponsable(int idEvento) {
        String sql = "SELECT id_reportero FROM Asignacion WHERE id_evento = ? AND es_responsable = TRUE";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty() || result.get(0)[0] == null) {
            return -1;
        }
        return ((Number) result.get(0)[0]).intValue();
    }

    /**
     * Comprueba si un evento tiene al menos un reportero de tipo BASE asignado.
     */
    public boolean tieneReporteroBase(int idEvento) {
        String sql = "SELECT COUNT(*) FROM Asignacion a " +
                     "JOIN Reportero r ON a.id_reportero = r.id " +
                     "WHERE a.id_evento = ? AND r.tipo = 'BASE'";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty() || result.get(0)[0] == null) {
            return false;
        }
        return ((Number) result.get(0)[0]).intValue() > 0;
    }

    /**
     * Comprueba si un evento tiene al menos un reportero de tipo BASE asignado
     * que NO sea el Reportero Responsable (RR).
     */
    public boolean tieneReporteroBaseNoRR(int idEvento) {
        String sql = "SELECT COUNT(*) FROM Asignacion a " +
                     "JOIN Reportero r ON a.id_reportero = r.id " +
                     "WHERE a.id_evento = ? AND r.tipo = 'BASE' AND a.es_responsable = FALSE";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty() || result.get(0)[0] == null) {
            return false;
        }
        return ((Number) result.get(0)[0]).intValue() > 0;
    }

    /**
     * Comprueba si el Reportero Responsable (RR) de un evento es de tipo BASE.
     * Si lo es, ya satisface por sí solo el requisito de BASE para poder finalizar.
     */
    public boolean esReporteroResponsableTipoBase(int idEvento) {
        String sql = "SELECT COUNT(*) FROM Asignacion a " +
                     "JOIN Reportero r ON a.id_reportero = r.id " +
                     "WHERE a.id_evento = ? AND a.es_responsable = TRUE AND r.tipo = 'BASE'";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty() || result.get(0)[0] == null) {
            return false;
        }
        return ((Number) result.get(0)[0]).intValue() > 0;
    }

    /**
     * Comprueba si la asignación de un evento está finalizada.
     */
    public boolean isAsignacionFinalizada(int idEvento) {
        String sql = "SELECT asignacion_finalizada FROM Evento WHERE id = ?";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty() || result.get(0)[0] == null) {
            return false;
        }
        Object val = result.get(0)[0];
        if (val instanceof Boolean) return (Boolean) val;
        return ((Number) val).intValue() != 0;
    }

    /**
     * Finaliza la asignación de un evento (bloquea cambios).
     */
    public void finalizarAsignacion(int idEvento) {
        String sql = "UPDATE Evento SET asignacion_finalizada = TRUE WHERE id = ?";
        db.executeUpdate(sql, idEvento);
    }

    /**
     * Obtiene los reporteros asignados a un evento, incluyendo si son RR.
     * Devuelve objetos con: id, nombre, tipo, id_agencia, es_responsable.
     */
    public List<Object[]> getReporterosAsignadosConRR(int idEvento) {
        String sql = "SELECT r.id, r.nombre, r.tipo, r.id_agencia, a.es_responsable " +
                     "FROM Reportero r " +
                     "JOIN Asignacion a ON r.id = a.id_reportero " +
                     "WHERE a.id_evento = ? " +
                     "ORDER BY a.es_responsable DESC, r.nombre";
        return db.executeQueryArray(sql, idEvento);
    }

    /**
     * Obtiene todos los eventos de una agencia incluyendo el campo asignacion_finalizada.
     */
    public List<EventoDTO> getTodosEventosConEstado(int idAgencia) {
        String sql = "SELECT id, nombre, fecha, fecha_inicio AS fechaInicio, fecha_fin AS fechaFin, id_agencia AS idAgencia, asignacion_finalizada AS asignacionFinalizada FROM Evento " +
                     "WHERE id_agencia = ? ORDER BY fecha_inicio, nombre";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene eventos con asignados incluyendo el campo asignacion_finalizada.
     */
    public List<EventoDTO> getEventosConAsignadosConEstado(int idAgencia) {
        String sql = "SELECT DISTINCT e.id, e.nombre, e.fecha, e.fecha_inicio AS fechaInicio, e.fecha_fin AS fechaFin, e.id_agencia AS idAgencia, e.asignacion_finalizada AS asignacionFinalizada " +
                     "FROM Evento e " +
                     "JOIN Asignacion a ON e.id = a.id_evento " +
                     "WHERE e.id_agencia = ? " +
                     "ORDER BY e.fecha_inicio, e.nombre";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene un evento por su ID (HU #34430).
     */
    public EventoDTO getEventoById(int idEvento) {
        String sql = "SELECT id, nombre, fecha, fecha_inicio AS fechaInicio, fecha_fin AS fechaFin, id_agencia AS idAgencia, asignacion_finalizada AS asignacionFinalizada FROM Evento WHERE id = ?";
        List<EventoDTO> result = db.executeQueryPojo(EventoDTO.class, sql, idEvento);
        return result.isEmpty() ? null : result.get(0);
    }

    // ===== HU #34437: Freelances en asignación =====

    private static final String DISPONIBILIDAD_FECHA =
        "AND r.id NOT IN (" +
        "    SELECT a.id_reportero FROM Asignacion a " +
        "    JOIN Evento e ON a.id_evento = e.id " +
        "    WHERE e.fecha_inicio <= (SELECT fecha_fin FROM Evento WHERE id = ?) " +
        "    AND e.fecha_fin >= (SELECT fecha_inicio FROM Evento WHERE id = ?) " +
        "    AND a.id_evento != ?" +
        ") " +
        "AND r.id NOT IN (" +
        "    SELECT a2.id_reportero FROM Asignacion a2 WHERE a2.id_evento = ?" +
        ") ";

    /**
     * Freelances con decisión INTERESADO o DUDOSO para el evento, disponibles por rango de fechas.
     */
    public List<ReporteroDTO> getFreelancesDisponibles(int idEvento) {
        String sql = "SELECT r.id, r.nombre, r.tipo, r.id_agencia AS idAgencia, r.email FROM Reportero r " +
                     "JOIN DecisionFreelance df ON r.id = df.id_reportero " +
                     "WHERE r.id_agencia IS NULL " +
                     "AND df.id_evento = ? " +
                     "AND df.decision IN ('INTERESADO', 'DUDOSO') " +
                     DISPONIBILIDAD_FECHA +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idEvento, idEvento, idEvento, idEvento, idEvento);
    }

    /**
     * Freelances disponibles filtrados por temática del evento.
     */
    public List<ReporteroDTO> getFreelancesDisponiblesPorTematica(int idEvento) {
        String sql = "SELECT DISTINCT r.id, r.nombre, r.tipo, r.id_agencia AS idAgencia, r.email " +
                     "FROM Reportero r " +
                     "JOIN DecisionFreelance df ON r.id = df.id_reportero " +
                     "JOIN ReporteroTematica rt ON r.id = rt.id_reportero " +
                     "JOIN EventoTematica et ON rt.id_tematica = et.id_tematica " +
                     "WHERE r.id_agencia IS NULL " +
                     "AND df.id_evento = ? " +
                     "AND et.id_evento = ? " +
                     "AND df.decision IN ('INTERESADO', 'DUDOSO') " +
                     DISPONIBILIDAD_FECHA +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idEvento, idEvento, idEvento, idEvento, idEvento, idEvento);
    }

    /**
     * Freelances disponibles filtrados por tipo.
     */
    public List<ReporteroDTO> getFreelancesDisponiblesPorTipo(int idEvento, String tipo) {
        String sql = "SELECT r.id, r.nombre, r.tipo, r.id_agencia AS idAgencia, r.email FROM Reportero r " +
                     "JOIN DecisionFreelance df ON r.id = df.id_reportero " +
                     "WHERE r.id_agencia IS NULL " +
                     "AND df.id_evento = ? " +
                     "AND r.tipo = ? " +
                     "AND df.decision IN ('INTERESADO', 'DUDOSO') " +
                     DISPONIBILIDAD_FECHA +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idEvento, tipo, idEvento, idEvento, idEvento, idEvento);
    }

    /**
     * Freelances disponibles filtrados por temática y tipo.
     */
    public List<ReporteroDTO> getFreelancesDisponiblesPorTematicaYTipo(int idEvento, String tipo) {
        String sql = "SELECT DISTINCT r.id, r.nombre, r.tipo, r.id_agencia AS idAgencia, r.email " +
                     "FROM Reportero r " +
                     "JOIN DecisionFreelance df ON r.id = df.id_reportero " +
                     "JOIN ReporteroTematica rt ON r.id = rt.id_reportero " +
                     "JOIN EventoTematica et ON rt.id_tematica = et.id_tematica " +
                     "WHERE r.id_agencia IS NULL " +
                     "AND df.id_evento = ? " +
                     "AND et.id_evento = ? " +
                     "AND r.tipo = ? " +
                     "AND df.decision IN ('INTERESADO', 'DUDOSO') " +
                     DISPONIBILIDAD_FECHA +
                     "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idEvento, idEvento, tipo, idEvento, idEvento, idEvento, idEvento);
    }

    /**
     * Obtiene el email de un reportero freelance para enviar notificación.
     */
    public String getEmailFreelance(int idReportero) {
        String sql = "SELECT email FROM Reportero WHERE id = ? AND id_agencia IS NULL";
        List<Object[]> result = db.executeQueryArray(sql, idReportero);
        if (result.isEmpty() || result.get(0)[0] == null) return null;
        return result.get(0)[0].toString();
    }

    /**
     * Comprueba si algún freelance asignado al evento tiene decisión DUDOSO.
     * En ese caso la asignación no puede finalizarse (HU #34437).
     */
    public boolean tieneFreelanceDudosoAsignado(int idEvento) {
        String sql = "SELECT COUNT(*) FROM Asignacion a " +
                     "JOIN Reportero r ON a.id_reportero = r.id " +
                     "JOIN DecisionFreelance df ON df.id_reportero = r.id AND df.id_evento = a.id_evento " +
                     "WHERE a.id_evento = ? AND r.id_agencia IS NULL AND df.decision = 'DUDOSO'";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty() || result.get(0)[0] == null) return false;
        return ((Number) result.get(0)[0]).intValue() > 0;
    }
}