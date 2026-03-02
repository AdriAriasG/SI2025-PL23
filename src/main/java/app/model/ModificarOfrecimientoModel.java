package app.model;

import java.util.List;

import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;
import giis.demo.util.Database;

public class ModificarOfrecimientoModel {

    private Database db = new Database();

    public List<EmpresaComunicacionDTO> getEmpresasConOfrecimiento(int idEvento) {
        String sql = """
            SELECT e.id, e.nombre
            FROM EmpresaComunicacion e
            JOIN Ofrecimiento o ON e.id = o.id_empresa
            WHERE o.id_evento = ?
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
        """;
        return db.executeQueryPojo(EmpresaComunicacionDTO.class, sql, idEvento);
    }

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
    
    public List<EventoDTO> getEventosByAgencia(int idAgencia) {

        String sql = """
            SELECT id, nombre, fecha
            FROM Evento
            WHERE id_agencia = ?
        """;

        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }
    
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
    
    public boolean tieneAccesoConcedido(int idEvento, int idEmpresa) {

        String sql = """
            SELECT acceso_concedido
            FROM Ofrecimiento
            WHERE id_evento = ?
            AND id_empresa = ?
        """;

        List<Object[]> res =
            db.executeQueryArray(sql, idEvento, idEmpresa);

        if (res.isEmpty()) {
            return false;
        }

        Number value = (Number) res.get(0)[0];
        return value.longValue() == 1;
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

        List<Object[]> res =
            db.executeQueryArray(sql, idEvento, idEmpresa);

        if (res.isEmpty()) {
            return false;
        }

        Number count = (Number) res.get(0)[0];
        return count.longValue() > 0;
    }
    
}