package app.model;

import java.util.List;

import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
import giis.demo.util.Database;

public class ModificarOfrecimientoModel {

    private Database db = new Database();

    // ================================
    // CONSULTAS DE EMPRESAS
    // ================================

    public List<EmpresaComunicacionDTO> getEmpresasConOfrecimiento(int idEvento) {
        String sql = """
            SELECT e.id, e.nombre
            FROM EmpresaComunicacion e
            JOIN Ofrecimiento o ON e.id = o.id_empresa
            WHERE o.id_evento = ?
            ORDER BY e.nombre
        """;
        return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento);
    }

    public List<EmpresaComunicacionDTO> getEmpresasSinOfrecimiento(int idEvento) {
        String sql = """
            SELECT e.id, e.nombre
            FROM EmpresaComunicacion e
            WHERE e.id NOT IN (
                SELECT id_empresa
                FROM Ofrecimiento
                WHERE id_evento = ?
            )
            ORDER BY e.nombre
        """;
        return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento);
    }

    // ================================
    // OPERACIONES SOBRE OFRECIMIENTO
    // ================================

    public void ofrecerEmpresa(int idEvento, int idEmpresa) {

        String sql = """
            INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido)
            VALUES (
                (SELECT COALESCE(MAX(id),0)+1 FROM Ofrecimiento),
                ?, ?, 'PENDIENTE', 0
            )
        """;

        db.executeUpdate(sql, idEvento, idEmpresa);
    }

    public void quitarOfrecimiento(int idEvento, int idEmpresa) {
        String sql = """
            DELETE FROM Ofrecimiento
            WHERE id_evento = ? AND id_empresa = ?
        """;
        db.executeUpdate(sql, idEvento, idEmpresa);
    }

    // ================================
    // VALIDACIONES DE NEGOCIO
    // ================================

    public boolean tieneAccesoConcedido(int idEvento, int idEmpresa) {

        String sql = """
            SELECT COUNT(*)
            FROM Ofrecimiento
            WHERE id_evento = ?
              AND id_empresa = ?
              AND acceso_concedido = 1
        """;

        List<Object[]> res = db.executeQueryArray(sql, idEvento, idEmpresa);
        Number count = (Number) res.get(0)[0];
        return count.longValue() > 0;
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

        List<Object[]> res = db.executeQueryArray(sql, idEvento, idEmpresa);
        Number count = (Number) res.get(0)[0];
        return count.longValue() > 0;
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

        List<EmpresaComunicacionDTO> res =
            db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEmpresa);

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