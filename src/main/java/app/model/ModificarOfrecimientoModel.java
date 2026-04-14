package app.model;

import java.util.List;

import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
import app.util.ApplicationException;
import app.util.Database;

public class ModificarOfrecimientoModel {

    private final Database db = new Database();

    // ================================
    // CONSULTAS DE EMPRESAS
    // ================================

    public List<EmpresaComunicacionDTO> getEmpresasConOfrecimiento(int idEvento) {
        return getEmpresasConOfrecimiento(idEvento, false);
    }

    public List<EmpresaComunicacionDTO> getEmpresasConOfrecimiento(int idEvento, boolean soloTarifaPlana) {
        String sql = """
                SELECT e.id, e.nombre
                FROM EmpresaComunicacion e
                JOIN Ofrecimiento o ON e.id = o.id_empresa
                WHERE o.id_evento = ?
                  AND (? = 0 OR e.tiene_tarifa_plana = 1)
                ORDER BY e.nombre
                """;
        return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento, soloTarifaPlana);
    }

    public List<EmpresaComunicacionDTO> getEmpresasConOfrecimientoConTematicaCoincidente(int idEvento) {
        return getEmpresasConOfrecimientoConTematicaCoincidente(idEvento, false);
    }

    public List<EmpresaComunicacionDTO> getEmpresasConOfrecimientoConTematicaCoincidente(int idEvento,
            boolean soloTarifaPlana) {
        String sql = """
                SELECT DISTINCT e.id, e.nombre
                FROM EmpresaComunicacion e
                JOIN Ofrecimiento o ON e.id = o.id_empresa
                JOIN EmpresaTematica et ON et.id_empresa = e.id
                JOIN EventoTematica evt ON evt.id_tematica = et.id_tematica
                WHERE o.id_evento = ?
                  AND evt.id_evento = ?
                  AND (? = 0 OR e.tiene_tarifa_plana = 1)
                ORDER BY e.nombre
                """;
        return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento, idEvento, soloTarifaPlana);
    }

    public List<EmpresaComunicacionDTO> getEmpresasSinOfrecimiento(int idEvento) {
        return getEmpresasSinOfrecimiento(idEvento, false);
    }

    public List<EmpresaComunicacionDTO> getEmpresasSinOfrecimiento(int idEvento, boolean soloTarifaPlana) {
        String sql = """
            SELECT e.id, e.nombre
            FROM EmpresaComunicacion e
            WHERE e.id NOT IN (
                  SELECT id_empresa
                  FROM Ofrecimiento
                  WHERE id_evento = ?
              )
              AND (? = 0 OR e.tiene_tarifa_plana = 1)
              AND (
                    e.tiene_tarifa_plana = 0
                    OR (e.tiene_tarifa_plana = 1 AND e.al_corriente_pago = 1)
                  )
            ORDER BY e.nombre
        """;
        return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento, soloTarifaPlana);
    }

    public List<EmpresaComunicacionDTO> getEmpresasSinOfrecimientoConTematicaCoincidente(int idEvento) {
        return getEmpresasSinOfrecimientoConTematicaCoincidente(idEvento, false);
    }

    public List<EmpresaComunicacionDTO> getEmpresasSinOfrecimientoConTematicaCoincidente(int idEvento,
            boolean soloTarifaPlana) {
        String sql = """
                SELECT DISTINCT e.id, e.nombre
                FROM EmpresaComunicacion e
                JOIN EmpresaTematica et ON et.id_empresa = e.id
                JOIN EventoTematica evt ON evt.id_tematica = et.id_tematica
                JOIN Evento ev ON ev.id = ?
                WHERE evt.id_evento = ?
                  AND ev.asignacion_finalizada = 1
                  AND e.id NOT IN (
                      SELECT id_empresa
                      FROM Ofrecimiento
                      WHERE id_evento = ?
                  )
                  AND (? = 0 OR e.tiene_tarifa_plana = 1)
                  AND (
                      e.tiene_tarifa_plana = 0
                      OR (e.tiene_tarifa_plana = 1 AND e.al_corriente_pago = 1)
                  )
                ORDER BY e.nombre
                """;
        return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql,
                idEvento, idEvento, idEvento, soloTarifaPlana);
    }

    // ================================
    // OPERACIONES SOBRE OFRECIMIENTO
    // ================================

    public void ofrecerEmpresa(int idEvento, int idEmpresa) {
        validarPuedeOfrecer(idEvento, idEmpresa);

        String sql = """
                INSERT INTO Ofrecimiento (id_evento, id_empresa, estado, acceso_concedido)
                VALUES (?, ?, 'PENDIENTE', 0)
                """;
        db.executeUpdate(sql, idEvento, idEmpresa);
    }

    public boolean quitarOfrecimiento(int idEvento, int idEmpresa) {
        validarPuedeQuitar(idEvento, idEmpresa);

        boolean notificar = estabaAceptadoSinAcceso(idEvento, idEmpresa);

        String sql = """
                DELETE FROM Ofrecimiento
                WHERE id_evento = ? AND id_empresa = ?
                """;
        db.executeUpdate(sql, idEvento, idEmpresa);

        return notificar;
    }

    // ================================
    // VALIDACIONES DE NEGOCIO
    // ================================

    public void validarPuedeOfrecer(int idEvento, int idEmpresa) {
        if (!asignacionFinalizada(idEvento)) {
            throw new ApplicationException(
                    "No se puede ofrecer el reportaje.\nLa asignación de reporteros del evento no está finalizada.");
        }

        if (tieneTarifaPlana(idEmpresa) && !estaAlCorrientePago(idEmpresa)) {
            throw new ApplicationException(
                    "No se puede ofrecer el reportaje.\nLa empresa tiene tarifa plana y no está al corriente de pago.");
        }

        if (tieneEmbargoVigente(idEvento) && !aceptaEmbargo(idEmpresa)) {
            throw new ApplicationException(
                    "No se puede ofrecer el reportaje.\nLa empresa no acepta reportajes con fecha de embargo vigente.");
        }

        if (existeOfrecimiento(idEvento, idEmpresa)) {
            throw new ApplicationException("La empresa ya tiene un ofrecimiento para este evento.");
        }
    }

    public void validarPuedeQuitar(int idEvento, int idEmpresa) {
        if (!existeOfrecimiento(idEvento, idEmpresa)) {
            throw new ApplicationException("No existe un ofrecimiento para esa empresa en el evento seleccionado.");
        }

        if (tieneAccesoConcedido(idEvento, idEmpresa)) {
            throw new ApplicationException(
                    "No se puede quitar el ofrecimiento.\nLa empresa ya tiene acceso concedido al reportaje.");
        }
    }

    public boolean tieneAccesoConcedido(int idEvento, int idEmpresa) {
        String sql = """
                SELECT COUNT(*)
                FROM Ofrecimiento
                WHERE id_evento = ?
                  AND id_empresa = ?
                  AND acceso_concedido = 1
                """;
        Long count = db.executeQueryScalar(Long.class, sql, idEvento, idEmpresa);
        return count != null && count > 0;
    }

    public boolean estabaAceptadoSinAcceso(int idEvento, int idEmpresa) {
        String sql = """
                SELECT COUNT(*)
                FROM Ofrecimiento
                WHERE id_evento = ?
                  AND id_empresa = ?
                  AND estado = 'ACEPTADO'
                  AND acceso_concedido = 0
                """;
        Long count = db.executeQueryScalar(Long.class, sql, idEvento, idEmpresa);
        return count != null && count > 0;
    }

    public boolean tieneTarifaPlana(int idEmpresa) {
        String sql = """
                SELECT COUNT(*)
                FROM EmpresaComunicacion
                WHERE id = ?
                  AND tiene_tarifa_plana = 1
                """;
        Long count = db.executeQueryScalar(Long.class, sql, idEmpresa);
        return count != null && count > 0;
    }

    public boolean estaAlCorrientePago(int idEmpresa) {
        String sql = """
                SELECT COUNT(*)
                FROM EmpresaComunicacion
                WHERE id = ?
                  AND al_corriente_pago = 1
                """;
        Long count = db.executeQueryScalar(Long.class, sql, idEmpresa);
        return count != null && count > 0;
    }

    public boolean aceptaEmbargo(int idEmpresa) {
        String sql = """
                SELECT COUNT(*)
                FROM EmpresaComunicacion
                WHERE id = ?
                  AND acepta_embargo = 1
                """;
        Long count = db.executeQueryScalar(Long.class, sql, idEmpresa);
        return count != null && count > 0;
    }

    public boolean asignacionFinalizada(int idEvento) {
        String sql = """
                SELECT COUNT(*)
                FROM Evento
                WHERE id = ?
                  AND asignacion_finalizada = 1
                """;
        Long count = db.executeQueryScalar(Long.class, sql, idEvento);
        return count != null && count > 0;
    }

    public boolean empresaTieneTematicaCoincidente(int idEvento, int idEmpresa) {
        String sql = """
                SELECT COUNT(*)
                FROM EmpresaTematica et
                JOIN EventoTematica evt ON evt.id_tematica = et.id_tematica
                WHERE et.id_empresa = ?
                  AND evt.id_evento = ?
                """;
        Long count = db.executeQueryScalar(Long.class, sql, idEmpresa, idEvento);
        return count != null && count > 0;
    }

    public boolean tieneEmbargoVigente(int idEvento) {
        String sql = """
                SELECT COUNT(*)
                FROM Reportaje
                WHERE id_evento = ?
                  AND fecha_fin_embargo IS NOT NULL
                  AND DATE(fecha_fin_embargo) >= DATE('now')
                """;
        Long count = db.executeQueryScalar(Long.class, sql, idEvento);
        return count != null && count > 0;
    }

    public boolean puedeOfrecerSegunEmbargo(int idEvento, int idEmpresa) {
        return !tieneEmbargoVigente(idEvento) || aceptaEmbargo(idEmpresa);
    }

    private boolean existeOfrecimiento(int idEvento, int idEmpresa) {
        String sql = """
                SELECT COUNT(*)
                FROM Ofrecimiento
                WHERE id_evento = ?
                  AND id_empresa = ?
                """;
        Long count = db.executeQueryScalar(Long.class, sql, idEvento, idEmpresa);
        return count != null && count > 0;
    }

    // ================================
    // EVENTOS
    // ================================

    public List<EventoDTO> getEventosByAgencia(int idAgencia) {
        String sql = """
                SELECT id, nombre, fecha
                FROM Evento
                WHERE id_agencia = ?
                ORDER BY fecha
                """;
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    public String getNombreEvento(int idEvento) {
        String sql = "SELECT nombre FROM Evento WHERE id = ?";
        List<Object[]> res = db.executeQueryArray(sql, idEvento);
        return res.isEmpty() ? null : (String) res.get(0)[0];
    }

    // ================================
    // EMPRESAS
    // ================================

    public EmpresaComunicacionDTO getEmpresaById(int idEmpresa) {
        String sql = """
                SELECT id, nombre
                FROM EmpresaComunicacion
                WHERE id = ?
                """;
        List<EmpresaComunicacionDTO> res = db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEmpresa);
        return res.isEmpty() ? null : res.get(0);
    }

    public String getEmailEmpresa(int idEmpresa) {
        String sql = "SELECT email FROM EmpresaComunicacion WHERE id = ?";
        List<Object[]> res = db.executeQueryArray(sql, idEmpresa);
        return res.isEmpty() ? null : (String) res.get(0)[0];
    }

    // ================================
    // AGENCIA
    // ================================

    public String getEmailAgencia(int idAgencia) {
        String sql = "SELECT email FROM AgenciaPrensa WHERE id = ?";
        List<Object[]> res = db.executeQueryArray(sql, idAgencia);
        return res.isEmpty() ? null : (String) res.get(0)[0];
    }
}