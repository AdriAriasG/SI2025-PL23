package app.model;

import java.util.ArrayList;
import java.util.List;

import app.dto.EventoDTO;
import app.dto.InformeEventoDTO;
import app.util.Database;

/**
 * Modelo para la generación de informes de eventos.
 * Cubre la HU #33548.
 */
public class InformeModel {
    private Database db = new Database();

    /**
     * Obtiene todos los eventos de una agencia
     */
    public List<EventoDTO> getEventosAgencia(int idAgencia) {
        String sql = "SELECT id, nombre, fecha, id_agencia FROM Evento " +
                     "WHERE id_agencia = ? ORDER BY fecha DESC, nombre";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene los nombres de los reporteros asignados a un evento
     */
    public List<String> getNombresReporterosAsignados(int idEvento) {
        String sql = "SELECT r.nombre FROM Reportero r " +
                     "JOIN Asignacion a ON r.id = a.id_reportero " +
                     "WHERE a.id_evento = ? ORDER BY r.nombre";
        List<Object[]> results = db.executeQueryArray(sql, idEvento);
        List<String> nombres = new ArrayList<>();
        for (Object[] row : results) {
            nombres.add((String) row[0]);
        }
        return nombres;
    }

    /**
     * Verifica si un evento tiene reportaje entregado
     */
    public boolean tieneReportaje(int idEvento) {
        String sql = "SELECT COUNT(*) FROM Reportaje WHERE id_evento = ?";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty() || result.get(0)[0] == null) {
            return false;
        }
        return ((Number) result.get(0)[0]).intValue() > 0;
    }

    /**
     * Obtiene el nombre del autor del reportaje de un evento
     */
    public String getNombreAutorReportaje(int idEvento) {
        String sql = "SELECT r.nombre FROM Reportero r " +
                     "JOIN Reportaje rep ON r.id = rep.id_reportero_autor " +
                     "WHERE rep.id_evento = ?";
        List<Object[]> result = db.executeQueryArray(sql, idEvento);
        if (result.isEmpty()) {
            return null;
        }
        return (String) result.get(0)[0];
    }

    /**
     * Obtiene los nombres de las empresas con acceso al reportaje de un evento
     */
    public List<String> getEmpresasConAcceso(int idEvento) {
        String sql = "SELECT ec.nombre FROM EmpresaComunicacion ec " +
                     "JOIN Ofrecimiento o ON ec.id = o.id_empresa " +
                     "WHERE o.id_evento = ? AND o.acceso_concedido = TRUE " +
                     "ORDER BY ec.nombre";
        List<Object[]> results = db.executeQueryArray(sql, idEvento);
        List<String> nombres = new ArrayList<>();
        for (Object[] row : results) {
            nombres.add((String) row[0]);
        }
        return nombres;
    }

    /**
     * Genera el informe completo de un evento
     */
    public InformeEventoDTO generarInforme(int idEvento, String nombreEvento, String fechaEvento) {
        InformeEventoDTO informe = new InformeEventoDTO();
        
        informe.setNombreEvento(nombreEvento);
        informe.setFechaEvento(fechaEvento);
        informe.setReporterosAsignados(getNombresReporterosAsignados(idEvento));
        informe.setTieneReportaje(tieneReportaje(idEvento));
        informe.setNombreAutor(getNombreAutorReportaje(idEvento));
        informe.setEmpresasConAcceso(getEmpresasConAcceso(idEvento));
        
        return informe;
    }
}